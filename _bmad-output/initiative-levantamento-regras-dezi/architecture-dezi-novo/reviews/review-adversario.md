---
name: 'Revisão adversária da espinha de arquitetura do DEZI novo (rodada 2)'
type: architecture-review
target: architecture-dezi-novo.md (AD-1 a AD-27, status draft, atualizada em 2026-10-20)
method: pares de unidades (módulos/times/histórias) que cumprem cada AD à letra e mesmo assim constroem peças incompatíveis; foco nas interações entre AD-19..27 e na mudança de AD-7 (atraso progressivo)
created: '2026-10-20'
supersedes: revisão de 2026-10-14 (seus achados viraram AD-19..27)
---

# Revisão adversária, rodada 2

## Veredito

**Ainda não liberar times em paralelo.** A rodada 1 fechou o dono de `participante`/`resposta`, as portas inversas, o contexto de empresa em job, os canais de evento, o catálogo versionado e o escopo por pares. As correções, porém, **introduziram acoplamentos de concorrência e de infraestrutura que nenhuma AD ordena**: o contador `versao_de_corte` (AD-11) colide com o `FOR SHARE` (AD-19) e gera deadlock de upgrade de lock; o `REQUIRES_NEW` (AD-21) colide com o contexto de empresa (AD-20), com o pool e com a linha da credencial (AD-7/AD-24); o registro de publicação de eventos e as filas rodam fora de qualquer contexto de empresa; a liberação (AD-23, `REPEATABLE READ`) entra em choque com `garantirAberta` (AD-19). O atraso progressivo (AD-7) fechou o bloqueio por terceiro mas deixou **password spraying** e **rajada paralela** sem limite.

São 16 achados: 6 críticos (C), 6 altos (A), 4 médios (M). Cada um traz **Par**, **Ambos obedecem**, **Colisão** e **Texto proposto** (novo AD ou aperto de AD existente). Os críticos 1 a 6 bloqueiam o scaffolding dos módulos `preenchimento`, `avaliacao`, `acesso` e da infraestrutura compartilhada.

Convenção: **Par** = duas unidades; **Ambos obedecem** = cada uma cita a AD que a autoriza; **Colisão** = o que quebra.

---

## C1. `versao_de_corte` (AD-11) x `FOR SHARE` (AD-19): deadlock de upgrade, dois escritores da linha, ETag sempre inválido

**Par:** história "gravar resposta" (`preenchimento`) x história "concluir avaliação" / "editar prazo" (`avaliacao`).

- **Ambos obedecem:** AD-19 manda toda escrita em `resposta` obter `avaliacao.garantirAberta` (`SELECT … FOR SHARE` na linha da avaliação). AD-11 manda `versao_de_corte` ser "contador monotônico da avaliação, incrementado a cada gravação de resposta". AD-2/AD-19 dizem que só o dono escreve a tabela; AD-15 diz que concorrência otimista usa a versão do registro (`ETag`/`If-Match`).
- **Colisão 1 (deadlock):** a gravação A e a gravação B tomam `FOR SHARE` na linha da avaliação; ambas tentam `UPDATE` (incremento) na mesma linha. `UPDATE` conflita com `FOR SHARE` alheio: cada uma espera a outra soltar. Deadlock clássico de upgrade, a cada par de respostas concorrentes na mesma avaliação.
- **Colisão 2 (dois donos):** o contador mora na linha de `avaliacao` (dona: `avaliacao`), mas quem incrementa é `preenchimento`. Um time cria coluna na linha da avaliação e a atualiza de `preenchimento` (viola AD-19); outro cria o contador em tabela própria de `preenchimento` (sem dono declarado no ER) e `resultados` lê de lá.
- **Colisão 3:** se o `ETag` da avaliação é a versão da linha, todo preenchimento invalida o `If-Match` do RH que prorroga prazo (412 permanente enquanto houver gente respondendo).
- **Colisão 4:** o incremento por gravação serializa todo o preenchimento de uma avaliação (ponto quente).

**Texto proposto (substitui o trecho do corte na AD-11 e acrescenta à AD-19):**
> O corte do recálculo total não usa contador por gravação. A conclusão (`FOR UPDATE` na avaliação) exclui por construção toda gravação posterior; portanto o corte é o instante/`txid` da transição para `CONCLUIDA`, gravado pela própria transição, e o total lê as respostas visíveis a partir desse marco. Se um contador for indispensável, é uma sequência por avaliação em tabela `preenchimento_*` própria (dona: `preenchimento`), jamais coluna da linha de `avaliacao`, incrementada com `UPDATE … RETURNING` atômico e fora do conjunto de colunas do `ETag` da avaliação. **Ordem global de locks** (nova regra de AD-19): `avaliacao` → `participante` → `etapa_participante` → `resposta`; nenhum caminho faz upgrade de `FOR SHARE` para escrita na mesma linha. Teste de concorrência: N gravações paralelas na mesma avaliação sem deadlock, mais prorrogação com `If-Match` durante o preenchimento.

---

## C2. `REQUIRES_NEW` (AD-21 canal 2) x contexto de empresa (AD-20) x linha da credencial (AD-7/AD-24) x pool

**Par:** time `acesso` (login e contador de falhas) x time de infraestrutura (`ContextoDeEmpresa`, pool).

- **Ambos obedecem:** AD-21 exige a auditoria de negado e "o contador do AD-7" em transação própria (`REQUIRES_NEW`) antes de lançar o erro. AD-20 diz que `ContextoDeEmpresa` faz `SET LOCAL` **dentro da transação**; AD-7 guarda contadores no Postgres (a credencial leva "bloqueios", AD-24).
- **Colisão 1 (self-deadlock):** o login externo lê/trava a linha de `credencial_acesso` (para checar bloqueio, `FOR UPDATE` ou `NO KEY UPDATE`); o contador em `REQUIRES_NEW`, em outra conexão, faz `UPDATE` na mesma linha e espera o commit do externo, que espera o interno: trava até o timeout. Se o contador é tabela separada, passa a ser escolha de cada time.
- **Colisão 2 (contexto):** a transação nova não herda `SET LOCAL`. Quem não reaplica `emRequisicao()` grava auditoria sob RLS fail-closed: a inserção falha ou some em silêncio; o erro de negócio é mascarado por erro de RLS. No login a empresa nem é de sessão (vem do corpo).
- **Colisão 3 (pool):** cada 403/falha de login segura uma conexão e abre uma segunda. Um atacante enviando credenciais inválidas em paralelo esgota o pool (cada requisição precisa de 2 conexões; com pool = N threads, todas seguram 1 e esperam a 2ª).
- **Colisão 4:** login para `empresa` inexistente: a que `empresa_id` pertence a linha de auditoria e de contador, sob RLS e FK? Cada time inventa (empresa "zero", descarte, ou contador sem limite de cardinalidade, permitindo encher a tabela com empresas fabricadas).

**Texto proposto (tightening de AD-21 e AD-20):**
> O contador e a auditoria de negado ficam em tabelas próprias de `acesso`/`auditoria`, nunca na linha da credencial; o login não mantém lock sobre a credencial entre a checagem e o erro (a checagem usa `SELECT` sem lock; o incremento é `INSERT … ON CONFLICT DO UPDATE … RETURNING` atômico, no próprio `REQUIRES_NEW`). `REQUIRES_NEW` é aberto **só por `ContextoDeEmpresa.novaTransacao(empresaId|PLATAFORMA)`**, que reaplica `SET LOCAL`; `Propagation.REQUIRES_NEW` direto é proibido (teste de arquitetura). Pool: reserva dedicada de conexões para o canal 2 (`maxPoolSize_negado`), e curto-circuito de rejeição antes de abrir a segunda conexão quando a reserva está cheia (a falha é 401/429 sem auditoria, contada em métrica). Empresa inexistente no login: o contador usa o hash de `(empresa_informada)` numa tabela sem RLS e com TTL e teto de linhas por IP; a auditoria registra `empresa_id = NULL` em partição de "plataforma" lida só pelo papel de plataforma. Teste: 200 logins falhos paralelos não esgotam o pool nem travam.

---

## C3. Eventos e filas executam fora de contexto de empresa; fila compartilhada sem dono; duas origens do outbox

**Par:** time `comunicacao` x time `resultados` x infraestrutura (Spring Modulith/ShedLock).

- **Ambos obedecem:** AD-21 canal 3 usa "publication registry persistente" e consumidor idempotente por `evento_id`. AD-14 diz que parcial, outbox e jobs "usam a mesma infraestrutura de fila no Postgres". AD-20 diz que todo trabalho assíncrono carrega `empresa_id` e roda sob `paraEmpresa`. AD-2 exige dono único por tabela e `empresa_id` em tabela de negócio. AD-13 diz que "a ação grava o e-mail pendente na mesma transação".
- **Colisão 1 (contexto):** o registro do Modulith reenvia publicações incompletas **na partida da aplicação**, fora de requisição e sem `ContextoDeEmpresa`. O listener de `resultados` roda com a variável ausente: RLS fail-closed devolve zero linhas, o parcial "não encontra" respostas e termina com sucesso. É o defeito que a AD-20 diz impedir, por um caminho que a AD-20 não cobre (ela fala em "trabalho assíncrono com `empresa_id` na linha", mas a linha do registro do Modulith não tem `empresa_id` por padrão).
- **Colisão 2 (dono):** a tabela de fila "compartilhada" (AD-14) é escrita por `resultados` (parcial), `comunicacao` (outbox), `cadastro` (virada de recesso) e `acesso`. Um time cria uma tabela por módulo, outro uma única compartilhada; o teste de arquitetura da AD-19 ("escrita em tabela só por classe do dono") ou proíbe a compartilhada ou precisa de exceção que não existe. As tabelas de infraestrutura (fila, registro de eventos, ShedLock, idempotência de eventos, chave de idempotência da AD-15) não carregam `empresa_id` e, com `FORCE ROW LEVEL SECURITY` "nas tabelas", ou são ilegíveis ao papel da aplicação ou ficam sem RLS por improviso.
- **Colisão 3 (duas origens do e-mail):** AD-13 diz que a ação grava o e-mail pendente na mesma transação; AD-21/AD-27 dizem que a ação publica evento de domínio e `comunicacao` o consome após o commit. Um time faz `avaliacao` inserir em `comunicacao_outbox` (viola AD-2), outro escuta o evento e insere (outbox em duas etapas, cada uma com retry próprio). A segunda tem uma fila a mais (o registro) cuja falha **não gera alerta**: AD-18 só alerta outbox em `ERRO`.
- **Colisão 4:** ordem. Registro reenvia fora de ordem; "cancelada" pode sair antes de "liberada".

**Texto proposto (novo AD-28, "Infraestrutura assíncrona única"):**
> (1) Existe uma única tabela de trabalho `infra_trabalho` (e `infra_evento_publicado`), dona: módulo `infra` (kernel compartilhado, fora do grafo de negócio), com `empresa_id` obrigatório, `tipo`, `agregado_id`, `sequencia`, estado, tentativas, `proxima_tentativa`. Módulos de negócio só enfileiram pela API do `infra` (a exceção ao teste de dono é a API). (2) Todo consumidor (listener de evento, worker de outbox, job) é invocado por um adaptador que lê `empresa_id` da linha e abre `ContextoDeEmpresa.paraEmpresa`; listener que roda sem o adaptador é proibido (teste de arquitetura sobre `@ApplicationModuleListener` com base `ConsumidorDeEvento`). Reenvio da partida também passa pelo adaptador. (3) AD-13 reescrita: o evento de domínio (canal 3) é a única origem; `comunicacao` cria a linha do outbox no consumidor idempotente; "mesma transação" vale para o registro do evento, não para o e-mail. (4) Ordem por agregado: `sequencia` por `agregado_id`; consumidor descarta/adia evento com sequência menor que a processada. (5) Alertas: registro com itens incompletos há mais de X min; fila com idade máxima; por empresa. (6) Allowlist explícita de tabelas sem RLS (`sessao`, tabelas de infra, `shedlock`), com teste de esquema: toda tabela com `empresa_id` tem política + `FORCE`; toda tabela fora da lista falha o build.

---

## C4. Atraso progressivo (AD-7): password spraying, rajada paralela, escrita em Argon2id e semântica indefinida

**Par:** time `acesso` (login) x time de segurança/testes (aceite do CAP-1).

- **Ambos obedecem:** "Contadores chaveados por (empresa, matrícula) e (IP, empresa, matrícula)"; bloqueio duro por (IP, empresa, matrícula): 5/10 min e 25/20 min; por usuário, de qualquer IP, só atraso progressivo com teto (valores a fixar); sucesso zera; janela expira e não se renova.
- **Colisão 1 (spraying):** um atacante, de um IP, tenta **uma** senha comum em 100 mil matrículas. Cada chave (IP, empresa, matrícula) vê 1 falha; o atraso por usuário mal dispara. Não há limite por IP agregado nem por empresa. A matrícula é previsível (numérica), logo a enumeração é viável. A AD-7 foi desenhada contra o ataque inverso (negação de serviço por usuário) e abriu este.
- **Colisão 2 (rajada):** o atraso é lido antes da verificação da senha, mas o incremento é pós-falha. 1000 requisições paralelas para a mesma matrícula leem contador 0 e executam todas a verificação. O time A implementa read-modify-write (subcontagem); o time B usa `UPDATE … RETURNING`, mas após a verificação.
- **Colisão 3 (semântica do atraso):** (a) sleep na thread: esgota threads (um atacante prende N threads com a matrícula da vítima); (b) rejeitar com 429 enquanto `nao_antes_de > agora`: para a vítima, é o mesmo bloqueio, por até o teto, que a AD-7 diz evitar (e o login correto durante o atraso? aceito ou recusado? ambos os times escolhem um); (c) o atraso diferencia "conta com falhas" de "conta sem falhas" e vaza atividade recente.
- **Colisão 4 (custo):** Argon2id é CPU e memória por tentativa; sem limite por IP, o login é vetor de DoS de CPU. A AD-8 audita cada falha: o volume de auditoria também é amplificável.
- **Colisão 5:** "desbloqueio manual pelo RH" de um bloqueio chaveado por IP: RH não conhece o IP; o que se zera?
- **Colisão 6 (90 dias):** "login forçado à troca" cria um estado de sessão (troca pendente) que a matriz da AD-4 não conhece; cada time libera uns endpoints.

**Texto proposto (tightening da AD-7):**
> Ordem do login, dentro do `REQUIRES_NEW` do canal 2: (1) limite agregado por IP (`janela deslizante`, X tentativas/min a qualquer matrícula e a qualquer empresa) e por empresa (teto de falhas/min), antes de qualquer hash; (2) leitura atômica de `nao_antes_de` por (empresa, matrícula) e por (IP, empresa, matrícula); se no futuro, responde 429 uniforme com `Retry-After` **sem** verificar a senha e **sem** distinguir existente de inexistente; (3) verificação da senha com concorrência máxima por matrícula = 1 (`pg_try_advisory_xact_lock` na chave; perdeu o lock, 429); (4) falha: `INSERT … ON CONFLICT DO UPDATE SET falhas = falhas + 1, nao_antes_de = f(falhas) RETURNING`. O atraso nunca é `sleep` em thread. Sucesso do usuário durante o atraso é recusado só se `nao_antes_de` vier de falhas do **mesmo IP**; de outros IPs, a senha correta é aceita com verificação extra (valor a fixar), para que terceiro não negue acesso. Valores: curva `min(2^(n-3) s, 15 min)` como padrão `[ASSUMPTION]`. Desbloqueio do RH limpa as duas famílias de chave da matrícula. Existe o estado de sessão `TROCA_DE_SENHA_PENDENTE`, com permissão apenas ao endpoint de troca, na matriz da AD-4. Auditoria de falha é amostrada/agregada quando o limite agregado estoura (um registro `BLOQUEIO_EM_MASSA`).

---

## C5. Liberação (AD-23, `REPEATABLE READ`) x `garantirAberta` (AD-19) x `prepararRespostas`

**Par:** time `avaliacao` (liberar) x time `preenchimento` (`prepararRespostas`).

- **Ambos obedecem:** AD-19: a liberação chama `preenchimento.prepararRespostas`; toda escrita em `resposta` obtém `avaliacao.garantirAberta` (valida **estado** e prazo). AD-23: a liberação roda em `REPEATABLE READ`; inconsistência abandona e deixa a avaliação em estado de erro recuperável. AD-19: liberar usa `FOR UPDATE` na linha.
- **Colisão 1:** no momento de `prepararRespostas` a avaliação ainda não é `LIBERADA` (a transição é o resultado da liberação). `garantirAberta` recusa e a liberação se autoimpede; ou o time de `preenchimento` cria `prepararRespostas` sem chamar `garantirAberta` (e então há um caminho de escrita sem guarda, exatamente o que AD-19 quer impedir).
- **Colisão 2:** em `REPEATABLE READ`, `SELECT … FOR UPDATE` em linha alterada por transação concluída depois do snapshot lança `40001` em vez de esperar. Uma prorrogação de prazo, a troca de participante ou outra liberação concorrente derruba a liberação. A regra "inconsistência abandona e deixa em estado de erro" pode tratar o `40001` como erro de negócio (avaliação em ERRO, aviso ao RH por causa benigna), ou como retentável (laço); a AD-23 não diz.
- **Colisão 3:** a ordem de leitura: o snapshot RR nasce na primeira consulta. Se o time faz o `FOR UPDATE` depois de `fotoDosAvaliados`, a foto é anterior ao lock. Nada obriga a tomar o lock antes do primeiro `SELECT`.

**Texto proposto (AD-19/AD-23):**
> `garantirAberta(avaliacaoId, etapa, modo)` tem `modo ∈ {ESCRITA_RESPOSTA, PREPARACAO}`; `PREPARACAO` só é aceito dentro da transação da liberação (flag da própria transação) e exige estado `EM_LIBERACAO`. A liberação é: `BEGIN ISOLATION LEVEL REPEATABLE READ` → **primeira instrução** `SELECT … FOR UPDATE` da avaliação → fotos → snapshot → `prepararRespostas` → estado `LIBERADA`. `40001` e `55P03` são retentáveis com retry limitado (3) antes de qualquer transição para ERRO; estado ERRO só para falha determinística (cargo sem gabarito, inconsistência de versão). Teste: liberar x prorrogar prazo concorrentes termina com uma liberação válida.

---

## C6. Remover participante, cancelar e excluir: sem lock em `avaliacao`, sem FK, resposta órfã

**Par:** história "remover participante" (`avaliacao`) x história "gravar resposta" (`preenchimento`).

- **Ambos obedecem:** AD-19 lista `FOR UPDATE` na avaliação para "concluir, cancelar, inativar e reabrir"; **remover participante não está na lista**, só "passa pela porta `RespostasDoParticipante`, na mesma transação". AD-2: sem FK entre módulos, logo `resposta.participante_id` é só um id.
- **Colisão:** `avaliacao` marca o participante removido, chama `existe/apagar` e comita. Em paralelo `preenchimento` tomou `FOR SHARE` (não bloqueado, ninguém tem `FOR UPDATE`) e insere a resposta do participante removido depois do `apagar`. Resposta órfã que entra no cálculo (a unicidade do banco não protege contra isso). O inverso: `existe` retorna falso e a remoção é permitida enquanto a gravação está em curso (D12 violada).
- **Também:** `etapa_participante` (dona `preenchimento`) não é coberta por `apagar` (a porta fala de respostas).

**Texto proposto (AD-19):**
> Toda operação sobre o conjunto de participantes de uma avaliação (adicionar, remover, trocar, aplicar pendência) toma `FOR UPDATE` na linha da avaliação e valida estado. `RespostasDoParticipante` ganha `existe`, `apagarRespostasEEtapas` e `bloquear(participanteId)`; a escrita de resposta valida, na mesma transação, que `participante.ativo` (lido por `avaliacao.garantirParticipante(participanteId)`, `FOR SHARE`) e não depende só do estado da avaliação. Remoção lógica (`removido_em`), nunca física, para que resposta tardia seja recusada, não órfã. Teste: remover x gravar.

---

## A1. Ordem de locks e chamada nos dois sentidos entre `cadastro` e `acesso` (AD-24 x AD-4/D10)

**Par:** time `cadastro` (desligar funcionário) x time `acesso` (alterar perfil).

- **Ambos obedecem:** AD-24: `cadastro` chama `acesso.aplicarSituacao` na mesma transação. `acesso` alterar perfil exige a regra D10 (ordem entre perfis) e, pela AD-4, escopo de árvore, resolvido pela porta `EscopoOrganizacional` implementada por `cadastro`. Nenhuma AD define ordem de locks.
- **Colisão:** desligar: `cadastro` trava `funcionario` e chama `acesso`, que trava `credencial_acesso` + sessões. Alterar perfil: `acesso` trava `credencial_acesso` e consulta a porta, que lê/trava `funcionario` (para validar situação e árvore). Ordem inversa: deadlock. A virada automática de recesso (D44), um job em lote, multiplica a chance.
- **Também:** criar funcionário pela API de `acesso` exige o **perfil inicial** e a matrícula; quem decide? A AD-24 diz que perfil só muda por `acesso`, e a importação de planilha (AD-17, em `cadastro`) traz a coluna de perfil (CRI/ACE). Também gera dois eventos de link de senha (um de `cadastro`, outro de `acesso`).

**Texto proposto (AD-24):**
> Ordem de lock entre módulos: `funcionario` → `credencial_acesso` → `sessao`. A porta `EscopoOrganizacional` é de **leitura sem lock** (snapshot da transação) e não pode ser chamada com lock de `credencial_acesso` mantido para escrita; `acesso` resolve o escopo **antes** de travar a credencial. Criação: `cadastro.criarFuncionario(dados, perfilInicial)` chama `acesso.criarCredencial(funcionarioId, matricula, perfilInicial)`; só `acesso` emite o evento de link de senha; `cadastro` não emite evento de credencial. Importação em lote: transação por linha ou por lote pequeno com relatório por linha (AD-17), nunca uma transação por planilha.

---

## A2. Destinatário do evento: `acesso` não pode resolvê-lo (AD-27 x grafo x AD-22)

**Par:** time `acesso` (link de senha) x time `comunicacao`.

- **Ambos obedecem:** AD-27: o emissor resolve "destinatários já resolvidos (id, e-mail, nome, preferência)" via `cadastro`. Grafo e regra do desenho: `acesso` **não chama nenhum módulo**. AD-22: `acesso` emite o token no envio.
- **Colisão:** `acesso` precisa do e-mail e do nome para o evento "link de senha" e não pode obtê-los. Um time lê `funcionario` por SQL (AD-1/AD-2 quebradas); outro põe uma aresta `acesso --> cadastro` (exige editar a espinha, e cria ciclo com `cadastro --> acesso`); outro deixa o e-mail fora do evento e `comunicacao` consulta `cadastro` (viola AD-27).
- **E-mail velho:** o destinatário é congelado na emissão, o envio é tardio (backoff, reenvio). Se o funcionário mudou o e-mail, o **link de redefinição de senha vai ao endereço antigo**. Para D56, o endereço antigo é um vetor de tomada de conta.
- **Tentativa duplicada:** o worker envia ao SMTP, cai antes de marcar `ENVIADO`, reenvia: novo token invalida o anterior (AD-22) e o usuário recebe dois e-mails, o primeiro inútil. RH reenviando em série invalida repetidamente.
- **SMTP dentro da transação:** AD-13 descreve `SELECT … FOR UPDATE SKIP LOCKED`, envio e só então marcar `ENVIADO`: conexão e lock durante E/S de rede, com o contexto de empresa de um item.

**Texto proposto (AD-27/AD-22/AD-13):**
> Eventos de `acesso` carregam apenas `funcionario_id`; o **adaptador de entrada de `comunicacao`** resolve nome/e-mail/preferência chamando a API de `cadastro` (aresta `comunicacao --> cadastro` declarada no grafo), no consumidor, sob `paraEmpresa`. Para evento de credencial (link de senha), o e-mail é lido **no momento do envio**, nunca copiado. O outbox faz: tx1 reserva (`estado = ENVIANDO`, `tentativa_id`), fim da tx; envio ao SMTP **fora** de transação; tx2 marca `ENVIADO`/`ERRO`. Reserva expira por lease. O token é emitido na tx1 com `tentativa_id` e só o último vale; reenvio manual do RH tem intervalo mínimo e é auditado.

---

## A3. Tardio e snapshot: AD-23 contradiz AD-9 e cria dois escritores do snapshot

**Par:** time `preenchimento` x time `avaliacao`.

- **Ambos obedecem:** AD-9: `preenchimento` e `resultados` leem **só** o snapshot, nunca `catalogo`. AD-23: participante tardio com cargo ausente do snapshot lê a versão fixada por `catalogo.obterGabarito(versaoId, cargoId)`; respostas referenciam `snapshot_pergunta_id`.
- **Colisão:** quem lê `catalogo` na hora de preencher? `preenchimento` (viola AD-9) ou `avaliacao` (copia sob demanda ao snapshot na entrada do tardio, mas então há dois momentos de escrita do snapshot, com `snapshot_pergunta_id` criado no futuro e sem a transação `REPEATABLE READ` da liberação). Concorrência: dois tardios do mesmo cargo ausente copiam o mesmo gabarito duas vezes (duplicação de `snapshot_pergunta` ou violação de unicidade). `resultados` calcula min-max com um conjunto de perguntas que **cresce** depois do primeiro parcial.
- **Também:** "peso do momento da entrada" (ELI-10) diz que o peso também pode ser lido do catálogo corrente; sem decisão o time A usa o peso da versão, o B o do dia.

**Texto proposto (AD-9/AD-23):**
> O snapshot é escrito por um único serviço de `avaliacao` (`SnapshotService.completarParaCargo(avaliacaoId, cargoId)`), invocado na liberação e na entrada de participante tardio, sob o `FOR UPDATE` da avaliação, idempotente por (avaliacao, cargo, versao_catalogo_id) com unicidade no banco. `preenchimento` e `resultados` nunca chamam `obterGabarito`. Enquanto ELI-10 estiver aberto, o peso é o da **versão fixada**. O motor de nota trata o snapshot como crescente: a função é pura sobre o snapshot completo no instante de leitura.

---

## A4. Resposta, etapa e consenso: chaves divergentes, "completa" x "finalizada", N+1

**Par:** time `preenchimento` x time `resultados`.

- **Ambos obedecem:** AD-9: unicidade **participante × pergunta × agente**. AD-26: **participante × `snapshot_pergunta_id` × etapa ∈ {AV1, AV2, AV3}**; consenso Av1=Av2 derivado na leitura; `progressoDaEtapa` é a única definição de completa. AD-10: situação de preenchimento é derivada, nunca gravada; AD-19: `etapa_participante` guarda início e conclusão (gravados).
- **Colisão 1:** "agente" (autoavaliação, gestor, par, subordinado) e "etapa" são eixos diferentes; as duas unicidades coexistem em tabelas distintas do mesmo módulo ou, pior, em equipes que lêem uma AD ou outra.
- **Colisão 2 (AV3):** se consenso é derivado quando Av1=Av2, a linha AV3 só existe para divergentes? Então `progressoDaEtapa(AV3)` "toda pergunta da etapa" é definida sobre as divergentes (depende da regra de "igual": mesmo valor? mesma graduação? dois NPA? NPA x nota?). `resultados` implementa a igualdade por valor da escala, `preenchimento` por `graduacao_snapshot_id`. Resultado diferente de nota com escalas que repetem valor.
- **Colisão 3:** "completa" (derivada) x "finalizada" (gravada em `etapa_participante`). Acompanhamento usa uma, o gatilho do parcial (AD-11 "ao finalizar") outra; reabertura (D32) edita respostas de etapa finalizada.
- **Colisão 4 (N+1):** `progressoDaEtapa(participanteId, etapa)` é por participante; acompanhamento paginado e filtrado por escopo (AD-25) chama N vezes, ou cada time escreve sua agregação SQL (o que a AD-26 proíbe).

**Texto proposto (AD-26):**
> A chave canônica é única: `(participante_id, snapshot_pergunta_id, etapa)`; "agente" é atributo do participante, não da chave; AD-9 passa a citar esta chave. Define-se `Igualdade(Av1, Av2)` em `preenchimento` (mesmo `graduacao_snapshot_id`, ou ambos NPA; nunca por valor numérico) e é a função exposta para `resultados`. AV3 contém linha apenas para divergentes e `progressoDaEtapa` as enumera. Dois estados, ambos com dono: `completa` (derivado, `progressoDaEtapa`) e `finalizada` (gravada, só alcançável se `completa`); reabertura apaga `finalizada`. API em lote: `progressoDasEtapas(participanteIds[], etapa)` devolve mapa com `atualizadoEm`, e a listagem de acompanhamento usa a página de `avaliacao.participantes(escopo)` e uma única chamada em lote.

---

## A5. Coordenada de escopo desnormalizada (AD-25 x AD-2): quem mantém a cópia

**Par:** time `pdi` x time `cadastro`.

- **Ambos obedecem:** AD-25: toda tabela que precisa de escopo guarda `unidade_id, departamento_id`; PDI sem avaliação e funcionário usam `atual`. AD-2: só o dono escreve; sem join entre módulos.
- **Colisão:** `acao_pdi.unidade_id` é cópia de `funcionario`. Quando `cadastro` move o funcionário, só um evento assíncrono avisa `pdi` (a AD-24 disse que evento não serve onde o efeito deve ser imediato; a AD-25 usa evento exatamente nesse caso). Janela de inconsistência: o ex-gestor ainda vê o PDI; o novo gestor não. Dois movimentos seguidos chegam invertidos (sem ordem, ver C3). Um time faz `pdi` consultar `cadastro` a cada leitura (volta ao N+1 e ao filtro em memória que a AD-25 queria evitar), outro mantém cópia.
- **Também:** PDI "sem avaliação" que depois se vincula a uma avaliação muda de `atual` para `congelada`: a linha precisa trocar de regra, sem decisão sobre qual coordenada vale a partir de então. Campanha não tem "avaliado": o que significa `congelada` para campanha?
- **Conjunto de pares grande:** `WHERE (u,d) IN (...)` com milhares de pares (par e descendentes) quebra o plano; a AD-25 não limita.

**Texto proposto (AD-25):**
> Coordenada `atual` não é copiada: tabelas com regra `atual` consultam `EscopoOrganizacional.pessoasNoEscopo` como **relação materializada em tabela temporária por requisição** (a porta devolve também uma `visão de escopo` consumível por join no módulo consumidor, fora da restrição do AD-2, que fica restrita a tabelas de negócio). Coordenada `congelada` é carimbada na criação por `avaliacao` e nunca atualizada. A regra de coordenada de cada recurso entra na matriz (AD-4) como coluna obrigatória, com a transição PDI avulso → vinculado documentada: o vínculo grava a coordenada congelada da avaliação. Campanha usa o escopo do criador. Limite: o escopo é passado como identificador de subárvore (nó + profundidade), não como lista de pares.

---

## A6. Anonimato (AD-12): diferenciação pela nota final e vazamento via acompanhamento

**Par:** time `resultados` (painel) x time de acompanhamento (U5).

- **Ambos obedecem:** AD-12: abaixo do limiar o agente vira "outros avaliadores"; "a nota final usa todos os pesos". U5/AD-11: progresso por avaliador e pendências, da mesma fonte.
- **Colisão 1 (diferença):** se só um grupo (ex.: subordinados, n=2) cai abaixo do limiar, "outros avaliadores" contém **um** grupo: identificável. Mesmo com dois grupos, nota final menos os grupos visíveis resolve a contribuição do oculto (o peso é conhecido).
- **Colisão 2:** o limiar conta respondentes por agente; o NPA reduz o n **por pergunta/competência** abaixo do limiar sem que o agente caia.
- **Colisão 3:** o parcial (defasado) mostra média por grupo com 2 de 3 finalizados; a lista de acompanhamento mostra quem finalizou. A união identifica as notas.
- **Colisão 4:** `consolidado imutável` (AD-11) com anonimização aplicada na leitura: mudar o limiar depois altera o que se vê do consolidado imutável.

**Texto proposto (AD-12):**
> O limiar é aplicado por (agente, competência/pergunta) sobre respondentes **finalizados com nota** e o agrupamento "outros" exige ao menos dois grupos abaixo do limiar ou é fundido com o próximo menor até atingir o limiar. A nota final do avaliado é exibida junto das notas por agente apenas se a diferença não permitir isolar um grupo suprimido (supressão complementar). Parcial e progresso nominal não podem ser exibidos juntos: o acompanhamento nominal mostra só contagem por agente enquanto a avaliação não está `CONCLUIDA`. O limiar efetivo é gravado no consolidado.

---

## M1. Papéis de banco e jobs de plataforma: quem usa o quê

**Par:** time de infraestrutura x time `acesso` (administrador de plataforma).

- AD-3: "jobs e plataforma usam papel próprio"; AD-20: segundo papel somente leitura para descobrir empresas; AD-8: dono da auditoria distinto; AD-21: papel de manutenção que "se registra"; "papel/política de banco explícito" para o administrador de plataforma. São pelo menos cinco papéis, sem tabela de privilégios.
- **Colisão:** a requisição HTTP do administrador de plataforma abre transação sob qual papel? Se troca de DataSource, a camada 1 (filtro de empresa do repositório base) não tem empresa de sessão; cada time contorna de um jeito. `paraEmpresa` chamado **dentro** de uma transação já com outra empresa sobrescreve `SET LOCAL` e vaza dado entre tenants no meio do caso de uso.
- Importação assíncrona (AD-17): o job roda como `sistema:<job>` (AD-20) mas deve respeitar o escopo AD-4 do usuário que enviou o arquivo, e a AD-8 pede "quem". Um time grava o usuário original, outro `sistema`.
- Partição mensal da auditoria: `DROP` de partição exige ser dono; o dono é "papel diferente do da aplicação", e a AD-21 cria um papel de manutenção: é o mesmo? E `FORCE RLS` sobre a auditoria vs expurgo.

**Texto proposto (nova AD-29, "Papéis de banco"):**
> Tabela única de papéis: `app_rw` (RLS, DML sem `BYPASSRLS`, `INSERT/SELECT` em auditoria), `app_descoberta` (leitura de colunas enumeradas das filas), `plataforma_ro` e `plataforma_rw` (usados só por endpoints de plataforma, com `DataSource` próprio e política RLS explícita para esses papéis), `auditoria_manutencao` (dono das partições, `DROP` apenas). `paraEmpresa` dentro de transação com contexto diferente lança exceção (só pode repetir a mesma empresa). Jobs de importação executam com `ator = usuario:<id>` e `executado_por = sistema:<job>`; o escopo AD-4 usa o usuário, revalidado no processamento.

---

## M2. Estados duplicados: matriz (AD-4) x tabela de transições (AD-10); reabrir x `CONCLUIDA` final x consolidado único

**Par:** time `avaliacao` x time de autorização.

- AD-4 define "perfil × recurso × ação × escopo" e carrega D52, D17, D32, D22, D12. AD-10 define a tabela de transições com "estado de origem, perfil, condição, efeito". Perfil e condição estão nas duas: dois times mantêm duas fontes que podem discordar (a célula da matriz permite reabrir, a tabela de transições recusa, ou o contrário). O gerador de testes por célula cobre a matriz, não as transições.
- AD-10: `CONCLUIDA` é final. AD-19 e AD-4 falam de **reabrir** (D32); AD-11: consolidado com `INSERT` único por avaliação. Se "reabrir" se aplica a avaliação concluída, o consolidado único bloqueia o novo; se se aplica à etapa do participante (D32), a AD-10 não a lista como transição e a AD-19 ata `FOR UPDATE` à avaliação.

**Texto proposto (AD-10/AD-4):**
> A tabela de transições referencia a célula da matriz (`acao_id`) para perfil/escopo; só acrescenta estado de origem, condição de dados e efeito. Gerador de teste por transição (origem × perfil × condição). Define-se o objeto de `reabrir` (etapa do participante, não avaliação) e se `CONCLUIDA` aceita alguma exceção; se sim, consolidado versionado (`versao`), não `INSERT` único.

---

## M3. Espera de drenagem do parcial (AD-11) x fila única (AD-14) x `SKIP LOCKED`

**Par:** time `resultados` x infraestrutura.

- AD-11: o recálculo total "espera drenar os parciais"; AD-14: parcial, outbox e jobs compartilham a mesma fila e pool de workers; AD-13: `SKIP LOCKED`.
- **Colisão 1:** o total é um item da mesma fila que espera os parciais. Com pool de workers pequeno, todos ocupados por totais esperando parciais enfileirados: inanição/deadlock de aplicação. Quem espera bloqueando thread, quem reagenda.
- **Colisão 2:** "drenou" medido por contagem de itens pendentes: itens travados por outro worker são invisíveis por `SKIP LOCKED`; o total conclui com parcial em curso.
- **Colisão 3:** "job de parcial verifica `NOT EXISTS consolidado` na mesma transação" em `READ COMMITTED` não é seguro: o total comita entre a verificação e a escrita do parcial.

**Texto proposto (AD-11/AD-14):**
> O total não espera: reagenda-se (`proxima_tentativa`) enquanto existirem itens de parcial da avaliação em qualquer estado, consultados por `empresa_id, agregado_id` sem `SKIP LOCKED`. Filas separadas por classe de trabalho e cotas de workers (total não consome a cota de parcial). A exclusão parcial x total usa um `pg_advisory_xact_lock(avaliacaoId)` tomado por ambos, ou o parcial grava com versão e o consolidado descarta qualquer parcial posterior.

---

## M4. Pequenas colisões de contrato (conjunto)

- **Chave de idempotência (AD-15) e `ETag`:** a tabela de chaves não tem dono nem `empresa_id` (colisão entre tenants com a mesma chave); o escopo da chave deve ser (empresa, usuário, operação). `ETag` de listas e de agregados com filhos (participantes) não está definido: o time A versiona só a linha, B o agregado.
- **Fuso do prazo (AD-10):** `Prazo.vencido(dataLimite, empresa)` lê o fuso corrente de `cadastro`; mudar o fuso da empresa desloca prazos de avaliações em curso. Gravar o fuso no snapshot da liberação.
- **Coordenadas AD-3/AD-20:** nome da variável de sessão (`app.empresa_id`) aparece só na AD-20; a política de RLS escrita por cada migração usa um nome diferente se não for fixado. Fixar nome e `current_setting('app.empresa_id', true)` com `NULLIF`.
- **Retenção por partição (AD-21):** retenção global mensal x retenção contratual por empresa: não são compatíveis; ou todas as empresas têm o mesmo prazo ou o expurgo por empresa precisa de `DELETE` (que o gatilho da AD-8 recusa).
- **Modulith e injeção circular:** `avaliacao` injeta a porta implementada por `preenchimento`, que injeta `avaliacao`; `verify()` pode passar e o contêiner Spring recusar (ciclo de beans) ou resolver com `@Lazy` improvisado. Regra: portas inversas são resolvidas por `ObjectProvider`/evento de aplicação, com teste de partida.

---

## Interações entre AD-19..27 (resumo)

| Interação | Efeito | Achado |
| --- | --- | --- |
| AD-11 (contador) x AD-19 (`FOR SHARE`) x AD-15 (`ETag`) | deadlock de upgrade; 412 permanente | C1 |
| AD-21 (`REQUIRES_NEW`) x AD-20 (`SET LOCAL`) x AD-7 (contador) | auditoria/contador sem empresa; self-deadlock; pool | C2 |
| AD-21 (registro) x AD-20 x AD-14 x AD-13 | consumidor sem empresa; fila sem dono; origem dupla do outbox | C3 |
| AD-7 (atraso) x AD-21 x AD-8 | spraying, rajada, DoS de Argon2/auditoria | C4 |
| AD-23 (RR) x AD-19 (`garantirAberta`) | liberação se auto-bloqueia ou erra com `40001` | C5 |
| AD-19 (sem FK AD-2) x remoção | resposta órfã | C6 |
| AD-24 x AD-4/D10 x porta AD-25 | ordem de locks cruzada | A1 |
| AD-27 x grafo x AD-22 | emissor `acesso` sem destinatário; e-mail antigo | A2 |
| AD-23 x AD-9 | tardio lê catálogo; dois escritores do snapshot | A3 |
| AD-26 x AD-9 x AD-10 x AD-11 | chave, igualdade, completa x finalizada | A4 |
| AD-25 x AD-2 | cópia de coordenada sem mantenedor | A5 |
| AD-12 x AD-11 x U5 | diferenciação | A6 |

## Ordem sugerida de correção

1. C1, C2, C3 e C5 antes de qualquer código de `preenchimento`, `avaliacao` e infraestrutura (mudam o desenho de tabelas e do `ContextoDeEmpresa`).
2. C4 junto do primeiro corte de `acesso` (critério de aceite do CAP-1 deve incluir o teste de spraying e de rajada).
3. C6, A1 a A4 antes dos contratos OpenAPI/portas congelarem.
4. A5, A6, M1 a M4 antes da UX de painéis (U3) e de acompanhamento (U5).
5. Novas ADs sugeridas: AD-28 (infra assíncrona única), AD-29 (papéis de banco), mais o apertado em AD-7, AD-9, AD-10, AD-11, AD-12, AD-19, AD-21, AD-23, AD-24, AD-25, AD-26 e AD-27.
