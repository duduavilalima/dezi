# Glossário e convenções

Tags de evidência: **[C]** código · **[B]** banco Postgres (agregados; dados até 2024-03-20) · **[B-inf]** deduzido do banco · **⚠** a confirmar (ver `a-confirmar.md`). IDs `RN-xxx` remetem aos documentos-fonte em `docs/regras-negocio/` (evidência com arquivo e linha).

| Termo | Significado |
|---|---|
| Campanha | Agrupador de avaliações; situação ATIVA/INATIVA; flag `avEmLiberacao`; `dimensao` liga o uso de dimensões |
| Avaliação | Ciclo de avaliação dentro de uma campanha: perfis de agentes, `preencheAv1..4`, `limiteAv1..4` |
| Agente | Trio (avaliação, tipo de competência, perfil) com **peso** relativo. Perfis: AUTOAVALIACAO(100), GESTOR(101), SUBORDINADO(102), PARES(103) |
| Participante | Linha (avaliado, avaliador) numa avaliação, com perfil, datas, marcadores `preenchida_avN`. Um avaliado tem vários |
| Av1 / Av2 / Av3 / Av4 | Autoavaliação (avaliado responde) / Avaliação (avaliador responde) / Consenso (avaliado + gestor, só perfil GESTOR) / Revisão (sem uso, sem tela) |
| "Ativo" (três predicados) | Um funcionário pode ser ativo em três sentidos, que dependem da situação de trabalho (D45): **pode acessar** o sistema, **é elegível a avaliar/ser avaliado** (D20, D36) e **ocupa o e-mail** na unicidade (D46, D50). EM_ATIVIDADE: sim, sim, sim. RECESSO: acessa ⚠ (ELI-17), não elegível, ocupa. DESLIGADO: não, não, não (libera o e-mail) |
| Perfil de acesso (`TipoPerfil`) | ADMINISTRADOR(3), FUNCIONARIO(4), CONSULTOR_RH(14), GESTOR_RH(15), GESTOR_AREA(16); um por funcionário |
| Situação cadastral | ATIVA(1) / INATIVA(98). Sem `CHECK` no banco; o enum Java é a única restrição |
| Situação funcional | EM_ATIVIDADE, DESLIGADO e 10 situações de recesso (férias, licenças, dispensa médica, luto, núpcias, serviço militar, recesso) |
| Situação da avaliação | AG_LIBERACAO(1), LIBERADA(2), INATIVA(98), CANCELADA(99, 0 usos), CONCLUIDA(100) |
| Tipo de competência | Agrupador por empresa (árvore); é o que o agente avalia |
| Competência / detalhamento / hint | Capacidade avaliada / a **pergunta** da competência / texto por nível da escala (com `comentario_obrigatorio`) |
| Gabarito | `detalhamento_cargo_competencia`: se a pergunta é **aplicável ao cargo** e com que peso |
| Escala (modelo de graduação) / nível | Conjunto de respostas possíveis / uma opção com rótulo, `ponto`, ordem, cor |
| NPA | "Não posso avaliar": nível com `ponto = 0`, identificado pelo texto; fora das médias |
| Snapshot | Cópia congelada na liberação (`avaliacao_*`, `modelo_graduacao_comp`, `graduacao_competencia`) |
| Pendência | Impedimento (COLABORADORES, CARGO, DIMENSAO) que bloqueia participante/liberação |
| Dimensão | Agrupador opcional de perguntas, só com `campanha.dimensao=true`; 0 usos reais |
| Gestor do departamento | Linha de `unidade_departamento` (unidade, departamento, gestor); `departamento_gestor` guarda gestor + descendentes (só gravada) |
| PDI | Plano de desenvolvimento: `pdi_acao` (ação cadastrada) e, separado, texto livre `avaliacao_participante.pdi` do consenso |
| Feedforward / apontamento | Mesma tabela `apontamento_funcional`; separados por predicado (`feedforward` vs `texto`) |
| `email_controle` / mensageria | Fila de e-mails (status PENDENTE, ENVIADO, REENVIO, ERRO, RESOLVIDO) / caixa de mensagens interna |
| BI / Pentaho | Servidor externo de relatórios; o `web/` só faz login técnico e gera a chave de escopo |

Cadastros de apoio sem lógica (Idioma, Município, Escolaridade, CursoGraduacao, TipoCurso, CBO) entram só aqui, sem regra.

## Volumes de referência [B]
19 empresas · 766 funcionários (perfis: ADM 19, FUNC 599, CONSULTOR 1, GESTOR_RH 38, GESTOR_AREA 109) · 263 departamentos · 665 cargos · 1.408 competências · 2.931 perguntas · 89 escalas · 144 avaliações (AG_LIBERACAO 10, LIBERADA 58, INATIVA 26, CONCLUIDA 50) · 166.192 respostas · 379 ações de PDI · 19 apontamentos · 2.808 e-mails · 392 mensagens.
