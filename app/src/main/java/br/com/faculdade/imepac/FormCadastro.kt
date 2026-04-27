package br.com.faculdade.imepac

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FormCadastro : AppCompatActivity() {

    // Modificador lateinit para inicialização posterior [cite: 99, 100]
    private lateinit var edit_nome: EditText
    private lateinit var edit_email: EditText
    private lateinit var edit_senha: EditText
    private lateinit var bt_cadastrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cadastro)

        // Oculta a Toolbar para design limpo [cite: 120]
        supportActionBar?.hide()

        iniciarComponentes()

        // Configuração do evento de clique no botão [cite: 134, 136]
        bt_cadastrar.setOnClickListener { view ->
            val nome = edit_nome.text.toString().trim() // [cite: 138, 140]
            val email = edit_email.text.toString().trim() // [cite: 141]
            val senha = edit_senha.text.toString().trim() // [cite: 141]

            // Validação de campos vazios [cite: 161, 170]
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                cadastrarUsuario(view, email, senha) // [cite: 164, 165]
            }
        }
    }

    private fun iniciarComponentes() {
        // Vinculação dos componentes da interface com as variáveis [cite: 121, 132]
        edit_nome = findViewById(R.id.edit_nome) // [cite: 123, 133]
        edit_email = findViewById(R.id.edit_email_cadastro) // [cite: 124]
        edit_senha = findViewById(R.id.edit_senha_cadastro) // [cite: 125]
        bt_cadastrar = findViewById(R.id.bt_cadastrar) // [cite: 126]
    }

    private fun cadastrarUsuario(view: View, email: String, senha: String) {
        // Comunicação com o Firebase Authentication [cite: 185, 186]
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task -> // [cite: 171, 172]
                if (task.isSuccessful) {
                    // Feedback de sucesso [cite: 171, 173]
                    val snackbar = Snackbar.make(view, "Sucesso ao cadastrar usuário!", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.BLUE)
                    snackbar.show()
                } else {
                    // Feedback de erro [cite: 171, 173]
                    val erro = "Erro ao cadastrar usuário!"
                    val snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }
            }
    }
}