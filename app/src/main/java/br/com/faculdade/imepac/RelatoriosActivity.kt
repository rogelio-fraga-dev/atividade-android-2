package br.com.faculdade.imepac

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.*
import android.widget.PopupMenu

class RelatoriosActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private var mesesFiltro = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_relatorios)
        db = FirebaseFirestore.getInstance()

        pieChart = findViewById(R.id.pie_chart_status)
        barChart = findViewById(R.id.bar_chart_custos)

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        
        findViewById<View>(R.id.btn_filtro_periodo).setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add("Último Mês")
            popup.menu.add("Últimos 3 Meses")
            popup.menu.add("Últimos 6 Meses")
            popup.menu.add("Últimos 12 Meses")
            popup.setOnMenuItemClickListener { item ->
                findViewById<TextView>(R.id.btn_filtro_periodo).text = "${item.title} ▼"
                mesesFiltro = when(item.title) {
                    "Último Mês" -> 1
                    "Últimos 3 Meses" -> 3
                    "Últimos 6 Meses" -> 6
                    else -> 12
                }
                carregarDadosFinanceiros()
                true
            }
            popup.show()
        }

        if (uid != null) {
            carregarKPIs()
            carregarDadosFinanceiros()
            carregarGraficoStatus()
            carregarGraficoCustos()
        }
    }

    private fun carregarKPIs() {
        // Total Equipamentos
        db.collection("Equipamentos").whereEqualTo("uid", uid).get().addOnSuccessListener { 
            findViewById<View>(R.id.kpi_total_equip).findViewById<TextView>(R.id.txt_kpi_label).text = "EQUIPAMENTOS"
            findViewById<View>(R.id.kpi_total_equip).findViewById<TextView>(R.id.txt_kpi_value).text = it.size().toString()
        }
        // Total Manutenções
        db.collection("Manutencoes").whereEqualTo("uid", uid).get().addOnSuccessListener { 
            findViewById<View>(R.id.kpi_total_manut).findViewById<TextView>(R.id.txt_kpi_label).text = "MANUTENÇÕES"
            findViewById<View>(R.id.kpi_total_manut).findViewById<TextView>(R.id.txt_kpi_value).text = it.size().toString()
        }
        // Total Setores
        db.collection("Setores").whereEqualTo("uid", uid).get().addOnSuccessListener { 
            findViewById<View>(R.id.kpi_total_setores).findViewById<TextView>(R.id.txt_kpi_label).text = "SETORES"
            findViewById<View>(R.id.kpi_total_setores).findViewById<TextView>(R.id.txt_kpi_value).text = it.size().toString()
        }
        // Total Estoque
        db.collection("Estoque").whereEqualTo("uid", uid).get().addOnSuccessListener { 
            findViewById<View>(R.id.kpi_total_estoque).findViewById<TextView>(R.id.txt_kpi_label).text = "PEÇAS ESTOQUE"
            findViewById<View>(R.id.kpi_total_estoque).findViewById<TextView>(R.id.txt_kpi_value).text = it.size().toString()
        }
    }

    private fun carregarDadosFinanceiros() {
        db.collection("Manutencoes")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                var total = 0.0
                val limiteData = Calendar.getInstance().apply { add(Calendar.MONTH, -mesesFiltro) }.time
                
                for (doc in snap) {
                    val dataDoc = doc.getTimestamp("createdAt")?.toDate()
                    if (dataDoc == null || dataDoc.after(limiteData)) {
                        total += doc.getDouble("custo") ?: 0.0
                    }
                }
                val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                findViewById<TextView>(R.id.txt_custo_total).text = format.format(total)
            }
    }

    private fun carregarGraficoStatus() {
        db.collection("Equipamentos")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                val counts = mutableMapOf<String, Int>()
                for (doc in snap) {
                    val status = doc.getString("status") ?: "Outros"
                    counts[status] = counts.getOrDefault(status, 0) + 1
                }

                val entries = mutableListOf<PieEntry>()
                counts.forEach { (status, count) ->
                    entries.add(PieEntry(count.toFloat(), status))
                }

                val dataSet = PieDataSet(entries, "")
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                dataSet.valueTextColor = Color.BLACK
                dataSet.valueTextSize = 12f

                val data = PieData(dataSet)
                pieChart.data = data
                pieChart.description.isEnabled = false
                pieChart.centerText = "Ativos"
                pieChart.animateY(1000)
                pieChart.invalidate()
            }
    }

    private fun carregarGraficoCustos() {
        db.collection("Manutencoes")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { snap ->
                val custosPorTipo = mutableMapOf<String, Double>()
                for (doc in snap) {
                    val tipo = doc.getString("tipo") ?: "Outros"
                    val custo = doc.getDouble("custo") ?: 0.0
                    custosPorTipo[tipo] = custosPorTipo.getOrDefault(tipo, 0.0) + custo
                }

                val entries = mutableListOf<BarEntry>()
                val labels = mutableListOf<String>()
                var i = 0f
                custosPorTipo.forEach { (tipo, custo) ->
                    entries.add(BarEntry(i, custo.toFloat()))
                    labels.add(tipo)
                    i++
                }

                val dataSet = BarDataSet(entries, "Custos (R$)")
                dataSet.colors = ColorTemplate.LIBERTY_COLORS.toList()
                dataSet.valueTextSize = 10f

                val data = BarData(dataSet)
                barChart.data = data
                barChart.description.isEnabled = false
                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                barChart.xAxis.granularity = 1f
                barChart.xAxis.isGranularityEnabled = true
                barChart.animateY(1000)
                barChart.invalidate()
            }
    }
}
