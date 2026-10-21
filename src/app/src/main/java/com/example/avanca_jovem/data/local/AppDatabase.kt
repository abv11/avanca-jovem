package com.example.avanca_jovem.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MetaEntity::class, HistoricoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun metaDao(): MetaDao
    abstract fun historicoDao(): HistoricoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "avanca_jovem_database"
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
            if (database.metaDao().countMetas() == 0) {
                database.metaDao().insertMeta(
                    MetaEntity(
                        titulo = "Estudar matemática por 30 minutos",
                        descricao = "Revisar equações do segundo grau e fazer 5 exercícios",
                        categoria = "Estudos",
                        pontos = 20,
                        concluida = false
                    )
                )
                database.metaDao().insertMeta(
                    MetaEntity(
                        titulo = "Treino de foco e atenção",
                        descricao = "Manter foco sem celular durante 25 minutos (Pomodoro)",
                        categoria = "Atenção",
                        pontos = 15,
                        concluida = false
                    )
                )
            }
        }
    }
}
