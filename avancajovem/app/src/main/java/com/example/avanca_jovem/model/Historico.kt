package com.example.avanca_jovem.model

data class Historico(
    val id: Long = 0L,
    val metaId: Long,
    val titulo: String,
    val categoria: String,
    val pontos: Int,
    val dataConclusao: Long = System.currentTimeMillis()
)
