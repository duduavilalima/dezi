# CAP-5 Ciclo de vida da avaliação (fontes: `05a-avaliacao-estados-transicoes.md` RN-EST, `05b-avaliacao-criacao-agentes-participantes.md` RN-CRI)

## Estados
`AG_LIBERACAO`(1) → `LIBERADA`(2) → `INATIVA`(98) / `CONCLUIDA`(100); `CANCELADA`(99). Sem `CHECK` no banco. Em [B]: AG_LIBERACAO 10, LIBERADA 58, INATIVA 26, CONCLUIDA 50, **CANCELADA 0**. Todas as datas limite estão vencidas hoje.

| Transição | Regra | Evidência |
|---|---|---|
| criar → AG_LIBERACAO | situação AG_LIBERACAO, preenchimento NAO_PREENCH, empresa do usuário (EST-01) | [C] |
| AG_LIBERACAO → LIBERADA | `liberar` (abaixo) | [C] |
| LIBERADA → INATIVA | via update: `limiteAv1..3` forçados para **ontem** (Av4 não); 18 de 26 inativas coerentes (EST-17, 18) | [C+B] |
| INATIVA → LIBERADA | existe "Ativar" na tela (transição genérica da biblioteca `commons`); efeito nas datas ⚠ | [C] |
| → CONCLUIDA | "Concluir" na tela (avaliação LIBERADA); 50 usos; não exige participantes preenchidos nem Av2 (13 sem `limite_av2`) | [C+B] ⚠ |
| → CANCELADA | sem item de menu, sem endpoint, 0 usos; `CANCELADA` não pode ser atualizada (`validation.canceled`) e não tem consequências implementadas (participantes, preenchimentos, mensagens) | [C] — **entra no novo** (D2); regras em D26: de AG_LIBERACAO/LIBERADA, por ADMINISTRADOR/GESTOR_RH, irreversível com motivo, dados preservados e congelados, aviso aos participantes |
| excluir | só em AG_LIBERACAO; apaga participantes, mensageria, agentes, pendências e a avaliação (físico) (EST-25, 26) | [C] |

- `doBeforeTransition` não valida nada; só LIBERADA zera a situação de preenchimento. Gatilho e perfis das transições dependem da biblioteca `commons`. (EST-21, 22) [C] ⚠
- Efeitos de CONCLUIDA: mobile exclui; tela desabilita participantes; views de BI excluem INATIVA/CANCELADA. (EST-23, 24) [C]
- Bloqueios só de tela (não de servidor): LIBERADA desabilita edição de agentes, percentuais e datas e exclusão no servidor; `emLiberacao` não bloqueia ação alguma. (seção 5 de 05a)
- **Situação de preenchimento da avaliação é campo morto** (sempre 1); progresso real = percentual `(Av1+Av2+Av3 preenchidas)*100/(totais)`, Av4 fora. Nunca vai a TOTALMENTE_PREENCH. (EST-28..31) [C+B]

## Criar e editar
- Nome (`descricaoAbreviada`) único por campanha, sem caixa; nova avaliação exige ao menos 1 agente. Flags nulos viram `false` (criar: 8 avaliações com `autoavaliacao` nulo). (CRI-01..05) [C+B]
- **Datas por etapa:** combinações reais Av1+2+3 (78), Av1+2 (27), só Av1 (27), só Av2 (12); Av4 nunca. Sem validação de ordem entre datas. (CRI-09) [B]
- **Propagação de datas na edição:** GESTOR recebe Av1, Av2, Av3; AUTOAVALIACAO só Av1; demais só Av2 (outras nulas). Na gravação em lote todos os limites do participante são sobrescritos pelos da avaliação (contradiz) ⚠; 892 de 7.616 limites Av2 diferem. (CRI-10, 11) [C+B]
- Edição de avaliação LIBERADA: API não impede alterar agentes (só a tela). (CRI-16) [C]

## Agentes
- Trio (avaliação, tipo, perfil) com **peso** inteiro (0–100; imutáveis exceto o peso); **não soma 100** por perfil (somas 0, 30, 50, 80, 100, 300): é relativo e renormalizado (ver `resultados.md`). Remover agente apaga participantes cujo cargo passa a gerar pendência (laço defeituoso). (CRI-12..15, 22) [C+B] ⚠
- Flags de perfil (`autoavaliacao`, `gestores`, `subordinados`, `pares`): todas as combinações aceitas (13 em uso; mais comuns: auto+gestor 48, só auto 27, auto+gestor+sub+pares 24); nenhum perfil obrigatório. (CRI-17, 18) [B]

## Pendências (bloqueiam participante e liberação)
Cargo: `cargo-inactive`; sem tipos (`...-tipos-competencia`, `-ative`, `-avaliacao`); sem competências (`cargo-not-found-competencia`, `-ative`); sem perguntas (`-detalhamento`, `-ative`); `cargo-not-found-gabarito` (nenhuma aplicável); `cargo-not-found-gabarito-dimensao`. Avaliado: `avaliado-situacao-funcional` (≠ EM_ATIVIDADE; inclui recesso); `not-found-avaliador`. (CRI seção 5) [C]
- Participante com pendência é **apagado** se já existia, ou não gravado; pendências regravadas a cada salvamento; 19 em [B] (mais comuns `cargo-not-found-gabarito` e `not-found-avaliador`). (CRI-20, 21) [C+B]

## Participantes e avaliadores
- Gerados por flag: GESTOR (se há gestor EM_ATIVIDADE), um SUBORDINADO por subordinado direto ativo, e se gestores e subordinados desligados um participante com avaliador = avaliado (AUTOAVALIACAO se `autoavaliacao`, **senão PARES**: 24 casos ⚠). Autoavaliação com gestores vem de `salvarAvaliadores`. (CRI-25, 26, 34) [C+B]
- **Localizar gestor (`findAvaliador`):** gestor do par (unidade, depto) se existe e ≠ avaliado; senão sobe pelos departamentos ascendentes (primeiro existente, ignora o próprio avaliado e `DESLIGADO`, aceita recesso); só vira avaliador se EM_ATIVIDADE (recesso é descartado sem subir); sem nenhum → pendência. (CRI-39, 40) [C] ⚠
- Um par (avaliado, avaliador, revisor) por avaliação (`validation.already-exists`; índice único). (CRI-30) [C+B]
- `revisado=true` na geração automática, em avaliação LIBERADA e por propagação; LIBERADA exige participante revisado (`error-liberacao-revisados`). **Revisor: 0 usos** (CRI-27, 28) [B] ⚠
- Planilha de pares (`.xlsx`): avaliador na 1ª coluna, pares em seguida, por **nome exato**; nome não encontrado é **ignorado em silêncio**; homônimos ⚠. (CRI-36..38) [C]
- Remover avaliador/avaliado/todos sem restrição por situação nem preenchimento; em LIBERADA apaga também perguntas e respostas. Subordinados não são removidos ao desligar o flag. (CRI-31, 33, 41) [C] ⚠

## Liberar (`liberar`, assíncrono)
1. Marca `emLiberacao=true` (avaliação e campanha), roda em **thread separada**; home office usa `liberarHomeOffice`. (EST-02..04)
2. Pré-condições (abortam): ≥1 participante; sem pendências; cada cargo com ≥1 pergunta aplicável ativa; com autoavaliação, perguntas para o perfil do avaliador no cargo. (EST-06..09)
3. Com dimensões, filtra perguntas por dimensão; vazio → pendência sem efeito prático. (EST-10) ⚠
4. Congela `idCargoAvaliado`, `nomeCargoAvaliado`, `idDeptoAvaliado`, `nomeDeptoAvaliado` (só se nulos) e as perguntas (ver `modelo-competencias.md`). (EST-11)
5. Sucesso: LIBERADA, flags zeradas, e-mail "concluído" aos GESTOR_RH. **Falha: avaliação e campanha ficam presas em "em liberação"** (0 presas hoje). Mensagem "iniciada" não é enviada quando quem libera é ADMINISTRADOR. (EST-05, 12) [C+B]
6. Liberação incremental em LIBERADA libera só novos participantes. (EST-13..16)

## Cópia
`copiar`: cria nova avaliação na mesma campanha (nome novo único) com campos, agentes e só os participantes cujo cargo tem competência de algum tipo dos agentes; datas Av1..4 = da cópia; usa o cargo **atual**; descarta sem aviso quem não casa; `criador/dataCriacao` do original mantidos ⚠; dimensões não são copiadas. (CRI-43..47) [C]

## Dimensões e home office
- Dimensões: só com `campanha.dimensao=true`; sem uso real. (CRI-48) [B]
- Home office: fluxo próprio (`/homeofficebrasil`, ids fixos por configuração, funcionário criado com matrícula = e-mail); usado em 2020 (132 mensagens) embora `av_homeoffice` seja nulo; `ajustarAvalPesoCargoComp` reprocessa pesos. (CRI-49..52) [C+B] ⚠

## Encerramento
Não existe operação "encerrar": só datas limite por etapa, CONCLUIDA e inativação (datas = ontem). Provável encerramento por data. (CRI-53) ⚠
