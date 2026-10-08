package br.edu.ifpe.avancajovem.data.repository

import androidx.room.withTransaction
import br.edu.ifpe.avancajovem.data.local.AppDatabase
import br.edu.ifpe.avancajovem.data.local.entity.ConclusaoEntity
import br.edu.ifpe.avancajovem.data.local.entity.EstudanteEntity
import br.edu.ifpe.avancajovem.data.local.entity.MetaEntity
import br.edu.ifpe.avancajovem.model.Conclusao
import br.edu.ifpe.avancajovem.model.Estudante
import br.edu.ifpe.avancajovem.model.HistoricoItem
import br.edu.ifpe.avancajovem.model.Meta
import br.edu.ifpe.avancajovem.model.Recompensa
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AvancaJovemRepository(private val database: AppDatabase) {

    private val estudanteDao = database.estudanteDao()
    private val metaDao = database.metaDao()
    private val recompensaDao = database.recompensaDao()
    private val conclusaoDao = database.conclusaoDao()

    val estudanteFlow: Flow<Estudante> = estudanteDao.getEstudanteFlow(1L).map { entity ->
        entity?.toDomain() ?: Estudante(id = 1L, nome = "Estudante", pontos = 0)
    }

    val metasFlow: Flow<List<Meta>> = metaDao.getAllMetasFlow().map { list ->
        list.map { it.toDomain() }
    }

    fun getMetaByIdFlow(id: Long): Flow<Meta?> = metaDao.getMetaByIdFlow(id).map { it?.toDomain() }

    val recompensasFlow: Flow<List<Recompensa>> = recompensaDao.getAllRecompensasFlow().map { list ->
        list.map { it.toDomain() }
    }

    val historicoFlow: Flow<List<HistoricoItem>> = conclusaoDao.getHistoricoFlow()

    suspend fun criarMeta(titulo: String, descricao: String, pontos: Int): Result<Long> {
        return try {
            val titleTrimmed = titulo.trim()
            if (titleTrimmed.isEmpty()) {
                return Result.failure(IllegalArgumentException("Digite um título para sua meta."))
            }
            if (pontos <= 0) {
                return Result.failure(IllegalArgumentException("Informe uma quantidade válida de pontos."))
            }

            val metaEntity = MetaEntity(
                titulo = titleTrimmed,
                descricao = descricao.trim(),
                pontos = pontos,
                concluida = false
            )
            val newId = metaDao.insertMeta(metaEntity)
            Result.success(newId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * REGRA CRÍTICA DE NEGÓCIO:
     * Uma meta só pode gerar pontos UMA VEZ.
     */
    suspend fun concluirMeta(metaId: Long): Result<Boolean> {
        return try {
            database.withTransaction {
                val metaEntity = metaDao.getMetaById(metaId)
                    ?: return@withTransaction Result.failure(NoSuchElementException("Meta não encontrada."))

                // Se já estiver concluída, NUNCA adicionar pontos novamente
                if (metaEntity.concluida) {
                    return@withTransaction Result.success(false)
                }

                // Verificar se já existe registro em Conclusao
                val conclusaoExistente = conclusaoDao.getConclusaoByMetaId(metaId)
                if (conclusaoExistente != null) {
                    // Garantir flag concluida true e sair
                    metaDao.updateMeta(metaEntity.copy(concluida = true))
                    return@withTransaction Result.success(false)
                }

                // Mark meta as completed
                val metaConcluida = metaEntity.copy(concluida = true)
                metaDao.updateMeta(metaConcluida)

                // Register conclusion
                val conclusao = ConclusaoEntity(
                    metaId = metaId,
                    dataConclusao = System.currentTimeMillis(),
                    pontosRecebidos = metaEntity.pontos
                )
                conclusaoDao.insertConclusao(conclusao)

                // Add points to student
                var estudante = estudanteDao.getEstudanteSync(1L)
                if (estudante == null) {
                    estudante = EstudanteEntity(id = 1L, nome = "Estudante", pontos = 0)
                    estudanteDao.insertOrUpdate(estudante)
                }
                estudanteDao.addPontos(1L, metaEntity.pontos)

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
