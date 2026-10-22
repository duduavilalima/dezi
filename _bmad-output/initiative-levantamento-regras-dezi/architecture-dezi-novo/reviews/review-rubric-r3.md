# Revisão (rubrica), rodada 3, da espinha de arquitetura do DEZI novo

Espinha revisada: `architecture-dezi-novo.md` (AD-1..30, atualizada em 2026-10-21). A espinha não foi editada. Severidade: A = alta (gera divergência real entre unidades), M = média, B = baixa. As versões de tecnologia não foram reverificadas fora da espinha; avalio só a coerência do que ela declara.

## Veredito: PASSA COM RESSALVAS

Os três achados altos da rodada 1 (ciclo de liberação, dono da outbox, contador de corte) foram fechados de verdade. Restam duas divergências de severidade alta (grafo de dependências ainda incompleto; Adiado ainda permite divergência em armazenamento e exportação) e uma contradição interna nova no AD-7. Nenhuma é bloqueio de arquitetura, mas as três devem ser corrigidas antes de distribuir as unidades de implementação.

## Fechamento dos achados da rodada 1

| Achado r1 | Situação | Evidência / resíduo |
| --- | --- | --- |
| A-1 ciclo avaliacao/preenchimento, aresta de comunicacao, `resultados --> catalogo` | Fechado | Portas `PreparacaoDeRespostas` e `RespostasDoParticipante` (AD-19) e aresta tracejada no grafo; `comunicacao --> acesso_api` e `--> cadastro` presentes; `resultados --> catalogo` removida e coerente com AD-9. Resíduo: R-1 abaixo (outras arestas ausentes). |
| A-2 quem grava a outbox | Fechado | AD-13 agora diz que o evento (AD-21 canal 3) é a origem e o consumidor cria a linha; "mesma transação" vale só para o evento; AD-28 unifica a infraestrutura. |
| A-3 `versao_de_corte` | Fechado | AD-11 abandonou o contador: corte é o instante da transição para `CONCLUIDA` sob `FOR UPDATE`, que espera as gravações em `FOR SHARE` (AD-19). Coerente com AD-2 (preenchimento não escreve na linha da avaliação). |
| M-1 texto obsoleto de auditoria | Fechado no texto | "Regra do desenho", Convenções e AD-8 falam em porta síncrona. Resíduo: AD-8 não cita AD-21 em Binds; arestas para `auditoria` incompletas (R-1). |
| M-2 bloqueio por usuário | Parcial | Valores padrão existem (`[ASSUMPTION]`), 429 uniforme definido, desbloqueio do RH define o que limpa. Mas surgiu contradição interna (R-3). |
| M-3 Adiado: arquivos e relatórios; importação em dois módulos | **Aberto** | Ver R-2. |
| M-4 RLS sem plano alternativo, ops, LGPD | Parcial | Fechado: queda para camada 1 do AD-3 com nova decisão (item 6), AD-30 (LGPD), AD-29. Aberto: hospedagem do Node SSR, TLS/ingress, rotação do cofre, configuração por ambiente (R-6). |
| M-5 stack não verificada | Majoritariamente fechado | Versões com fonte e data. Resíduo: gerador de cliente OpenAPI e biblioteca TOTP ainda "a escolher" (R-5); Flyway fixado em 13.10.2 com a 13.11.1 existente, sem justificativa. |
| B-1 papéis e `set_config` vs `SET LOCAL`, login pré-contexto, ShedLock vs SKIP LOCKED, mapa CAP, tags | Parcial | AD-14 esclarece a infra única; AD-29 tabela de papéis. Aberto: R-4 (papéis, SET LOCAL, login), R-7 (mapa CAP), tags (R-8). |

## Achados

### R-1 (A). O grafo ainda não contém arestas que as regras exigem, e o AD-27 diz que aresta ausente é proibida
- **Autorização central (AD-4a).** A checagem é "um ponto" e vive em `acesso` ("política"), mas todo módulo precisa invocá-la e informar o dono do recurso lido do registro. O grafo só tem `cadastro --> acesso_api` e `comunicacao --> acesso_api`. Ou a checagem é kernel de `infra` (então diga isso e tire "política" de `acesso`), ou faltam arestas de `catalogo`, `avaliacao`, `preenchimento`, `resultados` e `pdi` para `acesso`. Hoje cada unidade resolve de um jeito.
- **Aceite do consenso com a senha da própria parte (AD-5, D53).** O consenso é de `preenchimento`, mas a verificação de senha é de `acesso`. Não há aresta `preenchimento --> acesso_api`. O mesmo vale para a reautenticação recente do administrador de plataforma (AD-7), que cruza módulos.
- **Auditoria.** O AD-8 audita "leitura de dado sensível (resultado individual, comentário)", que ocorre em `resultados`, e o AD-13 audita o reenvio manual do RH, que ocorre em `comunicacao`. Nenhum dos dois tem aresta `-- porta RegistroDeAuditoria --> auditoria`. O parágrafo "Regra do desenho" diz "Todo módulo chama `auditoria`", o que contradiz o diagrama.
- **Correção sugerida:** completar o diagrama (ou declarar a checagem como kernel) e citar o AD-21 no Binds do AD-8.

### R-2 (A). Adiado ainda permite divergência: armazenamento de arquivos, exportações e importação em dois módulos
- "Armazenamento de arquivos (foto, planilhas) e relatórios/exportações PDF/Excel" continua em Adiado, com "cada módulo gera o relatório dos seus dados". O AD-17 exige upload e processamento assíncrono, ou seja, o arquivo precisa existir entre as duas fases; o AD-1 coloca a importação em `cadastro` e em `avaliacao`. Sem uma porta de armazenamento e um pipeline únicos (e dono, provavelmente `infra`), cada módulo escolhe disco local, tabela ou objeto, o que quebra o requisito de backend sem estado (AD-14) com duas instâncias.
- Relatórios por módulo reproduzem o risco que o AD-12 e o AD-4 tentam evitar: cada exportação pode contornar o anonimato mínimo e o escopo. É preciso uma regra mínima: exportação só pela API que já aplica AD-4 e AD-12, biblioteca única, escape de fórmula (já no AD-17) comum.
- Este achado já constava na rodada 1 (M-3) e não foi tratado.

### R-3 (A). AD-7: o passo 2 da ordem do login contradiz a exceção do "IP sem falhas"
- O passo (2) manda ler `nao_antes_de` por (empresa, matrícula) e por (IP, empresa, matrícula) e, se estiver no futuro, responder 429 "sem verificar a senha". Mais adiante, o AD-7 diz que, durante o atraso por usuário, "a senha correta vinda de IP sem falhas é aceita". Com o passo 2 literal, o usuário legítimo em IP limpo recebe 429 e nunca chega à verificação. O objetivo declarado (terceiro não negar acesso) fica sem implementação. Duas equipes implementam de formas opostas. É preciso dizer que o passo 2 só barra pela chave (IP, empresa, matrícula), e que a chave por usuário apenas atrasa quem vem de IP com histórico de falha.
- A aplicação do bloqueio duro depende do IP real, que o AD-6 deixa "a confirmar na implantação"; a granularidade para IPv6 (por /64, por exemplo) não é definida, e rotação de endereço dentro do prefixo contornaria o contador. Se IP não for confiável, as famílias de chave por IP perdem o sentido.
- O item 4 de "Decisões que dependem do negócio" diz que "falta fixar os valores", mas o AD-7 já traz padrões `[ASSUMPTION]`; alinhar os dois textos.

### R-4 (M). Contexto de empresa: resíduos de papéis, `SET LOCAL` e login pré-contexto
- **Papéis.** AD-3 diz que as varreduras entre empresas "usam o papel de plataforma e só leem ids"; AD-20 e AD-29 dizem que quem faz isso é `app_descoberta`, e `plataforma_ro/rw` são só para endpoints de plataforma. Alinhar o AD-3 ao AD-29.
- **`set_config` vs `SET LOCAL`.** AD-3 especifica `set_config(..., true)`; AD-20 diz que ambos os caminhos "fazem só `SET LOCAL`". No AD-23, a liberação em `REPEATABLE READ` exige que a "primeira instrução" seja o `SELECT … FOR UPDATE`; um `SELECT set_config(...)` antes dele já estabelece o snapshot, enquanto `SET LOCAL` não. Fixar uma forma e dizer como se compatibiliza com a primeira instrução do AD-23 (o retry 40001 mascararia o erro, não o corrige).
- **Login antes da empresa conhecida.** O AD-3 diz que a empresa é resolvida por nome antes da sessão, mas a lista de tabelas sem RLS do AD-28 não inclui `empresa` nem diz como `credencial_acesso` é lida nesse instante (função `SECURITY DEFINER`? tabela `empresa` fora da RLS?). Os contadores de login estão na lista "sem RLS" (AD-28) mas o AD-7 os põe "em tabela própria de `acesso`" com chave (empresa, matrícula): dizer que a tabela não leva política. Pendência herdada de B-1 da rodada 1.

### R-5 (M). Contrato e repositórios: onde mora o OpenAPI e quem o gera
- O AD-15 chama o OpenAPI de "fonte", mas não diz se o servidor é contract-first (stubs gerados) ou code-first; o AD-18 fala em CI "nos dois repositórios" enquanto a Semente Estrutural lista três diretórios (`dezi-backend`, `dezi-frontend`, `api-contract`). Não está dito de qual repositório é o contrato nem como o build "de ambos" detecta divergência.
- O gerador do cliente Angular e a biblioteca TOTP seguem "a escolher", com suporte ao Angular 22 não confirmado. O AD-15 depende do gerador; é ponto de divergência real no frontend.

### R-6 (M). Dimensões do altitude ainda soltas: implantação e operação
- Decididas/adiadas/abertas: provedor, RLS, RPO/RTO, LGPD estão listados. Ainda sem posição nem item de pendência: hospedagem e orquestração do Node SSR, TLS e terminação no ingress (também de que o ingress define o IP real e `Cache-Control`, ambos usados em regras), rotação de segredos do cofre, gestão de configuração por ambiente, escala do pool de conexões com a reserva do canal 2 (AD-21) e os papéis do AD-29 (número de `DataSource`s).
- "Particionamento do banco" está em Adiado, mas o AD-21 exige partição mensal para auditoria e o AD-29 define um papel que derruba partições. Esclarecer em Adiado que o particionamento adiado é o das tabelas de negócio.
- "Volume medido" e "20 vezes o legado, poucos GB" (Adiado) não têm fonte na espinha; pela política do projeto, registrar a evidência (consulta ao banco real) ou marcar "a confirmar".

### R-7 (B). Mapa Capacidade → Arquitetura e entidades incompletos
- CAP-8 omite AD-25 (coordenada de PDI) e AD-8 (aprovação, D22); CAP-7 omite AD-9; CAP-6 omite AD-11 (gatilho do parcial) e AD-20/AD-21; CAP-1 omite AD-21 e AD-20; CAP-3 omite AD-4. CAP-9 continua só com AD-4: sem máquina de estados, sem dono de tabela e sem entidade no ER.
- Donos de tabela (exigência do AD-2/AD-19) não listados para `catalogo` (versões, escalas, perguntas), `comunicacao` (outbox) e `auditoria`. O ER também não mostra estas entidades.

### R-8 (B). Comportamentos e tags
- **AD-28 "adia ou descarta" por `sequencia`:** vale para todo consumidor? Descartar evento de menor sequência é seguro para o gatilho de parcial, mas perde e-mail se aplicado a eventos de comunicação (cada evento é uma mensagem distinta). Dizer por tipo de consumidor.
- **Advisory locks:** AD-7 (`pg_try_advisory_xact_lock` por matrícula) e AD-11 (`pg_advisory_xact_lock(avaliacaoId)`) usam o mesmo espaço de chaves bigint, com ids UUID. Definir a forma de duas chaves (classe, hash) para evitar colisão, e inserir os advisory locks na ordem global de locks do AD-19.
- **Tags:** AD-9, AD-10 e AD-14 seguem `[ASSUMPTION]` embora carreguem estrutura (snapshot, estados, jobs); o AD-19 a AD-30 estão `[ADOPTED]` mas surgiram nas rodadas de revisão. Pela definição da própria espinha ("escolhido pelo responsável"), confirmar com o responsável que cada um foi de fato adotado, ou marcar como `[ASSUMPTION]`.
- **AD-21 vs AD-20:** o canal 2 diz `REQUIRES_NEW`, e o AD-20 proíbe `REQUIRES_NEW` direto (só `novaTransacao`). Citar `novaTransacao` no AD-21.

## Cobertura contra o checklist

| Critério | Resultado |
| --- | --- |
| Pontos reais de divergência fixados | Maioria sim. Faltam: checagem de autorização central e arestas (R-1), armazenamento/exportação (R-2), local do contrato (R-5). |
| Rule aplicável e que previne a divergência declarada | Em geral sim, com testes por AD. Falha: AD-7 passo 2 vs exceção (R-3); AD-23 primeira instrução vs `set_config` (R-4). |
| Adiado sem divergência | Não: armazenamento/exportações (R-2), particionamento (R-6). |
| Tecnologia verificada | Quase: versões com fonte e data; gerador OpenAPI e TOTP não escolhidos (R-5). |
| Ratifica o legado quando aplicável | Sim: `support/` como referência de formato, RabbitMQ adiado com justificativa, esquema novo, D-numerais do spec; falta fonte para o "volume medido" (R-6). |
| CAP-1..10 | Cobertas no mapa; CAP-9 fina; AD faltando em várias (R-7). |
| Altitude (implantação, provedor, operação) | Provedor/RLS/RPO/LGPD decididos ou em aberto com dono; hospedagem do SSR, ingress/TLS, rotação de segredos, config por ambiente sem posição (R-6). |
| Consistência interna | Melhor que na rodada 1; restam R-1, R-3, R-4, R-8. |

## Pontos fortes confirmados nesta rodada

Isolamento em duas camadas com papéis tabelados (AD-3, AD-20, AD-29); corte do consolidado por transição sob lock, sem contador (AD-11, AD-19); outbox com envio em três tempos e segredo fora do corpo (AD-13, AD-22); infraestrutura assíncrona única (AD-28); snapshot versionado e idempotente (AD-23); LGPD com dono e itens "a confirmar" explícitos (AD-30); decisões abertas rastreadas por ELI e pelo negócio.
