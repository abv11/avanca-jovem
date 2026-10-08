package br.edu.ifpe.avancajovem.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import br.edu.ifpe.avancajovem.ui.features.historico.HistoricoScreen
import br.edu.ifpe.avancajovem.ui.features.historico.HistoricoViewModel
import br.edu.ifpe.avancajovem.ui.features.home.HomeScreen
import br.edu.ifpe.avancajovem.ui.features.home.HomeViewModel
import br.edu.ifpe.avancajovem.ui.features.meta.CriarMetaScreen
import br.edu.ifpe.avancajovem.ui.features.meta.CriarMetaViewModel
import br.edu.ifpe.avancajovem.ui.features.meta.DetalheMetaScreen
import br.edu.ifpe.avancajovem.ui.features.meta.DetalheMetaViewModel
import br.edu.ifpe.avancajovem.ui.features.recompensas.RecompensasScreen
import br.edu.ifpe.avancajovem.ui.features.recompensas.RecompensasViewModel
import br.edu.ifpe.avancajovem.ui.theme.BluePrimary

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = NavTarget.Home.route,
        title = "Início",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = NavTarget.Historico.route,
        title = "Histórico",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    ),
    BottomNavItem(
        route = NavTarget.Recompensas.route,
        title = "Recompensas",
        selectedIcon = Icons.Filled.EmojiEvents,
        unselectedIcon = Icons.Outlined.EmojiEvents
    )
)

@Composable
fun AppNavHost(
    repository: AvancaJovemRepository,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show BottomBar only on top-level destinations
    val showBottomBar = currentRoute in listOf(
        NavTarget.Home.route,
        NavTarget.Historico.route,
        NavTarget.Recompensas.route
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BluePrimary,
                                selectedTextColor = BluePrimary,
                                indicatorColor = BluePrimary.copy(alpha = 0.12f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavTarget.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            // Home Screen
            composable(NavTarget.Home.route) {
                val viewModel: HomeViewModel = viewModel(
                    factory = HomeViewModel.Factory(repository)
                )
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToCriarMeta = {
                        navController.navigate(NavTarget.CriarMeta.route)
                    },
                    onNavigateToDetalheMeta = { metaId ->
                        navController.navigate(NavTarget.DetalheMeta.createRoute(metaId))
                    }
                )
            }

            // Criar Meta Screen
            composable(NavTarget.CriarMeta.route) {
                val viewModel: CriarMetaViewModel = viewModel(
                    factory = CriarMetaViewModel.Factory(repository)
                )
                CriarMetaScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Detalhe Meta Screen
            composable(
                route = NavTarget.DetalheMeta.route,
                arguments = listOf(
                    navArgument("metaId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val metaId = backStackEntry.arguments?.getLong("metaId") ?: 0L
                val viewModel: DetalheMetaViewModel = viewModel(
                    factory = DetalheMetaViewModel.Factory(metaId, repository)
                )
                DetalheMetaScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Historico Screen
            composable(NavTarget.Historico.route) {
                val viewModel: HistoricoViewModel = viewModel(
                    factory = HistoricoViewModel.Factory(repository)
                )
                HistoricoScreen(viewModel = viewModel)
            }

            // Recompensas Screen
            composable(NavTarget.Recompensas.route) {
                val viewModel: RecompensasViewModel = viewModel(
                    factory = RecompensasViewModel.Factory(repository)
                )
                RecompensasScreen(viewModel = viewModel)
            }
        }
    }
}
