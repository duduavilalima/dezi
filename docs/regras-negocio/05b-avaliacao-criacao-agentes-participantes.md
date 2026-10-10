# Avaliação — criação, cópia, agentes, participantes e pendências (passada 2 de 2)

Módulo: `web/` · Submódulo 5 do roteiro. Continua [`05a-avaliacao-estados-transicoes.md`](05a-avaliacao-estados-transicoes.md) (estados e transições; os IDs RN-EST-nn estão lá).
Escopo: criar/editar avaliação, agentes (tipos de competência por perfil), datas, participantes/avaliadores, pares por planilha, pendências, cópia, dimensões, home office.
Fora de escopo: cálculo de nota e preenchimento (itens 6 e 7), PDI (8), mensagens (9).

Convenção: **[código]** = evidência no código; **[banco]** = confirmado no Postgres (consulta somente leitura de 2026-09-29, host em rede Docker, dados até 2024-03-20; cópia ou produção: a confirmar); **A confirmar** = sem evidência suficiente.
Abreviações: `AvSvc` = `web/services/src/main/java/br/com/fasolti/service/impl/AvaliacaoServiceImpl.java`; `CargoSvc` = `.../CargoServiceImpl.java`; `AvalidorSvc` = `.../AvaliadorServiceImpl.java`.

## 1. Glossário

| Termo | Significado | Evidência |
|---|---|---|
| Agente / perfil do agente | Quem avalia. `TipoAvaliacaoPerfil`: AUTOAVALIACAO(100), GESTOR(101), SUBORDINADO(102), PARES(103). `CLIENTE_INTERNO` e `OUTROS` estão comentados no enum | `services.client/.../types/TipoAvaliacaoPerfil.java` **[código]** |
| Avaliado | Funcionário que é avaliado | `Participante.avaliado` |
| Avaliador | Funcionário que avalia o avaliado; na autoavaliação é o próprio avaliado | ver 4.4 |
| Participante | Linha de `avaliacao_participante`: um par (avaliado, avaliador) dentro de uma avaliação, com perfil, datas e marcadores de progresso. **Não** é uma pessoa; um avaliado tem vários participantes | `AvaliacaoParticipanteServiceImpl` |
| Agentes da avaliação | Linhas de `avaliacao_tipo_competencia`: quais tipos de competência cada perfil avalia e com qual **peso** | `AvaliacaoTipoCompetencia.java` |
| Revisado | Marca de que o participante foi conferido antes da liberação. Ver RN-CRI-27 | `Participante.revisado` |
| Pendência | Impedimento registrado para um avaliado ou cargo que não pode ser liberado; tipos COLABORADORES(1), CARGO(2), DIMENSAO(3) | `TipoPendencia.java` **[código]**; tabela `avaliacao_pendencia` **[banco]** |
| Dimensão | Agrupador opcional de detalhamentos de competência; só vale se a campanha tem `dimensao=true` | `Campanha.dimensao` |
| Av1..Av4 | Etapas de preenchimento com data limite própria. Pelo código de atualização de datas: Av1 = autoavaliação e etapa do gestor; Av2 = avaliação dos avaliadores (única etapa dos demais perfis); Av3 = etapa do gestor. Significado exato de cada uma: **a confirmar**. Av4 não é usada em nenhuma avaliação **[banco]** | `AvSvc:395-421` |

## 2. Modelo de dados envolvido

- `avaliacao` (144 linhas **[banco]**): descrição abreviada, descrição, campanha, situação, flags de perfis (`autoavaliacao`, `gestores`, `subordinados`, `pares`), `preenche_av1..4`, `limite_av1..4`, `email_padrao_da_avaliacao`, `av_homeoffice`, `em_liberacao`.
- `avaliacao_tipo_competencia`: `id_avaliacao`, `id_tipo` (tipo de competência), `id_perfil_agente`, `peso` (smallint).
- `avaliacao_participante`: avaliado, avaliador, revisor, perfil, `revisado`, datas `limite_avN`/`data_inicio_avN`/`data_conclusao_avN`, marcadores `preenchida_avN`, snapshot `id_cargo_avaliado`, `nome_cargo_avaliado`, `id_depto_avaliado`, `nome_depto_avaliado`.
- `avaliacao_pendencia`: avaliação, avaliado, avaliador, perfil, código do erro, tipo, cargo, mensagem, data.
- `avaliacao_dimensao`: avaliação x dimensão.
- **Unicidade no banco [banco]:** `(id_campanha, descricao_abreviada)` em `avaliacao`; `(id_avaliacao, id_avaliado, id_avaliador, id_revisor)` em `avaliacao_participante`. **Não** há `CHECK` em nenhuma dessas tabelas.

## 3. Criar e editar a avaliação

- **RN-CRI-01** Nome único: `descricaoAbreviada` (sem espaços nas pontas, sem diferenciar maiúsculas) deve ser única **dentro da campanha**. Ao criar ou editar, se já existe outra avaliação com o mesmo nome na mesma campanha, lança `validation.duplicate`. **[código]** `AvSvc:451-470`. **[banco]** índice único `uq_avaliacao_campanha_descricao_abrev`.
- **RN-CRI-02** Uma avaliação **nova** precisa de ao menos um agente (tipo de competência + perfil). Sem isso lança `validation.empty` ("tipos de competências"). **[código]** `AvSvc:472-477`. Na edição não se exige. **[banco]** todas as 144 avaliações têm agentes.
- **RN-CRI-03** Estado inicial e empresa: ver RN-EST-01 (situação AG_LIBERACAO, preenchimento NAO_PREENCH, empresa do usuário logado).
- **RN-CRI-04** Valores padrão ao gravar: `preencheAv1..4` e `autoavaliacao`, `gestores`, `subordinados`, `pares` nulos viram `false` ("a tela de preenchimento precisa desses campos preenchidos"). **[código]** `AvSvc:302-310`. **[banco]** ainda há 8 avaliações com `autoavaliacao` nulo (1 em `AG_LIBERACAO` e 7 em `LIBERADA`; criadas antes da regra ou por outro caminho).
- **RN-CRI-05** Na edição, `subordinados` e `pares` nulos viram `false`; `autoavaliacao` e `gestores` **não** ganham esse tratamento na edição. **[código]** `AvSvc:367-371`. Assimetria, **a confirmar**.
- **RN-CRI-06** Edição bloqueada em avaliação `CANCELADA` (RN-EST-20). Inativação altera datas (RN-EST-17).
- **RN-CRI-07** Ao criar, os agentes recebidos sem `id` viram linhas em `avaliacao_tipo_competencia` com tipo, perfil e peso. **[código]** `AvSvc:348-359`.
- **RN-CRI-08** Ao criar com participantes informados, eles são processados pelo mesmo fluxo de `salvarParticipantes` (ver seção 6) e, se a campanha usa dimensões, as dimensões escolhidas são gravadas. **[código]** `AvSvc:313-345`. O filtro `listAvaliados` do código nunca é preenchido, então todos os participantes passam (a "deduplicação" não funciona). **Suspeito, a confirmar**.
- **RN-CRI-09** **Datas limite por etapa.** Cada etapa AvN tem um flag `preencheAvN` e uma data `limiteAvN`. **[banco]** as combinações reais são: Av1+Av2+Av3 (78 avaliações), Av1+Av2 (27), só Av1 (27), só Av2 (12); Av4 nunca. Nenhuma avaliação tem flag ligado com data nula. Em 7 avaliações há `limite_av2` preenchido com `preenche_av2` desligado. O código não valida a coerência entre datas (por exemplo, Av1 anterior a Av2): **não achei validação de ordem de datas**.
- **RN-CRI-10** **Propagação de datas na edição.** Ao editar a avaliação, as datas dos participantes existentes são reescritas: **[código]** `AvSvc:389-421`
  - perfil GESTOR: recebe Av1, Av2 e Av3 da avaliação quando a etapa está marcada e a data existe;
  - AUTOAVALIACAO: recebe só Av1;
  - demais perfis: `limiteAv1`, `limiteAv3` e `limiteAv4` viram nulos e `limiteAv2` recebe a da avaliação.
  Ou seja, subordinados e pares só preenchem na etapa Av2. **[banco]** em avaliações liberadas, 892 de 7.616 participantes têm `limite_av2` diferente do da avaliação; a causa (prorrogação individual, edição posterior, importação) é **a confirmar**.
- **RN-CRI-11** Ao **criar** participantes (`criarAvaliadores`), `limiteAv1..3` do participante recebem as da avaliação. **[código]** `AvSvc:1505-1507`. O gestor gerado por `atualiazarAvalidores` recebe Av2 e, se houver, Av1 e Av3. Subordinados criados por `obterSubordinadosPorAvaliado` recebem só Av2 (`AvSvc:1796`). Na gravação final (`saveOrUpdateParticipante`), **todos** os limites Av1..Av4 do participante são sobrescritos pelos da avaliação (`AvSvc:1646-1649`), o que contradiz a regra de perfis da RN-CRI-10. **Comportamento suspeito, a confirmar** (qual regra prevalece depende do caminho de chamada).

## 4. Agentes (tipos de competência por perfil)

- **RN-CRI-12** Cada agente é um trio (avaliação, tipo de competência, perfil) com um **peso** inteiro. O perfil e o vínculo não mudam depois de criados (`updatable=false` em `id_perfil_agente`, `id_avaliacao`, `id_tipo`; só o peso muda). **[código]** `AvaliacaoTipoCompetencia.java`. Peso é obrigatório (`nullable=false`); **[banco]** coluna aceita nulo, mas não há nenhum nulo.
- **RN-CRI-13** **Pesos:** o código não valida que os pesos somem 100. **[código, por ausência]**; na tela o campo percentual tem `max=100` (`aba.agentes.html:38,47,56`). **[banco]** a soma por (avaliação, perfil) varia: 50 (72 casos), 0 (45), 30 (34), 100 (33), 80 (20), 300 (15) etc.; peso mínimo 0 e máximo 100. Ou seja, o peso **não** é um percentual que soma 100 por perfil. Semântica exata do peso (por tipo, por perfil, relativo entre tipos): **a confirmar** com o cálculo de resultado (item 7).
- **RN-CRI-14** Na edição, agentes ausentes da lista enviada pela tela são removidos e os demais são regravados (com o mesmo `id`). Se algum agente foi removido, participantes cujo cargo deixa de ter tipos de competência da avaliação são **apagados** (RN-CRI-22). **[código]** `AvSvc:391-393, 1836-1880`.
- **RN-CRI-15** **Defeito provável no laço de remoção:** o teste `if (remover)` está dentro do laço interno, então um agente do banco é marcado para exclusão na primeira comparação sem correspondência, mesmo que apareça depois na lista; depois é regravado com o mesmo `id`. Em geral o efeito final é correto, mas `contador` e o retorno (`!(contador == to.getAgentes().size())`) podem indicar "mudou" quando não mudou, disparando RN-CRI-22 sem necessidade. **[código]** `AvSvc:1846-1862`. **A confirmar**.
- **RN-CRI-16** A tela desabilita edição de agentes, percentuais e adição/remoção quando a avaliação está `LIBERADA` (ver seção 5 de 05a). A API **não** impede: `ajustarTipoCompetenciasDaAvaliacao` roda em qualquer update. **[código]**.
- **RN-CRI-17** **Flags de perfil versus agentes:** `autoavaliacao`, `gestores`, `subordinados`, `pares` dizem quais perfis a avaliação usa; os agentes dizem o que cada perfil avalia. O código não verifica que um perfil ligado tenha agente. **[banco]** todo participante tem perfil presente nos agentes da avaliação (0 exceções), então na prática são coerentes.
- **RN-CRI-18** **Combinações de perfis em uso [banco]:** as 144 avaliações usam 13 combinações distintas dos flags (autoavaliação, gestores, subordinados, pares). As mais comuns: autoavaliação+gestor (48), só autoavaliação (27), autoavaliação+gestor+subordinados+pares (24) e autoavaliação+gestor+subordinados (8). Também existem só pares (3), só gestor (5) e só subordinados (3). Portanto todas as combinações de flags são aceitas; nenhum perfil é obrigatório.

## 5. Pendências e validação do cargo (pré-requisito para participar)

`CargoSvc.validarParaLiberacao` (`CargoSvc:163-315`) gera pendências do tipo CARGO; o serviço de avaliação gera as do tipo COLABORADORES. Regras **[código]**:

| Código | Regra (o que dispara a pendência) | Tipo |
|---|---|---|
| `cargo-inactive` | Cargo não está `ATIVA` | CARGO |
| `cargo-not-found-tipos-competencia` | Cargo sem nenhum tipo de competência associado | CARGO |
| `cargo-not-found-tipos-competencia-ative` | Cargo tem tipos, mas nenhum ativo | CARGO |
| `cargo-not-found-tipos-competencia-avaliacao` | Nenhum dos tipos do cargo está entre os tipos da avaliação | CARGO |
| `cargo-not-found-competencia` | Cargo sem competências associadas | CARGO |
| `cargo-not-found-competencia-ative` | Nenhuma competência do cargo está ativa (vínculo e competência ativos) | CARGO |
| `cargo-not-found-detalhamento` | Competências ativas do cargo sem detalhamentos | CARGO |
| `cargo-not-found-detalhamento-ative` | Nenhum detalhamento ativo | CARGO |
| `cargo-not-found-gabarito` | Nenhum detalhamento ativo é "aplicável ao cargo" | CARGO |
| `cargo-not-found-gabarito-dimensao` | Campanha com dimensão e nenhum detalhamento aplicável pertence às dimensões da avaliação | CARGO |
| `avaliado-situacao-funcional` | Situação funcional do avaliado ≠ `EM_ATIVIDADE` | COLABORADORES |
| `not-found-avaliador` | Não foi possível gerar nenhum avaliador para o avaliado (ex.: sem gestor achado) | COLABORADORES |

- **RN-CRI-19** O resultado do cargo é memorizado por cargo dentro da chamada (`detalhamentosPorCargo`), então um cargo é validado uma vez por lote. Se o cargo já foi validado, `resultados` fica nulo e o participante passa mesmo que o primeiro tenha gerado pendência (o `contemPendencia` cobre isso: se já há pendência daquele cargo, o participante é barrado). **[código]** `AvSvc:1250-1271`.
- **RN-CRI-20** **Persistência das pendências:** ao final de cada operação de salvar participantes, as pendências antigas que não se repetem são apagadas e as novas gravadas (comparação por tipo + código + avaliado/cargo). Pendência é regravada a cada salvamento; quando o problema é resolvido e o participante passa, `removerPendencias` apaga as do avaliado e do cargo. **[código]** `AvSvc:1433-1497, 1275-1278`.
- **RN-CRI-21** **Participante com pendência não é gravado:** se o participante já existia (`id` não nulo), é **apagado**; se é novo, simplesmente não é gravado. **[código]** `AvSvc:1255-1259` (em `validarEpersistirParticipantes`). Na liberação, qualquer pendência bloqueia (RN-EST-07). **[banco]** 19 pendências gravadas hoje (códigos acima; a mais comum é `cargo-not-found-gabarito` e `not-found-avaliador`, 4 cada).
- **RN-CRI-22** Ao remover tipos de competência da avaliação, todo participante cujo cargo agora gera pendência é **apagado** (junto do respectivo preenchimento), sem aviso. Bug conhecido no laço: `pendencias` é reatribuída a cada cargo novo, perdendo pendências de participantes anteriores; o efeito prático é remover só quando a última validação gerou pendência. **[código]** `AvSvc:1934-1968`. **Suspeito, a confirmar**.

## 6. Participantes e avaliadores

Entradas da API: `POST /avaliacao/salvarparticipantes`, `/salvaravaliadores`, `/salvarpares`, `/upload`, `/removeravaliado`, `/removertodosparticipantes` (`AvaliacaoResource`).

### 6.1 Escolher avaliados (`salvarParticipantes`, `AvSvc:1121-1160`)

- **RN-CRI-23** Lista de participantes vazia lança `validation.empty`. Para cada avaliado enviado:
  - se **não** tem participantes na avaliação, gera os avaliadores automaticamente (6.2); se não gera nenhum, cria a pendência `not-found-avaliador`;
  - se já tem, mantém os existentes e completa (6.3).
  Em seguida remove os participantes que sumiram da lista (RN-CRI-25) e valida/persiste (seção 5).
- **RN-CRI-24** Cada avaliado é buscado por identificador; o funcionário é sempre recarregado do banco para preencher cargo e situação funcional (`validarEpersistirParticipantes` e `criarAvaliadores`). **[código]**.

### 6.2 Geração automática de avaliadores (`criarAvaliadores`, `AvSvc:1500-1544`)

- **RN-CRI-25** Regras por flag da avaliação:
  1. `gestores` ligado: descobre o gestor do avaliado (`findAvaliador`, RN-CRI-30). Se existe e está `EM_ATIVIDADE`, cria participante com perfil GESTOR e `revisado=true`.
  2. `subordinados` ligado: cria um participante SUBORDINADO para **cada subordinado direto ativo** (`EM_ATIVIDADE`) do avaliado, com `revisado=true` e só `limiteAv2` (`AvSvc:1782-1804`).
  3. `gestores` **e** `subordinados` desligados: cria **um** participante em que o avaliador é o próprio avaliado; o perfil é AUTOAVALIACAO se `autoavaliacao` está ligada e **PARES** caso contrário (`revisado=true`).
  4. A autoavaliação **não** é criada aqui quando há gestores ou subordinados; ela é criada em `salvarAvaliadores` (RN-CRI-28).
- **RN-CRI-26** **Comportamento suspeito, a confirmar:** no item 3, com `autoavaliacao=false` e `pares=true` o avaliado passa a avaliar a si mesmo com perfil PARES. **[banco]** confirma o efeito: 25 participantes têm `avaliado = avaliador` com perfil diferente de AUTOAVALIACAO (24 PARES e 1 SUBORDINADO); as 24 em PARES estão em avaliações `LIBERADA` (18) e `AG_LIBERACAO` (6). Se elas vêm todas de avaliações "só pares" não foi verificado. Possivelmente é o modo de "criar" o avaliado para depois importar os pares por planilha (6.5); não é uma regra de negócio clara.
- **RN-CRI-27** **Revisado:** o participante é marcado `revisado=true` quando: (a) é criado por geração automática; (b) é criado sob avaliação já `LIBERADA` (`AvaliacaoParticipanteServiceImpl:215-217`); (c) a tela propaga o valor do avaliado para todos os seus avaliadores (`atualizarParticipantesPorAvaliado`, `AvSvc:1672-1709`). Sob avaliação `LIBERADA`, salvar participante não revisado lança `error-liberacao-revisados` (RN-EST-15). **[banco]** em avaliações `AG_LIBERACAO`, só 8 de 213 participantes estão revisados (as 6 autoavaliações-como-pares e 2 gestores); em `LIBERADA`, 7.615 de 7.616 (falta 1 gestor); em `INATIVA` e `CONCLUIDA`, todos. Uso original do "revisado" na etapa de análise: **a confirmar** (a aba "análise" não existe mais, ver `atualizaAvaliadorAndRevisor`, RN-EST-30).
- **RN-CRI-28** **Revisor:** o modelo tem `id_revisor`, mas **[banco]** nenhum participante tem revisor preenchido (0 em todas as situações). O revisor é hoje um recurso sem uso nestes dados. **A confirmar** se ainda faz parte do processo.

### 6.3 Completar avaliadores de quem já participa (`atualiazarAvalidores`, `AvSvc:1547-1604`)

- **RN-CRI-29** Com `subordinados` ligado: acrescenta subordinados diretos ativos que ainda não constam como avaliadores.
- **RN-CRI-30** Com `gestores` ligado: só cria o gestor se **ainda não existe participante GESTOR** para o avaliado na avaliação (ou se o próprio avaliado é GESTOR e tem subordinados). Antes de criar, se já existe qualquer participante com o mesmo par (avaliado, gestor) lança `validation.already-exists`: "apenas pode existir um par avaliado/avaliador por avaliação". **[código]** `AvSvc:1569-1582`; **[banco]** confirmado pelo índice único e por 0 duplicidades (avaliado+avaliador) em `avaliacao_participante`.
- **RN-CRI-31** Não há remoção de subordinados quando o flag `subordinados` é desligado; o próprio código registra `TODO Pensar na remoção de subordinados`. **[código]** `AvSvc:1697`.

### 6.4 Autoavaliação e salvar avaliadores (`salvarAvaliadores`, `AvSvc:935-1013`)

- **RN-CRI-32** Lista vazia lança `validation.empty`. A tela envia só os participantes que têm avaliador, não todos os colaboradores.
- **RN-CRI-33** Avaliadores gravados que **não** vêm na lista da tela (e não são autoavaliação: avaliado ≠ avaliador) são excluídos, junto com o preenchimento do participante (`excluirPrenchimentoPorParticipante` + `excluirParticipante`). **[código]** `AvSvc:1005-1009`. Na liberação, essa exclusão apaga respostas já dadas: nenhuma checagem impede excluir quem já preencheu. **Suspeito, a confirmar**.
- **RN-CRI-34** Se a avaliação tem `autoavaliacao` e **não** tem `gestores`, cria automaticamente as autoavaliações que faltam para todo avaliado da lista (avaliador = avaliado, perfil AUTOAVALIACAO). Com `gestores` ligado a autoavaliação vem de outro caminho, a confirmar. **[código]** `AvSvc:981-1003`. **[banco]** 173 participantes autoavaliação em avaliações liberadas, todos com avaliado = avaliador e 0 autoavaliações com avaliador diferente.
- **RN-CRI-35** Em avaliação já `LIBERADA` a variante `salvarAvaliadoresLiberacao` (`AvSvc:1017-1117`): grava avaliadores, e para cada avaliado sem participantes cria os avaliadores; para quem já existe, marca como "novo" o avaliador que ainda não constava ou que não tem preenchimento gerado; participantes que sumiram são apagados; valida cargo e situação funcional (pendências) e libera **só os novos** (RN-EST-13). **[código]**.

### 6.5 Pares por planilha

- **RN-CRI-36** `POST /avaliacao/upload` lê a primeira aba de um `.xlsx`: cada linha traz na 1ª coluna o **avaliador** e nas colunas seguintes os **avaliados pares** (nome completo). As linhas de cabeçalho "Avaliador" e "Avaliado Par" são ignoradas. Cada nome é procurado por **nome exato** (espaços duplicados são normalizados). Nomes não encontrados são **ignorados em silêncio**, sem erro nem relatório. Cada par vira participante com perfil PARES. **[código]** `AvSvc:853-919`.
- **RN-CRI-37** `POST /avaliacao/salvarpares` grava a lista importada pelo mesmo fluxo de validação/pendências; lista vazia lança `validation.empty` ("planilha_pares"). **[código]** `AvSvc:923-931`.
- **RN-CRI-38** Nomes iguais de funcionários diferentes (homônimos) ou nome fora do padrão fazem o par ser perdido ou vinculado ao funcionário errado, dependendo de `funcionarioService.findByNome`. **A confirmar** o critério de desempate.

### 6.6 Localizar o gestor do avaliado (`AvaliadorServiceImpl.findAvaliador`)

- **RN-CRI-39** Regra **[código]**:
  1. Pega o par (unidade, departamento) do avaliado e o gestor cadastrado para ele (`UnidadeDepartamento.gestor`).
  2. Se existe e o gestor **não é** o próprio avaliado, esse é o avaliador.
  3. Se não existe gestor ou o avaliado **é** o gestor da própria unidade/departamento, sobe na hierarquia: procura o gestor do departamento pai, depois dos ancestrais, o primeiro que exista. Em cada nível, ignora gestor igual ao avaliado e gestor com situação funcional `DESLIGADO` (mas aceita, por exemplo, gestor em recesso).
  4. Se nada é encontrado: sem avaliador → pendência `not-found-avaliador`.
- **RN-CRI-40** O gestor encontrado só vira avaliador se estiver `EM_ATIVIDADE` (RN-CRI-25.1); mas a busca aceita qualquer situação diferente de DESLIGADO. Ou seja, gestor de licença/recesso é encontrado e depois descartado, sem subir para o nível acima. **Suspeito, a confirmar**.

### 6.7 Remover participantes

- **RN-CRI-41** `removerAvaliado`: remove todos os participantes de um avaliado (lista vazia é comparada com os atuais, então apaga todos). `removerTodosParticipantes`: apaga todos os participantes da avaliação. Nenhum dos dois tem restrição por situação da avaliação nem por preenchimento existente. **[código]** `AvSvc:1655-1670, 1738-1744`. Ao apagar participante de avaliação `LIBERADA`, apaga também as perguntas dele (RN-EST-27). **A confirmar** se a API deveria impedir isso.
- **RN-CRI-42** Perguntas por participante são congeladas na liberação (ver 05a, RN-EST-08 a 11). Depois disso, mudanças no cadastro de competências não afetam a avaliação já liberada (snapshot de competências, detalhamentos, graduações, hints e pesos em tabelas `avaliacao_*`/`modelo_graduacao_comp`).

## 7. Cópia de avaliação (`copiar`, `AvSvc:541-607`)

- **RN-CRI-43** `POST /avaliacao/copiar` cria uma **nova** avaliação (mesma campanha) a partir de uma existente: copia os campos, os **agentes** (com peso) e, das pessoas, apenas os participantes cujo cargo do avaliado tem competências de algum tipo presente nos agentes (`temTipoCompetenciaNoCargo`).
- **RN-CRI-44** O nome novo (`descricaoAbreviada`) vem da requisição e segue RN-CRI-01 (único por campanha). A cópia passa por `save` e portanto por RN-CRI-02 e RN-EST-01: situação AG_LIBERACAO, sem preenchimento.
- **RN-CRI-45** Participantes copiados: mesmo avaliado, avaliador, revisor e perfil; datas limite Av1..Av4 = as da cópia; `consensoLiberado=false` e `revisaoLiberada=false`. Datas de criação e criador do participante original são mantidos (não os da cópia). **[código]** `AvSvc:587-602`. **Suspeito:** `criador`/`dataCriacao` copiados do original.
- **RN-CRI-46** Participantes cujo cargo não tem competências ativas de algum tipo da avaliação são descartados sem aviso (o teste usa `findAllCargoCompetenciaByCargoAndAtivos`). **[código]**. O código da cópia não trata explicitamente dimensões nem pendências (as dimensões não são gravadas na cópia); se os campos `avHomeOffice` e `emailPadraoDaAvaliacao` seguem no mapeamento de `AvaliacaoTO` não foi verificado. **A confirmar**.
- **RN-CRI-47** Cópia de participantes usa o cargo **atual** do funcionário, então a cópia reflete a estrutura de hoje, não a da avaliação original.

## 8. Dimensões

- **RN-CRI-48** As dimensões só são usadas quando a campanha tem `dimensao=true`. **[banco]** apenas 1 campanha tem dimensão e **0 avaliações** têm linhas em `avaliacao_dimensao`: recurso sem uso nestes dados. **[código]** ao editar, dimensões ausentes da lista são removidas e as demais gravadas ou atualizadas (`AvSvc:1889-1932`); na liberação as perguntas são filtradas pelas dimensões (RN-EST-10).

## 9. Home office (campanhas rápidas)

- **RN-CRI-49** Existe um fluxo separado para "Pesquisa Home Office" (`HomeOfficeBrasilResource`, rota `homeofficebrasil`, `avHomeOffice=true`): cria um funcionário simples (matrícula = e-mail funcional, `ATIVA`, `EM_ATIVIDADE`, perfil `FUNCIONARIO`, primeiro acesso, admissão e data de cargo = hoje), com empresa, departamento, cargo e unidade **fixos por configuração** (`homeoffice.empresaId/deptoId/cargoId/unidadeId`), e o inclui na avaliação de id fixo por configuração (`homeoffice.avaliacaoId`; o comentário do código diz "ID da avaliação do COVID"). Só um cargo; usa o primeiro participante como referência. **[código]** `AvSvc:1164-1231, 2043-2235`. Login e senha do serviço também vêm de configuração (`homeoffice.user.*`). Os valores das propriedades: **a confirmar** (fora do repositório).
- **RN-CRI-50** **[banco]** `av_homeoffice` é nulo em todas as 144 avaliações: o fluxo não deixou rastro nestes dados (a avaliação do home office pode estar em outro ambiente, ou ter sido removida). **A confirmar**.
- **RN-CRI-51** Na liberação home office, se o cargo não tem detalhamento aplicável, grava a pendência `cargo-not-found-gabarito`; sem pendência, materializa competências, detalhamentos, dicas (hints), pesos e graduações da avaliação e cria os registros de preenchimento. **[código]** `AvSvc:2062-2229`. Diferente do fluxo padrão (`liberarDefault`), que lança exceção em vez de gravar pendência.
- **RN-CRI-52** Rotina `ajustarAvalPesoCargoComp`: reprocessa todas as avaliações e, para cada preenchimento sem registro de peso, grava o peso do cargo-competência e do tipo de competência do **cargo atual** do avaliado; quando não há peso, usa **1**. **[código]** `AvSvc:2293-2363`. Parece ferramenta de correção de dados; quem chama: **a confirmar**.

## 10. Encerramento

- **RN-CRI-53** Não existe no código uma operação de "encerrar avaliação". O que existe é: (a) datas limite por etapa (`limiteAvN`, que fecham o período de preenchimento); (b) o estado `CONCLUIDA`, alcançado por transição genérica cuja origem não está no repositório (ver 05a, RN-EST-20 a 23, decisão 7.1-1: segue pendente); (c) a inativação, que força as datas para ontem (RN-EST-17). Não há regra que exija participantes preenchidos para concluir **[banco: 50 concluídas, com preenchimento parcial]**. A mensagem do fluxo home office fala em "data de encerramento dos ciclos de avaliação" controlada pelo RH (`MensageriaServiceImpl:226`), o que sugere que o encerramento é por data. **A confirmar** com o negócio.

## 11. Pontos a confirmar (resumo)

1. Semântica de Av1..Av4 e por que a gravação final sobrescreve os limites por perfil (RN-CRI-09 a 11).
2. Semântica do peso dos agentes (soma não é 100) e como entra no cálculo (RN-CRI-13). Depende do item 7 do roteiro.
3. Laço de remoção de agentes e apagar participantes ao mudar agentes (RN-CRI-14, 15, 22).
4. Avaliado que avalia a si mesmo como PARES (RN-CRI-26) e uso real do "revisado" e do revisor (RN-CRI-27, 28).
5. Exclusão de avaliadores/participantes com preenchimento já feito (RN-CRI-33, 41).
6. Planilha de pares: nomes não encontrados são descartados; homônimos (RN-CRI-36 a 38).
7. Gestor em recesso é encontrado e descartado sem subir de nível (RN-CRI-40).
8. Cópia: `criador`/`dataCriacao`, dimensões e e-mail padrão (RN-CRI-45, 46).
9. Fluxo home office e valores de configuração (RN-CRI-49 a 51).
10. Encerramento por data ou por estado (RN-CRI-53).
11. Assimetria de valores padrão entre criar e editar (RN-CRI-05) e 8 avaliações com `autoavaliacao` nulo.
12. Participantes de avaliação `LIBERADA` sem snapshot de cargo/departamento (11) e de `CONCLUIDA` (2), contra a regra de congelar (RN-EST-11).

## 12. Fontes lidas

`AvaliacaoServiceImpl.java` (todo o arquivo, com as passadas 1 e 2), `CargoServiceImpl.java` (163-354), `AvaliadorServiceImpl.java` (findAvaliador e getAvaliador), `AvaliacaoTipoCompetencia.java`, `TipoPendencia.java`, `TipoAvaliacaoPerfil.java`, `AvaliacaoResource.java`, `HomeOfficeBrasilResource.java` (só referências), `webapp/partials/avaliacao/partials/aba.agentes.html`. Banco: consultas agregadas somente leitura em `avaliacao`, `avaliacao_tipo_competencia`, `avaliacao_participante`, `avaliacao_pendencia`, `avaliacao_dimensao`, `campanha`, `pg_indexes`, `pg_constraint` e `information_schema` (2026-09-29). Nenhum dado pessoal foi lido.
