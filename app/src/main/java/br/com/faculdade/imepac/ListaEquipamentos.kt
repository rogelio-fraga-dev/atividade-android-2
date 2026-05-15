package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
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
    private var PAGE_SIZE = 5L
    private var isLoading = false
    private var hasMore = true
    private var filtroStatus: String? = null
    
    // Stack de documentos para "Anterior"
    private var pageStack = mutableListOf<DocumentSnapshot?>()
    private var currentPage = 1
    private var currentFirstDoc: DocumentSnapshot? = null

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

        findViewById<View>(R.id.btn_proxima).setOnClickListener {
            if (hasMore) {
                pageStack.add(currentFirstDoc)
                currentPage++
                carregarEquipamentos(paginar = true, forward = true)
            }
        }

        findViewById<View>(R.id.btn_anterior).setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                val previousFirst = if (pageStack.isNotEmpty()) pageStack.removeAt(pageStack.size - 1) else null
                carregarEquipamentos(paginar = true, forward = false, startAtDoc = previousFirst)
            }
        }

        findViewById<View>(R.id.fab_novo_equipamento).setOnClickListener {
            startActivity(Intent(this, CadastroEquipamento::class.java))
        }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        findViewById<View>(R.id.ic_filtro_toolbar).setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(this, view)
            popup.menu.add("Mostrar 5 itens")
            popup.menu.add("Mostrar 10 itens")
            popup.menu.add("Mostrar 20 itens")
            popup.setOnMenuItemClickListener { item ->
                PAGE_SIZE = when(item.title) {
                    "Mostrar 5 itens" -> 5L
                    "Mostrar 10 itens" -> 10L
                    "Mostrar 20 itens" -> 20L
                    else -> 5L
                }
                // Reiniciar paginação completa
                listaCompleta.clear()
                lastVisible = null
                currentFirstDoc = null
                pageStack.clear()
                currentPage = 1
                hasMore = true
                carregarEquipamentos(paginar = false)
                true
            }
            popup.show()
        }

        findViewById<EditText>(R.id.edit_busca).addTextChangedListener { editable ->
            val queryText = editable.toString().lowercase()
            if (queryText.isEmpty()) {
                // Se a busca estiver vazia, restaura a lista original paginada
                adapter.atualizarLista(listaCompleta)
                layoutVazio.visibility = if (listaCompleta.isEmpty()) View.VISIBLE else View.GONE
            } else {
                // Filtra apenas localmente o que já está carregado na página atual
                val filtrada = listaCompleta.filter {
                    (it.nome.lowercase().contains(queryText) ||
                    it.codigo.lowercase().contains(queryText) ||
                    it.setor.lowercase().contains(queryText))
                }
                adapter.atualizarLista(filtrada)
                layoutVazio.visibility = if (filtrada.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        carregarEquipamentos(paginar = false)
    }

    private fun carregarEquipamentos(paginar: Boolean, forward: Boolean = true, startAtDoc: DocumentSnapshot? = null) {
        if (isLoading) return
        isLoading = true
        val progress = findViewById<View>(R.id.progress_equip)
        progress.visibility = View.VISIBLE
        
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        var query: Query = db.collection("Equipamentos")
            .whereEqualTo("uid", uid)

        filtroStatus?.let {
            query = query.whereEqualTo("status", it)
        }

        if (paginar) {
            if (forward && lastVisible != null) {
                query = query.startAfter(lastVisible!!)
            } else if (!forward && startAtDoc != null) {
                query = query.startAt(startAtDoc)
            }
        }
        
        query = query.limit(PAGE_SIZE)

        query.get().addOnSuccessListener { snapshots ->
            isLoading = false
            progress.visibility = View.GONE
            
            if (snapshots.isEmpty) {
                hasMore = false
                if (currentPage == 1) {
                    layoutVazio.visibility = View.VISIBLE
                    listaCompleta.clear()
                    adapter.notifyDataSetChanged()
                }
                updateUI()
                return@addOnSuccessListener
            }

            layoutVazio.visibility = View.GONE
            currentFirstDoc = snapshots.documents.first()
            lastVisible = snapshots.documents.last()
            
            // Verificação mais precisa de hasMore: carregar PAGE_SIZE + 1 para ter certeza? 
            // Por enquanto, baseamos no tamanho do retorno
            hasMore = snapshots.size() >= PAGE_SIZE.toInt()

            val novos = snapshots.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Equipamento::class.java)!!.copy(id = doc.id)
                } catch (e: Exception) { null }
            }
            
            listaCompleta.clear()
            listaCompleta.addAll(novos)
            adapter.notifyDataSetChanged()
            updateUI()
        }.addOnFailureListener { e ->
            isLoading = false
            progress.visibility = View.GONE
            if (e.message?.contains("index") == true) {
                carregarSemOrdenacao()
            }
        }
    }

    private fun updateUI() {
        findViewById<TextView>(R.id.txt_pagina_atual).text = "Página $currentPage"
        findViewById<View>(R.id.btn_anterior).isEnabled = currentPage > 1
        findViewById<View>(R.id.btn_proxima).isEnabled = hasMore
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
