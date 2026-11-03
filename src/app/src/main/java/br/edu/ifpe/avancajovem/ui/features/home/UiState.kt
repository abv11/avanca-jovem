package br.edu.ifpe.avancajovem.ui.features.home

import br.edu.ifpe.avancajovem.model.Estudante
import br.edu.ifpe.avancajovem.model.Meta

sealed interface UiState {
    data object Carregando : UiState
    data object Vazio : UiState
    data class Erro(val mensagem: String) : UiState
    data class Sucesso(
        val estudante: Estudante = Estudante(),
        val metas: List<Meta> = emptyList()
    ) : UiState
}
