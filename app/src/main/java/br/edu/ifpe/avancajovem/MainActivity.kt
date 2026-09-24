package br.edu.ifpe.avancajovem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.edu.ifpe.avancajovem.ui.navigation.NavGraph
import br.edu.ifpe.avancajovem.ui.theme.AvancajovemTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AvancajovemTheme {
                NavGraph()
            }
        }
    }
}