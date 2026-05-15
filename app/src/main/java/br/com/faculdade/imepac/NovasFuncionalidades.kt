package br.com.faculdade.imepac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Peca
import br.com.faculdade.imepac.model.Setor
import br.com.faculdade.imepac.model.Tutorial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

abstract class BaseListActivity<T : Any> : AppCompatActivity() {
    protected val db = FirebaseFirestore.getInstance()
    protected val uid = FirebaseAuth.getInstance().currentUser?.uid
    
    protected val listaCompleta = mutableListOf<T>()
    protected lateinit var adapter: RecyclerView.Adapter<*>
    protected lateinit var recyclerView: RecyclerView
    
    protected var lastVisible: DocumentSnapshot? = null
    protected var PAGE_SIZE = 5L
    protected var isLoading = false
    protected var hasMore = true
    protected var pageStack = mutableListOf<DocumentSnapshot?>()
    protected var currentPage = 1
    protected var currentFirstDoc: DocumentSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_generic_list)

        recyclerView = findViewById(R.id.rv_listagem)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        findViewById<View>(R.id.fab_adicionar).setOnClickListener { mostrarDialogForm() }
        
        findViewById<View>(R.id.btn_proxima).setOnClickListener {
            if (hasMore) {
                pageStack.add(currentFirstDoc)
                currentPage++
                carregarDados(paginar = true, forward = true)
            }
        }

        findViewById<View>(R.id.btn_anterior).setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                val prev = if (pageStack.isNotEmpty()) pageStack.removeAt(pageStack.size - 1) else null
                carregarDados(paginar = true, forward = false, startAtDoc = prev)
            }
        }

        findViewById<View>(R.id.ic_filtro_opcoes).setOnClickListener { view ->
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
                resetPaginacao()
                carregarDados(false)
                true
            }
            popup.show()
        }

        findViewById<EditText>(R.id.edit_busca).addTextChangedListener { editable ->
            val queryText = editable.toString().lowercase()
            if (queryText.isEmpty()) {
                configurarAdapter(listaCompleta)
            } else {
                val filtrada = filtrarLista(queryText)
                configurarAdapter(filtrada)
            }
        }

        if (uid != null) {
            carregarDados(false)
        }
    }

    private fun resetPaginacao() {
        listaCompleta.clear()
        lastVisible = null
        currentFirstDoc = null
        pageStack.clear()
        currentPage = 1
        hasMore = true
    }

    protected fun updateUI() {
        findViewById<TextView>(R.id.txt_pagina_atual).text = "Página $currentPage"
        findViewById<View>(R.id.btn_anterior).isEnabled = currentPage > 1
        findViewById<View>(R.id.btn_proxima).isEnabled = hasMore
    }

    open fun carregarDados(paginar: Boolean, forward: Boolean = true, startAtDoc: DocumentSnapshot? = null) {
        if (isLoading) return
        isLoading = true
        findViewById<View>(R.id.progress_loading).visibility = View.VISIBLE

        var query: Query = db.collection(getCollectionName()).whereEqualTo("uid", uid)

        if (paginar) {
            if (forward && lastVisible != null) query = query.startAfter(lastVisible!!)
            else if (!forward && startAtDoc != null) query = query.startAt(startAtDoc)
        }
        
        query = query.limit(PAGE_SIZE)

        query.get().addOnSuccessListener { snap ->
            isLoading = false
            findViewById<View>(R.id.progress_loading).visibility = View.GONE
            
            if (snap.isEmpty) {
                if (!paginar) {
                    listaCompleta.clear()
                    configurarAdapter(listaCompleta)
                }
                hasMore = false
                updateUI()
                return@addOnSuccessListener
            }
            
            currentFirstDoc = snap.documents.first()
            lastVisible = snap.documents.last()
            hasMore = snap.size() >= PAGE_SIZE.toInt()
            
            val novos = parseDocuments(snap.documents)
            
            listaCompleta.clear()
            listaCompleta.addAll(novos)
            configurarAdapter(listaCompleta)
            updateUI()
        }.addOnFailureListener {
            isLoading = false
            findViewById<View>(R.id.progress_loading).visibility = View.GONE
            Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
        }
    }

    abstract fun parseDocuments(docs: List<DocumentSnapshot>): List<T>
    abstract fun configurarAdapter(exibirLista: List<T>)
    abstract fun filtrarLista(query: String): List<T>
    abstract fun mostrarDialogForm(item: T? = null)
    abstract fun deletarItem(item: T)
    abstract fun getCollectionName(): String
}

class TutoriaisActivity : BaseListActivity<Tutorial>() {
    override fun getCollectionName() = "Tutoriais"
    override fun parseDocuments(docs: List<DocumentSnapshot>) = docs.mapNotNull { it.toObject(Tutorial::class.java)?.copy(id = it.id) }
    override fun configurarAdapter(exibirLista: List<Tutorial>) {
        adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
                return object : RecyclerView.ViewHolder(v) {}
            }
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = exibirLista[position]
                holder.itemView.findViewById<TextView>(android.R.id.text1).apply { text = item.titulo; setTextColor(resources.getColor(R.color.black, null)) }
                holder.itemView.findViewById<TextView>(android.R.id.text2).text = "${item.categoria}: ${item.descricao}"
                holder.itemView.setOnClickListener { mostrarOpcoes(item) }
            }
            override fun getItemCount() = exibirLista.size
        }
        recyclerView.adapter = adapter
    }
    override fun filtrarLista(query: String) = listaCompleta.filter { it.titulo.lowercase().contains(query) }
    private fun mostrarOpcoes(item: Tutorial) {
        AlertDialog.Builder(this).setItems(arrayOf("Editar", "Excluir")) { _, which -> if (which == 0) mostrarDialogForm(item) else deletarItem(item) }.show()
    }
    override fun deletarItem(item: Tutorial) { db.collection(getCollectionName()).document(item.id).delete().addOnSuccessListener { carregarDados(false) } }
    override fun mostrarDialogForm(item: Tutorial?) {
        val view = layoutInflater.inflate(R.layout.dialog_generic_form, null)
        val edit1 = view.findViewById<EditText>(R.id.edit_field1).apply { hint = "Título" }
        val edit2 = view.findViewById<EditText>(R.id.edit_field2).apply { hint = "Categoria" }
        val edit3 = view.findViewById<EditText>(R.id.edit_field3).apply { hint = "Descrição" }
        item?.let { edit1.setText(it.titulo); edit2.setText(it.categoria); edit3.setText(it.descricao) }
        AlertDialog.Builder(this).setView(view).setPositiveButton("Salvar") { _, _ ->
            val novo = Tutorial(id = item?.id ?: "", titulo = edit1.text.toString(), categoria = edit2.text.toString(), descricao = edit3.text.toString(), uid = uid!!)
            if (item == null) db.collection(getCollectionName()).add(novo).addOnSuccessListener { carregarDados(false) }
            else db.collection(getCollectionName()).document(item.id).set(novo).addOnSuccessListener { carregarDados(false) }
        }.show()
    }
}

class EstoqueActivity : BaseListActivity<Peca>() {
    override fun getCollectionName() = "Estoque"
    override fun parseDocuments(docs: List<DocumentSnapshot>) = docs.mapNotNull { it.toObject(Peca::class.java)?.copy(id = it.id) }
    override fun configurarAdapter(exibirLista: List<Peca>) {
        adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
                return object : RecyclerView.ViewHolder(v) {}
            }
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = exibirLista[position]
                holder.itemView.findViewById<TextView>(android.R.id.text1).apply { text = item.nome; setTextColor(resources.getColor(R.color.black, null)) }
                holder.itemView.findViewById<TextView>(android.R.id.text2).text = "Qtd: ${item.quantidade} | R$ ${item.precoUnitario}"
                holder.itemView.setOnClickListener { mostrarOpcoes(item) }
            }
            override fun getItemCount() = exibirLista.size
        }
        recyclerView.adapter = adapter
    }
    override fun filtrarLista(query: String) = listaCompleta.filter { it.nome.lowercase().contains(query) }
    private fun mostrarOpcoes(item: Peca) {
        AlertDialog.Builder(this).setItems(arrayOf("Editar", "Excluir")) { _, which -> if (which == 0) mostrarDialogForm(item) else deletarItem(item) }.show()
    }
    override fun deletarItem(item: Peca) { db.collection(getCollectionName()).document(item.id).delete().addOnSuccessListener { carregarDados(false) } }
    override fun mostrarDialogForm(item: Peca?) {
        val view = layoutInflater.inflate(R.layout.dialog_generic_form, null)
        val edit1 = view.findViewById<EditText>(R.id.edit_field1).apply { hint = "Nome da Peça" }
        val edit2 = view.findViewById<EditText>(R.id.edit_field2).apply { hint = "Quantidade" }
        val edit3 = view.findViewById<EditText>(R.id.edit_field3).apply { hint = "Preço Unitário" }
        item?.let { edit1.setText(it.nome); edit2.setText(it.quantidade.toString()); edit3.setText(it.precoUnitario.toString()) }
        AlertDialog.Builder(this).setView(view).setPositiveButton("Salvar") { _, _ ->
            val novo = Peca(id = item?.id ?: "", nome = edit1.text.toString(), quantidade = edit2.text.toString().toIntOrNull() ?: 0, precoUnitario = edit3.text.toString().toDoubleOrNull() ?: 0.0, uid = uid!!)
            if (item == null) db.collection(getCollectionName()).add(novo).addOnSuccessListener { carregarDados(false) }
            else db.collection(getCollectionName()).document(item.id).set(novo).addOnSuccessListener { carregarDados(false) }
        }.show()
    }
}

class SetoresActivity : BaseListActivity<Setor>() {
    override fun getCollectionName() = "Setores"
    override fun parseDocuments(docs: List<DocumentSnapshot>) = docs.mapNotNull { it.toObject(Setor::class.java)?.copy(id = it.id) }
    override fun configurarAdapter(exibirLista: List<Setor>) {
        adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
                return object : RecyclerView.ViewHolder(v) {}
            }
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = exibirLista[position]
                holder.itemView.findViewById<TextView>(android.R.id.text1).apply { text = item.nome; setTextColor(resources.getColor(R.color.black, null)) }
                holder.itemView.findViewById<TextView>(android.R.id.text2).text = "${item.predio} | Resp: ${item.responsavel}"
                holder.itemView.setOnClickListener { mostrarOpcoes(item) }
            }
            override fun getItemCount() = exibirLista.size
        }
        recyclerView.adapter = adapter
    }
    override fun filtrarLista(query: String) = listaCompleta.filter { it.nome.lowercase().contains(query) }
    private fun mostrarOpcoes(item: Setor) {
        AlertDialog.Builder(this).setItems(arrayOf("Editar", "Excluir")) { _, which -> if (which == 0) mostrarDialogForm(item) else deletarItem(item) }.show()
    }
    override fun deletarItem(item: Setor) { db.collection(getCollectionName()).document(item.id).delete().addOnSuccessListener { carregarDados(false) } }
    override fun mostrarDialogForm(item: Setor?) {
        val view = layoutInflater.inflate(R.layout.dialog_generic_form, null)
        val edit1 = view.findViewById<EditText>(R.id.edit_field1).apply { hint = "Nome do Setor" }
        val edit2 = view.findViewById<EditText>(R.id.edit_field2).apply { hint = "Prédio/Local" }
        val edit3 = view.findViewById<EditText>(R.id.edit_field3).apply { hint = "Responsável" }
        item?.let { edit1.setText(it.nome); edit2.setText(it.predio); edit3.setText(it.responsavel) }
        AlertDialog.Builder(this).setView(view).setPositiveButton("Salvar") { _, _ ->
            val novo = Setor(id = item?.id ?: "", nome = edit1.text.toString(), predio = edit2.text.toString(), responsavel = edit3.text.toString(), uid = uid!!)
            if (item == null) db.collection(getCollectionName()).add(novo).addOnSuccessListener { carregarDados(false) }
            else db.collection(getCollectionName()).document(item.id).set(novo).addOnSuccessListener { carregarDados(false) }
        }.show()
    }
}