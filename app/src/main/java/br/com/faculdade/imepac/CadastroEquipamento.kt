package br.com.faculdade.imepac

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CadastroEquipamento : AppCompatActivity() {

    private val statusList = listOf("Funcionando", "Atenção", "Em Manutenção", "Parado")
    private lateinit var editDataCompra: EditText
    private lateinit var editProximaManut: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cadastro_equipamento)

        editDataCompra = findViewById(R.id.edit_data_compra)
        editProximaManut = findViewById(R.id.edit_proxima_manut)

        val spinnerStatus = findViewById<Spinner>(R.id.spinner_status)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = spinnerAdapter

        // Configurar DatePickers
        editDataCompra.setOnClickListener { showDatePicker(editDataCompra) }
        editProximaManut.setOnClickListener { showDatePicker(editProximaManut) }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btn_salvar_equipamento).setOnClickListener { salvarEquipamento() }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(date)
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun salvarEquipamento() {
        val nome = findViewById<EditText>(R.id.edit_nome_eq).text.toString().trim()
        val codigo = findViewById<EditText>(R.id.edit_codigo_eq).text.toString().trim()
        val setor = findViewById<EditText>(R.id.edit_setor_eq).text.toString().trim()
        val dataCompra = editDataCompra.text.toString().trim()
        val proximaMaint = editProximaManut.text.toString().trim()
        val status = findViewById<Spinner>(R.id.spinner_status).selectedItem.toString()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        if (nome.isEmpty() || codigo.isEmpty() || setor.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG).show()
            return
        }

        val equipamento = hashMapOf(
            "uid" to uid,
            "nome" to nome,
            "codigo" to codigo,
            "setor" to setor,
            "dataCompra" to dataCompra,
            "status" to status,
            "proximaManutencao" to proximaMaint,
            // Usando timestamp local para garantir exibição imediata na lista ordenada
            "createdAt" to com.google.firebase.Timestamp.now()
        )

        FirebaseFirestore.getInstance().collection("Equipamentos")
            .add(equipamento)
            .addOnSuccessListener {
                Toast.makeText(this, "Equipamento cadastrado!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(findViewById(android.R.id.content), "Erro ao salvar", Snackbar.LENGTH_LONG).show()
            }
    }
}
