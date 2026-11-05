package br.edu.ifpe.avancajovem.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifpe.avancajovem.data.repository.AvancaJovemRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// Este ViewModel coordena o fluxo de carregamento da tela inicial, centraliza o estado da UI e
// garante que a home continue atualizando metas e estudante em sincronismo com o repositório.
// Caso esse arquivo deixe de existir, a tela de início perde a fonte de dados, a renderização da lista e o estado global da home.
class HomeViewModel(private val repository: AvancaJovemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Carregando)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        carregar()
    }

    fun carregar() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.value = UiState.Carregando

            try {
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
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.value = UiState.Erro(
                    "Sem conexão ou falha ao ler os dados. Verifique a internet e tente novamente."
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Erro(
                    "Não foi possível carregar suas metas. Tente novamente."
                )
            }
        }
    }

    fun deleteMeta(metaId: Long) {
        viewModelScope.launch {
            repository.deleteMeta(metaId)
                .onFailure {
                    _uiState.update {
                        UiState.Erro("Não foi possível excluir a meta. Tente novamente.")
                    }
                }
        }
    }


    fun clearError() {
        carregar()
    }

    class Factory(private val repository: AvancaJovemRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    }
}
