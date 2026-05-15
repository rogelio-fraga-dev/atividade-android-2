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

class AgendaActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ManutencaoAdapter
    private val lista = mutableListOf<Manutencao>()
    
    private var lastVisible: DocumentSnapshot? = null
    private var currentFirstDoc: DocumentSnapshot? = null
    private val PAGE_SIZE = 5L
    private var isLoading = false
    private var hasMore = true
    private var currentPage = 1
    private val pageStack = mutableListOf<DocumentSnapshot?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_agenda)
        db = FirebaseFirestore.getInstance()

        val rv = findViewById<RecyclerView>(R.id.rv_agenda)
        adapter = ManutencaoAdapter(lista) { manutencao ->
            val intent = Intent(this, EditarManutencao::class.java)
            intent.putExtra("manutencao_id", manutencao.id)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        findViewById<View>(R.id.btn_proxima_agenda).setOnClickListener {
            if (hasMore) {
                pageStack.add(currentFirstDoc)
                currentPage++
                carregarAgenda(paginar = true, forward = true)
            }
        }

        findViewById<View>(R.id.btn_anterior_agenda).setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                val prev = if (pageStack.isNotEmpty()) pageStack.removeAt(pageStack.size - 1) else null
                carregarAgenda(paginar = true, forward = false, startAtDoc = prev)
            }
        }

        carregarAgenda(paginar = false)
    }

    override fun onResume() {
        super.onResume()
        // Recarregar para ver mudanças de status
        currentPage = 1
        pageStack.clear()
        carregarAgenda(paginar = false)
    }

    private fun carregarAgenda(paginar: Boolean, forward: Boolean = true, startAtDoc: DocumentSnapshot? = null) {
        if (isLoading) return
        isLoading = true
        val progress = findViewById<View>(R.id.progress_agenda)
        progress.visibility = View.VISIBLE
        
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        var query: Query = db.collection("Manutencoes")
            .whereEqualTo("uid", uid)
            .whereEqualTo("statusManutencao", "Agendada")
            .orderBy("data", Query.Direction.ASCENDING) // Agenda é cronológica futura

        if (paginar) {
            if (forward && lastVisible != null) {
                query = query.startAfter(lastVisible!!)
            } else if (!forward && startAtDoc != null) {
                query = query.startAt(startAtDoc)
            }
        }

        query.limit(PAGE_SIZE).get().addOnSuccessListener { snap ->
            isLoading = false
            progress.visibility = View.GONE
            
            if (snap.isEmpty) { 
                hasMore = false
                if (currentPage == 1) findViewById<View>(R.id.layout_vazio_agenda).visibility = View.VISIBLE
                updateUI()
                return@addOnSuccessListener 
            }
            
            findViewById<View>(R.id.layout_vazio_agenda).visibility = View.GONE
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
            // Fallback sem ordenação se falhar índice
            carregarSemOrdenacao()
        }
    }

    private fun carregarSemOrdenacao() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("Manutencoes")
            .whereEqualTo("uid", uid)
            .whereEqualTo("statusManutencao", "Agendada")
            .limit(50)
            .get()
            .addOnSuccessListener { snap ->
                if (snap.isEmpty) {
                    findViewById<View>(R.id.layout_vazio_agenda).visibility = View.VISIBLE
                    return@addOnSuccessListener
                }
                findViewById<View>(R.id.layout_vazio_agenda).visibility = View.GONE
                val novos = snap.documents.mapNotNull { d -> d.toObject(Manutencao::class.java)?.copy(id = d.id) }
                lista.clear()
                lista.addAll(novos)
                adapter.notifyDataSetChanged()
            }
    }

    private fun updateUI() {
        findViewById<TextView>(R.id.txt_pagina_agenda).text = "Página $currentPage"
        findViewById<View>(R.id.btn_anterior_agenda).isEnabled = currentPage > 1
        findViewById<View>(R.id.btn_proxima_agenda).isEnabled = hasMore
    }
}
