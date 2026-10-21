package com.example.avanca_jovem.data.repository

import androidx.room.withTransaction
import com.example.avanca_jovem.data.local.AppDatabase
import com.example.avanca_jovem.data.local.HistoricoEntity
import com.example.avanca_jovem.data.local.MetaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MetaRepository(private val database: AppDatabase) {

    private val metaDao = database.metaDao()
    private val historicoDao = database.historicoDao()

    val metasFlow: Flow<List<MetaEntity>> = metaDao.getAllMetasFlow()
    val historicoFlow: Flow<List<HistoricoEntity>> = historicoDao.getHistoricoFlow()

    val pontosTotaisFlow: Flow<Int> = historicoFlow.map { list ->
        list.sumOf { it.pontos }
    }

    fun getMetaByIdFlow(id: Long): Flow<MetaEntity?> = metaDao.getMetaByIdFlow(id)

    suspend fun criarMeta(
        titulo: String,
        descricao: String,
        categoria: String,
        pontos: Int
    ): Result<Long> {
        return try {
            val titleTrimmed = titulo.trim()
            if (titleTrimmed.isEmpty()) {
                return Result.failure(IllegalArgumentException("O título da meta não pode ser vazio."))
            }
            if (pontos <= 0) {
                return Result.failure(IllegalArgumentException("A pontuação deve ser maior que zero."))
            }

            val meta = MetaEntity(
                titulo = titleTrimmed,
                descricao = descricao.trim(),
                categoria = if (categoria.isBlank()) "Estudos" else categoria.trim(),
                pontos = pontos,
                concluida = false,
                dataCriacao = System.currentTimeMillis()
            )
            val id = metaDao.insertMeta(meta)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun concluirMeta(metaId: Long): Result<Boolean> {
        return try {
            database.withTransaction {
                val meta = metaDao.getMetaById(metaId)
                    ?: return@withTransaction Result.failure(NoSuchElementException("Meta não encontrada."))

                // Se a meta já estiver concluída, NUNCA concede pontos novamente
                if (meta.concluida) {
                    return@withTransaction Result.success(false)
                }

                val historicoExistente = historicoDao.getHistoricoByMetaId(metaId)
                if (historicoExistente != null) {
                    metaDao.updateMeta(meta.copy(concluida = true))
                    return@withTransaction Result.success(false)
                }

                val now = System.currentTimeMillis()
                val metaAtualizada = meta.copy(
                    concluida = true,
                    dataConclusao = now
                )
                metaDao.updateMeta(metaAtualizada)

                val historico = HistoricoEntity(
                    metaId = metaId,
                    titulo = meta.titulo,
                    categoria = meta.categoria,
                    pontos = meta.pontos,
                    dataConclusao = now
                )
                historicoDao.insertHistorico(historico)

                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMeta(metaId: Long): Result<Boolean> {
        return try {
            val meta = metaDao.getMetaById(metaId)
            if (meta != null) {
                metaDao.deleteMeta(meta)
                Result.success(true)
            } else {
                Result.failure(NoSuchElementException("Meta não encontrada."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
