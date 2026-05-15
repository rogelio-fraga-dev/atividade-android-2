package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Equipamento
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FiltroEquipamentos : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: EquipamentoAdapter
    private val lista = mutableListOf<Equipamento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_filtro_equipamentos)
        db = FirebaseFirestore.getInstance()

        val rv = findViewById<RecyclerView>(R.id.rv_filtrado)
        adapter = EquipamentoAdapter(lista) { equipamento ->
            val intent = Intent(this, DetalhesEquipamento::class.java)
            intent.putExtra("equipamento_id", equipamento.id)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val chipGroup = findViewById<ChipGroup>(R.id.chip_group_filtro)
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val filtro = when (checkedId) {
                R.id.chip_funcionando -> "Funcionando"
                R.id.chip_atencao -> "Atenção"
                R.id.chip_manutencao -> "Em Manutenção"
                R.id.chip_parado -> "Parado"
                else -> "Todos"
            }
            carregarComFiltro(filtro)
        }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        carregarComFiltro("Todos")
    }

    private fun carregarComFiltro(filtro: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        var query: Query = db.collection("Equipamentos").whereEqualTo("uid", uid)

        if (filtro != "Todos") {
            query = query.whereEqualTo("status", filtro)
        }

        query.orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snap ->
                lista.clear()
                lista.addAll(snap.documents.map { d ->
                    d.toObject(Equipamento::class.java)!!.copy(id = d.id)
                })
                adapter.notifyDataSetChanged()
                val vazio = findViewById<View>(R.id.layout_vazio_filtro)
                vazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
            }
    }
}
