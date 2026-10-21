package com.example.avanca_jovem.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoricoDao {
    @Query("SELECT * FROM historico ORDER BY dataConclusao DESC")
    fun getHistoricoFlow(): Flow<List<HistoricoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistorico(historico: HistoricoEntity): Long

    @Query("SELECT * FROM historico WHERE metaId = :metaId LIMIT 1")
    suspend fun getHistoricoByMetaId(metaId: Long): HistoricoEntity?
}
