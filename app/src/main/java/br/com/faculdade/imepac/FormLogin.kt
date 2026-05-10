package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FormLogin : AppCompatActivity() {

    private lateinit var edit_email: EditText
    private lateinit var edit_senha: EditText
    private lateinit var bt_entrada: Button
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)

        supportActionBar?.hide()

        iniciarComponentes()

        val linkFormCadastro = findViewById<TextView>(R.id.text_tela_cadastro)
        linkFormCadastro.setOnClickListener {
            // Refatoração para amarração explícita de contexto (Robustez)
            val intent = Intent(this@FormLogin, FormCadastro::class.java)
            startActivity(intent)
        }

        bt_entrada.setOnClickListener { view ->
            val email = edit_email.text.toString().trim()
            val senha = edit_senha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                val mensagemErro = "Campos não preenchidos, tente novamente"
                Snackbar.make(view, mensagemErro, Snackbar.LENGTH_LONG).show()
            } else {
                autenticarUsuario(view, email, senha)
            }
        }
    }

    private fun iniciarComponentes() {
        edit_email = findViewById(R.id.edit_email)
        edit_senha = findViewById(R.id.edit_senha)
        bt_entrada = findViewById(R.id.bt_entrada)
        progressbar = findViewById(R.id.progressbar)
    }

    private fun autenticarUsuario(view: View, email: String, senha: String) {
        progressbar.visibility = View.VISIBLE

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressbar.visibility = View.GONE
                    navegarParaTelaPerfil()
                } else {
                    progressbar.visibility = View.GONE
                    val mensagemErro = task.exception?.message ?: "Erro desconhecido"
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Erro ao autenticar usuário: $mensagemErro",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun navegarParaTelaPerfil() {
        val intent = Intent(this@FormLogin, TelaPerfil::class.java)
        startActivity(intent)
        finish()
    }
}