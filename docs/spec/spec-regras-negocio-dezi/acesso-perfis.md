# CAP-1 Acesso e perfis (fonte: `01-acesso-perfis.md`, prefixo RN-ACE)

## Autenticação
- **Identidade** = empresa + matrícula (`empresaId%matrícula`), matrícula única por empresa, sem diferenciar caixa. Só funcionário `ATIVA` autentica; a situação funcional não bloqueia (só `DESLIGADO` inativa a pessoa). (ACE-01..03) [C+B]
- Lista de empresas do login é pública, filtrada pelo host da requisição em produção; host sem empresa: web lista vazia, mobile lista todas. (ACE-04) [C]
- **Ordem do login:** (1) bloqueio de IP → 404; (2) bloqueio de usuário → 403; (3) senha BCrypt inválida → 401 e grava `AUTENTICACAO_INVALIDA`; (4) sucesso grava `LOGIN_OK` e devolve token. Sessão sem estado, token por chamada; validade e algoritmo vêm da `commons` (inacessível). **No novo:** matrícula inexistente e senha errada dão a mesma resposta (401) e contam tentativas por matrícula/empresa e IP (**D42**); sessão expira por inatividade e por tempo total, e trocar a senha invalida os tokens (**D41**); CORS restrito ao próprio sistema fica ao `bmad-architecture`. (ACE-05, 06) [C]
- Pós-login (tela): primeiro acesso (flag verdadeira ou nula) força troca de senha; depois formações/idiomas se `preencheFormacaoEIdiomas`; senão ADMINISTRADOR e GESTOR_RH vão a acompanhamentos, demais a avaliações. (ACE-07) [C]
- `GET /user/logged` devolve o usuário (sem hash) e **cria a configuração pessoal** se não existe (idioma `pt_BR`, fonte `medium`): leitura com escrita. `GET /user/roles` alimenta menus e botões. Defeito de tela: quem já está logado e abre o login sempre vai a avaliações. (ACE-08, 11) [C] ⚠
- Reautenticação do consenso (`seclogin`) valida empresa+matrícula+senha, **sem bloqueio nem log**. **No novo:** descartado; o aceite do consenso confirma a própria senha na própria sessão (**D53**). (ACE-09) [C]

## Bloqueios
- Por usuário: bloqueia se nos últimos **10 min** há 5+ `AUTENTICACAO_INVALIDA` ou um `USUARIO_BLOQUEADO`; cada tentativa bloqueada renova o bloqueio; senha correta também é recusada. Sucesso **não zera** falhas. **No novo (D42):** sucesso zera as falhas e o bloqueio expira na janela, sem se renovar a cada tentativa. (ACE-12, 14) [C]
- Por IP (par IP+usuário): mais de **25** falhas/bloqueios em **20 min** → `IP_BLOQUEADO`. (ACE-13) [C]
- `USUARIO_SUSPENSO` nunca é gravado; não há bloqueio manual nem desbloqueio por administrador. (ACE-15) [C]

## Senhas
- BCrypt; 766/766 no banco. Sem política de tamanho/complexidade/histórico/expiração. (ACE-17, 20) [C+B]
- Funcionário novo: senha aleatória temporária, `primeiroAcesso=true`; e-mail só se `notificacao.email.funcionario.novo=true` (**false por decisão**: a senha chega por boas-vindas ou "esqueci"). (ACE-18) [C+B]
- "Esqueci minha senha" (público): se há funcionário ATIVO com o e-mail na empresa, grava senha nova imediatamente, `primeiroAcesso=true`, envia por e-mail; e-mail inexistente → `validation.not-found-email`. (ACE-19) [C]
- Troca: exige senha atual correta, nova = confirmação; zera `primeiroAcesso`; usuário-alvo vem do corpo, não do logado. (ACE-20) [C] ⚠
- Boas-vindas em massa gera nova senha por destinatário. (ACE-21) [C] ⚠ quem aciona
- SMS de senha não existe. (ACE-22) [C]

## Perfis, permissão e escopo
- Um perfil por funcionário, uma autoridade `ROLE_<perfil>`. (ACE-23) [C+B]
- **Servidor impõe quase nada:** só `PainelControleResource` tem `@PreAuthorize` (ADM, GESTOR_RH, CONSULTOR_RH); a matriz abaixo é só de tela. (ACE-24) [C]
- **Matriz de tela** (ACE-25) [C]: Cadastros (unidade, tipo, cargo, depto, funcionário, escala, dimensão, competência): ver ADM/CONSULTOR_RH/GESTOR_RH, editar ADM/GESTOR_RH. Campanha/avaliação: ver ADM/CONSULTOR_RH/GESTOR_RH, criar ADM/GESTOR_RH. Acompanhamentos: ver +GESTOR_AREA, editar ADM/GESTOR_RH. PDI: todos veem; editar ADM/GESTOR_RH/GESTOR_AREA. Administração (parâmetros, host, mensagens): só ADM. Importação de planilhas e "Ajustar pesos": só ADM. Visibilidade de menu por empresa via parâmetro `menus.visibilidade.porEmpresaId.<id>` (3 empresas).
- **Regras novas de segurança (Red Team):** empresa sempre da sessão, id de outra empresa → 404 (**D51**); ninguém age como RH sobre a própria avaliação (**D52**); trilha imutável de ações privilegiadas (**D54**); link de senha de uso único, nunca para DESLIGADO, resposta igual, invalida sessões (**D56**). Política de senha, ordem entre perfis, administrador de plataforma: a confirmar (ELI-11..13).
- **Regras impostas no servidor** (ACE-26) [C]: funcionário ADMINISTRADOR só é alterado por administrador; elegíveis a avaliação excluem administradores; PDI (ver `pdi-feedforward.md`); acompanhamento (FUNCIONARIO recusado; GESTOR_AREA só seu departamento); só ADMINISTRADOR lista todas as empresas; campanhas: ADM/GESTOR_RH veem todas da empresa (**no novo, D43:** ADMINISTRADOR todas as empresas; GESTOR_RH e CONSULTOR_RH as da empresa; GESTOR_AREA as com participantes na sua árvore; FUNCIONARIO as em que participa). ⚠ promoção a ADMINISTRADOR pela API.
- **Escopo de dados** (`getServiceFilter`, ACE-27..29) [C]: ADM/GESTOR_RH/CONSULTOR_RH → empresa do usuário; GESTOR_AREA → seu departamento (um nível de filhos em funcionário/PDI; todos os descendentes em dado de preenchimento/painel; **no novo, D33:** toda a árvore em tudo); FUNCIONARIO → só ele. CONSULTOR_RH vê só a própria empresa. Filtro mobile de competência de avaliação sem escopo ⚠.
- "Gestor": vínculo `unidade_departamento.gestor` recriado ao salvar GESTOR_AREA/GESTOR_RH; `departamento_gestor` só para GESTOR_AREA (+descendentes). (ACE-30) [C+B] — detalhe em `cadastro-organizacional.md`.

## Auditoria
- `log_auth`: só `/auth/login` e checagens de bloqueio gravam. (ACE-32) [C] — **tabela inexistente na instância** [B].
- `log_entity` (`@LogRequired`): POST/PUT/DELETE de Avaliação, Participante, CompetenciaPreenchimento, AvaliacaoCompDet, ApontamentoMobile, DetalhamentoMobile, TrocaDeSenha; grava corpo JSON completo. **Cadastros e campanha não são auditados** (mudança de perfil sem trilha). Troca de senha é auditada com senhas em texto. (ACE-33..35) [C] — tabela inexistente na instância [B].

## BI (Pentaho)
- `GET /pentaho/login`: login técnico (credencial de serviço); usuário `<base>_<identificador>` se `versao_bi=1` e identificador preenchido; sessão é da conta técnica, o recorte vem de `bi_parametro_usuario`. 1 empresa na versão 1, 18 na 2. (ACE-36, 37) [C+B]

## Decisões do responsável (2026-10-01)
Ver `decisoes-e-defeitos.md` (D1..D3).
