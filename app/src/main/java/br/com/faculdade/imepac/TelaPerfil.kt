package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPerfil : AppCompatActivity() {
    private lateinit var mailUser: TextView
    private lateinit var nomeUser: TextView
    private lateinit var cargoUser: TextView
    private lateinit var empresaUser: TextView
    private lateinit var bt_sair: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_perfil)
        supportActionBar?.hide()
        
        db = FirebaseFirestore.getInstance()
        iniciarComponentes()

        bt_sair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, FormLogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        findViewById<android.view.View>(R.id.btn_seed_full).setOnClickListener {
            br.com.faculdade.imepac.utils.SeedData.seedDatabase { success ->
                if (success) {
                    android.widget.Toast.makeText(this, "Sistema populado com sucesso!", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    android.widget.Toast.makeText(this, "Erro ao popular sistema.", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<android.view.View>(R.id.ic_voltar).setOnClickListener { finish() }

        carregarDadosUsuario()
    }

    private fun carregarDadosUsuario() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("Usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    nomeUser.text = doc.getString("nome") ?: "Não informado"
                    mailUser.text = doc.getString("email") ?: "Não informado"
                    cargoUser.text = doc.getString("cargo") ?: "Não informado"
                    empresaUser.text = doc.getString("empresa") ?: "Não informado"
                }
            }
    }

    private fun iniciarComponentes() {
        mailUser = findViewById(R.id.textEmailUser)
        nomeUser = findViewById(R.id.textNomeUser)
        cargoUser = findViewById(R.id.textCargoUser)
        empresaUser = findViewById(R.id.textEmpresaUser)
        bt_sair = findViewById(R.id.bt_sair)
    }
}