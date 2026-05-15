package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Equipamento
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FiltroEquipamentos : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: EquipamentoAdapter
    private val lista = mutableListOf<Equipamento>()
    
    private var lastVisible: DocumentSnapshot? = null
    private var currentFirstDoc: DocumentSnapshot? = null
    private val PAGE_SIZE = 5L
    private var isLoading = false
    private var hasMore = true
    private var currentPage = 1
    private val pageStack = mutableListOf<DocumentSnapshot?>()
    private var statusAtual = "Todos"

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
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            statusAtual = when (checkedId) {
                R.id.chip_funcionando -> "Funcionando"
                R.id.chip_atencao -> "Atenção"
                R.id.chip_manutencao -> "Em Manutenção"
                R.id.chip_parado -> "Parado"
                else -> "Todos"
            }
            resetPaginacao()
            carregarComFiltro(paginar = false)
        }

        findViewById<View>(R.id.btn_proxima_filtro).setOnClickListener {
            if (hasMore) {
                pageStack.add(currentFirstDoc)
                currentPage++
                carregarComFiltro(paginar = true, forward = true)
            }
        }

        findViewById<View>(R.id.btn_anterior_filtro).setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                val prev = if (pageStack.isNotEmpty()) pageStack.removeAt(pageStack.size - 1) else null
                carregarComFiltro(paginar = true, forward = false, startAtDoc = prev)
            }
        }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        carregarComFiltro(paginar = false)
    }

    private fun resetPaginacao() {
        currentPage = 1
        pageStack.clear()
        lastVisible = null
        currentFirstDoc = null
        hasMore = true
    }

    private fun carregarComFiltro(paginar: Boolean, forward: Boolean = true, startAtDoc: DocumentSnapshot? = null) {
        if (isLoading) return
        isLoading = true
        
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        var query: Query = db.collection("Equipamentos").whereEqualTo("uid", uid)

        if (statusAtual != "Todos") {
            query = query.whereEqualTo("status", statusAtual)
        }

        query = query.orderBy("createdAt", Query.Direction.DESCENDING)

        if (paginar) {
            if (forward && lastVisible != null) {
                query = query.startAfter(lastVisible!!)
            } else if (!forward && startAtDoc != null) {
                query = query.startAt(startAtDoc)
            }
        }

        query.limit(PAGE_SIZE).get().addOnSuccessListener { snap ->
            isLoading = false
            
            if (snap.isEmpty) {
                hasMore = false
                if (currentPage == 1) {
                    lista.clear()
                    adapter.notifyDataSetChanged()
                    findViewById<View>(R.id.layout_vazio_filtro).visibility = View.VISIBLE
                }
                updateUI()
                return@addOnSuccessListener
            }

            findViewById<View>(R.id.layout_vazio_filtro).visibility = View.GONE
            currentFirstDoc = snap.documents.first()
            lastVisible = snap.documents.last()
            hasMore = snap.size() >= PAGE_SIZE.toInt()

            val novos = snap.documents.mapNotNull { d ->
                d.toObject(Equipamento::class.java)?.copy(id = d.id)
            }
            
            lista.clear()
            lista.addAll(novos)
            adapter.notifyDataSetChanged()
            updateUI()
        }.addOnFailureListener {
            isLoading = false
        }
    }

    private fun updateUI() {
        findViewById<TextView>(R.id.txt_pagina_filtro).text = "Página $currentPage"
        findViewById<View>(R.id.btn_anterior_filtro).isEnabled = currentPage > 1
        findViewById<View>(R.id.btn_proxima_filtro).isEnabled = hasMore
    }
}
