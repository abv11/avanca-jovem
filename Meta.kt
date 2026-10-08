package br.edu.ifpe.avancajovem.model

data class Meta(
    val id: Long = 0L,
    val titulo: String,
    val descricao: String = "",
    val pontos: Int,
    val concluida: Boolean = false
)
