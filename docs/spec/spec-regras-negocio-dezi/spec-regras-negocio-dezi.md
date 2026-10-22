---
id: SPEC-regras-negocio-dezi
companions:
  - glossario.md
  - acesso-perfis.md
  - cadastro-organizacional.md
  - modelo-competencias.md
  - graduacao-conceitos.md
  - avaliacao.md
  - preenchimento.md
  - resultados.md
  - pdi-feedforward.md
  - comunicacao.md
  - decisoes-e-defeitos.md
  - a-confirmar.md
  - ../../../_bmad-output/initiative-levantamento-regras-dezi/architecture-dezi-novo/architecture-dezi-novo.md
sources:
  - ../../regras-negocio/00-plano-e-progresso.md
  - ../../regras-negocio/01-acesso-perfis.md
  - ../../regras-negocio/02-cadastro-organizacional.md
  - ../../regras-negocio/03-modelo-competencias.md
  - ../../regras-negocio/04-graduacao-conceitos.md
  - ../../regras-negocio/05a-avaliacao-estados-transicoes.md
  - ../../regras-negocio/05b-avaliacao-criacao-agentes-participantes.md
  - ../../regras-negocio/06-participantes-preenchimento.md
  - ../../regras-negocio/07-resultados-graficos.md
  - ../../regras-negocio/08-pdi-feedforward.md
  - ../../regras-negocio/09-comunicacao.md
---

> **Canonical contract.** This SPEC and the files in `companions:` are the complete, preservation-validated contract for what to build, test, and validate. Source documents listed in frontmatter are for traceability — consult them only if you need narrative rationale or prose color this contract intentionally omits.
>
> Neste projeto os `sources` guardam também a **evidência** (arquivo e linha de código, consultas ao banco), que os companions citam só por ID `RN-xxx`.

# Regras de negócio do legado DEZI

## Why

O DEZI é um sistema legado de avaliações de desempenho e pesquisas de clima, importado de um sistema antigo, sem documentação externa, que **não será reescrito**: serve de base para um sistema novo desenvolvido do zero. A equipe do novo sistema precisa saber o que o legado faz, o que é regra de negócio e o que é defeito ou resíduo, sem depender de executar o legado (a aplicação não sobe localmente). Isso é um mandato de extração: código e banco Postgres são as únicas fontes de verdade, e o que não tem evidência nos dois é "a confirmar". Os 11 documentos de `docs/regras-negocio/` cobrem os 9 módulos; este spec os condensa no contrato do que o sistema novo deve reproduzir, decidir ou corrigir.

## Capabilities

- **CAP-1** Acesso e perfis (`acesso-perfis.md`)
  - **intent:** Usuário autentica por empresa e matrícula, é bloqueado por tentativas excessivas, gerencia a própria senha, e só vê e altera o que seu perfil (ADMINISTRADOR, FUNCIONARIO, CONSULTOR_RH, GESTOR_RH, GESTOR_AREA) e sua empresa permitem; mudanças sensíveis deixam trilha.
  - **success:** Chamada à API com token de perfil não autorizado é recusada **no servidor** para cada ação da matriz; 5 falhas em 10 min bloqueiam o usuário (inclusive senha correta) e 25 em 20 min bloqueiam o par IP+usuário; `seclogin` obedece ao mesmo bloqueio; mudança de perfil gera registro de auditoria; nenhuma senha em texto em e-mail ou log.
- **CAP-2** Cadastro organizacional (`cadastro-organizacional.md`)
  - **intent:** RH mantém empresa, unidade, departamento, cargo e funcionário, com gestor, situação cadastral e funcional (recesso, desligamento) e subordinados definidos de forma consistente.
  - **success:** Salvar um funcionário comum **não** remove o gestor do departamento; e-mail, código do cliente e nome de unidade são únicos no banco; recesso termina na data fim; inativar departamento, cargo ou unidade tem efeito definido sobre as pessoas; o conjunto de subordinados de um gestor é o decidido pelo negócio (ORG-4) e testado.
- **CAP-3** Catálogo de competências (`modelo-competencias.md`)
  - **intent:** RH mantém tipos, competências, perguntas, dicas por nível e o gabarito por cargo (quais perguntas e com que pesos), com cascatas de situação previsíveis.
  - **success:** Só perguntas com gabarito aplicável e cadeia ativa chegam à liberação; inativar e reativar um nível da cadeia tem o efeito documentado e restaura o gabarito; gabarito de pergunta de outra competência é recusado; pesos têm semântica explícita; editar o catálogo depois da liberação não altera avaliação liberada.
- **CAP-4** Escalas de graduação (`graduacao-conceitos.md`)
  - **intent:** RH define escalas com níveis, pontos, "Não posso avaliar" (sem nota) e escala só de comentário; a liberação congela a escala usada.
  - **success:** Nível NPA e escala de comentário são atributos explícitos (não texto); pontos, faixa e ordem são validados no servidor; editar um nível não apaga texto personalizado por pergunta sem aviso; avaliação liberada mantém a escala congelada por versão; conceito não entra (D8).
- **CAP-5** Ciclo de vida da avaliação (`avaliacao.md`)
  - **intent:** RH cria e copia avaliações, define agentes com pesos e datas por etapa, escolhe participantes (gestor automático, subordinados, pares por planilha), resolve pendências, libera com congelamento e conclui, inativa, reativa ou cancela.
  - **success:** Avaliação nasce AG_LIBERACAO; liberação aborta sem participante, com pendência ou cargo sem pergunta aplicável; falha na liberação deixa estado de erro recuperável e idempotente (D4); concluir, inativar, reativar e cancelar só por ADMINISTRADOR e GESTOR_RH, recusado no servidor para os demais (D3); CANCELADA segue D26 (estados de origem, motivo obrigatório, dados preservados e congelados, aviso aos participantes, irreversível); a situação de preenchimento é derivada de dados e consistente (D5).
- **CAP-6** Preenchimento (`preenchimento.md`)
  - **intent:** Avaliado e avaliadores respondem por etapa (Av1 autoavaliação, Av2 avaliação, Av3 consenso) dentro do prazo, com comentário obrigatório por nível e consenso liberado sob regra única.
  - **success:** O servidor valida comentário obrigatório, "todas respondidas" ao finalizar e prazo; `preenchida_avN` é verdadeiro só com todas as perguntas respondidas; consenso exige a segunda parte; web e mobile liberam consenso pela mesma regra.
- **CAP-7** Resultados e acompanhamento (`resultados.md`)
  - **intent:** O avaliado, o gestor e o RH veem a nota ponderada e o progresso de preenchimento, com comparação entre perfis e evolução entre campanhas.
  - **success:** Nota reproduz a fórmula deduzida (consenso prevalece sobre avaliação; autoavaliação fora; ponto 0 fora; média do agente → pergunta ponderada por peso do agente renormalizado → competência simples → tipo ponderado por peso cargo-competência → final ponderado por peso cargo-tipo) com os pesos **congelados na liberação**; percentual do painel igual ao total marca "totalmente" também acima de 127 respostas; escopo do BI por identificador, não por nome.
- **CAP-8** PDI (`pdi-feedforward.md`)
  - **intent:** Funcionário e gestores registram, aprovam, executam e finalizam ações de desenvolvimento ligadas a competências e, opcionalmente, a uma avaliação.
  - **success:** Máquina de estados EM_APROVACAO → APROVADO → EM_ANDAMENTO → FINALIZADO imposta no servidor, com dono lido do registro, datas definidas pelo servidor e histórico de transições; exclusão lógica e com escopo; relatórios com colunas e datas corretas.
- **CAP-9** Apontamento e feedforward (`pdi-feedforward.md`)
  - **intent:** Avaliador registra feedforward ou registro crítico sobre um subordinado e uma competência; o avaliado vê o que recebeu e de quem.
  - **success:** Feedforward recebido é sempre o do usuário logado; apontamento e feedforward são tipos distintos; o avaliador é sempre o usuário logado.
- **CAP-10** Comunicação (`comunicacao.md`)
  - **intent:** O sistema envia e-mail (senha, boas-vindas, mensagens, avisos de liberação, contato) e mantém caixa de mensagens interna, com estado de envio confiável.
  - **success:** Estado do e-mail reflete a entrega ao SMTP e falha de SMTP é visível e reenviada; publicação na fila só após o commit; nenhum e-mail ou registro contém senha em texto; RH é avisado do erro de liberação; "Notificar via e-mail" e os parâmetros são respeitados; credencial SMTP rotacionada e fora do repositório.

## Constraints

- **Fonte de verdade:** só código e banco Postgres valem; regra sem evidência nos dois é "a confirmar", nunca fato inferido (`a-confirmar.md`).
- **O legado não é corrigido nem refatorado:** defeitos viram requisito do sistema novo (`decisoes-e-defeitos.md`).
- **Decisões do responsável (2026-10-01) valem sobre o legado:** concluir, inativar, reativar e cancelar avaliação só por ADMINISTRADOR e GESTOR_RH, CONSULTOR_RH só leitura; CANCELADA entra; `notificacao.email.funcionario.novo=false` é intencional; falha de liberação e situação de preenchimento são redesenhados (D1–D5); em 2026-10-09: dimensões, revisor/Av4 e conceito não entram, PDI entra com ciclo de vida revisto (D6–D9); só ADMINISTRADOR atribui ADMINISTRADOR, gestor do departamento é dado próprio, excluir quem já respondeu é bloqueado e a avaliação encerra por data e por estado (D10–D13); peso cargo×tipo é relativo e editável, e os pesos da nota são congelados na liberação (D14–D15); servidor valida comentário, completude e prazo, ENVIADO = aceito pelo SMTP e boas-vindas usam link de senha (D16–D19).
- **Permissão no servidor, não na tela:** o legado só impõe perfil em `PainelControleResource`; a matriz de `acesso-perfis.md` é a de tela e precisa ser imposta no servidor.
- **Isolamento por empresa** em todo cadastro e leitura; parâmetros (`parametros`) hoje são globais. No novo, a empresa vem sempre da sessão (D51). Ao `bmad-architecture` entra como requisito a matriz perfil × recurso × ação, com teste negativo de autorização por célula.
- **Congelamento na liberação:** cargo e departamento do avaliado, perguntas, dicas, pesos e escala são copiados; edição posterior do catálogo não altera avaliação liberada; quem entra depois deve receber a versão congelada, não a atual.
- **A nota dos relatórios não é calculada pelo `web/`:** vem do ETL do Pentaho (fora do repositório); a fórmula é **deduzida do banco** e é só referência do legado: o Pentaho não será reaproveitado (D24) e a fórmula do novo é validada com o negócio.
- **O esquema real diverge das migrações:** `log_auth` e `log_entity` inexistentes na instância; view `conjunto_resultados` com filtro de campanha ativa fora da migração; migrações não são a referência única.
- **Os dados são de uma instância (cópia ou produção: a confirmar) até 2024-03-20**; contagens citadas são dessa data; nenhum dado pessoal foi lido.
- **Fluxos presos à biblioteca inacessível `br.com.fasolti.commons`** (token, transição genérica de estados, escopo padrão, transação do `save`) só podem ser deduzidos pelo uso.

## Non-goals

- Replicar defeitos do legado (listados em `decisoes-e-defeitos.md`).
- Definir stack, camadas e arquitetura do sistema novo: ficam na espinha adotada como companion (`architecture-dezi-novo.md`, AD-1 a AD-30).
- Migração de dados e telas do legado.
- Dimensões (D6), revisor/Av4 (D7) e conceito (D8): não entram no novo.
- Reaproveitar o Pentaho (ETL, relatórios `.wcdf`, tabelas `bi_*`): o novo sistema define o próprio cálculo e relatórios (D24).
- SMS (nunca implementado), perfil `development` sem segurança, gráfico GAP em Java (zerado e sem uso) e enum `TipoComp`.
- Decidir pelo negócio os pontos "a confirmar": o spec os registra, não os resolve.

## Success signal

Uma pessoa do time do sistema novo responde, para cada módulo, **qual é a regra, de onde ela vem (código, banco ou dedução) e o que ainda depende do negócio**, sem abrir o legado; e os pontos marcados ★ em `a-confirmar.md` têm decisão registrada antes de iniciar a arquitetura.

## Assumptions

- Os companions destilam as regras; **arquivo:linha e consultas ao banco** ficam nos `sources` e são citados por ID `RN-xxx`. Quem precisar de evidência vai ao documento-fonte.
- Os documentos 05a e 05b foram fundidos em `avaliacao.md`; os prefixos RN originais (`RN-EST`, `RN-CRI`, `RN-ACE`…) foram mantidos.
- Contagens e percentuais do banco foram copiados dos documentos-fonte (consultas de 2026-09-29 a 2026-10-01) sem nova consulta.
- Onde dois documentos divergem, vale o mais recente e o posterior: 05b/RN-CRI-13 (peso dos agentes) foi resolvido em 07 como peso relativo; a precedência `AND`/`OR` da view v2 foi resolvida em 07; a correção de 01 sobre "Ativar" (INATIVA→LIBERADA) prevalece sobre 05a/RN-EST-19.
- "Conceito" não existe no legado; foi tratado como decisão aberta, não como capacidade.
- `CANCELADA`, `empresa.feedforward`, `ponto_focal_rh` e CBO têm 0 usos reais nos dados e foram mantidos como regra do legado marcada ⚠, sem decidir se entram no novo (exceto CANCELADA, que entra). Dimensões, revisor/Av4 e conceito foram decididos em 2026-10-09 (D6–D8): não entram.

## Open Questions

Lista completa (≈130 itens, por módulo) em `a-confirmar.md`. Os que mais bloqueiam o sistema novo:

- ~~Perfis que concluem e cancelam avaliação~~: decididos em D3 e D26.
- ~~Subordinados, escala de comentário, quem cria e aprova PDI, versão do catálogo~~: decididos em D20 a D23, D26 (`decisoes-e-defeitos.md`).
- ~~ETL e relatórios do Pentaho~~: descartados (D24). ~~Biblioteca `commons`~~: inacessível, vira decisão do novo (D25). Falta definir, no novo, a fórmula de nota e a forma de calcular e apresentar resultados (vai ao `bmad-architecture`).
