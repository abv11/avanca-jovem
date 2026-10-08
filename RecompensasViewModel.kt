package br.edu.ifpe.avancajovem.ui.features.recompensas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import br.edu.ifpe.avancajovem.model.Estudante
import br.edu.ifpe.avancajovem.model.Recompensa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecompensasUiState(
    val isLoading: Boolean = true,
    val estudante: Estudante = Estudante(),
    val recompensas: List<Recompensa> = emptyList(),
    val errorMessage: String? = null
)

class RecompensasViewModel(private val repository: AvancaJovemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RecompensasUiState())
    val uiState: StateFlow<RecompensasUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.estudanteFlow,
                repository.recompensasFlow
            ) { estudante, recompensas ->
                RecompensasUiState(
                    isLoading = false,
                    estudante = estudante,
                    recompensas = recompensas,
                    errorMessage = null
                )
            }.catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível carregar as recompensas: ${e.localizedMessage}"
                    )
                }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val repository: AvancaJovemRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecompensasViewModel(repository) as T
        }
    }
}
