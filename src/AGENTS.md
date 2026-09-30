# Diretrizes para IAs e Agentes no Projeto

Este arquivo serve como contexto persistente para ferramentas de IA (como o Gemini no Android Studio). Siga estas diretrizes estritamente em todas as tarefas deste repositório para manter a organização do grupo.

## Visão Geral do Projeto
- **Nome:** Avança Jovem
- **Descrição:** Aplicativo Android para ajudar estudantes de 12 a 18 anos a manter o foco e participar mais das atividades escolares por meio de metas simples, acompanhamento de progresso e pequenas recompensas.
- **Stack Principal:** Kotlin, Android Studio, Jetpack Compose e Room (Banco de dados local)
- **Application ID:** br.edu.ifpe.avancajovem

## Comandos do Projeto (Gradle)
Sempre use estes comandos exatos para gerenciar o projeto. Não utilize outros scripts ou caminhos alternativos.
- **Sincronizar dependências:** `./gradlew build --refresh-dependencies`
- **Gerar build de teste:** `./gradlew assembleDebug`
- **Rodar os testes locais:** `./gradlew test`
- **Verificar erros e lint:** `./gradlew lint`

## Escopo do MVP (Funcionalidades Permitidas)
O agente deve se limitar a construir ou dar suporte apenas às seguintes funcionalidades planejadas:
- **F1:** Criar e visualizar metas de estudo e atenção.
- **F2:** Marcar uma meta como concluída e atualizar o progresso.
- **F3:** Sistema simples de pontos e recompensas.
- **F4:** Histórico de progresso do estudante (opcional).

## Fora de Escopo (O que NÃO implementar)
Não adicione código ou dependências para as seguintes funções:
- Chat entre alunos e professores.
- Sistema de mensagens ou notificações push.
- Cadastro ou login com contas online (o app deve ser 100% offline).
- Sincronização em nuvem.
- Sistemas de punição ou exposição de alunos.
- Avaliação automática da atenção por meio de câmera ou microfone.
- Pagamentos ou compras reais.

## Padrões de Código e Arquitetura
- **Interface:** Desenvolvida inteiramente em Jetpack Compose. A interface deve ser limpa, sem poluição visual e adequada para o público-alvo (evitar visual infantil).
- **Persistência:** Uso exclusivo da biblioteca Room para o armazenamento local de metas, pontos e progresso.
- **Tratamento de Erros:** É obrigatório o uso de blocos `try/catch` nas operações de acesso ao banco (carregar, inserir ou atualizar dados). Caso ocorra uma falha, o app não deve fechar. Trate o erro exibindo uma mensagem amigável na tela: *"Não foi possível salvar seu progresso. Tente novamente."*

## Instruções de Fluxo de Trabalho
- Entenda a estrutura existente do projeto antes de realizar qualquer alteração.
- Modifique somente os arquivos necessários para executar a tarefa solicitada.
- Preserve a organização de pastas existente no projeto. Não reorganize arquivos por iniciativa própria.
- Priorize soluções simples, legíveis e adequadas ao nível do projeto.
- Ao finalizar, forneça um resumo curto das alterações feitas.

## Código Existente e Dependências
- Não refatore ou reescreva códigos existentes que já estejam funcionando por preferência pessoal.
- Não remova funcionalidades existentes sem autorização.
- Novas dependências só devem ser adicionadas se forem estritamente necessárias para a tarefa. Informe ao grupo qual biblioteca foi adicionada e o motivo.
- Nunca inclua chaves de API, senhas ou tokens diretamente no código.

## Erros Fora do Escopo e Testes
- Se encontrar erros que não estejam relacionados à tarefa atual, não tente corrigi-los sozinho. Informe o grupo.
- O agente não precisa executar testes obrigatoriamente. O integrante responsável pelo projeto fará a validação manual das alterações.

## Comentários de Fronteira e Autorização
- Arquivos novos devem seguir o padrão de comentário de fronteira exigido pelo projeto. Como não há um modelo específico, não invente um formato próprio.
- Não realize alterações fora do escopo solicitado sem autorização explícita do grupo. Na dúvida, aguarde orientação.
