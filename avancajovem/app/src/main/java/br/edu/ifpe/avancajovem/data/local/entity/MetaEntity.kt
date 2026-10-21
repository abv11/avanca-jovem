package br.edu.ifpe.avancajovem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ifpe.avancajovem.model.Meta

@Entity(tableName = "meta")
data class MetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val titulo: String,
    val descricao: String,
    val pontos: Int,
    val concluida: Boolean
) {
    fun toDomain(): Meta = Meta(
        id = id,
        titulo = titulo,
        descricao = descricao,
        pontos = pontos,
        concluida = concluida
    )

    companion object {
        fun fromDomain(meta: Meta): MetaEntity = MetaEntity(
            id = meta.id,
            titulo = meta.titulo,
            descricao = meta.descricao,
            pontos = meta.pontos,
            concluida = meta.concluida
        )
    }
}
