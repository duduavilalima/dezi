# CAP-8 PDI e CAP-9 Feedforward (fonte: `08-pdi-feedforward.md`, prefixo RN-PDI)

## Ação de PDI (`pdi_acao`)
379 ações de 112 funcionários, 4 empresas [B]: 84 em aprovação, 249 aprovadas, 18 em andamento, 28 finalizadas; 360 criadas em 2020. **93% com prazo previsto vencido e paradas.**

- **Ciclo desenhado na tela:** EM_APROVACAO(1) → (aprovar) APROVADO(2) → (iniciar) EM_ANDAMENTO(3) → (finalizar) FINALIZADO(4) → "revisado" (marca). Sem volta. (PDI-07..13) [C]
- **Criar:** tela cria em EM_APROVACAO; servidor não define situação inicial nem valida perfil (regra original comentada: ADM/GESTOR_AREA/GESTOR_RH); botão sem condição de perfil ⚠. Validações só na tela: texto 2–500; datas previstas obrigatórias, início ≥ hoje, fim ≥ início. (PDI-01..03) [C]
- **Transições no servidor (`doBeforeUpdate`):** `APROVADO→EM_ANDAMENTO` e `EM_ANDAMENTO→FINALIZADO` só pelo **funcionário dono** (lido do corpo, não do registro); **qualquer outra atualização** só ADM/GESTOR_AREA/GESTOR_RH. Não há máquina de estados (saltos e retrocessos aceitos); dono pode alterar texto/datas/`revisado` junto. (PDI-07, 08, 14) [C] ⚠
- Na tela: aprovar para quem edita **exceto a própria ação**; iniciar/finalizar só o dono (data = hoje do navegador + observação); revisado para todos menos o dono (efeito: nenhum; 3 de 28 true); editar nas abas aprovação/aprovado/andamento, não finalizado, mantendo a situação. (PDI-09..13) [C+B] ⚠
- **Excluir:** `excluirAcao` apaga competências e ação **sem checagem** de perfil/empresa/situação; exclusão física, sem histórico; `doBeforeDelete` (só ADM/GESTOR_AREA/GESTOR_RH) não é usado e tem NPE. (PDI-15) [C]
- Prazo sem efeito (sem aviso, sem bloqueio); **nenhum e-mail ou mensagem** em qualquer transição; sem histórico de quem aprovou/quando; `criador/atualizador` = `<id>%<nome>`. (PDI-16..18) [C+B]

## Vínculos
- Vínculo com avaliação **opcional** (37% sem); gravado com **um participante** arbitrário da avaliação (primeiro da lista), só avaliações LIBERADA; serve só de rótulo (sem nota/texto). (PDI-04..06) [C+B] ⚠
- Competências: 0, 1 ou várias (63% sem); **detalhamento só com exatamente uma competência** (`validation.detalhamentoPDI`); tela oferece competências do cargo atual; servidor não valida cargo nem pertinência; 20% dos vínculos fora do cargo atual; competências são **vivas** (não congeladas); erro provável ao trocar competência na edição. (PDI-19..23) [C+B] ⚠

## Texto de PDI do consenso (`avaliacao_participante.pdi`)
- Campo "Ações de PDI" (≤500) no preenchimento, exceto Av1; gravado/regravado a cada gravação (inclusive nulo); **195 com texto, na prática do GESTOR** (AUTOAVALIACAO 0). Tela de PDI ("Anotações") mostra o texto só de avaliações LIBERADA (não CONCLUIDA, apesar de 37). **Sem ligação automática com `pdi_acao`.** (PDI-24..28) [C+B] ⚠

## Escopo, perfis e relatórios
- Tela PDI: menu para os 5 perfis; `canEdit` das ações = ADM/GESTOR_RH/GESTOR_AREA; da lista de funcionários e relatório individual = ADM/GESTOR_RH. Lista de funcionários = ATIVA via busca genérica; `obterColaboradoresPDI` sem chamador. (PDI-29, 30) [C]
- Escopo no servidor: ADM/GESTOR_RH/CONSULTOR_RH → empresa do funcionário da ação; GESTOR_AREA → seu departamento ou filho direto; demais só as próprias; filtro do gestor de área provavelmente defeituoso; listagens por id na URL sem checagem de visibilidade ⚠; avaliações do PDI (`obterAvaliacoesPDI`) por perfil, sem filtro de situação. (PDI-31..34) [C]
- **Relatório (pesquisa/Excel/PDF):** período filtra datas **reais** (ações sem início somem; previstas não filtráveis); Excel troca colunas (Setor=cargo, Cargo=departamento, Competência=nome do colaborador) e usa fim real nulo; PDF individual com observação inicial perdida (final duas vezes), período = maior início até maior fim, ano `YYYY`; imprime qualquer lista sem escopo. (PDI-35..38) [C] — defeitos, não replicar.

## Apontamento e feedforward (`apontamento_funcional`)
- Uma tabela, duas funções separadas por predicado (apontamento = `texto` não nulo; feedforward = `feedforward` não nulo); 19 registros (11 texto, 8 feedforward, nunca os dois), sem chave para PDI/avaliação. (PDI-39) [C+B]
- **Enviar** (mobile): avaliador = usuário logado; avaliado e competência do corpo; texto obrigatório; data do servidor; `graduacaoVisual = nota×2` (estrelas); sem editar/excluir; servidor não valida subordinação nem competência. Opções do mobile: subordinados diretos; tipos ativos do cargo do avaliado; competências do tipo na empresa. (PDI-40..42) [C]
- **Receber:** o avaliado vê tipos com feedforward e os textos com **nome e foto do avaliador**; sem "lido"/resposta/notificação. Endpoint web `POST /feedsRecebidos` usa o avaliado do corpo, sem usuário logado nem escopo, e **nenhuma tela o chama**. (PDI-43..46) [C] ⚠
- `empresa.feedforward` sem efeito no mobile (menu sempre inclui); 0 empresas com `true`, mas 8 gravados. Nenhuma relação feedforward↔PDI no código. (PDI-47, 48) [C+B] ⚠
