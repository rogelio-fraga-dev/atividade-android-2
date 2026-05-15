package br.com.faculdade.imepac

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.faculdade.imepac.model.Equipamento

class EquipamentoAdapter(
    private val lista: MutableList<Equipamento>,
    private val onItemClick: (Equipamento) -> Unit,
) : RecyclerView.Adapter<EquipamentoAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtNome: TextView       = view.findViewById(R.id.txt_nome_equipamento)
        val txtCodigo: TextView     = view.findViewById(R.id.txt_codigo_equipamento)
        val txtSetor: TextView      = view.findViewById(R.id.txt_setor_equipamento)
        val txtStatus: TextView     = view.findViewById(R.id.txt_status_equipamento)
        val txtProxima: TextView    = view.findViewById(R.id.txt_proxima_manut)
        val statusBar: View        = view.findViewById(R.id.status_indicator)
        val layoutProxima: LinearLayout = view.findViewById(R.id.layout_proxima_manut)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_equipamento, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eq = lista[position]
        holder.txtNome.text   = eq.nome
        holder.txtCodigo.text = "Cód: ${eq.codigo}"
        holder.txtSetor.text  = eq.setor
        holder.txtStatus.text = eq.status

        // Cor do status
        val (barColor, textColor, bgColor) = when (eq.status) {
            "Funcionando"    -> Triple(R.color.status_funcionando, R.color.status_funcionando, R.color.status_funcionando_bg)
            "Atenção"        -> Triple(R.color.status_atencao, R.color.status_atencao, R.color.status_atencao_bg)
            "Em Manutenção"  -> Triple(R.color.status_manutencao, R.color.status_manutencao, R.color.status_manutencao_bg)
            "Parado"         -> Triple(R.color.status_parado, R.color.status_parado, R.color.status_parado_bg)
            else             -> Triple(R.color.text_secondary, R.color.text_secondary, R.color.surface_gray)
        }
        val ctx = holder.view.context
        holder.statusBar.setBackgroundColor(ContextCompat.getColor(ctx, barColor))
        holder.txtStatus.setTextColor(ContextCompat.getColor(ctx, textColor))
        holder.txtStatus.setBackgroundResource(bgColor) // Use setBackgroundResource for color IDs

        // Próxima manutenção
        if (eq.proximaManutencao.isNotEmpty()) {
            holder.layoutProxima.visibility = View.VISIBLE
            holder.txtProxima.text = "Próx. manutenção: ${eq.proximaManutencao}"
        } else {
            holder.layoutProxima.visibility = View.GONE
        }

        holder.view.setOnClickListener { onItemClick(eq) }
    }

    override fun getItemCount() = lista.size

    fun atualizarLista(novaLista: List<Equipamento>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}
