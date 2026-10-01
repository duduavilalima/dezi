# Modelo de competências — tipos, competências, perguntas, dicas, gabarito por cargo e dimensões

Módulo: `web/` · Submódulo 3 do roteiro. Continua [`05b`](05b-avaliacao-criacao-agentes-participantes.md) (validação do cargo para liberar, RN-CRI-19 e seção 5) e antecede o item 4 (graduação e conceitos).
Escopo: catálogo de tipos de competência (árvore), competências, detalhamentos (as "perguntas"), dicas por graduação (hints), vínculos do cargo (tipo, competência, gabarito com pesos), dimensões e o que a liberação congela.
Fora de escopo: escala e modelos de graduação (item 4), nota e média ponderada (item 7, já em 07), preenchimento (item 6).

Convenção: **[código]** = evidência no código; **[banco]** = consulta somente leitura ao Postgres em 2026-10-01 (host em rede Docker, dados até 2024-03-20; cópia ou produção: a confirmar); **A confirmar** = sem evidência suficiente.
Abreviações: `impl/` = `web/services/src/main/java/br/com/fasolti/service/impl/`; `AvSvc` = `impl/AvaliacaoServiceImpl.java`; `AvPartSvc` = `impl/AvaliacaoParticipanteServiceImpl.java`; `CompSvc` = `impl/CompetenciaServiceImpl.java`; `DetSvc` = `impl/CompetenciaDetalhamentoServiceImpl.java`; `TipoSvc` = `impl/TipoCompetenciaServiceImpl.java`; `CCSvc` = `impl/CargoCompetenciaServiceImpl.java`; `CTCSvc` = `impl/CargoTipoCompetenciaServiceImpl.java`; `GabSvc` = `impl/DetalhamentoCargoCompetenciaServiceImpl.java`; `DimSvc` = `impl/DimensaoServiceImpl.java`.
Situação: `Situacao` tem só `ATIVA` (1) e `INATIVA` (98) **[código]** `services.client/.../types/Situacao.java`. Os `CHECK`s de situação não existem no banco (V2.0_0027), o enum é a única restrição.

## 1. Glossário

| Termo | Significado | Evidência |
|---|---|---|
| Tipo de competência | Agrupador de competências, por empresa, com hierarquia (`id_pai`). Ex.: "Competências Técnicas", "Responsabilidades". É também o que o "agente" da avaliação escolhe avaliar (05b, RN-CRI-12) | `TipoCompetencia.java`, tabela `tipo_competencia` **[código][banco]** |
| Competência | Capacidade avaliada, de um único tipo. Tem descrição abreviada (nome), textos de direcionamento para gestor e não gestor | `Competencia.java` |
| Detalhamento | A **pergunta** de uma competência: descrição, ordem e modelo de graduação. A tela do preenchimento mostra detalhamentos agrupados por competência | `CompetenciaDetalhamento.java` |
| Hint (dica) | Texto explicativo de **cada nível da escala** para um detalhamento, com marca de "comentário obrigatório". Tabela `compet_detalhe_calibrahint` (nome legado "calibrahint") | `CompDetalhamentoHint.java` |
| Cargo × tipo | `cargo_tipo_competencia`: o tipo de competência está associado a um cargo, com peso e ordem | `CargoTipoCompetencia.java` |
| Cargo × competência | `cargo_competencia`: a competência está associada a um cargo, com peso e ordem | `CargoCompetencia.java` |
| Gabarito (detalhamento do cargo-competência) | `detalhamento_cargo_competencia` (nome original `cargo_compet_det_gabarito`): para cada detalhamento da competência no cargo, diz se é **aplicável ao cargo** e com que peso. É o que define as perguntas que o cargo recebe | `DetalhamentoCargoCompetencia.java`, V1.0_018, V1.0_019 |
| Dimensão | Agrupador opcional de detalhamentos, usado só em campanhas com `dimensao=true` (05b, seção 8) | `Dimensao.java` |
| Snapshot da liberação | Cópia congelada das perguntas, dicas e pesos nas tabelas `avaliacao_*` (ver seção 9) | ver seção 9 |

## 2. Modelo de dados envolvido

Cadeia: **empresa → tipo (árvore) → competência → detalhamento → hint (por graduação do modelo)**, e, do lado do cargo, **cargo → cargo×tipo, cargo → cargo×competência → gabarito (um por detalhamento)**.

| Tabela | Linhas **[banco]** | Campos de negócio |
|---|---|---|
| `tipo_competencia` | 118 (94 ativos, 24 inativos; 21 com pai) | `id_pai`, `id_empresa`, `nome`, `descricao`, `nome_curto`, `situacao` |
| `competencia` | 1.408 (1.160 ativas) | `id_empresa`, `id_tipo_competencia`, `descricao_abreviada` (150), `direcionamento_gestor/nao_gestor`, `id_modelo_graduacao` (opcional), `id_situacao` |
| `competencia_detalhamento` | 2.931 (2.652 ativos) | `id_competencia`, `descricao`, `ordem`, `id_modelo_graduacao` (obrigatório), `id_situacao` |
| `compet_detalhe_calibrahint` | 14.386 | `id_det_competencia`, `id_graduacao`, `hint`, `comentario_obrigatorio` |
| `cargo_tipo_competencia` | 2.048 (899 ativos) | `id_cargo`, `id_tipo_competencia`, `peso` (smallint, obrigatório), `ordem`, `id_situacao` |
| `cargo_competencia` | 10.360 (3.665 ativas) | `id_cargo`, `id_competencia`, `peso` (a migração V2.0_0014 criou nulável; entidade exige), `ordem`, `id_situacao` |
| `detalhamento_cargo_competencia` | 7.956 | `id_cargo_competencia`, `id_competencia_detalhamento`, `aplicavel_cargo` (obrigatório, padrão `false`), `peso` (nulável) |
| `dimensao` / `dimensao_competencia_detalhamento` | 4 / **0** | `nome`, `descricao`, `situacao`, `id_empresa` / vínculo dimensão × detalhamento |

**Unicidade no banco [banco]** (não há `CHECK` nem `UNIQUE` constraint nessas tabelas, só índices): `competencia (id_empresa, descricao_abreviada)`; `competencia_detalhamento (id_competencia, descricao)`; `cargo_competencia (id_cargo, id_competencia)`; `detalhamento_cargo_competencia (id_cargo_competencia, id_competencia_detalhamento)` (índice duplicado, dois com a mesma definição). **Sem unicidade:** `tipo_competencia` (nome por empresa), `cargo_tipo_competencia (id_cargo, id_tipo_competencia)`, `compet_detalhe_calibrahint (id_det_competencia, id_graduacao)`, `dimensao (nome, id_empresa)`.

## 3. Tipos de competência (árvore)

- **RN-MOD-01** **Escopo por empresa.** O tipo pertence a uma empresa (`id_empresa`) e a listagem filtra pela empresa do usuário logado. **[código]** `TipoSvc:76-79`. A listagem paginada devolve só os tipos **raiz** (`id_pai` nulo) **[código]** `TipoSvc:81-84`; filhos vêm por `obterFilhosDoTipoDeComepetencia` (`TipoSvc:122-131`).
- **RN-MOD-02** **Hierarquia.** Um tipo pode ter pai (`id_pai`) e a árvore é montada de forma recursiva, sem limite de profundidade no código (`TipoSvc:172-182, 237-249`). **[banco]** só há dois níveis (nenhum neto) e 21 dos 118 tipos têm pai. Não há controle de ciclo no código (mesmo ponto aberto do cargo e da unidade, ver 02). **A confirmar** se mais de dois níveis é requisito.
- **RN-MOD-03** **Competência pode ficar em tipo que tem filhos.** Nada impede associar competências a um tipo com subtipos, e a árvore mostra as duas coisas no mesmo nível (`CompSvc:659-678`). **[banco]** 31 competências estão sob tipos que têm filhos.
- **RN-MOD-04** **Nome único por empresa, comparação exata.** Ao criar ou editar, lança `validation.duplicate` se outro tipo da mesma empresa tem o **mesmo nome** (diferencia maiúsculas e não faz `trim`). **[código]** `TipoSvc:262-286`. **[banco]** sem índice único; hoje não há nome repetido na empresa. O código carrega todos os tipos para comparar em memória.
- **RN-MOD-05** **Nasce `ATIVA`** e com a empresa do usuário. **[código]** `TipoSvc:110-114`.
- **RN-MOD-06** **`nome_curto` não é informado: é sempre a descrição.** Em criar e editar, `nomeCurto = descricao`. **[código]** `TipoSvc:86-100`. O nome curto aparece em listagens e seleções de competência (`CompSvc:416, 435`).
- **RN-MOD-07** **Inativar um tipo inativa, em todos os cargos, o vínculo cargo×tipo e os vínculos cargo×competência daquele tipo** (e, por esse caminho, apaga os gabaritos, RN-MOD-23). **Não** inativa as competências do tipo, nem os subtipos. **[código]** `TipoSvc:102-108`; `CTCSvc:179-187`; `CCSvc:248-255`. **[banco]** não há competência ativa em tipo inativo (0), mas o código não impõe isso; os tipos inativos de hoje não têm competências ativas. **A confirmar** se o negócio espera que competências do tipo inativado também fiquem inativas.
- **RN-MOD-08** **Reativar um tipo não reativa nada.** Os vínculos cargo×tipo e cargo×competência continuam inativos e precisam ser reativados cargo a cargo. **[código]** (ausência de propagação em `TipoSvc:102-108`). Ativar o vínculo cargo×tipo com o tipo inativo lança `validation.status` (`CTCSvc:75-83`).
- **RN-MOD-09** **Enum `TipoComp`** (ORGANIZACIONAL 5, GERENCIAL 6, TECNICA 7, COMPORTAMENTAL 11, RESPONSABILIDADES 100) só é devolvido pelo endpoint `TipoResource` e não participa de nenhuma regra; os tipos reais são os da tabela, por empresa. **[código]** `services.client/.../types/TipoComp.java`, `rest/.../TipoResource.java:26-27`. **[banco]** as empresas cadastram nomes próprios (ex.: "Competências Técnicas", "AVD - Comportamental", "Resultados (metas)", e tipos de "Home Office" com uma só competência, ver 05b seção 9). Resíduo do modelo antigo, que a migração V2.0_0022/V2.0_0023 substituiu pela tabela `tipo_competencia`.

## 4. Competências

- **RN-MOD-10** **Nome único por empresa, sem diferenciar maiúsculas.** `descricaoAbreviada` (com `trim`, minúscula) não pode repetir na empresa; lança `validation.duplicate`. **[código]** `CompSvc:185-200`. O teste olha só a **primeira** competência encontrada (`get(0)`), o que basta enquanto houver uma só. **[banco]** índice único `(id_empresa, descricao_abreviada)`, mas **com** diferenciação de maiúsculas; o código é mais estrito que o banco.
- **RN-MOD-11** **Tipo obrigatório e fixado pela tela.** Na criação e edição o tipo é recarregado do banco pelo id informado (`CompSvc:136-168`); no banco `id_tipo_competencia` é obrigatório. A tela escolhe o tipo por uma árvore (`webapp/partials/cadastros/competencias/competencia.new.js`, `competencia.edit.js`).
- **RN-MOD-12** **Empresa e escopo.** A competência recebe a empresa do usuário e todas as consultas filtram por ela (`CompSvc:108-112, 136-138`). O código traz um `FIXME` dizendo que todos os `ServiceFilter` do sistema "foram feitos com copy/paste sem pensar nos casos específicos" (`CompSvc:110`).
- **RN-MOD-13** **Nasce `ATIVA`** (objeto novo devolvido pelo `doAfterCreate`, `CompSvc:179-183`). **Salvar um detalhamento novo reativa a competência** (`DetSvc:95-100`: carrega a competência, grava `ATIVA`, atualiza). Ou seja, a competência inativa volta a ficar ativa ao acrescentar uma pergunta, sem aviso. **[banco]** 2 competências ativas não têm nenhum detalhamento ativo.
- **RN-MOD-14** **Inativar uma competência inativa todos os vínculos cargo×competência ativos dela, em todos os cargos** (`CompSvc:170-176`). Não mexe nos detalhamentos (**[banco]** 382 detalhamentos ativos em competências inativas) nem em avaliações já liberadas (RN-MOD-38).
- **RN-MOD-15** **Reativar competência não reativa vínculos.** E ativar o vínculo cargo×competência com a competência inativa lança `validation.status` (`CCSvc:104-111`).
- **RN-MOD-16** **Direcionamentos.** `direcionamento_gestor` e `direcionamento_nao_gestor` são textos livres, opcionais, mostrados no preenchimento conforme o avaliador seja ou não gestor do avaliado (copiados para o snapshot, RN-MOD-38). **[banco]** 567 de 1.408 competências não têm texto de gestor e 569 não têm de não gestor.
- **RN-MOD-17** **Modelo de graduação: o efetivo é o do detalhamento.** A migração V2.0_0021 copiou o modelo da competência para cada detalhamento (`id_modelo_graduacao` virou obrigatório) e tornou o da competência opcional. **[banco]** 548 competências ainda têm modelo e 860 não; em 63 detalhamentos o modelo difere do da competência e 83 competências têm detalhamentos com **modelos diferentes entre si**. O código só usa o modelo da competência como alternativa quando o detalhamento não tem (`AvPartSvc:1048-1052`). Detalhes da escala: item 4.
- **RN-MOD-18** **Trocar o tipo de uma competência já associada a cargos não é bloqueado nem propagado.** O vínculo cargo×tipo de um cargo é procurado pelo **tipo atual** da competência (`CCSvc:116-117`), então a competência "muda" de tipo em todos os cargos de uma vez e pode ficar sem vínculo cargo×tipo (**[banco]** 9 vínculos cargo×competência não têm vínculo cargo×tipo correspondente). **A confirmar** (causa dos 9 e se a tela permite trocar o tipo na edição).

## 5. Detalhamentos (perguntas) e hints

- **RN-MOD-19** **Pergunta nasce `ATIVA`**, com descrição e ordem obrigatórias e modelo de graduação obrigatório no banco. **[código]** `DetSvc:60-63`; `CompetenciaDetalhamento.java:15-32`.
- **RN-MOD-20** **Descrição única dentro da competência, só ao criar.** Se já existe detalhamento da mesma competência com a mesma descrição (sem diferenciar maiúsculas, **inclusive inativo**), lança `validation.duplicate`. **[código]** `DetSvc:65-77`. A edição não valida. **[banco]** índice único `(id_competencia, descricao)` com diferenciação de maiúsculas. Não há validação de ordem única: **[banco]** 5 competências têm detalhamentos com a mesma ordem.
- **RN-MOD-21** **Hints (dicas por nível).** Cada detalhamento tem, no máximo em teoria, um hint por graduação **ativa** do modelo escolhido, com texto (`hint`, obrigatório) e `comentario_obrigatorio`. **[código]** `CompDetalhamentoHintServiceImpl.java:42-69`; `CompetenciaDetalhamento.java:35-36` (cascata total, remove órfãos).
  - Ao abrir um detalhamento, para cada graduação ativa do modelo que **não** tem hint, a tela recebe um hint vazio pré-preenchido com o `hint` e o `comentarioObrigatorio` da própria graduação (padrão da escala, item 4) (`CompDetalhamentoHintServiceImpl.java:46-69`).
  - **[banco]** 14.386 hints; `comentario_obrigatorio`: `true` em 287, nulo em 6.894 e `false` no restante (nulo vale "não", pela migração V1.0_017, que gravou `false` nos existentes). Sem índice único `(detalhamento, graduação)`, então duplicidade é possível, e hoje **a confirmar** se existe.
- **RN-MOD-22** **Quais hints são gravados.** Ao **criar** o detalhamento, só entram os hints cuja graduação está entre as graduações **ativas** do modelo (`DetSvc:101-106`; comparação por igualdade de objeto, **a confirmar** se funciona sempre). Ao **editar** um detalhamento ativo, os hints cuja graduação (por id) é ativa no modelo são regravados (`DetSvc:84-91`); ao **inativar** a edição não grava hints (`DetSvc:81-83`). Hints de um modelo antigo **não são apagados** ao trocar o modelo: **[banco]** 84 hints apontam para graduação de modelo diferente do atual do detalhamento. O snapshot da liberação (RN-MOD-38) ignora esses hints por filtrar pelo modelo do detalhamento (`AvPartSvc:1042-1046`), mas o caminho de liberação do home office não filtra (`AvSvc:2161-2175`). **A confirmar** se o efeito aparece lá.
- **RN-MOD-23** **Inativar o detalhamento apaga os gabaritos dele, em todos os cargos** (exclusão física, sem situação: `DetSvc:81-83`; `GabSvc:247-253`, com `TODO` pedindo exclusão lógica em `GabSvc:133, 237`). **[banco]** mesmo assim 3 gabaritos aplicáveis apontam para detalhamento inativo (gabaritos que sobrevivem à regra, ver RN-MOD-30).
- **RN-MOD-24** **Reativar o detalhamento não recria o gabarito.** O método `updateDetalhamentoCCList` ("ao reativar a competência, chama este método", `GabSvc:68-82`) **não é chamado em lugar nenhum** do `web/` **[código]**. A pergunta volta para o cargo só quando a tela do cargo recria a linha de gabarito (RN-MOD-26) e o RH marca "aplicável".
- **RN-MOD-25** **Perguntas ativas sem hints:** **[banco]** 1 detalhamento ativo não tem hint.

## 6. Cargo × tipo, cargo × competência e gabarito

- **RN-MOD-26** **O gabarito nasce sob demanda, nunca aplicável.** Ao abrir a associação de uma competência a um cargo, a tela recebe uma linha por detalhamento **ativo** da competência: as já gravadas mais as que faltam, estas com `aplicavelAoCargo=false` e sem peso, ordenadas pela ordem do detalhamento. **[código]** `GabSvc:84-130`. Só vira linha no banco quando o RH salva a associação: a tela grava o vínculo cargo×competência e depois **uma requisição por detalhamento** (criar ou atualizar), sem esperar o resultado de cada uma e avisando "sucesso" de qualquer forma (`dialogAssociarCompetencia.js:56-93, 175-188`); falha parcial passa despercebida. **A confirmar** (a tela tem mais de um ponto de entrada?). Por isso: **[banco]** 689 de 7.956 gabaritos não são aplicáveis (664 sem peso).
- **RN-MOD-27** **Perguntas que o cargo recebe.** A liberação usa os gabaritos com `aplicavelAoCargo=true` em que **o vínculo cargo×competência, o cargo, o detalhamento e a competência estão `ATIVA`**. **[código]** `DetalhamentoCargoCompetenciaRepository.java:20-29`, chamado em `AvSvc:753, 1334, 2071`. A consulta **não** exige que o vínculo cargo×tipo nem o tipo estejam ativos; esse filtro vem depois, pelos tipos escolhidos nos agentes da avaliação (05b, seção 5 e RN-CRI-19). **[banco]** 7.267 gabaritos são aplicáveis.
- **RN-MOD-28** **Pesos do cargo (três níveis).** O peso existe em três lugares, todos copiados no snapshot (RN-MOD-38) e usados pelo BI (07, RN-RES-11):
  - `cargo_competencia.peso` (competência dentro do cargo): a tela exige inteiro de 1 a 100 (`dialogassociarcompetencia.html:105-107`) e propõe **1** e a próxima ordem ao criar (`dialogAssociarCompetencia.js:152-158`); **[banco]** valores 1 (8.685), 2 (1.136), 3 (494), 4 (45), nenhum nulo.
  - `detalhamento_cargo_competencia.peso` (pergunta dentro da competência do cargo): a tela exige peso de 1 a 100 em todo detalhamento marcado como aplicável (`dialogassociarcompetencia.html:156`); **[banco]** 7.121 aplicáveis com peso 1, 88 com 2, 39 com 3, 19 com 4.
  - `cargo_tipo_competencia.peso` (tipo dentro do cargo, obrigatório): **não encontrei campo para editar esse peso** nas telas do cargo (`cargo.view.js`, `cargo.edit.js`, `associar.competencia.*`); **[banco]** 1.982 de 2.048 valem 1, os 66 restantes variam (os mais comuns: 50, 33, 67, 20, 25, 75, 80) e em 247 de 276 cargos com tipo ativo a soma **não** é 100; só 31 cargos têm algum peso acima de 1. Origem dos valores diferentes de 1 (importador, SQL manual, tela antiga): **a confirmar**.
  - Os pesos **não são percentuais** que somam 100 e o código não valida soma. A semântica de média ponderada está em 07.
- **RN-MOD-29** **Validação da tela do gabarito.** Salvar a associação exige peso em todo detalhamento marcado como aplicável; a mensagem diz "Pelo menos um deve ser selecionado com peso!", mas o teste só verifica os marcados, então **é possível salvar sem nenhum aplicável**. **[código]** `dialogAssociarCompetencia.js:56-71`. **[banco]** 15 vínculos cargo×competência ativos não têm nenhum detalhamento aplicável (esses cargos serão barrados na liberação por `cargo-not-found-gabarito`, 05b seção 5).
- **RN-MOD-30** **Cascata de inativação do cargo para baixo.** Inativar o cargo inativa todos os vínculos cargo×tipo e, em cada um, os vínculos cargo×competência (`CargoServiceImpl.java:112-117`, ver 02). Inativar o vínculo cargo×tipo inativa os cargo×competência do mesmo tipo (`CTCSvc:62-73`). Inativar um cargo×competência **apaga os gabaritos** dele (`CCSvc:97-100`, exclusão física, `GabSvc:239-245`). **[banco]** mesmo assim 35 vínculos inativos ainda têm gabarito e 40 gabaritos aplicáveis estão em vínculo inativo: dados anteriores à regra ou caminho que não passa por ela. **A confirmar.**
- **RN-MOD-31** **Ativação para cima.** Ativar um cargo×competência ativa o cargo×tipo correspondente e o **cargo** (`CCSvc:113-122`); ativar um cargo×tipo ativa o cargo (`CTCSvc:85-89`). É assim que o cargo (que nasce `INATIVA`, ver 02) passa a ativo ao associar competências. Se o cargo×tipo não existir, `updateSituacaoCargo` falha com `NullPointerException` (`CCSvc:117-118`, sem teste de nulo). **[banco]** 5 vínculos cargo×tipo ativos estão em cargo inativo.
- **RN-MOD-32** **Escopo de leitura do cargo×competência por perfil.** ADMINISTRADOR, GESTOR_RH e CONSULTOR_RH veem toda a empresa do usuário; GESTOR_AREA, o departamento dele; os demais, só o próprio cargo (`CCSvc:53-76`). Os outros três serviços (tipo, competência, detalhamento) filtram só por empresa. Pode-se gravar um gabarito para um detalhamento de **outra** competência: nenhum código valida que o detalhamento pertence à competência do vínculo; **[banco]** 195 gabaritos (171 aplicáveis) apontam para detalhamento de outra competência. Na liberação a pergunta aparece sob a competência do cargo (a chave é "competência do cargo + detalhamento", `AvSvc:2102`). **A confirmar** se é erro de carga ou uso intencional (compartilhar perguntas entre competências).
- **RN-MOD-33** **Cópia do cargo.** Copiar um cargo duplica vínculos cargo×tipo, cargo×competência e gabaritos, com situação, pesos e ordem iguais (`CargoServiceImpl.java:473-510`). Falha no clone só imprime a pilha, sem parar a cópia.

## 7. Dimensões

- **RN-MOD-34** **Cadastro.** Dimensão tem nome, descrição, situação e empresa. O nome não pode repetir na empresa **só ao criar** (`DimSvc:59-66`, `validation.duplicate`); a edição não valida. **[banco]** as 4 dimensões estão `ATIVA`, e duas têm o **mesmo nome na mesma empresa** ("Dimensão da Agerio", ids 102 e 103), o que mostra a duplicidade e faz o método `findByNomeAndEmpresaId` devolver mais de um resultado numa próxima criação com esse nome (**a confirmar** o erro).
- **RN-MOD-35** **Detalhamentos da dimensão.** Ao criar, cada detalhamento informado vira uma linha de vínculo (`DimSvc:68-74`). Ao editar, **se** a lista vier preenchida, todos os vínculos antigos são apagados e recriados; lista vazia **não** limpa (`DimSvc:81-97`), então não dá para esvaziar uma dimensão. Só `ATIVA` aparece na seleção da avaliação (`DimSvc:104-107`; o parâmetro `situacao` é ignorado e sempre usa `ATIVA`).
- **RN-MOD-36** **Efeito na liberação.** Com `campanha.dimensao=true`, as perguntas de cada participante ficam restritas aos detalhamentos das dimensões escolhidas na avaliação (casamento por id do detalhamento) (`AvSvc:~775-795`), e grava-se o vínculo dimensão × detalhamento no snapshot (`AvPartSvc:779-800, 1020-1022`), sem teste de nulo: se o detalhamento não pertence a nenhuma dimensão, `dimCompDetTO.getDimensao()` falha (`AvPartSvc:786-790`).
- **RN-MOD-37** **Dimensão não tem uso real [banco]:** `dimensao_competencia_detalhamento` tem **0** linhas, `avaliacao_dimensao` tem 0 e só 1 campanha tem `dimensao=true`. O que a liberação faz com essa campanha (nenhuma pergunta?) é **a confirmar**. Ver também 05b, seção 8.

## 8. Resumo das cascatas de situação

| Ação | Efeito [código] | Não faz |
|---|---|---|
| Inativar tipo | cargo×tipo e cargo×competência daquele tipo, em todos os cargos, ficam inativos; gabaritos desses vínculos são apagados | não inativa competências do tipo nem subtipos |
| Inativar competência | cargo×competência ativos ficam inativos (gabaritos apagados) | não inativa detalhamentos; não toca avaliações liberadas |
| Inativar detalhamento | gabaritos dele apagados em todos os cargos | não grava hints na edição |
| Inativar cargo×tipo | cargo×competência do mesmo tipo e cargo ficam inativos | — |
| Inativar cargo×competência | gabaritos apagados | — |
| Inativar cargo | cargo×tipo e cargo×competência do cargo ficam inativos | — |
| Reativar qualquer nível | só o próprio registro (bloqueado se o pai estiver inativo, nos dois vínculos de cargo) | nada é reativado para baixo; gabarito não é recriado (RN-MOD-24) |
| Salvar detalhamento novo | competência fica `ATIVA` | — |
| Ativar cargo×competência / cargo×tipo | ativa também o cargo (e o cargo×tipo, no primeiro caso) | — |

## 9. O que a liberação congela (ligação com 05a/05b)

- **RN-MOD-38** Na liberação, para cada pergunta aplicável do cargo do avaliado, grava-se (`AvPartSvc:752-873, 1006-1054`; fluxo home office em `AvSvc:2063-2190`):
  - `avaliacao_competencia`: id da competência, id e **nome** do tipo, descrição abreviada, direcionamentos, autor e data originais;
  - `avaliacao_comp_det`: id do detalhamento, descrição, ordem, modelo de graduação;
  - `avaliacao_comp_det_hint`: id do hint, id da graduação, texto, comentário obrigatório (só hints do modelo do detalhamento, RN-MOD-22);
  - `avaliacao_det_cargo_comp`: `peso_det_cargo_comp`, `peso_cargo_competencia`, `peso_cargo_tipo_competencia`, `aplicavel_cargo`, ids do gabarito e do vínculo;
  - modelo de graduação e graduações (item 4) e, se a campanha usa dimensões, o vínculo dimensão × detalhamento.
- **RN-MOD-39** **Efeito:** editar, inativar ou apagar competência, detalhamento, hint ou peso **depois** da liberação não altera avaliações já liberadas **[código]** (o preenchimento e os relatórios leem as cópias). **[banco]** os 4 conjuntos de tabelas somam 2.807, 5.678, 27.988 e 170.716 linhas; nenhum snapshot tem `aplicavel_cargo=false` nem peso nulo.
- **RN-MOD-40** **Peso do tipo no snapshot pode ficar nulo no código** (`CargoTipoCompetencia` não encontrado devolve `null`, `AvPartSvc:865`), mas **[banco]** não há nulo. A busca do cargo×tipo **não filtra situação**, então um vínculo inativo ainda fornece o peso.
- **RN-MOD-41** **Participantes que entram depois da liberação.** A competência, a pergunta e a dica são lidas do snapshot quando já existem e **criadas a partir do catálogo atual** quando não existem (`saveOrRead...`, `AvPartSvc:752-850`); já o registro de pesos (`saveAvaliacaoDetGab`, linha 852) **sempre grava uma linha nova**, sem procurar a existente. Resultado: avaliado incluído depois de uma edição do catálogo pode receber uma versão diferente da mesma competência, e os pesos se repetem por participante. **A confirmar** com o negócio (relaciona-se com 5b, RN-CRI-33 e 41, e com a duplicidade de pesos vista em 07, RN-RES-20).

## 10. Defeitos e fragilidades do legado a levar como requisito do sistema novo

(Não corrigir o legado.)

1. Exclusão **física** do gabarito ao inativar; reativação não restaura; pedir situação própria no gabarito e histórico (`TODO` no código).
2. Reativação ou inclusão de pergunta reativa a competência em silêncio (RN-MOD-13).
3. Falta de unicidade no banco para tipo (nome por empresa), cargo×tipo, hint por graduação e dimensão, e unicidade do código mais estrita ou mais frouxa que a do banco (RN-MOD-04, 10, 20, 34).
4. Cascatas parciais e inconsistentes na base atual (RN-MOD-07, 14, 30, 31); definir regra única de situação por nível.
5. Gabarito sem validação de que o detalhamento é da competência do cargo (RN-MOD-32).
6. Pesos sem semântica explícita: três níveis, nenhum percentual, sem tela para o do tipo e sem validação de soma (RN-MOD-28).
7. Modelo de graduação duplicado (competência e detalhamento) e hints órfãos ao trocar o modelo (RN-MOD-17, 22).
8. Dimensão sem uso, edição que não esvazia e erro sem teste de nulo (RN-MOD-35 a 37).
9. Snapshot com pesos regravados por participante e catálogo atual lido para quem entra depois (RN-MOD-41).
10. `nome_curto` derivado da descrição e enum `TipoComp` morto (RN-MOD-06, 09).

## 11. Pontos a confirmar (resumo)

1. Se mais de dois níveis de tipo e competência em tipo com subtipos são requisito (RN-MOD-02, 03).
2. Se inativar o tipo deve inativar as competências e subtipos (RN-MOD-07) e se reativar deve propagar (RN-MOD-08).
3. Troca do tipo de uma competência já associada a cargos e os 9 vínculos sem cargo×tipo (RN-MOD-18).
4. Comparação por igualdade na gravação de hints no insert (RN-MOD-22) e hints de modelo antigo (84) no fluxo home office.
5. Origem dos pesos de `cargo_tipo_competencia` diferentes de 1 e se deve existir tela para eles (RN-MOD-28).
6. Salvar associação sem nenhum detalhamento aplicável (RN-MOD-29; 15 casos).
7. Gabaritos e vínculos que sobrevivem à regra de exclusão (RN-MOD-23, 30; 3, 35 e 40 casos).
8. Gabaritos com detalhamento de outra competência: erro de carga ou reuso intencional (RN-MOD-32; 195 casos).
9. Dimensões: uso real, campanha com `dimensao=true` e 0 vínculos, e nome duplicado (RN-MOD-34 a 37).
10. Versão do catálogo usada por quem entra depois da liberação e duplicidade de pesos (RN-MOD-41).
11. `NullPointerException` ao ativar cargo×competência sem cargo×tipo (RN-MOD-31).
12. Quem pode editar o catálogo (competência, tipo, gabarito): o servidor não impõe perfil (ver 01, seção 6); só o escopo por empresa.

## 12. Fontes lidas

Serviços: `CompetenciaServiceImpl` (inteiro), `CompetenciaDetalhamentoServiceImpl`, `CompDetalhamentoHintServiceImpl`, `TipoCompetenciaServiceImpl`, `DimensaoServiceImpl`, `DimensaoCompetenciaDetalhamentoServiceImpl`, `CargoCompetenciaServiceImpl`, `CargoTipoCompetenciaServiceImpl`, `DetalhamentoCargoCompetenciaServiceImpl`; trechos de `AvaliacaoParticipanteServiceImpl` (752-873, 985-1054), `AvaliacaoServiceImpl` (740-795, 1905-1935, 2060-2190) e `CargoServiceImpl` (100-125, 465-515). Entidades, enum `Situacao` e `TipoComp`, repositório `DetalhamentoCargoCompetenciaRepository`, resources `CompetenciaResource`, `TipoCompetenciaResource`, `CargoCompetenciaResource`, `CargoTipoCompetenciaResource`, `DetalhamentoCargoCompetenciaResource`, `DimensaoResource` (só mapeamentos); migrações V1.0_016 a V1.0_020, V2.0_0014, V2.0_0021, V2.0_0022, V2.0_0023, V2.0_0030; webapp: `cadastros/competencias/competencia.new.js` e `competencia.edit.js`, `cadastros/cargo/dialogs/dialogAssociarCompetencia.js` e `.html`, buscas em `cargo.view.js`, `cargo.edit.js`, `associar.competencia.js` e `dimensao_entity.html`.
Não lidos: `CompetenciaDetalhamentoCalibrahintResource`, telas de dimensão e de tipo em detalhe, `webapp` do detalhamento (`dialog.detalhamento.html`), importador `support/` (pode explicar os pesos de cargo×tipo e os gabaritos cruzados), biblioteca `commons` (`BaseAvdService`: se `saveOrUpdate` e o `ServiceFilter` aplicam como se supõe).
Banco: consultas agregadas somente leitura (2026-10-01) em `tipo_competencia`, `competencia`, `competencia_detalhamento`, `compet_detalhe_calibrahint`, `cargo_competencia`, `cargo_tipo_competencia`, `detalhamento_cargo_competencia`, `dimensao`, `dimensao_competencia_detalhamento`, `avaliacao_*` (contagens), `campanha`, `pg_constraint`, `pg_indexes` e `information_schema`. Foram lidos nomes de tipos de competência e de dimensão (cadastro de catálogo); nenhum dado pessoal de funcionário.
