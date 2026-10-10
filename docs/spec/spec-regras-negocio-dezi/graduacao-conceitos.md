# CAP-4 Escalas de graduação e conceito (fonte: `04-graduacao-conceitos.md`, prefixo RN-GRA)

Volumes [B]: 89 escalas (76 ATIVA), 440 níveis (71 NPA), 318 snapshots em 133 avaliações, 1.701 níveis congelados, `conceito` com **0 linhas**.

## Escala
- Escopo empresa. Nome único por empresa (código: sem caixa e com `trim`, inclusive inativa; banco: diferencia caixa). Tela: 2–80 caracteres. Só `ADMINISTRADOR`/`GESTOR_RH` criam/editam na tela. (GRA-01, 08) [C+B]
- Nova escala: `qtdGraduacoes=0`, `INATIVA`, `naoGraduar=true`, **dimensão = primeira dimensão da tabela inteira** (86 de 89 apontam para a dimensão 100; campo sem uso). (GRA-02) [C+B]
- Ao salvar com `naoGraduar=true` (padrão, travado na tela): `qtdGraduacoes=1`, ATIVA e cria nível NPA ("Não posso avaliar", ponto 0, inativo). (GRA-03) [C] ⚠ roda na edição?
- **`nao_graduar` e `qtd_graduacoes` não são confiáveis** (52 de 89 divergem); o que vale é existir nível ativo com o texto exato "Não posso avaliar". (GRA-04, 05) [C+B]
- Criar nível reativa a escala; inativar escala não verifica uso nem inativa níveis (10 escalas inativas usadas por 476 perguntas); só esconde da escolha ao criar/editar competência; a liberação não olha a situação. Escala não é removível. (GRA-06, 07) [C+B]

## Níveis
- `label` ≤ 40 obrigatório e **único na escala** (sem caixa, inclusive inativos); `ponto` inteiro obrigatório (tela: 1–1000, ou 0 travado se já 0; dados 0–120); `hint` livre. (GRA-09, 10) [C+B]
- Nasce ATIVA; após criar/ativar/inativar a ordem é renumerada: ativos 1..n com o NPA ativo (pelo texto) por último; inativos n+1.. por `ponto` crescente. Subir/descer troca `ordem` de dois níveis. (GRA-11, 12) [C]
- Ativar/inativar sem validação (dá para zerar a escala). (GRA-13) [C+B]
- **Editar nível reescreve o texto (`hint`) de todas as perguntas que o usam**, apagando textos personalizados (807 de 14.386 seriam sobrescritos); rótulo e ponto não são copiados; avaliações liberadas não mudam. (GRA-14) [C+B]
- Remover nível bloqueado se algum hint o usa; FK impede apagar nível congelado. Nível sem filtro de empresa no servidor. (GRA-15, 16) [C+B] ⚠

## Pontos, NPA e cor
- Escala de pontos livre: não exige crescimento com a ordem (15 escalas **decrescem**), nem distintos (15 com repetidos). Máximo ativo: 100 (43), 120 (15), 50, 5, 10. (GRA-18) [B] ⚠
- **`ponto = 0` = "sem nota"**: NPA e **escala de comentário** (5 escalas "Comentário" com um nível "Comentar"; pergunta só de texto). Não há tipo explícito. (GRA-19) [C+B] ⚠
- **Cor:** a gravada em `graduacao` não é editável por tela (só importador, ordens 1–5); a vista no preenchimento vem de **paleta fixa por ordem** (`GraduacaoCorEnum`, 11 cores; sem ordem ou >10 = preto), web e mobile. Web ordena por `ordem`, mobile por `ponto` (diferem em escala decrescente). (GRA-20, 20a) [C+B] ⚠ BI usa cor?

## Cópia congelada na liberação
- Para cada par (escala, avaliação) copia descrição, `nao_graduar`, dimensão, empresa e **todos os níveis (ativos e inativos)** para `graduacao_competencia`; reaproveita o snapshot se já existe (níveis novos não entram). Depois da liberação a avaliação não acompanha o cadastro (cadastro vivo já difere em 5 rótulos, 4 pontos, 120 ordens, 29 textos). (GRA-21, 22) [C+B]
- Acha-se a escala pelo par (escala da pergunta, avaliação) e o nível por (snapshot, id original); caminhos alternativos esperam resultado único (`FIXME`). (GRA-23) [C] ⚠
- A resposta guarda nível vivo (`id_graduacao_avN`) e congelado (`id_graduacao_comp_avN`); 235 Av1, 883 Av2 e 63 Av3 têm nível sem vínculo; 23 Av2 apontam nível congelado divergente. (GRA-24) [B] ⚠
- Home office tem cópia própria; ordem nula ou >11 → NPE (1 nível de cada, inativos). (GRA-25) [C+B] ⚠

## Conceito
- **Não existe no legado:** tabela vazia, nenhuma regra nota→conceito, nenhuma tela usa, sem validação de faixa. Só lista conceitos da empresa. Entidade usa `TipoComp` e TO usa `TipoConceito` (códigos 1–3) — inconsistente. A faixa de nota→conceito do roteiro **não existe**. (GRA-26, 27) [C+B] ⚠ decisão: entra no novo?
