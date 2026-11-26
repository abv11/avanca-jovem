# Planejamento Técnico — Etapa 1: Limpeza da Identidade

Este documento detalha as ações necessárias para consolidar a identidade visual do aplicativo **Avança Jovem**, removendo elementos residuais do template padrão e estabelecendo a paleta de cores oficial.

## 1. Diagnóstico da Identidade Atual

- **Nome do Aplicativo:** Já configurado corretamente como "Avança Jovem" no recurso `app_name` em `strings.xml`.
- **Cores (Compose):** O arquivo `Color.kt` define `BluePrimary` como `#2563EB`, atendendo à marca.
- **Cores (XML):** O arquivo `colors.xml` mantém a paleta original (Purple/Teal) do template, o que gera inconsistência visual na Splash Screen nativa e na barra de status antes do carregamento total.
- **Temas:** O tema XML (`Theme.Avancajovem`) é um esqueleto genérico que não aplica a cor da marca aos atributos do sistema Android.

## 2. Análise do `strings.xml`

- **Estado Atual:** `<string name="app_name">Avança Jovem</string>`.
- **Planejamento:** Nenhuma alteração é necessária. O `AndroidManifest.xml` já referencia este recurso.

## 3. Análise do `Color.kt` e Cor Principal

- **Cor Oficial:** `#2563EB`.
- **Mapeamento Compose:** A variável `BluePrimary` está correta.
- **Mapeamento XML:** Deve-se adicionar a cor `blue_primary` ao `colors.xml` para permitir que o tema XML utilize o azul oficial da marca.

## 4. Identificação da Paleta Padrão (Remoção)

Devem ser eliminadas as seguintes cores do `colors.xml`:
- `purple_200`, `purple_500`, `purple_700`
- `teal_200`, `teal_700`

**Verificação:** Estas cores não fazem parte da identidade do Avança Jovem e servem apenas como "placeholder" do template.

## 5. Análise do Tema

### 5.1. Tema Compose (`Theme.kt`)
- O `AvancaJovemTheme` já aplica as cores corretas no `colorScheme`. O `SideEffect` garante a cor da barra de status em tempo de execução.

### 5.2. Tema Base XML (`themes.xml`)
- **Ação:** O tema nativo deve ser atualizado para definir `colorPrimary` e `colorPrimaryDark` apontando para a cor azul oficial. Isso garante que a identidade da marca seja visível desde o primeiro frame de carregamento.

## 6. Análise do `AndroidManifest.xml`

- **Manifest:** Utiliza `@string/app_name` e `@style/Theme.Avancajovem`.
- **Ação:** Manter as referências, pois a limpeza ocorrerá na definição dos recursos.

## 7. Arquivos Envolvidos e Finalidade

1. **`src/app/src/main/res/values/colors.xml`:** Remoção de cores legadas e adição da cor da marca.
2. **`src/app/src/main/res/values/themes.xml`:** Identidade visual para o sistema operacional (Splash, Status Bar nativa).
3. **`src/app/src/main/java/.../ui/theme/Color.kt`:** Referência de cores para a UI Compose.

## 8. Dependências e Compatibilidade

A interface Compose não será afetada negativamente, pois ela já utiliza o `MaterialTheme.colorScheme` injetado via `Theme.kt`. A limpeza resolve a discrepância visual na inicialização do app.

## 9. Riscos e Cuidados

- **Referência Órfã:** Se uma cor for removida do `colors.xml` e ainda for usada por um ícone vetorial, o app não compilará. É necessária uma busca global antes da exclusão.
- **Contraste:** Garantir que a barra de status azul mantenha a legibilidade dos ícones brancos do sistema.

## 10. Ordem Recomendada de Execução

1. Inserir `blue_primary` em `colors.xml`.
2. Atualizar o tema em `themes.xml` para usar o azul da marca.
3. Buscar e substituir referências a `purple` e `teal` em drawables.
4. Remover definitivamente as cores legadas de `colors.xml`.

## 11. Etapas de Validação e Compilação

1. Executar `./gradlew assembleDebug` para validar referências.
2. Verificar visualmente se a barra de status e o fundo da Splash permanecem azuis.
3. Confirmar o nome "Avança Jovem" no launcher do Android.
