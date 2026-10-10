# Levantamento de regras de negócio: plano e progresso

Sistema legado DEZI (`web/`). Documento de passagem entre conversas: mostra a ordem sugerida, o que já foi feito, o que falta e o contexto necessário para continuar.
Roteiro geral: [`../roteiro-bmad-levantamento-regras-negocio.md`](../roteiro-bmad-levantamento-regras-negocio.md) (passo 3, analista Mary, `/bmad-agent-analyst`).
Última atualização: 2026-10-10 (itens 1 a 9 concluídos; spec condensado em 2026-10-06; passo 5 do roteiro em andamento, com as decisões D6 a D19 de 2026-10-09; ver seção 6).

## 1. Sequência sugerida e progresso

Legenda: `[x]` abordado · `[~]` parcial · `[ ]` a fazer.

| Ordem | Situação | Submódulo | Documento |
|---|---|---|---|
| 1 | `[x]` | Acesso e perfis | [`01-acesso-perfis.md`](01-acesso-perfis.md) |
| 2 | `[x]` | Cadastro organizacional | [`02-cadastro-organizacional.md`](02-cadastro-organizacional.md) |
| 3 | `[x]` | Modelo de competências | [`03-modelo-competencias.md`](03-modelo-competencias.md) |
| 4 | `[x]` | Graduação e conceitos | [`04-graduacao-conceitos.md`](04-graduacao-conceitos.md) |
| 5a | `[x]` | Avaliação: estados e transições | [`05a-avaliacao-estados-transicoes.md`](05a-avaliacao-estados-transicoes.md) |
| 5b | `[x]` | Avaliação: criação, agentes, participantes, pendências | [`05b-avaliacao-criacao-agentes-participantes.md`](05b-avaliacao-criacao-agentes-participantes.md) |
| 6 | `[x]` | Participantes e preenchimento | [`06-participantes-preenchimento.md`](06-participantes-preenchimento.md) |
| 7 | `[x]` | Resultados e gráficos | [`07-resultados-graficos.md`](07-resultados-graficos.md) |
| 8 | `[x]` | PDI e feedforward | [`08-pdi-feedforward.md`](08-pdi-feedforward.md) |
| 9 | `[x]` | Comunicação | [`09-comunicacao.md`](09-comunicacao.md) |

O **item 5** foi abordado por inteiro (5a e 5b). Foi feito antes dos itens 1 a 4 por escolha do responsável; por isso algumas regras dependem de módulos ainda não levantados (ver seção 4).

Ordem recomendada daqui em diante: ~~6 → 7 → 1 → 2~~ (feitos) → ~~3~~ (feito) → ~~4~~ (feito) → ~~8~~ (feito) → ~~9~~ (feito). O item 1 foi feito e esclareceu o que a tela permite (concluir, inativar e reativar avaliação; "Ajustar pesos" só ADM), mas **o servidor quase não impõe perfis** (só o painel de controle tem `@PreAuthorize`) e a permissão da transição genérica segue a confirmar (biblioteca `commons`). O item 4 foi feito a partir de 07 (a nota usa `ponto`, escala de 0 a 120 por modelo).

Cadastros de apoio sem lógica (Idioma, Municipio, Escolaridade, CursoGraduacao, TipoCurso, CBO) entram só no glossário, sem arquivo próprio.

## 2. Itens a fazer

Referências de código abaixo são a partir de `web/`. Nomes de classes são os reais do repositório.

### 1. Acesso e perfis (concluído em 2026-10-01; ver `01-acesso-perfis.md`)
Achados que orientam os itens seguintes: o controle por perfil está quase todo na tela (`webapp`), não na API; o escopo de dados por perfil vive nos `getServiceFilter` de cada serviço (tabela na seção 7 de 01) e o item 2 deve detalhar a hierarquia que ele usa; as tabelas `log_auth` e `log_entity` **não existem na instância do banco** (o script de deploy `after-restore` as cria; `log_entity` não tem migração); a troca de senha é auditada com a senha em texto. Não lidos: biblioteca `commons`, endpoint de boas-vindas em massa, `webapp/partials/preenchimento/`, `mobile/`. Lista original do que ler, mantida por referência:
- **Ler:** `rest/.../AuthResource.java` (269 linhas), `services/.../SecurityServiceImpl.java` e `SecurityServiceDebug.java`, `LogAuthServiceImpl`, `UserResource`, `PentahoLoginServiceImpl` e as exceções `Pentaho*`, enum `TipoPerfil`, `SituacaoAuth`, `LogAuth`/`LogEntity` (auditoria).
- **Extrair:** perfis e o que cada um pode fazer; login, primeiro acesso, troca e recuperação de senha; bloqueios (`SituacaoAuth`); integração com Pentaho; auditoria por `@LogRequired`.
- **Atenção:** só há 1 anotação de permissão no `web/`; as permissões devem estar no código dos services e resources, procure `getLoggedUser().getTipoPerfil()`. A biblioteca `br.com.fasolti.commons` (segurança, transição genérica de estados) **não está mais acessível**; o que ela fazia só se deduz pelos usos.
- **Fecha pendência:** quem pode `CANCELADA`/`CONCLUIDA` (seção 3, item 1).

### 2. Cadastro organizacional (concluído em 2026-10-01; ver `02-cadastro-organizacional.md`)
Achados que orientam os itens seguintes: o gestor de um departamento (`unidade_departamento`) é mantido como efeito colateral de salvar **qualquer** funcionário e provavelmente se perde ao salvar um funcionário comum (57 de 156 pares sem gestor); `departamento_gestor` só é gravada, nenhum Java a lê (consumo provável pelo BI); recesso não termina sozinho e suas datas não são usadas por nenhuma regra de avaliação; cargo nasce `INATIVA` (54% inativos); hierarquias de unidade e de cargo praticamente sem uso; departamento inativo corta a subida do gestor. Não lidos: `support/` (importador), `commons` (`BaseAvdService`), `funcionario.edit.html`. Lista original do que ler, mantida por referência:
- **Ler:** `EmpresaServiceImpl`, `UnidadeServiceImpl`, `DepartamentoServiceImpl` (319 linhas), `DepartamentoGestorServiceImpl`, `UnidadeDepartamentoServiceImpl`, `CargoServiceImpl` (562 linhas; já lidas as linhas 163-354 em 5b), `FuncionarioServiceImpl` (921 linhas), `ApontamentoFuncionalServiceImpl`, `SituacaoServiceImpl`, `FuncionarioFormacao`/`FuncionarioIdioma`, `FotoServiceImpl`; enums `SituacaoFuncionario`, `TipoPerfil`, `TipoSexo`.
- **Extrair:** hierarquia de departamentos e unidades; quem é gestor de quê (`UnidadeDepartamento.gestor`); situação funcional (em atividade, recesso com datas, desligado) e efeitos; subordinados diretos (`obterSubordinados`); apontamentos funcionais; regras de cargo (cargo pai, ativo/inativo).
- **Migrações relevantes:** V1.0_001, V1.0_002, V2.0_0016, V2.0_0032, V2.0_0033, V2.0_0034, V2.0_0036, V3.0_20220124000001 (departamento gestor).
- **Já sabido (5b):** `findAvaliador` usa unidade+departamento e sobe para o departamento pai; ignora gestor `DESLIGADO`.

### 3. Modelo de competências (concluído em 2026-10-01; ver `03-modelo-competencias.md`)
Achados que orientam os itens seguintes: o modelo efetivo de graduação é o do **detalhamento** (o da competência é resíduo da V2.0_0021; 63 detalhamentos divergem) e hints de modelo antigo não são apagados (item 4 deve fechar escala, `comentario_obrigatorio` padrão e hint padrão da graduação); os pesos de cargo têm três níveis, nenhum percentual, e o do cargo×tipo não tem tela; inativar apaga gabarito fisicamente e reativar não restaura; dimensões sem uso real (0 vínculos); o snapshot grava pesos por participante. Não lidos: importador `support/`, `commons` (`BaseAvdService`), telas de dimensão e detalhamento. Lista original do que ler, mantida por referência:
- **Ler:** `CompetenciaServiceImpl` (690), `CompetenciaDetalhamentoServiceImpl`, `CompDetalhamentoHintServiceImpl`, `TipoCompetenciaServiceImpl` (333), `DimensaoServiceImpl`, `DimensaoCompetenciaDetalhamentoServiceImpl`, `CargoCompetenciaServiceImpl`, `CargoTipoCompetenciaServiceImpl`, `DetalhamentoCargoCompetenciaServiceImpl` (284); enum `TipoComp`.
- **Extrair:** relação tipo → competência → detalhamento (pergunta) → hint; peso por cargo (`cargo_competencia.peso`, `cargo_tipo_competencia.peso`); "aplicável ao cargo" e gabarito; ativo/inativo e efeitos em avaliações já liberadas; dimensões.
- **Migrações:** V1.0_016 a V1.0_020, V2.0_0014, V2.0_0021, V2.0_0022, V2.0_0023, V2.0_0030.
- **Já sabido (5a/5b):** a liberação congela competências, detalhamentos, hints, pesos e graduações em tabelas `avaliacao_*` (snapshot).

### 4. Graduação e conceitos (concluído em 2026-10-01; ver `04-graduacao-conceitos.md`)
Achados que orientam os itens seguintes: o **conceito não existe de fato** (tabela com 0 linhas, sem regra de nota → conceito nem tela; a "faixa de nota → conceito" do roteiro não existe no legado); `nao_graduar` e `qtd_graduacoes` não são confiáveis (o que vale é existir nível ativo com texto "Não posso avaliar"); `ponto = 0` marca "sem nota" (NPA e escala de comentário); editar um nível reescreve o texto de todas as perguntas; a cor vista no preenchimento vem da **ordem** (paleta fixa), não do cadastro; a liberação copia todos os níveis (ativos e inativos) para `graduacao_competencia` e a avaliação deixa de acompanhar o cadastro; web ordena níveis por ordem e mobile por ponto. Não lidos: `commons`, textos i18n, BI, `mobile/`. Lista original do que ler, mantida por referência:
- **Ler:** `ModeloGraduacaoServiceImpl`, `GraduacaoServiceImpl` (278), `GraduacaoCompetenciaServiceImpl`, `ModeloGraduacaoCompServiceImpl`, `ModeloGraduacaoAvalCompServiceImpl`, `ConceitoServiceImpl`; enums `TipoConceito`, `GraduacaoCorEnum`.
- **Extrair:** escala de notas (pontos, rótulos, cores, "não graduar"); modelo de graduação por detalhamento; conceito (faixas de nota → conceito).
- **Migrações:** V1.0_005, V1.0_015, V2.0_0029, V2.0_0035.

### 6. Participantes e preenchimento (concluído em 2026-09-30; ver `06-participantes-preenchimento.md`)
Não lidos nesta passada: `AvaliacaoCompetenciaMobileServiceImpl`, `FuncionarioServiceImpl.obterAvaliadosAtivosPorFuncionarioDaEmpresa`, origem de `dado_preenchimento` (item 7). Lista original do que ler, mantida por referência:
- **Ler:** `AvaliacaoParticipanteServiceImpl` (1.194 linhas; só 200-270 e 355-475 já lidas), `ParticipanteServiceImpl` (289), `PreenchimentoServiceImpl` (329), `DadoPreenchimentoServiceImpl` (233), `AvaliacaoCompDetServiceImpl`, `AvaliacaoCompetenciaServiceImpl`, `PendenciaServiceImpl`, versões mobile (`PreenchimentoMobileServiceImpl` 617 linhas, `ParticipanteMobileServiceImpl`, `AvaliacaoCompetenciaMobile`, `DetalhamentoMobile`), `ParticipanteResource`, `PreenchimentoResource`, `MobileResource`; enums `SituacaoPreenchimento`, `TipoClient`, `TipoAvMobile`.
- **Extrair:** fluxo Av1..Av4 (quem preenche o quê e quando; datas de início e conclusão; `preenchidaAvN`); consenso (`consensoIniciado`, `consensoLiberado`), revisão (`revisaoIniciada`, `revisaoLiberada`), comentário final; regras de preenchimento parcial/total e de comentário obrigatório (hint); prazos; o que é permitido depois do limite.
- **Perguntas em aberto para responder aqui:** semântica exata de Av1..Av4 (5b, RN-CRI-09 a 11); quando `preenchidaAvN` vira verdadeiro; se o revisor ainda é usado (5b, RN-CRI-28: 0 usos no banco); por que `id_situacao_preenchimento` é sempre 1.

### 7. Resultados e gráficos (concluído em 2026-09-30; ver `07-resultados-graficos.md`)
Achados que orientam os itens seguintes: o `web/` não calcula a nota dos relatórios (vive no ETL do Pentaho, fora do repositório; fórmula deduzida do banco); o cálculo Java de gráfico GAP está zerado e sem uso; pesos de cargo chegam ao BI por tabela preenchida só pelo botão "Ajustar pesos"; o painel de controle tem defeitos de comparação e de percentual. Não lidos: relatórios `.wcdf`, ETL, `listController.js` do preenchimento. Lista original do que ler, mantida por referência:
- **Ler:** `GraficoAvaliacaoServiceImpl` (346), `PainelControleServiceImpl` (891), `ParticipanteEvolucaoServiceImpl`, `BiParametroServiceImpl`, `PentahoLoginServiceImpl`, `AcompanhamentoResource`, `GraficoAvaliacaoResource`, `PainelControleResource`, `BiParametroUsuarioResource`; views e tabelas de BI nas migrações V3.0_20200730110000 a V3.0_20210405000001 (`view_dados_preenchimento`, `conjunto_resultados` v2/v3, tabelas ETL, `bi_parametro_usuario`).
- **Extrair:** cálculo de nota por competência, tipo, perfil e avaliação (uso dos pesos de agente, de cargo-competência e de cargo-tipo-competência); comparação entre perfis (auto x gestor x pares); evolução histórica; regras dos indicadores do painel.
- **Já sabido (5a/5b):** o progresso da avaliação é calculado como percentual (`calcularPreenchimentoTotalDaAvaliacao`) sem gravar estado; os pesos dos agentes não somam 100 (5b, RN-CRI-13).
- **Atenção:** a view `conjunto_resultados` v2 tem uma precedência `AND`/`OR` suspeita (5a, RN-EST-24).

### 8. PDI e feedforward (concluído em 2026-10-01; ver `08-pdi-feedforward.md`)
Achados que orientam o item 9: o ciclo do PDI **não dispara e-mail nem mensagem** em nenhuma transição (RN-PDI-17; item 9 confirma ou corrige); o PDI é cadastro manual, sem ligação automática com o texto de consenso (`avaliacao_participante.pdi`, escrito quase só pelo gestor) nem com notas; a permissão de transição vive em `doBeforeUpdate` (só o par de situações é conferido, o dono vem do corpo da requisição, sem máquina de estados) e a exclusão pela tela não tem checagem alguma; o PDI está praticamente parado (360 das 379 ações criadas em 2020; 93% com prazo vencido sem avançar); feedforward é uma coluna de `apontamento_funcional` (separada de "registro crítico" só por predicado), o parâmetro `empresa.feedforward` não tem efeito no mobile e o endpoint web `POST /feedsRecebidos` usa o avaliado do corpo e não é chamado por nenhuma tela. Não lidos: layout `.jrxml`, `commons`, i18n. Lista original do que ler, mantida por referência:
- **Ler:** `PDIAcaoServiceImpl` (704), `PDIAcaoCompetenciaDetalhamentoServiceImpl` (235), `FeedForwardRecebidoServiceImpl`, `FeedForwardMobileServiceImpl`, `FeedForwardRecebidoMobile`, `ApontamentoMobile`; enum `SituacaoPDI`; `PDIAcaoResource`, `FeedForwardRecebidoResource`; método `obterAvaliacoesPDI` de `AvaliacaoServiceImpl` (linha ~2028).
- **Extrair:** ciclo do PDI (estados e quem pode cada transição; já se vê em `PDIAcaoServiceImpl:130` que só o funcionário do PDI aprova para andamento e finaliza); vínculo com competências e resultados da avaliação; feedforward.

### 9. Comunicação (concluído em 2026-10-01; ver `09-comunicacao.md`)
Achados: todo e-mail passa por `email_controle` → RabbitMQ → `queue/`, e **"enviado" não prova entrega** (o consumidor engole erros de SMTP); 28% das linhas (792) estão em REENVIO por um defeito de estado compartilhado no consumidor (o erro é gravado na linha do último e-mail processado, não na do que falhou); o e-mail de troca de senha **não sai** (envio comentado) apesar do parâmetro `true`; a caixa "Notificar via e-mail" é ignorada pelo servidor; o parâmetro `notificacao.email.resposta.mensagem` é código morto; as 8 chaves de template da tabela `parametros` não são lidas (os modelos vivem no `queue/`); mensagens de liberação, erro e home office viram registros de `mensageria` **sem destinatário** (276 linhas), e o erro de liberação vai só para o suporte; senhas ficam em texto em `email_controle`; há credencial SMTP real como valor padrão em `queue/.../application.yml`; o fluxo home office foi usado em 2020 (132 mensagens), apesar de `av_homeoffice` nulo. Não lidos: `commons`, i18n dos avisos, 5 dos 8 modelos HTML, telas de contato e fale conosco. Lista original do que ler, mantida por referência:
- **Ler:** `MensageriaServiceImpl` (534), `MensagemServiceImpl`, `EmailControleServiceImpl` (388), `ParametroServiceImpl` (templates), `FaleConoscoServiceImpl`, `ContatoServiceImpl`, `BugReportServiceImpl`, `web/services/.../rabbitmq/sender`; enums `TipoMensagem`, `TipoEmailStatus`; módulo `queue/` (consumidor RabbitMQ que envia e-mail, fora do `web/`).
- **Extrair:** gatilhos de e-mail (liberação de avaliação, liberação concluída, erro de liberação, boas-vindas, lembretes); templates e parâmetros (V2.0_0012, V2.0_0017, V2.0_0020, V2.0_0028); reenvio, status e falhas; ligação com o `queue/`.
- **Já sabido (5a):** `mensagemLiberarAvaliacao` não é enviada quando o usuário é administrador; `mensagemLiberarAvaliacaoConcluido` é enviada ao fim da liberação e `mensagemLiberarAvaliacaoErro` em caso de falha.

## 3. Pendências e decisões já tomadas

Decisões do responsável (registradas em 5a, seção 7.1):
1. **Quem dispara `CANCELADA` e `CONCLUIDA`: parcialmente resolvido pelo item 1 (01, seção 10).** `CONCLUIDA` é acionada pela tela da campanha ("Concluir", avaliação `LIBERADA`) por quem vê essa tela (ADM, CONSULTOR_RH, GESTOR_RH, sem trava por perfil nos itens); o servidor não verifica perfil, e quais perfis de fato concluem fica **a confirmar com o negócio**. `CANCELADA` não tem caminho na tela nem endpoint, e 0 usos no banco (50 `CONCLUIDA`). Correção de 05a RN-EST-19: existe "Ativar" (`INATIVA` → `LIBERADA`) na tela.
2. **Falha na liberação deixa a avaliação e a campanha presas em "em liberação":** defeito a corrigir no **sistema novo** (redesenhar a liberação: estado de erro ou reversão, acompanhamento, idempotência). Não corrigir o legado.
3. **Validação no banco:** feita para 5a e 5b (ver seção 5).
4. **`id_situacao_preenchimento` é campo morto** (sempre 1) e o progresso hoje é só um percentual: defeito a corrigir no **sistema novo** com estado de preenchimento consistente.

5. **Defeitos do legado a corrigir no sistema novo, vindos do item 7** (não corrigir o legado): pesos de cargo do BI dependem de ação manual e deixam 18% das respostas com peso 1 (07, RN-RES-16 a 18); fonte única de peso congelado na liberação; percentual do painel com `Integer ==` e autoavaliação copiada da avaliação (RN-RES-39, 40); gráfico GAP zerado (RN-RES-30); escopo do BI por nome e `idFuncionario` sem checagem (RN-RES-23).

7. **Decisões do responsável em 2026-10-01 (ver 01, seção 11.1):** o parâmetro `notificacao.email.funcionario.novo=false` é intencional; **`CANCELADA` entra no sistema novo** (gatilho, perfil e consequências a definir); **concluir, inativar, reativar e cancelar avaliação: `ADMINISTRADOR` e `GESTOR_RH`; `CONSULTOR_RH` somente leitura**, imposto no servidor.
6. **Defeitos do legado a levar como requisito do sistema novo, vindos do item 1** (não corrigir o legado): autorização por perfil também no servidor; `seclogin` sem bloqueio nem log; senha enviada por e-mail e gravada em texto na auditoria; "esqueci minha senha" derruba a senha de terceiros e revela e-mails; sem política de senha; sem desbloqueio manual; cadastros (perfil) sem auditoria; chave do BI previsível (01, seção 12).

9. **Defeitos do legado a levar como requisito do sistema novo, vindos do item 3** (não corrigir o legado; lista em 03, seção 10): exclusão lógica do gabarito com histórico; reativação de pergunta não reativa competência em silêncio; unicidade no banco (tipo, cargo×tipo, hint, dimensão); regra única de cascata de situação; validar que o detalhamento pertence à competência do cargo; semântica explícita dos pesos; modelo de graduação só no detalhamento; descartar ou refazer dimensões; congelar catálogo com versão (não ler o catálogo atual para quem entra depois).

8. **Defeitos do legado a levar como requisito do sistema novo, vindos do item 2** (não corrigir o legado; lista em 02, seção 11): gestor como dado próprio, não como efeito de salvar pessoa; separar perfil de acesso de papel de gestão; recesso com término automático; unicidade garantida no banco (e-mail, código do cliente, nome da unidade); departamento, cargo e unidade inativos com efeito definido sobre pessoas; controle de ciclo nas hierarquias; escopo no servidor para foto, apontamento e empresa.

10. **Defeitos do legado a levar como requisito do sistema novo, vindos do item 4** (não corrigir o legado; lista em 04, seção 10): dimensão da escala sem sentido; `qtd_graduacoes`/`nao_graduar` sem semântica e NPA identificado por texto; unicidade no banco sem diferenciar maiúsculas; faixa e consistência de ponto no servidor e escala de comentário como tipo explícito; editar nível sem apagar texto personalizado; inativação de escala e nível com efeito definido; cor definida em um só lugar e ordem de exibição única entre web e mobile; congelar por versão sem `NullPointerException` nem vínculos ausentes ou divergentes; conceito: decidir se existe; autorização por perfil e escopo por empresa no servidor.

11. **Defeitos do legado a levar como requisito do sistema novo, vindos do item 8** (não corrigir o legado; lista em 08, seção 9): máquina de estados e permissões do PDI no servidor, com o dono lido do registro; datas definidas pelo servidor e histórico de transições; exclusão lógica e com escopo; vínculo da ação com a avaliação (não com participante arbitrário); validar competência e detalhamento contra cargo; relatórios Excel e PDF com colunas, observações e período corretos; separar apontamento de feedforward e decidir o destino de `empresa.feedforward`; feedforward recebido sempre pelo usuário logado.

12. **Defeitos do legado a levar como requisito do sistema novo, vindos do item 9** (não corrigir o legado; lista em 09, seções 4, 6, 7 e 10): estado de envio confiável (entrega confirmada, falha de SMTP visível, reenvio de tudo que falhou, sem estado compartilhado no consumidor); publicar na fila só depois do commit; troca de senha com e-mail ou remover o parâmetro; respeitar "Notificar via e-mail" por destinatário e o valor do parâmetro; mensagem de sistema como registro próprio (não como mensagem sem destinatário); RH avisado do erro de liberação; assunto e destinatários da conclusão pela empresa da campanha; modelos de e-mail com dono único (arquivo ou tabela, não os dois); links de acesso padronizados e com HTTPS; sem senha em texto no e-mail nem em `email_controle` (link de definição de senha); remetente e endereços de suporte por configuração; **rotacionar a credencial SMTP exposta** e retirar segredos do repositório; autorização no servidor para mensageria (usuário logado, não `userId` da URL).

13. **Decisões do responsável em 2026-10-09 (passo 5 do roteiro; texto completo em `docs/spec/spec-regras-negocio-dezi/decisoes-e-defeitos.md`, D6 a D19):** dimensões, revisor/Av4 e conceito **não entram** no sistema novo (D6 a D8); o PDI **entra, com ciclo de vida revisto** (D9); só `ADMINISTRADOR` atribui `ADMINISTRADOR` (D10); gestor do departamento é dado próprio (D11); excluir avaliador ou participante com resposta é bloqueado (D12); a avaliação encerra por data e por estado (D13); peso cargo×tipo é relativo e tem tela (D14); pesos congelados na liberação, fonte única do BI (D15); o servidor valida comentário obrigatório, completude e prazo (D16, D17); `ENVIADO` = aceito pelo SMTP (D18); boas-vindas por link de definição de senha (D19). Isso **resolve** os itens da lista abaixo sobre revisor e dimensões, apagar quem já preencheu, encerramento, conceito e `ENVIADO`; os demais seguem abertos. A lista consolidada e atual dos pontos abertos é `docs/spec/spec-regras-negocio-dezi/a-confirmar.md`.

Pontos "a confirmar" abertos (contagem na data de cada documento-fonte, anterior às decisões de 2026-10-09): 10 em 5a (seção 7), 12 em 5b (seção 11), 14 em 06 (seção 12), 14 em 07 (seção 9), 12 em 01 (seção 12), 14 em 02 (seção 12), 12 em 03 (seção 11), 15 em 04 (seção 9), 13 em 08 (seção 8) e 14 em 09 (seção 11). A dúvida sobre precedência `AND`/`OR` da view v2 (5a, ponto 8) e a semântica do peso dos agentes (5b, RN-CRI-13) foram **resolvidas** em 07. Os mais importantes para decidir com o negócio:
- se `CANCELADA` é necessário no sistema novo (nunca usada);
- semântica de Av1..Av4 e do peso dos agentes;
- se revisor e dimensões ainda fazem parte do processo (0 usos);
- apagar avaliadores/participantes que já preencheram;
- fluxo home office (campanhas rápidas) e suas propriedades de configuração;
- encerramento: por data ou por estado;
- se conceito (faixa de nota → classificação) deve existir no sistema novo (04, RN-GRA-26), e se a escala de comentário (`ponto = 0`) vira tipo de pergunta;
- quem cria, aprova e exclui ação de PDI, e se o PDI continua sendo processo vivo (08, RN-PDI-02, 07, 15, 16);
- se feedforward e `empresa.feedforward` têm função no sistema novo (08, RN-PDI-39, 47);
- por que 28% dos e-mails estão em REENVIO e o que "enviado" deve significar (09, RN-COM-10, 12 a 14); se a redefinição de senha nas boas-vindas em massa é regra (09, RN-COM-17).

## 4. Dependências entre o item 5 e os demais

| Regra de 5a/5b | Depende do item |
|---|---|
| Quem pode cancelar/concluir, quem pode liberar, `TipoPerfil.ADMINISTRADOR` | 1 (feito; ver 01, seções 6 e 10) |
| Situação funcional, gestor, subordinados, snapshot de cargo/departamento | 2 (feito; ver 02, seções 5 a 8) |
| Validação do cargo para liberar (12 pendências), snapshot de perguntas, dimensões | 3 (feito; ver 03, seções 5 a 9) |
| Modelo de graduação congelado na liberação | 4 (feito; ver 04, seção 6) |
| Preenchimento Av1..Av4, `preenchida_avN`, consenso, revisão | 6 |
| Peso dos agentes, percentual de preenchimento, views de resultado | 7 |
| PDI a partir da avaliação (`obterAvaliacoesPDI`) | 8 (feito; ver 08, seções 3, 5 e 6) |
| Mensagens de liberação | 9 (feito; ver 09, seção 7) |

## 5. Como continuar em outra conversa

> Esta seção é **orientação ao usuário**: não contém regras de negócio nem resultado do levantamento. Quem só quer consultar as regras pode ignorá-la.

### 5.1 Como retomar

A nova conversa começa sem memória da anterior: ela só sabe o que está nos arquivos. O levantamento por módulo (itens 1 a 9) está concluído e condensado em spec, então **a fonte canônica é o spec** em `docs/spec/spec-regras-negocio-dezi/`. Os documentos 00 a 09 desta pasta ficam como evidência (arquivo e linha, consultas ao banco), citada pelos IDs `RN-xxx`.

O trabalho que resta é o passo 5 do roteiro (decidir os pontos "a confirmar") e, depois, os passos 6 a 9 (seção 6). Para decidir pontos, chame a analista Mary e aponte o spec:

```
/bmad-agent-analyst
```
e depois:
```
Continue o passo 5 do levantamento de regras do DEZI (decidir os pontos a confirmar).
Leia o AGENTS.md, docs/regras-negocio/00-plano-e-progresso.md e, no spec,
spec-regras-negocio-dezi.md, decisoes-e-defeitos.md e a-confirmar.md.
Comece pelos itens que mais bloqueiam (seção 6 do plano).
```

Para os passos seguintes, use direto o skill do passo (`/bmad-architecture`, `/bmad-review`, `/bmad-walkthrough`, `/bmad-ticket`), apontando para o kernel do spec.

- Sem a referência ao spec e a este arquivo, a conversa não sabe quais decisões (D1 a D19) já foram tomadas, nem como acessar o banco (seção 5.3).
- O menu da Mary (brainstorming, pesquisa de mercado, product brief e outros) não tem item para este trabalho; a conversa é conduzida lendo o spec e, quando preciso, o código. Se a sua mensagem já disser o que você quer, ela pula o menu e começa direto.
- Contexto a carregar: `AGENTS.md` (política: só código e banco valem; regra sem evidência nos dois lados vai como "a confirmar"; não refatorar nem corrigir o legado), este arquivo e os três arquivos do spec acima.
- Para voltar a **levantar regras de um módulo** (novas leituras do código), use `05a`/`05b` como modelo de formato e nível de detalhe, e depois atualize o spec e `a-confirmar.md`.

### 5.2 Rotina de trabalho

1. **Um módulo por conversa.** Rode `/clear` ou abra uma conversa nova entre um item e outro, como o roteiro geral recomenda; o estado fica nos arquivos.
2. **Formato de cada documento:** glossário → modelo de dados → regras numeradas com prefixo próprio (`RN-EST`, `RN-CRI` e assim por diante; um prefixo por arquivo) → pontos a confirmar → fontes lidas. Toda regra cita arquivo e linha e marca a origem: `[código]`, `[banco]` ou `a confirmar`.
3. **Atualizar o progresso:** ao terminar cada item, peça que a Mary atualize a tabela da seção 1. Sem isso, o item concluído continua como `[ ]`.

### 5.3 Banco de dados (somente leitura)

Há um usuário `dezi_leitura` no banco `dezi` (host em rede Docker; cópia ou produção: a confirmar; dados até 2024-03-20). A senha fica em `~/.pgpass` (nunca em arquivos do projeto). O `psql` já está instalado, mas as variáveis `PG*` **não persistem entre sessões** (o `~/.pgpass` persiste), então exporte-as a cada sessão:

```bash
export PGHOST=172.18.0.2 PGPORT=5432 PGDATABASE=dezi PGUSER=dezi_leitura PGSSLMODE=prefer PGCONNECT_TIMEOUT=8
psql -c "select current_user, current_database()"
```

- Se o IP mudar (rede Docker), confira o host.
- Só consultas agregadas e de catálogo (`information_schema`, `pg_constraint`, `pg_indexes`); não ler dados pessoais de funcionários.
- A tabela de versões das migrações é `schema_version` (93 versões, todas aplicadas, iguais às do repositório); o esquema é o `public`.

### 5.4 Convenções descobertas até aqui

- Os `CHECK`s de situação foram removidos do banco (V2.0_0027): o enum Java é a única restrição.
- Regras em `web/services/.../service/impl` (`*ServiceImpl`); enums em `web/services.client/.../types`; entidades em `web/domain/.../domain`; migrações em `web/domain/src/main/resources/db/migration/`.
- `web/` e `webapp/` não têm testes e a aplicação não sobe localmente.

### 5.5 Estado do repositório

Branch `feature/bmad_init`. Os documentos desta pasta (00 a 09) estão commitados (`fe70281`, `ac25dcc`). **Sem commit:** `docs/spec/` (spec) e `_bmad-output/` (iniciativa). Commit só a pedido do responsável.

## 6. Etapas posteriores (do roteiro geral) e situação

| Passo do roteiro | Situação |
|---|---|
| 3. Analista, módulo a módulo | `[x]` Concluído (itens 1 a 9, seção 1). |
| 4. `/bmad-spec` | `[x]` Concluído em 2026-10-06: `docs/spec/spec-regras-negocio-dezi/` (kernel `spec-regras-negocio-dezi.md`, 9 companions por módulo, `glossario.md`, `decisoes-e-defeitos.md`, `a-confirmar.md`). Iniciativa em `_bmad-output/initiative-levantamento-regras-dezi/` (só o frontmatter; sem épicos nem histórias). |
| 5. Aprofundar pontos duvidosos | `[~]` Em andamento. Feitas: decisões D6 a D19 (2026-10-09) e verificação em código de ACE-6, ORG-1 e MOD-5. Faltam os itens de `a-confirmar.md` ainda abertos; os que mais bloqueiam: gatilho e consequências de `CANCELADA` (ACE-2, EST-1), conjunto de subordinados (ORG-4), escala de comentário `ponto=0` (GRA-6), quem cria e aprova PDI (PDI-1, PDI-3), e ETL/Pentaho e `commons` fora do repositório (RES-1). Ferramentas: `/bmad-party-mode`, `/bmad-advanced-elicitation`. |
| 6. `/bmad-architecture` | `[ ]` Não iniciado. Só depois de decidir os pontos a confirmar do passo 5. |
| 7. `/bmad-prd` (opcional) | `[ ]` Não iniciado. |
| 8. `/bmad-review` e `/bmad-walkthrough` | `[ ]` Não iniciados. |
| 9. `/bmad-ticket` (opcional) | `[ ]` Iniciativa criada, sem fatiamento. |

Para retomar, a fonte canônica agora é o spec (kernel + `decisoes-e-defeitos.md` + `a-confirmar.md`); estes documentos 00 a 09 guardam a evidência (arquivo e linha, consultas ao banco) citada pelos IDs `RN-xxx`.
