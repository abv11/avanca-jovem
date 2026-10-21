package br.edu.ifpe.avancajovem.ui.features.recompensas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ifpe.avancajovem.ui.components.EmptyState
import br.edu.ifpe.avancajovem.ui.components.ErrorMessage
import br.edu.ifpe.avancajovem.ui.components.PointsCard
import br.edu.ifpe.avancajovem.ui.components.RewardCard
import br.edu.ifpe.avancajovem.ui.theme.BluePrimary

@Composable
fun RecompensasScreen(
    viewModel: RecompensasViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BluePrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 88.dp, start = 20.dp, end = 20.dp, top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Recompensas",
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = BluePrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Use seus pontos para acompanhar suas conquistas.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (uiState.errorMessage != null) {
                    item {
                        ErrorMessage(
                            message = uiState.errorMessage!!,
                            onDismiss = { viewModel.clearError() }
                        )
                    }
                }

                // Points Card
                item {
                    PointsCard(pontos = uiState.estudante.pontos)
                }

                item {
                    Text(
                        text = "Conquistas e Recompensas",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (uiState.recompensas.isEmpty()) {
                    item {
                        EmptyState(
                            title = "Nenhuma recompensa cadastrada.",
                            message = "Continue realizando metas para acumular pontos!",
                            icon = Icons.Default.CardGiftcard
                        )
                    }
                } else {
                    items(
                        items = uiState.recompensas,
                        key = { it.id }
                    ) { recompensa ->
                        RewardCard(
                            recompensa = recompensa,
                            pontosEstudante = uiState.estudante.pontos
                        )
                    }
                }
            }
        }
    }
}
