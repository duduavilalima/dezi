---
type: epic
title: "PDI, apontamento e feedforward"
parent: initiative-levantamento-regras-dezi
covers: [CAP-8, CAP-9]
after: []
assignee: ""
risk: medium
---

# PDI, apontamento e feedforward

## Description

Ações de PDI com máquina de estados, histórico e vínculo opcional a avaliação, e apontamento e feedforward distintos.

## Outcome

Funcionários e gestores registram, aprovam e finalizam ações de desenvolvimento, com dono lido do registro.

## Requirements

- CAP-8: Estados EM_APROVACAO a FINALIZADO impostos no servidor (spec CAP-8, success).
- CAP-9: Feedforward recebido é do usuário logado; apontamento e feedforward são tipos distintos (spec CAP-9, success).

## Done when

1. Transições de PDI só pela porta única, com perfil, condição e histórico.
2. Ninguém aprova o próprio PDI (D22).
3. Feedforward recebido é sempre o do usuário logado.
4. Exclusão lógica com escopo.

## Boundaries

Módulo `pdi`, com CAP-9 dentro dele (módulo próprio se crescer).

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-8, CAP-9; pdi-feedforward.md
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-4, AD-8, AD-10

## Notes

- Open question: ELI-14 (PDI na árvore).
- Assumption: AD-10 é [ASSUMPTION].
