# CAP-6 Participantes e preenchimento (fonte: `06-participantes-preenchimento.md`, prefixo RN-PRE)

Resposta = linha de `avaliacao_preenchimento` (participante × pergunta), criada **vazia na liberação**; até 3 graduações (`id_graduacao_av1..3`), 3 observações. 166.192 linhas [B]; todas com `tipo_client = WEB` (mobile sem rastro ⚠).

## Quem preenche o quê
- **Av1 (autoavaliação):** o **avaliado** responde se a avaliação tem `preenche_av1` e o participante `limite_av1 >= agora`; vale para perfis GESTOR e AUTOAVALIACAO. (PRE-01, 04) [C+B]
- **Av2 (avaliação):** o **avaliador** responde se `preenche_av2` e `limite_av2 >= agora`; subordinados e pares só respondem Av2. (PRE-02, 04) [C+B]
- **Av3 (consenso):** só participante GESTOR (limite_av3 em 2.692 de 2.759); avaliado e gestor concluem juntos. (PRE-05) [B]
- **Av4 (revisão):** sem tela, sem uso (limite nulo, revisor nulo, concordância nula). (PRE-33) [B] ⚠
- CONCLUIDA fica fora das listas (web e mobile); INATIVA fecha por prazo. Usuário só vê participantes em que é avaliador ou avaliado (exceção: acompanhamento). Resposta gravada **por competência** (todas as perguntas juntas). (PRE-06..08) [C]

## Gravação e marcadores
- Três caminhos de gravação (web por participante, web por pergunta para vários avaliados, mobile) com regras parecidas; mobile só grava Av1 e Av2. (seção 4, PRE-39) [C]
- Ao gravar graduação grava-se também o vínculo ao nível congelado; 1.181 respostas com nível e sem vínculo (ficam sem nota no BI). (PRE-09) [C+B]
- `data_inicio_avN` = primeira gravação com graduação da etapa; **`preenchida_avN` = total de perguntas == respostas com graduação** (só "tudo ou nada", recalculado a cada gravação; confiável em 99,9% [B]); `data_conclusao_avN` regravada a cada gravação com marcador verdadeiro. (PRE-11..15) [C+B]
- `preenchida_avN` nulo = etapa não se aplica; regra de nulo×falso não está no código ⚠. (PRE-16)
- `id_situacao_preenchimento` da avaliação nunca atualizado (defeito). (PRE-19) [C+B]

## Comentários
- **Comentário obrigatório** por nível (hint congelado; 554 de 27.988 hints) validado **só no front** (web e mobile); servidor grava sem observação. "Finalizar" exige graduação em todas as perguntas da etapa só na tela. (PRE-22, 24) [C+B]
- `observacao_avN` por etapa; comentários finais (avaliado, avaliador, revisor) e texto de PDI gravados no participante a cada gravação, sobrescrevendo (inclusive nulo). (PRE-21, 23, 26) [C+B]
- **Consenso herda a resposta quando Av1 = Av2** (pré-preenche Av3 na leitura; 53% das Av3). (PRE-25) [C+B]

## Consenso
- **Liberação automática** (web): `consenso_liberado=false` (não nulo) + gravação com `finalizou` + Av1 e Av2 completas. **Mobile recalcula a cada gravação** `= preenchidaAV1 && preenchidaAV2`, podendo desligar (diverge) ⚠. (PRE-27, 28) [C+B]
- **Liberação manual** (`liberaAvaliacaoConsenso`): zera marcadores Av3/Av4, `consensoLiberado=true`, limpa comentários finais, zera revisão; **não apaga graduações Av3**. Chamador/perfil ⚠. (PRE-29)
- Consenso exige **login da outra parte** na tela (matrícula e senha); servidor não exige. (PRE-31) [C] ⚠
- `consenso_iniciado` nunca é ligado pelo `web/` (328 verdadeiros ⚠). (PRE-32) [B]
- `atualizaAvaliadorAndRevisor` limpa Av2/Av3 (provável código morto). (PRE-30) [C] ⚠

## Prazos
- **Prazo só esconde perguntas na listagem; gravação não o verifica**; comparação com o instante atual, não fim do dia ⚠; 12% das Av2 foram gravadas após o limite (prorrogação ou outro caminho ⚠); tela de consenso não consulta `limite_av3`. Mobile mostra "dias restantes" pelo limite da **avaliação**. (PRE-34..38) [C+B]

## Mobile, percentuais e pendências
- Percentual por competência = respostas/perguntas; por tipo: competências 100% concluídas e perguntas respondidas; `Math.round`. (PRE-40) [C]
- **Pendências de preenchimento** (`listarPendencias`): Auto, Avaliações **ou** Consenso ≠ TOTALMENTE; Gestor/Subordinados/Pares não entram. Acompanhamento: GESTOR_AREA filtra por departamento obrigatório. (PRE-41, 42) [C]
- Participantes sem linhas de resposta em LIBERADA/CONCLUIDA: 27 ⚠. Remoção de participante apaga respostas e, em cascata, pergunta/hint/gabarito/escala congelados não usados. (PRE-44, 45)
- Regras de criação/edição de participante (`prepare/validate`): GESTOR recebe limites Av1/Av2/Av3; AUTOAVALIACAO Av1; demais só Av2; `revisado` sempre verdadeiro; erros de data obrigatória. (PRE-47) [C]

## Defeitos conhecidos
Copia-e-cola em `ParticipanteServiceImpl.doBeforeUpdate` sobrescreve Av1 com Av2/Av3/Av4 (PRE-18); início de Av2 gravado sem resposta Av2 no caminho por pergunta (PRE-11); `somaObjetos` pode devolver nulo (PRE-43).
