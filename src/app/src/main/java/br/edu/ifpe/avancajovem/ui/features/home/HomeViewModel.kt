package br.edu.ifpe.avancajovem.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface UiState {
    object Carregando : UiState
    object Vazio : UiState
    data class Sucesso(
        val estudante: br.edu.ifpe.avancajovem.model.Estudante,
        val metas: List<br.edu.ifpe.avancajovem.model.Meta>
    ) : UiState

    data class Erro(val mensagem: String) : UiState
}

class HomeViewModel(private val repository: AvancaJovemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Carregando)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.estudanteFlow,
                repository.metasFlow
            ) { estudante, metas ->
                if (metas.isEmpty()) {
                    UiState.Vazio
                } else {
                    UiState.Sucesso(
                        estudante = estudante,
                        metas = metas
                    )
                }
            }.catch { e ->
                _uiState.update {
                    UiState.Erro("Não foi possível carregar suas metas: ${e.localizedMessage}")
                }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun deleteMeta(metaId: Long) {
        viewModelScope.launch {
            repository.deleteMeta(metaId)
                .onFailure { error ->
                    _uiState.update {
                        UiState.Erro("Não foi possível excluir a meta: ${error.localizedMessage ?: "erro desconhecido"}")
                    }
                }
        }
    }

    class Factory(private val repository: AvancaJovemRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    }
}
