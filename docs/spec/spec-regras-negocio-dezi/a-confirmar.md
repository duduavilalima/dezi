# Pontos a confirmar (consolidado)

Regra sem evidência suficiente no código **e** no banco não é fato. Cada item abaixo precisa de decisão do negócio ou de leitura de fonte inacessível (biblioteca `commons`, ETL e relatórios do Pentaho, propriedades de ambiente). Fonte: seção "pontos a confirmar" de cada documento; ID `módulo-n` aponta a ordem original. Itens marcados **★** são os que mais bloqueiam decisões do sistema novo.

## 01 Acesso e perfis
- ACE-1 ✔ Decidido (D3), imposto no servidor. Original: quais perfis concluem/inativam/reativam avaliação e se o servidor impõe (transição genérica em `commons`). Ver D3.
- ACE-2 ✔ Decidido (D26): gatilho, estados, dados, efeitos e reversão. Original: `CANCELADA` no novo, gatilho e consequências (D2). Verificado em código (2026-10-10): o legado tem só esqueleto — `doAfterTransition` grava o status com `TODO` e `FIXME` (`AvaliacaoServiceImpl:274-279`), `doBeforeUpdate` recusa editar avaliação cancelada (`validation.canceled`, `:374`) e a tela esconde colunas da análise; nada trata participantes, preenchimentos ou mensagens. Banco: 0 avaliações canceladas. Continua dependendo de decisão do negócio.
- ACE-3 Efeito da transição genérica INATIVA→LIBERADA sobre as datas limite. Banco (2026-10-10): inconclusivo; não há como distinguir avaliação reativada (1 LIBERADA tem limites perto da data de atualização, a de id 188, caso ambíguo) e as 58 LIBERADA já têm todos os limites vencidos. Segue em aberto (depende de `commons`).
- ACE-4 ✔ Decidido (D41, D42): sessão com expiração; resposta única e limite também para matrícula inexistente. CORS irrestrito com credenciais: implantação, vai ao `bmad-architecture`.
- ACE-5 Bloqueio por IP atrás de proxy; volumes de `log_auth`; por que `log_auth`/`log_entity` não existem na instância.
- ACE-6 ★ Verificado em código (2026-10-09): `FuncionarioServiceImpl.validar` só bloqueia **editar** quem já é ADMINISTRADOR; nada impede **definir** o perfil ADMINISTRADOR em outro funcionário (ou em si mesmo), e `FuncionarioResource` não tem `PreAuthorize`. Escalada pela API é possível no legado, salvo filtro em `commons` (inacessível). ✔ Decidido (D10): só ADMINISTRADOR atribui ADMINISTRADOR.
- ACE-7 ✔ Decidido (D33): toda a árvore, imposto no servidor e no mobile.
- ACE-8 Quem aciona boas-vindas em massa.
- ACE-9 Resíduos de gestor com perfil FUNCIONARIO.
- ACE-10 Redirecionamento de quem já está logado é caminho usado?
- ACE-11 CONSULTOR_RH com mais de uma empresa?
- ACE-12 ✔ Decidido (D43): campanhas por escopo do perfil.

## 02 Cadastro organizacional
- ORG-1 ★ Verificado em código (2026-10-09): `doBeforeUpdate` (edição) e `doAfterSave` (criação) chamam `atualizaGestorUnidadeDepartamento`, que **sempre apaga** a linha do par (unidade, departamento) e só recria se o salvo é GESTOR_AREA/GESTOR_RH. Salvar funcionário comum apaga o gestor do par. Incerteza restante: se `entity` na edição é o estado antes da alteração (`commons`). ✔ Decidido (D11): gestor é dado próprio do departamento; falta só o conjunto de subordinados (ORG-4).
- ORG-2 Origem dos resíduos em `unidade_departamento`.
- ORG-3 Quem lê `departamento_gestor` (BI?) e se depende dos descendentes ativos.
- ORG-4 ✔ Decidido (D20): toda a árvore. Original: conjunto desejado de subordinados (um nível, gestor do filho em vez da equipe, recesso, nome fixo "Administrador").
- ORG-5 Cargo nasce INATIVA por desenho? Cópia de cargo em uso?
- ORG-6 Papel de negócio do departamento do cargo (96 cargos com funcionários em outro depto).
- ORG-7 ✔ Decidido (D44): recesso volta sozinho na data fim.
- ORG-8 ✔ Decidido (D45): situação única com datas obrigatórias; resíduos não replicados.
- ORG-9 ✔ Decidido (D46): unicidade no banco; duplicados do legado não migrados.
- ORG-10 Cadastro de empresa (quem cria), `cbo`, `versao_bi`, consulta entre contratantes.
- ORG-11 Papel de `ponto_focal_rh` e `preenche_formacao_e_idiomas`.
- ORG-12 Apontamento em nome de outro avaliador; foto sem escopo.
- ORG-13 Funcionários sem unidade (10 ADMINISTRADOR) e erro ao editar.
- ORG-14 Escala 0–10 do apontamento × graduação.

## 03 Modelo de competências
- MOD-1 Mais de dois níveis de tipo; competência em tipo com subtipos.
- MOD-2 ✔ Decidido (D35): propaga ao inativar; reativar restaura só o que a cascata inativou.
- MOD-3 ✔ Decidido (D47): troca bloqueada se há cargos associados; 9 vínculos sem cargo×tipo são resíduo.
- MOD-4 Igualdade de objeto na gravação de hints; 84 hints de escala antiga no home office.
- MOD-5 ✔ Origem verificada em código (2026-10-09): o importador `support/` (`AssociacoesCargoSheet`) grava `cargo_tipo_competencia.peso` da coluna 5 da planilha, truncado para inteiro (`UtilsRow.converterDoubleCell`); valores como 50, 33, 67, 25 são pesos digitados na planilha. A tela do legado não edita esse peso. ✔ Decidido (D14): peso relativo, com tela.
- MOD-6 Associação sem pergunta aplicável (15 casos).
- MOD-7 Gabaritos/vínculos que sobrevivem à regra de exclusão (3, 35 e 40 casos).
- MOD-8 ✔ Decidido (D48): reuso entre competências permitido; a nota conta na competência do vínculo do cargo (deduzido do legado, a confirmar).
- MOD-9 ✔ Resolvido (D6): dimensões não entram no novo.
- MOD-10 ✔ Decidido (D23): versão congelada. Pesos duplicados: ver D15.
- MOD-11 NPE ao ativar cargo×competência sem cargo×tipo.
- MOD-12 ✔ Decidido (D34): ADMINISTRADOR e GESTOR_RH, no servidor.

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
- CRI-6 Planilha de pares: descartes e homônimos. CRI-7 ✔ Decidido (D36): sobe ao próximo gestor ativo; sem gestor, pendência para o RH. CRI-8 Cópia: criador/data, dimensões, e-mail padrão.
- CRI-9 Fluxo home office e configuração. CRI-10 ✔ Decidido (D13): por data e por estado. CRI-11 Assimetria de padrões criar×editar; 8 `autoavaliacao` nulos.
- CRI-12 Participantes LIBERADA/CONCLUIDA sem snapshot de cargo/depto (11 e 2).

## 06 Preenchimento
- PRE-1 ✔ Verificado (2026-10-10): nenhum código atual (`web/`, `webapp/`, `support/`) grava `consenso_iniciado = true`; o Java só o zera (`AvaliacaoParticipanteServiceImpl:334, 385`) ou normaliza nulo para falso. No banco, as 328 linhas verdadeiras têm todas `consenso_liberado` verdadeiro e última atualização entre 2017-06 e 2019-01-24: resíduo de versão antiga do código. Hoje o marcador é código morto de fato (a tela só o lê); no novo, derivar do estado do consenso em vez de gravar. PRE-2 ✔ Decidido (D30): libera com Av1 e Av2 finalizadas, regra única no servidor. PRE-3 Mobile sem rastro (`tipo_client`).
- PRE-4 ✔ Decidido (D16): servidor valida. PRE-5 ✔ Decidido (D17): servidor recusa após o prazo.
- PRE-6 ✔ Decidido (D31): aceite explícito de cada parte, no servidor. PRE-7 Copia-e-cola em `doBeforeUpdate`; início de Av2 sem resposta.
- PRE-8 ✔ Verificado (2026-10-10): o DDL tem `DEFAULT false`, mas o Hibernate insere `NULL` explícito porque nenhum código define o valor na criação do participante; o getter `DomainUtils.safeBoolean` trata nulo como falso, então o Java não distingue. Só `ParticipanteServiceImpl:244-246` e `ParticipanteResource:183-194` calculam o marcador (`total == preenchimentoAvN`), então **nulo = nunca recalculado; falso = recalculado e incompleto**. Corrige a leitura "nulo = etapa não se aplica" de RN-PRE-16. Banco: Av1 nulo 1.098 / falso 5.438; Av2 nulo 1.501 / falso 2; Av3 nulo 7.007 / falso 91. No novo, derivar das respostas (já em D16). PRE-9 27 participantes sem linhas. PRE-10 ✔ Resolvido (D7): revisor/Av4 não entra.
- PRE-11 1.181 respostas sem vínculo congelado. PRE-12 ✔ Parcial (D32): reabrir = ADMINISTRADOR e GESTOR_RH; falta definir quem vê o acompanhamento.
- PRE-13 3 Av1 de subordinados; 3 AUTOAVALIACAO com `limite_av3`. PRE-14 `limite_avN` é date ou timestamp.

## 07 Resultados
- RES-1 ✔ Resolvido (D24): Pentaho descartado; o novo define cálculo e relatórios. Original: onde ficam ETL, `.wcdf`, periodicidade e frescor de `bi_media_*`. RES-2 ✔ Sem objeto (D24/D38): fórmula do legado é só referência; divergência de 5% na média por agente não vira requisito.
- RES-3 ✔ Decidido (D15): congelados na liberação, fonte única. RES-4 ✔ Defeito (não replicar): o novo garante uma resposta por participante × pergunta × agente e uma linha de peso por resposta (D15).
- RES-5 ✔ Decidido (D37): consenso NPA exclui a pergunta. RES-6 ✔ Defeito (não replicar): toda resposta aponta para a graduação congelada (D23); sem vínculo não é gravada. RES-7 ✔ Decidido (D38): normalizar escala; peso 0 exclui, sem nota se vazio.
- RES-8 ✔ Sem objeto (D24): view/migração do legado descartadas; o novo define o filtro de resultados. RES-9 ✔ Decidido (D39): escopo no servidor pela sessão.
- RES-10 ✔ Decidido (D40): RH/gestor/próprio; `ajustarpesos` descartado. RES-11 ✔ Verificado em código (2026-10-12): `ChartAvaliacao` (`webapp/services/avaliacao.js:6`) não tem consumidor em `webapp/` nem `mobile/`; endpoints de gráfico são código morto, não replicar.
- RES-12 ✔ Defeito (não replicar): percentual correto por fase, autoavaliação própria, lista vazia = sem dados (não TOTALMENTE). RES-13 ✔ Sem objeto: o novo tem uma fonte única de progresso.
- RES-14 ✔ Resíduo de dados do legado (migração é non-goal); no novo, `limite_avN` só existe para fases que o tipo de avaliação preenche.

## 08 PDI e feedforward
- PDI-1 ✔ Decidido (D22): funcionário propõe a própria ação. Original: quem cria ação de PDI? PDI-2 Transições só no servidor (dono do corpo, sem máquina de estados).
- PDI-3 ✔ Decidido (D22): gestor direto aprova, ninguém aprova o próprio, imposto no servidor. PDI-4 Vínculo com participante arbitrário e só avaliação LIBERADA.
- PDI-5 Uso de `revisado`. PDI-6 Exclusão sem checagem. PDI-7 ✔ Resolvido (D9): PDI entra, com ciclo de vida revisto.
- PDI-8 Competências fora do cargo (20%) e erro ao trocar na edição. PDI-9 Anotações só LIBERADA; sem ligação texto→ação.
- PDI-10 Escopo do gestor de área e listagens por id. PDI-11 `colaboradorespdi` sem uso; relatórios com colunas trocadas.
- PDI-12 `POST /feedsRecebidos`. PDI-13 `empresa.feedforward` sem efeito; feedforward × registro crítico; escala 0–10.

## 09 Comunicação
- COM-1 ✔ Decidido (D18): ENVIADO = aceito pelo SMTP. COM-2 249 PENDENTE nunca enviados; 0 ERRO.
- COM-3 ✔ Decidido (D27): caixa e parâmetro valem, os dois, no servidor. COM-4 ✔ Decidido (D28): mesma regra, valida e-mail, sem duplicar.
- COM-5 `notificacao.email.resposta.mensagem` e resposta fora de avaliação. COM-6 ✔ Decidido (D29): aviso de segurança sem a senha, sempre enviado.
- COM-7 Falha de validação no meio do lote. COM-8 Assunto da conclusão pela empresa do logado; sem GESTOR_RH.
- COM-9 RH avisado do erro de liberação? COM-10 Forma e protocolo do link. COM-11 ✔ Decidido (D19): link de definição de senha.
- COM-12 Valores fora do repositório (`dezi.queue.*`, `homeoffice.*`, `email.contato.suporte`). COM-13 `doBeforeSave` redefine senha a cada gravação? Parcial (2026-10-10): a edição tem hook próprio (`doBeforeUpdate`, `FuncionarioServiceImpl:253`) sem tratar senha, e 460 funcionários atualizados depois da criação têm `primeiro_acesso` falso; indício forte de que não redefine na edição, mas só a `commons` (quem chama qual hook) confirma. Sem fonte (D25): no novo, redefinir senha nunca é efeito de salvar cadastro (D19).
- COM-14 Necessidade do relatório de bug e de `Mensagem`/`TipoMensagem`.

## Elicitação sobre D33-D48 (2026-10-12; Graph of Thoughts, Second-Order Thinking, Inversion Analysis, Red Team, Socratic Questioning)
Lacunas e riscos entre as decisões do passo 5. Cada item traz a recomendação do analista; nenhum está decidido.
- ELI-1 Escopo do gestor: árvore **atual** ou a da liberação (snapshot)? Se o RH troca o gestor do departamento, o antigo mantém resultados antigos e o novo passa a vê-los? Rec.: resultado de campanha concluída segue o snapshot da liberação; operação corrente segue a árvore atual. (D33, D11, D39)
- ELI-2 Anonimato: GESTOR_AREA vê detalhamento por agente? Qual o mínimo de respondentes para exibir um agente (com um só par, a média o identifica)? Rec.: mínimo de 3 por agente, senão agrega ao grupo; gestor vê só consolidado. (D40, D33)
- ELI-3 Mudanças depois da liberação: gestor que entra em recesso ou é desligado, quem aprova PDI quando o gestor direto está ausente, e tokens de quem perdeu perfil ou foi desligado. Rec.: substituição de avaliador por ação explícita do RH com auditoria; aprovador de PDI sobe ao próximo gestor ativo (como D36); invalidar sessões também ao mudar perfil, situação ou desligar. (D36, D22, D41)
- ELI-4 Competência sem nota (todas as perguntas NPA ou sem resposta): o tipo renormaliza sem ela ou conta zero? Rec.: renormaliza, como o legado faz com agentes ausentes, e o resultado indica a competência como "sem nota". (D37, D38)
- ELI-5 Recesso: situação gravada ou derivada das datas? Início futuro mantém EM_ATIVIDADE até a data? Quem executa a virada e como trata fuso e falha do job? Rec.: derivada das datas, virada diária idempotente com registro de evento. (D44, D45)
- ELI-6 Bloqueio de login como negação de serviço: contar por matrícula inexistente permite bloquear contas legítimas, inclusive ADMINISTRADOR. Rec.: contar por par IP + matrícula, desbloqueio pelo RH e administrador isento de bloqueio permanente. (D42)
- ELI-7 Retorno de recesso e entrada tardia: quem voltou depois da liberação fica fora da campanha. Rec.: RH pode incluí-lo como participante tardio, com a mesma versão do catálogo (D23). (D44, D23)
- ELI-8 Corrigir competência no tipo errado exige desassociar de todos os cargos e perde pesos e gabaritos; risco de duplicar competências e quebrar o histórico. Rec.: operação assistida "mover tipo" que cria os cargo×tipo faltantes com confirmação, em versão futura. (D47)
- ELI-9 Pendência da liberação sem gestor (D36): bloqueia a campanha inteira ou só o participante? Rec.: libera os demais e mantém só o participante pendente, até o RH designar. (D36, D4)
- ELI-10 Peso congelado (D15) para participante que entra depois da liberação: usa o peso da liberação da avaliação ou o do momento da entrada? Rec.: o do momento da entrada, congelado a partir dali. (D15, D23)

- ELI-11 Política de senha do novo (legado: sem tamanho, complexidade nem histórico): tamanho mínimo, bloqueio de senhas comuns e algoritmo de hash. Rec.: tamanho mínimo e lista de senhas comuns, hash BCrypt ou melhor. (D19, D29, D42)
- ELI-12 Ordem entre os perfis na D10 ("igual ou superior" não define a ordem). Rec.: ADMINISTRADOR > GESTOR_RH > CONSULTOR_RH > GESTOR_AREA > FUNCIONARIO, e GESTOR_RH só atribui perfis abaixo do seu. (D10)
- ELI-13 ✔ Decidido (D57): separar administrador de plataforma e da empresa. Original: ADMINISTRADOR de plataforma (todas as empresas) × administrador da empresa, e autenticação reforçada para o primeiro. Rec.: separar os dois papéis e exigir segundo fator do administrador de plataforma; viabilidade vai ao `bmad-architecture`. (D43, D51)
- ELI-14 PDI visível por toda a árvore (D33): conteúdo sensível para gestores dois ou mais níveis acima. Rec.: acima do gestor direto, só o status, não o conteúdo. (D33, D22)

- ELI-15 ✔ Decidido (D58): par (unidade, departamento) e descendentes na mesma unidade. Original: Escopo do gestor com a dimensão unidade: o gestor é uma linha de (unidade, departamento) e o departamento tem hierarquia real (6 níveis, 46 raízes), mas a D33 e a D20 falam só de departamento. Rec.: escopo = o par (unidade, departamento) mais os departamentos descendentes na mesma unidade; a hierarquia de unidades fica de fora (as 34 ativas são raiz). (D33, D20, D11)
- ELI-16 Quem avalia o gestor de departamento raiz? No legado, sem gestor acima vira pendência `not-found-avaliador`, que apaga ou não grava o participante e bloqueia a liberação. Rec.: o gestor do topo é avaliado por autoavaliação, pares e subordinados, sem pendência; a pendência de avaliador só vale para quem tem um nível acima. (D36, ELI-9)
- ELI-17 "Ativo" tem três predicados: pode acessar, é elegível a avaliar, ocupa e-mail (ver `glossario.md`). RECESSO pode acessar o sistema? Rec.: acessa, mas não avalia nem é avaliado; DESLIGADO não acessa. (D20, D36, D45, D50)
- ELI-18 ✔ Decidido (D59): legado somente leitura, novo começa com campanhas novas. Original: Histórico do legado (2020 a 2024): a migração de dados é non-goal, mas o spec tem resultados e evolução histórica. Rec.: o sistema antigo fica como consulta somente leitura; a importação só dos resultados consolidados é decisão futura, do `bmad-architecture`. (D24, D38)
- ELI-19 Fim do prazo da etapa: o legado compara com o instante atual, não com o fim do dia (12% das Av2 foram gravadas depois do limite). Rec.: o prazo vale até 23:59:59 do dia, no fuso da empresa. (D17, D13)
- ELI-20 Substituir um avaliador que já respondeu: o que acontece com as respostas e com a etapa finalizada? Rec.: as respostas do anterior ficam no histórico, o novo avaliador parte do zero e o RH reabre a etapa com motivo (D32). (D12, ELI-3)
- ELI-21 ✔ Decidido (D60): por mínimo e máximo. Original: Normalização da escala: "percentual do máximo" deixa o nível mais baixo em 20% numa escala de 1 a 5 e em 10% numa de 10 a 100, e o legado tem 15 escalas decrescentes. Comparar com a normalização por mínimo e máximo, que tira esse viés. Decisão de negócio, porque muda a nota; sem recomendação firme. (D38)
- ELI-22 Regra de dois olhos sem segundo ator: empresa com um só GESTOR_RH ou ADMINISTRADOR (o legado tem 19 ADMINISTRADOR e 19 empresas, o que sugere um por empresa; não verificado). Rec.: o administrador de plataforma (D57) é o ator de contingência. (D52, D57)

## Itens de infraestrutura que travam a leitura
**Posição do responsável (2026-10-10):** a `commons` está definitivamente inacessível (D25) e o Pentaho está descartado (D24); nenhum dos dois será lido. O que depende deles vira decisão do sistema novo, não pendência do legado. Lista original:

Biblioteca `br.com.fasolti.commons` (token, `BaseAvdService`, transição genérica, escopo padrão, transação do `save`); ETL/relatórios do Pentaho; propriedades de ambiente; `support/` (importador, explicaria dados fora da regra); `mobile/` (lido só por busca); textos i18n e 5 de 8 modelos HTML de e-mail.
