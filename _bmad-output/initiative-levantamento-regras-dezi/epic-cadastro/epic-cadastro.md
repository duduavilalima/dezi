---
type: epic
title: "Cadastro organizacional"
parent: initiative-levantamento-regras-dezi
covers: [CAP-2]
after: []
assignee: ""
risk: medium
---

# Cadastro organizacional

## Description

Empresa, unidade, departamento, cargo e funcionário, com gestor, situação cadastral e funcional, subordinados e importação de planilhas.

## Outcome

O RH mantém a estrutura organizacional de forma consistente, e os outros módulos leem o escopo pelas views públicas.

## Requirements

- CAP-2: Cadastro e situação de funcionário, gestor do departamento, unicidade e subordinados (spec CAP-2, success).

## Done when

1. Salvar um funcionário comum não remove o gestor do departamento.
2. E-mail, código do cliente e nome de unidade são únicos no banco.
3. Recesso termina na data fim por job idempotente e revoga ou restaura o acesso.
4. As views `escopo_visivel` e `lotacao_atual` existem com `security_invoker` e teste de esquema.
5. Importação de funcionários por planilha devolve relatório de erros por linha.

## Boundaries

Módulo `cadastro` e a importação de planilhas de cadastro. Não é credencial nem perfil (acesso).

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-2; docs/spec/spec-regras-negocio-dezi/cadastro-organizacional.md
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-17, AD-24, AD-25

## Notes

- Parked: histórias que dependem de ELI-1 (coordenada `atual` ou `congelada`) e da regra de subordinados de ORG-4 sem decisão.
- Open question: ELI-5 (recesso derivado ou gravado), ELI-16 (gestor de topo), ELI-17 (acesso em recesso).
