
## 1. Instruções

Analise o projeto Avança Jovem e elabore um planejamento para adaptar exclusivamente o `HomeViewModel.kt` à utilização dos quatro estados da tela:

- Carregando;
- Vazio;
- Erro;
- Sucesso.

O projeto já possui o `UiState.kt`, com a seguinte estrutura:

- `UiState.Carregando`
- `UiState.Vazio`
- `UiState.Erro(mensagem: String)`
- `UiState.Sucesso(estudante: Estudante, metas: List<Meta>)`

Utilize essa estrutura existente, sem modificá-la.

O planejamento deve considerar:

- Utilização de `MutableStateFlow` privado e `StateFlow` público.
- Inicialização do estado como Carregando.
- Inicialização do carregamento no `init`.
- Método público `carregar()` para realizar novas tentativas.
- Retorno ao estado Carregando antes de cada tentativa.
- Emissão de Vazio quando não houver metas.
- Emissão de Sucesso quando houver metas, incluindo estudante e lista de metas.
- Tratamento de `IOException` antes de `Exception`.
- Preservação de `CancellationException`.
- Mensagens de erro amigáveis.
- Prevenção de coletores duplicados.
- Preservação do funcionamento de `deleteMeta()` e `clearError()`.

Analise como o ViewModel atual utiliza o Repository e os dados do estudante e das metas. Verifique também as dependências existentes para evitar incompatibilidades com o restante do projeto.

## 2. Restrições

- Minha única responsabilidade é o `HomeViewModel.kt`.
- Não modificar `UiState.kt`.
- Não modificar `HomeScreen.kt`.
- Não modificar Repository, DAO, entidades, modelos ou outros ViewModels.
- Não modificar `strings.xml`.
- Não criar arquivos adicionais de código.
- Não adicionar dependências.
- Não realizar refatorações desnecessárias.
- Não remover funcionalidades existentes.
- Não implementar alterações nesta etapa.
- Não executar commit, push, reset ou checkout.
- Não descartar alterações locais.

Qualquer requisito que dependa de outro arquivo deve ser apenas identificado, sem propor alterações fora da minha responsabilidade.

## 3. Saída esperada

Elabore um planejamento técnico contendo:

1. Diagnóstico do `HomeViewModel.kt` atual.
2. Integração com o `UiState.kt` existente.
3. Alterações previstas exclusivamente no ViewModel.
4. Estratégia para emissão dos quatro estados.
5. Funcionamento do carregamento inicial e de `carregar()`.
6. Tratamento de erros e cancelamento.
7. Preservação de `deleteMeta()` e `clearError()`.
8. Compatibilidade com os componentes existentes.
9. Possíveis riscos e cuidados.
10. Etapas de validação e compilação.

Salve o planejamento em:

`ai/outputs/plano-homeviewmodel.md`

Nesta etapa, apenas analise o projeto e gere o planejamento. Não implemente código nem modifique arquivos do aplicativo.