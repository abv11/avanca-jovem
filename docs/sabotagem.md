# Relatório de Testes de Sabotagem — Avança Jovem

**Atividade:** P5 — Os Quatro Estados da Tela  
**Disciplina:** Linguagem e Técnicas de Programação II  
**Projeto:** Avança Jovem

## 1. Teste de lista vazia

**Sabotagem realizada:** Remoção de todas as metas cadastradas no aplicativo.

**Resultado observado:**  
O aplicativo funcionou conforme o esperado, apresentando comportamento adequado diante da ausência de metas cadastradas.

**Comportamento esperado:** O aplicativo deve exibir o estado Vazio, informando que não existem metas cadastradas e oferecendo a opção de adicionar a primeira meta.

**Status:** [Aprovado]

## 2. Teste sem conexão com a internet

**Sabotagem realizada:** Ativação do modo avião durante o uso do aplicativo.

**Resultado observado:**  
A aplicação continuou funcionando normalmente, sem apresentar uma mensagem específica de erro relacionada à falta de conexão.

**Comportamento esperado:** O aplicativo deve exibir o estado Erro, com uma mensagem amigável e o botão Tentar novamente, sem apresentar uma tela branca ou fechar inesperadamente.

**Status:** [Reprovado]

## 3. Teste de carregamento

**Sabotagem realizada:** Simulação de demora no carregamento dos dados.

**Resultado observado:**  
O aplicativo não apresentou demora ou lentidão em sua execução. Não foi possível observar claramente o estado Carregando durante o uso.

**Comportamento esperado:** O aplicativo deve exibir o estado Carregando enquanto os dados estão sendo buscados.

**Status:** [Reprovado]

## 4. Teste de campo obrigatório vazio

**Sabotagem realizada:** Tentativa de cadastrar uma meta sem preencher os campos obrigatórios.

**Resultado observado:**  
O aplicativo impediu o cadastro e exibiu uma caixa de mensagem informando o que deveria ser feito.

**Comportamento esperado:** O aplicativo deve impedir o cadastro inválido e informar ao usuário que os campos obrigatórios precisam ser preenchidos.

**Status:** [Aprovado]

## 5. Teste de erro no servidor

**Sabotagem realizada:** Tentativa de simular uma falha na comunicação com o servidor ou no carregamento dos dados.

**Resultado observado:**  
Não foi possível concluir o teste, pois o aplicativo apresentou problemas durante a instalação. Dessa forma, não foi possível verificar o comportamento da tela diante de uma falha no servidor.

**Comportamento esperado:** O aplicativo deve apresentar o estado Erro, sem encerrar sua execução, permitindo que o usuário tente carregar os dados novamente.

**Status:** [Não realizado]

## Conclusão

Os testes de sabotagem foram realizados com o objetivo de verificar o comportamento do aplicativo Avança Jovem diante de situações inesperadas.

Durante os testes, foi possível observar que o aplicativo apresentou comportamento adequado nos testes de lista vazia e de campo obrigatório vazio. Entretanto, nos testes de ausência de conexão e carregamento, não foi possível verificar completamente os comportamentos esperados.

O teste de erro no servidor não pôde ser concluído devido a problemas de instalação do aplicativo.

**Resultado final:** 2 testes aprovados, 2 testes reprovados e 1 teste não realizado.