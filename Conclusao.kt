package br.edu.ifpe.avancajovem.model

data class Conclusao(
    val id: Long = 0L,
    val metaId: Long,
    val dataConclusao: Long,
    val pontosRecebidos: Int
)
