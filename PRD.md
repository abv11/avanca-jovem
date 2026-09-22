📄 PRD — Documento de Requisitos do Produto


---

0. Informações do documento

Campo| Informação
App| Avança Jovem
Grupo| 3º ano B
Autores| Angélica, Laura, Luiz e Rillary
Versão| 1.0
Última atualização| 22/09/2026
Status| Em revisão

---

1. Visão do produto

1.1 Pitch

O Avança Jovem ajuda estudantes a manter o foco e participar mais das atividades escolares por meio de metas simples, acompanhamento do progresso e pequenas recompensas.

1.2 Problema

Estudantes entre 12 e 18 anos podem ter dificuldades para manter a atenção, acompanhar explicações e concluir atividades escolares. Atualmente, esse acompanhamento pode depender da observação, lembretes e registros manuais feitos pelos professores.

O aplicativo busca oferecer ao estudante uma forma simples e visual de acompanhar seu próprio progresso e estabelecer pequenas metas de melhoria.

1.3 Proposta de valor

Permitir que o estudante defina metas, registre quando elas forem concluídas, acompanhe seu progresso e receba pontos como forma de recompensa pelo cumprimento das metas.

---

2. Público e cenário de uso

2.1 Público-alvo

Estudantes de 12 a 18 anos, principalmente do Ensino Fundamental II e Ensino Médio.

2.2 Cenário de uso

O aplicativo poderá ser utilizado em casa ou antes/depois das aulas para que o estudante consulte suas metas, registre seu progresso e acompanhe seus pontos.

O aplicativo não tem como objetivo substituir a prática pedagógica do professor nem exigir que o estudante utilize o celular durante a aula.

2.3 Cliente/testador

Diesiane Benigno da Silva

---

3. Objetivos e não-objetivos

3.1 Objetivos

- Permitir que o estudante visualize suas metas.
- Permitir a criação de metas simples.
- Permitir marcar uma meta como concluída.
- Registrar o progresso do estudante.
- Atribuir pontos pelo cumprimento das metas.
- Permitir acompanhar o histórico de progresso.
- Apresentar uma interface simples e visual.

3.2 Não-objetivos

Não fazem parte do escopo do MVP:

- Chat ou mensagens entre usuários.
- Notificações push.
- Cadastro/login online.
- Sistema de punição ou exposição de estudantes.
- Avaliação automática da atenção utilizando câmera ou microfone.
- Pagamentos ou compras reais.
- Sincronização com a nuvem.

---

4. Requisitos funcionais

ID| Requisito| Prioridade
RF01| O sistema deve permitir visualizar as metas cadastradas.| Must
RF02| O sistema deve permitir criar uma nova meta.| Must
RF03| O sistema deve permitir marcar uma meta como concluída e registrar o progresso.| Must
RF04| O sistema deve atribuir pontos ao estudante após a conclusão de uma meta.| Must
RF05| O sistema deve permitir visualizar o histórico de conclusões e progresso.| Could
RF06| O sistema deve manter os dados cadastrados mesmo após o aplicativo ser fechado.| Must

Fluxo principal do MVP

Criar meta → Concluir meta → Registrar conclusão → Receber pontos → Atualizar progresso

---

5. Requisitos não funcionais

ID| Requisito
RNF01| O aplicativo deve funcionar sem travamentos durante pelo menos 5 minutos de uso contínuo.
RNF02| Operações que possam apresentar falhas devem possuir tratamento adequado com "try/catch" quando necessário.
RNF03| As mensagens de erro apresentadas ao usuário devem ser claras.
RNF04| O aplicativo deve utilizar Kotlin e Jetpack Compose.
RNF05| O aplicativo deve utilizar persistência local com Room.
RNF06| O projeto deve possuir comentários de limite de responsabilidade nos arquivos conforme orientação do projeto.
RNF07| Qualquer integrante do grupo deve conseguir localizar e realizar pequenas alterações no código principal.
RNF08| O aplicativo deve manter os dados localmente mesmo após ser fechado e aberto novamente.

---

6. Telas e navegação

6.1 Tela Principal

A tela principal deve apresentar:

- Metas do estudante.
- Progresso.
- Pontuação.
- Acesso à criação de metas.
- Ação principal para marcar uma meta como concluída.

Arquivo de referência:

"docs/telas/01-principal.png"

6.2 Tela de Cadastro/Detalhe da Meta

Deve permitir:

- Cadastrar uma nova meta.
- Visualizar informações da meta.
- Registrar sua conclusão.

Arquivo de referência:

"docs/telas/02-cadastro-detalhe.png"

6.3 Histórico

O histórico de conclusões e progresso será considerado uma funcionalidade Could no MVP. Portanto, não é obrigatório possuir uma tela exclusiva para ele nesta primeira versão.

---

7. Dados

O aplicativo utilizará quatro entidades principais:

7.1 Estudante

Representa o estudante que utiliza o aplicativo.

Campo| Tipo
"id"| Long
"nome"| String
"pontos"| Int

Responsável: Laura

---

7.2 Meta

Representa uma meta criada pelo estudante.

Campo| Tipo
"id"| Long
"titulo"| String
"descricao"| String
"pontos"| Int
"concluida"| Boolean

Responsável: Angélica

---

7.3 Recompensa

Representa uma recompensa relacionada à pontuação alcançada pelo estudante.

Campo| Tipo
"id"| Long
"nome"| String
"descricao"| String
"pontosNecessarios"| Int

Responsável: Luiz

---

7.4 Conclusao

Representa o registro de uma meta concluída pelo estudante.

Campo| Tipo
"id"| Long
"metaId"| Long
"dataConclusao"| Long
"pontosRecebidos"| Int

Responsável: Rillary


---

8. Arquitetura e tecnologias

8.1 Tecnologias

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Room
- Retrofit 2
- Gson Converter
- Coroutines
- Flow

8.2 Persistência

Os dados principais serão armazenados localmente utilizando Room.

8.3 Estrutura inicial

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

O boilerplate inicial deve conter apenas a estrutura necessária para o projeto compilar e executar.

Não serão criadas entidades, DAOs, interfaces Retrofit ou ViewModels no boilerplate inicial.

Esses elementos serão adicionados posteriormente pelos integrantes conforme as etapas do projeto.

---

9. Tratamento de erros

Situação| Comportamento esperado
Erro ao salvar uma meta| Informar ao usuário que não foi possível salvar a meta.
Erro ao carregar dados| Informar que os dados não puderam ser carregados.
Meta sem informações obrigatórias| Solicitar o preenchimento dos campos necessários.
Erro na atualização de pontos| Informar que não foi possível atualizar a pontuação.
Falha inesperada| Evitar o encerramento do aplicativo e apresentar uma mensagem compreensível.

As operações que possam falhar devem possuir tratamento adequado e mensagens claras para o usuário.

---

10. Identidade visual e publicação

10.1 Identidade visual

O aplicativo terá uma identidade visual:

- Limpa.
- Moderna.
- Voltada para estudantes.
- Sem aparência excessivamente infantil.
- Com destaque para progresso e metas.

Cores principais

- Azul principal: "#2563EB"
- Azul secundário: "#1D4ED8"
- Destaque: "#F59E0B"

Ícone

O ícone deverá utilizar uma seta de crescimento integrada a um elemento relacionado aos estudos, representando evolução e progresso.

10.2 Publicação

O projeto deverá possuir:

- Nome Avança Jovem.
- Ícone definido.
- APK para testes.
- AAB para entrega/publicação conforme orientação do professor.
- README com instruções básicas de execução e uso.

---

11. Plano de testes

11.1 Testes funcionais

Serão testados:

- Criação de uma meta.
- Visualização da meta.
- Conclusão de uma meta.
- Registro da conclusão.
- Atualização dos pontos.
- Persistência dos dados.
- Visualização do progresso.
- Tratamento de erros.

11.2 Testes com usuários

Pelo menos 2 pessoas externas ao grupo deverão instalar e utilizar o APK.

Serão observados:

- Facilidade de uso.
- Clareza das informações.
- Funcionamento das principais ações.
- Possíveis dificuldades encontradas.

O feedback será registrado e utilizado para possíveis ajustes.

---

12. Cronograma

Marco| Data| Entrega
M1| 16/09/2026| Canvas + repositório
M2| 30/09/2026| PRD + telas
M3| 21/10/2026| Funcionalidade base
M4| 11/11/2026| Dados + tratamento de erros
M5| 25/11/2026| Visual + APK
M6| 02/12/2026| AAB + publicação + README
Final| 10/12/2026| Entrega final

---

13. Riscos

Risco| Mitigação
Conflitos entre alterações dos integrantes| Utilizar commits separados e manter o repositório atualizado.
Falta de espaço nos computadores| Utilizar o GitHub como repositório central e evitar arquivos desnecessários no projeto.
Erros na implementação do Room| Implementar e testar as entidades gradualmente.
Alterações feitas por IA causarem problemas| Revisar, testar e entender o código antes de aceitar alterações.
Falta de tempo para finalizar o projeto| Priorizar as funcionalidades Must do MVP.
Perda de dados locais| Manter o código e as alterações versionadas no GitHub.

---

14. Como vamos orientar a implementação com IA

A IA será utilizada como ferramenta de apoio ao desenvolvimento.

Será utilizada principalmente para:

- Explicar conceitos.
- Auxiliar na implementação.
- Identificar e corrigir erros.
- Revisar código.
- Auxiliar na documentação.
- Criar testes quando necessário.

As regras para utilização da IA serão documentadas em:

"docs/USO_DE_IA.md"

O projeto também possuirá:

"AGENTS.md"

com as regras de arquitetura, tecnologias e limites para alterações realizadas com auxílio de IA.

Nenhum código sugerido pela IA deverá ser utilizado sem que os integrantes responsáveis tenham lido, testado e entendido sua finalidade.

Não serão inseridas senhas, chaves de API ou outras informações privadas nos prompts.

---

15. Histórico de versões

Versão| Data| Alterações| Responsável
1.0| 20/09/2026| Criação inicial do PRD com requisitos, telas, dados, tecnologias e cronograma.| Rillary
1.1| 22/09/2026| Inclusão da entidade "Conclusao" e divisão das entidades entre os quatro integrantes para atender à atividade de commits individuais.| Rillary
