# PRD — Avança Jovem

## 0. Informações do documento

| Campo                  | Informação                      |
| ---------------------- | ------------------------------- |
| **App**                | Avança Jovem                    |
| **Grupo**              | B3                              |
| **Autores**            | Angélica, Laura, Luiz e Rillary |
| **Versão**             | 1.0                             |
| **Última atualização** | 22/09/2026                      |
| **Status**             | Em revisão                      |

---

# 1. Visão do produto

### 1.1 Pitch

O Avança Jovem ajuda estudantes a manter o foco e participar mais das atividades escolares por meio de metas simples, acompanhamento do progresso e pequenas recompensas.

### 1.2 Problema

Estudantes entre 12 e 18 anos podem ter dificuldades para manter a atenção, acompanhar explicações e concluir atividades escolares. Atualmente, esse acompanhamento pode depender da observação do professor, de lembretes e de registros manuais.

O aplicativo permite que o estudante acompanhe seu próprio progresso e estabeleça pequenas metas de melhoria.

### 1.3 Proposta de valor

Oferecer ao estudante uma forma simples e visual de definir metas, registrar seu cumprimento, acompanhar o progresso e receber pontos como recompensa.

---

# 2. Público e cenário de uso

### 2.1 Público-alvo

Estudantes de 12 a 18 anos, principalmente do Ensino Fundamental II e Ensino Médio.

### 2.2 Cenário de uso

O aplicativo será utilizado pelo estudante em casa ou antes/depois das aulas para consultar suas metas, registrar seu progresso e acompanhar seus pontos.

O aplicativo não tem como objetivo substituir a prática pedagógica do professor ou exigir que o estudante utilize o celular durante a aula.

### 2.3 Cliente/testador

Diesiane Benigno da Silva.

---

# 3. Objetivos e não-objetivos

### 3.1 Objetivos

* Permitir que o estudante visualize suas metas.
* Permitir a criação de metas.
* Permitir marcar uma meta como concluída.
* Registrar o progresso do estudante.
* Atribuir pontos pelo cumprimento das metas.
* Permitir acompanhar o histórico de progresso.

### 3.2 Não-objetivos

Não fazem parte do escopo do MVP:

* Chat ou mensagens.
* Notificações push.
* Cadastro/login online.
* Sistemas de punição ou exposição.
* Avaliação automática da atenção por câmera ou microfone.
* Pagamentos ou compras reais.
* Sincronização com a nuvem.

---

# 4. Requisitos funcionais

| ID   | Requisito                                                                       | Prioridade |
| ---- | ------------------------------------------------------------------------------- | ---------- |
| RF01 | O sistema deve permitir visualizar as metas cadastradas.                        | Must       |
| RF02 | O sistema deve permitir criar uma nova meta.                                    | Must       |
| RF03 | O sistema deve permitir marcar uma meta como concluída e registrar o progresso. | Must       |
| RF04 | O sistema deve atribuir pontos ao estudante após a conclusão de uma meta.       | Must       |
| RF05 | O sistema deve permitir visualizar o histórico de conclusões e progresso.       | Could      |
| RF06 | O sistema deve manter os dados cadastrados mesmo após o aplicativo ser fechado. | Must       |

---

# 5. Requisitos não funcionais

| ID    | Requisito                                                                                                 |
| ----- | --------------------------------------------------------------------------------------------------------- |
| RNF01 | O aplicativo deve funcionar sem travamentos durante pelo menos 5 minutos de uso contínuo.                 |
| RNF02 | Operações que possam apresentar falhas devem possuir tratamento adequado com try/catch quando necessário. |
| RNF03 | As mensagens de erro apresentadas ao usuário devem ser claras.                                            |
| RNF04 | O aplicativo deve utilizar Kotlin e Jetpack Compose.                                                      |
| RNF05 | O aplicativo deve utilizar persistência local com Room.                                                   |
| RNF06 | O projeto deve possuir comentários de limite de responsabilidade nos arquivos.                            |
| RNF07 | Qualquer integrante deve conseguir localizar e realizar pequenas alterações no código principal.          |
| RNF08 | Os dados devem permanecer disponíveis após o fechamento e reabertura do aplicativo.                       |

---

# 6. Telas e navegação

| Tela             | Descrição                                                          | Prioridade |
| ---------------- | ------------------------------------------------------------------ | ---------- |
| Principal        | Exibe metas, progresso e pontos do estudante.                      | Must       |
| Cadastro/Detalhe | Permite cadastrar e visualizar uma meta e registrar sua conclusão. | Must       |
| Histórico        | Exibe o histórico de conclusões e progresso.                       | Could      |

### Arquivos de referência

* `docs/telas/01-principal.png`
* `docs/telas/02-cadastro-detalhe.png`

---

# 7. Dados

O aplicativo utilizará quatro entidades principais, distribuídas entre os integrantes do grupo para que cada usuário possa realizar seu próprio commit de entidade.

### Entidade 1 — Estudante

| Campo  | Tipo   |
| ------ | ------ |
| id     | Long   |
| nome   | String |
| pontos | Int    |

**Responsável:** Laura

### Entidade 2 — Meta

| Campo     | Tipo    |
| --------- | ------- |
| id        | Long    |
| titulo    | String  |
| descricao | String  |
| pontos    | Int     |
| concluida | Boolean |

**Responsável:** Angélica

### Entidade 3 — Recompensa

| Campo             | Tipo   |
| ----------------- | ------ |
| id                | Long   |
| nome              | String |
| descricao         | String |
| pontosNecessarios | Int    |

**Responsável:** Luiz

### Entidade 4 — Conclusao

| Campo           | Tipo |
| --------------- | ---- |
| id              | Long |
| metaId          | Long |
| dataConclusao   | Long |
| pontosRecebidos | Int  |

**Responsável:** Rillary

---

# 8. Arquitetura e tecnologias

### 8.1 Tecnologias

* Kotlin
* Jetpack Compose
* Material 3
* Navigation Compose
* Room
* Retrofit 2
* Gson Converter
* Coroutines
* Flow

### 8.2 Arquitetura

O projeto utilizará uma estrutura organizada em camadas:

```text
br.edu.ifpe.avancajovem
├── data
│   ├── local
│   ├── remote
│   └── repository
├── model
├── ui
│   ├── theme
│   ├── navigation
│   └── features
└── MainActivity.kt
```

### 8.3 Persistência

Os dados serão armazenados localmente utilizando Room.

O Retrofit 2 e o Gson Converter serão adicionados como dependências conforme solicitado no boilerplate, mas não haverá necessidade de comunicação com uma API externa no MVP.

---

# 9. Tratamento de erros

| Situação                       | Tratamento                                                       |
| ------------------------------ | ---------------------------------------------------------------- |
| Erro ao salvar dados           | Informar ao usuário que não foi possível salvar.                 |
| Erro ao carregar dados         | Informar que os dados não puderam ser carregados.                |
| Dados obrigatórios ausentes    | Solicitar o preenchimento dos campos necessários.                |
| Erro na atualização dos pontos | Informar que não foi possível atualizar a pontuação.             |
| Falha inesperada               | Evitar o encerramento do aplicativo e apresentar mensagem clara. |

As operações que possam falhar deverão possuir tratamento adequado.

---

# 10. Identidade visual e publicação

### 10.1 Identidade visual

O aplicativo terá uma identidade visual limpa, moderna e voltada para estudantes, evitando uma aparência excessivamente infantil.

### 10.2 Cores

| Elemento        | Cor       |
| --------------- | --------- |
| Cor principal   | `#2563EB` |
| Cor secundária  | `#1D4ED8` |
| Cor de destaque | `#F59E0B` |

### 10.3 Ícone

O ícone utilizará uma seta de crescimento integrada a um elemento relacionado aos estudos, representando evolução e progresso.

### 10.4 Entrega

A entrega deverá conter:

* APK para testes;
* AAB;
* README;
* Nome e identidade visual definidos;
* Documentação solicitada pelo projeto.

---

# 11. Plano de testes

### Testes funcionais

Serão realizados testes para:

* Criar uma meta.
* Visualizar uma meta.
* Marcar uma meta como concluída.
* Registrar uma conclusão.
* Atualizar os pontos.
* Manter os dados após fechar o aplicativo.
* Visualizar o progresso.
* Verificar mensagens de erro.

### Testes com usuários

Pelo menos duas pessoas externas ao grupo deverão instalar e utilizar o APK.

Serão observados:

* Facilidade de uso.
* Clareza das informações.
* Funcionamento das principais ações.
* Dificuldades encontradas durante o uso.

---

# 12. Cronograma

| Marco | Data       | Entrega                     |
| ----- | ---------- | --------------------------- |
| M1    | 16/09/2026 | Canvas + repositório        |
| M2    | 30/09/2026 | PRD + telas                 |
| M3    | 21/10/2026 | Base da funcionalidade      |
| M4    | 11/11/2026 | Dados + tratamento de erros |
| M5    | 25/11/2026 | Visual + APK                |
| M6    | 02/12/2026 | AAB + publicação + README   |
| Final | 10/12/2026 | Entrega final               |

---

# 13. Riscos

| Risco                                       | Mitigação                                                                    |
| ------------------------------------------- | ---------------------------------------------------------------------------- |
| Conflitos entre alterações dos integrantes  | Utilizar commits separados e manter o repositório atualizado.                |
| Falta de espaço nos computadores            | Utilizar o GitHub como repositório central e evitar arquivos desnecessários. |
| Erros na implementação do Room              | Implementar e testar as entidades gradualmente.                              |
| Alterações feitas por IA causarem problemas | Revisar, testar e entender o código antes de aceitar alterações.             |
| Falta de tempo                              | Priorizar as funcionalidades Must do MVP.                                    |
| Perda de dados                              | Manter o código versionado no GitHub.                                        |

---

# 14. Como vamos orientar a implementação com IA

A IA será utilizada como ferramenta de apoio ao desenvolvimento.

Será utilizada para:

* Explicar conceitos.
* Auxiliar na implementação.
* Identificar e corrigir erros.
* Revisar código.
* Auxiliar na documentação.
* Criar testes quando necessário.

As regras de utilização da IA serão documentadas em:

`docs/USO_DE_IA.md`

O projeto também possuirá:

`AGENTS.md`

Nenhum código sugerido pela IA deverá ser utilizado sem que os integrantes responsáveis tenham lido, testado e entendido sua finalidade.

Não serão inseridas senhas, chaves de API ou outras informações privadas nos prompts.

---

# 15. Histórico de versões

| Versão | Data       | Alterações                                                                            | Responsável |
| ------ | ---------- | ------------------------------------------------------------------------------------- | ----------- |
| 1.0    | 20/09/2026 | Criação inicial do PRD.                                                               | Rillary       |
| 1.1    | 22/09/2026 | Atualização das entidades e divisão entre os integrantes para os commits individuais. | Rillary       |
