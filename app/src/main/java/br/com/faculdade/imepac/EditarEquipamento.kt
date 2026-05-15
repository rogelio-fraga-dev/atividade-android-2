package br.com.faculdade.imepac

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.faculdade.imepac.model.Equipamento
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditarEquipamento : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var editNome: EditText
    private lateinit var editCodigo: EditText
    private lateinit var editSetor: EditText
    private lateinit var editCompra: EditText
    private lateinit var editProxima: EditText
    private lateinit var spinnerStatus: Spinner
    private val statusList = listOf("Funcionando", "Atenção", "Em Manutenção", "Parado")
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_editar_equipamento)
        db = FirebaseFirestore.getInstance()

        id = intent.getStringExtra("equipamento_id") ?: ""

        editNome = findViewById(R.id.edit_nome_eq)
        editCodigo = findViewById(R.id.edit_codigo_eq)
        editSetor = findViewById(R.id.edit_setor_eq)
        editCompra = findViewById(R.id.edit_data_compra)
        editProxima = findViewById(R.id.edit_proxima_manut)
        spinnerStatus = findViewById(R.id.spinner_status)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter

        // DatePickers
        editCompra.setOnClickListener { showDatePicker(editCompra) }
        editProxima.setOnClickListener { showDatePicker(editProxima) }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        findViewById<View>(R.id.btn_salvar_equipamento).setOnClickListener { atualizarEquipamento() }
        
        findViewById<View>(R.id.btn_excluir_eq).setOnClickListener { confirmarExclusao() }

        if (id.isNotEmpty()) carregarDados()
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(date)
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun carregarDados() {
        db.collection("Equipamentos").document(id).get()
            .addOnSuccessListener { doc ->
                val eq = doc.toObject(Equipamento::class.java)
                if (eq != null) {
                    editNome.setText(eq.nome)
                    editCodigo.setText(eq.codigo)
                    editSetor.setText(eq.setor)
                    editCompra.setText(eq.dataCompra)
                    editProxima.setText(eq.proximaManutencao)
                    
                    val pos = statusList.indexOf(eq.status)
                    if (pos >= 0) spinnerStatus.setSelection(pos)
                }
            }
    }

    private fun atualizarEquipamento() {
        val dados = hashMapOf(
            "nome" to editNome.text.toString().trim(),
            "codigo" to editCodigo.text.toString().trim(),
            "setor" to editSetor.text.toString().trim(),
            "status" to spinnerStatus.selectedItem.toString(),
            "dataCompra" to editCompra.text.toString().trim(),
            "proximaManutencao" to editProxima.text.toString().trim()
        )

        db.collection("Equipamentos").document(id)
            .update(dados as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Equipamento atualizado!", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun confirmarExclusao() {
        AlertDialog.Builder(this)
            .setTitle("Excluir Equipamento")
            .setMessage("Tem certeza que deseja remover este ativo?")
            .setPositiveButton("Sim") { _, _ ->
                db.collection("Equipamentos").document(id).delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Excluído com sucesso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Dashboard::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
            }
            .setNegativeButton("Não", null)
            .show()
    }
}
