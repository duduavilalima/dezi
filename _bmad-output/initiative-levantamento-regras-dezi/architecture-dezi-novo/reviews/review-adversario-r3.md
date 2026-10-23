---
name: 'Revisão adversária da espinha de arquitetura do DEZI novo (rodada 3)'
target: architecture-dezi-novo.md (AD-1 a AD-30, status draft, atualizada em 2026-10-21)
method: pares de unidades um nível abaixo (módulos/times/histórias) que cumprem cada AD à letra e constroem peças incompatíveis; só achados novos ou correções da rodada 2 que falharam
created: '2026-10-21'
supersedes: review-adversario.md (rodada 2)
---

# Revisão adversária, rodada 3

## Veredito: NÃO PASSA (para liberar times em paralelo)

A rodada 2 foi absorvida (AD-19 a AD-28 fecham os 16 achados em texto). Os problemas restantes vêm de três fontes: (1) **o grafo do diagrama, declarado lista verificada (AD-27), não contém arestas que AD-4, AD-8, AD-13 e AD-25 exigem**; (2) **correções recentes sem lugar para viver** (motivo fechado do AD-8, ordenação por `sequencia` do AD-28, corte por instante do AD-11, AD-30 sem mapa de cópias de dado pessoal); (3) lacunas de bootstrap (resolver empresa no login, enumerar empresas para jobs periódicos, papéis de banco versus migrações).

Total: 5 críticos (C), 8 altos (A), 9 médios (M), 3 baixos (B). Convenção: **Par** = duas unidades; **Ambos obedecem** = ADs citadas; **Colisão** = o que quebra; **Correção** = aperto sugerido (a espinha não foi alterada).

---

## C1. O grafo "verificado" não tem as arestas que as ADs exigem (AD-27/AD-1 x AD-4, AD-8, AD-13, AD-25)

**Par:** time `resultados` x time `comunicacao` x time `avaliacao`/`preenchimento`/`pdi`.

- **Ambos obedecem:** AD-27 e AD-1: o diagrama é a lista verificada; aresta nova exige editar a espinha; `verify()` quebra o build por aresta fora do grafo. AD-8 manda auditar leitura de resultado individual e de comentário (`resultados`) e AD-13 manda auditar reenvio manual do RH (`comunicacao`). AD-4 manda checagem central de matriz e AD-25 manda a porta de escopo, ambas de `acesso`.
- **Colisão 1 (auditoria):** o diagrama tem `RegistroDeAuditoria` para acesso, cadastro, avaliacao, catalogo, preenchimento e pdi. **Faltam `resultados` e `comunicacao`**, embora o texto diga "todo módulo chama auditoria". Quem implementa o "ler resultado" obedece AD-8 e quebra o `verify()`, ou obedece o grafo e não audita (defeito do spec D54). Cada time decide.
- **Colisão 2 (política e escopo):** só `cadastro` e `comunicacao` apontam para `acesso_api`. Nenhum de `avaliacao`, `preenchimento`, `resultados`, `pdi`, `catalogo` tem aresta para a política (AD-4 "um ponto central") nem para a porta que devolve o escopo (AD-25). Ou a checagem vive em filtro de `infra` (que não pode depender de `acesso`, AD-28), ou cada módulo ganha aresta não declarada. `acesso` "não chama nenhum módulo", mas os módulos também não chamam `acesso`: o consumidor do escopo não tem de quem obtê-lo.
- **Colisão 3 (join):** AD-25 manda `resultados` e `pdi` consumirem o escopo por join, mas `resultados` não tem aresta para `cadastro` (que implementa a porta e hospeda a hierarquia).
- **Colisão 4 (eventos):** CAP-10 envia aviso de PDI (D22), reabertura (D32) e erro de liberação (D4); só há "consome tipos de evento" de `avaliacao`, `acesso` e `cadastro`. Eventos de `pdi` e `preenchimento` não têm aresta. A confirmar no spec quais disparam e-mail, mas o grafo não admite nenhum.

**Correção:** redesenhar o grafo a partir dos AD-4/8/13/25 (arestas `* --> acesso_api` para política e escopo, `resultados`/`comunicacao --> auditoria`, `resultados --> cadastro` ou a visão de escopo publicada por `acesso`, `comunicacao -. eventos .-> pdi/preenchimento` se aplicável) e gerar o teste do `verify()` do próprio diagrama. Declarar onde mora o ponto central de checagem (`acesso`, API pública `autorizar(recurso, acao, escopo)`).

---

## C2. Motivo de auditoria virou código fechado: o "por quê" do negócio ficou sem lugar (AD-8/AD-30 x AD-10, D26, D32, D54)

**Par:** time `avaliacao` (cancelar/reabrir) x time `auditoria`.

- **Ambos obedecem:** AD-8: motivo é código de lista fechada, nunca texto livre. AD-10: "motivo obrigatório onde o spec exige (cancelar, reabrir)". D26: "exige motivo e registra quem, quando e por quê"; D32: "com motivo e auditoria".
- **Colisão 1:** o motivo que o RH digita (D26/D32) não cabe na trilha. Um time grava o texto livre numa coluna de `avaliacao` e o evento v1 o carrega (viola AD-27 "sem PII gorda" e vira dado pessoal fora do catálogo do AD-30); outro o descarta (D26 deixa de ser cumprido); outro o grava na própria auditoria como "código" `OUTRO` com texto (reabre o furo que o AD-30 fechou).
- **Colisão 2 (dono da lista):** quem é dono da lista fechada? Se vive na `api` de `auditoria`, `auditoria` conhece o vocabulário de todos os módulos (acoplamento reverso, edição em módulo alheio a cada novo caso). Se cada módulo registra o seu como string, a "lista fechada" é só convenção e o teste não a pode impor.
- **Colisão 3:** falha de login com matrícula inexistente (AD-21 canal 2): "quem" não tem id; grava-se a matrícula digitada, texto escolhido pelo atacante, na trilha imutável, contra o AD-30 "auditoria guarda ids".

**Correção:** separar dois conceitos. Auditoria: `codigo_motivo` (enum publicado em `auditoria.api`, um bloco por módulo, validado em tempo de compilação) mais `referencia_justificativa_id`. Justificativa livre: tabela própria do agregado dono (`avaliacao_transicao.justificativa`), com permissão de leitura na matriz, fora do evento e dentro do catálogo do AD-30. Falha de login com matrícula desconhecida grava hash/ HMAC da matrícula digitada, nunca o valor.

---

## C3. `sequencia` por agregado perde evento em silêncio (AD-28 x AD-21 canal 3, AD-27)

**Par:** time `acesso`/`cadastro` (emissores) x time de consumidores (`comunicacao`, `resultados`).

- **Ambos obedecem:** AD-28: `sequencia` por `agregado_id`; consumidor "adia ou descarta evento com `sequencia` menor que a processada". AD-21: o evento é registrado na mesma transação da ação.
- **Colisão 1 (ordem de commit):** a `sequencia` é atribuída no insert, não no commit. T1 recebe 4, T2 recebe 5, T2 comita primeiro, o consumidor processa 5, T1 comita, o consumidor vê 4 < 5 e **descarta**. "Cancelada" ou "liberada" some sem erro, sem alerta (AD-28 só alerta evento incompleto, e este foi "processado e descartado"). Nem a leitura "adia" resolve: o consumidor não distingue lacuna de evento ainda não comitado.
- **Colisão 2 (hot row):** se a `sequencia` vem de contador por `agregado_id` (`UPDATE ... RETURNING`), toda ação sobre o mesmo agregado serializa e vira ponto de deadlock com a ordem de locks do AD-19.
- **Colisão 3 (escopo do agregado):** `agregado_id` de eventos de dois emissores sobre o mesmo funcionário (`cadastro` e `acesso`) usa o mesmo espaço ou não? Cada time escolhe.
- **Colisão 4 (estado por consumidor):** o mesmo evento é consumido por `comunicacao` e por `resultados`. Se `infra_evento_publicado` tem uma coluna `estado`/`tentativas` por evento (AD-28), falha de um consumidor mantém o evento "incompleto" para ambos; se tem linha por (evento, consumidor), isso não está dito. Consumidor venenoso bloqueia a ordem do outro.

**Correção:** ordem não é garantida por `sequencia` de inserção. Ou (a) o evento é idempotente e comutativo (o consumidor relê o estado atual do agregado, e a ordem não importa), ou (b) a sequência é a versão do agregado dono, lida sob o lock que o AD-19 já toma, não um contador à parte. Proibir "descartar": só "reprocessar com estado atual". Definir `infra_evento_consumo(evento_id, consumidor, estado, tentativas)` como tabela de `infra`, com alerta por consumidor.

---

## C4. Corte do total por instante com relógio de aplicação (AD-11 x AD-10 `Relogio`, 2 instâncias)

**Par:** time `preenchimento` (grava `atualizado_em`) x time `resultados` (total).

- **Ambos obedecem:** AD-11: o corte "é o instante da transição para CONCLUIDA, gravado pela própria transição, e o total lê as respostas visíveis até ele". AD-10: `Relogio` injetado; AD-18/Ambientes: duas instâncias do backend.
- **Colisão 1 (skew):** `atualizado_em` da resposta vem do `Relogio` de uma instância; o corte, de outra (ou do `now()` do banco, que é o início da transação). Com skew de segundos, uma resposta legítima comitada antes do `FOR UPDATE` da conclusão tem timestamp posterior ao corte e **sai do consolidado imutável**. O defeito é permanente (D55).
- **Colisão 2:** `now()` do Postgres é início da transação, não o instante em que o lock foi obtido. A transição que esperou o lock tem corte anterior a respostas já comitadas.
- **Colisão 3:** "visíveis até ele" não define o critério (timestamp? `xmin`? versão?). Dois times implementam dois critérios.

**Correção:** o corte não é timestamp. A conclusão, sob `FOR UPDATE`, grava `corte_xid` (`pg_current_snapshot()`/`txid_current()`) ou, mais simples, o total roda na própria transação da conclusão sob o lock (que já exclui gravações) e lê tudo. Se for assíncrono, a regra é "todas as respostas da avaliação", justificada pelo lock, sem filtro por tempo.

---

## C5. Login precisa resolver empresa, mas `acesso` não chama ninguém (AD-3/AD-7 x regra do desenho, AD-24)

**Par:** time `acesso` (login) x time `cadastro` (`empresa`).

- **Ambos obedecem:** AD-3: o login recebe empresa e matrícula "para resolver a empresa antes de existir sessão". Regra do desenho: `acesso` não chama nenhum módulo. `empresa` é de `cadastro` (entidades centrais). AD-21: login com empresa inexistente tem tratamento próprio, o que pressupõe saber que não existe. AD-7: limite agregado por empresa antes de qualquer hash.
- **Colisão:** quem converte o texto da empresa (slug, código, CNPJ?) em `empresa_id`? `acesso` não pode ler `empresa`, nem chamar `cadastro`. Um time duplica a tabela em `acesso` (dois donos, sem sincronização; empresa suspensa continua logando), outro cria aresta `acesso --> cadastro` (ciclo, edita a espinha), outro põe `empresa_codigo` em `credencial_acesso` (não detecta empresa inexistente por outro caminho que não seja "nenhuma credencial", o que muda o 401 uniforme do AD-5).
- **Também:** estado da empresa (inativa, suspensa, contrato encerrado) não tem dono nem regra no login. Administrador de plataforma não tem empresa.

**Correção:** porta `ResolucaoDeEmpresa` declarada por `acesso` e implementada por `cadastro` (mesmo padrão da `EscopoOrganizacional`), leitura sem lock, devolvendo `empresa_id` e situação; fixar o identificador público de login da empresa (único, no `cadastro`). Resposta uniforme ao cliente para empresa inexistente.

---

## A1. Jobs periódicos não têm como enumerar empresas (AD-14/AD-20/AD-29 x AD-3)

**Par:** time `cadastro` (virada de recesso D44) / `acesso` (troca de 90 dias, expiração de link, purga de sessão) x infraestrutura.

- **Ambos obedecem:** AD-14: ShedLock, uma instância executa. AD-20: descobrir empresas "com trabalho pendente" usa `app_descoberta`, que lê só as colunas enumeradas **das tabelas de fila**. AD-3: varreduras entre empresas usam "o papel de plataforma e só leem ids"; AD-29: `plataforma_*` só para endpoints de plataforma.
- **Colisão:** um job periódico precisa gerar o trabalho (não há item de fila ainda): "quais empresas têm funcionário em recesso a virar hoje?". `app_descoberta` não lê `funcionario` nem `empresa`; `plataforma_ro` é proibido a job (AD-29); `app_rw` sob RLS fail-closed devolve zero linhas. Um time dá `SELECT` em `empresa` a `app_descoberta` (fora da lista de colunas), outro itera com `plataforma_ro` (contra o AD-29), outro faz o job rodar por empresa a partir de uma lista de configuração (sem empresa nova entrar).
- **Contradição de texto:** AD-3 diz "papel de plataforma" para varreduras; AD-29 e AD-20 dizem `app_descoberta`.

**Correção:** `infra` enumera empresas ativas por `app_descoberta` (`empresa.id` e situação, política RLS explícita para esse papel) e enfileira um item por (empresa, job); cada job roda sob `paraEmpresa`. Corrigir AD-3 para citar `app_descoberta`.

---

## A2. Visão de escopo por join: forma indefinida e contra AD-1 (AD-25 x AD-1, AD-2)

**Par:** time `pdi` x time `resultados`.

- **Ambos obedecem:** AD-25: a porta devolve "identificador de subárvore (nó e profundidade), consumível por join na consulta do módulo consumidor, fora da restrição de join do AD-2". AD-1: nenhum módulo lê tabela de outro; `verify()` e ArchUnit.
- **Colisão 1:** expandir (nó, profundidade) em pares (unidade, departamento) exige a tabela de hierarquia de `cadastro` (fechamento transitivo ou CTE recursiva). Cada consumidor escreve o seu SQL nativo contra tabelas de `cadastro` (viola AD-1 e acopla o esquema), ou `cadastro` publica uma view cujo nome, colunas e RLS não estão definidos. As exceções "só para esta porta de leitura" não têm teste que a distinga de um acesso indevido.
- **Colisão 2:** a coordenada `congelada` é par (unidade, departamento) do momento da liberação; o escopo do gestor é expandido na árvore **atual**. Unidade renomeada, fundida ou extinta deixa a coordenada congelada fora de qualquer subárvore atual: o resultado some do gestor para sempre.
- **Colisão 3:** a profundidade de "par e descendentes" (D58) com o par (unidade, departamento) como nó: o que é "descendente" de um par (desce na unidade, no departamento, ou ambos) não está fixado.

**Correção:** `cadastro` publica uma única view `escopo_visivel(empresa_id, no_escopo_id, unidade_id, departamento_id)` como parte da API pública (nome, colunas, política RLS fixos, teste de esquema), e a regra de expansão (D58) fica em `cadastro`. A coordenada congelada guarda também o id do nó da hierarquia, não só o par; unidade extinta mantém o nó como histórico.

---

## A3. Cópias de dado pessoal fora de `funcionario` (AD-30 x AD-27, AD-21, AD-22, AD-13)

**Par:** time `comunicacao` / `acesso` x time de LGPD.

- **Ambos obedecem:** AD-30: eliminação ou anonimização "anonimiza `funcionario` e referências"; catálogo de dados pessoais por tabela e retenção por categoria. AD-27: `comunicacao` "guarda só o necessário ao envio" (nome, e-mail). AD-21: contadores por (IP, empresa, matrícula); auditoria de login com IP e empresa. AD-13: mensagem interna sempre gravada.
- **Colisão 1:** quem executa a anonimização? `funcionario` é de `cadastro`; `credencial_acesso` (matrícula, TOTP cifrado), `sessao` (IP) e contadores de login são de `acesso`; outbox e mensagem interna, de `comunicacao`; `resposta.observacao` (texto livre do avaliador, ~identifica o titular) é de `preenchimento`. Não há porta de eliminação por módulo nem ordem de execução; cada um anonimiza o seu, ou ninguém.
- **Colisão 2:** a trilha imutável contém IP e identificador de login (dados pessoais), contra "auditoria guarda ids". Sem definir IP como categoria com retenção própria (a partição mensal é única).
- **Colisão 3:** AD-24 exige unicidade de e-mail entre ativos e matrícula por empresa: o valor anonimizado precisa ser único e não reversível; o formato não está dito, e dois módulos o geram de formas diferentes.

**Correção:** porta `EliminacaoDeTitular { anonimizar(funcionarioId) }` declarada por `cadastro`, implementada por cada módulo com dado pessoal, com a ordem e o registro na auditoria; o catálogo do AD-30 é um artefato testado (toda coluna marcada pessoal tem tratador). IP de auditoria: truncar/hashear. Texto livre de `observacao` e mensagem interna entram no catálogo.

---

## A4. Os caminhos de verificação de senha fora do login escapam do AD-7 (AD-7 x D53, AD-7 reautenticação)

**Par:** time `preenchimento` (aceite do consenso, D53) / `acesso` (reautenticação do AD-8) x time `acesso` (login).

- **Ambos obedecem:** AD-5: aceite do consenso "exige a senha da própria parte, na própria sessão". AD-7: reautenticação recente para ações de plataforma. AD-7: toda a ordem de limites, contadores e `pg_try_advisory_xact_lock` é descrita "do login".
- **Colisão:** o endpoint de aceite verifica senha com sessão válida. Sem regra, ele não conta falhas, não aplica o limite agregado, não passa pelo lock por matrícula e roda Argon2id sem teto: é um oráculo de senha e um vetor de CPU, ao lado do login, para quem rouba uma sessão (exatamente a ameaça do D53). Um time reaproveita `acesso.verificarSenha` (com contadores), outro chama o `PasswordEncoder` direto no módulo `preenchimento` (que nem deveria conhecer o hash, posse de `acesso`).

**Correção:** uma única API de `acesso`, `confirmarSenha(sessao, finalidade)`, que aplica o mesmo funil (limite, contador por sessão e por matrícula, lock, auditoria); proibir `PasswordEncoder` fora de `acesso` por teste de arquitetura.

---

## A5. Sem lugar para o aceite do consenso e `AV3` sem dono do participante (AD-26 x D30, D31, D53)

**Par:** time `preenchimento` x time `resultados`.

- **Ambos obedecem:** AD-26: chave `(participante_id, snapshot_pergunta_id, etapa)`, "agente é atributo do participante"; AV3 só para divergentes; `Igualdade(Av1, Av2)`. D7: Av1 autoavaliação, Av2 avaliação, Av3 consenso. D31/D53: aceite de cada parte (autor, data), apagado ao reabrir.
- **Colisão 1:** Av1 é do avaliado e Av2 é do avaliador (participantes diferentes). A chave contém `participante_id`, logo a igualdade compara linhas de dois participantes que o modelo não relaciona. Qual `participante_id` guarda a linha AV3, o do avaliado, o do avaliador ou um terceiro? Cada time escolhe, e `resultados` cruza de um jeito diferente do `preenchimento`.
- **Colisão 2:** "agente é atributo do participante, não da chave" contradiz AD-9/D (uma resposta por participante × pergunta × agente) na prática: autoavaliação é um agente com etapa AV1; par e subordinado têm etapa AV2 ou todas?
- **Colisão 3:** o aceite do consenso (D31) não está em `etapa_participante` (AD-19 lista início, finalização, comentários finais, consenso liberado) nem em tabela com dono. `reabrir` "apaga o aceite" (D32) sem tabela para apagar.
- **Colisão 4:** AV3 contém linhas "só para divergentes", mas a liberação pré-cria "linhas vazias" (AD-19). Se pré-cria AV3 para todas, "completa(AV3)" exige todas; se não, quem cria a linha AV3 quando Av1 é reaberta e muda a divergência?

**Correção:** definir `par_de_consenso(avaliacao_id, avaliado_participante_id, avaliador_participante_id)` e AV3 ligada a ele; definir a tabela `aceite_consenso(par, parte, autor, em)` de `preenchimento`; a liberação cria só AV1 e AV2; AV3 é materializada ao liberar o consenso e recalculada na reabertura.

---

## A6. Carimbo da coordenada congelada em outro instante que o snapshot (AD-25 x AD-9, AD-23)

**Par:** time `avaliacao` (criar) x time `avaliacao`/`cadastro` (liberar).

- **Ambos obedecem:** AD-25: coordenada congelada "carimbada na criação por `avaliacao`". AD-9/AD-23: a liberação copia cargo/departamento do avaliado numa transação `REPEATABLE READ` para impedir "liberação lendo dois instantes".
- **Colisão:** a avaliação nasce `AG_LIBERACAO` (spec) e fica dias antes de liberar; o par (unidade, departamento) foi carimbado na criação e o cargo/departamento do snapshot, na liberação. Se o funcionário mudou nesse intervalo, o mesmo avaliado tem dois departamentos em duas colunas. Resultado e PDI filtram por um, o relatório mostra o outro (o defeito que o AD-23 quer evitar). Campanha "usa o escopo do criador" sem dizer qual coordenada nem quando.

**Correção:** carimbar a coordenada na liberação, dentro da mesma transação RR; antes da liberação, `atual`. Registrar na matriz a coordenada do estado pré-liberado.

---

## A7. Corte do AD-23 x ERRO do parcial: o total espera para sempre (AD-11 M3 corrigido)

**Par:** time `resultados` x infraestrutura.

- **Ambos obedecem:** AD-11: o total "reagenda enquanto houver item de parcial da avaliação em qualquer estado". AD-28: itens esgotam tentativas e vão a `ERRO`.
- **Colisão:** parcial em `ERRO` permanente (poison, bug) conta como "em qualquer estado": o total nunca roda, a avaliação fica `CONCLUIDA` sem resultado, sem alerta específico (AD-28 alerta idade de fila, não esse bloqueio). Um time trata `ERRO` como "terminal, ignora"; outro como "pendente, espera".

**Correção:** o total só espera `PENDENTE`/`EM_PROCESSAMENTO`; parcial `ERRO` é ignorado pelo total (parcial é descartável, AD-11) e gera alerta. Como o total relê todas as respostas, o parcial é dispensável para a correção.

---

## A8. Chave do `pg_advisory_xact_lock` sem espaço de nomes (AD-7, AD-11 x ids UUID)

**Par:** time `acesso` (lock por matrícula) x time `resultados` (lock por avaliação).

- **Ambos obedecem:** AD-7: `pg_try_advisory_xact_lock` por matrícula. AD-11: `pg_advisory_xact_lock(avaliacaoId)`. Convenção: ids UUID.
- **Colisão:** os advisory locks ocupam um único espaço de 64 bits no banco. UUID não cabe em `bigint`: cada time faz hash diferente; colisão entre uma matrícula e uma avaliação gera 429 espúrio no login ou espera no total. Mais: advisory lock não respeita empresa, então colisões cruzam tenants. O lock também não está na ordem global de locks do AD-19.

**Correção:** usar a forma de dois inteiros (`classid` fixo por uso, `objid` = hash de 32 bits do id), registro central dos `classid` em `infra`, e incluir o advisory lock na ordem global do AD-19.

---

## M1. `app_rw` não consegue ser dono nem migrar: papéis do AD-29 x FORCE RLS x migrações (AD-3, AD-8, AD-18, AD-29)

- AD-29: migrações rodam com papel próprio, mas nenhum papel de migração é listado na tabela de privilégios e o AD-3 diz que o papel da aplicação nunca tem `BYPASSRLS`. Com `FORCE ROW LEVEL SECURITY`, **migração que faz backfill/transformação de dados (expandir e contrair, AD-18) roda sob RLS fail-closed e atualiza zero linhas sem erro**. Ou o papel de migração recebe `BYPASSRLS` (não dito) ou a migração faz laço por empresa (inviável em DDL com dados).
- AD-8: "dono da tabela de auditoria é um papel diferente do da aplicação"; AD-29: `auditoria_manutencao` é "dono das partições; só DROP". Quem é dono da tabela-mãe? O papel de migração a cria; `auditoria_manutencao` só pode `DROP` partição se for dono dela ou da mãe, e ser dono significa poder desligar gatilhos. Quem cria a próxima partição mensal (precisa `CREATE`)? Cada time resolve com um papel novo.
- AD-21: "partição mensal" e "partição de plataforma" para `empresa_id` nulo são dois critérios de partição na mesma tabela; sem subparticionamento, um dos dois não existe.
- `app_rw` insere na auditoria com `empresa_id` nulo (login desconhecido): a política `WITH CHECK` precisa permitir null, o que deixa qualquer SQL com `app_rw` forjar linhas de plataforma.

**Correção:** acrescentar à tabela do AD-29: papel de migração (com política explícita de `BYPASSRLS` apenas nele, nunca em runtime), papel criador de partições, dono da mãe; escolher partição única por mês com `empresa_id` nulo permitido só para o canal 2 por política dedicada.

---

## M2. Outbox do `comunicacao` x `infra_trabalho`: duas filas com a mesma máquina de estados (AD-13 x AD-14, AD-28)

- AD-13: a linha do outbox (`ENVIANDO`, `tentativa_id`, lease, `ERRO`, backoff) com `FOR UPDATE SKIP LOCKED`. AD-28: `infra_trabalho` é dono único com estado, tentativas e `proxima_tentativa`; "módulos só enfileiram pela API do infra"; o worker de outbox entra por adaptador. AD-22: nenhuma coluna de `comunicacao_*` contém token.
- **Colisão:** o outbox é `comunicacao_outbox` (dono `comunicacao`, estado e retry próprios) ou um `tipo` de `infra_trabalho`? Se os dois, há retry em duas camadas (o consumidor do evento que cria a linha, e o envio), duas noções de "ERRO", e o alerta do AD-18 só cobre uma. A tela do RH lê qual das duas? Dois times implementam um e outro.

**Correção:** decidir: `infra_trabalho` guarda só o item de consumo do evento (cria a linha); `comunicacao_outbox` é o estado de envio, com sua máquina e seu alerta; documentar a fronteira.

---

## M3. Quem é dono de `reabrir` e do gatilho do parcial (AD-10/AD-19/AD-26 x AD-11)

- `etapa_participante` é de `preenchimento`; AD-19 diz que "reabrir usa `FOR UPDATE` na avaliação" (módulo `avaliacao`); D32 e a matriz são de `avaliacao`/RH. A "única porta de entrada por agregado" (AD-10) da etapa é de quem? Duas implementações de `reabrir`.
- AD-11 dispara o recálculo do parcial **ao finalizar**; reabrir apaga a finalização (AD-26) e muda respostas, mas nenhum evento recalcula o parcial: a tela mostra nota de etapa que não está mais finalizada. Falta evento `etapa.reaberta` consumido por `resultados`.

**Correção:** `preenchimento` é a porta; `avaliacao` só autoriza e trava; evento `EtapaReaberta` v1 invalida o parcial.

---

## M4. Entidades sem dono declarado (AD-19 "o ER anota o módulo dono de cada tabela")

- A lista de donos da seção de entidades omite: `avaliacao` e `campanha` (campanha aparece no ER e no AD-25), tabelas de `catalogo` (versão, pergunta, escala, competência), `snapshot_pergunta`, `comunicacao_outbox`, mensagem interna, `auditoria_*`, contadores de login, `segredo_ref`, histórico de transição/justificativa (C2), importação (job e relatório por linha). O ER desenha relações entre módulos (`EMPRESA ||--o{ CAMPANHA`) como se houvesse FK, contra o AD-2, e ainda mostra `SNAPSHOT_LIBERACAO` 1:1 com a avaliação, quando o AD-23 faz o snapshot crescer por cargo.
- AD-1 diz que importação "vive em `cadastro` e `avaliacao`" e AD-17 a chama "módulo": upload, parser, limites e relatório são código e tabela duplicados, ou ficam em `infra` (kernel sem regra de negócio) sem dizê-lo.

**Correção:** tabela exaustiva tabela-dono (e quem lê), gerada e testada contra as migrações; ER marcado "ilustrativo, sem FK entre módulos".

---

## M5. Dois modos de rejeição para recurso fora do escopo (Convenções x AD-3, AD-4)

- AD-3: id de outra empresa responde 404. Convenções: 403 "só quando o recurso é visível ao usuário". Recurso da mesma empresa fora da árvore do usuário: visível? A lista paginada (AD-25) o oculta, o GET por id responde 403 ou 404? O gerador de teste negativo por célula (AD-4) precisa de um valor esperado; cada célula será escrita com um ou outro.

**Correção:** fora do escopo = 404; 403 só para ação negada sobre recurso dentro do escopo.

---

## M6. Mapeamento `situacao` de `cadastro` x `situacao_acesso` de `acesso` (AD-24)

- `aplicarSituacao(funcionarioId, situacao)` recebe a enumeração de `cadastro` e `acesso` guarda a própria `situacao_acesso`; o mapeamento em recesso depende de ELI-17 (aberto). Cada módulo tem uma tabela de conversão. Reativação: a sessão volta? A credencial bloqueada por falhas continua? Dois estados do mesmo fato com dois donos (o AD-24 chama de "coluna lógica", mas há duas colunas reais).

**Correção:** `situacao_acesso` é derivada por função única publicada por `acesso` (tabela de mapeamento versionada), com ELI-17 como parâmetro explícito.

---

## M7. Lock de linha de avaliação sob multixact e ETag (AD-19 x AD-15)

- Toda gravação de resposta toma `FOR SHARE` na linha da avaliação; `If-Match` do RH em prorrogação faz `UPDATE` (`FOR NO KEY UPDATE`), que conflita com `FOR SHARE`. Com centenas de respondentes, a prorrogação espera todos e novos `FOR SHARE` podem se acumular em multixact; a espera pode esbarrar no `lock_timeout` sem política (retry? 409? 503?). Falta o contrato HTTP desse conflito (Problem Details tipo e `Retry-After`).

**Correção:** documentar `lock_timeout` curto e mapa 409/503 para conflitos de lock; considerar linha de "controle de escrita" separada da linha editável (`avaliacao_estado` vs dados).

---

## M8. Parcial por `pg_advisory_xact_lock(avaliacaoId)` serializa a avaliação inteira (AD-11)

- Todos os parciais de uma avaliação tomam o mesmo lock exclusivo: o recálculo por participante fica sequencial na avaliação; com muitos participantes finalizando juntos o lag cresce sem limite. Total e parcial usam o mesmo lock, mas parcial por participante deveria usar lock compartilhado e o total, exclusivo.

**Correção:** `pg_advisory_xact_lock_shared` no parcial, `pg_advisory_xact_lock` no total.

---

## M9. Comentários e anonimato (AD-12 x AD-8, D16/D21)

- AD-12 cobre notas por agente; não cobre `observacao`/comentário final (AD-8 audita a leitura). Comentário agrupado por agente, mostrado ao avaliado, identifica o autor quando o grupo tem 1 a 2 pessoas, e o limiar aplicado a notas não se aplica a texto. O AD-19 coloca "comentários finais" em `etapa_participante`. `resultados` e `pdi` (ligação com o texto do consenso) exibem de formas distintas. A confirmar no spec a regra de exibição.

**Correção:** AD-12 estende o limiar e a supressão a comentário; comentário nunca atribuído a grupo abaixo do limiar.

---

## B1. Convenções de ids e advisory locks já citadas; ER 1:1 com snapshot; `Idempotency-Key` sem TTL

- Chave de idempotência (AD-15) sem prazo de expurgo nem política de resposta repetida (devolve o corpo original? só o status?). Duas implementações.

## B2. `Cache-Control: private, no-store` do ingress x `ETag`/`If-Match`

- `no-store` impede revalidação condicional pelo navegador; o `ETag` serve apenas ao `If-Match` na escrita. Registrar para que o front não conte com `If-None-Match` na leitura.

## B3. Fuso gravado só no snapshot da liberação

- Prazo de avaliação ainda não liberada (rascunho) lê qual fuso? AD-10 só o grava na liberação.

---

## Tabela-resumo

| ID | Severidade | ADs envolvidas | Tema |
| --- | --- | --- | --- |
| C1 | Crítico | AD-27, AD-1, AD-4, AD-8, AD-13, AD-25 | Grafo omite arestas exigidas (auditoria, acesso, cadastro, eventos) |
| C2 | Crítico | AD-8, AD-30, AD-10, D26/D32 | Motivo fechado sem lugar para a justificativa; dono da lista |
| C3 | Crítico | AD-28, AD-21, AD-27 | `sequencia` não é ordem de commit: descarte silencioso |
| C4 | Crítico | AD-11, AD-10, ambientes | Corte por instante com relógio de aplicação exclui resposta |
| C5 | Crítico | AD-3, AD-7, AD-21, regra do desenho | Login não consegue resolver empresa sem chamar `cadastro` |
| A1 | Alto | AD-14, AD-20, AD-29, AD-3 | Jobs periódicos sem enumeração de empresas |
| A2 | Alto | AD-25, AD-1, AD-2 | Visão de escopo por join sem forma definida; coordenada congelada órfã |
| A3 | Alto | AD-30, AD-27, AD-21, AD-13 | Cópias de dado pessoal e IP fora de `funcionario` |
| A4 | Alto | AD-7, AD-5/D53, AD-8 | Verificação de senha fora do login escapa dos limites |
| A5 | Alto | AD-26, AD-19, D7, D31 | AV3 sem `participante_id` claro; aceite sem tabela |
| A6 | Alto | AD-25, AD-9, AD-23 | Dois instantes para o mesmo avaliado (criação x liberação) |
| A7 | Alto | AD-11, AD-28 | Total espera parcial em `ERRO` para sempre |
| A8 | Alto | AD-7, AD-11, AD-19 | Advisory lock sem espaço de nomes, fora da ordem global |
| M1 a M9 | Médio | ver acima | Papéis x migrações, outbox x fila, `reabrir`, donos de tabela, 403/404, situação, lock de avaliação, lock de parcial, comentário |
| B1 a B3 | Baixo | ver acima | Idempotência, cache, fuso |

## Ordem sugerida de correção

1. C1 (grafo) e C5 (resolução de empresa): mudam a lista de portas e a regra do desenho.
2. C2, C3 e C4: mudam o esquema de auditoria, de eventos e de conclusão antes do primeiro código.
3. A1, A2, A4, A5, A6: contratos de portas e tabelas antes de congelar o OpenAPI.
4. A3, M1, M4: catálogo de dados pessoais e tabela de donos/papéis, geradas e testadas.
5. Restante junto da primeira implementação do módulo afetado.
