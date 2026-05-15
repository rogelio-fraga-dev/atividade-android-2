package br.com.faculdade.imepac.model

data class Equipamento(
    val id: String = "",
    val uid: String = "",
    val nome: String = "",
    val codigo: String = "",
    val setor: String = "",
    val dataCompra: String = "",
    val status: String = "Funcionando",
    val proximaManutencao: String = "",
    val createdAt: com.google.firebase.Timestamp? = null
)
