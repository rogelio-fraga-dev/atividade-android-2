package br.com.faculdade.imepac

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Equipamento
import br.com.faculdade.imepac.model.Manutencao
import com.google.firebase.firestore.FirebaseFirestore

class DetalhesEquipamento : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var equipamento: Equipamento
    private lateinit var adapter: ManutencaoAdapter
    private val historico = mutableListOf<Manutencao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detalhes_equipamento)
        db = FirebaseFirestore.getInstance()

        val id = intent.getStringExtra("equipamento_id") ?: ""

        val rv = findViewById<RecyclerView>(R.id.rv_historico_detalhe)
        adapter = ManutencaoAdapter(historico) { manutencao ->
            val intent = Intent(this, EditarManutencao::class.java)
            intent.putExtra("manutencao_id", manutencao.id)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        
        findViewById<View>(R.id.btn_ver_todas_manut).setOnClickListener {
            val intent = Intent(this, ListaManutencoes::class.java)
            intent.putExtra("equipamento_id", equipamento.id)
            intent.putExtra("equipamento_nome", equipamento.nome)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_nova_manut_detalhe).setOnClickListener {
            val intent = Intent(this, CadastroManutencao::class.java)
            intent.putExtra("equipamento_id", equipamento.id)
            intent.putExtra("equipamento_nome", equipamento.nome)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_editar_eq_detalhe).setOnClickListener {
            val intent = Intent(this, EditarEquipamento::class.java)
            intent.putExtra("equipamento_id", equipamento.id)
            startActivity(intent)
        }

        carregarDetalhes(id)
    }

    override fun onResume() {
        super.onResume()
        val id = intent.getStringExtra("equipamento_id") ?: ""
        if (id.isNotEmpty()) carregarDetalhes(id)
    }

    private fun carregarDetalhes(id: String) {
        db.collection("Equipamentos").document(id).get()
            .addOnSuccessListener { doc ->
                equipamento = doc.toObject(Equipamento::class.java)!!.copy(id = doc.id)
                preencherDados()
                carregarHistorico(id)
            }
    }

    private fun preencherDados() {
        findViewById<TextView>(R.id.txt_nome_detalhe).text = equipamento.nome
        findViewById<TextView>(R.id.txt_codigo_detalhe).text = "Patrimônio: ${equipamento.codigo}"
        findViewById<TextView>(R.id.txt_status_detalhe).text = equipamento.status.uppercase()
        findViewById<TextView>(R.id.txt_setor_detalhe).text = equipamento.setor
        findViewById<TextView>(R.id.txt_compra_detalhe).text = equipamento.dataCompra
        findViewById<TextView>(R.id.txt_proxima_detalhe).text = equipamento.proximaManutencao

        // Cores do Indicador
        val color = when (equipamento.status) {
            "Funcionando" -> R.color.status_funcionando
            "Atenção" -> R.color.status_atencao
            "Em Manutenção" -> R.color.status_manutencao
            else -> R.color.status_parado
        }
        findViewById<View>(R.id.view_status_indicador).backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, color))
        findViewById<TextView>(R.id.txt_status_detalhe).setTextColor(ContextCompat.getColor(this, color))
    }

    private fun carregarHistorico(id: String) {
        db.collection("Manutencoes")
            .whereEqualTo("equipamentoId", id)
            .limit(3)
            .get()
            .addOnSuccessListener { snap ->
                historico.clear()
                historico.addAll(snap.documents.map { it.toObject(Manutencao::class.java)!! })
                adapter.notifyDataSetChanged()
            }
    }
}
