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
    private lateinit var usuarioUser: TextView
    private lateinit var bt_sair: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_perfil)
        supportActionBar?.hide()
        
        iniciarComponentes()
        db = FirebaseFirestore.getInstance()

        bt_sair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@TelaPerfil, FormLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val email = FirebaseAuth.getInstance().currentUser?.email
        if (email != null) {
            mailUser.text = email
            buscarNomeDoEmail(email)
        }
    }

    private fun buscarNomeDoEmail(email: String) {
        val usuariosRef = db.collection("Usuarios")
        usuariosRef.whereEqualTo("email", email).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val nome = querySnapshot.documents[0].getString("nome")
                    usuarioUser.text = nome
                }
            }
    }

    private fun iniciarComponentes() {
        mailUser = findViewById(R.id.textEmailUser)
        usuarioUser = findViewById(R.id.textNomeUser)
        bt_sair = findViewById(R.id.bt_sair)
    }
}