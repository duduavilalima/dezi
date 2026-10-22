---
name: 'Revisão adversária da espinha de arquitetura do DEZI novo (rodada 7)'
target: architecture-dezi-novo.md (AD-1 a AD-30, status draft, atualizada em 2026-10-21)
method: verificação do fechamento de R6-A1 e R6-M1..M7 contra o texto atual; grafo contra portas; AD-19/AD-24 de locks; AD-28 contra GRANTs
created: '2026-10-21'
supersedes: review-adversario-r6.md
---

# Revisão adversária, rodada 7

## Veredito: NÃO PASSA (por um único alto, de correção curta)

Contagem: 0 críticos, 1 alto, 5 médios, 5 baixos.

O grafo está limpo: DAG, sem ciclo novo. Todos os implementadores de `EliminacaoDeTitular` (`comunicacao`, `preenchimento`, `avaliacao`, `pdi`, `resultados`) têm aresta para `cadastro`; `AutorizarLeitura` (declarada por `auditoria`, implementada por `acesso`) usa a aresta `acesso_api --> auditoria` que já existe; `EmpresasAtivas` (declarada por `infra`) não gera aresta. A cadeia de locks ficou numa ordem única no AD-19.

## Fechamento da r6

| ID | Situação |
| --- | --- |
| R6-A1 | Parcial. `infra_evento_publicado` agora é imutável e sem estado, e a descoberta compara `sequencia` com `ultima`. Mas o `GRANT` do AD-28 contradiz AD-20/AD-29 e está incompleto (R7-A1). |
| R6-M1 | Fechado quanto à contagem (transação própria por `novaTransacao`). Resíduo em R7-B1. |
| R6-M2 | Parcial. `credencial_plataforma` e o segredo TOTP entraram na lista sem RLS. Faltam códigos de recuperação e a sessão de plataforma; a lista traz contradições (R7-M1, R7-M4). |
| R6-M3 | Fechado (partições iniciais na migração, alerta com antecedência, falha do canal 2 sem derrubar login). Resíduo em R7-B3. |
| R6-M4 | Fechado (`DataSource` de `app_descoberta` explícito no AD-20). |
| R6-M5 | Fechado (`sucessor_id` e `escopo_visivel` com nós extintos). |
| R6-M6 | Parcial. AD-8 e AD-30 listam as tabelas de justificativa, mas "append-only, nunca sobrescritas" colide com anonimizar (R7-M2). |
| R6-M7 | Parcial. Ordem única no AD-19, mas faltam duas regras (R7-M3). |

## Alto

### R7-A1. Descoberta de eventos: `GRANT` do AD-28 diverge de AD-20/AD-29 e não permite escrever a consulta

**Par:** time `infra` (políticas e `GRANT`) x times `comunicacao`/`resultados` (adaptadores de consumo).

- AD-20 e AD-29 dizem que `app_descoberta` lê só `empresa_id, id, estado` das filas. O AD-3 diz "só leem ids". O AD-28 concede `empresa_id, agregado_tipo, agregado_id, sequencia, ultima` de `infra_evento_publicado` e `infra_consumo`. As três redações não podem valer juntas, e o AD-28 não enumera as colunas de `infra_trabalho` (`id`, `estado`, `proxima_tentativa`).
- A lista do AD-28 omite `consumidor`. A descoberta é por (consumidor, agregado): sem essa coluna, quem escreve a consulta não sabe qual consumidor está atrasado.
- Para o primeiro evento de um agregado não existe linha em `infra_consumo`. Falta dizer que ausência equivale a `ultima = 0` e quem fornece a lista de consumidores registrados para a junção.
- **Correção:** uma só lista de `GRANT` de coluna, no AD-29, por tabela: `infra_trabalho(empresa_id, id, estado, proxima_tentativa)`, `infra_evento_publicado(empresa_id, agregado_tipo, agregado_id, sequencia)`, `infra_consumo(empresa_id, consumidor, agregado_tipo, agregado_id, ultima)`. Registre a regra "ausente = 0" e o registro estático de consumidores. Alinhe AD-3 e AD-20 a essa lista.

## Médio

- **R7-M1.** Lista sem RLS do AD-28 vs AD-3/AD-2: `shedlock` aparece como de `acesso`, mas o mesmo AD-28 diz que ShedLock é de `infra`. `sessao` e "contadores de login" aparecem "sem `empresa_id`", mas AD-3 e AD-5 tiram a empresa da sessão e o AD-7 conta por (empresa, matrícula). Corrija dono e colunas.
- **R7-M2.** AD-30 diz que `avaliacao_justificativa` e `preenchimento_justificativa` são "append-only, nunca sobrescritas", mas `avaliacao` e `pdi` as anonimizam (`EliminacaoDeTitular`) e o AD-8 as chama "anonimizáveis". Declare a anonimização de `texto`/`autor_id` como única alteração permitida (`GRANT` de coluna e gatilho que recusa o resto). O AD-8 limita as tabelas a cancelar/prorrogar/reabrir; o PDI (D22) fica sem tabela.
- **R7-M3.** Locks: (a) uma transação que emite eventos de dois agregados toma duas linhas de `infra_sequencia` em ordem indefinida (deadlock entre duas transações); fixe a ordem (`agregado_tipo`, `agregado_id`) e a regra "emitir evento é o último passo". (b) `confirmarSenha` (aceite do consenso) atualiza `sessao` e toma advisory no meio da transação do aceite, depois de `participante`/`etapa_participante`, contra a ordem `sessao` → `avaliacao`. Exija que seja chamada antes de tomar qualquer lock de linha de negócio.
- **R7-M4.** A sessão do administrador de plataforma e os códigos de recuperação do TOTP não têm tabela, dono nem política (a `sessao` liga a `funcionario`, que a plataforma não tem). Nomeie-as e inclua na lista sem RLS.
- **R7-M5.** "Alerta: evento publicado incompleto" no AD-28 ficou sem definição depois que o evento perdeu o estado. Substitua por "lacuna de sequência acima de X min" e "atraso `sequencia - ultima` por consumidor".

## Baixo

- **R7-B1.** AD-28 diz que a contagem do venenoso é gravada em transação própria, mas não que `ultima` avança na mesma transação do efeito do consumidor.
- **R7-B2.** O teste de partida de beans com `ObjectProvider` está só no AD-19 (R6-B1 aberto). Generalize às portas inversas (`ResolucaoDeEmpresa`, `EscopoOrganizacional`, `EmpresasAtivas`, `EliminacaoDeTitular`, `AutorizarLeitura`) e fixe o tipo `fatos` na `api` de `acesso`.
- **R7-B3.** A migração (papel `migracao`) cria partições que devem pertencer a `dono_auditoria` (`NOLOGIN`): diga como (`ALTER ... OWNER` com `SET ROLE` ou função `SECURITY DEFINER` própria).
- **R7-B4.** A linha de entidades (donos) não cita `infra_sequencia`, `infra_consumo`, `infra_consumo_falha`, `credencial_plataforma`, `par_de_consenso`, `aceite_consenso` nem as tabelas de justificativa. O ER ainda tem `FUNCIONARIO ||--o{ SESSAO` e `PARTICIPANTE ||--o{ RESPOSTA` (R5-B1).
- **R7-B5.** A regra "implementação de `EmpresasAtivas` roda fora do repositório base com filtro" não está entre os testes de arquitetura.

## Tabela-resumo

| ID | Sev. | ADs | Tema |
| --- | --- | --- | --- |
| R7-A1 | Alto | AD-28, AD-20, AD-29, AD-3 | `GRANT` de descoberta contraditório e incompleto |
| R7-M1 | Médio | AD-28, AD-2, AD-3 | Lista sem RLS: dono do ShedLock e `empresa_id` da sessão |
| R7-M2 | Médio | AD-30, AD-8 | Append-only x anonimização; PDI sem tabela |
| R7-M3 | Médio | AD-19, AD-28, AD-7 | Ordem de `infra_sequencia` e `confirmarSenha` |
| R7-M4 | Médio | AD-3, AD-28 | Sessão de plataforma e códigos de recuperação |
| R7-M5 | Médio | AD-28 | Alerta de evento incompleto sem definição |
| R7-B1 a B5 | Baixo | vários | Resíduos de redação e testes |
