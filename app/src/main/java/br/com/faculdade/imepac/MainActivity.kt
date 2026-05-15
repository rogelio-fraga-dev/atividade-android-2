package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                // Dashboard::class.java ainda não existe, mas será criada em breve.
                // Vou comentar por enquanto ou usar uma string para evitar erro de compilação
                // se o usuário tentar buildar agora. Mas o roadmap diz "Dashboard".
                try {
                    val intent = Intent(this, Class.forName("br.com.faculdade.imepac.Dashboard"))
                    startActivity(intent)
                } catch (e: ClassNotFoundException) {
                    // Fallback se Dashboard ainda não existir durante a transição
                    val intent = Intent(this, FormLogin::class.java)
                    startActivity(intent)
                }
            } else {
                val intent = Intent(this, FormLogin::class.java)
                startActivity(intent)
            }
            finish()
        }, 1500)
    }
}