# CANVAS DO PROJETO — AVANÇA JOVEM

|                           |                                |
| ------------------------- | ------------------------------ |
| **Nome do Grupo**         | Avança Jovem                   |
| **Integrantes**           | Angélica, Laura, Luiz, Rillary |
| **Turma**                 | 3º ano B — Ensino Médio        |
| **Repositório**           | `https://github.com/____/____` |
| **Data de preenchimento** | 10/09/2026                     |
| **Entrega final**         | **10/12/2026**                 |

---

## 🧩 Bloco 1 — Nome e pitch do app

### **Avança Jovem**

**Pitch em uma frase:**

> **O Avança Jovem ajuda estudantes a manter o foco e participar mais das atividades escolares por meio de metas simples, acompanhamento do progresso e pequenas recompensas.**

---

## 😖 Bloco 2 — Problema

### Qual dor real estamos resolvendo?

A cliente do projeto, **Diesiane Benigno da Silva**, observa em sua rotina escolar que alguns alunos apresentam dificuldade em manter a atenção durante as aulas. Eles se distraem com frequência, deixam de acompanhar explicações, não realizam determinadas atividades no momento proposto e, consequentemente, podem apresentar dificuldades de aprendizagem e participação.

O problema não é simplesmente a falta de conhecimento do conteúdo. Muitas vezes, o estudante precisa de um estímulo para transformar a atenção e a participação em um hábito.

**Situações observadas:**

* Alunos conversam ou se distraem durante explicações importantes.
* Alguns deixam atividades para depois ou precisam ser lembrados várias vezes para começar.
* A falta de acompanhamento do próprio comportamento dificulta que o estudante perceba sua evolução.

### Como esse problema é resolvido hoje, sem o app?

Atualmente, o acompanhamento depende principalmente da observação do professor, de lembretes durante a aula, de atividades e de registros feitos manualmente.

Essas estratégias podem funcionar, mas não oferecem ao estudante uma forma simples e visual de acompanhar o próprio progresso e estabelecer pequenas metas de melhoria.

---

## 👥 Bloco 3 — Público-alvo

### Perfil principal

**Estudantes de 12 a 18 anos**, principalmente alunos do Ensino Fundamental II e Ensino Médio que apresentam dificuldade em manter a atenção, organizar suas tarefas ou manter uma rotina de participação escolar.

### Quando e onde usam?

O aplicativo poderá ser utilizado **em casa e nos momentos de organização dos estudos**, principalmente antes ou depois das aulas, para registrar metas e acompanhar o próprio progresso.

Durante a aula, o aplicativo não pretende substituir a atividade pedagógica nem exigir que o estudante fique utilizando o celular.

### Cliente e pessoa para validação

**Cliente:** Diesiane Benigno da Silva — professora.

**Pessoa real para testar o ****`.apk`****:** Diesiane Benigno da Silva — cliente do projeto e profissional que vivencia o problema no contexto escolar.

---

## 💡 Bloco 4 — Solução em uma tela

A tela principal será um **painel de acompanhamento**, apresentando de forma simples as metas do estudante e seu progresso.

### A tela principal lista:

* Metas de estudo e atenção.
* Progresso das metas.
* Pontos/recompensas conquistados.

### A ação principal do usuário é:

**Registrar uma meta como concluída**, como:

> “Prestei atenção durante a explicação.”

ou

> “Concluí a atividade proposta.”

### Depois de agir, o usuário vê:

O aplicativo atualiza seu progresso e registra a conclusão da meta, permitindo que o estudante acompanhe sua evolução e acumule pontos para as recompensas definidas pelo sistema.

---

## ✅ Bloco 5 — Funcionalidades do MVP

| #      | Funcionalidade                                         | Essencial? | Quem faz         |
| ------ | ------------------------------------------------------ | ---------- | ---------------- |
| **F1** | Criar e visualizar metas de estudo e atenção           | **Sim**    | Angélica + Laura |
| **F2** | Marcar uma meta como concluída e registrar o progresso | **Sim**    | Luiz + Rillary   |
| **F3** | Sistema simples de pontos e recompensas                | **Sim**    | Angélica + Luiz  |
| **F4** | Histórico do progresso do estudante                    | **Não**    | Laura + Rillary  |

### Prioridade do MVP

O núcleo do aplicativo será:

**Meta → conclusão → pontos → progresso.**

O objetivo é que o estudante tenha um ciclo simples e compreensível, sem excesso de funcionalidades.

---

## 🚫 Bloco 6 — Fora do escopo

Para manter o projeto viável e garantir qualidade até a entrega, algumas funcionalidades ficarão fora desta versão.

* ❌ Não haverá chat entre alunos e professores.
* ❌ Não haverá sistema de mensagens ou notificações push.
* ❌ Não haverá cadastro/login com contas online.
* ❌ Não haverá sistema de punição ou exposição de alunos.
* ❌ Não haverá avaliação automática da atenção do estudante por meio da câmera ou microfone.
* ❌ Não haverá pagamento ou compra real de recompensas.
* ❌ Não haverá sincronização em nuvem nesta primeira versão.

---

## ⚙️ Bloco 7 — Caminho técnico

### Opção escolhida

* [x] **Opção A — Room:** dados salvos no próprio celular.
* [ ] Opção B — Retrofit.
* [ ] Opção C — Desafio.

### Por que Room?

O aplicativo precisa armazenar informações como:

* metas;
* conclusão das metas;
* quantidade de pontos;
* recompensas;
* progresso do estudante.

Como esses dados podem ser armazenados localmente, **Room** atende ao objetivo do MVP sem acrescentar a complexidade de uma API externa.

### Bibliotecas que o grupo vai usar

* **Room** — persistência dos dados.
* **Jetpack Compose** — construção da interface.
* **Kotlin** — linguagem de programação.
* **Android Jetpack** — componentes de desenvolvimento Android.

### Onde entra o `try/catch`?

O `try/catch` será utilizado nas operações que envolvem acesso aos dados persistidos, principalmente ao carregar, inserir ou atualizar informações no banco local.

### Pode falhar:

* Falha ao salvar uma meta.
* Falha ao atualizar o progresso.
* Falha ao carregar os dados armazenados.
* Dados inválidos ou operação inesperada no banco.

### O usuário vê a mensagem:

> **“Não foi possível salvar seu progresso. Tente novamente.”**

O aplicativo não deve simplesmente fechar ou apresentar um erro técnico para o usuário.

---

## 🎨 Bloco 8 — Identidade visual

| Item                                     | Definição do grupo                                                                                      |
| ---------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| **Nome exibido (****`strings.xml`****)** | Avança Jovem                                                                                            |
| **Cor principal (****`Color.kt`****)**   | `#2563EB`                                                                                               |
| **Ideia do ícone (512×512)**             | Um símbolo de avanço representado por uma seta ascendente integrada a um elemento relacionado ao estudo |
| **`applicationId`**                      | `br.edu.ifpe.avancajovem`                                                                               |
| **Versão inicial**                       | `1.0` — `versionCode 1`                                                                                 |

### Conceito visual

A identidade visual deve transmitir **progresso, organização e incentivo**, evitando uma aparência infantil.

A interface deverá ser limpa, com poucos elementos por tela e informações fáceis de identificar rapidamente.

---

## 👤 Bloco 9 — Equipe, papéis e riscos

| Integrante   | Papel principal       | Responsável por                                                  |
| ------------ | --------------------- | ---------------------------------------------------------------- |
| **Angélica** | Dev / telas           | Interface principal, metas e integração das telas                |
| **Laura**    | Dev / dados           | Room, entidades, DAO e persistência                              |
| **Luiz**     | Dev / lógica          | Sistema de pontos, progresso e regras do aplicativo              |
| **Rillary**  | Design e documentação | Identidade visual, documentação, testes e organização da entrega |

> Todos programam. O “papel” define quem responde por aquela parte, não quem trabalha sozinho.

### Riscos — o que pode dar errado e o plano B

| Risco                                                                   | Plano B                                                                                                 |
| ----------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| **O banco Room apresentar problemas durante a implementação.**          | Reduzir a quantidade de dados armazenados e implementar primeiro apenas metas e conclusão.              |
| **O grupo não conseguir concluir todas as funcionalidades.**            | Priorizar F1, F2 e F3 e retirar o histórico do MVP.                                                     |
| **A interface ficar complexa para o público-alvo.**                     | Realizar testes com a cliente e simplificar as telas antes do lançamento.                               |
| **Algum integrante não compreender uma parte do código gerado por IA.** | Parar a implementação, estudar o arquivo e realizar uma explicação/revisão em grupo antes de continuar. |

---

## 🤖 Bloco 10 — Acordo de trabalho com IA

A implementação poderá utilizar o **Gemini no Android Studio** como ferramenta de apoio. A IA poderá auxiliar na escrita, explicação e correção do código, mas as decisões sobre o projeto serão tomadas pelo grupo.

### Três regras que vamos escrever no nosso `AGENTS.md`

**1.** A IA deve seguir a arquitetura e as tecnologias definidas pelo grupo, sem adicionar bibliotecas ou funcionalidades desnecessárias.

**2.** Nenhum código será aceito pelo grupo sem que pelo menos um integrante leia, teste e consiga explicar seu funcionamento.

**3.** A IA deve priorizar soluções simples, legíveis e adequadas ao nível do projeto, evitando alterações desnecessárias em arquivos que não fazem parte da tarefa.

### Combinados do grupo

* [x] Ninguém clica *Accept* no Agent Mode sem ler a mudança inteira.
* [x] Quem aceitou o código escreve o comentário de fronteira do arquivo.
* [x] Antes de cada marco, revisamos juntos: alguém aqui não entende alguma parte?
* [x] Nenhuma chave de API ou senha vai para o prompt.
* [x] **Nenhuma funcionalidade será adicionada apenas porque a IA sugeriu. O grupo deve justificar sua necessidade.**

### Como vamos garantir que todos entendem tudo?

A cada funcionalidade implementada, o integrante responsável deverá apresentar aos demais:

1. qual problema aquela parte resolve;
2. quais arquivos foram modificados;
3. como o código funciona;
4. como testar a funcionalidade;
5. quais erros podem acontecer.

Além disso, o grupo fará uma revisão conjunta antes de cada marco e cada integrante deverá conseguir realizar uma pequena alteração no projeto sem depender exclusivamente da IA.

---

## 🗓️ Bloco 11 — Marcos até 10/12

| Marco                                                          | Prazo     | Como se comprova no GitHub                          |
| -------------------------------------------------------------- | --------- | --------------------------------------------------- |
| **M1 — Canvas preenchido + repositório criado**                | **16/09** | `CANVAS.md` no `main`                               |
| **M2 — PRD aprovado + telas rascunhadas**                      | **30/09** | `PRD.md` + imagens em `docs/`                       |
| **M3 — Funcionalidade base rodando**                           | **21/10** | Tela principal lista dados + 1 ação + `try/catch`   |
| **M4 — Dados completos e erros tratados**                      | **11/11** | Commits da camada Room                              |
| **M5 — Identidade visual + ****`.apk`**** de release testado** | **25/11** | Ícone, cores e `.apk` testado por 2 pessoas de fora |
| **M6 — ****`.aab`**** + material de loja + ****`README.md`**   | **02/12** | Pasta `loja/` + `README.md` completo                |
| **Entrega e apresentação**                                     | **10/12** | Tag `v1.0` no repositório                           |

---

## 🏁 Bloco 12 — Definição de pronto

O grupo só considera o aplicativo pronto quando todas estas condições forem verdadeiras:

* [ ] O app abre e permanece funcionando durante pelo menos 5 minutos de uso.
* [ ] A tela principal mostra dados reais armazenados no aplicativo.
* [ ] O usuário consegue criar ou visualizar uma meta.
* [ ] O usuário consegue marcar uma meta como concluída.
* [ ] O sistema atualiza corretamente o progresso e os pontos.
* [ ] Quando algo falha, aparece uma mensagem clara — o aplicativo não quebra.
* [ ] O aplicativo possui nome, ícone e identidade visual próprios.
* [ ] Duas pessoas de fora do grupo instalaram o `.apk` e conseguiram utilizá-lo sem explicação.
* [ ] O `README.md` explica o objetivo, funcionamento, tecnologias utilizadas e processo de build.
* [ ] O `docs/USO_DE_IA.md` e o `AGENTS.md` estão preenchidos.
* [ ] Cada integrante consegue abrir o projeto e realizar uma pequena mudança sozinho.
* [ ] Cada integrante consegue explicar as principais partes do código.
* [ ] Todo arquivo produzido pelo grupo possui o comentário de fronteira correspondente.
* [ ] A cliente, **Diesiane Benigno da Silva**, testou o aplicativo e forneceu feedback sobre sua utilização.

---

# ✍️ Validação do professor

|                 |                                                         |
| --------------- | ------------------------------------------------------- |
| **Data**        |                                                         |
| **Situação**    | ( ) Aprovado    ( ) Aprovado com ajustes    ( ) Refazer |
| **Observações** |                                                         |
