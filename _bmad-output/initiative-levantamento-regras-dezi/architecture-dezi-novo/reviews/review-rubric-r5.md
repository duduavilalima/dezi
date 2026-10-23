# Revisão (rubrica), rodada 5, da espinha de arquitetura do DEZI novo

Espinha: `architecture-dezi-novo.md` (AD-1..30, draft, 2026-10-21). Não editada. Severidade: A alta, M média, B baixa. Versões não reverificadas.

## Veredito: PASSA COM RESSALVAS

Os seis achados pedidos foram fechados no essencial. Restam uma contradição interna nova (AD-21 x AD-29 sobre o dono do expurgo), um papel sem definição (o "papel do job" do AD-29) e a altitude de implantação ainda sem posição. Nenhum bloqueia a distribuição de unidades; corrigir antes.

## Fechamento dos achados da r4

| Achado r4 | Situação | Evidência / resíduo |
| --- | --- | --- |
| N-1 ciclo acesso/cadastro no grafo | Fechado | Aresta tracejada removida; "Regra do desenho" explica que `cadastro --> acesso_api` cobre a implementação das portas (AD-19). `EliminacaoDeTitular` declarada por `cadastro`, implementada por `comunicacao` e `preenchimento`, que já têm `--> cadastro`. Sem ciclo. Resíduo: M-3. |
| N-2 `empresa` sem RLS | Fechado | AD-3 e AD-28 listam `empresa` como exceção com teste de esquema; AD-20 troca a leitura de `infra` por view `empresa_ativa` publicada por `cadastro` e concedida a `app_descoberta`. Resíduos B-2. |
| N-3 view de escopo | Fechado | AD-25: `escopo_visivel(empresa_id, ator_funcionario_id, ...)`, `security_invoker = true`, teste de esquema, porta implementada sobre a mesma view. Resíduo: a exceção ao AD-2 é SQL e o ArchUnit não a vê; o AD-25 só diz "teste de esquema" (B). |
| N-6 contador de sequência | Fechado | `infra_sequencia` própria, última na ordem de locks, consumidor avança sobre tipos que não assina, `infra_consumo`, alerta de lacuna. Resíduo: AD-19 não lista `infra_sequencia` na ordem global (B). |
| N-7 papéis / dono das partições | Parcial | AD-29 reescrito (`dono_auditoria`, `migracao`), mas ver M-1 e M-2. |
| N-5 `set_config` x `SET LOCAL` | Parcial | AD-23 passou a dizer que o `SET LOCAL` do AD-20 "é esta mesma chamada" (reconcilia o sentido), mas AD-20 ainda escreve "só `SET LOCAL`" e AD-3 `set_config(..., true)`. Não há mais divergência de comportamento; falta uma frase única (B-1). |
| N-8 tags | Aberto (B) | AD-9, AD-10 e AD-14 seguem `[ASSUMPTION]`. O memlog confirma que AD-9/10 foram confirmados em 2026-10-21 e só o AD-14 ficou fora; a legenda ainda fala em sessões até 2026-10-20 (B-3). |
| Papéis (contradição AD-3/AD-29) | Fechado | AD-3 remete a `app_descoberta` e à plataforma conforme AD-29. |
| R-5, R-6, R-7 (r4) | Abertos | Ver M-4 e B-4. |

## Achados novos / residuais

### M-1. AD-21 contradiz AD-29 sobre quem é o dono do expurgo
AD-21 diz "função `SECURITY DEFINER` do papel de migração"; AD-29 diz que as funções pertencem a `dono_auditoria` (`NOLOGIN`) e que `migracao` é dono das tabelas de negócio. O memlog (decisão 5 de 2026-10-21) registra "papel de migração", e a r5 introduziu `dono_auditoria` sem registrar a decisão. Alinhar AD-21 ao AD-29 e registrar que a decisão (5) foi substituída. Quem cria a tabela-mãe de auditoria no Flyway (rodado por `migracao`) para que o dono seja `dono_auditoria` também não está dito.

### M-2. AD-29: "papel do job" não existe na tabela de papéis
`EXECUTE` das funções de partição vai "só ao papel do job", mas a lista traz `app_rw`, `app_descoberta`, `plataforma_ro/rw`, `dono_auditoria` e `migracao`. O AD-3 diz "jobs e plataforma usam papel próprio" e o AD-20 roda jobs sob `paraEmpresa` com o papel da aplicação. Dizer qual papel executa o job de partições (um `app_jobs`, ou `app_rw`), com o teste de privilégios correspondente.

### M-3. AD-30: a eliminação do titular não cobre todo módulo com texto livre pessoal
`EliminacaoDeTitular` é implementada só por `comunicacao` e `preenchimento`. `pdi` (ações, apontamento e feedforward, CAP-9), `avaliacao` (justificativa de cancelar e reabrir, AD-8) e `resultados` (comentários) guardam texto livre sobre pessoas, e o AD-30 exige "toda coluna marcada pessoal tem tratador". `pdi --> cadastro` e `avaliacao --> cadastro` já existem no grafo, então basta listá-los como implementadores. Sem isso o catálogo de dados pessoais pode ficar incompleto.

### M-4. Altitude: implantação e operação ainda sem posição (r4 R-6, aberto)
O que tem posição: provedor e RLS (pendência com dono), RPO/RTO (pendência), LGPD, cofre, TLS ao banco, duas instâncias atrás de balanceador (provisória). Sem decisão, adiamento ou pendência com dono: hospedagem do Node SSR (contêiner, escala, estado zero está no AD-6, mas onde roda não); TLS e terminação no ingress, que também é a fonte do IP real (AD-6 "a confirmar na implantação") e do `Cache-Control`; dimensionamento dos pools com ao menos quatro `DataSource`s/papéis (`app_rw`, plataforma, canal 2 reservado, `app_descoberta`); rotação de segredos além de SMTP e `migracao`; configuração por ambiente (só uma linha da tabela de convenções). "Particionamento do banco" em Adiado continua sem dizer que é o das tabelas de negócio (a auditoria já é particionada, AD-21), e "volume medido" e "20 vezes o legado" não têm fonte. Registrar cada um em Adiado ou "Decisões que dependem do negócio".

### B-1. `SET LOCAL` x `set_config` (AD-3, AD-20, AD-23)
Uniformizar: "`set_config('app.empresa_id', id, true)`, equivalente parametrizável de `SET LOCAL`", em AD-3 e AD-20, e dizer no AD-23 que a primeira consulta de dados é o `FOR UPDATE` (o `set_config` já fixa o snapshot do `REPEATABLE READ`, tratado pelo retry de `40001`).

### B-2. `empresa`: colunas e fuso
AD-3 diz que a tabela "expõe só `id`, `situacao` e fuso"; tabela sem RLS não restringe colunas, só `GRANT` por coluna ou view. AD-20/AD-29 citam `empresa_ativa(id, situacao)` (sem fuso). Dizer qual é o mecanismo para `ResolucaoDeEmpresa` e se o login abre `paraEmpresa` com motivo.

### B-3. Tags e legenda
Alinhar a legenda ("2026-10-13 a 2026-10-20" para 2026-10-21) e promover AD-9/AD-10 a `[ADOPTED]` conforme o memlog, ou explicar por que seguem `[ASSUMPTION]`.

### B-4. Contrato e mapa (r4 R-5, R-7, abertos)
OpenAPI sem dizer contract-first ou code-first; AD-18 "nos dois repositórios" x três diretórios da Semente (`api-contract/`); gerador Angular e TOTP "a escolher". Mapa CAP-ADs ainda incompleto: AD-30 ausente; CAP-8 sem AD-8/AD-9; CAP-6 sem AD-11/AD-21; CAP-1 sem AD-20/AD-21/AD-30; CAP-3 sem AD-4; AD-8 sem AD-21 em Binds.

## Cobertura contra o checklist

| Critério | Resultado |
| --- | --- |
| Pontos reais de divergência fixados | Quase todos. Faltam: papel do job (M-2), tratadores de PII fora de dois módulos (M-3), local do contrato (B-4). |
| Rule aplicável que previne a divergência | Em geral sim, com teste. Exceção SQL do AD-25 sem verificação de join. |
| Adiado sem divergência | Quase: foto e relatório têm decisão provisória? Não: AD-17 cobre só planilha; o Adiado diz "a definir com o provedor" e repassa para cada módulo. Registrar a provisória (B). |
| Altitude (implantação, provedor, operação) | Provedor, RLS, RPO/RTO, LGPD: decididos/pendentes com dono. Hospedagem do SSR, TLS/ingress, pools, rotação, configuração por ambiente: abertos sem registro (M-4). |
| Consistência interna | Referências cruzadas conferidas (AD-3→20/28/29, AD-7→19/21, AD-11→19/28, AD-20→28/29, AD-23→20, AD-25→2/19/23, AD-27→grafo, AD-30→8/29). Grafo x texto coerente (portas e `--> cadastro/acesso_api/auditoria`). Defeitos: M-1, M-2, B-1, B-3. |

## Pontos fortes
Grafo sem ciclo e coerente com AD-19/AD-27; `empresa` tratada como exceção explícita com teste; views `security_invoker`; `infra_sequencia` isolando o contador; `confirmarSenha` como funil único; trilha imutável com função de expurgo e partição `DEFAULT`; LGPD honesta quanto a pseudonimização.
