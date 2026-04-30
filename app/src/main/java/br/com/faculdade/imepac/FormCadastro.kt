package br.com.faculdade.imepac

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FormCadastro : AppCompatActivity() {

    private lateinit var edit_nome: EditText
    private lateinit var edit_email: EditText
    private lateinit var edit_senha: EditText
    private lateinit var btnCadastrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cadastro)

        supportActionBar?.hide()

        // Correção Crítica: Os IDs aqui mapeiam estritamente a nomenclatura do seu XML
        edit_nome = findViewById(R.id.edit_nome)
        edit_email = findViewById(R.id.edit_email_cadastro) // Corrigido para refletir o XML
        edit_senha = findViewById(R.id.edit_senha_cadastro) // Corrigido para refletir o XML
        btnCadastrar = findViewById(R.id.bt_cadastrar)

        btnCadastrar.setOnClickListener { view ->
            val nome = edit_nome.text.toString().trim()
            val email = edit_email.text.toString().trim()
            val senha = edit_senha.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                val mensagemErro = "Campos não preenchidos, tente novamente"
                Snackbar.make(view, mensagemErro, Snackbar.LENGTH_LONG).show()
            } else {
                cadastrarUsuario(view, email, senha)
            }
        }
    }

    private fun cadastrarUsuario(view: View, email: String, senha: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    salvarDadosUsuario()
                    val mensagemOk = "Cadastro realizado com sucesso"
                    Snackbar.make(view, mensagemOk, Snackbar.LENGTH_LONG).show()
                } else {
                    val mensagemErro = "Erro ao cadastrar usuário"
                    Snackbar.make(view, mensagemErro, Snackbar.LENGTH_LONG).show()
                }
            }
    }

    private fun salvarDadosUsuario() {
        val db = FirebaseFirestore.getInstance()
        val nome = edit_nome.text.toString().trim()
        val usuarioID = FirebaseAuth.getInstance().currentUser?.uid
        val email = FirebaseAuth.getInstance().currentUser?.email

        if (usuarioID != null && email != null) {
            val usuarios = hashMapOf(
                "nome" to nome,
                "email" to email,
                "uid" to usuarioID
            )

            db.collection("Usuarios")
                .add(usuarios)
                .addOnSuccessListener { documentReference ->
                    println("Documento adicionado com ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Erro ao adicionar documento: $e")
                }
        } else {
            println("Erro na autenticação")
        }
    }
}