package br.edu.ifpe.avancajovem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifpe.avancajovem.data.local.entity.ConclusaoEntity
import br.edu.ifpe.avancajovem.model.HistoricoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ConclusaoDao {
    @Query("""
        SELECT c.id, c.metaId, m.titulo AS metaTitulo, m.descricao AS metaDescricao, c.dataConclusao, c.pontosRecebidos
        FROM conclusao c
        INNER JOIN meta m ON c.metaId = m.id
        ORDER BY c.dataConclusao DESC
    """)
    fun getHistoricoFlow(): Flow<List<HistoricoItem>>

    @Query("SELECT * FROM conclusao WHERE metaId = :metaId LIMIT 1")
    suspend fun getConclusaoByMetaId(metaId: Long): ConclusaoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConclusao(conclusao: ConclusaoEntity): Long
}
