---
type: epic
title: "Resultados e acompanhamento"
parent: initiative-levantamento-regras-dezi
covers: [CAP-7]
after: []
assignee: ""
risk: high
---

# Resultados e acompanhamento

## Description

Motor de nota como função pura sobre o snapshot, parcial assíncrono, consolidado imutável na conclusão, acompanhamento e painéis por perfil.

## Outcome

O avaliado, o gestor e o RH veem a mesma nota e o mesmo progresso, vindos de uma única fonte.

## Requirements

- CAP-7: Nota reproduz a fórmula, pesos congelados, percentual correto e escopo por identificador (spec CAP-7, success).

## Done when

1. A função de nota única alimenta prévia e consolidado, com testes da fórmula (D60, consenso prevalece, NPA exclui).
2. O consolidado é gravado uma vez por avaliação e não muda depois de CONCLUIDA.
3. A API só devolve número dentro do escopo do AD-4.
4. Parcial e total usam filas separadas e o total reagenda enquanto houver parcial pendente.

## Boundaries

Módulo `resultados`. Os gráficos (A6) dependem da UX e ficam Adiados.

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-7; resultados.md
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-11, AD-12, AD-25, AD-26, AD-28

## Notes

- Parked: anonimato mínimo (AD-12) espera o limiar, o tratamento de gestor direto e da autoavaliação.
- Open question: a fórmula do novo precisa de validação com o negócio (D24).
