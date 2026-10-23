---
id: research-template-frontend-fuse
type: research
initiative: initiative-levantamento-regras-dezi
status: rascunho
author: Mary (Business Analyst)
date: 2026-10-13
inputs:
  - /home/eavila/desenv/fuse (código do template, lido em 2026-10-13)
  - docs/spec/spec-regras-negocio-dezi/ (spec de regras de negócio)
next: bmad-architecture
---

# Validação de encaixe: template Fuse × DEZI novo

## Resumo

O template de frontend **Fuse (Angular)** já está escolhido. Este documento valida o encaixe entre o que o template oferece e o que o spec de regras de negócio exige da interface. Ele **não decide** nada: registra evidências e deixa as perguntas abertas para as fases de arquitetura e UX.

**Conclusão:** o Fuse entrega a casca (layout, tema, componentes, exemplos de telas) e cobre bem cadastros, navegação e caixa de mensagens. O núcleo do DEZI não vem pronto: tela de preenchimento, gráficos de resultado e a camada de autenticação e permissão por perfil. Nenhum item encontrado inviabiliza a adoção; os de maior risco são **autenticação por perfil**, **tela de preenchimento** e **pt-BR**.

**Restrição declarada:** o template Fuse está escolhido. A arquitetura parte dele.

## Método e limites

- Leitura estática do código em `/home/eavila/desenv/fuse`. **O template não foi instalado nem executado** (sem `npm install`, `ng serve` ou `ng build`).
- Requisitos de interface vêm do spec (`CAP-1`, `CAP-6`, `CAP-7` e companions). Itens sem evidência no código ou no spec ficam como **a confirmar**.
- Versões citadas são as declaradas em `package.json`; não foi verificado se existem no registro npm nem se o build funciona.

## O que o template é

| Item | Evidência |
|---|---|
| Angular 22.1.1, TypeScript 6.0.3, Node ≥ 24 | `package.json` |
| Angular Material + CDK, Tailwind 4, Transloco 8, ApexCharts (`ng-apexcharts`), FullCalendar, date-fns | `package.json` |
| Formulários com Signal Forms (`@angular/forms/signals`) | 14 arquivos em `src/app` |
| SSR ligado (`outputMode: server`, Express) | `angular.json` |
| Licença Envato Standard | `LICENSE.md` |
| Testes: Vitest configurado, **0 arquivos `.spec.ts`** | `find src -name "*.spec.ts"` |
| Dados: módulos usam dados fictícios locais (`data/*.ts`); `provideHttpClient()` sem interceptor | `src/app/domains/admin/modules/*/data`, `app.config.ts` |
| Estrutura: `core/` (icons, media, local-storage, theming, transloco) e `domains/` (admin, auth, website, maintenance, coming-soon) | `src/app` |
| Módulos de exemplo em `admin/modules/apps`: academy, ai-chat, calendar, contacts, file-manager, help-center, mailbox, notes, orders, scrumboard, tasks; dashboards: project, analytics, finance; extras: error, invoice, notifications, profile, settings | `src/app/domains/admin/modules` |
| Idiomas: `en` e `es`, `defaultLang: 'en'` | `public/i18n`, `app.config.ts` |
| Componentes Material usados: button, icon, input, menu, tooltip, card, form-field, sidenav, select, checkbox, dialog, slide-toggle, list, table, sort, paginator, progress-bar, tabs, expansion, datepicker, timepicker, button-toggle, radio, autocomplete, badge; CDK: overlay, drag-drop, text-field | contagem de imports em `src/app` |
| **Não encontrados:** `MatStepper`, `MatSlider`, `MatSnackBar`, guards de rota, interceptors HTTP | busca em `src/app` |

## Matriz de aderência

Legenda: **Atende** · **Atende com adaptação** · **Não atende** · **A confirmar**.

| # | Necessidade do DEZI (spec) | O que o template tem | Veredito |
|---|---|---|---|
| 1 | Layout administrativo: menu lateral, cabeçalho, tema claro/escuro, responsivo | `admin/layout`, `sidenav`, `scheme-switcher`, `core/media` | **Atende** |
| 2 | Cadastros (CAP-2/3/4): listas, busca, ordenação, paginação, formulários, diálogos | `table`, `sort`, `paginator`, `dialog`, `form-field`, `select`, `datepicker`, `autocomplete` em uso (contacts, orders, tasks) | **Atende com adaptação.** Os componentes existem como exemplos; cadastros hierárquicos (departamento, cargo) e telas de gabarito/pesos precisam ser montados |
| 3 | Preenchimento (CAP-6): formulário longo por competência, escala de níveis, comentário obrigatório por nível, etapas Av1/Av2/Av3, consenso | `radio`, `button-toggle`, `progress-bar`, `tabs`, `expansion` existem. Sem `stepper` nem `slider` | **Atende com adaptação.** A tela central do sistema não existe pronta |
| 4 | Resultados e painéis (CAP-7): nota ponderada, comparação entre perfis, evolução, progresso | `ng-apexcharts` com 3 dashboards de exemplo (project, analytics, finance) | **Atende com adaptação.** Falta confirmar os tipos de gráfico necessários (ver pergunta A6). O cálculo da nota é decisão do negócio (D24, D38), não do template |
| 5 | PDI (CAP-8): estados EM_APROVACAO → APROVADO → EM_ANDAMENTO → FINALIZADO, histórico | Módulos `tasks` e `scrumboard` (drag-drop) como base visual | **Atende com adaptação.** O modelo de estados é do DEZI |
| 6 | Comunicação (CAP-10): caixa de mensagens interna, notificações | Módulos `mailbox` e `notifications` | **Atende** como base de interface |
| 7 | Login por empresa + matrícula (CAP-1, ACE-01) | Tela de login com e-mail + senha (`domains/auth/features/sign-in`) | **Atende com adaptação.** Mudança pequena e obrigatória |
| 8 | Perfis e permissão (CAP-1; 5 perfis: ADMINISTRADOR, FUNCIONARIO, CONSULTOR_RH, GESTOR_RH, GESTOR_AREA) | Sem guard de rota e sem interceptor HTTP. Navegação fixa em `admin/layout/data/navigation.ts`, sem filtro por perfil | **Não atende.** Autenticação, sessão e menu por perfil precisam ser construídos. A permissão efetiva é do servidor (D51, ACE-24); a tela só a reflete |
| 9 | Idioma pt-BR | Só `en` e `es` | **Atende com adaptação.** Transloco suporta; toda a tradução é nossa |
| 10 | Importação de planilhas (administração; ACE-25) | `file-manager` é maquete | **A confirmar.** Depende de decisão de arquitetura |
| 11 | Mobile | Template é web responsivo; o legado tem app Ionic separado (`mobile/`) | **A confirmar.** Pergunta aberta (A3) |
| 12 | Licença | Envato Standard | **A confirmar.** Ver pergunta A4 |
| 13 | Manutenção e versões | Angular 22 e TypeScript 6 de ponta a ponta | **A confirmar.** Ver pergunta A5 |
| 14 | Qualidade e testes | 0 testes no template | **A confirmar.** O que o template garante de qualidade não é verificável por leitura |

## Perguntas abertas para a arquitetura (`bmad-architecture`)

- **A1. Autenticação e sessão.** Como o frontend autentica por empresa + matrícula, guarda a sessão e trata expiração (D41), bloqueio (D42) e primeiro acesso/troca de senha? Onde ficam guard de rota e interceptor?
- **A2. Permissão por perfil na tela.** Como o frontend recebe os papéis e filtra menu e botões (matriz ACE-25)? O servidor impõe a mesma matriz (D51), com teste negativo por célula.
- **A3. Mobile: nativo ou web responsivo?** **Pergunta aberta, sem posição do responsável.** O legado tem app Ionic separado, com regras próprias de gravação (só Av1 e Av2) que o spec manda unificar (D30). A resposta muda arquitetura, escopo e esforço.
- **A4. Licença.** A licença Envato Standard cobre este uso (sistema multiempresa, possivelmente cobrado dos clientes)? Se não, é preciso licença estendida. Validar antes de publicar.
- **A5. Risco de versão.** O projeto fica preso a Angular 22 e TypeScript 6 e ao ritmo de atualização do fornecedor do template. Qual a política de atualização e o plano se o template ficar para trás?
- **A6. Gráficos de resultado.** Quais tipos o novo sistema precisa (radar por competência, comparação entre agentes, evolução entre campanhas) e o ApexCharts do template os cobre? Depende da fórmula de nota ainda a validar com o negócio (D24, D38).
- **A7. SSR.** O template liga SSR por padrão. O DEZI precisa dele (área autenticada, sem SEO), ou ele pode ser desligado para simplificar a implantação?
- **A8. Importação de planilhas.** Fica no frontend, no backend ou em módulo à parte (hoje é `support/`)? Como a tela se integra?
- **A9. Dados de exemplo e código morto.** O que do template (módulos de demonstração, dados fictícios) é removido e quando, para não carregar peso e superfície de ataque.
- **A10. Integração com a API.** Contrato (REST ou outro), geração de cliente, tratamento de erros e isolamento por empresa vindo da sessão (D51).

## Perguntas abertas para a UX (`bmad-ux`)

- **U1. Tela de preenchimento.** Como apresentar competências, perguntas, escala de níveis (incluindo "Não posso avaliar" e escala só de comentário), comentário obrigatório por nível e o progresso por etapa, sem `stepper` pronto?
- **U2. Consenso (Av3).** Como representar o aceite de cada parte com a própria senha (D31, D53) e a liberação quando Av1 e Av2 estão finalizadas (D30)?
- **U3. Painéis de resultado.** O que cada perfil vê: avaliado e gestor com agentes sem identificação (D52), GESTOR_AREA com toda a sua árvore (D33), RH com a empresa.
- **U4. PDI.** Fluxo de aprovação e execução (D9) sobre a base de `tasks`/`scrumboard`, ou desenho próprio.
- **U5. Acompanhamento.** Progresso por avaliador e pendências, com fonte única de progresso.
- **U6. Idioma e terminologia.** Glossário do spec (`glossario.md`) como base da tradução pt-BR.

## Próximos passos sugeridos

1. Usar este documento como entrada do `bmad-architecture`, tratando A1–A10 como requisitos de entrada.
2. Levar U1–U6 ao `bmad-ux`, em especial a tela de preenchimento.
3. Depois da arquitetura, rodar uma discussão em grupo (`bmad-party-mode`) com Winston, Sally e Mary para validar o encaixe entre a arquitetura proposta e o template.
4. Confirmar A3 (mobile) com o responsável antes de fechar o escopo.

## Fora do escopo deste documento

Escolher outro template, avaliar qualidade de código do Fuse, executar ou construir o template, e decidir as perguntas acima. Nenhuma regra de negócio foi alterada.
