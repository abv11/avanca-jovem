# Planejamento para a criação do `UiState.kt`

## 1) Definir o escopo da tela e os estados de interface

A tela de home/metas precisa representar claramente quatro cenários de carregamento e interação:

- `Carregando`: enquanto os dados ainda estão sendo consultados no banco local.
- `Vazio`: quando a consulta foi concluída e não existe nenhuma meta cadastrada.
- `Erro`: quando ocorre falha no acesso ao banco ou na leitura dos dados.
- `Sucesso`: quando os dados foram carregados corretamente e podem ser exibidos na tela.

Esses estados devem ser centralizados em um único tipo de estado da UI para manter a tela previsível e facilitar a renderização condicional do Compose.

## 2) Estruturar o `sealed interface UiState`

O tipo `UiState` deve ser um `sealed interface` porque ele precisa modelar estados mutuamente exclusivos. A estrutura ideal será:

- `Carregando`: estado de carregamento inicial, sem necessidade de dados extras.
- `Vazio`: estado de sucesso sem itens, podendo manter uma mensagem amigável ou contexto opcional, mas sem lista de dados.
- `Erro`: estado com mensagem para o usuário, por exemplo: a mensagem de falha ao carregar ou salvar progresso.
- `Sucesso`: estado com os dados carregados, normalmente contendo a lista de metas e, se necessário, também o estudante para exibir pontos e identificação.

A convenção apropriada é:

- estados sem dados relevantes devem ser `data object`;
- estados que carregam informações devem ser `data class`;
- os dados devem ser armazenados como propriedades do estado para permitir acesso direto na tela.

## 3) Identificar o local correto do arquivo

O arquivo deve ser criado na mesma pasta da feature que usa esse estado. No projeto atual, a tela principal de metas está organizada em:

- `br.edu.ifpe.avancajovem.ui.features.home`

Essa pasta já contém o `HomeScreen.kt` e o `HomeViewModel.kt`, que pertencem ao mesmo fluxo da tela de listagem das metas. Portanto, o `UiState.kt` deve ficar nessa mesma pasta, respeitando a organização por feature e evitando dispersão de responsabilidades entre pacotes.

## 4) Definir a representação do estado `Sucesso` com base no modelo do projeto

A entidade principal utilizada para esta tela é a model `Meta`, conforme verificado no projeto:

- `Meta` possui dados como `id`, `titulo`, `descricao`, `pontos` e `concluida`.
- O repositório expõe o fluxo `metasFlow`, que devolve `List<Meta>`.

Portanto, o estado `Sucesso` deve representar a listagem carregada de metas. A representação mais adequada é um estado com uma propriedade do tipo lista de `Meta`, como por exemplo:

- `Sucesso` contendo a lista de metas recebidas;
- opcionalmente, também pode incluir os dados do estudante, caso a tela precise exibir `nome` e `pontos` no mesmo estado.

Em termos de modelagem conceitual, o estado de sucesso deve refletir exatamente os dados que a tela vai consumir:

- lista de metas para renderizar na interface;
- dados do estudante para exibir informações gerais da tela.

Assim, a estrutura de sucesso deve se alinhar ao fluxo já existente do `HomeViewModel`, que combina `estudanteFlow` e `metasFlow` para montar a tela.

## 5) Verificar compatibilidade com a versão do Kotlin

A versão do Kotlin usada no projeto foi confirmada em `src/gradle/libs.versions.toml`:

- Kotlin: `2.1.10`

Essa versão é totalmente compatível com a estrutura proposta:

- `sealed interface` é suportado pela linguagem moderna do Kotlin;
- `data object` também é suportado em versões recentes do Kotlin;
- `data class` continua sendo a forma padrão para estados com propriedades.

Como a versão do projeto é `2.1.10`, a estrutura com `sealed interface` + `data object` + `data class` está compatível e segue as melhores práticas atuais do Kotlin.

## 6) Resultado esperado da modelagem

Ao final, a estrutura deve seguir a lógica abaixo:

- `Carregando` como estado sem dados;
- `Vazio` como estado sem itens carregados;
- `Erro` com mensagem amigável de falha;
- `Sucesso` com os dados recebidos da entidade principal (`Meta`) e, se necessário, também `Estudante`.

A organização final deve manter uma separação clara entre o estado da tela e a lógica de negócio, sem misturar regras de UI e regras de acesso ao banco.

## 7) Checklist final antes da implementação

- Confirmar a pasta `ui.features.home` como local do `UiState.kt`.
- Usar `sealed interface` para as transições de tela.
- Escolher `data object` para estados sem conteúdo relevante.
- Escolher `data class` para estados com dados, especialmente `Erro` e `Sucesso`.
- Definir `Sucesso` com a lista das metas recebidas pelo projeto.
- Validar que a versão do Kotlin do projeto suporta `data object`.
- Manter a mensagem de erro alinhada ao padrão da aplicação: “Não foi possível salvar seu progresso. Tente novamente.” quando a falha for de persistência, e uma mensagem de carregamento/erro adequada para consultas.

Esse plano respeita a estrutura do projeto, a convenção de pacotes e a versão atual do Kotlin, sem gerar código nem alterar arquivos do repositório.
