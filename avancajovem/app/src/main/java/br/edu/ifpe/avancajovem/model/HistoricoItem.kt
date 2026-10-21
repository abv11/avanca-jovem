package br.edu.ifpe.avancajovem.model

data class HistoricoItem(
    val id: Long,
    val metaId: Long,
    val metaTitulo: String,
    val metaDescricao: String,
    val dataConclusao: Long,
    val pontosRecebidos: Int
)
