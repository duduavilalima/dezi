# CAP-10 Comunicação (fonte: `09-comunicacao.md`, prefixo RN-COM; módulos `web/` e `queue/`)

## Arquitetura do envio
`web/` grava `email_controle` (PENDENTE) → publica o **id** na fila RabbitMQ `dezi-queue` → `queue/` lê a linha, escolhe o template, envia por SMTP e atualiza o status; em falha publica na fila de reenvio com atraso. Caminho único (`salvarEEnviarEmail`); sem SMTP direto no `web/`. Status: PENDENTE(1), ENVIADO(2), REENVIO(3), ERRO(4), RESOLVIDO(5). (COM-01) [C]
- Falha ao publicar → REENVIO com "Erro ao enviar email para a fila RabbitMQ !"; **nada reenvia** essas linhas (0 casos); risco de duplicar linha. (COM-02) [C] ⚠
- Dados nunca apagados; guardam **senhas em texto** (2.352 do template 2 têm `senha` no JSON; 62 do template 1 no texto). (COM-04, 47) [B]

## Consumidor `queue/`
- Templates por código em `email_controle.template`: 1 boas-vindas (62), 2 esqueci senha (2.352), 3 troca de senha (5, de 2019), 4 novo cadastro (0), 5 mensagem/acompanhamento (71), 6 fale conosco (2), 7 contato (18), 8 sem arquivo (0), `null` avulso (298: liberação, erro, home office, bug). **Código desconhecido não envia e marca ENVIADO.** Modelos são HTML Thymeleaf do `queue/`; as 8 chaves `mail.template.*`/`mail.send.*` de `parametros` não são lidas. Assunto parametrizado por `mail.subject.*`. Remetente = `default.email-from` (padrão `no-reply@dezi.com.br`); `mail.default` não é lido. (COM-05..09) [C+B]
- **ENVIADO não prova entrega:** exceções de SMTP são engolidas (`@Async`); REENVIO/ERRO só para falha **antes** do envio (linha, parâmetro ou modelo ausentes); sem tratamento de bounce. (COM-10) [C]
- **Reenvio:** até 3 tentativas com atraso 10 s × tentativa, depois ERRO (0 hoje); retry do listener 2×; 4–8 consumidores. RESOLVIDO nunca é gravado. (COM-11) [C+B]
- **Defeito de estado compartilhado:** o e-mail em processamento é campo de instância de singleton; "E-mail não encontrada" é gravado na linha do **último e-mail processado**: 795 linhas com a mensagem (nunca o id próprio), **792 REENVIO (28%)**, 249 PENDENTE (9%), 1.767 ENVIADO (63%), 0 ERRO. REENVIO no banco **não** significa pendente de reenvio. Causa provável: publicação antes do commit ⚠. (COM-12..14) [C+B]
- Filas duráveis `dezi-queue`, `dezi-queue-reenvio`, exchanges direta e atrasada; nomes no `web/` vêm de propriedades fora do repositório ⚠. (COM-15) [C]

## Gatilhos
| Gatilho | Parâmetro (valor [B]) | Destinatário | Template |
|---|---|---|---|
| Esqueci minha senha (**no novo, D56:** link de uso único, nunca para DESLIGADO, resposta igual) | `notificacao.email.funcionario.esqueci.senha`=true | funcionário ATIVO com o e-mail | 2 |
| Troca de senha | `...troca.senha`=true | — | 3 — **não sai** (envio comentado) |
| Novo cadastro | `...funcionario.novo`=**false** (intencional) | — | 4 |
| Boas-vindas em massa | `...bemvindo`=true | 1 ou todos de um perfil | 1 |
| Mensagem de avaliação | `...avaliacao.nova.mensagem`=true | participantes escolhidos | 5 |
| Resposta a mensagem | `...resposta.mensagem`=true | (código morto: sem efeito) | 5 |
| Acompanhamento | — | colaboradores listados | 5 |
| Liberação iniciada / concluída / erro | — | suporte fixo / GESTOR_RH ativos da empresa da campanha / suporte fixo | avulso |
| Home office | — | funcionário / contato da empresa | avulso |
| Contato web / fale conosco mobile | — | `email.contato.suporte` | 7 / 6 |
| Relatório de bug | `mail.receiving.notification.bug.report` | — | avulso: **sem chamador** |
| SMS (esqueci, troca, novo) | `notificacao.sms.*`=true | — | não implementado |

- **Senha:** "esqueci" grava senha nova **antes** de olhar o parâmetro (com `false` troca e ninguém é avisado); e-mail leva senha **em texto** e link. **Boas-vindas redefine a senha de quem já usava**, substitui `#URL#`, `#LOGIN#`, `#PASSWORD#`; só enfileira se `bemvindo=true`; servidor não confere perfil; NPE em matrícula inexistente. Links inconsistentes (`#/login/{id}` × `#login/{id}`, `http` fixo; `avd.url.link.email` não lido). (COM-16..18, 45) [C+B] ⚠

## Mensageria interna
- Tabela `mensageria` (392): remetente, destinatário (opcionais), avaliação, `id_original` (resposta), título, texto, `num_mensagem`, datas de envio e abertura. (seção 6.1) [B]
- `num_mensagem` = maior+1 sem trava (3 colisões). Não lida = destinatário ≠ remetente e abertura nula (28 não lidas); contador usa `userId` da URL. Marcar como lida só pela tela. Caixa devolve enviadas e recebidas (por data). Agrupamento por avaliação: `título + número`. Excluir avaliação AG_LIBERACAO apaga suas mensagens. **Nenhum endpoint de mensageria/e-mail/contato tem `@PreAuthorize`.** (COM-19..26) [C+B]
- **Mensagem de avaliação:** uma por destinatário marcado; e-mail enfileirado **antes** de gravar; exige aviso, e-mail do destinatário e do remetente, senão a gravação desfaz mas os e-mails anteriores já foram enfileirados; o parâmetro é testado só por **existir**; a caixa "Notificar via e-mail" é **ignorada** pelo servidor. Placeholders `_enviada_`, `_enviadaAv_`, `_data_`, `_horas_`, `_assunto_`. (COM-27..31) [C] ⚠
- **Resposta:** e-mail só se `avaliacao.nova.mensagem=true` **e** a mensagem tem avaliação; resposta sem avaliação nunca envia. (COM-32) [C]
- **Acompanhamento:** uma mensagem por colaborador, sem avaliação nem número; e-mail se marcado, sem consultar parâmetro nem validar e-mail. 14 mensagens. (COM-33) [C+B]
- **Mensagens de sistema:** liberação, erro e home office gravam `mensageria` com remetente "HomeOffice" e **sem destinatário** (276 = 174 + 5 + 97); funcionam como log. (COM-34) [C+B]

## Avisos de liberação
- **Início:** só se o usuário não é ADMINISTRADOR; e-mail "Liberação DEZI" para endereço fixo de suporte; síncrono, falha interrompe depois de marcar "em liberação". (COM-35) [C+B]
- **Conclusão:** e-mail a cada GESTOR_RH ATIVO da empresa da campanha ("a liberação de sua avaliação foi processada com sucesso"); assunto usa a empresa do **usuário logado**; sem GESTOR_RH ninguém é avisado. (COM-36..38) [C] ⚠
- **Erro:** "ERRO - Liberação DEZI" com pilha completa ao suporte fixo; **o RH não é avisado**; avaliação e campanha ficam em liberação. 5 ocorrências. (COM-39) [C+B]
- Ramo "avisa pela mensageria com e-mail por usuário" é código morto. (COM-39a)

## Home office, contato, fale conosco, bug
- Home office: boas-vindas (senha em texto, endereços fixos) só se `primeiroAcesso`; e-mail ao contato da empresa com link de planilha em bucket fixo e menção a LGPD; **132 mensagens em 2020**. (COM-40, 41) [C+B]
- Contato web (template 7, 18 envios) e fale conosco mobile (template 6, 2 envios; nome/e-mail do corpo; tabela `fale_conosco` não existe) vão a `email.contato.suporte`. (COM-42, 43) [C+B]
- `BugReportService` sem chamador. (COM-44) [C]

## Segurança e operação
Credencial SMTP real como valor padrão em `queue/.../application.yml` (rotacionar, não repetir); padrões `admin/admin` (RabbitMQ) e `deziuser/deziuser` (banco); senhas em texto em e-mail e `email_controle`; remetente e endereços de suporte únicos e fixos no código; "enviado" = entregue ao SMTP sem exceção capturada. Parâmetros são **globais** (sem empresa) e lidos sem tratar ausência (NPE). (COM-46..49, seção 9) [C+B]
