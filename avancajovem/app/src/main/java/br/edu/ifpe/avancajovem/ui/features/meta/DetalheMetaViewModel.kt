package br.edu.ifpe.avancajovem.ui.features.meta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import br.edu.ifpe.avancajovem.model.Meta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalheMetaUiState(
    val metaId: Long = 0L,
    val meta: Meta? = null,
    val isLoading: Boolean = true,
    val isCompleting: Boolean = false,
    val showCelebration: Boolean = false,
    val pontosGanhos: Int = 0,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false
)

class DetalheMetaViewModel(
    private val metaId: Long,
    private val repository: AvancaJovemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalheMetaUiState(metaId = metaId))
    val uiState: StateFlow<DetalheMetaUiState> = _uiState.asStateFlow()

    init {
        loadMeta()
    }

    private fun loadMeta() {
        viewModelScope.launch {
            repository.getMetaByIdFlow(metaId).collect { meta ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        meta = meta,
                        errorMessage = if (meta == null) "Meta não encontrada." else null
                    )
                }
            }
        }
    }

    fun concluirMeta() {
        val currentMeta = _uiState.value.meta ?: return
        if (currentMeta.concluida) return

        _uiState.update { it.copy(isCompleting = true, errorMessage = null) }

        viewModelScope.launch {
            val result = repository.concluirMeta(metaId)
            result.fold(
                onSuccess = { pontosAdicionados ->
                    _uiState.update {
                        it.copy(
                            isCompleting = false,
                            showCelebration = true,
                            pontosGanhos = currentMeta.pontos
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isCompleting = false,
                            errorMessage = e.localizedMessage ?: "Não foi possível concluir sua meta. Tente novamente."
                        )
                    }
                }
            )
        }
    }

    fun dismissCelebration() {
        _uiState.update { it.copy(showCelebration = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun deleteMeta() {
        viewModelScope.launch {
            val result = repository.deleteMeta(metaId)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeleted = true) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(errorMessage = e.localizedMessage ?: "Não foi possível excluir a meta.")
                    }
                }
            )
        }
    }

    class Factory(
        private val metaId: Long,
        private val repository: AvancaJovemRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetalheMetaViewModel(metaId, repository) as T
        }
    }
}
