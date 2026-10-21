package br.edu.ifpe.avancajovem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifpe.avancajovem.data.local.entity.RecompensaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecompensaDao {
    @Query("SELECT * FROM recompensa ORDER BY pontosNecessarios ASC")
    fun getAllRecompensasFlow(): Flow<List<RecompensaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recompensas: List<RecompensaEntity>)

    @Query("SELECT COUNT(*) FROM recompensa")
    suspend fun countRecompensas(): Int
}
