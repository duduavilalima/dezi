---
type: epic
title: "Preenchimento das etapas"
parent: initiative-levantamento-regras-dezi
covers: [CAP-6]
after: []
assignee: ""
risk: high
---

# Preenchimento das etapas

## Description

Respostas por etapa (Av1, Av2, Av3), validação de comentário, completude e prazo no servidor, e consenso com aceite por senha.

## Outcome

Avaliado e avaliadores respondem dentro do prazo, e a completude é definida uma única vez.

## Requirements

- CAP-6: Validação no servidor, `preenchida_avN` derivado e consenso por regra única (spec CAP-6, success).

## Done when

1. O servidor recusa resposta sem comentário obrigatório, finalização incompleta e gravação após o prazo.
2. `completa` é derivado e `finalizada` só se alcança se completa.
3. O aceite do consenso exige a senha da própria parte e é apagado ao reabrir.
4. N gravações paralelas não geram deadlock.

## Boundaries

Módulo `preenchimento`. Não inclui nota nem painel (resultados).

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-6; preenchimento.md
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-10, AD-19, AD-26

## Notes

- Parked: Av3 e quantos consensos por avaliado quando há mais de um avaliador (AD-26) esperam o negócio.
- Assumption: AD-10 é [ASSUMPTION].
