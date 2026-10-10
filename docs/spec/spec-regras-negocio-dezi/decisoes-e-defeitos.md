# Decisões do responsável e defeitos do legado a levar ao sistema novo

Política: **não corrigir o legado**. Cada defeito abaixo é requisito de redesenho do sistema novo. Códigos `RN-xxx` remetem a `docs/regras-negocio/`.

## Decisões do responsável (2026-10-01; fontes 00, 01 §11.1, 05a §7.1)
- **D1** `notificacao.email.funcionario.novo=false` é **intencional** (RN-ACE-18): funcionário novo não recebe a senha por e-mail; ela chega por boas-vindas ou "esqueci minha senha".
- **D2** **`CANCELADA` entra no sistema novo.** Gatilho, perfil e consequências (participantes, preenchimentos, mensagens) a definir (RN-EST-21).
- **D3** **Concluir, inativar, reativar e cancelar avaliação: `ADMINISTRADOR` e `GESTOR_RH`** (as quatro ações, mesmos perfis). **`CONSULTOR_RH` somente leitura.** `GESTOR_AREA` e `FUNCIONARIO` nenhuma. **Imposto no servidor.**
- **D4** Falha na liberação (flags `emLiberacao`/`avEmLiberacao` presas) é defeito: redesenhar com estado de erro ou reversão, acompanhamento e idempotência (RN-EST-12).
- **D5** `id_situacao_preenchimento` (sempre 1) é campo morto: o sistema novo tem um estado de preenchimento consistente e único (RN-EST-31, RN-PRE-19).

## Decisões do responsável (2026-10-09; passo 5, lotes de escopo e de verificação)
- **D6** **Dimensões não entram** no sistema novo (MOD-9; 0 usos e 0 vínculos no banco). Descartar a estrutura; EST-5 e a parte de CRI-8 sobre dimensões ficam sem objeto.
- **D7** **Revisor/Av4 não entra** (PRE-10). O processo de preenchimento é Av1 autoavaliação, Av2 avaliação e Av3 consenso.
- **D8** **Conceito não entra** (GRA-14; tabela com 0 linhas). Vira não-objetivo.
- **D9** **PDI entra, com ciclo de vida revisto** (PDI-7; 93% das ações paradas, 360 de 379 criadas em 2020). Quem cria e quem aprova ainda a decidir (PDI-1, PDI-3).
- **D10** **Só `ADMINISTRADOR` atribui o perfil `ADMINISTRADOR`** (ACE-6); cada perfil só é atribuído por perfil igual ou superior e ninguém altera o próprio perfil. Imposto no servidor.
- **D11** **O gestor do departamento é dado próprio de (unidade, departamento)** (ORG-1, ORG-4): o RH o designa explicitamente e salvar funcionário nunca o altera. Perfil de acesso fica separado do papel de gestão. Conjunto de subordinados (ORG-4) ainda a decidir.
- **D12** **Excluir avaliador ou participante com resposta é bloqueado** (CRI-5); só substituir ou inativar, mantendo o histórico.
- **D13** **A avaliação encerra por data e por estado** (CRI-10): a data limite fecha o preenchimento sozinha; `CONCLUIDA` é ação explícita de ADMINISTRADOR e GESTOR_RH (D3) que consolida os resultados.
- **D14** **Peso cargo×tipo é relativo (normalizado na conta), com tela de edição na tela do cargo** (MOD-5); não exige soma 100 nem migração dos 247 cargos. Mesma semântica do peso dos agentes.
- **D15** **Os pesos da nota são congelados na liberação** (RES-3) e são a fonte única do BI: sem rotina manual de ajuste (`ajustarAvalPesoCargoComp`), sem duplicatas e sem resposta sem peso.
- **D16** **O servidor valida comentário obrigatório por nível e "todas respondidas" ao finalizar a etapa** (PRE-4); o front só antecipa o aviso.
- **D17** **O servidor recusa gravação após o prazo da etapa** (PRE-5); só ADMINISTRADOR e GESTOR_RH prorrogam a data.
- **D18** **`ENVIADO` significa aceito pelo SMTP** (COM-1): falha vira `ERRO` visível, reenviada com limite de tentativas; publicação na fila só depois do commit.
- **D19** **Boas-vindas enviam link de definição de senha com expiração** (COM-11); nunca redefinem a senha de quem já usava o sistema nem enviam senha em texto.
- **D20** **Subordinados de um gestor = toda a árvore** (ORG-4): todos os funcionários ativos dos departamentos que ele chefia, incluindo descendentes. Recesso e desligado ficam fora da avaliação mas continuam listados.
- **D21** **Pergunta só de comentário é um tipo explícito** (GRA-6): ESCALA ou COMENTARIO; comentário não tem nível nem nota e fica fora das médias. "Não posso avaliar" vira atributo do nível, não texto nem `ponto = 0`.
- **D22** **PDI: o funcionário propõe a própria ação (`EM_APROVACAO`) e o gestor direto aprova; ninguém aprova o próprio PDI** (PDI-1, PDI-3). Gestor e RH também podem criar para o funcionário. Imposto no servidor, com o dono lido do registro (D9).
- **D23** **Catálogo congelado por versão na liberação** (MOD-10): quem entra depois recebe a mesma versão da avaliação, não o catálogo atual; todos os participantes veem as mesmas perguntas.
- **D24** **O Pentaho não é reaproveitado** (RES-1): ETL, relatórios `.wcdf` e tabelas `bi_*` ficam fora do sistema novo. O Pentaho só totalizava resultados e gerava relatórios. O sistema novo define por conta própria como calcular e apresentar resultados e relatórios, com abordagem moderna (decisão técnica do `bmad-architecture`). A fórmula deduzida do banco (`resultados.md`) é só **referência do legado** para comparar resultados, não contrato; o negócio valida a fórmula do novo (inclusive escala, pesos zerados e NPA, RES-2, RES-5, RES-7).
- **D25** **A biblioteca `commons` está definitivamente inacessível.** O que só ela explicaria (token, escopo padrão, transição genérica, transação do `save`, efeito de INATIVA→LIBERADA sobre as datas) não será confirmado no legado: vira decisão de projeto do sistema novo, nunca fato do legado. Itens que dependem dela ficam marcados "sem fonte (commons)".
- **D26** **Cancelamento de avaliação** (ACE-2, EST-1; detalha D2 e D3): 
  - **Quem:** `ADMINISTRADOR` e `GESTOR_RH` (D3), imposto no servidor.
  - **De que estados:** `AG_LIBERACAO` e `LIBERADA`. `CONCLUIDA` e `INATIVA` não cancelam.
  - **Dados:** participantes e respostas são preservados para auditoria; a avaliação fica somente leitura e sai dos resultados, do progresso e do acompanhamento.
  - **Efeitos:** ações de PDI vinculadas continuam válidas (vínculo histórico); participantes com pendência recebem aviso de cancelamento e a pendência some.
  - **Reversão:** irreversível (estado final). Exige motivo e registra quem, quando e por quê. Para refazer, copia-se a avaliação.

## Defeitos por módulo
**01 Acesso (RN-ACE-12):** autorização por perfil também no servidor; `seclogin` com bloqueio e log; senha nunca enviada nem registrada em texto; "esqueci minha senha" por link com expiração, sem revelar se o e-mail existe e sem derrubar a senha de terceiros; política de senha; desbloqueio e suspensão manuais; auditoria dos cadastros (principalmente perfil); chave do BI imprevisível; quem pode definir ADMINISTRADOR.

**02 Cadastro (RN-ORG-11):** gestor como dado próprio, não efeito de salvar pessoa; separar perfil de acesso de papel de gestão; recesso com término automático; unicidade garantida no banco (e-mail, código do cliente, nome da unidade); departamento, cargo e unidade inativos com efeito definido sobre pessoas; controle de ciclo nas hierarquias; escopo no servidor para foto, apontamento e empresa; validação de ponteiros nulos.

**03 Competências (RN-MOD-10):** exclusão lógica do gabarito com histórico (reativar restaura); reativação de pergunta não reativa competência em silêncio; unicidade no banco (tipo, cargo×tipo, hint, dimensão); regra única de cascata de situação; detalhamento deve pertencer à competência do cargo; semântica explícita dos pesos (3 níveis, nenhum percentual, tela para o do tipo); modelo de graduação só na pergunta; descartar ou refazer dimensões; **congelar o catálogo com versão** (quem entra depois não lê o catálogo atual).

**04 Graduação (RN-GRA-10):** dimensão da escala sem sentido (descartar); `qtd_graduacoes`/`nao_graduar` derivar ou descartar; NPA como atributo próprio (não texto); unicidade sem diferenciar maiúsculas; faixa de ponto e consistência com a ordem no servidor; escala de comentário como tipo explícito; editar nível sem apagar texto personalizado; inativar escala e nível com efeito definido; cor em um só lugar e ordem de exibição única entre web e mobile; congelar por versão sem NPE nem vínculos ausentes/divergentes; conceito: decidir se existe.

**05 Avaliação (05a §7.1, 05b §11):** ver D4 e D5; validação de datas entre etapas; uma única regra de propagação de datas por perfil; avaliador de recesso sobe de nível; confirmação antes de apagar participantes com preenchimento; relatório de nomes não encontrados na planilha de pares.

**06 Preenchimento:** validar no servidor comentário obrigatório, "todas respondidas", prazo e login do consenso; marcadores `preenchida_avN` sem nulo ambíguo; liberação de consenso igual em web e mobile; sem sobrescrever Av1 (copia-e-cola); `tipo_client` correto.

**07 Resultados (RN-RES nos §4, 5 e 7):** peso de cargo congelado na liberação como **fonte única** (sem ação manual, sem duplicatas); percentual do painel por igualdade ao total (`Integer ==`) e autoavaliação correta; GAP zerado não replicar; escopo do BI por id (não por nome) e `idFuncionario` validado; chave com expiração; casas decimais e escala normalizada ou explícita.

**08 PDI (§9):** máquina de estados e permissões no servidor (criar, aprovar, iniciar, finalizar, revisar, excluir) com dono lido do registro; datas definidas pelo servidor e histórico de transições; exclusão lógica com escopo; prazo com efeito (ou sem prazo, por decisão); vínculo da ação com a avaliação (não participante arbitrário) e ligação opcional com o texto de consenso; competência/detalhamento validados contra cargo; unicidade no banco; relatórios Excel/PDF corretos; separar apontamento de feedforward e decidir o destino de `empresa.feedforward`; feedforward recebido sempre pelo usuário logado.

**09 Comunicação (RN-COM seções 4, 6, 7, 10):** estado de envio confiável (entrega confirmada, falha de SMTP visível, reenvio de tudo que falhou, sem estado compartilhado no consumidor); publicar na fila só depois do commit; troca de senha com e-mail ou remover o parâmetro; respeitar "Notificar via e-mail" por destinatário e o valor do parâmetro; mensagem de sistema como registro próprio; RH avisado do erro de liberação; assunto e destinatários da conclusão pela empresa da campanha; modelos de e-mail com dono único; links padronizados com HTTPS; sem senha em texto em e-mail nem em `email_controle` (link de definição de senha); remetente e endereços de suporte por configuração; **rotacionar a credencial SMTP exposta** e retirar segredos do repositório; autorização no servidor para mensageria (usuário logado, não `userId` da URL).
