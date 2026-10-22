---
name: 'Revisão adversária da espinha de arquitetura do DEZI novo (rodada 4)'
target: architecture-dezi-novo.md (AD-1 a AD-30, status draft, atualizada em 2026-10-21)
method: verificação do fechamento de C1-C5 e A1-A8 da r3 contra o texto atual; pares de unidades que cumprem cada AD à letra e constroem peças incompatíveis, com foco em contradições introduzidas pelas correções recentes
created: '2026-10-21'
supersedes: review-adversario-r3.md
---

# Revisão adversária, rodada 4

## Veredito: NÃO PASSA

Contagem: 1 crítico, 6 altos, 7 médios, 4 baixos. M2-M9 e B1-B3 da r3 não foram reabertos, salvo o resíduo bloqueante de M1 (F1-A6, criação de partições).

A r3 foi absorvida em texto na maior parte. Os problemas novos vêm de correções que mudaram a direção de uma porta ou criaram uma regra sem lugar onde viver: portas declaradas do lado errado (ciclo), `sequencia` por contador, view de escopo sem chave de pessoa, AV3 sem participante, corte por tempo.

## Fechamento de C1-C5 e A1-A8

| ID | Situação | Observação |
| --- | --- | --- |
| C1 | Fechado nas arestas; reabre pela semântica da seta (R4-C1) | Faltantes de auditoria, acesso, cadastro e eventos foram acrescentadas. |
| C2 | Parcial | Motivo é código e a justificativa vai ao agregado. A justificativa é sobrescrita na reabertura repetida e fica fora do catálogo LGPD (R4-M2). |
| C3 | Parcial | A ordem por contador não define onde o contador vive. O estado por consumidor e o avanço sobre tipos não assinados também não estão definidos (R4-A1). |
| C4 | Parcial | O corte virou `clock_timestamp()` do banco, mas o carimbo da resposta continua sem fonte de relógio (R4-A5). |
| C5 | Parcial | Há resolução de empresa, mas a porta foi declarada do lado oposto ao do AD-19 (R4-C1), e a leitura "sem RLS" não tem papel nem lugar na lista do AD-28 (R4-A2). |
| A1 | Fechado | `infra` enumera por `app_descoberta`; AD-3 corrigido. |
| A2 | Parcial | A view existe, mas não mapeia pessoa para par (R4-A3). |
| A3 | Parcial | A porta `EliminacaoDeTitular` foi declarada do lado que cria ciclo (R4-C1). A lista de tratadores é incompleta (R4-M2). |
| A4 | Fechado | `confirmarSenha` único, com teste de arquitetura. |
| A5 | Parcial | `par_de_consenso` e `aceite_consenso` existem, mas a chave de AV3 não cabe nelas (R4-A4). |
| A6 | Fechado | Carimbo na liberação. Resíduo: "campanha" congelada não tem evento de carimbo (R4-A3). |
| A7 | Fechado | Total ignora parcial em `ERRO`. |
| A8 | Fechado | Forma de dois inteiros e ordem do AD-19. |

---

## Crítico

### R4-C1. Portas declaradas por `cadastro` e implementadas por `acesso`: ciclo `acesso` <-> `cadastro` (AD-3, AD-4, AD-30, diagrama x AD-19, AD-24)

**Par:** time `acesso` x time `cadastro`.

- **Ambos obedecem:** AD-19: "o dono A que precisa de B, que depende de A, declara a porta; B a implementa". O diagrama e o texto (AD-3, AD-4, "regra do desenho") dizem que `EscopoOrganizacional` e `ResolucaoDeEmpresa` são "de `cadastro`" e que `acesso` as usa. AD-30: `EliminacaoDeTitular` é "declarada por `cadastro`" e implementada por cada módulo com dado pessoal, inclusive `acesso`. O grafo tem `cadastro --> acesso_api` (AD-24, `aplicarSituacao` e `criarCredencial`).
- **Colisão:** quem declara a porta importa o tipo do declarante. Com a porta em `cadastro.api`, `acesso` importa `cadastro.api`, e o `verify()` vê o ciclo com `cadastro --> acesso_api`. O próprio diagrama traz as duas arestas (`acesso_api -.-> cadastro` e `cadastro --> acesso_api`). Com a seta definida como "depende da API pública de", o grafo "verificado" tem ciclo. Um time declara a porta em `acesso.api` (AD-19, como a r3 C5 sugeriu), o outro em `cadastro.api` (o texto), e o contrato do login e da anonimização diverge. O pontilhado de `preenchimento -.-> avaliacao` aponta para o lado oposto da lógica do `acesso_api -.-> cadastro` (aquele coincide com a dependência real, este não): a semântica do tracejado é ambígua.
- **Correção:** `ResolucaoDeEmpresa`, `EscopoOrganizacional` e `EliminacaoDeTitular` são declaradas em `acesso.api`, ou em `infra`, se forem comuns, e implementadas por `cadastro`. O diagrama passa a usar uma única semântica de seta (dependência de compilação), sem tracejado ambíguo.

---

## Alto

### R4-A1. `sequencia` por contador: sem lugar do contador, estagnação por tipo não assinado, estado por consumidor (AD-28 x AD-19, AD-21)

**Par:** time `preenchimento`/`avaliacao` (emissores) x time `comunicacao` x time `resultados` (consumidores).

- **Ambos obedecem:** AD-28: a `sequencia` é um contador "na linha do agregado, sob o lock que a transação já detém"; o consumidor registra a última por (consumidor, agregado) e "adia o que chega com lacuna". AD-19: gravação de resposta toma só `FOR SHARE` na avaliação; "nenhum caminho faz upgrade de `FOR SHARE` para escrita na mesma linha"; só `infra` escreve em tabelas de `infra`.
- **Colisão 1:** "linha do agregado" é de `avaliacao` (dono único, AD-2). Incrementar nela é escrita fora do dono. Em `FOR SHARE` é upgrade proibido, e com `FOR UPDATE` serializa todo o preenchimento. Se o contador vive numa tabela de `infra`, a frase "sob o lock que a transação já detém" é falsa: o que garante a ordem é o lock da própria linha do contador, mantido até o commit. Esse lock não está na ordem global de locks do AD-19. Dois times implementam as duas leituras.
- **Colisão 2 (estagnação silenciosa):** a numeração conta todos os eventos do agregado, mas o consumidor só assina alguns tipos. `comunicacao` nunca vê a sequência 4 (tipo que ela não assina), então adia o 5 para sempre. Não há alerta (o alerta é "evento publicado incompleto", não "consumidor parado por lacuna").
- **Colisão 3 (r3 C3 colisão 4, não tratada):** `infra_evento_publicado` tem "estado, tentativas" por evento, mas há dois consumidores (`comunicacao`, `resultados`). Estado por (evento, consumidor), poison de um bloqueando o outro e alerta por consumidor não estão definidos.
- **Correção:** o contador é uma linha de `infra` por agregado, travada só na emissão e sempre por último na ordem do AD-19. O consumidor avança sobre os tipos que ele não assina. Defina `infra_evento_consumo(evento_id, consumidor, estado, tentativas)` e alerta por lacuna com idade.

### R4-A2. Resolução de empresa no login: leitura "sem RLS" sem papel, fora da lista do AD-28 e sem caminho para o administrador de plataforma (AD-3, AD-28, AD-29, AD-7)

**Par:** time `acesso` (login) x time `cadastro` (`empresa`) x time de esquema (teste do AD-28).

- **Ambos obedecem:** AD-3: `ResolucaoDeEmpresa` é "leitura sem RLS limitada a `id`, `situacao`". AD-28: "toda tabela fora da lista de tabelas sem RLS falha o build"; `empresa` não está na lista (`sessao`, `shedlock`, contadores, tabelas de `infra`). AD-29: `app_rw` sem `BYPASSRLS`.
- **Colisão:** antes do login não há variável de sessão. Então, com `empresa` sob RLS e FORCE, `app_rw` lê zero linhas e todo login dá 401. Um time põe política permissiva de leitura para `app_rw` em `empresa` (e passa a listar todas as empresas e colunas), outro põe `empresa` na lista sem RLS (e o AD-28 não a prevê), outro usa `app_descoberta` (que o AD-29 limita a filas e `empresa.id`/situação). O identificador público de login da empresa também não está fixado (slug, código?). Administrador de plataforma "não tem empresa" (r3 C5), mas o login exige `empresa`: não há caminho de login para ele, a credencial dele pertence a qual empresa e como o 401 uniforme se aplica.
- **Correção:** view `empresa_login(codigo_publico, id, situacao)` com `GRANT SELECT` ao papel da aplicação, listada no AD-28 como exceção com teste de esquema. Defina o login de plataforma como endpoint e `DataSource` próprios (`plataforma_rw`), com a credencial sem `empresa_id` e TOTP obrigatório.

### R4-A3. AD-25 contradiz a si mesmo: "atual não é copiada" x "toda tabela guarda o par"; a view não mapeia pessoa a par (AD-25 x AD-2, AD-1)

**Par:** time `pdi` x time `resultados`.

- **Ambos obedecem:** AD-25: "toda tabela que precisa de escopo guarda `unidade_id, departamento_id` e o módulo filtra por esses pares"; "coordenada `atual` não é copiada para outro módulo", e é resolvida por join na view `escopo_visivel(empresa_id, no_escopo_id, unidade_id, departamento_id)`; a porta devolve "um conjunto de pares, nunca ids de pessoas".
- **Colisão 1:** a coordenada `atual` vale para funcionário e PDI sem avaliação. Mas a view só tem "nós de escopo -> pares visíveis" e nenhuma linha "funcionário -> par atual". O `pdi` não tem como saber o par do dono da ação sem gravar uma cópia (contra a frase "não é copiada") ou sem join com `funcionario` (contra o AD-1). A lista de avaliações ainda não liberadas (coordenada `atual`) tem o mesmo problema.
- **Colisão 2:** a view é indexada por `no_escopo_id`, mas a porta devolve pares e não nós. O consumidor não sabe qual `no_escopo_id` aplicar no join. Um time usa `IN (pares)` (lista grande para o gestor de árvore larga), o outro faz join por nó.
- **Colisão 3:** o carimbo "congelada" guarda o id do nó, mas a regra de filtro só usa o par. A unidade fundida ou renomeada continua fora de qualquer subárvore atual (r3 A2 colisão 2 só mudou de forma). "Campanha" é `congelada` por padrão, mas a campanha não passa por liberação: não existe evento de carimbo para ela.
- **Correção:** `escopo_visivel` expõe também `funcionario_id -> unidade_id, departamento_id` atuais; a porta devolve `no_escopo_id[]` para o join; o filtro congelado usa o nó; para a campanha, defina o instante do carimbo (criação) ou use `atual`.

### R4-A4. AV3: a chave canônica não diz qual `participante_id` a linha leva (AD-26 x AD-9, AD-19)

**Par:** time `preenchimento` x time `resultados`.

- **Ambos obedecem:** AD-26: chave `(participante_id, snapshot_pergunta_id, etapa)`, única no banco; "a linha AV3 pertence ao par" de `par_de_consenso(avaliacao_id, avaliado_participante_id, avaliador_participante_id)`.
- **Colisão:** a chave não contém o par. Se a linha AV3 leva o `participante_id` do avaliado, o consenso com dois avaliadores diferentes repete (X, pergunta, AV3) e viola a unicidade. Se leva o do avaliador, a leitura "consenso do avaliado" precisa de join por `par_de_consenso`. Os dois times escolhem lados e a unicidade imposta pelo AD-9 só passa em um deles.
- **Correção:** AV3 guarda `par_de_consenso_id` e a chave canônica vira `(participante_id, snapshot_pergunta_id, etapa, par_de_consenso_id nulo)` com índice único parcial por etapa. Ou o `participante_id` de AV3 é sempre o do avaliador, escrito por regra.

### R4-A5. Corte do total por instante ainda depende do relógio que carimba a resposta (AD-11 x AD-10)

**Par:** time `preenchimento` (grava `atualizado_em`) x time `resultados` (total).

- **Ambos obedecem:** AD-11: o corte é `clock_timestamp()` do banco, depois do `FOR UPDATE`, e "o total lê as respostas visíveis até ele"; o `Relogio` da aplicação não serve ao corte. O AD-10 expõe só o `Relogio` injetado como relógio.
- **Colisão:** a coluna da resposta que se compara ao corte não tem fonte definida. Se `atualizado_em` vem do `Relogio` da instância (única que o AD-10 fornece), com skew entre as duas instâncias uma resposta comitada antes do `FOR UPDATE` tem carimbo posterior ao corte e sai do consolidado imutável (D55). Fora isso, o filtro é redundante: depois de obtido o `FOR UPDATE`, nenhuma gravação nova é possível (AD-19), então "todas as respostas da avaliação" é exato.
- **Correção:** remova o filtro por tempo; o total lê todas as respostas da avaliação sob a justificativa do lock. Se o instante for útil para auditoria, grave-o, mas não o use como filtro. Se houver filtro, `atualizado_em` é `clock_timestamp()` do banco.

### R4-A6. Quem cria a partição mensal da auditoria (AD-21, AD-29 x AD-8)

**Par:** time de infra/DBA x time `acesso`/`cadastro` (ações auditadas).

- **Ambos obedecem:** AD-21: auditoria particionada por mês e "falha de gravação aborta a ação". AD-29: `app_rw` só `INSERT`/`SELECT`; `migracao` é dona das partições; a função `SECURITY DEFINER` do expurgo "só remove partição vencida".
- **Colisão:** ninguém tem a tarefa de criar a partição do mês seguinte (`CREATE` exige o dono; o job roda como `app_rw`/`app_descoberta`; o expurgo não cria). No primeiro dia sem partição, o `INSERT` na auditoria falha e, como a gravação é obrigatória, toda ação auditada (login incluso, via canal 2 degradado) aborta. Um time cria partições à mão por migração, outro adiciona `CREATE` ao `app_rw` (e passa a poder criar objetos), outro cria partição `DEFAULT` (a que o expurgo por `DROP` não consegue limpar).
- **Correção:** a função `SECURITY DEFINER` de manutenção cria a partição seguinte (e a do mês corrente), é chamada por job do AD-14 e gera alerta se faltar a partição dos próximos 2 meses.

---

## Médio

### R4-M1. `REPEATABLE READ` com "primeira instrução `FOR UPDATE`" é impossível com a variável de empresa (AD-23 x AD-3, AD-20)

O AD-3 e o AD-20 exigem `set_config(..., true)` em cada transação, antes de qualquer leitura (política fail-closed). Em `REPEATABLE READ` o snapshot nasce na primeira instrução, que é o `set_config`, e não no `FOR UPDATE` como o AD-23 supõe. Se um `prorrogar` fizer commit entre as duas, o `FOR UPDATE` falha com 40001. O retry (3x) cobre, mas a premissa "snapshot depois do lock" é falsa e o teste "liberar x prorrogar" passa por retry, não por desenho. **Correção:** reconheça que o snapshot precede o lock e aceite o retry como mecanismo, ou use `READ COMMITTED` com `FOR UPDATE` e leitura em lote já sob o lock.

### R4-M2. A justificativa livre (D26, D32) é sobrescrita e fica fora do tratamento de LGPD (AD-8 x AD-26, AD-30)

A justificativa fica "em campo do próprio agregado" e a trilha guarda "o código e o id do registro". `reabrir` age sobre a etapa e apaga a finalização (AD-26); uma segunda reabertura da mesma etapa sobrescreve o campo, e a primeira justificativa some sem rastro (D32 exige o "por quê" de cada reabertura). Por fim, a lista de tratadores do AD-30 cita `resposta.observacao`, mas não a justificativa, os "comentários finais" da etapa (AD-19), o texto de PDI e o arquivo `bytea` da importação. **Correção:** tabela de histórico `transicao_justificativa` (append-only, `FK` por id ao agregado) e a lista dessas colunas no catálogo do AD-30.

### R4-M3. AD-10 diz que a situação de preenchimento é derivada; AD-26 grava `finalizada` (AD-10 x AD-26)

AD-10: "situação de preenchimento e `preenchida_avN` são derivados das respostas, nunca campo gravado". AD-26: `finalizada` é gravada em `etapa_participante`. Um time deriva "finalizada" de `completa` (e vê finalizadas por conclusão automática), o outro lê o campo. **Correção:** AD-10 passa a distinguir `completa` (derivada) de `finalizada` (gravada).

### R4-M4. Resposta 401 uniforme, mas não uniforme em tempo (AD-5, AD-7)

Nada exige hash fictício quando a matrícula ou a empresa não existe. Matrícula existente custa Argon2id (dezenas de ms); inexistente, quase nada. A enumeração de matrículas por tempo fura a uniformidade do AD-5 e anula o "429 sem distinguir existente de inexistente" do AD-7. **Correção:** verificação fictícia de custo igual, incluída no teste de aceite do CAP-1.

### R4-M5. Atraso por usuário não vale contra ataque distribuído (AD-7)

O atraso por usuário só barra quando o IP também tem falhas; IP novo é "limpo" e sempre passa ao passo 3. Um ataque de muitos IPs a uma matrícula só é limitado pelo lock (uma verificação por vez) e pelo limite por empresa. O "Prevents: rajada que contorna o contador" não cobre botnet. É uma troca declarada pelo responsável (contra negação de serviço). **Correção:** registre o risco residual, ou limite global por matrícula alto (por exemplo, 1.000 falhas por dia) que dispare alerta e exija desbloqueio do RH.

### R4-M6. O "ponto central de checagem" do AD-4 não tem lugar nem assinatura (AD-4 x AD-28)

O AD-4 exige "um ponto, não anotação por endpoint"; a regra do desenho manda cada módulo chamar a porta `Autorizacao`. O filtro central não pode viver em `infra` (não depende de `acesso`) e "dono do recurso lido do registro" só se resolve depois de carregar o registro, dentro do módulo. Um time faz filtro por URL em `acesso` (não vê o registro), outro chama `autorizar` no serviço. O gerador de teste negativo por célula também não sabe ligar endpoint a célula. **Correção:** assinatura `autorizar(recurso, acao, registro)` na API de `acesso`, chamada pelo serviço, com o gerador de teste a partir do OpenAPI (`x-celula`). Isso inclui `auditoria` (leitura da trilha pelo RH): hoje não tem aresta para `acesso_api`.

### R4-M7. Auditoria com `empresa_id` nulo: papel de escrita e política não definidos (AD-21, AD-29)

O canal 2 grava auditoria sem empresa (login com empresa inexistente) "em partição de plataforma lida só pelo papel de plataforma". O AD-29 diz que `app_rw` só insere; a política `WITH CHECK` precisa aceitar `empresa_id` nulo, o que permite a qualquer SQL de `app_rw` forjar linhas de plataforma. "Partição mensal" e "partição de plataforma" são dois critérios na mesma tabela. **Correção:** tabela `auditoria_plataforma` separada, com política de `INSERT` própria (colunas restritas) e partição mensal.

---

## Baixo

- **R4-B1.** Campos de autoria divergem: AD-17 grava `ator = usuario:<id>` e `executado_por = sistema:<job>`; AD-20 grava `ator = sistema:<job>`. Um esquema só, com os dois campos.
- **R4-B2.** `infra` lê `empresa` (de `cadastro`) por `app_descoberta` por SQL direto, o que o `verify()` não vê e o AD-2 proíbe. Registre como exceção nomeada e testada (view).
- **R4-B3.** O parcial de uma etapa finalizada logo antes da conclusão pode ser criado pelo consumidor depois da conclusão e gravar parcial de avaliação `CONCLUIDA`. Defina que o parcial recusa avaliação concluída.
- **R4-B4.** O aceite do consenso (D53) chama `confirmarSenha` (Argon2id) dentro da transação com `FOR SHARE` na avaliação, segurando trava e conexão durante o hash. Faça a verificação antes de abrir a transação de gravação.

---

## Tabela-resumo

| ID | Severidade | ADs | Tema |
| --- | --- | --- | --- |
| R4-C1 | Crítico | AD-3, AD-4, AD-30, AD-19, AD-24 | Portas declaradas por `cadastro`: ciclo com `acesso` |
| R4-A1 | Alto | AD-28, AD-19, AD-21 | Contador de `sequencia`: lugar, estagnação, estado por consumidor |
| R4-A2 | Alto | AD-3, AD-28, AD-29, AD-7 | Resolução de empresa sem RLS; login de plataforma |
| R4-A3 | Alto | AD-25, AD-2, AD-1 | View sem pessoa->par; "não copiar" x "guardar par"; campanha |
| R4-A4 | Alto | AD-26, AD-9 | Chave de AV3 sem par |
| R4-A5 | Alto | AD-11, AD-10 | Corte por tempo sem fonte de relógio na resposta |
| R4-A6 | Alto | AD-21, AD-29, AD-8 | Partição mensal da auditoria sem criador |
| R4-M1 | Médio | AD-23, AD-3, AD-20 | RR "primeira instrução" x `set_config` |
| R4-M2 | Médio | AD-8, AD-26, AD-30 | Justificativa sobrescrita; fora do catálogo LGPD |
| R4-M3 | Médio | AD-10, AD-26 | Derivado x gravado |
| R4-M4 | Médio | AD-5, AD-7 | 401 não uniforme em tempo |
| R4-M5 | Médio | AD-7 | Ataque distribuído |
| R4-M6 | Médio | AD-4, AD-28 | Ponto central de autorização |
| R4-M7 | Médio | AD-21, AD-29 | Auditoria sem empresa |
| R4-B1 a B4 | Baixo | AD-17, AD-20, AD-2, AD-11, AD-7 | Autoria, SQL de `infra`, parcial tardio, hash em transação |
