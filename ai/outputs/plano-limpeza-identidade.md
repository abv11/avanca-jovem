# Planejamento Técnico — Etapa 1: Limpeza da Identidade

Este documento detalha o plano de ação para a consolidação da identidade visual do aplicativo **Avança Jovem**, eliminando elementos residuais do template padrão e garantindo a aplicação correta da marca oficial.

## 1. Diagnóstico da Identidade Atual

- **Nome do Aplicativo**: Centralizado corretamente no recurso `app_name` em `strings.xml`.
- **Cores (Compose)**: O arquivo `Color.kt` já contém a definição `BluePrimary = Color(0xFF2563EB)`, atendendo à especificação técnica.
- **Cores (XML)**: O arquivo `colors.xml` mantém a paleta "Purple" e "Teal" do template original, o que polui os recursos do projeto.
- **Tema Nativo**: O estilo `Theme.Avancajovem` em `themes.xml` é genérico e não mapeia as cores da marca para os atributos do sistema Android (como `colorPrimary` e `statusBarColor` no nível do Manifest).

## 2. Análise do `strings.xml` e Nome do Aplicativo

- **Recurso**: `<string name="app_name">Avança Jovem</string>`.
- **Uso**: Já está sendo referenciado pelo `AndroidManifest.xml` através de `@string/app_name`.
- **Ação**: Manter conforme está, garantindo que nenhuma tela ou componente utilize o nome "My Application" ou similar de forma estática.

## 3. Análise do `Color.kt` e Cor Principal

- **Cor Oficial**: `#2563EB`.
- **Organização**: A cor principal deve ser mantida como `BluePrimary` no Compose.
- **Integração XML**: Planeja-se a criação da cor `blue_primary` em `colors.xml` com o valor `#2563EB` para servir de ponte com os recursos não-Compose (como o tema XML e ícones vetoriais).

## 4. Identificação da Paleta Padrão (Remoção)

Devem ser removidas as seguintes cores de `colors.xml`:
- `purple_200`, `purple_500`, `purple_700`
- `teal_200`, `teal_700`

**Cuidados**: Antes da remoção, deve-se realizar uma busca global para garantir que nenhum `drawable` ou outro arquivo XML dependa dessas chaves.

## 5. Análise do Tema

### 5.1. Estrutura do `Theme.kt` (Compose)
- O tema já aplica as cores corretas através do `AppColorScheme`. O `SideEffect` no `AvancaJovemTheme` garante que a barra de status seja azul em tempo de execução.

### 5.2. Estrutura do `themes.xml` (Nativo)
- O estilo `Theme.Avancajovem` deve ser atualizado para definir `colorPrimary` e `colorPrimaryVariant` usando a cor oficial. Isso evita que o sistema exiba o roxo padrão durante a Splash Screen ou no gerenciador de tarefas.

## 6. Análise do `AndroidManifest.xml`

- **Tema**: A aplicação está vinculada corretamente a `android:theme="@style/Theme.Avancajovem"`.
- **Ação**: Manter a referência, mas validar se a cor da barra de status definida no tema XML não conflita com a gerência feita pelo Compose.

## 7. Arquivos Envolvidos e Finalidade

1.  **`src/app/src/main/res/values/colors.xml`**: Remoção de cores legadas e adição da cor da marca.
2.  **`src/app/src/main/res/values/themes.xml`**: Atualização do tema base para consistência com o sistema Android.
3.  **`src/app/src/main/res/values/strings.xml`**: Fonte de verdade para o nome do app.
4.  **`src/app/src/main/java/.../ui/theme/Color.kt`**: Definições de cores para a UI Compose.

## 8. Dependências e Compatibilidade

- **Telas**: As telas do Avança Jovem utilizam majoritariamente Compose, portanto, a limpeza de recursos XML tem baixo risco de quebrar a interface principal.
- **Componentes**: Botões, cards e textos que usam `MaterialTheme.colorScheme.primary` já estão apontando para o azul correto.

## 9. Riscos e Cuidados

- **Ícones Vetoriais**: Alguns ícones gerados pelo Android Studio podem usar `fillColor="@color/purple_500"`. Estes devem ser identificados e corrigidos para `@color/blue_primary`.
- **Contraste**: Ao aplicar o azul `#2563EB` na barra de status, deve-se garantir que os ícones do sistema (bateria/relógio) sejam brancos (Light Mode).

## 10. Ordem Recomendada de Execução

1.  Pesquisar referências à paleta "Purple/Teal" em drawables.
2.  Adicionar `blue_primary` ao `colors.xml`.
3.  Substituir referências em drawables por `blue_primary` ou cores neutras.
4.  Remover as cores legadas do `colors.xml`.
5.  Atualizar o `themes.xml` para refletir a marca.

## 11. Etapas de Validação e Compilação

1.  **Build**: Executar `./gradlew assembleDebug` para garantir que não restaram referências órfãs.
2.  **Launcher**: Verificar se o nome exibido no menu do celular é "Avança Jovem".
3.  **Splash**: Observar se a transição inicial do app não apresenta tons de roxo.
4.  **Consistência**: Validar se a cor azul é uniforme em todas as superfícies principais.
