---
type: epic
title: "Comunicação e e-mail"
parent: initiative-levantamento-regras-dezi
covers: [CAP-10]
after: []
assignee: ""
risk: medium
---

# Comunicação e e-mail

## Description

Outbox transacional, envio SMTP em três tempos, caixa de mensagens interna e consumo de eventos de domínio.

## Outcome

E-mail e mensagem interna saem de forma confiável, com estado que reflete a entrega e sem segredo persistido.

## Requirements

- CAP-10: Estado do e-mail, outbox pós-commit, sem senha em texto, parâmetros respeitados (spec CAP-10, success).

## Done when

1. `ENVIADO` só após aceite do SMTP; falha aparece ao RH e é reenviada com backoff.
2. Nenhuma coluna `comunicacao_*` contém token; o link de senha é emitido no envio.
3. O consumidor é idempotente por `evento_id` e o evento só sai após o commit.
4. Welcome com link de senha (D56) chega ao funcionário criado.

## Boundaries

Módulo `comunicacao`. Os tipos de evento pertencem aos módulos emissores; as histórias de aviso de liberação e outros ficam no épico do emissor.

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-10; docs/spec/spec-regras-negocio-dezi/comunicacao.md
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-13, AD-21, AD-22, AD-27, AD-28

## Notes

- Open question: provedor SMTP e cofre dependem do provedor de nuvem.
