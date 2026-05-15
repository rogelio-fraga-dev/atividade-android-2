package br.com.faculdade.imepac.model

data class Tutorial(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val uid: String = ""
)

data class Peca(
    val id: String = "",
    val nome: String = "",
    val quantidade: Int = 0,
    val precoUnitario: Double = 0.0,
    val uid: String = ""
)

data class Setor(
    val id: String = "",
    val nome: String = "",
    val predio: String = "",
    val responsavel: String = "",
    val uid: String = ""
)
