package com.example.avanca_jovem.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historico")
data class HistoricoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val metaId: Long,
    val titulo: String,
    val categoria: String,
    val pontos: Int,
    val dataConclusao: Long = System.currentTimeMillis()
)
