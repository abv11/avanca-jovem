package br.edu.ifpe.avancajovem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifpe.avancajovem.data.local.entity.EstudanteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EstudanteDao {
    @Query("SELECT * FROM estudante WHERE id = :id LIMIT 1")
    fun getEstudanteFlow(id: Long): Flow<EstudanteEntity?>

    @Query("SELECT * FROM estudante WHERE id = :id LIMIT 1")
    suspend fun getEstudanteSync(id: Long): EstudanteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(estudante: EstudanteEntity)

    @Query("UPDATE estudante SET pontos = pontos + :pontosToAdd WHERE id = :id")
    suspend fun addPontos(id: Long, pontosToAdd: Int)
}
