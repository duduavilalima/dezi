# Revisão (rubrica), rodada 7, da espinha de arquitetura do DEZI novo

Espinha: `architecture-dezi-novo.md` (AD-1..30, draft, 2026-10-21). Não editada. Severidade: A alta, M média, B baixa. Versões não reverificadas; volume conferido contra `.memlog.md`.

## Veredito: PASSA COM RESSALVAS

As ressalvas da r6 foram fechadas no essencial. Restam resíduos baixos e uma ambiguidade média na lista sem RLS. Nada bloqueia a distribuição de unidades.

## Fechamento das ressalvas da r6

| Ressalva r6 | Situação | Evidência |
| --- | --- | --- |
| M-1 lista sem RLS | Quase fechado | AD-28 passou a listar `credencial_plataforma` e o segredo TOTP de plataforma. A auditoria com `empresa_id` nulo é coberta por política (AD-21), não pela lista. Ver M-1. |
| B-1 `codigo` da empresa | Fechado | AD-28 e AD-3 concordam: `id`, `codigo`, situação e fuso. |
| B-2 papéis e Flyway/`dono_auditoria` | Fechado, com resíduo | AD-3 diz que jobs usam `app_rw` sob `paraEmpresa`. AD-29: "a migração cria a tabela-mãe e transfere o dono a `dono_auditoria`". Resíduo B-2. |
| B-3 mapa CAP | Parcial | CAP-8 corrigido; ainda irregular (B-1). |
| B-4 CI do `api-contract` | Fechado | AD-18 inclui o repositório de `api-contract`. |
| B-4 ordem de locks | Fechado | AD-19 inclui a cadeia do AD-24 e AD-24 a declara prefixo da global. |
| B-4 fonte do volume | Fechado para "volume medido" | O memlog traz 144 avaliações e 166.192 linhas. "20 vezes o legado" segue como premissa, sem tag `[ASSUMPTION]`. |

## Achados

### M-1. AD-28: "contadores de login ... sem empresa_id" contradiz o AD-7
O AD-7 chaveia os contadores por (empresa, matrícula) e (IP, empresa, matrícula), e o teste de esquema do AD-28 exige política e `FORCE` em toda tabela com `empresa_id`. A lista diz "sem `empresa_id`", o que não vale para os contadores por empresa. Separar o contador por IP/empresa inexistente (AD-21, sem RLS) do contador por (empresa, matrícula), que tem `empresa_id` e RLS. Listar também os códigos de recuperação do TOTP e dizer se `sessao` carrega `empresa_id`, pois o AD-2 exige `empresa_id` em tabela de negócio.

### B-1. Mapa Capacidade → AD ainda incompleto
Os Binds não estão refletidos. AD-9 liga CAP-3 a CAP-7, mas CAP-7 não o lista. AD-8 liga CAP-5, e CAP-5 não o lista. AD-3 liga CAP-2 a CAP-10, e só CAP-1 e CAP-2 o listam. CAP-4 cita AD-23, que liga só CAP-3 e CAP-5. CAP-9 não cita AD-10 nem AD-30. Derivar o mapa dos Binds ou dizer que ele é indicativo.

### B-2. Papéis: ALTER OWNER e "segundo papel" do AD-20
Não está dito que `migracao` precisa ser membro de `dono_auditoria` para transferir o dono (em provedor gerenciado isso pesa na escolha). O AD-20 fala em "segundo papel de banco, somente leitura" para as filas, mas o AD-29 só tem `app_descoberta` (que cobre fila e a view `empresa_ativa`). Unificar a redação.

### B-3. Ordem de locks: `infra_sequencia` "última" x advisory
O AD-28 diz que `infra_sequencia` é a última na ordem do AD-19, mas o AD-19 coloca o advisory depois dela. O AD-7 toma `pg_try_advisory_xact_lock` no login antes de qualquer lock de linha (não há deadlock, pois é `try`, mas a regra "advisory depois dos locks de linha" não o menciona). Alinhar a redação.

### B-4. Referências e tags menores
O front matter aponta o companion `architecture-dezi-novo/.memlog.md`, mas o arquivo está em `./.memlog.md`. "Até 20 vezes o legado" (Adiado e decisão 6) é premissa sem tag `[ASSUMPTION]`.

## Cobertura contra o checklist

| Critério | Resultado |
| --- | --- |
| Pontos de divergência fixados | Sim; lacuna apenas em M-1. |
| Regra que previne a divergência, com teste | Sim (arquitetura, esquema, privilégios, concorrência, isolamento). |
| Adiado sem divergência | Sim; implantação/operação com dono a designar; foto e relatório em Adiado com reuso da API do dono. |
| Altitude (implantação, provedor, operação) | Adequada: decidido ou adiado com dono; RPO/RTO e RLS como pendência de negócio. |
| Consistência interna | Boa. Grafo, portas e arestas coerentes (`EliminacaoDeTitular` completa, sem ciclo); `set_config(..., true)` uniforme; tabela de papéis coerente com AD-3/8/20/21. Defeitos novos só M-1, B-2 e B-3. |

## Pontos fortes
Papéis com tabela de privilégios e teste; ordem global de locks unificada; CI cobre os três repositórios; `codigo` alinhado entre AD-3 e AD-28; volume rastreável ao memlog.
