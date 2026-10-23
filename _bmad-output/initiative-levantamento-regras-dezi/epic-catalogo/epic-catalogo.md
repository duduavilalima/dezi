---
type: epic
title: "Catálogo de competências e escalas"
parent: initiative-levantamento-regras-dezi
covers: [CAP-3, CAP-4]
after: []
assignee: ""
risk: medium
---

# Catálogo de competências e escalas

## Description

Tipos, competências, perguntas, dicas, gabarito por cargo e escalas de graduação, com versões imutáveis a cada edição.

## Outcome

O RH mantém o catálogo e as escalas, e cada edição gera versão imutável que a avaliação pode fixar.

## Requirements

- CAP-3: Catálogo, cascatas e gabarito por cargo (spec CAP-3, success).
- CAP-4: Escalas com NPA e escala de comentário explícitos, validadas no servidor (spec CAP-4, success).

## Done when

1. Editar o catálogo cria nova versão e não altera versão já fixada.
2. `avaliarAplicabilidade(versaoId, cargoId, perfil)` devolve lista ou motivo codificado.
3. Gabarito de pergunta de outra competência é recusado.
4. NPA e escala de comentário são atributos, não texto; pontos e ordem validados no servidor.

## Boundaries

Módulos `catalogo`. Não inclui o snapshot de liberação (avaliacao). Dimensões, Av4 e conceito não entram (D6-D8).

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-3, CAP-4; modelo-competencias.md; graduacao-conceitos.md
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-9, AD-23

## Notes

- Open question: D48 (reuso de pergunta entre competências) e D60 (escala sem amplitude) a confirmar.
