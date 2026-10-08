package br.edu.ifpe.avancajovem.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import br.edu.ifpe.avancajovem.ui.components.MetaCard
import br.edu.ifpe.avancajovem.ui.components.PointsCard
import br.edu.ifpe.avancajovem.ui.components.ProgressCard
import br.edu.ifpe.avancajovem.ui.theme.BluePrimary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCriarMeta: () -> Unit,
    onNavigateToDetalheMeta: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCriarMeta,
                containerColor = BluePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Criar nova meta"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Criar meta",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Olá, estudante! 👋",
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = BluePrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Pronto para avançar hoje?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Error Message if any
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

                // Progress Card
                item {
                    ProgressCard(
                        concluidas = uiState.metasConcluidas,
                        total = uiState.totalMetas
                    )
                }

                // Section Title: Minhas metas
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Minhas metas",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (uiState.totalMetas > 0) {
                            Text(
                                text = "${uiState.metas.size} no total",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // List of metas OR Empty state
                if (uiState.metas.isEmpty()) {
                    item {
                        EmptyState(
                            title = "Você ainda não possui metas.",
                            message = "Crie uma pequena meta e comece a avançar.",
                            buttonText = "Criar minha primeira meta",
                            onButtonClick = onNavigateToCriarMeta
                        )
                    }
                } else {
                    items(
                        items = uiState.metas,
                        key = { it.id }
                    ) { meta ->
                        MetaCard(
                            meta = meta,
                            onClick = { onNavigateToDetalheMeta(meta.id) }
                        )
                    }
                }
            }
        }
    }
}
