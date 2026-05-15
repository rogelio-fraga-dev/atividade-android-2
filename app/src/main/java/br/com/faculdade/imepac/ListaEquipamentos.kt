package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Equipamento
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ListaEquipamentos : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutVazio: LinearLayout
    private lateinit var adapter: EquipamentoAdapter
    private val listaCompleta = mutableListOf<Equipamento>()

    private var lastVisible: DocumentSnapshot? = null
    private val PAGE_SIZE = 20L
    private var isLoading = false
    private var hasMore = true
    private var filtroStatus: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_lista_equipamentos)
        db = FirebaseFirestore.getInstance()

        filtroStatus = intent.getStringExtra("filtro_status")

        recyclerView = findViewById(R.id.rv_equipamentos)
        layoutVazio  = findViewById(R.id.layout_vazio)

        adapter = EquipamentoAdapter(listaCompleta) { equipamento ->
            val intent = Intent(this, DetalhesEquipamento::class.java)
            intent.putExtra("equipamento_id", equipamento.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                if (!isLoading && hasMore &&
                    lm.findLastCompletelyVisibleItemPosition() >= listaCompleta.size - 3) {
                    carregarEquipamentos(paginar = true)
                }
            }
        })

        findViewById<View>(R.id.fab_novo_equipamento).setOnClickListener {
            startActivity(Intent(this, CadastroEquipamento::class.java))
        }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        findViewById<EditText>(R.id.edit_busca).addTextChangedListener { editable ->
            val queryText = editable.toString().lowercase()
            val filtrada = listaCompleta.filter {
                (it.nome.lowercase().contains(queryText) ||
                it.codigo.lowercase().contains(queryText) ||
                it.setor.lowercase().contains(queryText))
            }
            adapter.atualizarLista(filtrada)
            layoutVazio.visibility = if (filtrada.isEmpty()) View.VISIBLE else View.GONE
        }

        carregarEquipamentos(paginar = false)
    }

    private fun carregarEquipamentos(paginar: Boolean) {
        if (isLoading || !hasMore) return
        isLoading = true
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        var query = db.collection("Equipamentos")
            .whereEqualTo("uid", uid)

        // Aplicar filtro de status se vier do Dashboard
        filtroStatus?.let {
            query = query.whereEqualTo("status", it)
        }

        query = query.orderBy("createdAt", Query.Direction.DESCENDING) 
            .limit(PAGE_SIZE)

        if (paginar && lastVisible != null) {
            query = query.startAfter(lastVisible!!)
        }

        query.get().addOnSuccessListener { snapshots ->
            isLoading = false
            if (snapshots.isEmpty) {
                hasMore = false
                if (!paginar && listaCompleta.isEmpty()) layoutVazio.visibility = View.VISIBLE
                return@addOnSuccessListener
            }

            layoutVazio.visibility = View.GONE
            lastVisible = snapshots.documents.last()
            hasMore = snapshots.size() >= PAGE_SIZE.toInt()

            val novos = snapshots.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Equipamento::class.java)!!.copy(id = doc.id)
                } catch (e: Exception) { null }
            }
            
            if (!paginar) listaCompleta.clear()
            listaCompleta.addAll(novos)
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { e ->
            isLoading = false
            if (e.message?.contains("index") == true) {
                carregarSemOrdenacao()
            }
        }
    }

    private fun carregarSemOrdenacao() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        var query = db.collection("Equipamentos").whereEqualTo("uid", uid)
        
        filtroStatus?.let {
            query = query.whereEqualTo("status", it)
        }

        query.limit(50).get()
            .addOnSuccessListener { snapshots ->
                listaCompleta.clear()
                snapshots.documents.forEach { doc ->
                    doc.toObject(Equipamento::class.java)?.let {
                        listaCompleta.add(it.copy(id = doc.id))
                    }
                }
                adapter.notifyDataSetChanged()
                layoutVazio.visibility = if (listaCompleta.isEmpty()) View.VISIBLE else View.GONE
            }
    }
}
