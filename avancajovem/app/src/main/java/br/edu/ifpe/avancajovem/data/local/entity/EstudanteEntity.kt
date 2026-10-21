package br.edu.ifpe.avancajovem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ifpe.avancajovem.model.Estudante

@Entity(tableName = "estudante")
data class EstudanteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 1L,
    val nome: String,
    val pontos: Int
) {
    fun toDomain(): Estudante = Estudante(
        id = id,
        nome = nome,
        pontos = pontos
    )

    companion object {
        fun fromDomain(estudante: Estudante): EstudanteEntity = EstudanteEntity(
            id = estudante.id,
            nome = estudante.nome,
            pontos = estudante.pontos
        )
    }
}
