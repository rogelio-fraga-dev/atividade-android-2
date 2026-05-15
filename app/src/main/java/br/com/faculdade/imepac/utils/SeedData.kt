package br.com.faculdade.imepac.utils

import br.com.faculdade.imepac.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.util.*

object SeedData {

    fun seedDatabase(onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return onComplete(false)

        val batch = db.batch()

        // 1. Equipamentos
        val equipamentos = listOf(
            Equipamento("", uid, "MacBook Pro M2", "PAT-001", "Lab Info A", "10/01/2023", "Funcionando", "10/07/2024", Timestamp.now()),
            Equipamento("", uid, "Dell XPS 15", "PAT-002", "Lab Info A", "15/02/2023", "Atenção", "15/06/2024", Timestamp.now()),
            Equipamento("", uid, "Servidor PowerEdge", "PAT-003", "Data Center", "01/12/2022", "Em Manutenção", "01/06/2024", Timestamp.now()),
            Equipamento("", uid, "Switch Cisco 48p", "PAT-004", "Data Center", "20/11/2022", "Funcionando", "20/11/2024", Timestamp.now()),
            Equipamento("", uid, "Monitor LG 29'", "PAT-005", "Lab Design", "05/03/2023", "Parado", "05/03/2024", Timestamp.now())
        )

        equipamentos.forEach { eq ->
            val ref = db.collection("Equipamentos").document()
            batch.set(ref, eq.copy(id = ref.id))
            
            // 2. Manutenções para cada equipamento
            repeat(2) { i ->
                val manutRef = db.collection("Manutencoes").document()
                val isRealizada = i == 0
                val manut = hashMapOf(
                    "uid" to uid,
                    "equipamentoId" to ref.id,
                    "equipamentoNome" to eq.nome,
                    "tipo" to if (i == 0) "Preventiva" else "Corretiva",
                    "descricao" to if (isRealizada) "Reparo efetuado com sucesso." else "Manutenção agendada para o próximo ciclo.",
                    "data" to if (isRealizada) "10/05/2024" else "25/06/2024",
                    "custo" to if (isRealizada) (150..800).random().toDouble() else 0.0,
                    "responsavel" to "Técnico IMEPAC",
                    "statusManutencao" to if (isRealizada) "Realizada" else "Agendada",
                    "checklist" to if (isRealizada) listOf("Limpeza Geral", "Teste de Voltagem") else emptyList<String>(),
                    "createdAt" to Timestamp.now()
                )
                batch.set(manutRef, manut)
            }
        }

        // 3. Tutoriais (Base de Conhecimento)
        val tutoriais = listOf(
            Tutorial("", "Reset de BIOS Dell", "Pressione F2 repetidamente ao iniciar e selecione Load Defaults.", "Hardware", uid),
            Tutorial("", "Configuração de IP Fixo", "Vá em Configurações de Rede -> IPv4 e defina o IP conforme a tabela do setor.", "Redes", uid),
            Tutorial("", "Troca de Toner HP", "Abra a tampa frontal, remova o cartucho antigo e insira o novo até ouvir o clique.", "Impressoras", uid)
        )
        tutoriais.forEach { t ->
            val ref = db.collection("Tutoriais").document()
            batch.set(ref, t.copy(id = ref.id))
        }

        // 4. Estoque de Peças
        val estoque = listOf(
            Peca("", "Memória RAM 8GB DDR4", 15, 250.0, uid),
            Peca("", "SSD 480GB Kingston", 8, 180.0, uid),
            Peca("", "Cabo HDMI 2m", 25, 35.0, uid),
            Peca("", "Bateria CR2032", 50, 5.0, uid)
        )
        estoque.forEach { p ->
            val ref = db.collection("Estoque").document()
            batch.set(ref, p.copy(id = ref.id))
        }

        // 5. Setores
        val setores = listOf(
            Setor("", "Laboratório de Informática A", "Bloco 1", "João Silva", uid),
            Setor("", "Data Center Central", "Bloco 2", "Maria Oliveira", uid),
            Setor("", "Secretaria Acadêmica", "Bloco Administrativo", "Pedro Santos", uid)
        )
        setores.forEach { s ->
            val ref = db.collection("Setores").document()
            batch.set(ref, s.copy(id = ref.id))
        }

        batch.commit()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
