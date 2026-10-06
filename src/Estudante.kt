package br.edu.ifpe.avancajovem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estudantes")
data class Estudante(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val pontos: Int = 0
)
