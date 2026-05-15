package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Dashboard : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var txtContFuncionando: TextView
    private lateinit var txtContManutencao: TextView
    private lateinit var txtContAtencao: TextView
    private lateinit var txtContParado: TextView
    private lateinit var txtSaudacao: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_dashboard)
        db = FirebaseFirestore.getInstance()

        // Mapear views
        txtContFuncionando = findViewById(R.id.txt_count_funcionando)
        txtContManutencao  = findViewById(R.id.txt_count_manutencao)
        txtContAtencao     = findViewById(R.id.txt_count_atencao)
        txtContParado      = findViewById(R.id.txt_count_parado)
        txtSaudacao        = findViewById(R.id.txt_saudacao)

        carregarNomeUsuario()

        // KPI Clicks (Filtering)
        findViewById<View>(R.id.card_kpi_funcionando).setOnClickListener {
            val intent = Intent(this, ListaEquipamentos::class.java)
            intent.putExtra("filtro_status", "Funcionando")
            startActivity(intent)
        }
        findViewById<View>(R.id.card_kpi_manutencao).setOnClickListener {
            val intent = Intent(this, ListaEquipamentos::class.java)
            intent.putExtra("filtro_status", "Em Manutenção")
            startActivity(intent)
        }
        findViewById<View>(R.id.card_kpi_atencao).setOnClickListener {
            val intent = Intent(this, ListaEquipamentos::class.java)
            intent.putExtra("filtro_status", "Atenção")
            startActivity(intent)
        }
        findViewById<View>(R.id.card_kpi_parado).setOnClickListener {
            val intent = Intent(this, ListaEquipamentos::class.java)
            intent.putExtra("filtro_status", "Parado")
            startActivity(intent)
        }

        // Navegação por Cards
        findViewById<View>(R.id.btn_ir_equipamentos_card).setOnClickListener {
            startActivity(Intent(this, ListaEquipamentos::class.java))
        }
        findViewById<View>(R.id.btn_ir_manutencoes_card).setOnClickListener {
            startActivity(Intent(this, ListaManutencoes::class.java))
        }
        findViewById<View>(R.id.btn_ir_relatorios_card).setOnClickListener {
            startActivity(Intent(this, RelatoriosActivity::class.java))
        }
        findViewById<View>(R.id.btn_ir_agenda_card).setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }

        findViewById<View>(R.id.btn_ir_tutoriais).setOnClickListener {
            startActivity(Intent(this, TutoriaisActivity::class.java))
        }
        findViewById<View>(R.id.btn_ir_estoque).setOnClickListener {
            startActivity(Intent(this, EstoqueActivity::class.java))
        }
        findViewById<View>(R.id.btn_ir_setores).setOnClickListener {
            startActivity(Intent(this, SetoresActivity::class.java))
        }

        // Toolbar Actions
        findViewById<View>(R.id.card_profile_thumb).setOnClickListener {
            startActivity(Intent(this, TelaPerfil::class.java))
        }
        
        findViewById<View>(R.id.bt_logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, FormLogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarContagensStatus()
    }

    private fun carregarNomeUsuario() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("Usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                val nome = doc.getString("nome") ?: "Usuário"
                txtSaudacao.text = "Olá, $nome"
            }
    }

    private fun carregarContagensStatus() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("Equipamentos")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                var func = 0; var manu = 0; var aten = 0; var para = 0
                for (doc in snap) {
                    when (doc.getString("status")) {
                        "Funcionando" -> func++
                        "Em Manutenção" -> manu++
                        "Atenção" -> aten++
                        "Parado" -> para++
                    }
                }
                txtContFuncionando.text = func.toString()
                txtContManutencao.text = manu.toString()
                txtContAtencao.text = aten.toString()
                txtContParado.text = para.toString()
            }
    }
}
