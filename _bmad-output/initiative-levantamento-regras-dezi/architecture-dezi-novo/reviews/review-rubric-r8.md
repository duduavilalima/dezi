# Revisão (rubrica), rodada 8, da espinha de arquitetura do DEZI novo

Espinha: `architecture-dezi-novo.md` (AD-1..30, draft, 2026-10-21). Não editada. Severidade: A alta, M média, B baixa. Versões não reverificadas.

## Veredito: PASSA COM RESSALVAS

As ressalvas da r7 foram fechadas, salvo o mapa CAP (parcial) e um resíduo de redação nos papéis. Há uma contradição interna nova, baixa (advisory do AD-11). Nada bloqueia a distribuição de unidades.

## Fechamento das ressalvas da r7

| Ressalva r7 | Situação | Evidência |
| --- | --- | --- |
| M-1 contadores de login e TOTP na lista sem RLS | Fechado | AD-28 separa o contador por IP/empresa inexistente (sem RLS) dos contadores por (empresa, matrícula) e (IP, empresa, matrícula) (com RLS, sob `paraEmpresa`); lista `sessao` (com nota de que carrega `empresa_id`), `totp_plataforma` e `recuperacao_totp_plataforma`. Concorda com AD-7 e AD-21. |
| B-1 mapa CAP | Parcial | Ver B-1. |
| B-2 `migracao` e `dono_auditoria` | Fechado | AD-29: "`migracao` é membro de `dono_auditoria` para isso". Resíduo de "segundo papel" em B-3. |
| B-3 advisory na ordem de locks | Fechado | AD-19 coloca o advisory após `infra_sequencia`, com a exceção do `try` do login; AD-28 ("última entre os locks de linha") fica coerente. |
| B-4 caminho do companion | Fechado | Front matter aponta `.memlog.md`, que existe ao lado da espinha. |
| B-4 tag de 20x | Fechado | Adiado: "premissa `[ASSUMPTION]`, sem projeção do negócio". |

## Achados

### B-1. Mapa Capacidade → AD ainda não reflete os Binds
Sem declarar-se indicativo. AD-3 liga CAP-2 a CAP-10, e só CAP-1 e CAP-2 o listam. AD-15 (CAP-1 a CAP-10) e AD-18 (CAP-10) não aparecem. Derivar o mapa dos Binds ou declará-lo indicativo.

### B-2. AD-11 x AD-19: advisory com id cru
AD-11 diz `pg_advisory_xact_lock(avaliacaoId)`. AD-19 exige a forma de dois inteiros (`classid` registrado em `infra`, `objid` = hash de 32 bits), "nunca o id cru". Alinhar o AD-11 à forma do AD-19 e registrar o `classid` da exclusão parcial/total.

### B-3. AD-20: "segundo papel de banco" x AD-29
AD-20 fala em "segundo papel de banco, somente leitura" para descobrir empresas com trabalho pendente, mas o AD-29 só tem `app_descoberta`. Trocar por `app_descoberta`.

### B-4. AD-7 passo 1: onde vivem os limites agregados
O limite agregado "por IP" e "por empresa", aplicado antes de qualquer hash e antes de a empresa estar resolvida, não está classificado na lista do AD-28 (com ou sem RLS). Dizer em que tabela ficam, se têm `empresa_id` e como são lidos para empresa inexistente.

## Cobertura contra o checklist

| Critério | Resultado |
| --- | --- |
| Pontos de divergência fixados | Sim; lacunas apenas B-1 e B-4. |
| Regra que previne a divergência, com teste | Sim (arquitetura, esquema, privilégios, concorrência, isolamento). |
| Adiado sem divergência | Sim; implantação e operação com dono a designar; premissa de volume tagueada. |
| Altitude (implantação, provedor, operação) | Adequada: decidido ou adiado com dono; RPO/RTO e RLS como pendência de negócio. |
| Consistência interna | Boa. Grafo e portas coerentes; `set_config(..., true)` uniforme; papéis coerentes com AD-3/8/20/21/29. Defeitos novos: B-2 e B-3. |

## Pontos fortes
Lista sem RLS agora consistente com AD-7 e AD-21; ordem global de locks com exceção explícita; cadeia `migracao` / `dono_auditoria` declarada; front matter e premissas rastreáveis.
