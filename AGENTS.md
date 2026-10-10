<!-- bmad:context -->
<!-- Verified 2026-09-28 against b22d993. Managed by bmad-project-context; edits inside this block are replaced on refresh. Keep anything you want preserved outside the markers. -->

## desenv (DEZI)

Sistema legado de avaliações de desempenho e pesquisas de clima, importado de um sistema antigo. Não será reescrito: serve de base para extrair as regras de negócio de um sistema novo, desenvolvido do zero. Módulos: `web/` (backend Java/Maven), `webapp/` (frontend), `mobile/` (Ionic), `queue/` (consumidor RabbitMQ que envia e-mail), `support/` (importador de planilhas xlsx), `deploy/` (Docker Compose). Roteiro do levantamento: `docs/roteiro-bmad-levantamento-regras-negocio.md`.

## Policy

- Código e banco de dados (instância real em Postgres) são as únicas fontes de verdade; não há documentação externa. Regra sem evidência nos dois vai como "a confirmar", nunca como fato inferido.
- Não refatore nem corrija o código legado; comportamento suspeito (bug ou regra?) é registrado como "a confirmar" no documento da regra.

## Where things are

- Esquema versionado: `web/domain/src/main/resources/db/migration/`; scripts de restore em `deploy/servers/dezi/db-scripts/`. O esquema real é o da instância Postgres, e as migrações podem divergir dele.
- Regras extraídas: `docs/regras-negocio/<modulo>.md`, um módulo por vez.

## Running and verifying

- Padrão de execução: JDK 11 em todos os módulos Java; `web/pom.xml` ainda declara Java 9.
- A aplicação ainda não sobe localmente; extraia regras lendo código e migrações, sem depender de execução. `web/` e `webapp/` não têm testes.
<!-- /bmad:context -->
