# Planejamento — Autenticação Google e sincronização Firebase

## 1. Resumo do estado atual do projeto

O projeto Avança Jovem é um aplicativo Android em Kotlin que utiliza Jetpack Compose, Navigation Compose, ViewModels, Room e uma arquitetura organizada em camadas:

- `ui`: telas, componentes, navegação e ViewModels;
- `data.local`: banco Room, entidades e DAOs;
- `data.repository`: coordenação das operações de dados;
- `data.remote`: serviço Retrofit placeholder;
- `model`: modelos usados pela interface e pelo domínio.

A aplicação já possui persistência local em um banco Room denominado `avancajovem_database`, na versão 2. Os dados atuais são:

- estudante;
- metas;
- conclusões;
- recompensas.

Não foram encontrados no projeto:

- Firebase Authentication;
- Cloud Firestore;
- SDK ou dependências Firebase;
- arquivo `google-services.json`;
- tela ou ViewModel de autenticação;
- mecanismo atual de troca de usuário;
- DataStore ou SharedPreferences usados para dados da aplicação.

O `AvancaJovemApiService` é apenas um placeholder Retrofit e não realiza persistência remota no fluxo atual.

## 2. Objetivo da implementação

O objetivo é adicionar autenticação com conta Google sem substituir a arquitetura ou remover o armazenamento local.

A implementação deverá:

1. identificar o usuário pelo UID do Firebase Authentication;
2. proteger os dados no Firestore pelo mesmo UID;
3. manter o funcionamento offline por meio do Room;
4. sincronizar dados locais e remotos;
5. migrar dados existentes no primeiro login;
6. recuperar dados em outro dispositivo;
7. isolar os dados ao trocar de conta;
8. preservar metas, conclusões, pontos, recompensas e histórico.

A funcionalidade deverá ser integrada ao fluxo atual, mantendo as telas principais, a navegação Compose e as operações já disponíveis.

## 3. Análise dos arquivos existentes envolvidos

### 3.1 Inicialização e navegação

- `MainActivity.kt` cria o banco Room e o repositório, depois fornece o repositório ao host de navegação.
- `AppNavHost.kt` inicia diretamente em `NavTarget.Home` e cria os ViewModels com o repositório compartilhado.
- `NavTarget.kt` contém as rotas atuais para Home, criação/detalhe de meta, histórico e recompensas.

A implementação precisará definir uma rota ou estado anterior ao conteúdo principal para permitir a autenticação persistida.

### 3.2 Persistência local

- `AppDatabase.kt` configura Room e inicia os dados padrão.
- `EstudanteEntity.kt` representa o estudante com ID fixo `1L`.
- `MetaEntity.kt` representa título, descrição, pontos e conclusão.
- `ConclusaoEntity.kt` registra meta concluída, data e pontos recebidos.
- `RecompensaEntity.kt` representa o catálogo de recompensas.

O banco cria automaticamente:

- um estudante padrão;
- cinco recompensas iniciais;
- duas metas iniciais, caso não existam metas.

Esses dados iniciais não devem ser confundidos automaticamente com dados pessoais recém-criados pelo usuário.

### 3.3 DAOs e regras locais

- `EstudanteDao.kt` consulta e atualiza o estudante pelo ID `1L`.
- `MetaDao.kt` controla consulta, criação, atualização e exclusão de metas.
- `ConclusaoDao.kt` mantém uma conclusão única por meta.
- `RecompensaDao.kt` consulta o catálogo e permite inserir recompensas.

A regra crítica de conclusão já impede que uma meta gere pontos duas vezes no fluxo local atual. Essa regra deve permanecer em uma operação equivalente no processo sincronizado.

### 3.4 Repositório

`AvancaJovemRepository.kt` é o principal ponto de integração da nuvem. Ele já expõe `Flow` para estudante, metas, recompensas e histórico, além de métodos para criar, concluir e excluir metas.

O repositório poderá continuar como camada única utilizada pelos ViewModels, desde que passe a coordenar a fonte local e remota. Os ViewModels não precisam conhecer o Firestore diretamente.

### 3.5 Modelos

- `Estudante`: ID, nome e pontos.
- `Meta`: ID, título, descrição, pontos e estado de conclusão.
- `Conclusao`: ID, referência da meta, data e pontos recebidos.
- `Recompensa`: ID, nome, descrição e pontos necessários.
- `HistoricoItem`: representação derivada das conclusões e metas.

Os IDs do Room não são adequados como identidade global em múltiplos dispositivos. Antes da sincronização, metas e conclusões precisam receber identificadores estáveis compatíveis entre dispositivos.

## 4. Tecnologias e serviços necessários

### 4.1 Firebase Authentication

Será utilizado o SDK Firebase Authentication para:

- login com Google;
- persistência da sessão pelo Firebase;
- obtenção do UID;
- recuperação da sessão na inicialização;
- logout;
- tratamento de cancelamentos e erros de autenticação.

A sessão do Firebase não será considerada suficiente para recuperar os dados: o aplicativo deverá confirmar que os documentos do UID existem ou concluir a migração inicial.

### 4.2 Cloud Firestore

O Firestore será usado como armazenamento remoto por UID. O aplicativo deverá habilitar persistência local do SDK para continuar recebendo alterações quando a internet estiver temporariamente indisponível.

Em operações que já existem no aplicativo, o repositório continuará retornando dados locais imediatamente e depois aplicará as alterações confirmadas pelo Firestore.

### 4.3 Firebase Security Rules

As regras deverão permitir acesso somente ao nó do usuário autenticado. Todas as operações para outro UID deverão ser negadas. As regras serão testadas com usuários simulados autenticados e não autenticados.

## 5. Plano de configuração do Firebase

Antes da implementação, será necessário:

1. criar o projeto Firebase associado ao aplicativo;
2. registrar o application ID Android real;
3. habilitar Google como provedor de autenticação;
4. cadastrar certificados SHA-1 e SHA-256 no Firebase;
5. obter o arquivo de configuração `google-services.json` e colocá-lo no módulo Android;
6. habilitar o Cloud Firestore;
7. configurar persistência offline do SDK;
8. publicar inicialmente apenas regras restritas;
9. testar login, leitura, gravação e negação de acesso entre UIDs.

Há uma inconsistência que deve ser corrigida antes do registro Firebase:

- a documentação indica `br.edu.ifpe.avancajovem`;
- parte do código utiliza `br.edu.ifpe.avancajovem`;
- `src/app/build.gradle.kts` utiliza `com.example.avanca_jovem` como namespace e application ID;
- o teste instrumentado espera `br.edu.ifpe.avancajovem`.

A aplicação deverá possuir um único application ID consistente entre Gradle, pacote Android, Firebase Console e testes.

## 6. Planejamento da implementação do login com Google

### 6.1 Tela de login

Será criada uma tela Compose responsável por:

- explicar que o login usa conta Google;
- iniciar o fluxo de autenticação;
- mostrar carregamento;
- mostrar mensagens amigáveis de cancelamento, falha de rede e erro inesperado;
- navegar para o conteúdo principal somente após autenticação confirmada;
- oferecer um botão de sair da conta.

Não será necessário armazenar senha ou criar cadastro próprio.

### 6.2 Fluxo de autenticação

Na inicialização:

1. verificar o usuário atual no Firebase Authentication;
2. se não houver sessão, apresentar a tela de login;
3. se houver sessão, recuperar o UID;
4. validar o estado de sincronização desse UID;
5. executar migração ou recuperação conforme necessário;
6. iniciar a navegação principal somente após o estado mínimo necessário estar disponível;
7. manter o usuário autenticado entre aberturas quando a sessão Firebase continuar válida.

### 6.3 Logout

No logout:

1. cancelar sincronizações e observadores ativos;
2. encerrar a sessão Firebase;
3. limpar o estado das telas em memória;
4. impedir a troca silenciosa de dados locais entre contas;
5. manter os dados locais da conta anterior enquanto não estiver definido que devem ser excluídos ou enviados a outra conta;
6. retornar à tela de login.

## 7. Estrutura de armazenamento dos dados por usuário

Estrutura conceitual recomendada:

- `users/{uid}`: perfil resumido e metadados de sincronização;
- `users/{uid}/metas/{metaId}`: metas do usuário;
- `users/{uid}/conclusoes/{conclusaoId}`: conclusões do usuário;
- `users/{uid}/sync/migration`: estado e marcador da migração;
- `users/{uid}/sync/pending/{operationId}`: operações pendentes, quando forem armazenadas no Firestore.

A coleção `recompensas` não precisa ser pessoal para os dados já distribuídos pelo aplicativo. O catálogo poderá continuar local. A informação pessoal relevante é somente se o usuário conquistou ou resgatou algo e essa regra não existe atualmente no modelo.

Um documento recomendado para o perfil deverá conter:

- UID;
- nome exibido, apenas se necessário;
- total de pontos calculado;
- data da última alteração;
- versão dos dados;
- estado da migração.

Os pontos não deverão ser armazenados sem uma forma confiável de recomputação. A fonte preferencial será a soma das conclusões únicas confirmadas.

## 8. Planejamento da migração dos dados locais existentes

### 8.1 Inventário migrado

Dados que podem ser migrados:

- estudante e seus pontos;
- metas e seu estado concluída;
- conclusões e respectivos registros de pontos;
- associações entre conclusões e metas.

Dados que podem não precisar ser migrados:

- recompensas iniciais, consideradas catálogo compartilhado;
- identificadores locais de Room;
- dados derivados, como histórico e progresso, que podem ser reconstruídos;
- metadados internos do Room.

### 8.2 Estratégia segura

A migração seguirá estas etapas:

1. carregar todos os dados existentes do Room em uma operação controlada;
2. gerar identificadores estáveis para metas sem UUID;
3. mapear conclusões aos mesmos identificadores das metas;
4. criar uma cópia em memória antes de alterar o banco;
5. verificar se o usuário já possui dados no Firestore;
6. classificar os dados locais e remotos em conjuntos novos, iguais ou conflitantes;
7. enviar o conjunto combinado por lote ou transação do Firestore;
8. aguardar confirmação individual das operações;
9. atualizar o Room com os dados finais somente após confirmação segura;
10. marcar a migração como concluída;
11. manter os dados locais até a conclusão integral.

### 8.3 Identificação da migração

Será criado um estado persistido por UID, contendo:

- usuário associado;
- fase da migração;
- quantidade de itens encontrados;
- quantidade enviada;
- data de início;
- data de confirmação;
- versão da migração;
- último erro conhecido.

Não se deve considerar concluída uma migração apenas porque uma solicitação foi iniciada. O estado será atualizado somente após confirmação de que todos os itens críticos foram persistidos.

## 9. Integração com telas, modelos e repositórios

### 9.1 MainActivity e navegação

`MainActivity.kt` deverá criar o repositório com acesso aos fontes local e remoto e exibir uma árvore de autenticação antes do `AppNavHost` principal.

### 9.2 Repositório

`AvancaJovemRepository.kt` será responsável por:

- obter o UID ativo;
- verificar dados remotos;
- migrar dados locais;
- observar alterações locais;
- enviar alterações ao Firestore;
- baixar dados remotos;
- resolver conflitos;
- atualizar o Room;
- manter operações pendentes.

A interface não deverá ler diretamente o Firestore.

### 9.3 ViewModels

Os ViewModels existentes deverão continuar recebendo `Flow` do repositório local. A criação, conclusão e exclusão seguirão a semântica atual:

- `CriarMetaViewModel` continua criando uma meta pelo repositório;
- `DetalheMetaViewModel` continua concluindo ou excluindo pelo repositório;
- `HomeViewModel` continua observando estudante e metas;
- `HistoricoViewModel` continua observando o histórico local;
- `RecompensasViewModel` continua observando estudante e recompensas.

Um novo ViewModel deverá controlar exclusivamente autenticação e migração. Os ViewModels de conteúdo serão reiniciados após troca de conta ou login.

## 10. Planejamento da sincronização entre local e nuvem

A sincronização usará o Room como cache operacional e o Firestore como fonte remota autoritativa após login.

### 10.1 Operações locais

Ao criar, concluir ou excluir uma meta:

1. atualizar o Room imediatamente;
2. registrar a operação em uma fila local;
3. enviar a operação ao Firestore quando houver conexão;
4. aguardar confirmação;
5. marcar a operação como sincronizada;
6. atualizar o documento remoto da forma correspondente;
7. manter a operação local em caso de falha.

### 10.2 Recuperação remota

Ao abrir a aplicação:

1. observar o usuário autenticado;
2. ler o estado de migração;
3. consultar o perfil do UID;
4. sincronizar metas e conclusões;
5. atualizar o Room;
6. iniciar observadores dos ViewModels.

O SDK Firestore poderá fornecer cache local. Os dados locais do Room permanecem disponíveis mesmo sem rede.

### 10.3 Sincronização em segundo plano

Não é necessário implementar notificações ou sincronização em um serviço contínuo nesta etapa. A sincronização pode ser iniciada:

- após login;
- após o repositório detectar alteração;
- ao retornar internet;
- ao reabrir o aplicativo.

Deve haver um único processo de sincronização ativo por UID para evitar operações duplicadas.

## 11. Estratégia para evitar perda, duplicação e inconsistência

### 11.1 Identificadores estáveis

Metas e conclusões devem receber UUID persistido no Room e no Firestore. Após migração, o identificador estável não poderá ser alterado.

### 11.2 Conclusões únicas

Uma conclusão será identificada pelo identificador da meta. O Firestore não deverá permitir que o mesmo metaId gere duas conclusões independentes. A soma de pontos será recalculada pela lista única de conclusões.

### 11.3 Confiança nos timestamps

Os documentos terão `createdAt`, `updatedAt` e uma versão ou token de alteração. Para conflitos simples, a alteração mais recente validada poderá prevalecer. Para estudante e conclusão, a integridade histórica prevalecerá sobre uma soma manual de pontos.

### 11.4 Exclusão

A exclusão de uma meta não deverá remover imediatamente os dados locais antes da confirmação remota. Um marcador de exclusão será mantido até o Firestore confirmar a operação.

### 11.5 Estados pendentes

Toda operação remota terá estado:

- pendente;
- enviada;
- confirmada;
- erro;
- desfeito pelo usuário, quando aplicável.

Somente o estado confirmado fará parte da migração concluída.

## 12. Planejamento da recuperação em outro dispositivo

Em um novo dispositivo:

1. o usuário fará login com a mesma conta Google;
2. não será utilizada migração automática dos dados locais de exemplo;
3. o aplicativo verificará se o UID possui perfil e dados no Firestore;
4. os documentos remotos serão recuperados;
5. os identificadores estáveis serão utilizados para preencher o Room;
6. a interface exibirá metas, conclusões e pontos recuperados;
7. alterações posteriores serão sincronizadas normalmente.

Não se deve depender do celular antigo. Uma migração local é necessária apenas no dispositivo que possui dados anteriores à integração Firebase.

## 13. Planejamento do funcionamento offline

O Room continua sendo usado como armazenamento funcional quando não houver internet.

A solução deverá:

- permitir criar, concluir e excluir metas offline;
- manter o histórico e os pontos disponíveis localmente;
- registrar operações pendentes;
- aguardar reconexão antes de enviar ao Firestore;
- mostrar mensagens claras de indisponibilidade sem apagar dados;
- não iniciar uma segunda operação após já haver uma pendente com o mesmo identificador;
- evitar marcar migração concluída enquanto houver itens sem confirmação.

O cache offline do Firestore complementará o Room, mas não substituirá o banco local já existente.

## 14. Planejamento das regras de segurança do Firestore

Cada usuário só poderá acessar o caminho `users/{uid}` correspondente ao UID da sessão autenticada.

Regras esperadas deverão garantir:

- usuário não autenticado não lê ou grava dados;
- o usuário só lê documentos cujo UID corresponde à sua sessão;
- o usuário só cria documentos associados ao seu próprio UID;
- o usuário não altera o UID proprietário de outro usuário;
- o usuário não consulta ou exclui dados de outra conta;
- o perfil, metas e conclusões são protegidos por escopo;
- campos críticos possuam tipos e formatos válidos;
- leitura e escrita sejam testadas separadamente.

As regras não deverão permitir acesso global à coleção de usuários. Antes da publicação, deverão ser realizados testes para:

- login correto;
- login com UID diferente;
- acesso sem autenticação;
- tentativa de leitura de documento alheio;
- tentativa de alteração de documentos alheios;
- criação válida no próprio UID;
- estrutura inválida ou campo proprietário incorreto.

## 15. Planejamento do tratamento de erros e da autenticação persistente

### 15.1 Login

- cancelamento pelo usuário: cancelar o carregamento sem exibir falha grave;
- rede indisponível: informar que o login precisa de conexão;
- conta cancelada ou rejeitada: informar que não foi possível entrar;
- erro Firebase: apresentar mensagem amigável e permitir nova tentativa;
- falha inesperada: não encerrar o aplicativo e manter o estado seguro.

### 15.2 Persistência da sessão

A sessão será recuperada pelo Firebase Authentication. O aplicativo verificará o UID ao iniciar e navegará diretamente ao conteúdo quando houver sessão válida e dados disponíveis.

### 15.3 Banco e sincronização

Seguindo as diretrizes do projeto, operações de banco deverão utilizar tratamento de erros. O texto amigável para falha de gravação deverá ser:

> “Não foi possível salvar seu progresso. Tente novamente.”

Outros estados poderão usar mensagens específicas para login, carregamento, migração e sincronização offline.

## 16. Lista dos arquivos existentes que precisarão ser modificados

Provável alteração:

- `src/app/build.gradle.kts` — plugins, dependências e consistência do application ID;
- `src/gradle/libs.versions.toml` — versões e aliases das bibliotecas Firebase;
- `src/build.gradle.kts` — plugin do Google Services;
- `src/settings.gradle.kts` — repositório Google, caso ainda não esteja disponível;
- `src/app/src/main/AndroidManifest.xml` — configuração necessária do aplicativo, se houver;
- `src/app/src/main/java/br/edu/ifpe/avancajovem/MainActivity.kt` — inicialização da autenticação e repositório;
- `ui/navigation/AppNavHost.kt` — rota de login e reinicialização após troca de conta;
- `ui/navigation/NavTarget.kt` — destino de autenticação, se necessário;
- `data/repository/AvancaJovemRepository.kt` — coordenação de Room, Firebase e migração;
- `data/local/AppDatabase.kt` — tabelas ou migrações locais necessárias;
- entidades e DAOs envolvidos em sincronização;
- `HistoricoViewModel.kt` — eventual adaptação para dados isolados por conta;
- `RecompensasViewModel.kt` — eventual adaptação para dados isolados por conta;
- `HomeViewModel.kt`, `CriarMetaViewModel.kt` e `DetalheMetaViewModel.kt` — reinício ou recuperação de dados por UID, apenas se necessário;
- testes unitários e instrumentados — cenários Firebase, migração e isolamento.

O `AvancaJovemApiService.kt` não precisa ser convertido em serviço Firestore e pode continuar como placeholder REST não utilizado.

## 17. Novos arquivos possíveis

Arquivos possíveis e sua justificativa:

- tela Compose de login;
- ViewModel de autenticação;
- ViewModel ou coordenador de migração;
- classe responsável pelo Firebase Authentication;
- classe responsável pelo Firebase Firestore;
- objeto ou repositório remoto;
- modelos Firestore internos;
- gerenciador de fila de sincronização;
- entidade Room para operações pendentes;
- DAO para operações pendentes;
- migração Room para novos campos e tabelas;
- arquivo de regras do Firebase;
- testes unitários para comparação e resolução de conflitos;
- testes instrumentados para login, migração e troca de conta.

Cada arquivo novo deverá ter responsabilidade única e comentário de limite adequado às convenções existentes.

## 18. Ordem recomendada das etapas

1. Corrigir a inconsistência do application ID.
2. Cadastrar e configurar o projeto Firebase.
3. Adicionar dependências Firebase sem alterar funcionalidades existentes.
4. Criar uma fonte local de configuração de autenticação.
5. Implementar o fluxo básico de login Google e persistência de sessão.
6. Criar a tela de login e o estado de autenticação.
7. Integrar uma rota de login à navegação atual.
8. Criar a estrutura conceitual do Firestore por UID.
9. Adicionar extensão estável de IDs no Room para metas e conclusões.
10. Criar regras Firestore restritas por UID.
11. Testar as regras antes da migração real.
12. Adicionar operações remotas ao repositório.
13. Implementar uma fila de sincronização local.
14. Implementar migração segura dos dados locais existentes.
15. Implementar combinação de dados locais e remotos.
16. Implementar conflito e exclusão com tombstones.
17. Integrar recuperação em novo dispositivo.
18. Isolar dados ao trocar de conta.
19. Testar funcionamento offline e reconexão.
20. Executar lint, testes locais e validação manual.

## 19. Critérios para verificar se a implementação funcionará corretamente

### Autenticação

- o usuário entra com conta Google;
- a sessão permanece após fechar e reabrir;
- logout encerra o acesso ao UID anterior;
- falhas de login não encerram o aplicativo;
- o UID é exibido ou registrado apenas internamente conforme necessário.

### Dados individuais

- cada conta lê somente dados do próprio UID;
- um usuário não lê ou altera dados de outro usuário;
- dados locais não vazam para uma nova conta;
- metas, conclusões e pontos são associados à conta correta.

### Migração

- dados existentes são enviados ao UID correto;
- uma migração não é marcada como concluída antes da confirmação;
- uma migração não é enviada repetidamente;
- dados locais não são excluídos antes do sucesso;
- dados locais e remotos não são duplicados.

### Sincronização

- criar, concluir e excluir metas atualiza o Room e o Firestore;
- concluir uma meta não concede pontos duas vezes;
- operações offline ficam pendentes;
- reconexão envia as operações pendentes;
- exclusões não reaparecem indevidamente;
- histórico mantém as conclusões únicas.

### Recuperação

- login no segundo dispositivo recupera os dados;
- o celular antigo não é necessário;
- a reinstalação não perde dados remotos;
- alterações no novo dispositivo aparecem no antigo após sincronização.

### Segurança

- usuário não autenticado não acessa dados;
- tentativas de acessar outro UID são negadas;
- regras são validadas em ambiente controlado;
- configuração de teste não permite acesso público irrestrito.

## 20. Riscos, dependências e possíveis problemas

### Riscos técnicos

- IDs locais podem colidir ou mudar entre dispositivos.
- A migração pode sobrescrever dados remotos existentes.
- Dados locais podem ser enviados automaticamente para a conta errada.
- Conclusões duplicadas podem conceder pontos repetidos.
- Falhas de rede podem gerar estado pendente incorreto.
- A rede pode cair entre a confirmação local e a atualização remota.
- Tarefas simultâneas podem executar sincronização duas vezes.
- Dados iniciais criados pelo seed podem ser confundidos com dados pessoais.

### Dependências externas

- projeto Firebase válido;
- conta Google configurada;
- certificado de assinatura correto;
- acesso à internet no primeiro login;
- disponibilidade do Firestore;
- configuração correta de `google-services.json`.

### Limitações identificadas

- Não foi encontrado um Firebase configurado no workspace.
- Não existe autenticação atual no projeto.
- Não foi possível analisar regras Firebase ou configuração remota porque não existem arquivos correspondentes.
- Não é possível confirmar a relação real entre recompensas e estudante, pois o modelo atual não possui resgate persistido.
- A quantidade exata de dados locais existentes depende do histórico do usuário no dispositivo.
- O application ID atual exige confirmação antes do registro no Firebase.

### Recomendação final

Preservar o Room como camada de cache e operação offline, manter o repositório como ponto central e migrar os dados somente após confirmar o UID e a integridade dos registros. Não excluir dados locais em nenhuma etapa antes da confirmação completa do envio ao Firestore.
