package br.edu.ifpe.avancajovem.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.edu.ifpe.avancajovem.data.local.dao.ConclusaoDao
import br.edu.ifpe.avancajovem.data.local.dao.EstudanteDao
import br.edu.ifpe.avancajovem.data.local.dao.MetaDao
import br.edu.ifpe.avancajovem.data.local.dao.RecompensaDao
import br.edu.ifpe.avancajovem.data.local.entity.ConclusaoEntity
import br.edu.ifpe.avancajovem.data.local.entity.EstudanteEntity
import br.edu.ifpe.avancajovem.data.local.entity.MetaEntity
import br.edu.ifpe.avancajovem.data.local.entity.RecompensaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        EstudanteEntity::class,
        MetaEntity::class,
        RecompensaEntity::class,
        ConclusaoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun estudanteDao(): EstudanteDao
    abstract fun metaDao(): MetaDao
    abstract fun recompensaDao(): RecompensaDao
    abstract fun conclusaoDao(): ConclusaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "avancajovem_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedDatabase(database)
                    }
                }
            }
        }

        suspend fun seedDatabase(database: AppDatabase) {
            // Seed Estudante
            if (database.estudanteDao().getEstudanteSync(1L) == null) {
                database.estudanteDao().insertOrUpdate(
                    EstudanteEntity(id = 1L, nome = "Estudante", pontos = 0)
                )
            }

            // Seed Recompensas if empty
            if (database.recompensaDao().countRecompensas() == 0) {
                val initialRecompensas = listOf(
                    RecompensaEntity(
                        nome = "Momento de Lazer",
                        descricao = "Reserve 30 minutos para seu hobby ou jogo favorito.",
                        pontosNecessarios = 50
                    ),
                    RecompensaEntity(
                        nome = "Episódio Extra de Série",
                        descricao = "Assista a um episódio da sua série favorita sem culpa.",
                        pontosNecessarios = 100
                    ),
                    RecompensaEntity(
                        nome = "Lanche Especial",
                        descricao = "Escolha seu lanche preferido para saborear no fim de semana.",
                        pontosNecessarios = 150
                    ),
                    RecompensaEntity(
                        nome = "Passeio com Amigos",
                        descricao = "Organize uma saída divertida com seus colegas de classe.",
                        pontosNecessarios = 250
                    ),
                    RecompensaEntity(
                        nome = "Dia Livre de Estudos",
                        descricao = "Tire um dia do fim de semana sem pensar em lição de casa.",
                        pontosNecessarios = 400
                    )
                )
                database.recompensaDao().insertAll(initialRecompensas)
            }

            // Optional sample initial goals
            if (database.metaDao().countMetas() == 0) {
                database.metaDao().insertMeta(
                    MetaEntity(
                        titulo = "Revisar matemática",
                        descricao = "Estudar equações do 2º grau e resolver 5 exercícios",
                        pontos = 20,
                        concluida = false
                    )
                )
                database.metaDao().insertMeta(
                    MetaEntity(
                        titulo = "Ler 10 páginas de história",
                        descricao = "Leitura do capítulo sobre o Brasil Império",
                        pontos = 15,
                        concluida = false
                    )
                )
            }
        }
    }
}
