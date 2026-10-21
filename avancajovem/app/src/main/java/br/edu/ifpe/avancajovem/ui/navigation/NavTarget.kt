package br.edu.ifpe.avancajovem.ui.navigation

sealed class NavTarget(val route: String) {
    object Home : NavTarget("home")
    object CriarMeta : NavTarget("criar_meta")
    object DetalheMeta : NavTarget("detalhe_meta/{metaId}") {
        fun createRoute(metaId: Long) = "detalhe_meta/$metaId"
    }
    object Historico : NavTarget("historico")
    object Recompensas : NavTarget("recompensas")
}
