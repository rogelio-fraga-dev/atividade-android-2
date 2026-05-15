package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Manutencao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ListaManutencoes : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ManutencaoAdapter
    private val lista = mutableListOf<Manutencao>()
    private var equipamentoId: String? = null
    private var equipamentoNome: String? = null
    private var lastVisible: DocumentSnapshot? = null
    private val PAGE_SIZE = 10L
    private var isLoading = false
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_lista_manutencoes)
        db = FirebaseFirestore.getInstance()

        equipamentoId   = intent.getStringExtra("equipamento_id")
        equipamentoNome = intent.getStringExtra("equipamento_nome")

        // Ajustar título da toolbar
        if (equipamentoNome != null) {
            findViewById<TextView>(R.id.txt_toolbar_manutencoes).text = "Manutenções de $equipamentoNome"
        }

        val rv = findViewById<RecyclerView>(R.id.rv_manutencoes)
        adapter = ManutencaoAdapter(lista) { /* click reservado para detalhes futuro */ }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // Scroll paginação
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                if (!isLoading && hasMore && lm.findLastCompletelyVisibleItemPosition() >= lista.size - 3) {
                    carregarManutencoes(paginar = true)
                }
            }
        })

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        findViewById<View>(R.id.fab_nova_manutencao).setOnClickListener {
            val intent = Intent(this, CadastroManutencao::class.java)
            equipamentoId?.let { intent.putExtra("equipamento_id", it) }
            equipamentoNome?.let { intent.putExtra("equipamento_nome", it) }
            startActivity(intent)
        }

        carregarManutencoes(paginar = false)
    }

    override fun onResume() {
        super.onResume()
        lista.clear(); lastVisible = null; hasMore = true
        carregarManutencoes(paginar = false)
    }

    private fun carregarManutencoes(paginar: Boolean) {
        if (isLoading || !hasMore) return
        isLoading = true
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        var query: Query = db.collection("Manutencoes")
            .whereEqualTo("uid", uid)

        // Filtrar por equipamento se veio de DetalhesEquipamento
        if (equipamentoId != null) {
            query = query.whereEqualTo("equipamentoId", equipamentoId)
        }

        query = query.orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(PAGE_SIZE)

        if (paginar && lastVisible != null) query = query.startAfter(lastVisible!!)

        query.get().addOnSuccessListener { snap ->
            isLoading = false
            if (snap.isEmpty) { 
                hasMore = false
                if (!paginar && lista.isEmpty()) {
                    findViewById<View>(R.id.layout_vazio_manut).visibility = View.VISIBLE
                }
                return@addOnSuccessListener 
            }
            
            findViewById<View>(R.id.layout_vazio_manut).visibility = View.GONE
            lastVisible = snap.documents.last()
            hasMore = snap.size() >= PAGE_SIZE.toInt()
            val novos = snap.documents.map { d -> d.toObject(Manutencao::class.java)!!.copy(id = d.id) }
            if (!paginar) lista.clear()
            lista.addAll(novos)
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { isLoading = false }
    }
}
