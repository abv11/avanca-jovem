package br.edu.ifpe.avancajovem.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.edu.ifpe.avancajovem.model.Conclusao

@Entity(
    tableName = "conclusao",
    foreignKeys = [
        ForeignKey(
            entity = MetaEntity::class,
            parentColumns = ["id"],
            childColumns = ["metaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["metaId"])]
)
data class ConclusaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val metaId: Long,
    val dataConclusao: Long,
    val pontosRecebidos: Int
) {
    fun toDomain(): Conclusao = Conclusao(
        id = id,
        metaId = metaId,
        dataConclusao = dataConclusao,
        pontosRecebidos = pontosRecebidos
    )

    companion object {
        fun fromDomain(conclusao: Conclusao): ConclusaoEntity = ConclusaoEntity(
            id = conclusao.id,
            metaId = conclusao.metaId,
            dataConclusao = conclusao.dataConclusao,
            pontosRecebidos = conclusao.pontosRecebidos
        )
    }
}
