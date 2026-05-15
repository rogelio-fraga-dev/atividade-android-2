package br.com.faculdade.imepac.model

data class Manutencao(
    val id: String = "",
    val uid: String = "",
    val equipamentoId: String = "",
    val equipamentoNome: String = "",
    val tipo: String = "Preventiva",
    val descricao: String = "",
    val data: String = "",
    val custo: Double = 0.0,
    val responsavel: String = "",
    val statusManutencao: String = "Agendada",
    val checklist: List<String> = emptyList(),
    val createdAt: com.google.firebase.Timestamp? = null
)
