# Revisão (rubrica), rodada 6, da espinha de arquitetura do DEZI novo

Espinha: `architecture-dezi-novo.md` (AD-1..30, draft, 2026-10-21). Não editada. Severidade: A alta, M média, B baixa. Versões e memlog não reverificados.

## Veredito: PASSA COM RESSALVAS

Todas as ressalvas da r5 foram fechadas no essencial; nenhuma contradição de AD citando AD errado foi achada. Restam uma lacuna na lista de tabelas sem RLS (M-1) e resíduos baixos. Nada bloqueia a distribuição de unidades.

## Fechamento das ressalvas da r5

| Ressalva r5 | Situação | Evidência |
| --- | --- | --- |
| M-1 dono do expurgo | Fechado | AD-21 e AD-29 concordam: funções `SECURITY DEFINER` de `dono_auditoria`. Resíduo B-2. |
| M-2 papel do job | Fechado | AD-29 define `app_manutencao` (só `EXECUTE`), com teste de privilégios. Resíduo B-2. |
| M-3 EliminacaoDeTitular | Fechado | AD-30 lista `comunicacao`, `preenchimento`, `avaliacao`, `pdi`, `resultados`; todos têm `--> cadastro` no grafo. Sem ciclo. |
| M-4 implantação/operação | Fechado (como adiamento) | Linha de Adiado cobre SSR, TLS/IP real, pools, rotação e configuração por ambiente, com dono a designar; particionamento de negócio esclarecido. Resíduo: "volume medido" e "20 vezes o legado" sem fonte (B). |
| B-1 `set_config` x `SET LOCAL` | Fechado | AD-3 `set_config(..., true)`, AD-20 "equivalente a `SET LOCAL`", AD-23 "esta mesma chamada". Coerente. |
| B-2 empresa e colunas | Parcial | `GRANT` de coluna (AD-3) e view `empresa_ativa(id, situacao, fuso)` (AD-20) definidos. Divergência B-1 abaixo. |
| B-3 tags e legenda | Fechado quanto à legenda (2026-10-13 a 2026-10-21). AD-9/10/14 seguem `[ASSUMPTION]`, com forma explícita no AD-9; não conferido contra o memlog. |
| B-4 contract-first | Parcial | AD-18 declara contract-first `[ASSUMPTION]` em repositório próprio; consistente com `api-contract/` na Semente. Resíduo B-4. |
| B-4 mapa de capacidades | Parcial | AD-30 entrou em CAP-2, mas ver B-3. |

## Achados

### M-1. AD-28: lista de tabelas sem RLS incompleta
A lista (`empresa`, `sessao`, `shedlock`, contadores de login) "falha o build" para toda tabela fora dela, mas `credencial_plataforma` (AD-3, sem `funcionario_id` nem empresa), a tabela de TOTP/códigos de recuperação do administrador de plataforma e a auditoria com `empresa_id` nulo (AD-21) não constam, e o AD-21 só trata a última por política. Como o teste de esquema é a trava, ou a lista inclui essas tabelas ou o build reprova a própria espinha.

### B-1. AD-28 x AD-3: colunas de `empresa`
AD-3 concede `id`, `codigo`, `situacao` e fuso; AD-28 diz "só com `id`, situação e fuso". Sem `codigo`, a porta `ResolucaoDeEmpresa` não resolve o login. Alinhar AD-28.

### B-2. Papéis: criação da tabela-mãe e papel dos jobs
Não está dito como o Flyway (`migracao`) cria a tabela-mãe e as partições com dono `dono_auditoria` (`ALTER OWNER` no pipeline). AD-3 ainda diz "jobs e plataforma usam papel próprio", enquanto AD-20 roda jobs sob `paraEmpresa` e AD-29 só nomeia papel próprio para o job de partições; dizer que os demais jobs usam `app_rw`.

### B-3. Mapa Capacidade → AD ainda irregular
CAP-8 lista AD-9, que não liga CAP-8 (Binds do AD-9: CAP-3 a CAP-7); CAP-1 não lista AD-20, AD-21, AD-22 nem AD-30; CAP-9 só tem AD-4 (AD-30 aplica a `pdi`); CAP-5/CAP-6 sem AD-21/AD-26 em CAP-5 e AD-11 em CAP-6 quando aplicável. Derivar o mapa dos Binds.

### B-4. Resíduos menores
AD-18 manda CI "nos repositórios de backend e de frontend", mas o contrato está em repositório próprio, sem CI declarado. AD-24 introduz ordem de lock `funcionario → credencial_acesso → sessao` fora da ordem global do AD-19 (dizer que é prefixo da global). "Volume medido" e "20 vezes o legado" sem fonte. Foto e relatório: sem decisão provisória.

## Cobertura contra o checklist

| Critério | Resultado |
| --- | --- |
| Pontos de divergência fixados | Sim; lacunas apenas em M-1 e B-1. |
| Regra que previne a divergência, com teste | Sim (arquitetura, esquema, privilégios, concorrência). |
| Adiado sem divergência | Quase; ver B-4 (foto/relatório). |
| Altitude (implantação, provedor, operação) | Adequada: decidido ou adiado com dono; RPO/RTO e RLS como pendência. |
| Consistência interna (ADs citados, grafo x texto) | Boa. Referências conferidas: AD-3→5/7/20/28/29, AD-4→19/25, AD-8→4/21/29, AD-9→23/26, AD-11→19/28, AD-20→28/29, AD-21→20/28/29, AD-22→13, AD-23→20, AD-25→2/19/23, AD-27→grafo, AD-30→8/29. Grafo coerente com portas e arestas `--> cadastro/acesso_api/auditoria`. Defeitos só B-1 e M-1. |

## Pontos fortes
Grafo sem ciclo e coerente com texto; tabela de papéis com `app_manutencao` e teste de privilégios; `EliminacaoDeTitular` completa; `set_config` uniforme; legenda e Adiado de operação atualizados.
