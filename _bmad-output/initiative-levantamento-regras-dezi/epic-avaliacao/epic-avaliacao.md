---
type: epic
title: "Ciclo de vida da avaliação"
parent: initiative-levantamento-regras-dezi
covers: [CAP-5]
after: []
assignee: ""
risk: high
---

# Ciclo de vida da avaliação

## Description

Criar, copiar, liberar com congelamento, concluir, inativar, reativar e cancelar avaliações, com agentes, participantes, pendências e importação de pares.

## Outcome

O RH conduz a avaliação do rascunho à conclusão, com liberação idempotente e estados impostos no servidor.

## Requirements

- CAP-5: Estados, liberação com congelamento, cancelamento e situação derivada (spec CAP-5, success).

## Done when

1. Liberação aborta sem participante, com pendência ou cargo sem pergunta aplicável; falha deixa ERRO recuperável e idempotente.
2. Concluir, inativar, reativar e cancelar só por ADMINISTRADOR e GESTOR_RH, recusado no servidor aos demais.
3. Edição do catálogo depois da liberação não altera a avaliação.
4. Testes de concorrência do AD-19 (liberar × prorrogar, remover × gravar) passam.
5. Planilha de pares reporta nomes não encontrados por linha.

## Boundaries

Módulo `avaliacao`: avaliação, participante, snapshot e estados. A resposta do avaliado é do épico preenchimento.

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-5; avaliacao.md
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-9, AD-10, AD-17, AD-19, AD-23, AD-25

## Notes

- Assumption: AD-9 e AD-10 são [ASSUMPTION] na espinha; as histórias de snapshot e de máquina de estados levam `unknown` e `plan_checkpoint`.
- Open question: ELI-3, ELI-7, ELI-9, ELI-10, ELI-20 e ELI-1 (coordenada congelada).
