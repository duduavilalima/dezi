# Comunicação — e-mails, mensageria interna, notificações e fila

Módulos: `web/` e `queue/` · Submódulo 9 do roteiro (último). Prefixo das regras: `RN-COM`.
Escopo: gatilhos de e-mail, templates e parâmetros, controle de envio e reenvio (`email_controle`), fila RabbitMQ e consumidor `queue/`, mensageria interna (caixa de mensagens), mensagens de liberação, home office, contato, fale conosco e relatório de bug.
Fora de escopo: regras de senha e login em si (ver [`01-acesso-perfis.md`](01-acesso-perfis.md)); aqui só o e-mail que elas disparam.

Convenção: **[código]** = evidência no código; **[banco]** = confirmado no Postgres (consulta somente leitura de 2026-10-01, agregados apenas, dados até 2024-06-14 em `email_controle` e 2024-01-29 em `mensageria`; cópia ou produção: a confirmar); **A confirmar** = sem evidência suficiente.
Abreviações de caminho: `MensSvc` = `web/services/src/main/java/br/com/fasolti/service/impl/MensageriaServiceImpl.java`; `EmailSvc` = `.../service/impl/EmailControleServiceImpl.java`; `QEmailSvc` = `queue/src/main/java/br/com/fasolti/queue/services/impl/EmailControleServiceImpl.java`; `QMailSvc` = `queue/.../services/impl/EmailServiceImpl.java`; `FuncSvc` = `.../service/impl/FuncionarioServiceImpl.java`; `AvSvc` = `.../service/impl/AvaliacaoServiceImpl.java`.

## 1. Glossário

| Termo | Significado | Evidência |
|---|---|---|
| `email_controle` | Tabela-fila de e-mails: um registro por e-mail a enviar, com destinatário, assunto, corpo (texto ou JSON de variáveis), código do template, status e mensagem de erro. É o elo entre `web/` e `queue/` | `domain/EmailControle.java`; `queue/entities/EmailControle.java` **[código]** |
| Status do e-mail | `TipoEmailStatus`: PENDENTE(1), ENVIADO(2), REENVIO(3), ERRO(4), RESOLVIDO(5) | `services.client/.../types/TipoEmailStatus.java`; cópia idêntica em `queue/` **[código]** |
| Código de template | Texto `"1"` a `"8"` gravado em `email_controle.template`; `null` = e-mail avulso com corpo já pronto (ver RN-COM-05) | `EmailSvc:40-47`; `queue/util/Constant.java` **[código]** |
| Mensageria | Caixa de mensagens **dentro do sistema**: remetente, destinatário, título, texto, avaliação de origem, resposta (`id_original`), data de abertura. Não é o e-mail | `domain/Mensageria.java` **[código]**; tabela `mensageria` (392 linhas) **[banco]** |
| Mensagem de acompanhamento | Mensagem enviada pelo painel de controle a quem ainda não preencheu | `PainelControleServiceImpl:695-715` **[código]** |
| Mensagem de avaliação | Mensagem do RH aos participantes de uma avaliação, pela aba "Mensagens" da avaliação | `AvaliacaoResource:60-68` **[código]** |
| Mensagem de sistema | Registro de `mensageria` criado pelo próprio sistema para documentar um e-mail (liberação, erro, home office); não tem destinatário | RN-COM-34 |
| `Mensagem` / `TipoMensagem` | Entidade e enum de "mensagem de tela" (ícone, som, tipo Informação/Atenção/Erro). Sem uso: tabela `mensagem` com 0 linhas, `MensagemService` só envia boas-vindas, nenhum código lê o enum **[código, por ausência]** **[banco]** | `domain/Mensagem.java`, `TipoMensagem.java` |
| Parâmetro | Linha da tabela `parametros` (chave, valor), lida a cada uso (sem cache) | `ParametroServiceImpl` **[código]**; 30 linhas **[banco]** |

## 2. Arquitetura do envio (visão de uma linha)

`web/` grava `email_controle` com status PENDENTE → publica o **id** (texto) na fila RabbitMQ `dezi-queue` → `queue/` consome, lê a linha, escolhe o template, envia por SMTP e atualiza o status. Em falha, `queue/` publica na fila de reenvio com atraso.

Fluxo detalhado nas seções 3 e 4; gatilhos na seção 5.

## 3. Controle de envio (`web/` → fila)

- **RN-COM-01** **Caminho único.** Todo e-mail passa por `salvarEEnviarEmail`: carimba `dataCriacao`, grava a linha (status PENDENTE) e publica o id na fila. Não há envio direto por SMTP no `web/`. **[código]** `EmailSvc:107-119`, `rabbitmq/sender/RabbitMQSender.java:22`.
- **RN-COM-02** **Falha ao publicar.** Se gravar ou publicar lança exceção, o e-mail fica com status **REENVIO** e `msg_erro` = "Erro ao enviar email para a fila RabbitMQ !". **Nada reenvia essas linhas**: não há agendador no `web/` nem no `queue/` que varra o status REENVIO. **[código, por ausência]** `EmailSvc:112-118`. **[banco]** nenhuma linha tem essa mensagem. Suspeito: se a falha ocorre depois da gravação, o `catch` grava de novo o mesmo objeto sem `id` e pode duplicar a linha; **a confirmar**.
- **RN-COM-03** **Quem consulta o resultado.** `GET /email/consultaremailscomErro` e `GET /email/consultaremailspendentes` listam e-mails por status ERRO e PENDENTE. REENVIO não é consultável; `resolverEmail()` é vazio e `atualizarStatusEmail` só grava. Não achei tela do `webapp/` que chame esses endpoints. **[código]** `EmailSvc:347-386`, `EmailControleResource`.
- **RN-COM-04** **Os dados nunca são apagados** e guardam o conteúdo enviado, inclusive **senhas em texto**: todas as 2.352 linhas do template 2 (esqueci minha senha) têm a chave `senha` no JSON de `mensagem` e todas as 62 do template 1 (boas-vindas) trazem a senha no texto. **[banco]**; **[código]** `EmailSvc:130-143, 239-254`.

## 4. Consumidor `queue/`

- **RN-COM-05** **Escolha do template** por `email_controle.template` (`QEmailSvc:136-181`):

| Código | Uso | Arquivo HTML no `queue/` | Assunto | Linhas **[banco]** |
|---|---|---|---|---|
| `1` | Boas-vindas em massa (RN-COM-17) | `bemVindoFuncionario` | parâmetro `mail.subject.bemvindo.funcionario` ou o digitado na tela | 62 |
| `2` | Esqueci minha senha | `esqueceuSenhaFuncionario` | parâmetro `mail.subject.esqueceu.senha.funcionario` | 2.352 |
| `3` | Troca de senha | `trocaSenhaFuncionario` | parâmetro `mail.subject.troca.senha.funcionario` | 5 (todas de 2019) |
| `4` | Novo cadastro de funcionário | `novoCadastroFuncionario` | parâmetro `mail.subject.novo.cadastro.funcionario` | 0 |
| `5` | Mensagem de avaliação e de acompanhamento | `messageDefault` | título da mensagem (ou "Nova mensagem - DEZI") | 71 |
| `6` | Fale conosco (mobile) | `suporteFaleconosco` | assunto digitado | 2 |
| `7` | Contato (web) | `sendContato` | assunto digitado | 18 |
| `8` | "Nova avaliação" | **não existe** | — | 0 |
| `null` | E-mail avulso (liberação, erro, home office, bug) | nenhum: corpo HTML pronto vai direto | passado por quem chama | 298 |

- **RN-COM-06** **Código desconhecido não envia nada, mas marca ENVIADO.** Se `template` não é nulo e não casa com nenhum ramo (hoje só o `8`), nenhum e-mail sai e o status vira ENVIADO do mesmo jeito. O `web/` não gera o `8`; é constante sem uso. **[código]** `QEmailSvc:142-176`.
- **RN-COM-07** **Assunto parametrizado.** Se o assunto gravado é igual à chave `mail.subject.*`, o consumidor troca pelo valor do parâmetro; senão usa o texto. **[código]** `QEmailSvc:197-199`. **[banco]** valores: "Bem vindo - DEZI", "Esqueci minha senha - DEZI", "Troca de senha - DEZI", "Novo cadastro - DEZI".
- **RN-COM-08** **O corpo vem do `queue/`, não da tabela `parametros`.** Os modelos HTML são arquivos Thymeleaf do classpath (`queue/src/main/resources/templates/`), preenchidos com o JSON de `email_controle.mensagem`. As 8 chaves `mail.template.*` / `mail.send.*` da tabela `parametros` (de 876 a 1.382 caracteres cada) **não são lidas por nenhum código**: no `web/` o bloco que as lia está comentado (`EmailSvc:49-56`) e o `queue/` só lê as de assunto. A única leitura é a do `web/` na mensageria (RN-COM-30), que exige a existência da chave `mail.send.message.nova.avaliacao` sem usar o valor. **[código]** **[banco]**. Os textos dos modelos HTML não foram transcritos; só `messageDefault` e `sendContato` mostram variáveis (`CONTENT`, `link`, `MSG_LINK`; id, matrícula, usuário, empresa, contato).
- **RN-COM-09** **Remetente** é `default.email-from` (variável `DEZI_EMAIL_FROM`, padrão `no-reply@dezi.com.br`). O parâmetro `mail.default` (valor `no-reply@dezi.com.br` **[banco]**) não é lido. **[código]** `queue/.../application.yml:80`, `QMailSvc:37-38, 148-151`.
- **RN-COM-10** **ENVIADO não prova entrega.** O consumidor marca ENVIADO depois de chamar o envio, mas o envio **engole as exceções de SMTP**: `sendEmailDezi` captura `MessagingException`, `IOException` e `MailException` apenas imprime a pilha; `send` é `@Async` e o erro some na thread. Falha de SMTP, endereço inválido recusado na hora ou credencial vencida **não** geram REENVIO nem ERRO. O que gera REENVIO é só falha **antes** do envio: linha não encontrada, parâmetro de assunto ausente, modelo HTML ausente ou com erro. **[código]** `QMailSvc:49-60, 84-97`, `QEmailSvc:95-105, 175-176`. Também não há tratamento de devolução (bounce).
- **RN-COM-11** **Reenvio com atraso.** Na falha, o consumidor publica `{id, tentativa+1}` na exchange com atraso (plugin `x-delayed-message`), com espera de `send-delay` (10.000 ms) × tentativa. O consumidor da fila de reenvio tenta de novo enquanto `tentativa <= 3`; acima disso grava ERRO. Ou seja: até 3 reenvios (10 s, 20 s, 30 s depois) e então ERRO. Há ainda retry do listener (2 tentativas, 1 a 5 s) e concorrência de 4 a 8 consumidores. **[código]** `queue/rabbitmq/impl/EmailQueueConsumer.java:19-31`, `EmailQueueSender.java`, `application.yml:11-19, 69-76`. **[banco]** **0** e-mails em ERRO e 0 em RESOLVIDO. O estado RESOLVIDO só é gravado por `resolverEmail(id)` do `queue/`, que nenhum código chama.
- **RN-COM-12** **Defeito: erro gravado na linha errada.** `QEmailSvc` guarda o e-mail em processamento num **campo de instância** (`private EmailControle emailControle`, linha 50) de um serviço singleton com vários consumidores. Quando o id não existe, `emailErroMsgAndUpdate` (linhas 129-134) grava "E-mail não encontrada. ID: N" e o status REENVIO na linha do **último e-mail processado** (ou lança `NullPointerException` se não houve nenhum). **[banco]** 795 linhas têm essa mensagem e **em nenhuma** o N é o id da própria linha; 792 ficaram em REENVIO (28% das 2.808 linhas) e 3 em ENVIADO; todas as 792 em REENVIO têm essa mensagem e nenhuma tem data de atualização nula, e nenhuma tem a mensagem do `web/` (RN-COM-02). Portanto **REENVIO no banco não quer dizer "pendente de reenvio"**: é, na maioria, linha cujo status foi sobrescrito por falha de **outro** e-mail; se o e-mail da própria linha chegou a sair, **a confirmar** (a linha continua sem ser reprocessada).
- **RN-COM-13** **Causa provável do "não encontrada" (a confirmar).** O `web/` publica o id na fila dentro da transação que grava a linha; se o consumidor lê antes do commit, não acha a linha. A segunda tentativa (RN-COM-11), com atraso, acha. É a explicação mais simples para ERRO ser 0 e REENVIO ser alto, mas depende do tipo de transação do `save` da biblioteca `commons`, **não acessível**. **A confirmar.**
- **RN-COM-14** **Distribuição [banco] por status:** PENDENTE 249 (9%), ENVIADO 1.767 (63%), REENVIO 792 (28%), nenhum ERRO. Em PENDENTE ficam linhas que o consumidor nunca processou (ex.: 185 do template 2). Por ano, o volume de PENDENTE mais alto foi 2022 (168). Causa **a confirmar** (consumidor parado, fila perdida, ou id que nunca chegou). Mediana entre criação e atualização dos ENVIADO: 0,4 s; máximo 2 min.
- **RN-COM-15** **Infraestrutura da fila.** Filas duráveis `dezi-queue` (envio) e `dezi-queue-reenvio` (reenvio), exchange direta `dezi-exchange` e exchange atrasada `dezi-delayed-exchange`. O `web/` lê nome da fila, exchange e chave de propriedades (`dezi.queue.name`, `dezi.exchange.name`, `dezi.routing.key`) cujos valores estão **fora do repositório**: que casam com `pubsub.*` do `queue/` é **a confirmar**. Não verifiquei fila de mensagens mortas. **[código]** `RabbitMQConfig.java`, `QueueFactory.java`.

## 5. Gatilhos de e-mail (quem dispara o quê)

Todos passam por RN-COM-01. Sempre que a coluna "Parâmetro" cita uma chave, o valor é o do **[banco]**.

| Gatilho | Parâmetro | Destinatário | Template | Observação |
|---|---|---|---|---|
| Esqueci minha senha (`forgotPassword`) | `notificacao.email.funcionario.esqueci.senha` = true | funcionário ATIVO da empresa com aquele e-mail funcional | 2 | RN-COM-16 |
| Troca de senha | `notificacao.email.funcionario.troca.senha` = true | funcionário | 3 | **Não envia**: RN-COM-16 |
| Novo cadastro de funcionário | `notificacao.email.funcionario.novo` = **false** | funcionário | 4 | Desligado de propósito (decisão registrada em 00, item 7) |
| Boas-vindas em massa | `notificacao.email.funcionario.bemvindo` = true | um colaborador ou todos de um perfil | 1 | RN-COM-17 |
| Mensagem de avaliação | `notificacao.email.avaliacao.nova.mensagem` = true | participantes escolhidos | 5 | RN-COM-27 a 31 |
| Resposta a mensagem | `notificacao.email.resposta.mensagem` = true | remetente original | 5 | RN-COM-32 |
| Mensagem de acompanhamento | nenhum (só a marca da tela) | colaboradores listados | 5 | RN-COM-33 |
| Liberação iniciada / concluída / com erro | nenhum | e-mail de suporte fixo / GESTOR_RH / e-mail de suporte fixo | avulso | RN-COM-35 a 39 |
| Home office | nenhum | funcionário / contato da empresa | avulso | RN-COM-40, 41 |
| Contato (web) | — | `email.contato.suporte` | 7 | RN-COM-42 |
| Fale conosco (mobile) | — | `email.contato.suporte` | 6 | RN-COM-43 |
| Relatório de bug | `mail.receiving.notification.bug.report` | lista de e-mails separada por `;` | avulso | RN-COM-44: sem uso |
| SMS (esqueci, troca, novo) | `notificacao.sms.*` = true | — | — | RN-COM-45: não implementado |

### 5.1 E-mails de senha e cadastro

- **RN-COM-16** **Esqueci minha senha:** a senha nova é gerada e gravada (com `primeiroAcesso=true`) **antes** de olhar o parâmetro; com o parâmetro `false`, a senha de quem pediu é trocada e ninguém é avisado. O e-mail leva usuário, **senha em texto** e link `http://{host da empresa}/index.html#/login/{id da empresa}`. Se o e-mail não existe, lança `validation.not-found-email` e registra o e-mail digitado no log. **[código]** `FuncSvc:371-390`, `EmailSvc:130-143`. **Troca de senha:** o ramo do parâmetro chama `sendEmailTrocaSenha`, que só imprime "Troca de senha." (o envio está comentado), então **nenhum e-mail sai**, embora o parâmetro esteja `true`; as 5 linhas do template 3 são de 2019 (versão anterior). **[código]** `EmailSvc:145-158`, `FuncSvc:443-452`. **[banco]**. Parâmetro de **novo cadastro**: `doAfterSave` do funcionário chama `sendEmailNovoCadastro` com a senha temporária gerada em `doBeforeSave` (`FuncSvc:225-235`); com o parâmetro `false` (hoje) nada é enviado e ninguém recebe a senha por esse caminho. Se `doBeforeSave` também roda em edição (e portanto redefine a senha a cada gravação) depende da classe base em `commons`: **a confirmar** (ver 01/02).
- **RN-COM-17** **Boas-vindas em massa** (`POST /mensagem/enviarmensagemboasvindas`, menu "Mensagem Boas vindas", visível só para `ROLE_ADMINISTRADOR` na tela; o servidor não confere perfil): só envia se `notificacao.email.funcionario.bemvindo` for `true`. Destinatários: o colaborador escolhido, ou todos os do perfil na empresa do usuário logado. Para **cada** um: gera senha nova, grava criptografada com `primeiroAcesso=true` (**redefine a senha de quem já usava o sistema**), substitui `#URL#`, `#LOGIN#` (matrícula) e `#PASSWORD#` no texto digitado e enfileira. Assunto: o digitado ou o padrão. O texto padrão da tela está escrito no código do `webapp`. Não valida e-mail do destinatário (a coluna `email` é obrigatória, então a linha falha) nem trata matrícula não encontrada ou inativa (`NullPointerException`). **[código]** `MensagemServiceImpl:33-42`, `EmailSvc:210-256`, `webapp/partials/administracao/mensagens/mensagem.controller.js`. **[banco]** 62 envios.
- **RN-COM-18** **Endereços de acesso nos e-mails são inconsistentes:** esqueci senha usa `#/login/{id}`; boas-vindas, mensagens e acompanhamento usam `#login/{id}`; novo cadastro usa `http://{host}/tour`; todos com `http` fixo. O parâmetro `avd.url.link.email` (aponta para um ambiente de um cliente específico **[banco]**) não é lido por nenhum código. Se as duas formas de `#` funcionam na tela: **a confirmar**. **[código]** `EmailSvc:136, 202, 240, 290, 320`.

## 6. Mensageria interna (caixa de mensagens)

### 6.1 Modelo

- Tabela `mensageria` **[banco]** (392 linhas, 2017-06-26 a 2024-01-29; pico em 2020 com 222): `id_envio`, `id_destino` (ambos opcionais desde V2.0_0017), `id_avaliacao`, `id_av_envio`, `id_av_destino`, `id_original` (resposta), `titulo`, `texto`, `num_mensagem`, `data_hora_envio` (obrigatória), `data_hora_abertura`. Chaves estrangeiras para funcionário e avaliação. **[código]** `domain/Mensageria.java`, V2.0_0017.
- Perfis de uso **[banco]**: 100 mensagens ligadas a avaliação (97 com `id_av_envio`; 0 com `id_av_destino`); 14 de acompanhamento (sem avaliação, com destino); **276 sem destinatário** (mensagens de sistema, ver RN-COM-34); 3 respostas, todas ligadas a avaliação; 14 em que remetente e destinatário são o mesmo funcionário.

### 6.2 Regras

- **RN-COM-19** **Numeração.** `num_mensagem` = maior número existente + 1, lido e gravado sem sequência nem trava. **[código]** `MensSvc:395-403`. **[banco]** 3 de 361 números são usados por títulos diferentes (corrida) e 19 mensagens estão sem número.
- **RN-COM-20** **Não lida** = mensagem em que o usuário é destinatário, **não** é o remetente e `data_hora_abertura` é nula. O contador usa o `userId` da URL (sem conferir o usuário logado: `FIXME` no código). **[código]** `MensSvc:510-524`, `MensageriaResource:31-41`. **[banco]** 28 não lidas e 74 lidas.
- **RN-COM-21** **Marcar como lida** é feito pela tela: ao abrir uma mensagem em que o usuário é destinatário e sem data de abertura, grava a data. **[código]** `webapp/partials/toolbar/mensagens/dialogs/dialogs.controller.js:61-62`. O servidor não impede gravar a abertura de mensagem de terceiros.
- **RN-COM-22** **Caixa do usuário:** `GET /mensageria/user/{id}/all` devolve duas listas (enviadas e recebidas, por data crescente). Sem filtro por usuário logado. **[código]** `MensSvc:405-416`.
- **RN-COM-23** **Agrupamento por avaliação.** A tela da avaliação lista as mensagens **enviadas** (com `id_av_envio`) agrupadas por `título + número` e mostra a quantidade de destinatários; os destinatários vêm de `findByAvaliacaoIdAndTitulo`. **[código]** `MensSvc:474-508`.
- **RN-COM-24** **Exclusão em cascata.** Remover uma avaliação em `AG_LIBERACAO` apaga todas as mensagens com aquele `id_avaliacao` (`AvSvc:1745-1760`). **[código]**.
- **RN-COM-25** **Respostas.** `POST /mensageria` (gravação genérica) cria a resposta com `id_original`; o e-mail segue RN-COM-32.
- **RN-COM-26** **Permissão.** Nenhum dos endpoints de mensageria, e-mail, contato e boas-vindas tem `@PreAuthorize`; a restrição vive só nos menus do `webapp`. **[código]** (consistente com 01, seção 7).

### 6.3 Mensagem de avaliação (`POST /avaliacao/{id}/mensagens`)

- **RN-COM-27** A tela monta uma mensagem por destinatário marcado, com título, texto, avaliação de origem como remetente (`avaliacaoEnvio`) e dois textos do arquivo de tradução (`AVALIACAO.MSG_NOTIFICACAO` e `AVALIACAO.MSG_LINK_APPLICATION`) que o servidor usa como corpo do e-mail. **[código]** `webapp/partials/avaliacao/avaliacao.mensagem.js` (`$ctrl.enviarMensagem`). O texto do aviso e o do link ficam nos arquivos de i18n (não lidos).
- **RN-COM-28** Cada mensagem recebe usuário remetente = usuário logado, data/hora de envio = agora e número novo; o e-mail é enfileirado **antes** de gravar a mensagem. **[código]** `MensSvc:115-150`.
- **RN-COM-29** **Validação por mensagem:** exige aviso preenchido, destinatário com e-mail funcional **e** remetente com e-mail funcional; senão lança `validation.mensageria` / `empty-fields` e a gravação inteira é desfeita **depois** de e-mails dos destinatários anteriores já terem sido enfileirados (as linhas de `email_controle` somem no rollback, e o consumidor não as acha: ver RN-COM-12). **[código]** `MensSvc:115-160`. Suspeito, **a confirmar**.
- **RN-COM-30** **O parâmetro é testado só por existir.** `saveAndSendMensagensAvaliacao` envia o e-mail se o valor de `notificacao.email.avaliacao.nova.mensagem` **não é nulo**; `"false"` não desliga. **[código]** `MensSvc:117-122`. Hoje o valor é `true`, então o efeito é o mesmo.
- **RN-COM-31** **A caixa "Notificar via e-mail" é ignorada pelo servidor.** A tela envia `envioEmail` por destinatário, mas `saveAndSendMensagensAvaliacao` não o lê: todo destinatário selecionado recebe e-mail. **[código]** `MensSvc:117-140` x `avaliacao.mensagem.js:169`. Suspeito: pode ser intencional; **a confirmar**. Os placeholders do aviso são `_enviada_` (aqui, o **nome abreviado da avaliação**), `_enviadaAv_`, `_data_` (dd/MM/yyyy), `_horas_` (HH:mm) e `_assunto_` (título); o `&gt;`/`&lt;` voltam a `>`/`<`. O e-mail leva o link de acesso (RN-COM-18). **[código]** `EmailSvc:312-345`.
- **RN-COM-32** **Resposta e mensagem direta** (`doAfterSave`, `POST /mensageria`): envia e-mail só se `notificacao.email.avaliacao.nova.mensagem` for `true` **e** a mensagem tiver avaliação. O ramo do parâmetro `notificacao.email.resposta.mensagem` exige `avaliacao == null` dentro de um `if` que exige `avaliacao != null`: **código morto**, então esse parâmetro (`true`) não tem efeito, e resposta a mensagem sem avaliação (como as de acompanhamento) nunca gera e-mail. Aqui `_enviada_` é o **nome de quem enviou**. **[código]** `MensSvc:100-113`, `EmailSvc:322-329`.

### 6.4 Mensagem de acompanhamento (painel de controle)

- **RN-COM-33** **Acompanhamento.** `envioDemensagensDeAcompanhamento` grava **uma mensagem por colaborador** da lista (sem avaliação, sem número, sem checar duplicidade, destinatário mapeado do que a tela mandou) e, se a tela marcou `envioEmail`, enfileira um e-mail "Nova mensagem - DEZI" por colaborador. **Não consulta** o parâmetro `notificacao.email.avaliacao.nova.mensagem` nem valida e-mail. O método devolve `null`. **[código]** `PainelControleServiceImpl:695-715`, `EmailSvc:279-310`. **[banco]** 14 mensagens desse tipo. O template 5 (71 e-mails, 2020-05 a 2022-03) atende tanto este caso quanto as mensagens de avaliação; não dá para separar os dois pelo banco.
- **RN-COM-34** **Mensagens de sistema não têm destinatário.** Liberação, erro de liberação e home office gravam `mensageria` com remetente "HomeOffice" (id configurado em `homeoffice.user.id`, **mesmo para avaliações que não são home office**) e título/texto do e-mail, mas o `idDestino` atribuído no código **não é persistido** (o mapeamento ignora o campo): **[banco]** 174 de liberação, 5 de erro e 97 de home office, **todas sem destinatário e sem avaliação**. Funcionam como **registro/log** e não aparecem na caixa de ninguém. No fluxo de conclusão, o código ainda atribui como "destino" o **id da avaliação** (`MensSvc:336`), sem efeito. **[código]** `MensSvc:197-216, 241-259, 279-297, 330-348`. As 174 + 5 + 97 somam exatamente as **276** linhas sem destinatário da tabela.

## 7. Notificações de liberação da avaliação

- **RN-COM-35** **Início:** ao comandar a liberação, se o usuário logado **não** é `ADMINISTRADOR`, envia e-mail avulso "Liberação DEZI" para o endereço fixo `faleconosco@dezi.com.br` (constante no código): "A empresa X comandou a liberação da avaliação Y da campanha Z". Roda de forma síncrona, antes da liberação em segundo plano; se lançar exceção (ex.: parâmetro de mensageria ausente), o comando de liberação é interrompido depois de a avaliação já ter sido marcada "em liberação" (efeito exato sobre o estado gravado: **a confirmar**). **[código]** `AvSvc:651-653`, `MensSvc:262-298`. **[banco]** 37 registros "Liberação DEZI".
- **RN-COM-36** **Conclusão:** ao terminar a liberação (`LIBERADA`, `em_liberacao=false`), envia um e-mail avulso **para cada funcionário ATIVO com perfil `GESTOR_RH` da empresa da campanha**: "Prezado(a) {nome}, a liberação de sua avaliação foi processada com sucesso". Assunto: "Liberação Empresa {empresa} - Campanha {campanha} - Avaliação {avaliação}". Vale para o fluxo padrão e o home office. **[código]** `AvSvc:839, 2228`, `MensSvc:300-350`. **[banco]** 137 registros "Liberação Empresa …" (os 174 de liberação menos os 37 de "Liberação DEZI").
- **RN-COM-37** **A empresa do assunto é a do usuário logado**, não a da campanha: se o usuário que liberou for de outra empresa (ex.: administrador da plataforma), o assunto traz o nome da empresa dele. Os destinatários, sim, são da empresa da campanha. **[código]** `MensSvc:321-325` x `304-308`. Suspeito, **a confirmar**.
- **RN-COM-38** Sem funcionário `GESTOR_RH` ativo na empresa, **ninguém é avisado** da conclusão; quem liberou (por exemplo, um `CONSULTOR_RH` ou o administrador) também não recebe. **[código]**.
- **RN-COM-39** **Erro:** na falha da liberação, envia "ERRO - Liberação DEZI" com a **pilha de erro completa** para o endereço fixo de suporte; o cliente (RH) não é avisado. A avaliação e a campanha ficam em "em liberação" (decisão do responsável, 00 item 3.2: corrigir no sistema novo). **[código]** `AvSvc:662, 680`, `MensSvc:352-393`. **[banco]** 5 ocorrências (2021-02 a 2024-01), uma por falha registrada.
- **RN-COM-39a** Em todas as três mensagens, o ramo "avisa também pela mensageria com e-mail por usuário" é **código morto**: o texto de aviso (`avisoEmailInternacionalizado`) nunca é preenchido nesses objetos, então a condição é falsa e `sendEmailVerificaMensagem` não é chamado. O e-mail que sai é o de `sendEmail`. O parâmetro `mail.send.message.nova.avaliacao` só é lido para existir (se faltasse, daria `NullPointerException`). **[código]** `MensSvc:278-297, 329-348, 373-392`.

## 8. Home office, contato, fale conosco, relatório de bug

- **RN-COM-40** **Home office, boas-vindas** (`HomeOfficeBrasilResource:55`): se o funcionário está com `primeiroAcesso`, gera senha nova, grava e envia e-mail avulso "Bem-vindo(a) à Pesquisa Nacional de Clima Home Office!" com login (matrícula) e **senha em texto**, endereços web e do app (PWA) **fixos no código** (`homeofficebrasil.dezi.com.br`, `homeofficebrasilmobile.dezi.com.br`) e o e-mail de suporte fixo. Se não está em primeiro acesso, não envia e-mail, mas grava a mensagem de sistema. Texto e assunto fixos no código. **[código]** `MensSvc:164-217`.
- **RN-COM-41** **Home office, ambiente da empresa** (`HomeOfficeBrasilResource:59`): e-mail avulso ao contato da empresa com o link de uma planilha de carga em bucket S3 fixo no código, pedindo nome, gênero, e-mail e data de nascimento dos colaboradores e a indicação do responsável de RH; o texto diz que o RH controla a frequência das avaliações e a data de encerramento (ver 05b, RN-CRI-53). Menciona adequação à LGPD. **[código]** `MensSvc:219-260`. **[banco]** 132 mensagens do fluxo home office entre 2020-05-08 e 2020-12-29: **o fluxo foi usado em 2020**, embora `av_homeoffice` esteja nulo em todas as avaliações (05b, RN-CRI-50: aquela "ausência de rastro" vale só para `avaliacao`).
- **RN-COM-42** **Contato (web)** (`POST /contato/enviar`): enfileira e-mail para o endereço do parâmetro `email.contato.suporte` (template 7) com id, matrícula, nome, empresa e e-mail funcional do solicitante, assunto, classificação e mensagem. Sem limite nem validação de tamanho; o remetente do e-mail é o do sistema, e o do solicitante só vai no corpo. **[código]** `EmailSvc:176-193`, `ContatoServiceImpl`. **[banco]** 18 envios (14 enviados). O valor atual de `email.contato.suporte` não foi lido.
- **RN-COM-43** **Fale conosco (mobile)** (`POST …/faleconosco` em `MobileResource`; também `FuncSvc:814-816`): e-mail para o mesmo endereço de suporte (template 6) com mensagem, classificação, plataforma, **nome e e-mail do remetente informados no corpo** (não os do usuário logado; só a matrícula do logado vai como criador). Usa o `id` do próprio formulário como "destinatário". A entidade `FaleConosco` existe, mas a tabela `fale_conosco` **não existe** no banco e nada grava nela: a solicitação só existe como e-mail. **[código]** `EmailSvc:160-174`, `FaleConoscoServiceImpl`. **[banco]** 2 envios (2020-03).
- **RN-COM-44** **Relatório de bug por e-mail:** `BugReportService.notificateBug` montaria um e-mail (título, descrição, data, classe e pilha de exceção até 30 níveis) para a lista de `mail.receiving.notification.bug.report`, com assunto de `mail.bug.report.subject`. **Nenhum código chama** esse serviço; o valor semeado na migração V2.0_0020 é um e-mail pessoal de desenvolvedor e o valor atual não foi lido. **[código, por ausência]** `BugReportServiceImpl:26-74`.
- **RN-COM-45** **SMS não existe.** Os parâmetros `notificacao.sms.*` (todos `true`) só disparam `logger.warn("Envio de SMS não implementado")`. Em "novo cadastro" o teste ainda concatena o id da empresa ao valor (`"true/106"`), então nunca é `true`. **[código]** `FuncSvc:355-360, 383-386, 455-459`.

## 9. Parâmetros de comunicação (inventário)

| Chave | Valor **[banco]** | Quem lê |
|---|---|---|
| `notificacao.email.funcionario.esqueci.senha` | true | `forgotPassword` |
| `notificacao.email.funcionario.troca.senha` | true | lido, mas sem efeito (RN-COM-16) |
| `notificacao.email.funcionario.novo` | **false** (intencional) | gravação de funcionário |
| `notificacao.email.funcionario.bemvindo` | true | boas-vindas em massa |
| `notificacao.email.avaliacao.nova.mensagem` | true | mensagens de avaliação e resposta |
| `notificacao.email.resposta.mensagem` | true | **ninguém com efeito** (RN-COM-32) |
| `notificacao.sms.*` (3) | true | só log (RN-COM-45) |
| `mail.subject.*` (4) | textos curtos | consumidor `queue/` (RN-COM-07) |
| `mail.template.*`, `mail.send.*` (8) | HTML de 876 a 1.382 caracteres | **ninguém** (RN-COM-08) |
| `mail.default` | `no-reply@dezi.com.br` | ninguém (RN-COM-09) |
| `avd.url.link.email` | URL de um ambiente de cliente | ninguém (RN-COM-18) |
| `email.contato.suporte` | não lido | contato e fale conosco |
| `mail.bug.report.subject`, `mail.receiving.notification.bug.report` | não lidos | serviço sem uso (RN-COM-44) |
| `homeoffice.*` | **0 linhas** na tabela; são propriedades de ambiente fora do repositório | fluxo home office |

Todas as chaves de e-mail e de SMS são lidas com `findOne(...).getValor()` sem tratar ausência (`NullPointerException` se a linha faltar). Parâmetros são **globais** (sem empresa): ligar ou desligar vale para todas as empresas. **[código]** **[banco]**. As chaves `menus.visibilidade.*` e `data_revisao_TU_PP` pertencem a outros assuntos e ficam fora deste documento.

## 10. Segurança e operação (levar ao sistema novo)

- **RN-COM-46** **Credencial SMTP exposta no repositório:** `queue/src/main/resources/application.yml:49-50` traz usuário e senha reais do provedor de e-mail como **valor padrão** (não só placeholder). Há também padrões `admin/admin` para o RabbitMQ (linhas 9-10) e `deziuser/deziuser` para o banco (linhas 42-43). A credencial deve ser considerada comprometida e rotacionada; **não** repetida aqui. Em produção o `deploy/servers/dezi/docker-compose.mqint.yml` passa `DEZI_QUEUE_*` por variável.
- **RN-COM-47** Senhas em texto: no corpo do e-mail (esqueci senha, boas-vindas, home office, novo cadastro) e **gravadas em `email_controle`** (RN-COM-04). Já registrado em 01 para a auditoria; aqui é a segunda cópia.
- **RN-COM-48** O sistema não sabe se o e-mail chegou (RN-COM-10), não reenvia o que falhou antes da fila (RN-COM-02) e rotula errado o que falhou (RN-COM-12). Para o negócio, "enviado" significa apenas "entregue ao servidor SMTP sem exceção capturada".
- **RN-COM-49** Nenhum e-mail leva identificação de empresa no remetente: o remetente é único para todas as empresas (RN-COM-09), e os endereços de suporte e de erro são fixos no código (`faleconosco@dezi.com.br`).

## 11. Pontos a confirmar (resumo)

1. Por que há 792 e-mails em REENVIO e como o negócio quer tratar o estado (RN-COM-12 a 14); se a causa é publicação antes do commit (RN-COM-13).
2. Linhas PENDENTE que nunca saíram (249) e 0 em ERRO: o consumidor chegou a ficar parado? (RN-COM-11, 14).
3. A caixa "Notificar via e-mail" deve valer por destinatário? E o parâmetro `false` deve desligar? (RN-COM-30, 31).
4. Mensagem de acompanhamento deve respeitar o parâmetro de notificação e validar e-mail? (RN-COM-33).
5. `notificacao.email.resposta.mensagem` e e-mail de resposta fora de avaliação: o negócio quer? (RN-COM-32).
6. E-mail de troca de senha: deve existir? Hoje não sai (RN-COM-16).
7. Falha de validação no meio do lote de mensagens: comportamento desejado (RN-COM-29).
8. Assunto de conclusão com a empresa do usuário logado (RN-COM-37) e quem deve receber a conclusão se não há `GESTOR_RH` (RN-COM-38).
9. O RH deve ser avisado do erro de liberação (hoje só o suporte, RN-COM-39)?
10. Endereço de acesso dos e-mails: forma do link e protocolo (RN-COM-18).
11. Boas-vindas em massa **redefine a senha** de quem já tem acesso: é a regra? (RN-COM-17).
12. Valores das propriedades fora do repositório: `dezi.queue.*`, `homeoffice.*` e `email.contato.suporte` (RN-COM-15, 41, 42).
13. Se `doBeforeSave` de funcionário redefine a senha a cada gravação (RN-COM-16; ver 01/02).
14. Há necessidade do relatório de bug por e-mail e de `Mensagem`/`TipoMensagem`? Sem uso hoje (RN-COM-44, glossário).

## 12. Fontes lidas

`web/services/.../MensageriaServiceImpl.java` (todo), `EmailControleServiceImpl.java` (todo), `MensagemServiceImpl.java`, `ParametroServiceImpl.java`, `FaleConoscoServiceImpl.java`, `ContatoServiceImpl.java`, `BugReportServiceImpl.java`, `rabbitmq/RabbitMQConfig.java`, `rabbitmq/sender/RabbitMQSender.java`; `FuncionarioServiceImpl.java` (222-240, 335-460, 805-820), `AvaliacaoServiceImpl.java` (640-690, 825-842, 1745-1760, 2220-2230), `PainelControleServiceImpl.java` (695-715); `TipoMensagem.java`, `TipoEmailStatus.java`; entidades `Mensageria`, `EmailControle`, `Mensagem`, `FaleConosco`; `MensageriaTO`; `MensageriaResource`, `EmailControleResource`, `MensagemResource`, `ContatoResource`, `AvaliacaoResource` (50-75), `MobileResource` (95-112); migração V2.0_0017 (todas as demais só por `grep` das chaves de parâmetro); módulo `queue/`: `EmailControleServiceImpl`, `EmailServiceImpl`, `ParametroServiceImpl`, `EmailQueueConsumer`, `EmailQueueSender`, `QueueFactory`, `PubSubConfig`, `EmailParametro`, `EmailControle`, `Constant`, `application.yml`, `DeziQueueApplication` (só `@EnableAsync`), e 3 dos 8 modelos HTML (`messageDefault`, `bemVindoFuncionario`, `sendContato`); `webapp`: `avaliacao.mensagem.js` (120-255), `toolbar/mensagens/mensagens.js` (90-177), `dialogs.controller.js` (só a marcação de lida), `administracao/mensagens/mensagem.controller.js` e `mensagem.route.js`, `services/contato.js`; `deploy/servers/dezi/docker-compose.mqint.yml` (só variáveis `DEZI_QUEUE_*`).
Não lidos: biblioteca `commons` (transação do `save`, mapeamento de `idDestino`), textos de i18n dos avisos (`AVALIACAO.MSG_NOTIFICACAO`, `MSG_LINK_APPLICATION`), 5 dos 8 modelos HTML, telas de contato e fale conosco do `webapp` e do `mobile/`, `ParametroResource`/tela de parâmetros, `HomeOfficeBrasilResource` (só as duas chamadas), `obterFuncionariosPorPerfilAndEmpresa`, testes do `queue/`.
Banco: consultas agregadas somente leitura em `parametros`, `email_controle`, `mensageria`, `mensagem`, `information_schema` (2026-10-01). Nenhum dado pessoal foi lido (sem endereços de e-mail, nomes, mensagens ou senhas; contagens e padrões apenas).
