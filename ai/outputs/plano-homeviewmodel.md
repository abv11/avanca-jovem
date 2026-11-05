# Planejamento Técnico — Adaptação do HomeViewModel

Este documento detalha o plano de ação para adaptar o `HomeViewModel.kt` aos quatro estados de interface do projeto Avança Jovem, garantindo robustez, tratamento de erros adequado e consistência arquitetural.

## 1. Diagnóstico do `HomeViewModel.kt` Atual

A implementação atual do `HomeViewModel` já utiliza `StateFlow` e `UiState`, mas apresenta pontos que necessitam de ajuste para total conformidade com os requisitos:
- **Observação de Dados**: Utiliza `combine(repository.estudanteFlow, repository.metasFlow)` dentro de um `collect`. Isso garante reatividade, mas a lógica de emissão de `Vazio` vs `Sucesso` precisa ser validada a cada emissão do fluxo combinado.
- **Prevenção de Duplicação**: O uso de `loadJob?.cancel()` antes de iniciar uma nova coleta no método `carregar()` está correto e será preservado.
- **Lacuna Funcional**: O método `clearError()` não está implementado, ao contrário de outros ViewModels do projeto, o que impede a limpeza de mensagens de erro na UI.
- **Tratamento de Exceções**: Embora trate `IOException` e `Exception`, a ordem e a preservação de `CancellationException` devem ser garantidas para não quebrar o ciclo de vida das Coroutines.

## 2. Integração com o `UiState.kt` Existente

O ViewModel utilizará a estrutura de `sealed interface` sem qualquer modificação:
- **`UiState.Carregando`**: Estado neutro de processamento.
- **`UiState.Vazio`**: Quando o repositório retorna uma lista de metas vazia.
- **`UiState.Erro(mensagem)`**: Para falhas de rede, banco de dados ou lógica.
- **`UiState.Sucesso(estudante, metas)`**: Quando ambos os dados estão disponíveis e há ao menos uma meta.

## 3. Alterações Previstas no ViewModel

As adaptações serão cirúrgicas:
1. **Encapsulamento de Estado**: Manter `_uiState` como `MutableStateFlow` privado e `uiState` como `StateFlow` público usando `.asStateFlow()`.
2. **Bloco `init`**: Manter a chamada automática para `carregar()`.
3. **Implementação de `clearError()`**: Adicionar o método para resetar o estado de erro, preferencialmente redisparando o carregamento para atualizar a tela.
4. **Refinamento de `deleteMeta()`**: Assegurar que falhas na exclusão atualizem o estado para `Erro` com mensagem amigável.

## 4. Estratégia de Emissão dos Quatro Estados

A lógica de emissão seguirá este fluxo dentro do `collect` do `combine`:

1. **Carregando**: Emitido imediatamente ao chamar `carregar()`.
2. **Vazio**: Se `metas.isEmpty()`, emitir `UiState.Vazio`.
3. **Sucesso**: Se `metas.isNotEmpty()`, emitir `UiState.Sucesso(estudante, metas)`.
4. **Erro**: Capturado via blocos `catch` ao redor da coleta do Flow.

## 5. Carregamento Inicial e Método `carregar()`

O método `carregar()` será o motor principal:
- **Reset**: `loadJob?.cancel()` garante que apenas uma "escuta" ao banco de dados esteja ativa.
- **Estado Inicial**: Atribuição de `UiState.Carregando` antes de abrir o `launch`.
- **Persistência**: A coleta via `collect` manterá a tela atualizada em tempo real sempre que o Room notificar mudanças nos dados (metas adicionadas ou estudante atualizado).

## 6. Tratamento de Erros e Cancelamento

O bloco `try-catch` será estruturado para:
- **`CancellationException`**: Relançar imediatamente para permitir o cancelamento correto do escopo do ViewModel.
- **`IOException`**: Emitir `UiState.Erro` com mensagem sobre conexão ou leitura de arquivos ("Sem conexão ou falha ao ler os dados...").
- **`Exception`**: Emitir `UiState.Erro` com mensagem genérica amigável ("Não foi possível carregar suas metas...").

## 7. Preservação de `deleteMeta()` e `clearError()`

- **`deleteMeta()`**: Utiliza o `viewModelScope` para chamar o repositório. Em caso de `onFailure`, o estado da UI deve transitar para `UiState.Erro`.
- **`clearError()`**: Como a `HomeScreen` pode exibir um diálogo ou snackbar de erro, este método permitirá que a UI solicite a volta ao estado de carregamento ou sucesso. Dado que o `UiState.Erro` não armazena os dados anteriores, a implementação chamará `carregar()` para tentar reaver os dados.

## 8. Compatibilidade com Componentes Existentes

- **Repository**: Utilização de `estudanteFlow` e `metasFlow` que retornam `Flow<Estudante>` e `Flow<List<Meta>>` respectivamente.
- **HomeScreen**: A tela consome `uiState`. O plano garante que o contrato de dados (tipos das classes no Sucesso) não seja alterado.

## 9. Possíveis Riscos e Cuidados

- **Perda de Dados em Erros de Operação**: Como `UiState.Erro` não contém a lista de metas, um erro no `deleteMeta()` fará a lista sumir da tela. Isso é uma limitação do `UiState.kt` atual. Para mitigar, o erro deve ser "limpável" via `clearError()`.
- **Concorrência**: Múltiplas chamadas rápidas a `carregar()` são evitadas pelo cancelamento do `loadJob`.

## 10. Etapas de Validação e Compilação

1. **Check de Imports**: Validar presença de `kotlinx.coroutines.flow.*` e `java.io.IOException`.
2. **Teste de Fluxo Vazio**: Confirmar se ao deletar todas as metas a tela exibe o estado `Vazio`.
3. **Teste de Reatividade**: Adicionar uma meta via DAO/Outra tela e ver se a `Home` atualiza automaticamente para `Sucesso`.
4. **Verificação de Erros**: Simular falha de IO e validar a mensagem exibida.
5. **Build**: Executar `./gradlew assembleDebug` para confirmar integridade sintática.
