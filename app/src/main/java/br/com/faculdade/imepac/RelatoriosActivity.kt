package br.com.faculdade.imepac

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.*

class RelatoriosActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_relatorios)
        db = FirebaseFirestore.getInstance()

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        
        findViewById<View>(R.id.btn_seed_data).setOnClickListener {
            br.com.faculdade.imepac.utils.SeedData.seedDatabase { success ->
                if (success) {
                    android.widget.Toast.makeText(this, "Banco populado com sucesso!", android.widget.Toast.LENGTH_SHORT).show()
                    recreate() // Recarregar dados
                } else {
                    android.widget.Toast.makeText(this, "Erro ao gerar dados.", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (uid != null) {
            carregarDadosFinanceiros()
            carregarDistribuicaoTipos()
            carregarStatusFrota()
        }
    }

    private fun carregarStatusFrota() {
        val container = findViewById<android.widget.LinearLayout>(R.id.container_stats_list)
        container.removeAllViews()

        db.collection("Equipamentos")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                val counts = mutableMapOf<String, Int>()
                for (doc in snap) {
                    val status = doc.getString("status") ?: "Outros"
                    counts[status] = counts.getOrDefault(status, 0) + 1
                }

                counts.forEach { (status, count) ->
                    val textView = TextView(this)
                    textView.text = "$status: $count"
                    textView.setPadding(0, 8, 0, 8)
                    textView.setTextColor(resources.getColor(R.color.text_primary, null))
                    textView.textSize = 16f
                    container.addView(textView)
                }
            }
    }

    private fun carregarDadosFinanceiros() {
        db.collection("Manutencoes")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                var total = 0.0
                for (doc in snap) {
                    total += doc.getDouble("custo") ?: 0.0
                }
                val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                findViewById<TextView>(R.id.txt_custo_total).text = format.format(total)
            }
    }

    private fun carregarDistribuicaoTipos() {
        db.collection("Manutencoes")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                var preventiva = 0
                var corretiva = 0
                for (doc in snap) {
                    when (doc.getString("tipo")) {
                        "Preventiva" -> preventiva++
                        "Corretiva" -> corretiva++
                    }
                }
                findViewById<TextView>(R.id.txt_count_preventiva).text = preventiva.toString()
                findViewById<TextView>(R.id.txt_count_corretiva).text = corretiva.toString()
            }
    }
}
