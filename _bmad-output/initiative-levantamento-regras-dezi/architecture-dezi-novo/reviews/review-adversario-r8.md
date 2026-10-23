---
name: 'Revisão adversária da espinha de arquitetura do DEZI novo (rodada 8)'
target: architecture-dezi-novo.md (AD-1 a AD-30, status draft, atualizada em 2026-10-21)
method: verificação do fechamento de R7-A1 e R7-M1..M5; busca de contradições e ciclos novos (GRANT de app_descoberta, lista sem RLS, prova de senha, ordem de locks)
created: '2026-10-21'
supersedes: review-adversario-r7.md
---

# Revisão adversária, rodada 8

## Veredito: PASSA COM RESSALVAS

Contagem: 0 críticos, 0 altos, 6 médios, 9 baixos.

Nada bloqueia times em paralelo: o grafo continua DAG (todos os implementadores de `EliminacaoDeTitular` têm aresta para `cadastro`; `AutorizarLeitura` e `EmpresasAtivas` não criam aresta). Os médios são contratos a fechar antes do código de `acesso`, `infra` e da anonimização, não impedem o início dos demais módulos.

## Fechamento da r7

| ID | Situação |
| --- | --- |
| R7-A1 | Fechado no essencial. O AD-28 é a lista única de `GRANT` de coluna, com `consumidor`, `tipo` só na fila e "sem linha, `ultima = 0`"; AD-3, AD-20 e AD-29 remetem a ele. Resíduo em R8-M6 (registro de consumidores, ausência de `tipo`). |
| R7-M1 | Fechado. `shedlock` é de `infra`; `sessao` declara que carrega `empresa_id`; o contador por (empresa, matrícula) tem RLS e o por IP/empresa inexistente não. |
| R7-M2 | Parcial. AD-30 diz "só mudam pela anonimização" e o AD-8 inclui `pdi_justificativa`, mas o mecanismo (GRANT de coluna e gatilho) não está dito e `preenchimento_justificativa` ficou sem implementador (R8-M3, R8-B8). |
| R7-M3 | Parcial. Ordem de `infra_sequencia` fixada e `confirmarSenha` movida para transação própria; a prova de uso único criou lacuna nova (R8-M1) e a sessão e `infra_consumo` seguem fora da ordem (R8-M2, R8-M5). |
| R7-M4 | Fechado (`sessao_plataforma`, `totp_plataforma`, `recuperacao_totp_plataforma` na lista). Resíduo em R8-B1. |
| R7-M5 | Parcial. O alerta virou "lacuna de sequência"; "atraso `sequencia - ultima` por consumidor" não entrou (R8-B3). |

## Críticos e altos

Nenhum.

## Médios (uma linha cada)

- **R8-M1. Prova de senha sem armazenamento definido (AD-7).** "Prova de uso único, atada à sessão e à finalidade, 60 s, consumida pela transação de negócio" não diz onde vive (tabela, dono, RLS), nem a posição do seu lock na ordem do AD-19, nem se o rollback da transação de negócio devolve a prova. Se for coluna de `sessao`, desfaz o ganho declarado ("`sessao` não é tomada no meio"). Fixe: tabela própria de `acesso`, `consumirProva` como primeiro passo da transação de negócio, antes de qualquer lock de linha de negócio.
- **R8-M2. Renovação da inatividade da sessão fora da ordem de locks (AD-5, AD-19, AD-24).** "30 min de inatividade" implica `UPDATE sessao` por requisição. Se estiver na transação da requisição, ela toma `sessao` antes de `funcionario`, contra a cadeia `funcionario` → `credencial_acesso` → `sessao` (RH desliga o usuário × o próprio usuário em requisição: deadlock). Declare a renovação em transação própria, sem lock de espera (ou com `SKIP LOCKED`).
- **R8-M3. Anonimização de titular (AD-30) sem ordem de locks nem exceção ao guarda de estado.** Atravessa seis módulos a partir de `cadastro` e não consta no AD-19. Escreve `resposta.observacao` e comentários do consolidado, mas `garantirAberta` recusa escrita em avaliação `CONCLUIDA` e o AD-11 declara o consolidado imutável com `INSERT` único. Declare modo próprio do guarda, ordem de chamada das portas, transação única ou por módulo (com registro reaplicável) e quem anonimiza `preenchimento_justificativa` (AD-30 a coloca sob "avaliacao e pdi").
- **R8-M4. `infra_evento_publicado` e `infra_consumo` sem retenção (AD-28, AD-21).** O registro é imutável e a retenção só está definida para auditoria e outbox; uma linha de consumo por (consumidor, agregado) cresce sem fim e a descoberta compara contra o registro inteiro, sem `GRANT` em `infra_sequencia` (onde está o `ultima` por agregado). Defina expurgo (partição ou função `SECURITY DEFINER`, como na auditoria) e use `infra_sequencia.ultima` como alvo da comparação.
- **R8-M5. Emissão de evento e `infra_consumo` na ordem de locks (AD-19, AD-28).** "Ordem crescente de (`agregado_tipo`, `agregado_id`)" e "emitir é último" exigem coletor pré-commit; com upsert imediato no ponto da chamada (`cadastro` chama `acesso`, que emite antes) a ordem não é garantida. Além disso, o avanço de `infra_consumo` não está na lista de locks nem declarado como na mesma transação do efeito do consumidor (R7-B1 segue aberto).
- **R8-M6. Descoberta: registro estático de consumidores e coluna `tipo` (AD-28).** A regra "ausente = 0" precisa de uma lista de consumidores registrados para a junção (pedida em R7-A1, não escrita). Sem `tipo` em `infra_evento_publicado` na lista, todo consumidor é acordado por qualquer evento do agregado, inclusive os que não assinam o tipo.

## Baixos (uma linha cada)

- **R8-B1.** AD-29 não diz que `app_rw` não tem `GRANT` em `credencial_plataforma`, `sessao_plataforma`, `totp_plataforma`, `recuperacao_totp_plataforma`; sem RLS, só o `GRANT` as protege.
- **R8-B2.** `migracao` é membro permanente de `dono_auditoria` (AD-29): o pipeline mantém poder sobre a trilha; use `NOINHERIT`/`SET ROLE` só na migração inicial e registre.
- **R8-B3.** Alerta "evento mais antigo sem consumo" precisa de `ocorrido_em`, que `app_descoberta` não lê; a lacuna de sequência é impossível se a numeração é por linha travada até o commit (regra de adiar morta); falta "atraso `sequencia - ultima`".
- **R8-B4.** O teste de partida de beans (`ObjectProvider`) e o teste de `EmpresasAtivas` fora do repositório base seguem só no AD-19 (R7-B2 e R7-B5 abertos).
- **R8-B5.** Linha de donos e ER ainda omitem `infra_sequencia`, `infra_consumo`, `infra_consumo_falha`, `credencial_plataforma`, `par_de_consenso`, `aceite_consenso`, tabelas de justificativa e a prova de senha; o ER mantém `FUNCIONARIO ||--o{ SESSAO` (R7-B4, R5-B1).
- **R8-B6.** AD-7: o funil de `confirmarSenha` cita "contador por matrícula", mas o contador próprio "não alimenta o atraso de login da matrícula"; escolha uma redação. A exceção do advisory no AD-19 cita só o login; `confirmarSenha` também usa `try` (não espera, sem deadlock), mas deve constar.
- **R8-B7.** AD-28 diz que toda tabela com `empresa_id` tem política e `FORCE`, e logo antes lista `sessao` (com `empresa_id`) sem RLS; reescreva como "salvo a lista".
- **R8-B8.** "Só mudam pela anonimização" (AD-30) sem mecanismo: `GRANT` de coluna em `texto`/`autor_id` e gatilho que recusa o resto, como no AD-8.
- **R8-B9.** Job global sem empresa (partições) não cabe em `infra_trabalho`, que tem `empresa_id` e RLS; diga que ele roda só sob ShedLock, sem item de fila.

## Tabela-resumo

| ID | Sev. | ADs | Tema |
| --- | --- | --- | --- |
| R8-M1 | Médio | AD-7, AD-19 | Prova de senha sem tabela, dono e posição de lock |
| R8-M2 | Médio | AD-5, AD-19, AD-24 | Renovação de inatividade da sessão fora da ordem |
| R8-M3 | Médio | AD-30, AD-11, AD-19 | Anonimização: ordem de locks e guarda de estado |
| R8-M4 | Médio | AD-28, AD-21 | Retenção do registro de eventos e do consumo |
| R8-M5 | Médio | AD-19, AD-28 | Coletor pré-commit; `infra_consumo` na ordem |
| R8-M6 | Médio | AD-28 | Registro de consumidores e `tipo` na descoberta |
| R8-B1 a B9 | Baixo | vários | Resíduos de redação, GRANTs e testes |
