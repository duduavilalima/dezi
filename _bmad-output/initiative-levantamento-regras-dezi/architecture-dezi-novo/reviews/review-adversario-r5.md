---
name: 'Revisão adversária da espinha de arquitetura do DEZI novo (rodada 5)'
target: architecture-dezi-novo.md (AD-1 a AD-30, status draft, atualizada em 2026-10-21)
method: verificação do fechamento de R4-C1 e R4-A1..A6 contra o texto atual; grafo do diagrama conferido contra as portas e dependências citadas nos ADs; busca de contradições e ciclos introduzidos pelas correções
created: '2026-10-21'
supersedes: review-adversario-r4.md
---

# Revisão adversária, rodada 5

## Veredito: NÃO PASSA

Contagem: 0 críticos, 4 altos, 5 médios, 3 baixos. Médios e baixos antigos só entram quando bloqueantes ou quando a correção da r4 os tornou piores.

O ciclo crítico da r4 foi fechado. Restam quatro pontos altos: dois são resíduo de A1 e M6 que a correção não cobriu, dois são contradições novas criadas pelas próprias correções (partição `DEFAULT`; tabelas de `infra` com e sem RLS).

## Fechamento de R4-C1 e R4-A1..A6

| ID | Situação | Observação |
| --- | --- | --- |
| R4-C1 | Fechado | `ResolucaoDeEmpresa` e `EscopoOrganizacional` são declaradas por `acesso` e implementadas por `cadastro` (AD-3, AD-4, AD-24, regra do desenho). `EliminacaoDeTitular` é declarada por `cadastro` e implementada por `comunicacao` e `preenchimento`, ambos com aresta para `cadastro` (AD-30). O diagrama não tem mais `acesso_api -.-> cadastro`; o tracejado sempre aponta do implementador/consumidor para o declarante. Grafo conferido: DAG, sem ciclo. |
| R4-A1 | Parcial | `infra_sequencia` e `infra_consumo` fecham lugar do contador e estagnação por tipo não assinado. Estado por (evento, consumidor) e poison continuam abertos (R5-A2). |
| R4-A2 | Parcial | `empresa` fora da RLS, `empresa_ativa`, `plataforma_*` e login de plataforma existem. Ver R5-A3 (contradição da lista sem RLS) e R5-M2 (credencial de plataforma). |
| R4-A3 | Parcial | `escopo_visivel` e `lotacao_atual` mapeiam pessoa a par; campanha carimba na criação. O id do nó carimbado não é usado por nenhum filtro (R5-M3). |
| R4-A4 | Fechado | AV3 usa `par_de_consenso_id` como dono, com restrição de exatamente um dono. Detalhe de implementação: unicidade com colunas anuláveis exige dois índices parciais ou `NULLS NOT DISTINCT`. |
| R4-A5 | Fechado | AD-11: total lê todas as respostas sob o `FOR UPDATE`; `clock_timestamp()` é só registro. Resíduo de redação: "respostas visíveis". |
| R4-A6 | Parcial | Criação de partições existe, mas a partição `DEFAULT` criada para a falha quebra a própria criação (R5-A4). |
| set_config no AD-23 | Fechado | `set_config` como primeira instrução, snapshot antes do lock reconhecido, retry 3x em `40001`/`55P03`. Coerente com AD-3 e AD-20. |

---

## Alto

### R5-A1. `Autorizacao.autorizar(sessao, recurso, acao)` não tem como cumprir dono, dois olhos e escopo sem criar ciclo (AD-4 x AD-19, diagrama)

**Par:** time `acesso` x times dos módulos de negócio (e `auditoria`).

- **Ambos obedecem:** AD-4: o ponto central é `Autorizacao.autorizar(sessao, recurso, acao)` em `acesso`; o "dono do recurso é lido do registro"; a matriz carrega a regra de dois olhos (D52: quem é participante não conclui a própria avaliação) e o escopo por par. Regra do desenho: `acesso` não chama nenhum módulo.
- **Colisão:** o dono, a lista de participantes e o par (unidade, departamento) do recurso pertencem a `avaliacao`, `pdi`, etc. `acesso` não pode lê-los (aresta `acesso -> avaliacao` fecha ciclo com `avaliacao --> acesso_api`). Ou o chamador passa esses fatos em `recurso` (então "lido do registro" vira "fornecido pelo módulo", e a assinatura precisa dizer quais campos), ou o módulo avalia a matriz por conta própria (anotação por endpoint, o que o AD-4 proíbe). Dois times fazem as duas leituras. `auditoria` (leitura da trilha pelo RH, "leitura de dado sensível" do AD-8) precisa de `Autorizacao` mas não tem aresta para `acesso_api`; adicioná-la fecha o ciclo `acesso_api -> auditoria -> acesso_api`.
- **Correção:** assinatura `autorizar(sessao, acao, FatosDoRecurso{empresa, donoFuncionarioId, participantesIds, unidadeId, departamentoId})` com o tipo na API de `acesso`; o módulo dono monta os fatos do registro que ele já carregou. Para `auditoria`, declare a porta de leitura na própria `auditoria`, implementada por `acesso`, e registre a aresta.

### R5-A2. Estado por consumidor e evento venenoso ainda sem definição (AD-28 x AD-13, AD-21)

**Par:** times `comunicacao` e `resultados` (consumidores) x time `infra`.

- **Ambos obedecem:** AD-28: `infra_evento_publicado` tem "estado, tentativas" por evento; `infra_consumo(consumidor, agregado, ultima)` guarda só a última sequência; o consumidor "só adia quando falta uma sequência cuja transação ainda não comitou". Há dois consumidores para os mesmos eventos de `preenchimento`/`avaliacao`.
- **Colisão:** o evento N falha de forma determinística para `comunicacao` (destinatário sem e-mail, por exemplo). Não é lacuna, então o texto não diz o que acontece. Um time marca `estado=ERRO` no evento (derruba também o `resultados`, que o processou). Outro não avança `ultima` (bloqueia todos os eventos seguintes do agregado só para aquele consumidor, sem alerta, pois o alerta é "evento incompleto" e "lacuna"). Um terceiro avança e perde o evento. Além disso, `infra_consumo` não tem `empresa_id` (o AD-28 exige `empresa_id` nas tabelas de `infra`).
- **Correção:** `infra_consumo` guarda também `estado`, `tentativas` e `erro_em` por (consumidor, agregado); o evento envenenado tem política explícita (N tentativas, depois `ERRO` por consumidor, com alerta e decisão de avançar ou travar o agregado) e `infra_evento_publicado.estado` passa a significar só "publicado/completo".

### R5-A3. A lista de tabelas sem RLS contradiz a regra de `empresa_id` e deixa `app_descoberta` sem política (AD-28 x AD-20, AD-29)

**Par:** time de esquema/teste x time `infra`.

- **Ambos obedecem:** AD-28: `infra_trabalho`, `infra_evento_publicado` e `infra_sequencia` têm `empresa_id`; "toda tabela com `empresa_id` tem política e `FORCE`"; e a lista sem RLS inclui "tabelas de `infra` sem dado de negócio". AD-20/AD-29: `app_descoberta` (sem `BYPASSRLS`) lê `empresa_id, id, estado` das filas; consumidor lê `empresa_id` da linha e só depois abre `paraEmpresa`.
- **Colisão:** se as tabelas de `infra` têm RLS e `FORCE`, `app_descoberta` lê zero linhas (nenhuma política para ele; o AD-29 só fala de política atada ao papel para `plataforma_*`), e o worker, sem variável, nunca acha trabalho. Se estão na lista sem RLS, o teste "tabela com `empresa_id` tem política" falha o build, e `app_rw` com variável errada lê fila de todas as empresas. Um time escreve cada versão. O teste de esquema do AD-28 não passa em nenhuma sem decisão.
- **Correção:** diga que as tabelas de `infra` com `empresa_id` têm RLS e `FORCE`, com política `SELECT` adicional `TO app_descoberta` limitada às colunas enumeradas (via view), e que o claim (`FOR UPDATE SKIP LOCKED`) ocorre sob `paraEmpresa` por empresa. Remova "tabelas de `infra`" da lista sem RLS, salvo `shedlock`.

### R5-A4. Partição `DEFAULT` impede a criação da partição do mês e a expurgo (AD-29 x AD-21, AD-8)

**Par:** time de infra/DBA x time de auditoria/LGPD.

- **Ambos obedecem:** AD-29: `criar_particao_auditoria` cria as partições dos próximos 3 meses; existe partição `DEFAULT` "para que nenhuma ação auditada aborte"; `expurgar_particao_auditoria` remove só partição vencida; retenção por `DROP` de partição.
- **Colisão:** no PostgreSQL, criar ou anexar uma partição cujo intervalo já tem linhas na `DEFAULT` falha ("updated partition constraint for default partition would be violated"). O caminho de falha que a `DEFAULT` cobre (job parado, partição faltando) é exatamente o que depois impede o job de recuperar a partição do mês, e as linhas na `DEFAULT` nunca expiram (nem por `DROP`, nem por `DELETE`, que o gatilho recusa). O AD-21 ainda fala em "partição de plataforma" para `empresa_id` nulo, um segundo critério de partição que não existe em uma tabela particionada por mês.
- **Correção:** a função de criação move as linhas do intervalo da `DEFAULT` (como `dono_auditoria`, antes de criar, com `DETACH`/reanexação) e o expurgo cobre a `DEFAULT` por idade; alerta se a `DEFAULT` não estiver vazia. Para plataforma, tabela `auditoria_plataforma` própria (R4-M7).

---

## Médio

### R5-M1. `infra` depende de `cadastro` por `empresa_ativa`, contra "infra não depende de módulo de negócio" (AD-20, AD-28, B2 da r4)

A view `empresa_ativa` é "publicada por `cadastro` na sua API" e lida por `infra`; `cadastro` enfileira em `infra`. Em nível de SQL é dependência dupla que o `verify()` não vê; o AD-28 afirma que `infra` não depende de módulo de negócio. Registre como exceção nomeada com teste de esquema, ou inverta (`cadastro` implementa uma porta `EmpresasAtivas` declarada em `infra`).

### R5-M2. Credencial de plataforma contradiz AD-24 e a unicidade (AD-3 x AD-24, AD-2)

AD-3: administrador de plataforma tem "credencial de `empresa_id` nulo em `acesso`". AD-24: `credencial_acesso` é ligada por `funcionario_id` e a matrícula é única por empresa; `funcionario` pertence a uma empresa. Plataforma não tem `funcionario_id`, e `UNIQUE (empresa_id, matricula)` com `empresa_id` nulo não impede duplicata. O identificador público da empresa no login (slug/código) continua sem definição, e `empresa` "só com id, situação e fuso" não tem a chave de busca. Defina tabela `credencial_plataforma` separada e a coluna `codigo_publico` da empresa.

### R5-M3. O id do nó congelado não é usado por nenhum filtro (AD-25)

O carimbo guarda o par e o id do nó, "unidade extinta mantém o nó como histórico", mas o filtro compara apenas o par com `escopo_visivel`. A unidade fundida ou extinta sai do par visível de qualquer gestor e o item fica invisível. Ou `escopo_visivel` inclui pares de nós extintos sob o nó pai que os absorveu, ou o filtro congelado resolve por `no_id`. Hoje o campo é gravado e nunca lido.

### R5-M4. `EliminacaoDeTitular` não cobre `pdi` e a justificativa livre continua sobrescrita (AD-30, AD-8, AD-26)

Implementadores listados: `comunicacao` e `preenchimento`. Texto livre de ação de PDI (`pdi`, que já depende de `cadastro`), comentários de `resultados` e o `bytea` da importação ficam sem tratador, e o teste "toda coluna pessoal tem tratador" falha o build. A justificativa de `reabrir` continua em campo do agregado, sobrescrita na segunda reabertura (D32 exige o por quê de cada uma). Acrescente `pdi` como implementador e uma tabela append-only de justificativas.

### R5-M5. Ordem de locks entre linhas de `infra_sequencia` e AD-19 não definida

AD-28 manda tomar `infra_sequencia` por último, mas o AD-19 não inclui o item na ordem global, e uma transação que emite dois eventos de agregados diferentes (por exemplo, conclusão que emite do agregado avaliação e do participante) pode tomar as duas linhas em ordens opostas a outra, gerando deadlock. Fixe a ordem (por tipo de agregado e id) no AD-19 e um único `agregado_id` por ação.

---

## Baixo

- **R5-B1.** AD-10 chama "situação de preenchimento" de derivada, e AD-26 grava `finalizada`; diga que `finalizada` não é "situação de preenchimento". O ER ainda liga `PARTICIPANTE ||--o{ RESPOSTA` e não mostra `par_de_consenso` para AV3.
- **R5-B2.** "Adiado" declara "Particionamento do banco" adiado, mas a auditoria já é particionada por mês (AD-21, AD-29). Especifique "particionamento de tabelas de negócio".
- **R5-B3.** "Papel do job" com `EXECUTE` nas funções `SECURITY DEFINER` (AD-29) não está na lista de papéis; `app_descoberta` é só leitura e `app_rw` deixa de ser restrito. Nomeie o papel. Parcial tardio em avaliação `CONCLUIDA` (B3 da r4) segue sem regra.

---

## Tabela-resumo

| ID | Severidade | ADs | Tema |
| --- | --- | --- | --- |
| R5-A1 | Alto | AD-4, AD-19, AD-8 | `autorizar` sem fatos do recurso; ciclo latente com `auditoria` |
| R5-A2 | Alto | AD-28, AD-13, AD-21 | Estado por consumidor e poison |
| R5-A3 | Alto | AD-28, AD-20, AD-29 | Tabelas de `infra` com `empresa_id` x lista sem RLS; `app_descoberta` sem política |
| R5-A4 | Alto | AD-29, AD-21, AD-8 | Partição `DEFAULT` quebra criação e expurgo |
| R5-M1 | Médio | AD-20, AD-28 | `infra` lê `cadastro` |
| R5-M2 | Médio | AD-3, AD-24 | Credencial de plataforma x `funcionario_id`; chave pública da empresa |
| R5-M3 | Médio | AD-25 | Id do nó nunca usado |
| R5-M4 | Médio | AD-30, AD-8, AD-26 | Tratadores incompletos; justificativa sobrescrita |
| R5-M5 | Médio | AD-28, AD-19 | Ordem entre linhas de `infra_sequencia` |
| R5-B1 a B3 | Baixo | AD-10, AD-26, Adiado, AD-29 | Redação e papel do job |
