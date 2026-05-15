package br.com.faculdade.imepac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Tutorial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TutoriaisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_generic_list)
        findViewById<TextView>(R.id.txt_titulo_tela).text = "Base de Conhecimento"
        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        
        val rv = findViewById<RecyclerView>(R.id.rv_listagem)
        rv.layoutManager = LinearLayoutManager(this)
        
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("Tutoriais")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                val lista = snap.documents.mapNotNull { it.toObject(Tutorial::class.java) }
                rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
                        return object : RecyclerView.ViewHolder(v) {}
                    }
                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        val item = lista[position]
                        holder.itemView.findViewById<TextView>(android.R.id.text1).text = item.titulo
                        holder.itemView.findViewById<TextView>(android.R.id.text1).setTextColor(resources.getColor(R.color.black, null))
                        holder.itemView.findViewById<TextView>(android.R.id.text2).text = "${item.categoria}: ${item.descricao}"
                    }
                    override fun getItemCount() = lista.size
                }
            }
    }
}

class EstoqueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_generic_list)
        findViewById<TextView>(R.id.txt_titulo_tela).text = "Estoque de Peças"
        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        
        val rv = findViewById<RecyclerView>(R.id.rv_listagem)
        rv.layoutManager = LinearLayoutManager(this)
        
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("Estoque")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                val lista = snap.documents.mapNotNull { it.toObject(br.com.faculdade.imepac.model.Peca::class.java) }
                rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
                        return object : RecyclerView.ViewHolder(v) {}
                    }
                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        val item = lista[position]
                        holder.itemView.findViewById<TextView>(android.R.id.text1).text = item.nome
                        holder.itemView.findViewById<TextView>(android.R.id.text1).setTextColor(resources.getColor(R.color.black, null))
                        holder.itemView.findViewById<TextView>(android.R.id.text2).text = "Quantidade: ${item.quantidade} | Preço: R$ ${item.precoUnitario}"
                    }
                    override fun getItemCount() = lista.size
                }
            }
    }
}

class SetoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_generic_list)
        findViewById<TextView>(R.id.txt_titulo_tela).text = "Gestão de Setores"
        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        
        val rv = findViewById<RecyclerView>(R.id.rv_listagem)
        rv.layoutManager = LinearLayoutManager(this)
        
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("Setores")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                val lista = snap.documents.mapNotNull { it.toObject(br.com.faculdade.imepac.model.Setor::class.java) }
                rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
                        return object : RecyclerView.ViewHolder(v) {}
                    }
                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        val item = lista[position]
                        holder.itemView.findViewById<TextView>(android.R.id.text1).text = item.nome
                        holder.itemView.findViewById<TextView>(android.R.id.text1).setTextColor(resources.getColor(R.color.black, null))
                        holder.itemView.findViewById<TextView>(android.R.id.text2).text = "Prédio: ${item.predio} | Resp: ${item.responsavel}"
                    }
                    override fun getItemCount() = lista.size
                }
            }
    }
}
