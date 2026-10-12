# CAP-7 Resultados, gráficos e acompanhamento (fonte: `07-resultados-graficos.md`, prefixo RN-RES)

## Onde a nota é calculada
- **Decisão D24 (2026-10-10):** o Pentaho não é reaproveitado. O que está abaixo sobre ETL, `bi_*`, relatórios e acesso ao BI descreve o **legado**; o novo sistema define o próprio cálculo e relatórios, e a fórmula deduzida serve só de referência de comparação.
- **O `web/` não calcula a nota dos relatórios**: as telas só obtêm uma chave e abrem o Pentaho. O cálculo está no ETL externo (fora do repositório) sobre a view `conjunto_resultados` e as tabelas `bi_media_*`; a fórmula abaixo é **deduzida do banco** [B-inf]. (RES-01) 
- Cálculo Java de gráfico GAP é **código morto e zerado** (nota=0, peso=0); endpoints sem chamador. **Não replicar.** Evolução histórica é relatório do Pentaho. (RES-02, 03, 30) [C]

## Base: `conjunto_resultados` (uma linha por participante × pergunta)
- Entram respostas com graduação **Av2 ou Av3**, de avaliações ≠ INATIVA/CANCELADA; a view **real** ainda filtra `campanha.id_situacao = 1` (ausente da migração v3). Perfis GESTOR, SUBORDINADO, PARES; **autoavaliação nunca entra em médias** (só `pontuacao_auto` na linha do gestor); avaliações só de autoavaliação (27) usam outro relatório. (RES-04, 05) [C+B]
- `pontuacao_final = NULLIF(COALESCE(ponto Av3, ponto Av2), 0)`: **consenso prevalece**; ponto 0 vira nulo e sai das médias; consenso NPA exclui a pergunta, sem cair para a Av2 (**D37**). (RES-06) [C]
- Pontuação vem da graduação **congelada**; resposta sem vínculo fica sem nota (defeito; no novo toda resposta aponta para a graduação congelada, D23). (RES-07) [C+B]
- Junção interna com agentes (perfil sem agente para o tipo não entra). Departamento e cargo do avaliado vêm do **snapshot** do participante. (RES-08, 09) [C]

## Fórmula deduzida [B-inf]
1. Média do agente por pergunta = média de `pontuacao_final` por (avaliação, avaliado, tipo, competência, pergunta, agente).
2. Nota da pergunta = média dos agentes **ponderada por `peso_agente`**, só entre agentes que responderam (renormaliza; agente ausente não reduz a nota).
3. Competência = média **simples** das perguntas.
4. Tipo = média das competências **ponderada por `peso_cargo_competencia`**.
5. Nota final = média dos tipos **ponderada por `peso_cargo_tipo_competencia`**.
Aderência: 99,9% a 100% nos níveis 1–5; média por agente na avaliação 95% (resto ⚠). (RES-11, 12) Escala **não normalizada** (pontos brutos 0–120) e soma de pesos 0 → divisão por zero: **no novo, D38** normaliza por escala, peso 0 exclui o item e vazio = "sem nota". Casas decimais por empresa (`bi_parametro_empresa`, padrão 2) lidas só pelo Pentaho. (RES-13, 14)

## Pesos de cargo: duas fontes
- O BI lê `aval_peso_cargo_comp` (uma linha por resposta) por LEFT JOIN; sem linha, pesos = **1**. Só é preenchida pelo botão "Ajustar pesos" (`POST /avaliacao/{id}/ajustarpesos`: ignora o `{id}`, percorre todas as avaliações, grava o peso **atual** do cargo atual do avaliado, 1 se ausente; sem `@PreAuthorize`). A liberação não grava. (RES-16, 17) [C]
- **18% das respostas (24.483) sem linha** → peso 1; 1.342 respostas com linha duplicada (peso em dobro). O snapshot `avaliacao_det_cargo_comp` já guarda os pesos mas o BI não o usa. Fonte única no novo: pesos congelados na liberação (**D15**); `ajustarpesos` descartado (**D40**). (RES-18..21) [B]

## Acesso ao BI
- `GET /pentaho/login` (login técnico) e `GET /parametrousuario/criarparametros/{campanhaId}/{idFuncionario}`: grava `bi_parametro_usuario` (funcionário, perfil, empresa, campanha, filtros `nome_gestor`/`nome_funcionario`) e devolve chave = SHA-256(milissegundos + id). Escopo por **nome**; `idFuncionario` do cliente sem checagem; chave não expira (4.596 linhas). (RES-22, 23) [C+B] **No novo:** escopo no servidor pela sessão (**D39**) e acesso por perfil (**D40**).
- Versão do BI por empresa: v1 monta o escopo na URL (controlado pelo navegador); v2 usa `chave`. Relatório: campanha só de autoavaliação → `avaliacao.wcdf`; v1: ADM/GESTOR_RH `resultados`, GESTOR_AREA filtra por nome, colaborador `resultados_colaborador`; v2: `resultados` para todos; histórico `graficos_evolucao`. (RES-24, 25) [C]

## Painel de controle / acompanhamento (`acompanhamento/*`)
- **No novo (D52):** quando o avaliado é o próprio gestor, vale a regra do FUNCIONARIO (consolidado, sem identificar avaliadores). ELI-2 e ELI-14 pendentes.
- Escopo: GESTOR_AREA só participantes cujo **avaliador** é do seu departamento; FUNCIONARIO recusado; demais veem a empresa. (RES-35) [C]
- Progresso **por avaliador** somando todos os seus participantes. Gráfico Av1/Av2: totalmente se Av2==total e (sem autoavaliação ou Av1==total); parcialmente se Av2>0 e Av1>0; senão não preenchido. Consenso: só quem tem `limite_av3`. (RES-36..38) [C]
- Percentual = feitos×100/total (arredondado); lista: TOTALMENTE se percentuais existentes todos 100 (ausente conta 100). Filtro por situação aplicado depois do cálculo. (RES-40..42) [C]
- **Defeitos:** `Integer ==` marca como parcial quem tem >127 respostas (até 298 casos); percentual da autoavaliação copiado da avaliação; lista vazia dá TOTALMENTE. (RES-39..41) [C+B]
- Mensagem de acompanhamento: uma `Mensageria` por colaborador; e-mail se marcado. (RES-43) [C]
- `dado_preenchimento` (view, 103.789 linhas) e `progresso_preenchimento` são fontes de progresso independentes; o novo adota uma fonte única de progresso. Painel (`avaliacao/{id}/chart/*`, `@PreAuthorize` ADM/GESTOR_RH/CONSULTOR_RH) sem uso no front: gauge, percentual por fase (janela arbitrária de 2 anos), linha do tempo. (RES-32..34, 44) [C]
