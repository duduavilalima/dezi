---
type: epic
title: "Fundação técnica do DEZI novo"
parent: initiative-levantamento-regras-dezi
covers: []
after: []
assignee: ""
risk: high
---

# Fundação técnica do DEZI novo

## Description

Base sobre a qual os módulos de negócio são construídos: repositórios, build, CI que executa as regras da espinha, kernel `infra`, isolamento por empresa com RLS e papéis de banco, contrato OpenAPI e frontend Fuse limpo.

## Outcome

O time constrói qualquer módulo de negócio sobre uma base em que fronteira de módulo, isolamento por empresa e contrato quebram o build quando violados; o sinal é o CI vermelho nesses casos.

## Requirements

- F1: Backend Spring Boot modular com um pacote `api` e um `internal` por módulo; o build falha em aresta fora do grafo ou ciclo (AD-1, AD-19, AD-27).
- F2: Toda tabela de negócio tem `empresa_id`, RLS com `FORCE` e variável `app.empresa_id` por transação; papéis `app_rw`, `app_descoberta`, `app_manutencao`, `plataforma_*`, `migracao` (AD-2, AD-3, AD-20, AD-29).
- F3: Kernel `infra` com fila, eventos publicados, consumo idempotente, ShedLock, chaves de idempotência, relógio e prazo (AD-10, AD-14, AD-15, AD-28).
- F4: Contrato OpenAPI em `api-contract` como fonte; cliente Angular gerado; erros em Problem Details (AD-15).
- F5: Frontend Fuse limpo, com SSR, pt-BR, guard, interceptor e CSRF, sem módulos de exemplo (AD-6, AD-16).
- F6: Pipeline de CI com build, arquitetura, isolamento por empresa contra Postgres real, privilégios de banco, contrato e SCA; desenvolvimento local em Docker Compose (AD-18).

## Done when

1. O CI reprova uma aresta de módulo fora do grafo, uma escrita em tabela de outro módulo e uma tabela com `empresa_id` sem política RLS.
2. Um teste contra Postgres real prova que a conexão reutilizada não carrega empresa residual e que dois tenants na fila afetam só o tenant correto.
3. Um teste de privilégios prova que nenhum papel de aplicação altera, trunca nem desabilita gatilho da trilha.
4. O Fuse limpo sobe com SSR e chama um endpoint de `/api` pelo cliente gerado, sob o mesmo domínio e sem CORS.
5. Um desenvolvedor novo sobe backend, frontend e Postgres com Docker Compose seguindo o README.

## Boundaries

Repositórios, build, CI, kernel `infra`, papéis de banco e base do frontend. Não é regra de negócio de nenhum módulo, nem deploy em nuvem (provedor em aberto). Tracer: tela do Fuse chama um endpoint de `/api` que lê uma tabela com RLS sob `paraEmpresa`.

## References

- parent — _bmad-output/initiative-levantamento-regras-dezi/initiative-levantamento-regras-dezi.md, Requirements
- architecture — _bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md, AD-1, AD-2, AD-3, AD-14, AD-15, AD-16, AD-18, AD-19, AD-20, AD-27, AD-28, AD-29, seções Stack e Semente Estrutural
- spec — docs/spec/spec-regras-negocio-dezi/spec-regras-negocio-dezi.md, seção Constraints (isolamento por empresa, permissão no servidor)

## Notes

- Open question: provedor de nuvem, suporte a RLS e RPO/RTO (AD-18, AD-3) estão em aberto; este épico entrega CI e Docker Compose local, sem deploy em nuvem.
- Open question: licença Envato do Fuse (A4); bloqueia usar o template em produção, não o desenvolvimento.
- Assumption: AD-14 (ShedLock) é [ASSUMPTION] na espinha; histórias que dependem dele levam `unknown`.
- Decision: 12 histórias aprovadas; tracer pela trilha 1, 4 e 10; `plan_checkpoint` nas histórias 4 e 5; sem alteração de descrições (usuário, 2026-10-22).
- Parked: deploy em nuvem, homologação, backup e restauração (provedor e RPO/RTO em aberto).
- Decision: sem suíte e2e de fechamento neste épico; a verificação da história 10 é teste automatizado rodado no CI da história 11, e a suíte e2e fica para o épico acesso (usuário, 2026-10-22).
