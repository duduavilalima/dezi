# Roteiro BMAD: levantamento de regras de negócio do sistema atual

> Não existe um skill do BMAD dedicado à engenharia reversa de regras de negócio. Este roteiro usa os skills que mais se aproximam disso, com base nas descrições deles. Confira o comportamento real na primeira execução.

## 1. Orientação: `/bmad`
Peça "onde começar" para um sistema já existente (brownfield). Ele mostra o que está instalado e a ordem sugerida.

## 2. Contexto do repositório: `/bmad-project-context`
Adota o repositório e gera o bloco do `AGENTS.md` com stack, estrutura de pastas, convenções e módulos. Serve de base para os passos seguintes.

## 3. Analista de negócio: `/bmad-agent-analyst` (Mary)
Agente principal da tarefa. Converse com ela módulo a módulo, pedindo que leia o código (entidades, services, validações, controllers, queries) e extraia:
- regras de negócio (validações, cálculos, estados e transições, permissões);
- glossário do domínio;
- fluxos e exceções.

Salve o resultado em arquivos, por exemplo `docs/regras-negocio/<modulo>.md`. Trabalhe um módulo por vez para não estourar o contexto.

## 4. Condensar em spec: `/bmad-spec`
Transforma as notas do passo 3 em um spec curto, com um arquivo central e arquivos de apoio por módulo. Pode servir como documento canônico das regras.

## 5. Aprofundar os pontos duvidosos
- `/bmad-advanced-elicitation`: pré-mortem, primeiros princípios e outras técnicas de crítica para achar regras implícitas ou contraditórias.
- `/bmad-party-mode`: reúne analista, arquiteto, PM e dev para discutir ambiguidades, como uma regra que parece bug ou legado.
- `/bmad-forge-idea`: para algo ainda mal definido.

## 6. Visão técnica: `/bmad-architecture`
Aceita partir de uma base de código existente. Documenta como as regras estão distribuídas (camadas, integrações, banco) e onde cada regra vive.

## 7. Visão de produto: `/bmad-prd` (opcional)
Use só se quiser formalizar os requisitos funcionais do sistema atual, por exemplo para migração ou reescrita. Para apenas documentar regras, o spec do passo 4 basta.

## 8. Validação: `/bmad-review` e `/bmad-walkthrough`
- `/bmad-review`: crítica dos documentos gerados (lacunas, inconsistências).
- `/bmad-walkthrough`: revisão guiada, útil para validar com especialistas de negócio.

## 9. Organização do trabalho: `/bmad-ticket` (opcional)
Cria uma iniciativa "Levantamento de regras" e fatia em épicos por módulo e histórias por fluxo. Ajuda em sistemas grandes.

## Dicas
- Confirme as regras com quem conhece o negócio. O código mostra o que o sistema faz, não o que deveria fazer. Marque como "a confirmar" o que for ambíguo.
- Rode `/clear` entre módulos e mantenha o estado nos arquivos gerados.
- `/bmad-deep-recon` e `/bmad-brainstorming` ajudam pouco aqui, pois servem mais para pesquisa externa e ideação.
