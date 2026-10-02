# Graduação e conceitos — escalas, níveis, pontos, cores, congelamento e conceito

Módulo: `web/` · Submódulo 4 do roteiro. Continua [`03`](03-modelo-competencias.md) (o modelo de graduação efetivo é o do detalhamento, RN-MOD-17; hints por nível, RN-MOD-21/22) e alimenta [`06`](06-participantes-preenchimento.md) e [`07`](07-resultados-graficos.md) (nota = `ponto`).
Escopo: escala de graduação (`modelo_graduacao`), níveis (`graduacao`), "Não posso avaliar", escala só de comentário, cores, cópia congelada na liberação (`modelo_graduacao_comp`, `graduacao_competencia`, `modelo_graduacao_aval_comp`) e o cadastro de conceito.
Fora de escopo: cálculo da nota e das médias (07), preenchimento (06), hints por pergunta (03).

Convenção: **[código]** = evidência no código; **[banco]** = consulta somente leitura ao Postgres em 2026-10-01 (host em rede Docker, dados até 2024-03-20; cópia ou produção: a confirmar); **A confirmar** = sem evidência suficiente.
Abreviações: `impl/` = `web/services/src/main/java/br/com/fasolti/service/impl/`; `MGSvc` = `impl/ModeloGraduacaoServiceImpl.java`; `GradSvc` = `impl/GraduacaoServiceImpl.java`; `ConcSvc` = `impl/ConceitoServiceImpl.java`; `GCSvc` = `impl/GraduacaoCompetenciaServiceImpl.java`; `MGCSvc` = `impl/ModeloGraduacaoCompServiceImpl.java`; `MGACSvc` = `impl/ModeloGraduacaoAvalCompServiceImpl.java`; `AvPartSvc` = `impl/AvaliacaoParticipanteServiceImpl.java`; `AvSvc` = `impl/AvaliacaoServiceImpl.java`; `webapp/escala/` = `webapp/partials/cadastros/modelosgraduacao/`.
Situação: `ATIVA` (1) e `INATIVA` (98); os `CHECK`s foram removidos (V2.0_0027), o enum Java é a única restrição.

## 1. Glossário

| Termo | Significado | Evidência |
|---|---|---|
| Escala (modelo de graduação) | Conjunto de níveis de resposta que uma pergunta (detalhamento) aceita. Por empresa. Ex.: "Escala 1 a 5", "Comentário". A tela chama de "Escala de graduação" (mensagem de duplicidade) e de "Modelo" | `ModeloGraduacao.java`, tabela `modelo_graduacao` **[código][banco]** |
| Graduação (nível) | Uma opção da escala: `label` (até 40), `hint` (texto explicativo), `ponto` (nota), `ordem` e `cor` | `Graduacao.java`, tabela `graduacao` |
| Ponto | A nota do nível. Valor bruto, sem normalização entre escalas (07, RN-RES-13) | `Graduacao.ponto` |
| "Não posso avaliar" (NPA) | Nível especial com `ponto = 0`, que não entra em médias (07, RN-RES-29). Identificado **pelo texto exato** do `label` | `MGSvc:84`, `GradSvc:247, 266` |
| `nao_graduar` | Marca do modelo. No código significa "oferecer o nível NPA" (ver RN-GRA-04) | `ModeloGraduacao.java:20` |
| Escala de comentário | Convenção: modelo "Comentário" com um único nível de `ponto = 0` ("Comentar"). Faz a pergunta ser só de texto. Não há tipo próprio no código | **[banco]** 5 modelos |
| Cópia congelada (snapshot) | Cópia da escala feita na liberação da avaliação: `modelo_graduacao_comp` (modelo), `graduacao_competencia` (níveis) e `modelo_graduacao_aval_comp` (ligação com a competência ou a pergunta da avaliação) | V2.0_0019, `AvPartSvc:875-972` |
| Conceito | Cadastro por empresa com nome, descrição, faixa de pontuação e tipo. **Sem uso** (ver seção 7) | `Conceito.java`, tabela `conceito` |

## 2. Modelo de dados envolvido

| Tabela | Linhas **[banco]** | Campos de negócio |
|---|---|---|
| `modelo_graduacao` | 89 (76 ATIVA, 13 INATIVA; 14 empresas) | `id_empresa`, `descricao_modelo` (80; a base original tinha 40, V1.0_005), `qtd_graduacoes`, `nao_graduar` (nulável; V1.0_015 gravou `false` nos existentes), `id_dimensao` (obrigatório), `id_situacao` |
| `graduacao` | 440 (410 ATIVA, 30 INATIVA; 71 são NPA: 42 ativas e 29 inativas) | `id_modelo_graduacao`, `label` (40, obrigatório), `hint` (text), `ponto` (smallint), `ordem` (smallint), `cor` (text, V2.0_0043), `id_situacao` |
| `modelo_graduacao_comp` | 318 (133 avaliações) | `id_modelo_graduacao` (escala original), `id_avaliacao`, `descricao_modelo`, `nao_graduar`, `id_dimensao`, `id_empresa`, `atualizador_original`, `data_atualizacao_original` |
| `graduacao_competencia` | 1.701 | `id_modelo_graduacao_comp`, `id_graduacao` (nível original), `label`, `hint`, `ponto`, `ordem`, `cor`, `atualizador_original`, `data_atualizacao_original` |
| `modelo_graduacao_aval_comp` | 6.194 (530 por competência da avaliação; 5.664 por pergunta) | `id_modelo_graduacao_comp`, `id_avaliacao_competencia` ou `id_avaliacao_comp_det` |
| `conceito` | **0** | `id_empresa`, `id_situacao`, `id_tipo_conceito` (obrigatório), `nome`, `descricao` (255), `pontuacao_inicial`, `pontuacao_final` (double) |

**Unicidade e integridade no banco [banco]:** índice único `modelo_graduacao (id_empresa, descricao_modelo)` e `graduacao (id_modelo_graduacao, label)`, ambos **com** diferenciação de maiúsculas (o código ignora maiúsculas, é mais estrito). FK `graduacao_competencia.id_graduacao → graduacao` (impede apagar nível já congelado). **Não há FK** de `compet_detalhe_calibrahint.id_graduacao` nem de `avaliacao_preenchimento.id_graduacao_avN` para `graduacao`. Respostas guardam o nível vivo (`id_graduacao_avN`) e o congelado (`id_graduacao_comp_avN`, FK para `graduacao_competencia`, V2.0_0042).

## 3. Escala (modelo de graduação)

- **RN-GRA-01** **Escopo e nome único por empresa.** A listagem filtra pela empresa do usuário (`MGSvc:63-69`). O nome (`descricaoModelo`) não pode repetir na empresa, comparando com `trim` e sem diferenciar maiúsculas, inclusive contra escala inativa; lança `validation.duplicate` ("escala de graduação") ao criar e ao editar (`MGSvc:95-101, 112-118, 120-124`). O método `findByEmpresaAndDescricaoModelo` **ignora** o parâmetro `idEmpresa` e usa a empresa do logado. Tela: nome obrigatório, 2 a 80 caracteres (`webapp/escala/modelosgraduacao.edit.html`); TO: máximo 80, empresa, situação e dimensão obrigatórias (`ModeloGraduacaoTO.java:13-29`). **[banco]** o índice único diferencia maiúsculas (seção 2): o banco aceita nomes que o código recusa.
- **RN-GRA-02** **Valores iniciais de escala nova** (`MGSvc:103-110`): `qtdGraduacoes = 0`, empresa do usuário, `situacao = INATIVA`, `naoGraduar = TRUE` e **dimensão = a primeira dimensão da tabela inteira** (`dimensaoRepository.findAll().iterator().next()`), de qualquer empresa. **[banco]** 86 das 89 escalas (14 empresas) apontam para a dimensão 100, que pertence à empresa 100; 3 apontam para a 103. A dimensão da escala não é lida por nenhuma regra: só é copiada para o snapshot (RN-GRA-21). Importador: usa a dimensão do parâmetro `default.dimensao.id` (`support/.../EscalaGraduacoesSheet.java:29-41`).
- **RN-GRA-03** **Ao salvar com `naoGraduar = true`** (padrão, e o campo é desabilitado na tela: `modelosgraduacao.edit.html`, `ng-disabled="true"`): grava `qtdGraduacoes = 1` e `situacao = ATIVA` (`MGSvc:71-78`) e **cria o nível NPA**, com `label` e `hint` "Não posso avaliar", `ponto = 0`, `ordem` nula e `INATIVA` (`MGSvc:80-93`). Resultado: a escala nasce `ATIVA` com um único nível, inativo. Se `naoGraduar` for falso ou nulo nada disso acontece (só possível por API; a tela não deixa mudar). **A confirmar** se `doAfterSave` roda também na edição (a classe base está em `commons`); **[banco]** nenhuma escala tem dois NPA, e `GradSvc:128-134` impediria o segundo.
- **RN-GRA-04** **`nao_graduar` não indica que a escala tem NPA.** O campo só decide a criação inicial do nível (RN-GRA-03), é copiado ao snapshot (`AvPartSvc:888`) e trava o campo de pontuação do nível no diálogo (`webapp/partials/dialogs/modelograduacao/dialog_graduacao.html:24`, não lido por inteiro). **[banco]** das 82 escalas com `true`: 37 têm NPA ativo, 29 têm NPA inativo e 16 não têm NPA; das 7 com `false`: 5 têm NPA ativo e 2 não têm. O que vale na prática é existir **nível ativo com texto "Não posso avaliar"**. Importador grava `true` sem criar NPA (`EscalaGraduacoesSheet.java:54`).
- **RN-GRA-05** **`qtd_graduacoes` é dado morto.** Só recebe 0 (criação), 1 (RN-GRA-03) e a contagem feita pelo importador no momento da carga (`GraduacaoSheet.java:73-79`); nunca é atualizado ao criar, inativar ou remover nível. **[banco]** 52 das 89 escalas divergem do número de níveis ativos; 54 escalas valem 1 e têm de 1 a 11 níveis. A tela mostra o campo desabilitado.
- **RN-GRA-06** **Situação da escala.** Criar um nível **reativa a escala** (`GradSvc:158-165`). Ativar e inativar pelo botão de situação do cadastro (`webapp/escala/modelosgraduacao.view.js`, `onChangeSituacao`). `doBeforeUpdate` não tem efeito: reatribui `ATIVA` a quem já é `ATIVA` (`MGSvc:112-118`). **Inativar não verifica uso nem inativa os níveis.** **[banco]** 10 escalas inativas são usadas por 476 detalhamentos ativos. A inativação só tira a escala da lista de escolha ao criar ou editar competência (`webapp/partials/cadastros/competencias/competencia.controller.js:196-198` filtra `ATIVA`); a liberação **não** olha a situação da escala (`MGSvc:132-153` sem filtro; `AvPartSvc:875-911`), então ela segue valendo nas perguntas já associadas.
- **RN-GRA-07** **Não há remoção de escala na tela** (só de nível, RN-GRA-15). O banco impede apagar escala usada (FKs).
- **RN-GRA-08** **Perfis (só na tela).** Ver: `CONSULTOR_RH`; criar e editar: `ADMINISTRADOR` e `GESTOR_RH` (`webapp/escala/modelosgraduacao.route.js:7-8, 44-45`); "Detalhes" (auditoria) só `ADMINISTRADOR` (`modelosgraduacao.view.html:35`). O servidor não verifica perfil (01, seção 6).

## 4. Níveis (graduações)

- **RN-GRA-09** **Campos e limites.** `label` obrigatório, até 40; `ponto` obrigatório e inteiro; `hint` livre (`GraduacaoTO.java:10-18`). A tela (`webapp/escala/dialogs/dialog_graduacao.html`) exige rótulo, pontuação e descrição; **pontuação de 1 a 1000, ou 0 travado** se o nível já tem 0 (nível comum não consegue receber 0 pela tela). O servidor só exige não nulo: a faixa vive na tela. **[banco]** `ponto` de 0 a 120 nos níveis ativos.
- **RN-GRA-10** **Rótulo único na escala**, sem diferenciar maiúsculas e com `trim`, **contando níveis inativos**; lança `validation.duplicate` ("graduação") ao criar e ao editar (`GradSvc:128-144, 186-190`). **[banco]** hoje não há repetição.
- **RN-GRA-11** **Nasce `ATIVA`** (`GradSvc:152-156`). Depois de salvar, reativa a escala e **renumera a ordem** (`GradSvc:158-165`, RN-GRA-12). A tela de criação não pede ordem. Nível novo entra com `ordem` nula e o repositório ordena por `ordem` crescente (`GraduacaoRepository`): qual posição ele toma antes da renumeração é **a confirmar** (depende do ordenamento de nulos do Postgres).
- **RN-GRA-12** **Ordem.** Após criar, ativar ou inativar (não após editar: a chamada em `GradSvc:169` está comentada), `updateGraduacoesOrdem` (`GradSvc:232-277`):
  - níveis ativos recebem 1..n na ordem atual; o NPA ativo, **identificado pelo texto exato "Não posso avaliar"**, fica sempre por último;
  - níveis inativos recebem n+1.. ordenados por `ponto` crescente, NPA por último.
  - Renomear o NPA pela tela faz ele perder o tratamento especial (a tela deixa editar o rótulo). Migrações V3.0_20200902/04/24 ajustaram os NPA existentes (`ordem` nula, depois 1000, depois max+1; `ponto = 0`).
  - Subir e descer: a tela troca a `ordem` de dois níveis por duas atualizações (`webapp/escala/modelosgraduacao.view.js`, `onMoveGraduacaoUp/Down`); botões bloqueados para NPA, inativos e `ordem` nula (`modelosgraduacao.view.html:213, 218`).
  - **[banco]** 3 escalas têm `ordem` repetida entre níveis ativos; 1 nível inativo tem `ordem` nula e 1 tem `ordem` 1000.
- **RN-GRA-13** **Ativar e inativar nível** por `GET /graduacao/{id}/ativar|inativar` (`GraduacaoResource.java:37-47`), em transação própria, seguido de renumeração (`GradSvc:213-230`). Sem validação: dá para inativar todos os níveis (**[banco]** 1 escala ativa sem nenhum nível ativo utilizável) ou deixar um só. Inativar não toca hints nem snapshots; novas perguntas só geram hints para níveis ativos (`CompetenciaDetalhamentoServiceImpl.java:85, 101`; 03, RN-MOD-22).
- **RN-GRA-14** **Editar nível reescreve o texto de todas as perguntas.** `doAfterUpdate` copia o `hint` do nível para **todos** os hints de detalhamento que o usam (`GradSvc:167-176`), apagando textos personalizados por pergunta (03, RN-MOD-21). **[banco]** 807 dos 14.386 hints têm texto diferente do nível (personalizados, ou de versão anterior), e seriam sobrescritos na próxima edição. Rótulo e ponto não são copiados (hints não os guardam). A tela pede confirmação (`DIALOG.MSG_EDIT_MG`, texto não lido) e recarrega a página (`webapp/escala/dialogs/dialog_graduacao.dialogs.js`, `save`). Avaliações já liberadas **não** mudam (RN-GRA-22).
- **RN-GRA-15** **Remover nível.** Bloqueado se algum hint de detalhamento o usa, com a mensagem "Esta graduação não pode ser removida pois está associada a algum hint de datalhamento de competência" (`GradSvc:178-183`). Não há outra regra de código: a FK de `graduacao_competencia` impede apagar nível já congelado (erro genérico de banco). **[banco]** 61 dos 440 níveis não têm hint; 27 destes já foram congelados (não removíveis). Como não há FK de hint nem de resposta, a regra de código é a única proteção dos hints.
- **RN-GRA-16** **Níveis sem filtro de empresa.** `GradSvc` não define `getServiceFilter`; a busca por modelo usa o id enviado pelo cliente (`GraduacaoResource.java:26-35`). A proteção por empresa existe só ao abrir o modelo (RN-GRA-01). **A confirmar** o comportamento padrão da classe base (`commons`).
- **RN-GRA-17** **Código morto e nomes enganosos.** `POST /graduacao/graduacoesativaspormodelo` devolve **todos** os níveis (chama `findAllGraduacoesByModelo`, `GraduacaoResource.java:32-35`); quem usa filtra "ativa" no navegador (`competencia.controller.js:696-701`). `findAllGraduacoesAtivasByModelo` compara a situação (enum) com o texto `"ATIVA"` (`GradSvc:72`) e nunca devolve nada; nenhum chamador. `updateCoresGraduacaoCompetenciaByModelo` (`GCSvc:38-46`, anotado `readOnly` mas grava) nunca é chamado.

## 5. Pontos, "não graduar" e cores

- **RN-GRA-18** **Escala de pontos livre.** O código não exige ponto crescente com a ordem, nem diferente entre níveis. **[banco]** nas 83 escalas com 2 ou mais níveis ativos: 55 crescem com a ordem, **15 decrescem** (ordem 1 = maior ponto) e 13 têm primeiro e último iguais. Pontos repetidos entre níveis ativos em 15 escalas; ex.: uma escala "0 a 10" tem `ponto = 10` nos níveis 1 a 10 e 0 no nível "0" (**a confirmar** se é erro de cadastro). Máximo da escala (níveis ativos): 100 (43 escalas), 120 (15), 50 (8), 5 (6), 10 (5) e outros. Misturar escalas em média: 07, RN-RES-13.
- **RN-GRA-19** **`ponto = 0` significa "sem nota".** Vale para o NPA (a tela trava em 0 e a migração V3.0_20200924 zerou os existentes; **[banco]** 0 NPA com ponto diferente de 0) e para a **escala de comentário**. **[banco]** 5 escalas "Comentário" (cada uma com um nível ativo "Comentar", `ponto = 0`), usadas como pergunta só de texto; `comentario_obrigatorio` em 20 dos 31 hints delas. O importador aceita ponto 0 em qualquer nível (`GraduacaoSheet.java:60`). O efeito nas médias está em 07 (RN-RES-29).
- **RN-GRA-20** **Cor: três origens que não conversam.**
  - `cor` gravada em `graduacao` e `graduacao_competencia` (V2.0_0043): **nenhuma tela edita** (o diálogo não tem o campo). Só o importador a preenche, com 5 cores para as ordens 1 a 5 (`GraduacaoSheet.java:105-113`); ordem acima de 5 fica sem cor. **[banco]** 206 níveis e 720 níveis congelados sem cor; cinco valores (`#f44336`, `#782b90`, `#fdb913`, `#4aa741`, `#1c9ad6`).
  - **Paleta fixa por ordem** `GraduacaoCorEnum` (11 cores; ordem nula ou acima de 10 vira preto `#000000`): **sobrescreve a cor gravada** em todas as telas de preenchimento, web e mobile (`AvaliacaoCompDetServiceImpl.java:87`; `CompetenciaServiceImpl.java:255`; `PreenchimentoMobileServiceImpl.java:501, 554, 571`). A cor vista no preenchimento é função da **ordem**, não do cadastro.
  - Consequência: com 15 escalas decrescentes (RN-GRA-18), a ordem 1 (vermelho) é o nível de maior nota. **A confirmar** se é intencional.
  - **[código]** nenhuma view versionada de BI lê `cor`; uso pelo BI **a confirmar** (Pentaho fora do repositório).
- **RN-GRA-20a** **Ordenação na tela de preenchimento.** Web ordena os níveis por `ordem` (`AvaliacaoCompDetServiceImpl.java:94`; `CompetenciaServiceImpl.java:262`); **mobile ordena por `ponto`** (`PreenchimentoMobileServiceImpl.java:579-580`, falha com `ponto` nulo). Em escala decrescente a ordem exibida difere entre web e mobile.

## 6. Escala na avaliação: congelamento e uso

- **RN-GRA-21** **O que a liberação congela.** Para cada par (escala, avaliação) cria um `modelo_graduacao_comp` copiando descrição, `nao_graduar`, dimensão, empresa, id da avaliação, "atualizador e data originais" e `created_by` = nome de quem liberou, e copia **todos** os níveis da escala, **ativos e inativos**, para `graduacao_competencia` com `ordem`, `label`, `hint`, `ponto`, `cor` e id original (`AvPartSvc:875-912, 948-972`, via `GradSvc:192-211`). Se já existe snapshot do par (escala, avaliação) ele é **reaproveitado sem recopiar** (`AvPartSvc:879-883, 911`); níveis criados depois não entram nele. A ligação com a competência da avaliação ou com a pergunta é gravada em `modelo_graduacao_aval_comp` (`AvPartSvc:914-946`). **[banco]** 318 snapshots em 133 avaliações; 64 avaliações têm 1 escala, as demais de 2 a 8; nenhum par duplicado.
- **RN-GRA-22** **Depois da liberação a avaliação não muda.** Rótulo, ponto, ordem, texto e situação editados no cadastro não afetam a avaliação liberada. **[banco]** o cadastro vivo já difere do congelado em 5 rótulos, 4 pontos, 120 ordens e 29 textos (de 1.701 níveis). A nota do resultado vem do congelado (07, RN-RES-07).
- **RN-GRA-23** **Como a avaliação acha a escala.** Pelo par (escala da pergunta, avaliação) (`MGCSvc:79-87`) e o nível por (snapshot, id do nível original) (`GCSvc:53-76`). Caminhos alternativos por competência da avaliação e por id da escala (`MGCSvc:47-66`, `ModeloGraduacaoAvalCompRepository`) esperam um único resultado e têm `FIXME` de múltiplos; **a confirmar** o efeito quando uma competência tem perguntas com escalas diferentes (83 competências, 03 RN-MOD-17; **[banco]** 69 avaliações têm 2 ou mais escalas, ver RN-GRA-21).
- **RN-GRA-24** **Resposta guarda nível vivo e congelado.** Ao gravar `id_graduacao_avN` grava-se também `id_graduacao_comp_avN` (06, RN-PRE-09). A reabertura do consenso refaz esse vínculo **sem teste de nulo** (`AvPartSvc:344-358, 415-429`). **[banco]** 235 respostas Av1, 883 Av2 e 63 Av3 têm nível e não têm vínculo (as de Av2: todas de 2017-10-17, 4 avaliações; ficam fora do BI, 07, RN-RES-07); **23 respostas Av2 apontam para um nível congelado cujo nível original não é o escolhido** (**a confirmar**). Nenhuma resposta aponta para nível de escala diferente da do detalhamento.
- **RN-GRA-25** **Home office tem cópia própria** (`AvSvc:2237-2284`). Ali a cor é calculada por `GraduacaoCorEnum.valueOf(ordem)` (`AvSvc:2274`), que devolve nulo para ordem nula ou acima de 11, e o `.getNome()` seguinte falha com `NullPointerException` antes de a cor gravada ser reatribuída. Qualquer escala com nível sem `ordem` ou com `ordem` 1000 (**[banco]** 1 nível de cada, ambos inativos) quebraria a liberação do home office, que copia também níveis inativos. **A confirmar** se essas escalas são usadas nesse fluxo.

## 7. Conceito

- **RN-GRA-26** **Conceito não está implementado.** O cadastro tem `nome`, `descricao`, `pontuacao_inicial`, `pontuacao_final`, empresa, situação e tipo (`TipoConceito`: `TIPOCOMPETENCIA` 1, `COMPETENCIA` 2, `DETALHAMENTO` 3). O único comportamento é listar os conceitos da empresa do usuário (`ConcSvc:26-29`, `GET /conceito/conceitosdaempresalogada`, `ConceitoResource.java:26-30`). **Não existe** regra que converta nota em conceito, nem validação de faixa (ordem, sobreposição, cobertura), nem unicidade. **[código]** `webapp/services/conceito.js` não é referenciado por nenhuma tela e o `mobile/` não usa conceito; **[banco]** a tabela tem **0 linhas**. Conclusão: a "faixa de nota → conceito" do roteiro **não existe** no sistema legado.
- **RN-GRA-27** **Defeitos de modelagem.** A entidade mapeia o tipo como `TipoComp` (códigos 5, 6, 7, 11, 100; `Conceito.java:40`, com comentário "não existe tipo para conceito atualmente"), enquanto o TO usa `TipoConceito` (códigos 1 a 3; `ConceitoTO.java:18`); o `CodedEnumType` devolve nulo para código desconhecido (`CodedEnumType.java`, `valueOf`). `id_tipo_conceito` é obrigatório no banco. A coluna `descricao` veio de `descricao_conceito varchar(5)` (V2.0_0035, renomeada e ampliada para 255) e `nome` foi acrescentada nulável. **A confirmar** se o tamanho original (5) indicava uma sigla de classificação (inferência, sem evidência).

## 8. Ligações com os outros módulos

| Regra deste documento | Onde continua |
|---|---|
| Modelo da pergunta, hints por nível, `comentario_obrigatorio` | 03 (RN-MOD-17, 21, 22) |
| Cópia do nível no preenchimento e vínculos `id_graduacao_comp_avN` | 06 (RN-PRE-09) |
| `ponto = 0` fora das médias; escala não normalizada; nota do BI vem do congelado | 07 (RN-RES-07, 13, 29) |
| Modelo congelado na liberação | 05a (RN-EST-08 a 11), 05b (RN-CRI-42) |

## 9. Pontos a confirmar

1. Semântica e destino de `nao_graduar` (RN-GRA-04): é "tem NPA"? Hoje não reflete a realidade.
2. `doAfterSave` da escala roda na edição? (RN-GRA-03; `commons`).
3. Posição de nível novo antes da renumeração (RN-GRA-11).
4. Escalas decrescentes (15) e cor por ordem: intencional? (RN-GRA-18, 20).
5. Pontos repetidos e escala "0 a 10" com 10 em todos os níveis: erro de cadastro? (RN-GRA-18).
6. Escala de comentário (`ponto = 0`) é convenção ou deveria ser tipo de pergunta? (RN-GRA-19).
7. A cor gravada é usada pelo BI? Quem deveria defini-la? (RN-GRA-20).
8. Ordem de níveis diferente entre web (ordem) e mobile (ponto) é intencional? (RN-GRA-20a).
9. Inativar escala deveria impedir uso em perguntas ou só esconder da escolha? (RN-GRA-06).
10. Escopo por empresa e perfil de `Graduacao` e `Conceito` no servidor (RN-GRA-08, 16; `commons`).
11. Efeito do `FIXME` de múltiplos snapshots por competência (RN-GRA-23).
12. As 23 respostas Av2 com vínculo divergente (RN-GRA-24).
13. Liberação do home office com escala de nível sem `ordem` (RN-GRA-25).
14. Conceito foi abandonado? Entra no sistema novo? Para classificar o quê (nota final, competência, pergunta)? (RN-GRA-26, 27).
15. Texto da mensagem de confirmação `DIALOG.MSG_EDIT_MG` ao editar nível (RN-GRA-14).

## 10. Defeitos do legado a levar como requisito do sistema novo (não corrigir o legado)

- Dimensão da escala sem sentido (primeira dimensão do banco) e sem uso: descartar.
- `qtd_graduacoes` e `nao_graduar` sem semântica confiável: derivar ou descartar; NPA identificado por texto: usar atributo próprio.
- Unicidade no banco sem diferenciar maiúsculas (escala por empresa, nível por escala).
- Faixa de ponto, ponto distinto e consistência com a ordem validados no servidor, e escala de comentário como tipo explícito.
- Editar nível não pode apagar texto personalizado por pergunta sem aviso, e deve ter versionamento coerente com o congelamento.
- Inativar escala e nível com efeito definido (hoje só esconde); impedir escala sem nenhum nível utilizável.
- Cor definida em um só lugar (hoje cadastro ignorado pela tela) e ordem de exibição única entre web e mobile.
- Congelar escala por versão, sem `NullPointerException` e sem vínculos ausentes ou divergentes nas respostas.
- Conceito: decidir se existe; se existir, faixas validadas, tipo consistente e regra explícita de cálculo.
- Autorização por perfil e escopo por empresa também no servidor (nível e conceito).

## 11. Fontes lidas

`MGSvc` (todo), `GradSvc` (todo), `ConcSvc`, `GCSvc`, `MGCSvc`, `MGACSvc` (todos); `GraduacaoResource`, `ModeloGraduacaoResource`, `ConceitoResource`, `GraduacaoCompetenciaResource`; entidades `ModeloGraduacao`, `Graduacao`, `GraduacaoCompetencia`, `ModeloGraduacaoComp`, `ModeloGraduacaoAvalComp`, `Conceito`; `ModeloGraduacaoTO`, `GraduacaoTO`, `ConceitoTO`; enums `TipoConceito`, `GraduacaoCorEnum`, `CodedEnumType`; `AvPartSvc` linhas 335-362 e 870-975; `AvSvc` linhas 2190-2300; `AvaliacaoCompDetServiceImpl` 60-120; `CompetenciaServiceImpl` 225-275; `PreenchimentoMobileServiceImpl` 490-580; `support/.../GraduacaoSheet.java` e `EscalaGraduacoesSheet.java`; telas `webapp/escala/` (`edit.js`, `view.js`, `route.js`, `edit.html`, parte de `view.html`, `dialogs/dialog_graduacao.html` e `.dialogs.js`), `webapp/services/conceito.js`; migrações V1.0_005, V1.0_015, V2.0_0019, V2.0_0021, V2.0_0024, V2.0_0035, V2.0_0042, V2.0_0043, V2.0_0044, V3.0_20200902, V3.0_20200904, V3.0_20200924 e trechos de V1.0 (base).
**Não lidos:** `webapp/partials/dialogs/modelograduacao/dialog_graduacao.html` (só a linha 24), textos i18n, `ModeloGraduacaoSimpleTO`, `GraduacaoMobileTO`, `DetalhamentoSheet` (só o uso da escala), `commons` (`BaseAvdService`), ETL e relatórios do BI, `mobile/`.
