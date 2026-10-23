---
name: 'Revisão adversária da espinha de arquitetura do DEZI novo (rodada 6)'
target: architecture-dezi-novo.md (AD-1 a AD-30, status draft, atualizada em 2026-10-21)
method: verificação do fechamento de R5-A1..A4 e R5-M1..M5 contra o texto atual; grafo do diagrama conferido contra portas e dependências dos ADs; busca de contradições e ciclos introduzidos pelas correções
created: '2026-10-21'
supersedes: review-adversario-r5.md
---

# Revisão adversária, rodada 6

## Veredito: NÃO PASSA

Contagem: 0 críticos, 1 alto, 6 médios, 3 baixos. Antigos só entram quando bloqueantes.

O grafo está limpo (DAG, sem ciclo novo). Três dos quatro altos da r5 fecharam de fato (A1, A3, A4). O quarto (A2) fechou pela metade e deixou uma contradição interna no AD-28 que impede `infra`, `comunicacao` e `resultados` de trabalharem em paralelo.

## Fechamento de R5-A1..A4 e R5-M1..M5

| ID | Situação | Observação |
| --- | --- | --- |
| R5-A1 | Fechado | `Autorizacao.autorizar(sessao, acao_id, fatos)` com fatos do registro passados pelo módulo; `acesso` não chama ninguém. `AutorizarLeitura` declarada por `auditoria`, implementada por `acesso`, que já tem aresta `acesso_api --> auditoria`: sem aresta nova, sem ciclo. Resíduo: ver R6-B1. |
| R5-A2 | Parcial | `infra_consumo` e `infra_consumo_falha` existem e têm `empresa_id`. Ver R6-A1 e R6-M1. |
| R5-A3 | Fechado | Tabelas de `infra` com `empresa_id` têm política e `FORCE`; `app_descoberta` tem `SELECT` explícito com `GRANT` de coluna. Resíduo de descoberta de eventos em R6-A1. |
| R5-A4 | Fechado | Partição `DEFAULT` removida; AD-21 e AD-29 coerentes. Consequência operacional em R6-M3. |
| R5-M1 | Fechado | `EmpresasAtivas` declarada por `infra`, implementada por `cadastro`. Resíduo em R6-M4. |
| R5-M2 | Fechado com resíduo | `credencial_plataforma` e `empresa.codigo` definidos no AD-3. Resíduos em R6-M2 e R6-B2. |
| R5-M3 | Não fechado | O texto agora declara o id do nó "não para filtro", o que remove a contradição mas mantém o defeito (R6-M5). |
| R5-M4 | Parcial | `EliminacaoDeTitular` cobre `avaliacao`, `pdi`, `resultados`; todos têm aresta para `cadastro` (conferido). A justificativa agora contradiz o AD-8 (R6-M6). |
| R5-M5 | Parcial | `infra_sequencia` entrou na ordem do AD-19, mas há duas cadeias de lock (R6-M7). |

---

## Alto

### R6-A1. `infra_evento_publicado` é "imutável" e ao mesmo tempo tem estado, tentativas e fila de descoberta (AD-28 x AD-20, AD-29)

**Par:** time `infra` x times `comunicacao`/`resultados` (consumidores).

- **Ambos obedecem:** AD-28 abre dizendo que `infra_trabalho` e `infra_evento_publicado` têm "estado, tentativas e `proxima_tentativa`", e mais adiante que `infra_evento_publicado` "é só o registro imutável do evento; o estado vive por consumidor". AD-20/AD-29: `app_descoberta` lê só `empresa_id, id, estado` das filas. Alerta: "evento publicado incompleto há mais de X min".
- **Colisão:** o consumidor precisa descobrir quais agregados têm sequência acima de `infra_consumo.ultima`. Isso exige ler `agregado_tipo, agregado_id, sequencia` do evento e `ultima` do consumo, colunas fora da lista enumerada de `app_descoberta`, e `estado` do evento deixa de ter significado. Um time faz o worker de eventos varrer `infra_evento_publicado` por `estado` (coluna que o outro considera imutável); outro varre por comparação de sequência (precisa de `GRANT` que o AD-29 não dá). "Incompleto" não tem definição sem `estado`. O mesmo vale para quem escreve a política `SELECT` de `app_descoberta`.
- **Correção:** diga como se descobre trabalho de evento (consulta por (consumidor, agregado) com sequência maior que `ultima`, ou linha de pendência em `infra_trabalho` criada na emissão), liste as colunas dessa consulta no `GRANT` do AD-29 e retire `estado`/`tentativas`/`proxima_tentativa` de `infra_evento_publicado`.

---

## Médio

### R6-M1. Contagem de tentativas do evento venenoso sem transação própria (AD-28 x AD-20, AD-21)
`infra_consumo_falha(..., tentativas, motivo)` só diz "após N tentativas marca ERRO". A tentativa que falha dá rollback da transação do consumidor e leva o incremento junto. Falta dizer que o registro da falha usa `novaTransacao` (AD-20) e que `ultima` avança na mesma transação que o efeito. Sem isso o evento vira laço infinito silencioso ou perde evento, conforme o time.

### R6-M2. `credencial_plataforma` e tabelas de plataforma fora da lista sem RLS (AD-3, AD-28)
`credencial_plataforma` não tem `empresa_id` nem está na lista sem RLS do AD-28 ("toda tabela fora da lista falha o build"). Idem códigos de recuperação do TOTP e a sessão do administrador de plataforma (`sessao` liga a `funcionario`, que plataforma não tem). Inclua-as na lista com política atada a `plataforma_*` e diga como a sessão de plataforma é representada.

### R6-M3. Sem partição `DEFAULT`, partição faltante derruba todo login e toda ação auditada (AD-29 x AD-21)
A escolha é coerente, mas "a ação auditada aborta" inclui login com sucesso e canal 2. Um job parado por 3 meses é indisponibilidade total. Faltam: criação das partições iniciais na migração (quem, com qual papel, já que `dono_auditoria` é `NOLOGIN`), alerta de partição futura como critério de aceite do deploy e o que o canal 2 faz quando o `INSERT` falha por falta de partição.

### R6-M4. `EmpresasAtivas`: em qual `DataSource` o código de `cadastro` roda? (AD-20 x AD-3, AD-29)
A porta é implementada por `cadastro` (código Java, repositório base com filtro de empresa), mas a view é concedida a `app_descoberta`, que é outro papel/`DataSource`. O texto não diz se `cadastro` abre conexão com o papel de descoberta. Um time faz o repositório base com filtro de empresa (zero linhas), outro cria `DataSource` próprio dentro de `cadastro`. Especifique que a implementação usa um repositório fora do filtro, sobre o `DataSource` de descoberta, e acrescente à regra de arquitetura.

### R6-M5. Id do nó congelado segue sem uso; unidade fundida ou extinta some do escopo (AD-25, R5-M3)
O texto passou a dizer "não para filtro". O filtro compara o par congelado com `escopo_visivel` atual; quando a unidade é extinta ou fundida, o par não aparece em nenhum `escopo_visivel` e o item fica invisível ao gestor (e a regra D58 "par e descendentes" não diz como tratar nó extinto). Defina em `cadastro` que `escopo_visivel` inclui pares de nós extintos sob o nó que os absorveu.

### R6-M6. Justificativa de cancelar/reabrir: AD-8 e AD-30 se contradizem e a tabela não tem dono
AD-8: a justificativa "fica em campo do próprio agregado". AD-30: é registro acrescentado `justificativa(agregado, ocorrida_em, autor_id, texto)`, "nunca sobrescrito", mas também anonimizável (precisa de `UPDATE`). A tabela não aparece nos donos (AD-19, linha de entidades), e cancelar é de `avaliacao` enquanto reabrir atua sobre a etapa de `preenchimento`. Alinhe o AD-8, nomeie o dono (ou uma tabela por módulo) e diga que a anonimização é a única alteração permitida.

### R6-M7. Duas cadeias de lock sem ordem conjunta (AD-19 x AD-24)
AD-19 põe `infra_sequencia` na penúltima posição e AD-24 define `funcionario → credencial_acesso → sessao`. Desligar (cadastro) emite evento e chama `acesso.aplicarSituacao`; `acesso` emite o evento de link de senha depois de travar a credencial. Se `cadastro` emite antes de chamar `acesso`, toma `infra_sequencia` antes de `credencial_acesso`; o outro caminho toma na ordem inversa. Escreva uma ordem global única incluindo `funcionario`, `credencial_acesso`, `sessao` e a regra "emitir evento é o último passo da transação".

---

## Baixo

- **R6-B1.** `AutorizarLeitura` e as demais portas inversas (`ResolucaoDeEmpresa`, `EscopoOrganizacional`, `EmpresasAtivas`, `EliminacaoDeTitular`) criam ciclo de beans; o teste de partida com `ObjectProvider` está só no AD-19 para `avaliacao`/`preenchimento`. Generalize. Os campos de `fatos` estão listados em prosa e não como tipo; fixe o tipo na `api` de `acesso`.
- **R6-B2.** AD-28 descreve `empresa` "só com id, situação e fuso"; AD-3 inclui `codigo`. Linha de entidades (donos) não cita `infra_sequencia`, `infra_consumo`, `infra_consumo_falha`, `credencial_plataforma` nem `par_de_consenso`.
- **R6-B3.** Pendentes da r5 sem bloqueio: R5-B1 (ER sem `par_de_consenso`; `PARTICIPANTE ||--o{ RESPOSTA`), R5-B3 parcial tardio em avaliação `CONCLUIDA`.

---

## Grafo

Conferido arestas e portas: `acesso_api --> auditoria`, todos os módulos `--> acesso_api` e `--> auditoria`, implementadores de `EliminacaoDeTitular` (`comunicacao`, `preenchimento`, `avaliacao`, `pdi`, `resultados`) todos com aresta para `cadastro`, `cadastro --> acesso_api` cobre `ResolucaoDeEmpresa`/`EscopoOrganizacional`. `EmpresasAtivas` (declarada em `infra`, fora do grafo) não gera aresta. Sem ciclo.

## Tabela-resumo

| ID | Severidade | ADs | Tema |
| --- | --- | --- | --- |
| R6-A1 | Alto | AD-28, AD-20, AD-29 | Evento "imutável" com estado; descoberta de eventos sem colunas liberadas |
| R6-M1 | Médio | AD-28, AD-20 | Tentativas do venenoso perdidas no rollback |
| R6-M2 | Médio | AD-3, AD-28 | `credencial_plataforma` fora da lista sem RLS |
| R6-M3 | Médio | AD-29, AD-21 | Partição faltante derruba login; partições iniciais |
| R6-M4 | Médio | AD-20, AD-3, AD-29 | `DataSource` da implementação de `EmpresasAtivas` |
| R6-M5 | Médio | AD-25 | Nó extinto invisível; id do nó sem uso |
| R6-M6 | Médio | AD-8, AD-30, AD-19 | Justificativa: campo x tabela; sem dono |
| R6-M7 | Médio | AD-19, AD-24 | Ordem de lock entre cadeias |
| R6-B1 a B3 | Baixo | vários | Ciclo de beans, redação, donos |
