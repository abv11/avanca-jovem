package br.edu.ifpe.avancajovem.ui.features.meta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CriarMetaUiState(
    val titulo: String = "",
    val descricao: String = "",
    val pontosText: String = "20",
    val pontosSelecionados: Int = 20,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class CriarMetaViewModel(private val repository: AvancaJovemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CriarMetaUiState())
    val uiState: StateFlow<CriarMetaUiState> = _uiState.asStateFlow()

    fun onTituloChange(newTitulo: String) {
        _uiState.update { it.copy(titulo = newTitulo, errorMessage = null) }
    }

    fun onDescricaoChange(newDescricao: String) {
        _uiState.update { it.copy(descricao = newDescricao) }
    }

    fun onPontosChange(newPontosText: String) {
        val parsed = newPontosText.filter { it.isDigit() }.toIntOrNull() ?: 0
        _uiState.update {
            it.copy(
                pontosText = newPontosText,
                pontosSelecionados = parsed,
                errorMessage = null
            )
        }
    }

    fun selectPontosChip(pontos: Int) {
        _uiState.update {
            it.copy(
                pontosText = pontos.toString(),
                pontosSelecionados = pontos,
                errorMessage = null
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun criarMeta() {
        val state = _uiState.value
        val tituloTrimmed = state.titulo.trim()

        if (tituloTrimmed.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Digite um título para sua meta.") }
            return
        }

        val pontos = state.pontosSelecionados
        if (pontos <= 0) {
            _uiState.update { it.copy(errorMessage = "Informe uma quantidade válida de pontos.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = repository.criarMeta(
                titulo = tituloTrimmed,
                descricao = state.descricao.trim(),
                pontos = pontos
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.localizedMessage ?: "Não foi possível salvar sua meta. Tente novamente."
                        )
                    }
                }
            )
        }
    }

    class Factory(private val repository: AvancaJovemRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CriarMetaViewModel(repository) as T
        }
    }
}
