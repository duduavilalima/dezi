# Pontos a confirmar (consolidado)

Regra sem evidência suficiente no código **e** no banco não é fato. Cada item abaixo precisa de decisão do negócio ou de leitura de fonte inacessível (biblioteca `commons`, ETL e relatórios do Pentaho, propriedades de ambiente). Fonte: seção "pontos a confirmar" de cada documento; ID `módulo-n` aponta a ordem original. Itens marcados **★** são os que mais bloqueiam decisões do sistema novo.

## 01 Acesso e perfis
- ACE-1 ✔ Decidido (D3), imposto no servidor. Original: quais perfis concluem/inativam/reativam avaliação e se o servidor impõe (transição genérica em `commons`). Ver D3.
- ACE-2 ✔ Decidido (D26): gatilho, estados, dados, efeitos e reversão. Original: `CANCELADA` no novo, gatilho e consequências (D2). Verificado em código (2026-10-10): o legado tem só esqueleto — `doAfterTransition` grava o status com `TODO` e `FIXME` (`AvaliacaoServiceImpl:274-279`), `doBeforeUpdate` recusa editar avaliação cancelada (`validation.canceled`, `:374`) e a tela esconde colunas da análise; nada trata participantes, preenchimentos ou mensagens. Banco: 0 avaliações canceladas. Continua dependendo de decisão do negócio.
- ACE-3 Efeito da transição genérica INATIVA→LIBERADA sobre as datas limite. Banco (2026-10-10): inconclusivo; não há como distinguir avaliação reativada (1 LIBERADA tem limites perto da data de atualização, a de id 188, caso ambíguo) e as 58 LIBERADA já têm todos os limites vencidos. Segue em aberto (depende de `commons`).
- ACE-4 Validade/algoritmo do token; matrícula inexistente no login; CORS irrestrito com credenciais.
- ACE-5 Bloqueio por IP atrás de proxy; volumes de `log_auth`; por que `log_auth`/`log_entity` não existem na instância.
- ACE-6 ★ Verificado em código (2026-10-09): `FuncionarioServiceImpl.validar` só bloqueia **editar** quem já é ADMINISTRADOR; nada impede **definir** o perfil ADMINISTRADOR em outro funcionário (ou em si mesmo), e `FuncionarioResource` não tem `PreAuthorize`. Escalada pela API é possível no legado, salvo filtro em `commons` (inacessível). ✔ Decidido (D10): só ADMINISTRADOR atribui ADMINISTRADOR.
- ACE-7 Alcance do GESTOR_AREA (um nível × todos os descendentes) e filtro ausente no mobile.
- ACE-8 Quem aciona boas-vindas em massa.
- ACE-9 Resíduos de gestor com perfil FUNCIONARIO.
- ACE-10 Redirecionamento de quem já está logado é caminho usado?
- ACE-11 CONSULTOR_RH com mais de uma empresa?
- ACE-12 Resto do filtro de campanhas para perfis ≠ ADM/GESTOR_RH.

## 02 Cadastro organizacional
- ORG-1 ★ Verificado em código (2026-10-09): `doBeforeUpdate` (edição) e `doAfterSave` (criação) chamam `atualizaGestorUnidadeDepartamento`, que **sempre apaga** a linha do par (unidade, departamento) e só recria se o salvo é GESTOR_AREA/GESTOR_RH. Salvar funcionário comum apaga o gestor do par. Incerteza restante: se `entity` na edição é o estado antes da alteração (`commons`). ✔ Decidido (D11): gestor é dado próprio do departamento; falta só o conjunto de subordinados (ORG-4).
- ORG-2 Origem dos resíduos em `unidade_departamento`.
- ORG-3 Quem lê `departamento_gestor` (BI?) e se depende dos descendentes ativos.
- ORG-4 ✔ Decidido (D20): toda a árvore. Original: conjunto desejado de subordinados (um nível, gestor do filho em vez da equipe, recesso, nome fixo "Administrador").
- ORG-5 Cargo nasce INATIVA por desenho? Cópia de cargo em uso?
- ORG-6 Papel de negócio do departamento do cargo (96 cargos com funcionários em outro depto).
- ORG-7 Recesso vencido sem retorno automático: procedimento do RH.
- ORG-8 18 funcionários INATIVA em atividade; 1 desligado sem data.
- ORG-9 Duplicados já gravados (2 e-mails, 3 nomes de unidade) e impacto na edição.
- ORG-10 Cadastro de empresa (quem cria), `cbo`, `versao_bi`, consulta entre contratantes.
- ORG-11 Papel de `ponto_focal_rh` e `preenche_formacao_e_idiomas`.
- ORG-12 Apontamento em nome de outro avaliador; foto sem escopo.
- ORG-13 Funcionários sem unidade (10 ADMINISTRADOR) e erro ao editar.
- ORG-14 Escala 0–10 do apontamento × graduação.

## 03 Modelo de competências
- MOD-1 Mais de dois níveis de tipo; competência em tipo com subtipos.
- MOD-2 Inativar tipo deve inativar competências/subtipos? Reativar deve propagar?
- MOD-3 Troca de tipo de competência já associada; 9 vínculos sem cargo×tipo.
- MOD-4 Igualdade de objeto na gravação de hints; 84 hints de escala antiga no home office.
- MOD-5 ✔ Origem verificada em código (2026-10-09): o importador `support/` (`AssociacoesCargoSheet`) grava `cargo_tipo_competencia.peso` da coluna 5 da planilha, truncado para inteiro (`UtilsRow.converterDoubleCell`); valores como 50, 33, 67, 25 são pesos digitados na planilha. A tela do legado não edita esse peso. ✔ Decidido (D14): peso relativo, com tela.
- MOD-6 Associação sem pergunta aplicável (15 casos).
- MOD-7 Gabaritos/vínculos que sobrevivem à regra de exclusão (3, 35 e 40 casos).
- MOD-8 Gabaritos com pergunta de outra competência (195): erro de carga ou reuso intencional?
- MOD-9 ✔ Resolvido (D6): dimensões não entram no novo.
- MOD-10 ✔ Decidido (D23): versão congelada. Pesos duplicados: ver D15.
- MOD-11 NPE ao ativar cargo×competência sem cargo×tipo.
- MOD-12 Quem pode editar o catálogo (servidor não impõe perfil).

## 04 Graduação e conceitos
- GRA-1 Semântica de `nao_graduar`. GRA-2 `doAfterSave` na edição? GRA-3 Posição de nível novo antes da renumeração.
- GRA-4 Escalas decrescentes (15) e cor por ordem: intencional? GRA-5 Pontos repetidos / escala "0 a 10" com 10 em todos.
- GRA-6 ✔ Decidido (D21): tipo explícito. Original: escala de comentário (`ponto=0`), convenção ou tipo de pergunta? GRA-7 A cor gravada é usada pelo BI?
- GRA-8 Ordem web (ordem) × mobile (ponto) intencional? GRA-9 Inativar escala impede uso ou só esconde?
- GRA-10 Escopo por empresa/perfil de nível e conceito. GRA-11 `FIXME` de múltiplos snapshots por competência.
- GRA-12 23 respostas Av2 com vínculo divergente. GRA-13 Home office com nível sem `ordem`.
- GRA-14 ✔ Resolvido (D8): conceito não entra. GRA-15 Texto da confirmação `DIALOG.MSG_EDIT_MG`.

## 05a/05b Avaliação
- EST-1 ✔ Decidido (D3 e D26): quem dispara CANCELADA/CONCLUIDA. EST-2 Falha de liberação (D4); banco (2026-10-10): 0 avaliações e 0 campanhas com a flag de liberação presa nesta instância. EST-3 ✔ Verificado em código (2026-10-10): `emLiberacao` só é gravado e exibido (`campanha.view.html`, rótulo "em liberação"); nenhum Java o lê e o servidor não bloqueia ação com a flag ligada.
- EST-4 INATIVA→LIBERADA e `limiteAv4` não ajustado; banco (2026-10-10): `limite_av4` é nulo em todas as INATIVA que têm limites forçados, então a omissão não tem efeito observável na instância. EST-5 ✔ Sem objeto (D6). EST-6 Autoavaliação desligada sem perguntas não aborta.
- EST-7 Atalho de liberação só com autoavaliação. EST-8 (resolvido em 07) precedência AND/OR. EST-9 Significado Av1..Av4 (respondido em 06). EST-10 (validado no banco).
- CRI-1 Av1..4 e sobrescrita de limites. CRI-2 ★ Semântica do peso dos agentes (resolvido em 07: relativo). CRI-3 Laço de remoção de agentes.
- CRI-4 Avaliado avaliando a si mesmo como PARES; uso do revisado e revisor. CRI-5 ✔ Decidido (D12): bloquear.
- CRI-6 Planilha de pares: descartes e homônimos. CRI-7 Gestor em recesso sem subir de nível. CRI-8 Cópia: criador/data, dimensões, e-mail padrão.
- CRI-9 Fluxo home office e configuração. CRI-10 ✔ Decidido (D13): por data e por estado. CRI-11 Assimetria de padrões criar×editar; 8 `autoavaliacao` nulos.
- CRI-12 Participantes LIBERADA/CONCLUIDA sem snapshot de cargo/depto (11 e 2).

## 06 Preenchimento
- PRE-1 `consenso_iniciado` chega a verdadeiro (328). PRE-2 Web × mobile liberam consenso diferente. PRE-3 Mobile sem rastro (`tipo_client`).
- PRE-4 ✔ Decidido (D16): servidor valida. PRE-5 ✔ Decidido (D17): servidor recusa após o prazo.
- PRE-6 Login do outro participante no consenso só no front. PRE-7 Copia-e-cola em `doBeforeUpdate`; início de Av2 sem resposta.
- PRE-8 `preenchida_avN` nulo × falso. PRE-9 27 participantes sem linhas. PRE-10 ✔ Resolvido (D7): revisor/Av4 não entra.
- PRE-11 1.181 respostas sem vínculo congelado. PRE-12 Quem libera consenso/reabre etapas e quem vê acompanhamento.
- PRE-13 3 Av1 de subordinados; 3 AUTOAVALIACAO com `limite_av3`. PRE-14 `limite_avN` é date ou timestamp.

## 07 Resultados
- RES-1 ✔ Resolvido (D24): Pentaho descartado; o novo define cálculo e relatórios. Original: onde ficam ETL, `.wcdf`, periodicidade e frescor de `bi_media_*`. RES-2 Fórmula da média por agente (5%) e 0,02% dos demais níveis.
- RES-3 ✔ Decidido (D15): congelados na liberação, fonte única. RES-4 Duplicatas dobram resposta nas médias.
- RES-5 Consenso NPA anula a avaliação. RES-6 Respostas sem vínculo fora do BI. RES-7 Escalas misturadas; peso 0/divisão por zero.
- RES-8 Filtro de campanha ativa só na view real. RES-9 Escopo por nome, chave sem expiração, escopo na URL (v1).
- RES-10 Quem acessa resultados/histórico/`/pentaho/login`/`ajustarpesos`. RES-11 Outro cliente dos endpoints de gráfico?
- RES-12 Percentuais do painel (autoavaliação, `Integer ==`, lista vazia). RES-13 Qual progresso a tela usa.
- RES-14 47 AUTOAVALIACAO com `limite_av2`.

## 08 PDI e feedforward
- PDI-1 ✔ Decidido (D22): funcionário propõe a própria ação. Original: quem cria ação de PDI? PDI-2 Transições só no servidor (dono do corpo, sem máquina de estados).
- PDI-3 ✔ Decidido (D22): gestor direto aprova, ninguém aprova o próprio, imposto no servidor. PDI-4 Vínculo com participante arbitrário e só avaliação LIBERADA.
- PDI-5 Uso de `revisado`. PDI-6 Exclusão sem checagem. PDI-7 ✔ Resolvido (D9): PDI entra, com ciclo de vida revisto.
- PDI-8 Competências fora do cargo (20%) e erro ao trocar na edição. PDI-9 Anotações só LIBERADA; sem ligação texto→ação.
- PDI-10 Escopo do gestor de área e listagens por id. PDI-11 `colaboradorespdi` sem uso; relatórios com colunas trocadas.
- PDI-12 `POST /feedsRecebidos`. PDI-13 `empresa.feedforward` sem efeito; feedforward × registro crítico; escala 0–10.

## 09 Comunicação
- COM-1 ✔ Decidido (D18): ENVIADO = aceito pelo SMTP. COM-2 249 PENDENTE nunca enviados; 0 ERRO.
- COM-3 "Notificar via e-mail" por destinatário; parâmetro `false` desliga? COM-4 Acompanhamento respeita parâmetro e valida e-mail?
- COM-5 `notificacao.email.resposta.mensagem` e resposta fora de avaliação. COM-6 E-mail de troca de senha deve existir?
- COM-7 Falha de validação no meio do lote. COM-8 Assunto da conclusão pela empresa do logado; sem GESTOR_RH.
- COM-9 RH avisado do erro de liberação? COM-10 Forma e protocolo do link. COM-11 ✔ Decidido (D19): link de definição de senha.
- COM-12 Valores fora do repositório (`dezi.queue.*`, `homeoffice.*`, `email.contato.suporte`). COM-13 `doBeforeSave` redefine senha a cada gravação?
- COM-14 Necessidade do relatório de bug e de `Mensagem`/`TipoMensagem`.

## Itens de infraestrutura que travam a leitura
**Posição do responsável (2026-10-10):** a `commons` está definitivamente inacessível (D25) e o Pentaho está descartado (D24); nenhum dos dois será lido. O que depende deles vira decisão do sistema novo, não pendência do legado. Lista original:

Biblioteca `br.com.fasolti.commons` (token, `BaseAvdService`, transição genérica, escopo padrão, transação do `save`); ETL/relatórios do Pentaho; propriedades de ambiente; `support/` (importador, explicaria dados fora da regra); `mobile/` (lido só por busca); textos i18n e 5 de 8 modelos HTML de e-mail.
