package com.example.avanca_jovem.model

data class Meta(
    val id: Long = 0L,
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val pontos: Int,
    val concluida: Boolean = false,
    val dataCriacao: Long = System.currentTimeMillis(),
    val dataConclusao: Long? = null
)
