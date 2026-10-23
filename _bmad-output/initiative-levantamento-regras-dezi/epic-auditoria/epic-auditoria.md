---
type: epic
title: "Trilha de auditoria imutável"
parent: initiative-levantamento-regras-dezi
covers: []
after: []
assignee: ""
risk: medium
---

# Trilha de auditoria imutável

## Description

Módulo `auditoria`: tabela só de inserção, particionada por mês, gravada na mesma transação da ação pela porta `RegistroDeAuditoria`, mais o canal de auditoria de negado.

## Outcome

Toda ação privilegiada dos módulos seguintes deixa rastro imutável sem derrubar o fluxo de login.

## Requirements

- AU1: Porta `RegistroDeAuditoria` na mesma transação; falha de gravação aborta a ação (AD-8, AD-21 canal 1).
- AU2: Tabela imutável: papel da aplicação só com `INSERT`/`SELECT`, gatilho recusa alteração, dono `dono_auditoria` (AD-8, AD-29).
- AU3: Canal 2 (`AuditoriaDeNegado`) em transação própria, com reserva de conexões (AD-21).
- AU4: Partições mensais criadas à frente, com alerta de falta (AD-29).
- AU5: Leitura da trilha só pela porta `AutorizarLeitura`, declarada por `auditoria` e implementada por `acesso` (AD-4).

## Done when

1. Ação auditada que lança erro de negócio ainda deixa registro de negado.
2. UPDATE, DELETE e TRUNCATE na trilha falham para `app_rw`; o teste de privilégios passa no CI.
3. Sem partição futura o alerta dispara com mais de 2 meses de antecedência e a ação auditada aborta.
4. Leitura da trilha só pela porta `AutorizarLeitura`.

## Boundaries

Módulo `auditoria` e suas portas. Os motivos de lista fechada pertencem a cada módulo emissor. Não é o hash encadeado (Adiado).

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-8, AD-21, AD-29
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-1 (success: mudança de perfil gera auditoria)

## Notes

- Parked: `expurgar_particao_auditoria` com prazo embutido; retenção da auditoria é decisão do negócio em aberto (AD-8, AD-21).
- Parked: anonimização de IP e matrícula por HMAC depende do cofre e vai ao épico lgpd-titular.
- Decision: AU5 acrescentado a partir do Done when 4, que não tinha requisito (usuário, 2026-10-22).
- Decision: 6 histórias; tracer pela 1; `plan_checkpoint` nas histórias 1, 2 e 4; closing story "Refactor sweep"; a 2 depende da 1.11 porque o Done when 2 exige o teste de privilégios no CI; a 4 depende da 3 porque as duas alteram o DDL de `auditoria`; sem suíte e2e de fechamento (2026-10-22).
- Decision: `expurgar_particao_auditoria` e a anonimização de IP e matrícula por HMAC ficam fora das histórias; o registro do canal 2 não grava IP nem matrícula; `app_manutencao` só recebe `EXECUTE` de `criar_particao_auditoria`.
- Open question: o job de partição mora em `infra` (AD-29) ou em `auditoria` registrando-se na API de jobs (AD-28)? A 1.5 aceita job global sem item por empresa?
- Open question: mecanismo de métrica e alerta (a fundação entrega só logs); limiar do alerta lido como "cobertura à frente menor que 3 meses" (AD-29), a confirmar.
- Open question: quem expõe a consulta da trilha por endpoint e com quais filtros; a implementação real de `AutorizarLeitura` fica no épico acesso.
