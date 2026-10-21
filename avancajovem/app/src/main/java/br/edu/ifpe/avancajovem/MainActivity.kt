package com.example.avanca_jovem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.edu.ifpe.avancajovem.data.local.AppDatabase
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import br.edu.ifpe.avancajovem.ui.navigation.AppNavHost
import br.edu.ifpe.avancajovem.ui.theme.AvancaJovemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = AvancaJovemRepository(database)

        setContent {
            AvancaJovemTheme {
                AppNavHost(repository = repository)
            }
        }
    }
}
