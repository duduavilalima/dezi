---
type: epic
title: "Acesso, sessão e perfis"
parent: initiative-levantamento-regras-dezi
covers: [CAP-1]
after: []
assignee: ""
risk: high
---

# Acesso, sessão e perfis

## Description

Autenticação por empresa e matrícula, sessão no servidor, bloqueio, credenciais, matriz de autorização única e administrador de plataforma com TOTP.

## Outcome

Usuário só vê e altera o que seu perfil e empresa permitem, imposto no servidor; o sinal é o teste negativo por célula da matriz passando no CI.

## Requirements

- CAP-1: Autenticação, bloqueio, senha, perfis e trilha de mudanças sensíveis (spec CAP-1, success).

## Done when

1. Cada célula da matriz perfil × recurso × ação tem teste negativo gerado e passando.
2. Teste de password spraying e de rajada paralela passa; 5 falhas em 10 min bloqueiam o par IP+matrícula.
3. Troca de senha, perfil ou situação apaga as sessões do usuário na hora.
4. O endpoint de plataforma exige TOTP e reautenticação recente.
5. Nenhuma senha ou token aparece em log, e-mail ou coluna.

## Boundaries

Módulo `acesso`: política, credencial, sessão, matriz e plataforma. Não é cadastro de funcionário (épico cadastro) nem envio de e-mail (épico comunicacao).

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, CAP-1; docs/spec/spec-regras-negocio-dezi/acesso-perfis.md
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-4, AD-5, AD-6, AD-7, AD-22, AD-24

## Notes

- Parked: valores do atraso progressivo e do teto do login (AD-7); o mecanismo entra, os valores esperam o negócio.
- Open question: tempos de sessão (30 min, 10 h), fonte da lista de senhas vazadas e ELI-12 (ordem entre perfis).
- Unknown: `ResolucaoDeEmpresa` e `EscopoOrganizacional` são implementados por `cadastro`; até lá as histórias usam stubs de teste.
- Decision: este épico recebe a suíte e2e (login ponta a ponta), estendendo o teste automatizado da história 10 do épico fundação (usuário, 2026-10-22).
