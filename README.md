
# Avança Jovem

Aplicativo Android desenvolvido para ajudar estudantes de 12 a 18 anos a manter o foco e participar das atividades escolares por meio de metas simples, acompanhamento de progresso e pequenas recompensas.

## 🎯 Objetivo

O **Avança Jovem** busca ajudar estudantes a organizar suas atividades, acompanhar seu progresso e receber pontos ao concluir metas.

### Público-alvo

Estudantes de **12 a 18 anos**, principalmente do Ensino Fundamental II e Ensino Médio.

## 🚀 Funcionalidades

- **F1 — Criar e visualizar metas:** cadastro e visualização de metas de estudo e atenção.
- **F2 — Concluir metas:** permite marcar uma meta como concluída e registrar o progresso.
- **F3 — Pontos e recompensas:** o estudante recebe pontos ao concluir suas metas.
- **F4 — Histórico de progresso:** permite consultar registros anteriores de progresso.

### Fluxo principal

**Meta → Conclusão → Pontos → Progresso**

## 🛠️ Tecnologias

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Navigation Compose
- Room
- KSP
- Coroutines
- Flow

O aplicativo utiliza o **Room** para armazenamento local e funciona de forma offline.

## 📁 Estrutura do projeto


app/
└── src/main/java/br/edu/ifpe/avancajovem/
    ├── data/
    │   ├── local/
    │   ├── remote/
    │   └── repository/
    ├── model/
    └── ui/
        ├── features/
        ├── navigation/
        └── theme/


## 🗃️ Entidades

* **Estudante**
* **Meta**
* **Recompensa**
* **Conclusão**

## 🎨 Identidade visual

* Cor principal: `#2563EB`
* Cor escura: `#1D4ED8`
* Cor de destaque: `#F59E0B`
* Ícone: seta para cima integrada a um elemento relacionado aos estudos.

A interface deve transmitir **progresso, organização e incentivo**, evitando uma aparência infantil.

## ⚠️ Tratamento de erros

As operações de persistência utilizando Room devem possuir tratamento de erros com `try/catch`.

Em caso de falha, será exibida uma mensagem amigável:

> Não foi possível salvar seu progresso. Tente novamente.

## 🤖 Uso de Inteligência Artificial

A Inteligência Artificial é utilizada como ferramenta de apoio ao desenvolvimento, principalmente para:

* criação e revisão de código;
* documentação;
* organização do projeto;
* esclarecimento de dúvidas;
* apoio na identificação de erros.

Todo código gerado por IA deve ser analisado, testado e compreendido pelos integrantes do grupo.

As regras para utilização de IA estão documentadas no arquivo [`AGENTS.md`](AGENTS.md) e no documento [`docs/USO_DE_IA.md`](docs/USO_DE_IA.md).

## 👥 Equipe

* **Angélica** — Desenvolvimento, telas e integração.
* **Laura** — Desenvolvimento, dados, Room e persistência.
* **Luiz** — Desenvolvimento, lógica, pontos e progresso.
* **Rillary** — Design, documentação, testes e organização da entrega.

Todos os integrantes participam do desenvolvimento do projeto.

## 🚫 Fora do escopo

O aplicativo não possui:

* Chat entre alunos e professores;
* Mensagens ou notificações push;
* Login ou cadastro online;
* Sincronização em nuvem;
* Sistemas de punição ou exposição de estudantes;
* Avaliação de atenção por câmera ou microfone;
* Pagamentos ou compras reais.

## 📌 Informações do projeto

**Nome:** Avança Jovem
**Application ID:** `br.edu.ifpe.avancajovem`
**Versão:** 1.0
**Plataforma:** Android
**Armazenamento:** Room (local)
**Repositório:** GitHub

## 📚 Documentação

* [`CANVAS.md`](CANVAS.md)
* [`PRD.md`](PRD.md)
* [`AGENTS.md`](AGENTS.md)
* [`docs/USO_DE_IA.md`](docs/USO_DE_IA.md)
* [`RUBRICA.md`](RUBRICA.md)

```
```
