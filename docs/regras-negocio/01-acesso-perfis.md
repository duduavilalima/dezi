# Acesso e perfis: autenticação, senhas, bloqueios, permissões e auditoria

Módulo: `web/` (backend) e `webapp/` (onde está a maior parte do controle de acesso) · Submódulo 1 do roteiro.
Escopo: login, token, primeiro acesso, troca e recuperação de senha, bloqueios (`SituacaoAuth`), perfis (`TipoPerfil`) e o que cada um pode fazer, escopo de dados por perfil, auditoria (`@LogRequired`), login técnico no Pentaho.
Fora de escopo: cadastro do funcionário e hierarquia (item 2), regras de cada tela de negócio (itens 3 a 9; aqui só o que decide quem pode).

Convenção: **[código]** = evidência no código; **[banco]** = confirmado no Postgres (consulta somente leitura de 2026-10-01, usuário `dezi_leitura`, dados até 2024-03-20; cópia ou produção: a confirmar); **[migração]** = só em migração ou script; **A confirmar** = sem evidência suficiente. Prefixo das regras: `RN-ACE`.
Abreviações: `FuncSvc` = `web/services/src/main/java/br/com/fasolti/service/impl/FuncionarioServiceImpl.java`; `Auth` = `web/rest/src/main/java/br/com/fasolti/avd/rest/AuthResource.java`; `LogAuthSvc` = `.../LogAuthServiceImpl.java`; `UserTO` = `web/services.client/src/main/java/br/com/fasolti/avd/transfer/UserTO.java`; caminhos do `webapp/` são a partir de `webapp/`.

**Limite do levantamento:** a biblioteca `br.com.fasolti.commons` (token, `UserDetailsServiceImpl`, `BaseSecurityService`, endpoint genérico de transição) não está acessível. O que ela faz só se deduz pelos usos. Os pontos que dependem dela estão marcados.

## 1. Glossário

| Termo | Significado | Evidência |
|---|---|---|
| Usuário | Um `Funcionario`. Não há entidade `User` (`//TODO Criar a entidade User`) | `UserResource.java:18`, `UserTO` |
| Login (username) | Texto `empresaId%matrícula`. A empresa faz parte da identidade | `UsernameParser.java:7-15` |
| Perfil | `TipoPerfil`, um por funcionário (coluna `id_tipo_perfil_uso`): `ADMINISTRADOR`(3), `FUNCIONARIO`(4), `CONSULTOR_RH`(14), `GESTOR_RH`(15), `GESTOR_AREA`(16) | `TipoPerfil.java`; `Funcionario.java:87-89` |
| Autoridade (role) | `ROLE_<perfil>`, uma só por usuário | `UserTO:184-188` |
| Primeiro acesso | Flag `primeiro_acesso`: senha é temporária e a troca é obrigatória na tela | `Funcionario.java`, `login.controller.js:36-45` |
| `SituacaoAuth` | Tipo de registro de `log_auth`: `LOGIN_OK`(1), `AUTENTICACAO_INVALIDA`(2), `USUARIO_SUSPENSO`(3), `USUARIO_BLOQUEADO`(4), `IP_BLOQUEADO`(5) | `SituacaoAuth.java` |
| Consenso (`seclogin`) | Reautenticação para confirmar uma ação sensível da avaliação | `Auth:149-165` |

## 2. Modelo de dados envolvido

- `funcionario`: `matricula`, `senha` (hash), `primeiro_acesso`, `id_tipo_perfil_uso`, `id_situacao` (ATIVA 1 / INATIVA 98), `id_situacao_funcional`, `id_empresa`, `id_cargo`, `id_departamento`, `email_funcional`. **[banco]** único `(id_empresa, matricula)` (`ix_funcionario_empresa_matricula`); **não** há único de e-mail nem `CHECK`; não há mais colunas de tentativas/bloqueio (removidas em V2.0_0048 **[migração]**, confirmado **[banco]**).
- `log_auth`: `user_name`, `data`, `ip`, `id_situacao`. Criada em V2.0_0049 **[migração]**.
- `log_entity`: `id_entidade`, `nome_entidade`, `usuario`, `data_criacao`, `objeto` (JSON do corpo), `uri`. **Não tem migração em `web/`**: só o script de deploy `deploy/servers/dezi/db-scripts/after-restore.sql.d/10-criar-tabelas-log.sql` a cria (junto com `log_auth`, `IF NOT EXISTS`); o `dump.sh` exclui os dados `public.log_*`. **[migração]**
- `parametros` (chaves `notificacao.*.funcionario.*`, `menus.visibilidade.porEmpresaId.<id>`), `bi_parametro_usuario` (ponte para o BI), `empresa` (`host`, `versao_bi`, `identificador`).
- **[banco] As tabelas `log_auth` e `log_entity` não existem na instância consultada** (só as sequências `seq_log_auth` e `seq_log_entity`). Logo, nenhuma regra de bloqueio ou auditoria pôde ser validada com dados reais. Causa possível: o script `after-restore` não foi aplicado nesta cópia (a confirmar).

## 3. Autenticação

- **RN-ACE-01** A identidade é empresa + matrícula. O login monta `empresaId%matrícula`; a matrícula é única por empresa e comparada sem diferenciar maiúsculas. **[código]** `UsernameParser:13-15`, `SecurityServiceImpl:30`, `FuncionarioRepository.findForLogin`. **[banco]** índice único.
- **RN-ACE-02** Só funcionário com situação `ATIVA` autentica (`findForLogin` filtra `ATIVA`; `UserTO.isEnabled`). `isAccountNonLocked`, `isCredentialsNonExpired` e `isAccountNonExpired` retornam sempre `true` (`TODO Definir o critério`). **[código]** `UserTO:155-176`. O que acontece com matrícula inexistente (o serviço mapeia `null`): **a confirmar**.
- **RN-ACE-03** A situação funcional não bloqueia o login por si só. Só `DESLIGADO` desativa: ao atualizar, a situação passa a `INATIVA` e a data de desligamento é preenchida se vazia. **[código]** `FuncSvc:264-271`. Inativar sem ser `DESLIGADO` é rejeitado (`validation.invalidInactivation`; o bloco aparece duplicado em `FuncSvc:286-296`). **[banco]** os 261 funcionários com situação funcional 11 (DESLIGADO) estão todos `INATIVA`; porém 18 `INATIVA` estão "em atividade" (dados anteriores à regra) e 7 `ATIVA` estão em recesso/licença (podem logar).
- **RN-ACE-04** **Lista de empresas do login.** `GET /auth/empresas` (e `/auth/mobile/empresas`) é público. Em ambiente `production`, filtra pelo host da requisição (`Origin`, depois `Referer`, depois `Host`); no mobile remove o sufixo `mobile` do primeiro rótulo do host e, se nenhuma empresa tiver o host, lista **todas** (apps híbridos não enviam `Origin`). Na web, host sem empresa devolve lista vazia. Fora de `production`, lista todas as ativas. **[código]** `Auth:173-268`, `EmpresaServiceImpl:96-129`. **[banco]** 19 empresas, todas ativas.
- **RN-ACE-05** **Ordem do login** (`POST /auth/login`): (1) bloqueio de IP, resposta 404; (2) bloqueio do usuário, resposta 403; (3) autenticação por senha com BCrypt; falha grava `AUTENTICACAO_INVALIDA` e responde 401; (4) sucesso grava `LOGIN_OK` e devolve token (`TokenUtils.createAPIToken`, da biblioteca externa). Erro interno nas checagens responde 500. A tela traduz 401, 403 e 404 em mensagens distintas. **[código]** `Auth:121-147`, `login.controller.js` (`loginError`). Validade do token e algoritmo: **a confirmar** (biblioteca `commons`).
- **RN-ACE-06** Sessão sem estado (`create-session="stateless"`); o token vai em cada chamada e o filtro `AuthenticationTokenProcessingFilter` (externo) o processa. **[código]** `applicationContext-security.xml`. CORS aceita qualquer origem com credenciais (`allowedOrigins=*`, `allowCredentials=true`, `web.xml`): **a confirmar** se há outra camada (proxy) que restringe.
- **RN-ACE-07** **Destino pós-login (só na tela).** `primeiroAcesso` verdadeiro ou nulo: abre a troca obrigatória de senha e refaz o login com a nova senha; depois, se `preencheFormacaoEIdiomas` é verdadeiro ou nulo, vai à tela de formações e idiomas; senão `ADMINISTRADOR` e `GESTOR_RH` vão a acompanhamentos e os demais perfis às avaliações. **[código]** `login.controller.js:36-60`. **[banco]** `primeiro_acesso`: verdadeiro 196, falso 567, nulo 3 (os 3 são administradores).
- **RN-ACE-08** **Defeito provável de redirecionamento.** Quem já está logado e abre a tela de login deveria ir ao destino do perfil, mas o teste compara a lista de papéis com `role.authority` de uma string (`roles.some(role => roles.indexOf(role.authority) >= 0)`), então é sempre falso e todos vão às avaliações. **[código]** `app.config.js:15,27-32`. **A confirmar** se a rota que cai nesse caminho é usada.
- **RN-ACE-09** **Reautenticação do consenso** (`POST /auth/seclogin`): valida empresa, matrícula e senha e devolve novo token; erro vira `password-not-match`. **Não** verifica bloqueio e **não** grava log, e como `/auth/**` é público, serve para testar senhas sem limite de tentativas. **[código]** `Auth:149-165`, `applicationContext-security.xml`. Suspeito; a corrigir no sistema novo.
- **RN-ACE-10** **Perfil de desenvolvimento** (profile `development`): segurança desligada para todas as rotas, codificador de senha sem criptografia e usuário fixo (`debug.user.*`). O padrão do `web.xml` é `production`. **[código]** `applicationContext-security-dev.xml`, `SecurityServiceDebug`, `web.xml`. Não é regra de negócio; não levar ao sistema novo.
- **RN-ACE-11** `GET /user/logged` devolve o usuário (sem o hash da senha) com a configuração pessoal; se não existe, cria com idioma `pt_BR` e fonte `medium`; se existe, apenas apara o idioma ou o completa. É uma leitura com escrita. **[código]** `UserResource:33-58`. `GET /user/roles` devolve as autoridades usadas para esconder menus e botões.

## 4. Bloqueios (`SituacaoAuth`)

Constantes fixas no código: `USERNAME_ERROR_WINDOW=10` min, `MAX_USERNAME_ERRROS=5`, `IP_ERROR_WINDOW=20` min, `MAX_IP_ERRORS=25` (`LogAuthSvc:24-27`).

- **RN-ACE-12** **Bloqueio por usuário (resposta 403).** Bloqueia se, nos últimos 10 minutos, existe um registro `USUARIO_BLOQUEADO` para o usuário **ou** 5 ou mais `AUTENTICACAO_INVALIDA`. Cada tentativa bloqueada grava um novo `USUARIO_BLOQUEADO`, e esse registro é contado na janela seguinte. **[código]** `LogAuthSvc:41-56,69-81`. Efeito deduzido: **enquanto o usuário continua tentando, o bloqueio se renova**; ele só expira 10 minutos depois da última tentativa. A senha correta também é recusada durante o bloqueio.
- **RN-ACE-13** **Bloqueio por IP (resposta 404, verificada antes).** Bloqueia se o par (IP, usuário) tem mais de 25 registros `AUTENTICACAO_INVALIDA` ou `USUARIO_BLOQUEADO` nos últimos 20 minutos; grava `IP_BLOQUEADO` (que não conta). Como as tentativas bloqueadas por usuário também gravam `USUARIO_BLOQUEADO`, em ~26 tentativas a resposta passa de 403 para 404. O nome "IP" é enganoso: o filtro inclui o usuário. **[código]** `LogAuthSvc:58-67,83-96`. Atrás de proxy, `request.getRemoteAddr()` pode ser o IP do proxy: **a confirmar**.
- **RN-ACE-14** Login com sucesso **não zera** as falhas: 4 falhas, 1 sucesso e 1 falha bloqueiam. **[código]** a contagem só considera `AUTENTICACAO_INVALIDA`/`USUARIO_BLOQUEADO`.
- **RN-ACE-15** `USUARIO_SUSPENSO` existe no enum mas nunca é gravado nem lido. **[código, por ausência]** Não há bloqueio manual nem desbloqueio por administrador; o desbloqueio é só o decurso de tempo.
- **RN-ACE-16** O mecanismo antigo de bloqueio na tabela `funcionario` (tentativas e data de bloqueio) foi removido. **[migração]** V2.0_0048; **[banco]** colunas inexistentes.
- **Sem validação no banco** (seção 2): volume, janelas e bloqueios reais **a confirmar**.

## 5. Senhas

- **RN-ACE-17** Senhas guardadas com BCrypt. **[código]** `applicationContext-security.xml`. **[banco]** as 766 senhas têm formato `$2a/$2b/$2y`; nenhuma nula ou em texto.
- **RN-ACE-18** **Criação do funcionário:** gera senha aleatória temporária, grava o hash, marca `primeiroAcesso=true` e cria a configuração pessoal. O e-mail com a senha só sai se o parâmetro `notificacao.email.funcionario.novo` for `true`. **[código]** `FuncSvc:224-245,345-361`. **[banco]** esse parâmetro é `false`: **hoje o funcionário novo não recebe a senha automaticamente**; ela chega pelo envio de boas-vindas (RN-ACE-21) ou por "esqueci minha senha".
- **RN-ACE-19** **Esqueci minha senha** (`POST /auth/forgotpassword`, público): recebe e-mail e empresa; se há funcionário `ATIVO` com esse e-mail na empresa, gera nova senha, **grava na hora** (a anterior deixa de valer), marca `primeiroAcesso=true` e envia por e-mail (parâmetro `notificacao.email.funcionario.esqueci.senha`, **[banco]** `true`). Se não existe, erro `validation.not-found-email` (revela quais e-mails existem). **[código]** `FuncSvc:368-392`, `Auth:167-171`. Consequências: qualquer pessoa que conheça o e-mail invalida a senha de outra; a senha trafega em texto no e-mail. **[banco]** há 2 casos de e-mail duplicado entre ativos da mesma empresa (a busca por e-mail passaria a ser ambígua; `FuncSvc:282-284` impede novos).
- **RN-ACE-20** **Troca de senha** (`POST /funcionario/trocarsenha`): exige senha atual correta, nova senha igual à confirmação, e campos não nulos; grava o novo hash, zera `primeiroAcesso` e, se o parâmetro `notificacao.email.funcionario.troca.senha` é `true` (**[banco]** `true`), envia e-mail **com a nova senha em texto**. Não há regra de tamanho, complexidade, histórico nem expiração. **[código, por ausência]** `FuncSvc:394-461`. O usuário alvo vem do corpo (`trocaSenhaTO.getFuncionario().getId()`) e não do usuário logado (há `FIXME` em `FuncionarioResource:53`); a exigência da senha atual do alvo limita o abuso. **A confirmar.**
- **RN-ACE-21** **Boas-vindas com nova senha** (`sendEmailWelcomeMessage`): para um colaborador ou para todos de um perfil na empresa do usuário logado, gera nova senha, marca `primeiroAcesso=true`, substitui `#LOGIN#`, `#PASSWORD#` e `#URL#` no texto e envia. **[código]** `EmailControleServiceImpl:210-245`. Quem pode acionar: **a confirmar** (endpoint não lido; ver item 9). Existe variante para a pesquisa Home Office que só gera senha se `primeiroAcesso` for verdadeiro (`MensageriaServiceImpl:164-196`; o fluxo nunca foi usado, 05a 7.2).
- **RN-ACE-22** O envio de SMS de senha não existe: o código só registra aviso (`FIXME`). Além disso, a leitura do parâmetro de SMS de novo cadastro concatena `"/" + empresaId` ao valor antes de converter para booleano, o que dá sempre `false`. **[código]** `FuncSvc:354-360`. **[banco]** os parâmetros de SMS estão `true`, sem efeito.

## 6. Perfis e permissões

- **RN-ACE-23** Cada funcionário tem **um** perfil e uma única autoridade `ROLE_<perfil>`; o código admite que isso é provisório (`FIXME` de modelar funcionário x perfil). **[código]** `UserTO:184-188`. **[banco]** (funcionários, ativos entre parênteses): `ADMINISTRADOR` 19 (18), `FUNCIONARIO` 599 (361), `CONSULTOR_RH` 1 (1), `GESTOR_RH` 38 (27), `GESTOR_AREA` 109 (80). Todos têm cargo e departamento.
- **RN-ACE-24** **No servidor quase não há controle por perfil.** A única anotação de permissão do `web/` é `@PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR','ROLE_GESTOR_RH','ROLE_CONSULTOR_RH')")` na classe `PainelControleResource`. Todas as demais rotas exigem apenas estar autenticado. As rotas `/auth/**`, `/homeofficebrasil/**` e `/municipio/**` são públicas. Os `import` de `PreAuthorize` em `CompetenciaDetalhamentoCalibrahintResource` e `CursoGraduacaoResource` não são usados. **[código]** `PainelControleResource.java:19`, `applicationContext-security.xml`. Consequência: **a matriz abaixo (tela) não é imposta pela API**; quem tem token de qualquer perfil pode chamar os endpoints. O endpoint genérico `PUT /<recurso>/{id}/transition` vem da biblioteca externa: a permissão dele é **a confirmar**.
- **RN-ACE-25** **Matriz de menus e botões (só na tela).** Definida em `roles` de cada módulo e em `requiredRoles`/`editRoles`. **[código]**

| Área | Menu visível para | Pode criar/editar |
|---|---|---|
| Cadastros (unidade, tipo de competência, cargo, departamento, funcionário, modelo de graduação) | ADM, CONSULTOR_RH, GESTOR_RH | ADM, GESTOR_RH (CONSULTOR_RH só consulta) |
| Cadastros (dimensão, competência, formações e idiomas) | ADM, CONSULTOR_RH, GESTOR_RH | ADM, GESTOR_RH (competência e dimensão: parte dos botões só ADM) |
| Campanha e avaliação (criar, liberar, concluir, inativar) | ADM, CONSULTOR_RH, GESTOR_RH | Criar: ADM, GESTOR_RH; os itens do menu de ação da avaliação não têm trava por perfil |
| Acompanhamentos | ADM, CONSULTOR_RH, GESTOR_RH, GESTOR_AREA | Editar: ADM, GESTOR_RH |
| PDI | os 5 perfis | Editar: ADM, GESTOR_RH, GESTOR_AREA; só ver: CONSULTOR_RH, FUNCIONARIO; relatório PDI do RH: ADM, GESTOR_RH |
| Administração (parâmetros, host, mensagens) | só ADM | só ADM |
| Importação de planilhas (funcionários, competências) | tela de cadastro | só ADM (`checkImportPermit`) |
| Botão "Ajustar pesos" e "detalhes" da avaliação | só ADM | só ADM |
| Liberar avaliação com a campanha já "em liberação" | todos que veem a ação | só ADM não fica desabilitado |

  Fontes: `cadastros.module.js:26`, `*.route.js` (`ROLES_VIEW`/`ROLES_EDIT`), `acompanhamentos.route.js:10-11`, `pdi.module.js:24`, `pdi.route.js:8-9`, `administracao.module.js:27`, `aba.resumo.view.html:326`, `campanha.view.html:213`. Visibilidade de menu por empresa pode ser ajustada pelo parâmetro `menus.visibilidade.porEmpresaId.<id>` (`onlyRoles`/`exceptRoles`). **[banco]** 3 empresas têm esse parâmetro (100, 106, 115), todas restringindo "relatórios" e uma restringindo "acompanhamentos" a ADM e GESTOR_RH.
- **RN-ACE-26** **Regras de permissão que o servidor impõe.** **[código]**
  - Funcionário com perfil `ADMINISTRADOR` só pode ser alterado por administrador (`validation.permission`). `FuncSvc:308-315`. Não há checagem de quem pode **definir** o perfil `ADMINISTRADOR` em outro funcionário; a tela só oculta essa opção da lista (`funcionario.view.js:210`). Escalada de privilégio pela API: **suspeito, a confirmar**.
  - A lista de funcionários elegíveis a uma avaliação exclui administradores. `FuncSvc:602-610`.
  - **PDI:** `APROVADO` para `EM_ANDAMENTO` e `EM_ANDAMENTO` para `FINALIZADO` só pelo próprio funcionário do PDI (`validation.not-employee`); qualquer outra alteração e a exclusão só por `ADMINISTRADOR`, `GESTOR_AREA` e `GESTOR_RH` (`validation.permission`). A criação não valida perfil (trecho comentado). `PDIAcaoServiceImpl:117-153`. `CONSULTOR_RH` não edita PDI.
  - **Acompanhamento:** `FUNCIONARIO` recebe `validation.not-allowed`; `GESTOR_AREA` vê só participantes do seu departamento; `ADMINISTRADOR`, `GESTOR_RH` e `CONSULTOR_RH` veem toda a empresa. `PainelControleServiceImpl:768-781`.
  - **Empresas:** só `ADMINISTRADOR` lista todas as empresas (os demais recebem lista vazia). `EmpresaServiceImpl:140-151`.
  - **Campanhas:** `ADMINISTRADOR` e `GESTOR_RH` veem todas as da empresa; os demais veem um subconjunto filtrado por avaliações (`CampanhaServiceImpl:127-150`; o restante do filtro **a confirmar**).
  - **Liberação:** a mensagem "liberação iniciada" não é enviada quando quem libera é `ADMINISTRADOR` (05a RN-EST-05) e a mensagem "liberação concluída" vai para os `GESTOR_RH` ativos da empresa (`MensageriaServiceImpl:300-312`).

## 7. Escopo de dados por perfil (`getServiceFilter`)

Cada serviço injeta um filtro obrigatório conforme o perfil do usuário logado. **[código]** (linhas em `.../service/impl/`).

| Serviço | ADMINISTRADOR, GESTOR_RH | CONSULTOR_RH | GESTOR_AREA | FUNCIONARIO (padrão) |
|---|---|---|---|---|
| Funcionário `FuncSvc:157-187` | empresa do usuário | empresa do usuário | seu departamento ou o departamento pai (ver nota) | só ele mesmo |
| Cargo `CargoServiceImpl:65-90` | empresa | empresa | cargos do seu departamento | só o seu cargo |
| Cargo x competência `CargoCompetenciaServiceImpl:52-75` | empresa | empresa | seu departamento | seu cargo |
| Departamento `DepartamentoServiceImpl:55-80` | empresa | empresa | só o seu | só o seu |
| PDI `PDIAcaoServiceImpl:78-102` | empresa | empresa | departamento (ver nota) | só os seus |
| Apontamento (usuário é o avaliador) `ApontamentoFuncionalServiceImpl:48-65` | empresa | só os que ele fez | só os que ele fez | só os que ele fez |
| Feedforward recebido (usuário é o avaliado) `FeedForwardRecebidoServiceImpl:48-65` | empresa | só os dele | só os dele | só os dele |
| Participantes de PDI `AvaliacaoParticipanteServiceImpl:1150-1175` | empresa | empresa | avaliado ou avaliador no seu departamento | avaliado ou avaliador ele mesmo |
| Dados de preenchimento `DadoPreenchimentoServiceImpl:112-130` | tudo | tudo | seu departamento e descendentes | sem filtro de perfil |
| Competência de avaliação (mobile) `AvaliacaoCompetenciaMobileServiceImpl:44-64` | empresa | **sem filtro** | **sem filtro** | **sem filtro** |

- **RN-ACE-27** **`CONSULTOR_RH` enxerga só a empresa do próprio usuário** (`//TODO Ver todas as empresas da qual ele é gestor` repetido em vários serviços); a ideia de consultor com várias empresas não está implementada. **[código]** **[banco]** existe 1 usuário com esse perfil.
- **RN-ACE-28** **Nota sobre o filtro de `GESTOR_AREA` em Funcionário e PDI.** O código testa `qFuncionario.departamento.departamentoPai != null` (um caminho de consulta, nunca nulo), então sempre aplica "departamento do gestor ou departamento pai igual ao do gestor" (um nível de filhos). Já `DadoPreenchimento` e o painel usam todos os descendentes (`obterDepartamentosDescendentes`). Alcance diferente por tela: **suspeito, a confirmar**. **[código]** `FuncSvc:171-180`, `PDIAcaoServiceImpl:90-97`.
- **RN-ACE-29** O filtro de competência de avaliação mobile retorna `ServiceFilter.create(q, null)` para todos os não administradores (`TODO: Como fazer o filtro de funcionario aqui?`). **Suspeito, a confirmar** se expõe dados de outras empresas.
- **RN-ACE-30** **Quem é "gestor".** Ao gravar um funcionário, o vínculo `unidade_departamento.gestor` do par (unidade, departamento) é apagado e recriado se o perfil é `GESTOR_AREA` ou `GESTOR_RH`; `departamento_gestor` é apagado e recriado só para `GESTOR_AREA` (incluindo os departamentos descendentes). **[código]** `UnidadeDepartamentoServiceImpl:38-55`, `DepartamentoGestorServiceImpl:47-70`. **[banco]** os 88 gestores distintos de `departamento_gestor` têm todos perfil `GESTOR_AREA` e os 80 `GESTOR_AREA` ativos têm vínculo; em `unidade_departamento` há 5 gestores com perfil `FUNCIONARIO` (3 ativos): resíduo de mudança de perfil ou de departamento, **a confirmar** (detalhar no item 2).
- **RN-ACE-31** **Ponte para o BI.** `criarParametros` grava em `bi_parametro_usuario` o funcionário, o perfil (código), a empresa e a campanha; `nomeGestor` é o nome do usuário se `GESTOR_AREA`, senão `%`; `nomeFuncionario` é o nome do usuário se `FUNCIONARIO`, o nome do funcionário pedido se não há campanha, senão `%`. A chave é o SHA-256 de `milissegundos + idFuncionario`, previsível por quem conhece o id e o horário. **[código]** `BiParametroServiceImpl:40-75`. **[banco]** linhas por perfil: 3 → 234, 4 → 2.589, 15 → 691, 16 → 1.082; nenhum do perfil 14. Já registrado em 07 (RN-RES-23); aqui só a parte de acesso.

## 8. Auditoria

- **RN-ACE-32** **`log_auth`:** só `/auth/login` grava (`LOGIN_OK`, `AUTENTICACAO_INVALIDA`) e as checagens de bloqueio (`USUARIO_BLOQUEADO`, `IP_BLOQUEADO`). `seclogin`, "esqueci minha senha" e troca de senha não gravam. **[código]**
- **RN-ACE-33** **`log_entity` (`@LogRequired`).** Depois de concluída a requisição `POST`, `PUT` ou `DELETE` de um método anotado, `LogInterceptor` grava: nome da entidade, `id` do corpo, matrícula do usuário, data, o **corpo JSON inteiro** e a URI. Entidades auditadas: `Avaliacao` (8 métodos de `AvaliacaoResource`, incluindo liberar e ajustar pesos), `Participante` (`AvaliacaoResource:138`, `AvaliacaoParticipanteResource:36,42`), `CompetenciaPreenchimento` (`ParticipanteResource:89,99`), `AvaliacaoCompDet` (`ParticipanteResource:142`), `ApontamentoMobile` e `DetalhamentoMobile` (`MobileResource:60,100`) e `TrocaDeSenha` (`FuncionarioResource:52`). **[código]** `LogInterceptor:45-129`.
- **RN-ACE-34** Limites da auditoria **[código]**: roda em `afterCompletion`, então também registra requisições que falharam; falha ao gravar só vai para o log da aplicação; o `id` é lido com conversão para `Integer`, o que falha se o corpo não tem `id` ou ele é grande (nesse caso nada é gravado); **cadastros (funcionário, perfil, cargo, departamento, competência) e campanha não são auditados**, então a mudança de perfil de alguém não deixa trilha.
- **RN-ACE-35** **Defeito de segurança provável:** a troca de senha é auditada com o corpo completo (`senha`, `novaSenha`, `confirmarSenha` em texto, mais o objeto do funcionário) em `log_entity.objeto`. **[código]** `FuncionarioResource:52`, `LogInterceptor:111-126`, `TrocaDeSenha.java`. Não verificável no banco (tabela ausente). A evitar no sistema novo.

## 9. Integração com o BI (Pentaho)

- **RN-ACE-36** `GET /pentaho/login` (qualquer usuário autenticado) faz um login técnico no Pentaho (`POST /pentaho/j_spring_security_check`) com credencial de serviço (`DEZI_BI_USER`/`DEZI_BI_PASSWORD`) e devolve ao navegador o servidor e o `JSESSIONID`. O usuário Pentaho é o base; se a empresa tem `versao_bi = 1` e `identificador` preenchido, vira `<base>_<identificador>`. Resposta 302 com `login_error=1` é credencial inválida; `login_error=2` (sessão reaproveitada) repete uma vez; sem `Set-Cookie` `JSESSIONID` é erro. **[código]** `PentahoLoginServiceImpl:39-124`, `PentahoResource`. **[banco]** 1 empresa com `versao_bi=1` e 18 com `2`. `versao_bi` nulo geraria erro (`versao.equals(1)`): **a confirmar**.
- **RN-ACE-37** A sessão Pentaho é da conta técnica, não do usuário; o recorte por perfil e funcionário é feito pelos parâmetros de `bi_parametro_usuario` (RN-ACE-31). Ver 07 (RN-RES-23).

## 10. Pendências de 05a/05b que este item fecha ou corrige

- **Quem dispara `CONCLUIDA` (05a 7.1-1): resolvido no que a tela mostra.** Na tela da campanha, o menu de ação da avaliação oferece "Concluir" para avaliação `LIBERADA` e chama `PUT /avaliacao/{id}/transition` com `CONCLUIDA`; o mesmo mecanismo faz `LIBERADA` → `INATIVA` ("Inativar") e `INATIVA` → `LIBERADA` ("Ativar"). Quem vê a tela: `ADMINISTRADOR`, `CONSULTOR_RH` e `GESTOR_RH`; os itens do menu não têm trava por perfil na tela (RN-ACE-25). No servidor não há verificação de perfil (RN-ACE-24; o endpoint de transição é da biblioteca externa). **[código]** `campanha.view.js:118-148`, `campanha.view.html:205-244`, `app.config.js` (ação `transition`). **Quais perfis de fato concluem** (provavelmente `ADMINISTRADOR` e `GESTOR_RH`; `CONSULTOR_RH` não deveria): **a confirmar com o negócio**. **[banco]** 50 `CONCLUIDA`, 10 atualizadores distintos (05a 7.2): não permite separar perfis sem cruzar `data_atualizacao`/usuário, o que exigiria ler dados pessoais.
- **`CANCELADA` não tem caminho na tela.** Não há item de menu "Cancelar"; existe a ação `cancelar` em `services/avaliacao.js:58-63`, mas **não há endpoint `/avaliacao/{id}/cancelar` no `web/`** e nenhum chamador. **[código]** `campanha.view.html:205-244`, busca no `rest/`. Junto com 0 linhas no banco, reforça que `CANCELADA` é estado sem uso (decisão a tomar para o sistema novo).
- **Correção de 05a RN-EST-19** ("não há caminho para reativar `INATIVA`"): **há** na tela, "Ativar" faz `INATIVA` → `LIBERADA` pela transição genérica (`campanha.view.js:140-146`). O que a transição faz com as datas limite (que a inativação por `update` força para ontem, RN-EST-17) depende da biblioteca externa: **a confirmar**.
- **05a seção 5, `emLiberacao`:** a tela desabilita "Liberar" quando a campanha está em liberação, **exceto para `ADMINISTRADOR`** (`campanha.view.html:213`). O servidor não bloqueia.
- **Botão "Ajustar pesos"** (07): só aparece para `ADMINISTRADOR` (`aba.resumo.view.html:326`); o endpoint `POST /avaliacao/{id}/ajustarpesos` não verifica perfil e não usa o `id` (`AvaliacaoResource:150-155`).

## 11. Validação no banco (evidência **[banco]**)

Consulta em 2026-10-01 com `dezi_leitura`; só contagens agregadas, nenhum dado pessoal.

| Consulta | Resultado | Efeito |
|---|---|---|
| `funcionario` por perfil e situação | ver RN-ACE-23 | Confirma os 5 perfis; nenhum valor fora do enum |
| Formato das senhas | 766 de 766 BCrypt | Confirma RN-ACE-17 |
| `primeiro_acesso` | true 196, false 567, nulo 3 | O código trata nulo como "primeiro acesso" (RN-ACE-07) |
| Únicos e `CHECK` em `funcionario` | único `(empresa, matrícula)`; sem `CHECK`; sem único de e-mail | Confirma RN-ACE-01 e a duplicidade de e-mail (RN-ACE-19) |
| E-mails duplicados entre ativos na empresa | 2 casos | "Esqueci minha senha" pode falhar para eles |
| Parâmetros `notificacao.*` | e-mail novo: `false`; esqueci, troca, boas-vindas: `true`; SMS: `true` (sem implementação) | RN-ACE-18, 19, 20, 22 |
| Parâmetros `menus.visibilidade.*` | empresas 100, 106 e 115 | RN-ACE-25 |
| `bi_parametro_usuario` por perfil | 3, 4, 15 e 16; nenhum do 14 | RN-ACE-31 |
| `departamento_gestor` e `unidade_departamento` | ver RN-ACE-30 | Vínculo de gestor coerente com o perfil, com 5 resíduos |
| `log_auth`, `log_entity` | **tabelas inexistentes** | Bloqueio e auditoria sem evidência no banco |
| Versão do esquema | 93 migrações (05a 7.2) | `log_auth` consta como aplicada em V2.0_0049, mas a tabela não está na instância: **o esquema real diverge das migrações** neste ponto |

## 11.1 Decisões do responsável (2026-10-01)

1. **`notificacao.email.funcionario.novo = false` é intencional** (RN-ACE-18): funcionário novo não recebe a senha por e-mail; ela é entregue pelas boas-vindas (RN-ACE-21) ou por "esqueci minha senha". Não é lacuna.
2. **`CANCELADA` entra no sistema novo.** O legado não tem caminho para cancelar (sem item de menu, sem endpoint, 0 usos); o sistema novo precisa definir o gatilho, o perfil autorizado e as consequências (participantes, preenchimentos, mensagens), que o legado deixou em `TODO` (05a RN-EST-21).
3. **Quem pode concluir, inativar, reativar e cancelar uma avaliação: `ADMINISTRADOR` e `GESTOR_RH`**, os mesmos perfis para as quatro ações (cancelar não é mais restrito que concluir). **`CONSULTOR_RH` tem acesso somente de leitura** (regra do sistema novo; o legado deixava o consultor ver essas ações na tela, RN-ACE-25). `GESTOR_AREA` e `FUNCIONARIO` não executam nenhuma delas. No sistema novo a regra deve ser imposta no servidor, não só na tela (RN-ACE-24).

## 12. Pontos a confirmar (resumo)

1. Quais perfis podem concluir, inativar e reativar avaliação, e se o servidor impõe (transição genérica em biblioteca externa). Seção 10; RN-ACE-24.
2. Se `CANCELADA` é necessário no sistema novo (sem caminho na tela, sem endpoint, 0 usos). Seção 10.
3. Efeito da transição genérica `INATIVA` → `LIBERADA` sobre as datas limite. Seção 10.
4. Validade e algoritmo do token; matrícula inexistente no login; CORS irrestrito com credenciais. RN-ACE-02, 05, 06.
5. Bloqueio por IP atrás de proxy; volumes reais de `log_auth`; por que as tabelas de log não existem na instância. RN-ACE-13, seção 2.
6. Escalada de privilégio: quem pode definir o perfil `ADMINISTRADOR` pela API. RN-ACE-26.
7. Alcance do `GESTOR_AREA` (um nível ou todos os descendentes) e filtro ausente no mobile. RN-ACE-28, 29.
8. Quem aciona o envio de boas-vindas em massa. RN-ACE-21.
9. Resíduos de gestor com perfil `FUNCIONARIO`. RN-ACE-30.
10. Se o redirecionamento de quem já está logado (RN-ACE-08) é caminho usado.
11. Se `CONSULTOR_RH` deveria acessar mais de uma empresa. RN-ACE-27.
12. Resto do filtro de campanhas para perfis que não são `ADMINISTRADOR` nem `GESTOR_RH`. RN-ACE-26.

Defeitos do legado a levar como requisito do **sistema novo** (não corrigir o legado; política do `AGENTS.md`): autorização por perfil também no servidor; `seclogin` com bloqueio e log; senha nunca enviada nem registrada em texto; "esqueci minha senha" por link com expiração e sem revelar se o e-mail existe; política de senha; desbloqueio e suspensão manuais; auditoria dos cadastros (principalmente perfil); chave do BI imprevisível.

## 13. Fontes lidas

`AuthResource.java`, `UserResource.java`, `PentahoResource.java`, `FuncionarioResource.java` (trocarsenha), `PainelControleResource.java` (anotação), `LogInterceptor.java`, `LogRequired.java`, `SecurityServiceImpl.java`, `SecurityServiceDebug.java`, `LogAuthServiceImpl.java`, `PentahoLoginServiceImpl.java`, `FuncionarioServiceImpl.java` (140-500 e 595-620), `EmailControleServiceImpl.java` (205-245), `MensageriaServiceImpl.java` (150-200, 295-320), `BiParametroServiceImpl.java` (40-75), trechos de filtro por perfil em `CargoServiceImpl`, `CargoCompetenciaServiceImpl`, `DepartamentoServiceImpl`, `DepartamentoGestorServiceImpl`, `UnidadeDepartamentoServiceImpl`, `ApontamentoFuncionalServiceImpl`, `FeedForwardRecebidoServiceImpl`, `AvaliacaoCompetenciaMobileServiceImpl`, `AvaliacaoParticipanteServiceImpl`, `DadoPreenchimentoServiceImpl`, `EmpresaServiceImpl`, `CampanhaServiceImpl`, `PDIAcaoServiceImpl`, `PainelControleServiceImpl`, `AvaliacaoServiceImpl` (651, 1189); enums `TipoPerfil`, `SituacaoAuth`, `SituacaoFuncionario`; `UserTO`, `UsernameParser`, `LogAuth`, `LogEntity`, `Funcionario`, `FuncionarioRepository`; `applicationContext-security.xml`, `-security-dev.xml`, `applicationContext-rest.xml`, `web.xml`, `debug.properties`; migração `V2.0_0049` e scripts `deploy/servers/dezi/db-scripts/after-restore.sql.d/10-criar-tabelas-log.sql` e `dump.sh`; no `webapp/`: `app.config.js`, `login.controller.js`, `services/user.service.js`, `utils/menu.utils.js`, `partials/model/TipoPerfil.js`, `*.route.js` e módulos de cadastros, campanha, PDI, acompanhamentos e administração, `campanha.view.js` e `.html`, `aba.resumo.view.html`. **Não lidos:** biblioteca `commons`; `FotoServiceImpl`; endpoint de boas-vindas em massa; `webapp/partials/preenchimento/` (regras de tela do preenchimento); `mobile/`. Banco Postgres: consultado em 2026-10-01 (seção 11).
