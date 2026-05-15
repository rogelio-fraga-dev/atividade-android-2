package br.com.faculdade.imepac

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import br.com.faculdade.imepac.model.Equipamento
import java.util.*

class CadastroManutencao : AppCompatActivity() {

    private lateinit var editDataManut: EditText
    private lateinit var toggleStatus: MaterialButtonToggleGroup
    private lateinit var autoEquipamentos: AutoCompleteTextView
    private var listaEquipamentos = mutableListOf<Equipamento>()
    private var selecionadoId = ""
    private var selecionadoNome = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cadastro_manutencao)

        val equipIdFromIntent   = intent.getStringExtra("equipamento_id") ?: ""
        val equipNomeFromIntent = intent.getStringExtra("equipamento_nome") ?: ""

        editDataManut = findViewById(R.id.edit_data_manut)
        toggleStatus = findViewById(R.id.toggle_status_manut)
        autoEquipamentos = findViewById(R.id.auto_equipamentos)
        
        editDataManut.setOnClickListener { showDatePicker() }

        if (equipIdFromIntent.isNotEmpty()) {
            selecionadoId = equipIdFromIntent
            selecionadoNome = equipNomeFromIntent
            autoEquipamentos.setText(equipNomeFromIntent)
            findViewById<View>(R.id.layout_escolher_equipamento).isEnabled = false
        } else {
            carregarEquipamentosParaDropdown()
        }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btn_salvar_manutencao).setOnClickListener {
            salvarManutencao(selecionadoId, selecionadoNome)
        }
    }

    private fun carregarEquipamentosParaDropdown() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("Equipamentos")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                listaEquipamentos = snap.documents.mapNotNull { doc ->
                    doc.toObject(Equipamento::class.java)?.copy(id = doc.id)
                }.toMutableList()

                val nomes = listaEquipamentos.map { it.nome }
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nomes)
                autoEquipamentos.setAdapter(adapter)

                autoEquipamentos.setOnItemClickListener { _, _, position, _ ->
                    val eq = listaEquipamentos[position]
                    selecionadoId = eq.id
                    selecionadoNome = eq.nome
                }
            }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            editDataManut.setText(date)
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun salvarManutencao(equipId: String, equipNome: String) {
        // Capturar status do ToggleGroup
        val statusManut = if (toggleStatus.checkedButtonId == R.id.btn_status_realizada) "Realizada" else "Agendada"
        
        val data        = editDataManut.text.toString().trim()
        val custoStr    = findViewById<EditText>(R.id.edit_custo).text.toString().trim()
        val descricao   = findViewById<EditText>(R.id.edit_descricao).text.toString().trim()
        val uid         = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Técnico padrão (pode ser expandido futuramente)
        val responsavel = "Técnico Responsável"

        if (data.isEmpty() || descricao.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Preencha a data e a descrição", Snackbar.LENGTH_LONG).show()
            return
        }

        val custo = custoStr.replace(",", ".").toDoubleOrNull() ?: 0.0

        val manutencao = hashMapOf(
            "uid" to uid,
            "equipamentoId" to equipId,
            "equipamentoNome" to equipNome,
            "tipo" to "Manutenção", // Simplificado para o Toggle
            "descricao" to descricao,
            "data" to data,
            "custo" to custo,
            "responsavel" to responsavel,
            "statusManutencao" to statusManut,
            "createdAt" to com.google.firebase.Timestamp.now()
        )

        FirebaseFirestore.getInstance().collection("Manutencoes")
            .add(manutencao)
            .addOnSuccessListener {
                Toast.makeText(this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(findViewById(android.R.id.content), "Erro ao salvar no Firebase", Snackbar.LENGTH_LONG).show()
            }
    }
}
