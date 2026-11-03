# Planejamento técnico para adaptação do `HomeViewModel` aos quatro estados da tela

## 1) Diagnóstico do `HomeViewModel.kt` atual

O `HomeViewModel.kt` já está centrado no fluxo de dados da home e já usa `MutableStateFlow` e `StateFlow`, o que atende parcialmente ao padrão desejado.

Observações relevantes:

- O estado inicial é `UiState.Carregando`.
- O `init` dispara a carga inicial por meio da função `loadData()`.
- O `uiState` é exposto como `StateFlow`, com encapsulamento privado do fluxo interno.
- A combinação de `repository.estudanteFlow` e `repository.metasFlow` já monta a base de dados da tela.
- Quando a lista de metas está vazia, a tela emite `UiState.Vazio`.
- Quando há itens, a tela emite `UiState.Sucesso(...)` com estudante e lista de metas.
- O `catch` do fluxo trata falhas de leitura com uma mensagem amigável.
- O método `deleteMeta()` ainda preserva a lógica de exclusão e trata falha no banco.

O diagnóstico principal é que o código atual já segue a intenção geral da arquitetura, mas ainda precisa ser ajustado para cumprir o contrato completo dos quatro estados da tela, principalmente em termos de:

- reset do estado para `Carregando` antes de cada tentativa;
- uso explícito de `carregar()` como método público de re-tentativa;
- separação mais clara entre carregamento inicial e recarga manual;
- tratamento de `IOException` antes de `Exception`;
- preservação de `CancellationException` sem convertê-la em erro de UI;
- prevenção de coletores duplicados ao reexecutar o fluxo;
- alinhamento com a estrutura já existente em `UiState.kt`, sem alterar esse arquivo.

## 2) Integração com o `UiState.kt` existente

O projeto já define a interface de estado em `UiState.kt` com a estrutura esperada:

- `UiState.Carregando`
- `UiState.Vazio`
- `UiState.Erro(mensagem: String)`
- `UiState.Sucesso(estudante: Estudante, metas: List<Meta>)`

A estratégia de planejamento é manter essa estrutura como fonte de verdade e adaptar o `HomeViewModel.kt` para consumir exatamente esses estados, sem editar `UiState.kt`.

Importante: o projeto atual parece ter uma duplicação do `UiState` dentro do próprio `HomeViewModel.kt`, o que se configura como risco de incompatibilidade. No plano de implementação, essa duplicação deve ser tratada como um ajuste localizado no ViewModel para garantir que o tipo seja importado corretamente do arquivo de estado existente e que a compilação do restante da feature continue estável.

## 3) Alterações previstas exclusivamente no ViewModel

Como a responsabilidade do plano é limitada ao `HomeViewModel.kt`, as alterações previstas ficam restritas a:

- manter o `_uiState` privado como `MutableStateFlow<UiState>`;
- expor `uiState` como `StateFlow<UiState>`;
- inicializar `_uiState` em `UiState.Carregando`;
- usar `init { carregar() }` para disparar o carregamento inicial;
- criar um método público `carregar()` para reprocessar os dados manualmente;
- antes de cada tentativa, atualizar o estado para `Carregando`;
- tratar `IOException` antes de `Exception`;
- preservar `CancellationException` sem convertê-lo em mensagem de erro;
- manter o comportamento de `deleteMeta()` e `clearError()` sem prejuízo;
- evitar múltiplos coletores ou emissões duplicadas no fluxo de UI.

Nenhuma alteração é prevista em `UiState.kt`, `HomeScreen.kt`, `Repository`, `DAO`, `modelo`, `strings.xml` ou em outros ViewModels.

## 4) Estratégia para emissão dos quatro estados

A lógica de emissão deve seguir a mesma sequência conceitual da tela:

1. `Carregando`
   - Estado inicial e também usado antes de uma nova tentativa de carregamento.
   - Deve ser emitido imediatamente antes de consultar o repositório.

2. `Vazio`
   - Quando o flow de metas retorna lista vazia.
   - Emite `UiState.Vazio` para renderizar a mensagem de “sem metas” na `HomeScreen`.

3. `Erro`
   - Quando houver falha de leitura ou persistência.
   - Emite `UiState.Erro(mensagem = "...")` com texto amigável.
   - Devem ser usados casos como falha ao carregar metas ou ao excluir uma meta.

4. `Sucesso`
   - Quando houver metas disponíveis.
   - Emite `UiState.Sucesso(estudante = estudante, metas = metas)` para permitir a exibição dos pontos, progresso e lista de metas.

A ordem da transição é importante: `Carregando` -> tentativa -> `Vazio` ou `Sucesso` -> `Erro` se falhar.

## 5) Funcionamento do carregamento inicial e do método `carregar()`

O plano de fluxo proposto é:

- `init { carregar() }`
  - garante que os dados iniciais sejam lidos assim que o ViewModel for criado.

- `fun carregar()`
  - deve ser público para permitir recarga manual a partir da UI ou de eventos de refresh.
  - deve executar em `viewModelScope`.
  - deve iniciar com `_uiState.value = UiState.Carregando`.
  - deve coletar `repository.estudanteFlow` e `repository.metasFlow` por combinação ou por processamento sequencial, mantendo a mesma lógica da tela.

Pseudo-lógica do planejamento:

- `_uiState.value = UiState.Carregando`
- coletar dados do repositório
- se `metas.isEmpty()`: `_uiState.value = UiState.Vazio`
- se `metas.isNotEmpty()`: `_uiState.value = UiState.Sucesso(estudante, metas)`
- se houver exceção: tratar conforme a regra de `IOException` e `Exception`

Esse desenho garante que o estado da tela reflita o momento exato da operação e evita que uma recarga preserve um estado de sucesso antigo quando o carregamento falha.

## 6) Tratamento de erros e cancelamento

O ViewModel deve seguir a regra de priorização da exceção:

- `IOException` primeiro: falha de leitura/escrita em armazenamento local, muito comum no Room e em dados persistidos;
- `Exception` depois: exceções genéricas;
- `CancellationException` preservada: não deve ser transformada em erro de UI nem interromper o fluxo de estado de forma silenciosa de maneira que o app pare de responder.

Mensagens amigáveis:

- para falha de carregamento: “Não foi possível carregar suas metas. Tente novamente.”
- para falha ao excluir meta: “Não foi possível excluir a meta. Tente novamente.”
- para falha de persistência/atualização conforme o padrão do app: “Não foi possível salvar seu progresso. Tente novamente.”

A regra de negócio deve respeitar o padrão da interface atual e não introduzir mensagens agressivas ou técnicas para o usuário final.

## 7) Preservação de `deleteMeta()` e `clearError()`

O planejamento deve manter intacta a funcionalidade já existente:

- `deleteMeta(metaId: Long)` continua fazendo a exclusão no repositório.
- caso a operação falhe, o ViewModel emite `UiState.Erro(...)`.
- caso a operação entre bem, a tela continua no estado de sucesso, sem quebrar a atualização da lista.

Se houver uma função de limpeza de erro (`clearError()`), o plano deve preservá-la e mantê-la sem efeitos colaterais sobre o carregamento. Essa operação deve simplesmente limpar o estado de erro e voltar para a execução normal da tela, sem rearmar a tela indevidamente.

## 8) Compatibilidade com os componentes existentes

A `HomeScreen.kt` já faz `when (val state = uiState)` e contempla:

- `UiState.Carregando`
- `UiState.Vazio`
- `UiState.Erro`
- `UiState.Sucesso`

Esse contrato é compatível com a estrutura atual e com as negociações de visualização da tela, logo a adaptação do ViewModel deve ser feita sem tocar na `HomeScreen.kt`.

Pontos de atenção:

- `UiState.Sucesso` exige `estudante` e `metas`; a `HomeScreen` usa `estudante.pontos` e `metas.count { it.concluida }`.
- `UiState.Erro` usa `mensagem`, e a tela renderiza como texto amigável.
- `UiState.Vazio` dispara o estado de sem metas, sem necessidade de estrutura extra.

A compatibilidade fica intacta se o ViewModel seguir exatamente o mesmo contrato já esperado pela UI.

## 9) Possíveis riscos e cuidados

Os principais riscos desta mudança são:

- duplicidade de `UiState` no mesmo pacote, o que pode causar conflitos de tipo e compilação;
- emissão de `Sucesso` com dados parcialmente vazios;
- `catch` muito amplo capturando `CancellationException` e convertendo em mensagem técnica;
- reexecução do `loadData` sem controle de coleta, gerando múltiplos observadores e estados inconsistentes;
- manter o estado em erro depois de tentativa bem-sucedida, caso a lógica de reprocessamento não limpe o erro;
- alterar a semântica de `deleteMeta()` ou `clearError()` em uma correlação que afete `HomeScreen`.

Para mitigar esses riscos, a implementação deve manter o escopo restrito ao ViewModel, preservar a estrutura de estado já aceita pela tela e seguir a ordem correta de transição entre estados.

## 10) Etapas de validação e compilação

A validação deve ser feita após a implementação do ViewModel, sem alterar outros arquivos do app:

1. validar que o projeto compila sem erros na feature de home;
2. verificar que a tela inicia em `Carregando`;
3. confirmar que, sem metas, o estado muda para `Vazio`;
4. confirmar que, com metas, o estado passa para `Sucesso` com estudante e lista;
5. forçar falha de leitura para testar `Erro` e a mensagem amigável;
6. confirmar que `IOException` é tratada antes de `Exception`;
7. verificar que `CancellationException` não vira erro de UI;
8. validar que `deleteMeta()` e `clearError()` continuam funcionando sem regressões;
9. checar se `HomeScreen.kt` continua compatível com as emissões do `StateFlow`.

Como a tarefa exige apenas planejamento, esta etapa final tem caráter de preparação para implementação e não deve incluir código ou alteração de arquivos do aplicativo.

## Conclusão

O objetivo do plano é adaptar o `HomeViewModel.kt` ao contrato já pedido pela tela, sem modificar `UiState.kt` ou `HomeScreen.kt`. A visão central é que o ViewModel mantenha um `StateFlow` corretamente encapsulado, inicialize em `Carregando`, recarregue por `carregar()`, emita `Vazio` e `Sucesso` conforme a realidade dos dados e trate falhas de forma amigável e segura, preservando a lógica de exclusão e limpeza de erro já existente.
