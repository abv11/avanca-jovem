# Plano de implementação da tela de login

## 1. Contexto e análise do projeto

O app já está estruturado em Jetpack Compose com navegação por `NavHost`, `ViewModel` e acesso local por Room via `AvancaJovemRepository`.

Pontos relevantes observados:

- A navegação principal em `AppNavHost` referencia `NavTarget.Login`, mas essa rota não existe em `NavTarget.kt`.
- O package `ui/features/login` está vazio, ou seja, a tela de login ainda não foi implementada.
- A app inicia em fluxo de autenticação teoricamente, mas sem rota/arquivo correspondente.
- A arquitetura atual é consistente com o MVP: telas por feature, ViewModels por tela e repositório único para dados locais.
- O app é 100% offline, então a autenticação deve ser local e simples, sem backend ou cadastro online.

## 2. Objetivo do plano

Implementar a tela de login com:

- campo de e-mail
- campo de senha
- validação básica de entrada
- botão de entrar
- fluxo de navegação para a home após login bem-sucedido
- tratamento de erro amigável em caso de falha

## 3. Restrições e conformidade com o escopo

Conforme o projeto e o `AGENTS.md`:

- A autenticação deve ser offline.
- Não deve haver integração com API, Firebase, conta online ou cloud.
- O app deve permanecer focado no MVP: metas, progresso, pontos e recompensas.
- O uso de Room deve continuar como persistência local principal.
- Qualquer erro de banco deve seguir o padrão de mensagem amigável: "Não foi possível salvar seu progresso. Tente novamente."
- Não é permitido adicionar recursos fora do escopo do projeto.

## 4. Diagnóstico atual da implementação

### Estado atual da navegação

- `AppNavHost` já inclui a rota de login no `startDestination`.
- O código chama `LoginScreen` e espera a callback `onLoginSuccess`.
- Porém, `NavTarget.Login` não foi declarado.
- Também falta o arquivo real da tela `LoginScreen` e qualquer ViewModel associado.

### Estado atual do repositório

- O `AvancaJovemRepository` já lida com estudantes, metas, recompensas e histórico.
- Não há persistência de credenciais de acesso no banco.
- Para manter a solução offline e simples, a autenticação não precisa exigir novo banco para usuários; pode usar uma validação local baseada em e-mail e senha fixos ou em dados do estudante salvo localmente.

### Conclusão do diagnóstico

A estrutura principal já está pronta para receber a feature; o ponto de bloqueio é a ausência da tela de login e da rota correspondente.

## 5. Estratégia de implementação sugerida

### 5.1. Criar a rota de login

Adicionar no `NavTarget` uma entrada da rota `Login`, seguindo o mesmo padrão das demais telas.

Objetivo:

- permitir navegação consistente com o restante do app
- manter a tela inicial correta no fluxo de autenticação

### 5.2. Criar a tela de login em Compose

Implementar a `LoginScreen` dentro do package `ui/features/login`.

Componentes esperados:

- título do app
- subtexto de apresentação
- campo de e-mail
- campo de senha
- botão de entrar
- mensagem de erro visual quando a validação falhar
- estado de carregamento ou botão desabilitado durante tentativa

Estilo:

- manter visual limpo e mais adulto, sem elementos infantis
- seguir a identidade visual atual do app já usada em outras telas e tema

### 5.3. Definir regra de autenticação simples e offline

Como o projeto é offline e não há cadastro online, a solução mais adequada é:

- validar e-mail no formato básico
- validar senha preenchida
- aceitar credenciais pré-definidas de uso local, como exemplo de login do estudante, ou validar contra uma credencial fixa salva localmente

Opções válidas:

- login fixo local de demonstração do app
- persistência simples de usuário no Room (mais robusto, mas exige mais escopo)
- uso de dados do estudante já existentes no banco para autenticar de forma mínima

Recomendação:

- manter a solução mais simples possível, sem criar estrutura nova complexa que não seja necessária ao MVP

### 5.4. Integrar com a navegação

Em `AppNavHost`:

- garantir que `startDestination` continue apontando para `Login`
- ao sucesso do login, navegar para `Home`
- limpar a tela de login da pilha de navegação

Isso deve evitar que o usuário volte para a tela de autenticação após entrar no app.

### 5.5. Tratamento de erros

Implementar validações e mensagens amigáveis:

- e-mail vazio
- senha vazia
- e-mail em formato inválido
- erro genérico de autenticação

A mensagem de erro deve ser amigável e clara para o usuário, sem quebrar a experiência.

## 6. Arquivos e locais previstos para alteração

### Arquivo principal de navegação

- `src/app/src/main/java/br/edu/ifpe/avancajovem/ui/navigation/NavTarget.kt`

Ajustar a definição da rota `Login`.

### Arquivo principal de navegação do app

- `src/app/src/main/java/br/edu/ifpe/avancajovem/ui/navigation/AppNavHost.kt`

Verificar a rota e garantir a navegação correta da tela de login para home.

### Novo pacote de feature

- `src/app/src/main/java/br/edu/ifpe/avancajovem/ui/features/login/`

Adicionar:

- `LoginScreen.kt`
- possível `LoginViewModel.kt` se a lógica for separada

### Possível ajuste de camada de domínio/modelo

- `src/app/src/main/java/br/edu/ifpe/avancajovem/model/`

Se houver necessidade de criar um objeto de usuário ou sessão local.

### Persistência local

- `src/app/src/main/java/br/edu/ifpe/avancajovem/data/`

Só se a solução exigir guardar credenciais ou sessão atual em Room.

## 7. Sequência recomendada de implementação

1. Criar a rota `Login` no `NavTarget`.
2. Confirmar a navegação do `AppNavHost` para a nova tela.
3. Implementar a `LoginScreen` com os campos e validação visual.
4. Definir a regra de autenticação local e offline.
5. Conectar o botão de entrar com a callback de sucesso.
6. Validar o fluxo de navegação para a home.
7. Ajustar mensagens de erro e estados de carregamento.
8. Revisar se a estética está alinhada ao restante do app.

## 8. Critérios de aceite

A implementação será considerada correta quando:

- o app iniciar na tela de login
- o usuário consegue inserir e-mail e senha
- a validação bloqueia campos vazios ou inválidos
- ao login correto, o app navega para a home
- a tela de login não permanece na pilha após o acesso
- a experiência visual mantém o padrão do app e a linguagem do MVP

## 9. Risco e observações

- Não há autenticação backend; por isso a implementação deve ser deliberadamente simples.
- Evitar gerar complexidade extra em banco de dados ou autenticação por sessão, porque isso extrapola o escopo do MVP.
- O ponto central é resolver a ausência de login na estrutura atual, sem mexer em elementos que já funcionam.

## 10. Recomendação final

A melhor solução para este projeto é uma implementação local e simples de login em Compose, sem backend, sem novas bibliotecas e sem alteração de regras de negócio da aplicação principal. A prioridade é fechar a lacuna atual da navegação, criar a tela visualmente consistente e conectar o fluxo de autenticação ao restante do app.

