---
type: epic
title: "Direitos do titular e LGPD"
parent: initiative-levantamento-regras-dezi
covers: []
after: []
assignee: ""
risk: high
---

# Direitos do titular e LGPD

## Description

Anonimização de titular em todos os módulos pela porta `EliminacaoDeTitular`, catálogo de dados pessoais e hash de IP e matrícula.

## Outcome

A empresa cliente atende pedido de titular sem quebrar a trilha nem o consolidado imutáveis.

## Requirements

- L1: `cadastro` conduz a anonimização e cada módulo trata seus campos pessoais (AD-30).
- L2: Catálogo de dados pessoais por tabela, testado: coluna marcada pessoal tem tratador (AD-30).
- L3: IP e matrícula em auditoria e contadores guardados como HMAC (AD-30).

## Done when

1. A anonimização de um titular roda na ordem definida e só altera identificação e texto livre.
2. Nota, estado e consolidado permanecem intactos.
3. O teste de catálogo reprova coluna pessoal sem tratador.
4. A eliminação é reaplicável após restore.

## Boundaries

Transversal; dono é `cadastro` (condução) e cada módulo (tratador).

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-8, AD-30
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, seção Constraints

## Notes

- Open question: base legal, DPO, prazos, região do provedor e se pseudonimização basta são decisões jurídicas (AD-30).
