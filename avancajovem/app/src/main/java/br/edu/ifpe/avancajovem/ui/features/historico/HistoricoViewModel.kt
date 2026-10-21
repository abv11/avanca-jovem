package br.edu.ifpe.avancajovem.ui.features.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import br.edu.ifpe.avancajovem.model.HistoricoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoricoUiState(
    val isLoading: Boolean = true,
    val items: List<HistoricoItem> = emptyList(),
    val totalMetasConcluidas: Int = 0,
    val totalPontosGanhos: Int = 0,
    val errorMessage: String? = null
)

class HistoricoViewModel(private val repository: AvancaJovemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricoUiState())
    val uiState: StateFlow<HistoricoUiState> = _uiState.asStateFlow()

    init {
        loadHistorico()
    }

    private fun loadHistorico() {
        viewModelScope.launch {
            repository.historicoFlow
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Não foi possível carregar o histórico: ${e.localizedMessage}"
                        )
                    }
                }
                .collect { items ->
                    val totalPontos = items.sumOf { it.pontosRecebidos }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            items = items,
                            totalMetasConcluidas = items.size,
                            totalPontosGanhos = totalPontos,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val repository: AvancaJovemRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HistoricoViewModel(repository) as T
        }
    }
}
