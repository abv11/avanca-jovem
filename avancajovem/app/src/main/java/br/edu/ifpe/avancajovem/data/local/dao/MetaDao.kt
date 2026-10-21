package br.edu.ifpe.avancajovem.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.edu.ifpe.avancajovem.data.local.entity.MetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaDao {
    @Query("SELECT * FROM meta ORDER BY id DESC")
    fun getAllMetasFlow(): Flow<List<MetaEntity>>

    @Query("SELECT * FROM meta WHERE id = :id LIMIT 1")
    fun getMetaByIdFlow(id: Long): Flow<MetaEntity?>

    @Query("SELECT * FROM meta WHERE id = :id LIMIT 1")
    suspend fun getMetaById(id: Long): MetaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeta(meta: MetaEntity): Long

    @Update
    suspend fun updateMeta(meta: MetaEntity)

    @Delete
    suspend fun deleteMeta(meta: MetaEntity)

    @Query("SELECT COUNT(*) FROM meta")
    suspend fun countMetas(): Int
}
