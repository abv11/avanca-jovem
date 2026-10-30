# Plano de implementação: integridade e ordenação da tabela de conclusões

## 1. Contexto do diagnóstico

O ajuste solicitado está diretamente ligado às entidades e ao DAO de conclusões do app:

- O arquivo de entidade atual é `src/app/src/main/java/br/edu/ifpe/avancajovem/data/local/entity/ConclusaoEntity.kt`.
- A entidade já define um índice simples em `metaId`, mas ele não é único e também não cobre a ordenação por `dataConclusao`.
- O DAO `src/app/src/main/java/br/edu/ifpe/avancajovem/data/local/dao/ConclusaoDao.kt` executa a consulta de histórico com `ORDER BY c.dataConclusao DESC`.
- O insert atual usa `OnConflictStrategy.REPLACE`, o que pode sobrescrever um registro existente quando houver conflito de chave/índice, inclusive alterando `id` e `pontosRecebidos` por engano.
- O banco é gerenciado pelo `AppDatabase`, em `src/app/src/main/java/br/edu/ifpe/avancajovem/data/local/AppDatabase.kt`, e hoje está em `version = 1` sem migração explícita.

## 2. Objetivo do plano

Implementar as três correções pedidas sem mudar o comportamento principal do MVP:

1. adicionar índice para `dataConclusao` para otimizar a ordenação do histórico;
2. tornar `metaId` único na entidade para impedir que a mesma meta seja concluída mais de uma vez;
3. trocar a estratégia de conflito de `REPLACE` para `ABORT` para evitar sobrescritas acidentais.

## 3. Diagnóstico específico do código atual

### 3.1. Entidade de conclusão

Na `ConclusaoEntity`, a definição atual é:

- `@Entity(...)`
- `indices = [Index(value = ["metaId"]) ]`

Essa configuração atende apenas a criação de um índice não único para `metaId`, sem bloquear duplicatas no nível do banco.

### 3.2. Consulta do histórico

No `ConclusaoDao`, a consulta de histórico usa:

- `ORDER BY c.dataConclusao DESC`

Essa ordenação funciona, mas pode ser mais lenta em tabelas maiores se `dataConclusao` não tiver um índice dedicado. Como o histórico depende da ordem por data, esse índice é diretamente útil para desempenho.

### 3.3. Inserção de conclusão

O DAO usa:

- `@Insert(onConflict = OnConflictStrategy.REPLACE)`

Essa estratégia sobrescreve registros conflitantes. Em um cenário de `metaId` duplicado, isso pode alterar a conclusão já gravada e até mexer no `id` do registro, o que contraria a regra de negócio do MVP.

## 4. Estratégia de implementação passo a passo

### Etapa 1: ajustar a entidade para índices corretos

Em `ConclusaoEntity.kt`:

- manter `metaId` como chave estrangeira e `id` como `@PrimaryKey(autoGenerate = true)`;
- atualizar o atributo `indices` para incluir:
  - `Index(value = ["metaId"], unique = true)`;
  - `Index(value = ["dataConclusao"])`.

Objetivo:

- garantir que o banco rejeite tentativas de inserir duas conclusões para a mesma meta;
- acelerar a ordenação do histórico por data de conclusão.

### Etapa 2: revisar a regra de conflito do DAO

Em `ConclusaoDao.kt`:

- trocar `OnConflictStrategy.REPLACE` por `OnConflictStrategy.ABORT`.

Esse ajuste faz o banco falhar explicitamente quando houver tentativa de inserir uma conclusão duplicada, evitando conflitos silenciosos de sobrescrita.

Observação importante:

- como o app já exige tratamento amigável de erros no acesso ao banco, o fluxo que chama esse insert precisa capturar a exceção do Room e mostrar a mensagem de fallback adequada, como `"Não foi possível salvar seu progresso. Tente novamente."`

### Etapa 3: preparar a migração do banco com limpeza preventiva

Como a estrutura do banco muda (índices e restrição de unicidade), o `AppDatabase` deve ser atualizado para refletir a nova versão do schema de forma segura para usuários que já possuem dados antigos.

Em `AppDatabase.kt`:

- aumentar `version` de `1` para `2`;
- adicionar uma `Migration(1, 2)`;
- garantir que a migração use comandos SQL puros, sem depender de lógica Kotlin de limpeza antes da criação do índice;
- incluir explicitamente os nomes dos índices no padrão do Room: `index_conclusao_metaId` (como `UNIQUE`) e `index_conclusao_dataConclusao`.

Estratégia segura da migração:

- primeiro limpar registros duplicados em `conclusao` para `metaId`; 
- depois criar o índice único em `conclusao(metaId)` com o nome `index_conclusao_metaId`;
- em seguida criar o índice em `conclusao(dataConclusao)` com o nome `index_conclusao_dataConclusao`.

A migração deve ser aplicada via `addMigrations(...)` no `Room.databaseBuilder`, mantendo a operação atômica e compatível com dispositivos já instalados.

### Etapa 4: eliminar duplicatas existentes dentro da própria migração

Antes de aplicar a restrição de unicidade, é necessário garantir que o banco não contenha mais de uma conclusão por `metaId`, porque a criação do índice único falharia em dispositivos com dados legados duplicados.

A abordagem recomendada é uma limpeza automatizada dentro da própria migração, executada antes da criação do índice único. A lógica conceitual a ser aplicada é a mesma da seguinte regra de remoção preventiva:

- deletar da tabela `conclusao` todos os registros cujo `id` não seja o maior `id` para cada `metaId`;
- manter apenas a conclusão mais recente por meta;
- essa regra evita perda de dados mais relevantes e garante que o índice único possa ser criado sem travar a atualização.

Em termos conceituais, a query planejada para a migração deve seguir a lógica de exclusão baseada no valor máximo do `id` por `metaId`, conforme a regra: `DELETE FROM conclusao WHERE id NOT IN (SELECT MAX(id) FROM conclusao GROUP BY metaId);`.

Essa etapa é essencial para evitar falhas na migração em instalações já existentes do app e proteger a atualização sem causar travamentos ou bloqueios de inicialização no banco local.

### Etapa 5: confirmar o impacto no histórico do app

A consulta atual de histórico em `ConclusaoDao` já está correta em termos de ordenação lógica:

- `SELECT ... FROM conclusao c INNER JOIN meta m ... ORDER BY c.dataConclusao DESC`

Depois do ajuste, a consulta continua igual, mas passa a se beneficiar do índice em `dataConclusao`, especialmente em tabelas com muitos registros.

### Etapa 6: validar comportamento esperado em execução

Checklist de verificação da implementação:

- uma meta não pode ter duas conclusões gravadas no banco;
- ao tentar inserir uma segunda conclusão para a mesma meta, o app não sobrescreve a anterior;
- a operação falha com erro controlado e exibe mensagem amigável;
- a listagem do histórico continua ordenada da conclusão mais recente para a mais antiga;
- o desempenho do histórico melhora em bases maiores sem alterar a UI.

## 5. Arquivos necessariamente envolvidos

- `src/app/src/main/java/br/edu/ifpe/avancajovem/data/local/entity/ConclusaoEntity.kt`
- `src/app/src/main/java/br/edu/ifpe/avancajovem/data/local/dao/ConclusaoDao.kt`
- `src/app/src/main/java/br/edu/ifpe/avancajovem/data/local/AppDatabase.kt`

## 6. Ordem recomendada de implementação

1. ajustar `ConclusaoEntity` com os índices corretos;
2. alterar `ConclusaoDao` para `ABORT`;
3. criar a `Migration(1, 2)` no `AppDatabase`;
4. tratar duplicados existentes para permitir a migração com sucesso;
5. validar histórico e comportamento de inserção manual em banco;
6. confirmar que a UX continua exibindo o histórico corretamente sem sobrescrita de pontos ou IDs.

## 7. Conclusão

O projeto já está muito perto do ajuste pedido. O principal ponto é que a entidade e o DAO precisam refletir as regras de negócio do banco de forma explícita: unicidade por `metaId`, índice por data para o histórico e rejeição de conflitos sem sobrescrita. A migração do Room é o ponto central para que essas regras sejam aplicadas sem quebrar dados existentes.
