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
    private var PAGE_SIZE = 5L
    private var isLoading = false
    private var hasMore = true
    
    private var pageStack = mutableListOf<DocumentSnapshot?>()
    private var currentPage = 1
    private var currentFirstDoc: DocumentSnapshot? = null

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
        adapter = ManutencaoAdapter(lista) { manutencao ->
            val intent = Intent(this, EditarManutencao::class.java)
            intent.putExtra("manutencao_id", manutencao.id)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // Scroll paginação
        findViewById<View>(R.id.btn_proxima_manut).setOnClickListener {
            if (hasMore) {
                pageStack.add(currentFirstDoc)
                currentPage++
                carregarManutencoes(paginar = true, forward = true)
            }
        }

        findViewById<View>(R.id.btn_anterior_manut).setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                val prev = if (pageStack.isNotEmpty()) pageStack.removeAt(pageStack.size - 1) else null
                carregarManutencoes(paginar = true, forward = false, startAtDoc = prev)
            }
        }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        findViewById<View>(R.id.fab_nova_manutencao).setOnClickListener {
            val intent = Intent(this, CadastroManutencao::class.java)
            equipamentoId?.let { intent.putExtra("equipamento_id", it) }
            equipamentoNome?.let { intent.putExtra("equipamento_nome", it) }
            startActivity(intent)
        }

        // Botão de filtro (listagem/ordenar)
        findViewById<View>(R.id.ic_filtro_toolbar_manut).setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(this, view)
            popup.menu.add("Mostrar 5 registros")
            popup.menu.add("Mostrar 10 registros")
            popup.menu.add("Mostrar 20 registros")
            popup.setOnMenuItemClickListener { item ->
                PAGE_SIZE = when(item.title) {
                    "Mostrar 5 registros" -> 5L
                    "Mostrar 10 registros" -> 10L
                    "Mostrar 20 registros" -> 20L
                    else -> 5L
                }
                // Reiniciar paginação completa
                lista.clear()
                lastVisible = null
                currentFirstDoc = null
                pageStack.clear()
                currentPage = 1
                hasMore = true
                carregarManutencoes(paginar = false)
                true
            }
            popup.show()
        }

        carregarManutencoes(paginar = false)
    }

    override fun onResume() {
        super.onResume()
        // Ao voltar para a tela, mantemos a configuração de PAGE_SIZE mas limpamos o estado da lista
        lista.clear()
        lastVisible = null
        currentFirstDoc = null
        pageStack.clear()
        currentPage = 1
        hasMore = true
        carregarManutencoes(paginar = false)
    }

    private fun carregarManutencoes(paginar: Boolean, forward: Boolean = true, startAtDoc: DocumentSnapshot? = null) {
        if (isLoading) return
        isLoading = true
        val progress = findViewById<View>(R.id.progress_manut)
        progress.visibility = View.VISIBLE
        
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        var query: Query = db.collection("Manutencoes")
            .whereEqualTo("uid", uid)

        if (equipamentoId != null) {
            query = query.whereEqualTo("equipamentoId", equipamentoId)
        }

        if (paginar) {
            if (forward && lastVisible != null) {
                query = query.startAfter(lastVisible!!)
            } else if (!forward && startAtDoc != null) {
                query = query.startAt(startAtDoc)
            }
        }

        query = query.limit(PAGE_SIZE)

        query.get().addOnSuccessListener { snap ->
            isLoading = false
            progress.visibility = View.GONE
            
            if (snap.isEmpty) { 
                hasMore = false
                if (currentPage == 1) {
                    findViewById<View>(R.id.layout_vazio_manut).visibility = View.VISIBLE
                    lista.clear()
                    adapter.notifyDataSetChanged()
                }
                updateUI()
                return@addOnSuccessListener 
            }
            
            findViewById<View>(R.id.layout_vazio_manut).visibility = View.GONE
            currentFirstDoc = snap.documents.first()
            lastVisible = snap.documents.last()
            
            hasMore = snap.size() >= PAGE_SIZE.toInt()
            val novos = snap.documents.mapNotNull { d -> d.toObject(Manutencao::class.java)?.copy(id = d.id) }
            
            lista.clear()
            lista.addAll(novos)
            adapter.notifyDataSetChanged()
            updateUI()

        }.addOnFailureListener { 
            isLoading = false
            progress.visibility = View.GONE
            carregarSemOrdenacao()
        }
    }

    private fun updateUI() {
        findViewById<TextView>(R.id.txt_pagina_manut).text = "Página $currentPage"
        findViewById<View>(R.id.btn_anterior_manut).isEnabled = currentPage > 1
        findViewById<View>(R.id.btn_proxima_manut).isEnabled = hasMore
    }

    private fun carregarSemOrdenacao() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        var query: Query = db.collection("Manutencoes").whereEqualTo("uid", uid)
        
        if (equipamentoId != null) {
            query = query.whereEqualTo("equipamentoId", equipamentoId)
        }

        query.limit(50).get().addOnSuccessListener { snap ->
            if (snap.isEmpty && lista.isEmpty()) {
                findViewById<View>(R.id.layout_vazio_manut).visibility = View.VISIBLE
                return@addOnSuccessListener
            }
            findViewById<View>(R.id.layout_vazio_manut).visibility = View.GONE
            val novos = snap.documents.map { d -> d.toObject(Manutencao::class.java)!!.copy(id = d.id) }
            lista.clear()
            lista.addAll(novos)
            adapter.notifyDataSetChanged()
        }
    }
}
