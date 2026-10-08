package br.edu.ifpe.avancajovem.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import br.edu.ifpe.avancajovem.model.Estudante
import br.edu.ifpe.avancajovem.model.Meta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val estudante: Estudante = Estudante(),
    val metas: List<Meta> = emptyList(),
    val metasConcluidas: Int = 0,
    val totalMetas: Int = 0,
    val errorMessage: String? = null
)

class HomeViewModel(private val repository: AvancaJovemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.estudanteFlow,
                repository.metasFlow
            ) { estudante, metas ->
                val concluidas = metas.count { it.concluida }
                HomeUiState(
                    isLoading = false,
                    estudante = estudante,
                    metas = metas,
                    metasConcluidas = concluidas,
                    totalMetas = metas.size,
                    errorMessage = null
                )
            }.catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível carregar suas metas: ${e.localizedMessage}"
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
            return HomeViewModel(repository) as T
        }
    }
}
