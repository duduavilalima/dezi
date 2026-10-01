# Levantamento de regras de negócio: plano e progresso

Sistema legado DEZI (`web/`). Documento de passagem entre conversas: mostra a ordem sugerida, o que já foi feito, o que falta e o contexto necessário para continuar.
Roteiro geral: [`../roteiro-bmad-levantamento-regras-negocio.md`](../roteiro-bmad-levantamento-regras-negocio.md) (passo 3, analista Mary, `/bmad-agent-analyst`).
Última atualização: 2026-10-01 (itens 1, 2 e 3 concluídos; próximo recomendado: 4).

## 1. Sequência sugerida e progresso

Legenda: `[x]` abordado · `[~]` parcial · `[ ]` a fazer.

| Ordem | Situação | Submódulo | Documento |
|---|---|---|---|
| 1 | `[x]` | Acesso e perfis | [`01-acesso-perfis.md`](01-acesso-perfis.md) |
| 2 | `[x]` | Cadastro organizacional | [`02-cadastro-organizacional.md`](02-cadastro-organizacional.md) |
| 3 | `[x]` | Modelo de competências | [`03-modelo-competencias.md`](03-modelo-competencias.md) |
| 4 | `[ ]` | Graduação e conceitos | `04-graduacao-conceitos.md` (a criar) |
| 5a | `[x]` | Avaliação: estados e transições | [`05a-avaliacao-estados-transicoes.md`](05a-avaliacao-estados-transicoes.md) |
| 5b | `[x]` | Avaliação: criação, agentes, participantes, pendências | [`05b-avaliacao-criacao-agentes-participantes.md`](05b-avaliacao-criacao-agentes-participantes.md) |
| 6 | `[x]` | Participantes e preenchimento | [`06-participantes-preenchimento.md`](06-participantes-preenchimento.md) |
| 7 | `[x]` | Resultados e gráficos | [`07-resultados-graficos.md`](07-resultados-graficos.md) |
| 8 | `[ ]` | PDI e feedforward | `08-pdi-feedforward.md` (a criar) |
| 9 | `[ ]` | Comunicação | `09-comunicacao.md` (a criar) |

O **item 5** foi abordado por inteiro (5a e 5b). Foi feito antes dos itens 1 a 4 por escolha do responsável; por isso algumas regras dependem de módulos ainda não levantados (ver seção 4).

Ordem recomendada daqui em diante: ~~6 → 7 → 1 → 2~~ (feitos) → ~~3~~ (feito) → **4 → 8 → 9**. O item 1 foi feito e esclareceu o que a tela permite (concluir, inativar e reativar avaliação; "Ajustar pesos" só ADM), mas **o servidor quase não impõe perfis** (só o painel de controle tem `@PreAuthorize`) e a permissão da transição genérica segue a confirmar (biblioteca `commons`). O item 4 tem agora um ponto de partida concreto: 07 mostra que a nota usa `peso_cargo_competencia`, `peso_cargo_tipo_competencia` e `ponto`, e que a escala de pontos varia de 0 a 120 por modelo.

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

### 4. Graduação e conceitos
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

### 8. PDI e feedforward
- **Ler:** `PDIAcaoServiceImpl` (704), `PDIAcaoCompetenciaDetalhamentoServiceImpl` (235), `FeedForwardRecebidoServiceImpl`, `FeedForwardMobileServiceImpl`, `FeedForwardRecebidoMobile`, `ApontamentoMobile`; enum `SituacaoPDI`; `PDIAcaoResource`, `FeedForwardRecebidoResource`; método `obterAvaliacoesPDI` de `AvaliacaoServiceImpl` (linha ~2028).
- **Extrair:** ciclo do PDI (estados e quem pode cada transição; já se vê em `PDIAcaoServiceImpl:130` que só o funcionário do PDI aprova para andamento e finaliza); vínculo com competências e resultados da avaliação; feedforward.

### 9. Comunicação
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

Pontos "a confirmar" abertos: 10 em 5a (seção 7), 12 em 5b (seção 11), 14 em 06 (seção 12), 14 em 07 (seção 9), 12 em 01 (seção 12), 14 em 02 (seção 12) e 12 em 03 (seção 11). A dúvida sobre precedência `AND`/`OR` da view v2 (5a, ponto 8) e a semântica do peso dos agentes (5b, RN-CRI-13) foram **resolvidas** em 07. Os mais importantes para decidir com o negócio:
- se `CANCELADA` é necessário no sistema novo (nunca usada);
- semântica de Av1..Av4 e do peso dos agentes;
- se revisor e dimensões ainda fazem parte do processo (0 usos);
- apagar avaliadores/participantes que já preencheram;
- fluxo home office (campanhas rápidas) e suas propriedades de configuração;
- encerramento: por data ou por estado.

## 4. Dependências entre o item 5 e os demais

| Regra de 5a/5b | Depende do item |
|---|---|
| Quem pode cancelar/concluir, quem pode liberar, `TipoPerfil.ADMINISTRADOR` | 1 (feito; ver 01, seções 6 e 10) |
| Situação funcional, gestor, subordinados, snapshot de cargo/departamento | 2 (feito; ver 02, seções 5 a 8) |
| Validação do cargo para liberar (12 pendências), snapshot de perguntas, dimensões | 3 (feito; ver 03, seções 5 a 9) |
| Modelo de graduação congelado na liberação | 4 |
| Preenchimento Av1..Av4, `preenchida_avN`, consenso, revisão | 6 |
| Peso dos agentes, percentual de preenchimento, views de resultado | 7 |
| PDI a partir da avaliação (`obterAvaliacoesPDI`) | 8 |
| Mensagens de liberação | 9 |

## 5. Como continuar em outra conversa

> Esta seção é **orientação ao usuário**: não contém regras de negócio nem resultado do levantamento. Quem só quer consultar as regras pode ignorá-la.

### 5.1 Como retomar

A nova conversa começa sem memória da anterior: ela só sabe o que está nos arquivos. Chame a analista Mary e aponte este arquivo:

```
/bmad-agent-analyst
```
e depois:
```
Continue o levantamento de regras de negócio a partir do item 4.
Leia docs/regras-negocio/00-plano-e-progresso.md e o AGENTS.md antes,
e use 05a e 05b como modelo de formato.
```

- Sem a referência a este arquivo, ela não sabe que o item 5 já foi feito, nem as decisões tomadas, nem como acessar o banco.
- O menu da Mary (brainstorming, pesquisa de mercado, product brief e outros) não tem item para este trabalho; a análise é conduzida na conversa, lendo o código. Se a sua mensagem já disser o que você quer, ela pula o menu e começa direto.
- Contexto a carregar: `AGENTS.md` (política: só código e banco valem; regra sem evidência nos dois lados vai como "a confirmar"; não refatorar nem corrigir o legado), este arquivo e `05a`/`05b` como modelo de formato e nível de detalhe.

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

Os documentos desta pasta estão na branch `feature/bmad_init` e ainda **não foram commitados** (commit só a pedido do responsável).

## 6. Ideias de etapas posteriores (do roteiro geral)

Depois dos módulos: `/bmad-spec` (condensar em spec com arquivo central e um de apoio por módulo), `/bmad-advanced-elicitation` e `/bmad-party-mode` para os pontos "a confirmar", `/bmad-architecture` (onde vivem as regras: camadas, integrações, banco), `/bmad-review` e `/bmad-walkthrough` para validar com especialistas de negócio.
