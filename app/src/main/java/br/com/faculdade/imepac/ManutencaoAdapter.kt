package br.com.faculdade.imepac

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Manutencao

class ManutencaoAdapter(
    private val lista: MutableList<Manutencao>,
    private val onItemClick: (Manutencao) -> Unit
) : RecyclerView.Adapter<ManutencaoAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtTipo: TextView        = view.findViewById(R.id.txt_tipo_manutencao)
        val txtStatus: TextView      = view.findViewById(R.id.txt_status_manutencao)
        val txtDesc: TextView        = view.findViewById(R.id.txt_desc_manutencao)
        val txtEquip: TextView       = view.findViewById(R.id.txt_equipamento_manutencao)
        val txtData: TextView        = view.findViewById(R.id.txt_data_manutencao)
        val txtResp: TextView        = view.findViewById(R.id.txt_responsavel_manutencao)
        val txtCusto: TextView       = view.findViewById(R.id.txt_custo_manutencao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_manutencao, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val m = lista[position]
        holder.txtTipo.text   = m.tipo
        holder.txtStatus.text = m.statusManutencao
        holder.txtDesc.text   = m.descricao
        holder.txtEquip.text  = "⚙ ${m.equipamentoNome}"
        holder.txtData.text   = m.data
        holder.txtResp.text   = m.responsavel
        holder.txtCusto.text  = "%.2f".format(m.custo)

        val ctx = holder.view.context
        // Cor do tipo
        val tipoColor = if (m.tipo == "Preventiva") R.color.status_manutencao else R.color.status_atencao
        holder.txtTipo.setTextColor(ContextCompat.getColor(ctx, tipoColor))

        // Cor do status
        val statusColor = if (m.statusManutencao == "Realizada") R.color.status_funcionando else R.color.status_atencao
        holder.txtStatus.setTextColor(ContextCompat.getColor(ctx, statusColor))

        holder.view.setOnClickListener { onItemClick(m) }
    }

    override fun getItemCount() = lista.size

    fun atualizarLista(novaLista: List<Manutencao>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}
