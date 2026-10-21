package br.edu.ifpe.avancajovem.model

data class Recompensa(
    val id: Long = 0L,
    val nome: String,
    val descricao: String,
    val pontosNecessarios: Int
)
