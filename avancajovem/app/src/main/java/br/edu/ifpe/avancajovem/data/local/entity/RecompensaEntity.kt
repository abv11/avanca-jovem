package br.edu.ifpe.avancajovem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ifpe.avancajovem.model.Recompensa

@Entity(tableName = "recompensa")
data class RecompensaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nome: String,
    val descricao: String,
    val pontosNecessarios: Int
) {
    fun toDomain(): Recompensa = Recompensa(
        id = id,
        nome = nome,
        descricao = descricao,
        pontosNecessarios = pontosNecessarios
    )

    companion object {
        fun fromDomain(recompensa: Recompensa): RecompensaEntity = RecompensaEntity(
            id = recompensa.id,
            nome = recompensa.nome,
            descricao = recompensa.descricao,
            pontosNecessarios = recompensa.pontosNecessarios
        )
    }
}
