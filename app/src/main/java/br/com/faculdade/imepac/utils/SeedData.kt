package br.com.faculdade.imepac.utils

import br.com.faculdade.imepac.model.Equipamento
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.util.*

object SeedData {

    fun seedDatabase(onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return onComplete(false)

        val equipamentos = listOf(
            Equipamento("", uid, "MacBook Pro M2", "PAT-001", "Lab Info A", "10/01/2023", "Funcionando", "10/07/2024", Timestamp.now()),
            Equipamento("", uid, "Dell XPS 15", "PAT-002", "Lab Info A", "15/02/2023", "Atenção", "15/06/2024", Timestamp.now()),
            Equipamento("", uid, "Servidor PowerEdge", "PAT-003", "Data Center", "01/12/2022", "Em Manutenção", "01/06/2024", Timestamp.now()),
            Equipamento("", uid, "Switch Cisco 48p", "PAT-004", "Data Center", "20/11/2022", "Funcionando", "20/11/2024", Timestamp.now()),
            Equipamento("", uid, "Monitor LG 29'", "PAT-005", "Lab Design", "05/03/2023", "Parado", "05/03/2024", Timestamp.now()),
            Equipamento("", uid, "Projetor Epson", "PAT-006", "Auditório", "12/05/2023", "Funcionando", "12/11/2023", Timestamp.now()),
            Equipamento("", uid, "Impressora HP", "PAT-007", "Secretaria", "18/04/2023", "Atenção", "18/10/2023", Timestamp.now()),
            Equipamento("", uid, "Nobreak 3KVA", "PAT-008", "Data Center", "22/01/2023", "Funcionando", "22/01/2025", Timestamp.now()),
            Equipamento("", uid, "iPad Pro 12.9", "PAT-009", "Diretoria", "30/06/2023", "Funcionando", "30/12/2023", Timestamp.now()),
            Equipamento("", uid, "Scanner Kodak", "PAT-010", "Arquivo", "10/08/2023", "Parado", "10/02/2024", Timestamp.now())
        )

        val batch = db.batch()

        // Adicionar Equipamentos
        equipamentos.forEach { eq ->
            val ref = db.collection("Equipamentos").document()
            batch.set(ref, eq.copy(id = ref.id))
            
            // Adicionar Manutenções (Algumas realizadas, algumas agendadas)
            repeat(2) { i ->
                val manutRef = db.collection("Manutencoes").document()
                val isRealizada = Random().nextBoolean()
                val manut = hashMapOf(
                    "uid" to uid,
                    "equipamentoId" to ref.id,
                    "equipamentoNome" to eq.nome,
                    "tipo" to if (i == 0) "Preventiva" else "Corretiva",
                    "descricao" to if (isRealizada) "Reparo de circuito efetuado." else "Verificação técnica agendada.",
                    "data" to if (isRealizada) "10/05/2024" else "25/05/2024",
                    "custo" to if (isRealizada) (150..1200).random().toDouble() else 0.0,
                    "responsavel" to "Técnico Especialista",
                    "statusManutencao" to if (isRealizada) "Realizada" else "Agendada",
                    "createdAt" to Timestamp.now()
                )
                batch.set(manutRef, manut)
            }
        }

        batch.commit()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
