---
type: initiative
title: "DEZI novo: construção a partir das regras levantadas"
parent: none
covers: [CAP-1, CAP-2, CAP-3, CAP-4, CAP-5, CAP-6, CAP-7, CAP-8, CAP-9, CAP-10]
after: []
assignee: ""
risk: high
---

# DEZI novo: construção a partir das regras levantadas

## Description

O DEZI legado de avaliações de desempenho e pesquisas de clima não será reescrito. As regras extraídas dele (spec CAP-1 a CAP-10, decisões D1-D60) e a espinha de arquitetura (AD-1 a AD-30) definem o sistema novo, desenvolvido do zero. Esta iniciativa o constrói, módulo a módulo. O spec e a espinha são donos das capacidades, restrições e decisões; os tickets as citam por CAP-N e AD-n.

## Outcome

As empresas clientes conduzem avaliações de desempenho do cadastro ao resultado no sistema novo, com as regras decididas pelo negócio impostas no servidor; o sinal é cada `success` do spec coberto por teste automatizado.

## Requirements

- CAP-1 a CAP-10: as capacidades do spec, com seus `success` (spec, seção Capabilities). Cada épico as detalha em seus Requirements.
- Restrições transversais (isolamento por empresa, permissão no servidor, congelamento na liberação) vêm do spec (Constraints) e da espinha, e são entregues pelo épico fundacao e conferidas em cada módulo.

## Done when

1. Cada `success` de CAP-1 a CAP-10 tem teste automatizado passando no CI.
2. O CI reprova violação de fronteira de módulo, de isolamento por empresa, de matriz de autorização e de contrato OpenAPI.
3. Uma avaliação percorre criação, liberação, preenchimento de Av1 a Av3, conclusão e resultado, sem passo manual no banco.
4. Nenhuma tabela de negócio fica sem RLS e nenhum endpoint recebe a empresa como parâmetro, exceto o login.
5. Cada decisão do negócio em aberto está decidida ou registrada com responsável antes da produção.

## Boundaries

Backend Spring (monólito modular), frontend Fuse e contrato OpenAPI. Não inclui migração de dados do legado (D59), Pentaho (D24), dimensões, Av4 e conceito (D6-D8), nem mobile enquanto A3 estiver sem decisão. Tracer entre épicos: um funcionário é cadastrado, entra, é autorizado pela matriz e a ação deixa auditoria.

- Touch point: SMTP e cofre de segredos — configuração; owner: epic-fundacao
- Touch point: `infra` (fila, eventos, ShedLock) — kernel usado por todos; owner: epic-fundacao
- Touch point: frontend Fuse — telas entregues junto a cada épico; owner: o épico do módulo

## References

- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, seção Capabilities
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-1 a AD-30
- constraint — o spec, seção Constraints
- research — _bmad-output/initiative-levantamento-regras-dezi/research-template-frontend-fuse/research-template-frontend-fuse.md

## Notes

- Decision: um épico por módulo da espinha; fundação e auditoria separadas; lgpd-titular no fim; telas junto a cada épico (usuário, 2026-10-22).
- Decision: o título da iniciativa mudou; o nome da pasta fica por causa de `active_initiative` (usuário, 2026-10-22).
- Parked: mobile (A3), sem decisão do responsável.
- Open question: licença Envato (A4); bloqueia o Fuse em produção.
- Open question: RPO/RTO, provedor de nuvem e suporte a RLS; ficam fora os tickets de deploy em nuvem.
- Open question: limiar de anonimato (AD-12), retenção da auditoria, valores do atraso do login (AD-7), ELI-1 e consensos por avaliado (AD-26); os tickets que dependem deles ficam `Parked` nos épicos.
- Assumption: AD-9, AD-10 e AD-14 são [ASSUMPTION] na espinha; as histórias que dependem deles levam `unknown`.
