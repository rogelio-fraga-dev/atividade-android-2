package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FormCadastro : AppCompatActivity() {

    private lateinit var edit_nome: EditText
    private lateinit var edit_email: EditText
    private lateinit var edit_senha: EditText
    private lateinit var edit_cargo: EditText
    private lateinit var edit_empresa: EditText
    private lateinit var btnCadastrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_form_cadastro)

        edit_nome = findViewById(R.id.edit_nome)
        edit_email = findViewById(R.id.edit_email_cadastro)
        edit_senha = findViewById(R.id.edit_senha_cadastro)
        edit_cargo = findViewById(R.id.edit_cargo)
        edit_empresa = findViewById(R.id.edit_empresa)
        btnCadastrar = findViewById(R.id.bt_cadastrar)

        findViewById<View>(R.id.bt_back).setOnClickListener { finish() }

        btnCadastrar.setOnClickListener { view ->
            val nome = edit_nome.text.toString().trim()
            val email = edit_email.text.toString().trim()
            val senha = edit_senha.text.toString().trim()
            val cargo = edit_cargo.text.toString().trim()
            val empresa = edit_empresa.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Snackbar.make(view, "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG).show()
            } else {
                cadastrarUsuario(view, email, senha, nome, cargo, empresa)
            }
        }
    }

    private fun cadastrarUsuario(view: View, email: String, senha: String, nome: String, cargo: String, empresa: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        salvarDadosUsuario(uid, nome, email, cargo, empresa)
                    }
                } else {
                    val mensagemErro = task.exception?.message ?: "Erro ao cadastrar usuário"
                    Snackbar.make(view, mensagemErro, Snackbar.LENGTH_LONG).show()
                }
            }
    }

    private fun salvarDadosUsuario(uid: String, nome: String, email: String, cargo: String, empresa: String) {
        val db = FirebaseFirestore.getInstance()
        val usuarios = hashMapOf(
            "nome" to nome,
            "email" to email,
            "uid" to uid,
            "cargo" to cargo,
            "empresa" to empresa
        )

        db.collection("Usuarios").document(uid)
            .set(usuarios)
            .addOnSuccessListener {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Dashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar dados: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}