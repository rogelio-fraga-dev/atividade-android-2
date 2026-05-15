package br.com.faculdade.imepac

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.faculdade.imepac.model.Manutencao
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditarManutencao : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var id: String
    private lateinit var editData: EditText
    private lateinit var editCusto: EditText
    private lateinit var editDescricao: EditText
    private lateinit var txtEquipamento: TextView
    private lateinit var toggleStatus: MaterialButtonToggleGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_editar_manutencao)
        db = FirebaseFirestore.getInstance()

        id = intent.getStringExtra("manutencao_id") ?: ""

        editData = findViewById(R.id.edit_data_manut)
        editCusto = findViewById(R.id.edit_custo)
        editDescricao = findViewById(R.id.edit_descricao)
        txtEquipamento = findViewById(R.id.txt_nome_equipamento_edit)
        toggleStatus = findViewById(R.id.toggle_status_manut)

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        editData.setOnClickListener { mostrarDatePicker() }

        findViewById<View>(R.id.btn_atualizar_manutencao).setOnClickListener {
            atualizarManutencao()
        }

        findViewById<View>(R.id.btn_excluir_manut).setOnClickListener {
            excluirManutencao()
        }

        carregarDados()
    }

    private fun carregarDados() {
        if (id.isEmpty()) return
        db.collection("Manutencoes").document(id).get()
            .addOnSuccessListener { doc ->
                val m = doc.toObject(Manutencao::class.java) ?: return@addOnSuccessListener
                txtEquipamento.text = m.equipamentoNome
                editData.setText(m.data)
                editCusto.setText(m.custo.toString())
                editDescricao.setText(m.descricao)
                
                if (m.statusManutencao == "Realizada") {
                    toggleStatus.check(R.id.btn_status_realizada)
                } else {
                    toggleStatus.check(R.id.btn_status_agendada)
                }
            }
    }

    private fun mostrarDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            val dataStr = String.format("%02d/%02d/%d", d, m + 1, y)
            editData.setText(dataStr)
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun atualizarManutencao() {
        val data = editData.text.toString()
        val custo = editCusto.text.toString().toDoubleOrNull() ?: 0.0
        val desc = editDescricao.text.toString()
        val status = if (toggleStatus.checkedButtonId == R.id.btn_status_realizada) "Realizada" else "Agendada"

        if (data.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = mapOf(
            "data" to data,
            "custo" to custo,
            "descricao" to desc,
            "statusManutencao" to status
        )

        db.collection("Manutencoes").document(id).update(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Registro atualizado!", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun excluirManutencao() {
        db.collection("Manutencoes").document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Registro excluído!", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}
