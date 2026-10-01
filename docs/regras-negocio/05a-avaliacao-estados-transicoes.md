# Avaliação — estados e transições (passada 1 de 2)

Módulo: `web/` · Submódulo 5 do roteiro (ciclo de vida da avaliação), parte "estados e transições".
Escopo: `Avaliacao`, `SituacaoAvaliacao`, `SituacaoPreenchimento`, flags `emLiberacao` / `avEmLiberacao`.
Fora de escopo (passada 2): criação, cópia, configuração de agentes/participantes, encerramento.

Convenção: **[código]** = evidência no código; **[migração]** = evidência só em migração; **[banco]** = confirmado na instância Postgres (nenhuma regra abaixo foi confirmada no banco: a instância não foi consultada). **A confirmar** = sem evidência suficiente.

Caminhos abreviados: `AvSvc` = `web/services/src/main/java/br/com/fasolti/service/impl/AvaliacaoServiceImpl.java`; `AvPartSvc` = `.../AvaliacaoParticipanteServiceImpl.java`.

## 1. Glossário

| Termo | Significado | Evidência |
|---|---|---|
| Avaliação | Ciclo de avaliação de desempenho dentro de uma Campanha; guarda perfis de agentes (auto, gestor, subordinados, pares), datas limite `limiteAv1..4` e flags `preencheAv1..4` | `domain/.../Avaliacao.java` |
| Campanha | Agrupador de avaliações; tem `Situacao` (ATIVA/INATIVA) e `avEmLiberacao` | `domain/.../Campanha.java` |
| Liberar | Materializar as perguntas para cada participante e abrir a avaliação para preenchimento | `AvSvc:639-849` |
| Situação (`id_situacao`) | Estado do ciclo de vida, enum `SituacaoAvaliacao` | ver 2 |
| Situação de preenchimento (`id_situacao_preenchimento`) | Progresso agregado do preenchimento, enum `SituacaoPreenchimento` | ver 3 |
| Av1..Av4 | Etapas de preenchimento (`preencheAvN` / `limiteAvN`). O significado de cada etapa não está nomeado no código | **a confirmar** |

## 2. Situação da avaliação (`SituacaoAvaliacao`)

| Valor | Código | Observação |
|---|---|---|
| `AG_LIBERACAO` | 1 | Aguardando liberação (planejamento). Estado inicial |
| `LIBERADA` | 2 | Aberta para preenchimento |
| `INATIVA` | 98 | Fora de uso; oculta do preenchimento |
| `CANCELADA` | 99 | Cancelada; não pode mais ser alterada |
| `CONCLUIDA` | 100 | Concluída |

Evidência: `web/services.client/.../types/SituacaoAvaliacao.java`; espelhado em `webapp/partials/model/SituacaoAvaliacao.js:21-25`.

- **[migração]** O comentário da coluna em `V1.0__Base_version.sql:142` diz "1 - em planejamento 2 - liberada (ativa) 3 - inativa", mas o `CHECK` da mesma migração aceitava 1, 2, 98 e 99, e o enum usa 98 para inativa. O comentário está desatualizado. `CONCLUIDA` (100) não estava no `CHECK` original.
- **[migração]** `V2.0_0027` remove `check_situacao` e `check_situacao_preenchimento` de `avaliacao` e `check_situacao` de `campanha`. Portanto, o banco (segundo as migrações) não restringe os valores; a restrição fica só no enum Java. **A confirmar** no Postgres real.

## 3. Situação de preenchimento (`SituacaoPreenchimento`)

`NAO_APLICAVEL`=0, `NAO_PREENCH`=1, `PARCIALMENTE_PREENCH`=2, `TOTALMENTE_PREENCH`=3 (`.../types/SituacaoPreenchimento.java`).

- **[migração]** O `CHECK` original aceitava só 1, 2 e 3 (`V1.0__Base_version.sql:113`), removido depois (V2.0_0027).

## 4. Transições

### 4.1 Diagrama (o que o código implementa de fato)

```
                 criar
   (novo) ───────────────► AG_LIBERACAO ──── liberar ────► LIBERADA
                             │    ▲                           │  │
                excluir      │    └── liberar (reentrada) ────┘  │ inativar (via update)
                (remove)     ▼                                  ▼
                          (apagada)                          INATIVA
   CANCELADA ◄── transição genérica ──  ?      CONCLUIDA ◄── transição genérica ── ?
```

Transições **implementadas e verificadas**: criar → `AG_LIBERACAO`; `AG_LIBERACAO` → `LIBERADA`; `LIBERADA` → `INATIVA` (via update); `AG_LIBERACAO` → apagada. As setas para `CANCELADA` e `CONCLUIDA` dependem do mecanismo genérico descrito em 4.6 e **não foram verificadas**.

### 4.2 Criação → `AG_LIBERACAO`

- **RN-EST-01** Toda avaliação nasce com situação `AG_LIBERACAO` e situação de preenchimento `NAO_PREENCH`; a empresa é a do usuário logado. **[código]** `AvSvc:203-207` (`setDefaultValues`). O próprio código tem `TODO Implementar comportamento do TransactionalObject`.

### 4.3 Liberação: `AG_LIBERACAO` → `LIBERADA`

Ponto de entrada: `POST /avaliacao/liberar` (`AvaliacaoResource.liberar`) → `AvSvc.liberar` (`AvSvc:639`).

- **RN-EST-02** Ao iniciar a liberação, a avaliação é gravada como `AG_LIBERACAO` com `emLiberacao=true` e a campanha com `avEmLiberacao=true` (trava de "liberação em andamento"). **[código]** `AvSvc:640-649`.
- **RN-EST-03** O restante da liberação roda em **thread separada** (não bloqueia a requisição); erro na thread envia mensagem de erro (`mensagemLiberarAvaliacaoErro`) e apenas registra log. **[código]** `AvSvc:655-693`.
- **RN-EST-04** Se `avHomeOffice` é verdadeiro, usa a rotina `liberarHomeOffice`; caso contrário `liberarDefault`. **[código]** `AvSvc:656-692`. Conteúdo de `liberarHomeOffice` (`AvSvc:2043+`) fica para a passada 2.
- **RN-EST-05** Só um administrador **não** dispara a mensagem de "liberação iniciada" (`mensagemLiberarAvaliacao`); para os demais perfis ela é enviada. **[código]** `AvSvc:651-653`. Intenção **a confirmar**.
- **Pré-condições de `liberarDefault`** (`AvSvc:698-849`), todas lançam exceção e abortam a liberação:
  - **RN-EST-06** Deve haver ao menos um participante (`empty-participantes`). `AvSvc:711-713`.
  - **RN-EST-07** Não pode haver pendências da avaliação (`error-pendencias`). `AvSvc:715-719`.
  - **RN-EST-08** Para cada cargo dos avaliados deve existir ao menos um detalhamento aplicável e ativo (`Situacao.ATIVA`); senão "não há detalhamentos aplicáveis ao cargo X". `AvSvc:753-758`.
  - **RN-EST-09** Se a avaliação tem autoavaliação e não há perguntas para o perfil do avaliador naquele cargo, aborta ("não há perguntas para avaliadores com perfil X para o cargo Y"). `AvSvc:768-771`. Se a autoavaliação está desligada e não há perguntas, o código não aborta e segue (**a confirmar** se é intencional).
- **RN-EST-10** Se a campanha usa dimensões (`Campanha.dimensao`), as perguntas do participante são filtradas para as que pertencem às dimensões da avaliação. Se o filtro resulta em vazio, não aborta: registra pendência `cargo-not-found-gabarito-dimensao` (tipo CARGO) e mantém as perguntas originais. **[código]** `AvSvc:773-802`. Como a lista de pendências não é gravada depois nem impede a liberação (a checagem de pendências já ocorreu), o efeito prático da pendência é **a confirmar**.
- **RN-EST-11** Cada participante liberado tem `idCargoAvaliado`, `nomeCargoAvaliado`, `idDeptoAvaliado`, `nomeDeptoAvaliado` congelados a partir do cargo/departamento atual do avaliado (só se ainda nulos), e suas perguntas são gravadas. **[código]** `AvSvc:805-820`. É um snapshot: mudanças posteriores no cargo do funcionário não alteram a avaliação.
- **RN-EST-12** Ao final com sucesso: avaliação → `LIBERADA`, `emLiberacao=false`; campanha → `avEmLiberacao=false`; envia `mensagemLiberarAvaliacaoConcluido`. **[código]** `AvSvc:827-839`.
- **Falha:** em caso de exceção na thread, a avaliação **permanece `AG_LIBERACAO` com `emLiberacao=true`** e a campanha com `avEmLiberacao=true`; não há código que reverta essas flags. **Suspeito, a confirmar** (pode travar a campanha).

### 4.4 Liberação incremental (avaliação já `LIBERADA`)

- **RN-EST-13** Ao salvar avaliadores de uma avaliação já `LIBERADA`, usa-se `salvarAvaliadoresLiberacao`: se ela ainda não estava `LIBERADA` libera todos os participantes; se já estava, libera apenas os **novos**. **[código]** `AvSvc:1112-1117`.
- **RN-EST-14** O `AvaliacaoResource.salvarAvaliadores` desvia para `liberar(...)` quando a avaliação está `LIBERADA` e só a autoavaliação está ativa (sem gestores, pares nem subordinados). **[código]** `AvaliacaoResource.java:93-100`. Comportamento **a confirmar** (parece atalho para reprocessar a autoavaliação).
- **RN-EST-15** Em avaliação `LIBERADA`, todo participante salvo deve estar revisado (`revisado=true`); senão `error-liberacao-revisados`. **[código]** `AvSvc:1611-1613`. Participantes criados sob avaliação `LIBERADA` já nascem revisados (`AvPartSvc:215-217`; `AvSvc:1686-1687`).
- **RN-EST-16** `liberarParticipantes` também grava `LIBERADA` diretamente por `updateStatus` ao terminar. **[código]** `AvSvc:1384-1385`, `AvaliacaoRepository.java:26-29`. Note que aqui **não** zera `emLiberacao` nem trata a campanha.

### 4.5 `LIBERADA` → `INATIVA`

- **RN-EST-17** Ao atualizar uma avaliação cuja situação atual não é `INATIVA` para `INATIVA`, as datas limite `limiteAv1..3` são forçadas para **ontem**, "para não mostrar a avaliação inativa nos preenchimentos". **[código]** `AvSvc:377-385` (comentário `FIXME`). Ou seja, a inativação depende também das datas; o filtro de telas de preenchimento usa `LIBERADA` e datas.
- **RN-EST-18** `limiteAv4` não é alterado na inativação. **[código]** `AvSvc:382-384`. Omissão suspeita, **a confirmar**.
- **RN-EST-19** Não há caminho no código para reativar (`INATIVA` → `LIBERADA`), mas nada o proíbe: a validação de update só bloqueia `CANCELADA`. **A confirmar**.

### 4.6 `CANCELADA` e `CONCLUIDA`

- **RN-EST-20** Avaliação `CANCELADA` **não pode ser atualizada** (`validation.canceled`). **[código]** `AvSvc:373-376`.
- **RN-EST-21** Os hooks `doAfterTransition` gravam `CANCELADA` e `CONCLUIDA` diretamente na entidade, com comentário `FIXME: Provisório, pois a chamada $transition não tá funcionando`. **[código]** `AvSvc:268-287`. O cancelamento tem `TODO Verificar as ações que devem ser feitas após o cancelamento`: nenhuma consequência é implementada (participantes, preenchimentos, mensagens ficam como estão).
- **RN-EST-22** `doBeforeTransition` traz `TODO Validar se pode transicionar` e não valida nada: qualquer transição solicitada é aceita. Só para `LIBERADA` zera `situacaoPreenchimento` para `NAO_PREENCH`. **[código]** `AvSvc:258-266`.
- **Não encontrado no repositório:** o endpoint genérico que dispara `doBeforeTransition`/`doAfterTransition` (`AvaliacaoResource` só sobrescreve `valueOfStatus`, `AvaliacaoResource.java:50-53`). Ele vem da biblioteca externa `br.com.fasolti.commons` (`commons-services`), que não está no repositório nem em `~/.m2`. Por isso **não sei quem, com que perfil e em que condições** muda uma avaliação para `CANCELADA` ou `CONCLUIDA`. **A confirmar.**
- **RN-EST-23** Efeitos observáveis de `CONCLUIDA` nas consultas (só leitura): preenchimento mobile e participantes mobile excluem avaliações `CONCLUIDA` (`PreenchimentoMobileServiceImpl.java:76`, `ParticipanteMobileServiceImpl.java:58`); consultas de preenchimento usam `CONCLUIDA` como parâmetro em `CompetenciaServiceImpl.java:312` e `AvaliacaoCompDetServiceImpl.java:65-66` (semântica do parâmetro **a confirmar**). Na tela, os controles de participantes são desabilitados em `CONCLUIDA` (`webapp/partials/avaliacao/partials/aba.participantes.html:173,208`).
- **RN-EST-24** Efeitos de `CANCELADA`/`INATIVA` em relatórios: a view `conjunto_resultados` v2/v3 exclui avaliações com `id_situacao` 98 e 99 (`V3.0_20210405000001__view_conjunto_resultados_v3.sql:53`; v2 na linha 49, com precedência de `AND`/`OR` que pode não excluir como se espera — **suspeito, a confirmar**).

### 4.7 Exclusão

- **RN-EST-25** Uma avaliação só pode ser excluída em `AG_LIBERACAO`; em qualquer outro estado lança `error-status-remover-avaliacao`. **[código]** `AvSvc:1748-1773` (`removeragliberacao`, `GET /avaliacao/{id}/removeragliberacao`).
- **RN-EST-26** A exclusão apaga, nesta ordem: participantes em `AG_LIBERACAO`, mensageria da avaliação, `AvaliacaoTipoCompetencia`, pendências e a própria avaliação. **[código]** `AvSvc:1752-1769`. Não há exclusão lógica.
- **RN-EST-27** Remover um participante de avaliação `LIBERADA` também apaga todas as perguntas dele (`removerTodasPerguntasDoParticipante`). **[código]** `AvPartSvc:257-260`.

## 5. Bloqueios por situação

| Situação | O que fica bloqueado | Onde |
|---|---|---|
| `LIBERADA` | Edição dos flags de agentes, percentuais e datas na tela (campos desabilitados) | `webapp/.../aba.agentes.html`, `aba.resumo.edit.html` (~20 ocorrências de `object.status === 'LIBERADA'`) |
| `LIBERADA` | Exclusão da avaliação | `AvSvc:1748` |
| `CANCELADA` | Qualquer update | `AvSvc:374` |
| `CONCLUIDA` | Alterações nos participantes (só na tela) | `aba.participantes.html:173,208` |
| `emLiberacao=true` | Campo existe em avaliação e campanha; **não encontrei código que bloqueie ações** enquanto ele está `true`. Só a tela pode usá-lo | **a confirmar** (buscar em `webapp/`) |

Bloqueios só na tela (webapp) não são regra do servidor: a API não impede a mesma alteração.

## 6. Situação de preenchimento: regras de mudança

- **RN-EST-28** Inicial `NAO_PREENCH` (`AvSvc:206`); ao liberar volta a `NAO_PREENCH` (`doBeforeTransition`, `AvSvc:261-264`).
- **RN-EST-29** Se a avaliação está `TOTALMENTE_PREENCH` e um participante é atualizado (fluxo que reprocessa graduações), ela volta a `PARCIALMENTE_PREENCH`. **[código]** `AvPartSvc:366-371`.
- **RN-EST-30** Em `atualizaAvaliadorAndRevisor`, se a avaliação **não** está `NAO_PREENCH`: com 1 participante, fica `NAO_PREENCH` se `limiteAv1` é nulo, ou `PARCIALMENTE_PREENCH` se Av1 foi preenchida; com mais de 1, fica `NAO_PREENCH` se nenhum participante preencheu Av2 **e** `limiteAv1` e `limiteAv4` do participante são nulos, senão `PARCIALMENTE_PREENCH`. **[código]** `AvPartSvc:440-467`. O próprio método tem `FIXME` dizendo que era usado por uma aba que não existe mais: pode ser código morto (**a confirmar** se há chamadores).
- **RN-EST-31** Não achei código que leve a avaliação a `TOTALMENTE_PREENCH`. **[código, por ausência]** Só aparece o enum e os retornos dele para `PARCIALMENTE_PREENCH`. O cálculo real do progresso é `calcularPreenchimentoTotalDaAvaliacao`, que gera um **percentual** sem gravar a situação: `(Av1+Av2+Av3 preenchidas) * 100 / (Av1+Av2+Av3 totais)`, ou 0 se não há totais; Av4 não entra. **[código]** `AvSvc:1970-1997`. Será detalhado na passada de preenchimento (item 6).

## 7. Pontos a confirmar (resumo)

1. Quem dispara `CANCELADA` e `CONCLUIDA`, com quais permissões e pré-condições (biblioteca `commons-services` fora do repositório). RN-EST-20 a 23.
2. Falha na thread de liberação deixa `emLiberacao=true` na avaliação e na campanha, sem reversão. RN-EST-12.
3. Se `emLiberacao` bloqueia alguma ação (só a tela usaria). Seção 5.
4. Se `INATIVA` pode voltar a `LIBERADA`; e por que `limiteAv4` não é ajustado na inativação. RN-EST-17 a 19.
5. Efeito da pendência `cargo-not-found-gabarito-dimensao`. RN-EST-10.
6. Autoavaliação desligada sem perguntas não aborta. RN-EST-09.
7. Atalho de liberação só com autoavaliação em `AvaliacaoResource.salvarAvaliadores`. RN-EST-14.
8. Precedência `AND`/`OR` na view `conjunto_resultados` v2. RN-EST-24.
9. Significado de Av1..Av4 e se `TOTALMENTE_PREENCH` é usado. RN-EST-31.
10. Valores reais de `id_situacao` no Postgres (constraint removida): consultar a instância.

## 7.1 Decisões do responsável (registradas na revisão)

1. **Quem dispara `CANCELADA`/`CONCLUIDA` (item 1 acima): segue pendente.** A biblioteca `commons` não está mais acessível. A definição só fecha depois do levantamento de acessos e perfis (submódulo 1 do roteiro) e/ou da validação no banco e na tela.
2. **Falha na liberação (item 2): confirmado como defeito a corrigir no sistema novo.** O processo de liberação será redesenhado (fica como requisito para o sistema novo, não como correção no legado, conforme a política do `AGENTS.md`). Pontos a levar para o redesenho: estado de erro explícito ou reversão das flags `emLiberacao`/`avEmLiberacao`; execução assíncrona com acompanhamento; idempotência (liberar de novo após falha).
3. **Valores reais de `id_situacao` (item 10): validar no Postgres.** Consultas propostas na resposta de revisão; nenhuma executada ainda.
4. **`TOTALMENTE_PREENCH` e situação de preenchimento (item 9): defeito/lacuna a corrigir no sistema novo.** Hoje o progresso é só um percentual calculado, sem estado gravado. O sistema novo deve ter um estado de preenchimento consistente e único.

## 7.2 Validação no banco (evidência **[banco]**)

Consulta em 2026-09-29, usuário somente leitura `dezi_leitura`, banco `dezi` (host em rede Docker; **cópia ou instância real: a confirmar**; a data mais recente de atualização é 2024-03-20). Só consultas agregadas; nenhum dado pessoal foi lido.

| Consulta | Resultado | Efeito sobre as regras |
|---|---|---|
| Versão do esquema (`schema_version`) | 93 migrações, 0 falhas, última `3.0.20220124000001`; os 93 arquivos do repositório batem com as 93 aplicadas | O esquema do banco corresponde às migrações do repositório (não há divergência de versão) |
| Restrições `CHECK` em `avaliacao` e `campanha` | Nenhuma | **Confirma** V2.0_0027: o banco não limita `id_situacao`; só o enum Java limita |
| `avaliacao` por (`id_situacao`, `id_situacao_preenchimento`) | AG_LIBERACAO(1): 10 · LIBERADA(2): 58 · INATIVA(98): 26 · **CONCLUIDA(100): 50** · CANCELADA(99): **0**. Total 144. `id_situacao_preenchimento` = 1 em **todas** | Ver abaixo |
| `em_liberacao` / `av_em_liberacao` verdadeiros | 0 avaliações, 0 campanhas | Hoje não há liberação presa; o defeito do item 2 não se manifestou nestes dados (não invalida o risco) |
| `em_liberacao`, `av_homeoffice` | `em_liberacao` = false em 79 e nulo em 65; `av_homeoffice` nulo em **todas** | Fluxo home office (`liberarHomeOffice`) nunca usado nestes dados |
| `campanha.id_situacao` | ATIVA(1): 49 · INATIVA(98): 27 | Só os valores esperados |

Conclusões (atualizam a seção 7):

- **`CONCLUIDA` é usada de fato** (50 avaliações, 2018-12 a 2023-05, 10 atualizadores distintos), então algum caminho a grava: a transição genérica ou edição direta. Como e quem: **segue pendente** (decisão 7.1-1).
- **`CANCELADA` nunca foi usada** (0 linhas). Pode ser estado sem uso real. **A confirmar** com o negócio antes de reproduzi-la no sistema novo.
- **`id_situacao_preenchimento` é sempre 1 (`NAO_PREENCH`)**, inclusive nas 50 avaliações `CONCLUIDA` e nas 58 `LIBERADA` com milhares de participantes já preenchidos. Confirma RN-EST-31: a situação de preenchimento da avaliação é um campo **morto**; o progresso real vem dos participantes. Reforça a decisão 7.1-4.
- **Inativação (RN-EST-17):** em 18 das 26 avaliações `INATIVA`, algum limite Av1..Av3 é igual ao dia anterior à `data_atualizacao`, coerente com a regra "limite = ontem". Nas outras 8, não: a inativação pode ter ocorrido por outro caminho ou antes da regra existir. **A confirmar**.
- **Todas** as avaliações têm todas as datas limite vencidas (incluindo as 58 `LIBERADA`): nenhuma está aberta hoje. Não permite inferir se `LIBERADA` continua sendo encerrada manualmente ou só pelo prazo.
- **Avaliações `CONCLUIDA` sem `limite_av2`:** 13 de 50. Regra de conclusão **não** exige Av2 preenchida. **A confirmar** se é aceitável.
- Participantes: preenchimento Av2 e Av3 existe em `LIBERADA` (7.069 e 1.603 de 7.616) e em `CONCLUIDA` (443 e 319 de 961). Ou seja, `CONCLUIDA` **não** exige todos os participantes preenchidos.

## 8. Fontes lidas

`AvaliacaoServiceImpl.java` (linhas 195-490, 636-850, 1100-1118, 1275-1300, 1370-1392, 1595-1625, 1676-1695, 1745-1780, 1965-2000, 2043-2062, 2200-2230; o arquivo tem 2.366 linhas e o restante fica para a passada 2), `AvaliacaoResource.java`, `Avaliacao.java`, `Campanha.java`, `AvaliacaoRepository.java`, enums em `services.client/.../types`, trechos de `AvaliacaoParticipanteServiceImpl.java`, migrações `V1.0__Base_version.sql`, `V2.0_0027`, `V3.0_20200525115300`, `V3.0_20210405000001`, e `webapp/partials/model/SituacaoAvaliacao.js` e `aba.*.html`. Banco Postgres: consultado em 2026-09-29 (seção 7.2). As marcações "nenhuma regra confirmada no banco" no cabeçalho valem para o texto das seções 1 a 7; a seção 7.2 traz o que o banco confirmou.
