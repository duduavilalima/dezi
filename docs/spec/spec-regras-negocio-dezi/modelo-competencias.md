# CAP-3 Catálogo de competências (fonte: `03-modelo-competencias.md`, prefixo RN-MOD)

Cadeia: **empresa → tipo (árvore) → competência → detalhamento (pergunta) → hint (por nível da escala)**; do lado do cargo: **cargo → cargo×tipo; cargo → cargo×competência → gabarito (um por pergunta)**.
Volumes [B]: 118 tipos, 1.408 competências, 2.931 perguntas, 14.386 hints, 2.048 cargo×tipo, 10.360 cargo×competência, 7.956 gabaritos, 4 dimensões (0 vínculos).

## Tipos de competência
- Escopo por empresa; listagem paginada devolve só raízes. Árvore sem limite de profundidade e sem controle de ciclo (hoje 2 níveis). Competência pode ficar em tipo com subtipos (31). (MOD-01..03) [C+B]
- Nome único por empresa, comparação exata (sem `trim`, com caixa); sem índice. Nasce ATIVA. `nome_curto` = descrição sempre. Enum `TipoComp` é resíduo morto. (MOD-04..06, 09) [C+B]
- **Inativar tipo** inativa, em todos os cargos, cargo×tipo e cargo×competência do tipo (apagando gabaritos); não inativa competências nem subtipos. **Reativar não reativa nada**; ativar cargo×tipo com tipo inativo → `validation.status`. (MOD-07, 08) [C+B] ⚠

## Competências
- Nome (`descricaoAbreviada`) único por empresa sem caixa (banco diferencia caixa; código mais estrito). Tipo obrigatório. Escopo por empresa. (MOD-10..12) [C+B]
- Nasce ATIVA; **salvar detalhamento novo reativa a competência** em silêncio. (MOD-13) [C]
- Inativar inativa todos os cargo×competência dela (gabaritos apagados); não toca detalhamentos (382 ativos em competências inativas) nem avaliações liberadas. Reativar não reativa vínculos; ativar vínculo com competência inativa → `validation.status`. (MOD-14, 15) [C+B]
- Direcionamentos (texto para gestor e não gestor): opcionais, copiados ao snapshot. (MOD-16) [C]
- **Modelo de graduação efetivo = o do detalhamento** (V2.0_0021 copiou o da competência; 63 detalhamentos divergem; 83 competências têm perguntas com escalas diferentes). (MOD-17) [C+B]
- Trocar o tipo de competência já associada não é bloqueado nem propagado (9 vínculos sem cargo×tipo). **No novo (D47):** troca bloqueada se há cargos associados. (MOD-18) [B]

## Perguntas (detalhamentos) e hints
- Nasce ATIVA; descrição, ordem e escala obrigatórias. Descrição única na competência só ao **criar** (sem caixa, inclusive inativo); ordem repetida possível (5 casos). (MOD-19, 20) [C+B]
- **Hints:** um por nível ativo da escala, com texto e `comentario_obrigatorio` (nulo = não). Níveis sem hint abrem pré-preenchidos com o texto do nível. Sem unicidade (detalhamento, nível). Hints de escala antiga não são apagados (84). (MOD-21, 22) [C+B] ⚠ igualdade de objeto no insert
- **Inativar pergunta apaga os gabaritos em todos os cargos** (exclusão física). **Reativar não recria gabarito** (`updateDetalhamentoCCList` sem chamador). (MOD-23, 24) [C]

## Cargo × tipo × competência × gabarito
- Gabarito nasce sob demanda, nunca aplicável: ao abrir a associação a tela recebe uma linha por pergunta ativa; só grava ao salvar (uma requisição por pergunta, sucesso exibido mesmo com falha parcial). 689 de 7.956 não aplicáveis. (MOD-26) [C+B] ⚠
- **Perguntas que o cargo recebe na liberação:** gabarito `aplicavelAoCargo=true` com vínculo cargo×competência, cargo, pergunta e competência ATIVA; o filtro por cargo×tipo/tipo vem depois, pelos tipos dos agentes. (MOD-27) [C]
- **Pesos em três níveis**, todos inteiros e **não percentuais** (sem validar soma): cargo×competência (tela 1–100, padrão 1; valores 1–4), gabarito (tela 1–100 se aplicável; 1–4), cargo×tipo (obrigatório, **sem tela de edição**; 1.982 de 2.048 valem 1, até 81). (MOD-28) [C+B] ⚠ origem dos ≠1
- Tela permite salvar associação sem nenhuma pergunta aplicável (15 casos → pendência `cargo-not-found-gabarito`). (MOD-29) [C+B]
- **Cascatas:** inativar cargo → cargo×tipo → cargo×competência (gabarito apagado). **Ativação sobe**: ativar cargo×competência ativa cargo×tipo e o **cargo** (cargo ativa-se assim); NPE se faltar cargo×tipo. Inconsistências existentes: 35 vínculos inativos com gabarito, 40 gabaritos aplicáveis em vínculo inativo. (MOD-30, 31) [C+B]
- Escopo de leitura do cargo×competência por perfil (como cargo). Nada valida que a pergunta pertence à competência do vínculo (195 gabaritos cruzados). **No novo (D49, ajusta D48):** pergunta com competência dona; cascata da D35 só atinge a dona; inativar pergunta reutilizada bloqueado com gabarito ativo em outro vínculo; uma vez por cargo. **No novo (D48):** reuso entre competências permitido; a pergunta conta na competência do vínculo do cargo (a confirmar). Cópia de cargo duplica vínculos e gabaritos. (MOD-32, 33) [C+B]

## Dimensões
- Nome único só ao criar (2 duplicadas); editar com lista vazia não esvazia; só ATIVA na seleção. Com `campanha.dimensao=true` a liberação restringe perguntas às dimensões da avaliação e grava o vínculo no snapshot (NPE se a pergunta não tem dimensão). **0 vínculos, 0 `avaliacao_dimensao`, 1 campanha** — sem uso real. (MOD-34..37) [C+B] ⚠

## Cascatas de situação (resumo)
Inativar tipo/competência/pergunta/vínculo/cargo propaga para baixo na cadeia do cargo e **apaga gabaritos**; reativar qualquer nível afeta só o próprio registro (bloqueado se o pai de cargo estiver inativo); salvar pergunta nova reativa a competência; ativar vínculo ativa o cargo. (seção 8 da fonte)

## O que a liberação congela
`avaliacao_competencia` (id, tipo id+nome, descrição, direcionamentos), `avaliacao_comp_det`, `avaliacao_comp_det_hint` (só hints da escala da pergunta), `avaliacao_det_cargo_comp` (pesos det, competência e tipo; aplicável), escala e níveis, vínculo de dimensão. Edição posterior não altera avaliação liberada; nenhum snapshot tem aplicável=false nem peso nulo. **Participante que entra depois** relê o catálogo atual quando faltam dados no snapshot, e os pesos são regravados por participante. (MOD-38..41) [C+B] ⚠
