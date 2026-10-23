# Revisão (rubrica), rodada 4, da espinha de arquitetura do DEZI novo

Espinha revisada: `architecture-dezi-novo.md` (AD-1..30, status draft, atualizada em 2026-10-21). A espinha não foi editada. Severidade: A = alta (gera divergência real entre unidades), M = média, B = baixa. Versões de tecnologia não foram reverificadas (escopo da revisão de versões/segurança).

## Veredito: PASSA COM RESSALVAS

R-1 (arestas) e R-3 (AD-7 passo 2) foram fechados de verdade; R-2 e R-4 fecharam em parte; R-5 e R-6 seguem abertos. As correções recentes criaram um defeito novo de alta severidade no grafo (direção da aresta da porta `ResolucaoDeEmpresa`/`EscopoOrganizacional`, que desenha um ciclo) e deixaram a consulta de `empresa` sem base em dois ADs. Nenhum é bloqueio de arquitetura; devem ser corrigidos antes de distribuir unidades.

## Fechamento dos achados da rodada 3

| Achado r3 | Situação | Evidência / resíduo |
| --- | --- | --- |
| R-1 arestas do grafo | Fechado | Há `-> acesso_api` de cadastro, catalogo, avaliacao, preenchimento, resultados, pdi, comunicacao; `-- RegistroDeAuditoria --> auditoria` de todos os módulos; "Regra do desenho" (porta `Autorizacao`, `confirmarSenha`) coerente com AD-7 e AD-8. Resíduos novos: N-1 e N-3 abaixo. AD-8 ainda não cita AD-21 em Binds (B). |
| R-2 armazenamento e exportação | Parcial | AD-17 decide `bytea` em tabela do módulo importador, retenção curta, e o Adiado agora diz que exportação reusa a API do módulo dono (escopo AD-4, anonimato AD-12). Resta: N-4. |
| R-3 AD-7 passo 2 vs "IP sem falhas" | Fechado | Passo 2 agora só barra quando a chave do IP também tem falhas; "decidido" e "falta fixar os valores" alinhados em essência. Resíduo menor: a frase é ambígua sobre a chave (IP, empresa, matrícula) em atraso e a por usuário limpa (deve ser 429, bloqueio duro); IPv6 (/64) e "IP real a confirmar" (AD-6) continuam sem posição (B). |
| R-4 papéis, SET LOCAL, login pré-contexto | Parcial | Papéis: AD-3 alinhado ao AD-29 (fechado). `ResolucaoDeEmpresa` criada, mas ver N-2. `set_config(..., true)` (AD-3) vs "só `SET LOCAL`" (AD-20) persiste, ver N-5. |
| R-5 contrato e gerador | Aberto | OpenAPI: não diz contract-first ou code-first; AD-18 "nos dois repositórios" vs três diretórios da Semente; gerador Angular e TOTP "a escolher". Flyway agora tem a decisão (manter 13.x ou 12.4.0) marcada "a decidir" (aceitável, é pendência explícita). |
| R-6 altitude | Aberto | Sem posição: hospedagem do Node SSR, TLS/ingress (também fonte do IP real e do `Cache-Control`), rotação de segredos além do SMTP, configuração por ambiente, dimensionamento de pools com a reserva do canal 2 e os 3+ `DataSource`s. "Particionamento do banco" em Adiado continua sem dizer que é o das tabelas de negócio (AD-21 já exige partição mensal da auditoria). "Volume medido" e "20 vezes o legado" sem fonte. |
| R-7 mapa CAP | Aberto (B) | CAP-8 sem AD-25/AD-8, CAP-7 sem AD-9, CAP-6 sem AD-11/AD-20/AD-21, CAP-1 sem AD-20/AD-21/AD-30, CAP-3 sem AD-4; CAP-9 só AD-4; AD-30 não aparece no mapa. |
| R-8 AD-28 sequência, advisory, tags, AD-21 vs AD-20 | Parcial | Sequência: descarta só o já processado e adia lacuna (corrigido, mas ver N-6). Advisory: AD-19 fixa a forma de dois inteiros e a ordem (fechado; AD-7 e AD-11 ainda escrevem o lock sem citar a forma, B). AD-21 canal 2 ainda diz `REQUIRES_NEW` sem `novaTransacao` (B). Tags: ver N-8. |

## Achados novos desta rodada

### N-1 (A). Grafo: a aresta tracejada de `acesso_api` para `cadastro` tem direção oposta à convenção e desenha um ciclo
- Linha `acesso_api -. "portas EscopoOrganizacional e ResolucaoDeEmpresa, implementadas por cadastro" .-> cadastro` aponta do declarante ao implementador. A legenda diz "seta = depende da API pública de", e a outra aresta de porta (`preenchimento -. ... declaradas por avaliacao .-> avaliacao`) aponta do implementador ao declarante. Com `cadastro --> acesso_api` (linha 58) e `preenchimento --> avaliacao`, a primeira vira ciclo `acesso <-> cadastro`. O AD-27 diz que o diagrama é a lista verificada e o AD-1 que ciclo quebra o build: duas equipes geram o teste de formas opostas. Corrigir: remover a aresta (a dependência real já é `cadastro --> acesso_api`) ou anotá-la como não verificada, com uma convenção única para portas.
- Mesmo problema de omissão no AD-30: `EliminacaoDeTitular` é declarada por `cadastro` e implementada por `acesso`, `comunicacao` e `preenchimento` (e demais módulos com dado pessoal). Só `cadastro --> acesso_api` existe; faltam as portas inversas para `comunicacao` e `preenchimento` (AD-19: "A declara a porta, B implementa"), e o AD-27 proíbe aresta não declarada.

### N-2 (M). `empresa` lida sem RLS pelo login e pelo `infra`, sem constar na lista de exceções e violando AD-1/AD-2
- AD-3: `ResolucaoDeEmpresa` é "leitura sem RLS limitada a `id`, `situacao`". AD-28 lista as tabelas sem RLS e `empresa` não está nela; o teste de esquema "toda tabela com `empresa_id` tem política" e "toda tabela fora da lista falha o build" não diz como a tabela passa. AD-20 e AD-29 falam em "política RLS explícita para `app_descoberta`" em `empresa`, o que é outra solução (política por papel), e o login roda com o papel da aplicação, não com `app_descoberta`. Escolher: função `SECURITY DEFINER`, política para `app_rw` só em `id, situacao`, ou `empresa` na lista sem RLS; dizer qual papel executa o login e que o login abre `paraEmpresa` com motivo (hoje `emRequisicao()` não existe antes da sessão).
- AD-20 faz `infra` enumerar "`empresa.id` e situação", tabela de `cadastro`: leitura cruzada fora da API pública (AD-2) e dependência de `infra` em módulo de negócio, que a "Regra do desenho" proíbe. Declarar como exceção (como o AD-25 fez com `escopo_visivel`) ou fazer o enumerador via porta de `cadastro`.

### N-3 (M). AD-25: `escopo_visivel` não tem chave de usuário e o RLS de view não está definido
- A view `escopo_visivel(empresa_id, no_escopo_id, unidade_id, departamento_id)` não diz quem é `no_escopo_id` (raiz do solicitante? nó listado?) nem como o consumidor o obtém sem chamar a porta; a porta `EscopoOrganizacional` devolve "pares", e o consumidor faz join na view: dois caminhos para o mesmo escopo. Em PostgreSQL, a view roda com os privilégios do dono (a política RLS das tabelas de base não se aplica ao chamador sem `security_invoker`); a espinha fixa "política RLS", mas não esse atributo. A exceção ao AD-2 não está refletida no teste do AD-19 (regra ArchUnit só vê classes, não SQL): dizer como é verificada.

### N-4 (M). AD-17/Adiado: o `bytea` cobre planilha, mas a foto e o relatório gerado não têm destino
- Foto do funcionário e arquivo exportado continuam sem lugar enquanto o armazenamento de objetos está adiado; com duas instâncias sem estado (AD-14) cada módulo escolhe de novo. Também faltam biblioteca única de geração e escape de fórmula na exportação (AD-17 só o exige "ao exportar planilhas" sem dono). Registrar no Adiado a decisão provisória (mesma tabela `bytea`, ou sem foto na primeira versão).

### N-5 (M). AD-3 `set_config(..., true)` vs AD-20 "só `SET LOCAL`" vs AD-23 "primeira instrução `SELECT … FOR UPDATE`"
- Persiste da r3 (R-4). `SELECT set_config` é uma consulta e fixa o snapshot do `REPEATABLE READ` antes do `FOR UPDATE`; `SET LOCAL` não. Fixar `SET LOCAL` no AD-3 (ou dizer que o `set_config` roda antes do `BEGIN ISOLATION LEVEL`, impossível na mesma transação) e que o AD-23 conta como primeira instrução a primeira consulta de dados.

### N-6 (M). AD-28: contador de `sequencia` "sob o lock que a transação já detém" conflita com AD-19, e lacuna pode travar consumidor
- Muitos emissores só detêm `FOR SHARE` na avaliação (AD-19: gravação de resposta, finalização de etapa) e a regra proíbe upgrade para escrita na mesma linha; incrementar contador "na linha do agregado" exige escrita. Dizer qual é o agregado de cada tipo de evento (por exemplo `etapa_participante`) e que o contador não fica na linha da avaliação.
- Cada consumidor assina só alguns tipos, mas a sequência é por agregado: o consumidor vê lacuna por evento que não assina e "adia" para sempre. Falta regra (consumidor avança também sobre tipos ignorados, ou sequência por tipo, e tempo máximo de espera com alerta).

### N-7 (B). AD-29: contradição interna sobre o dono das partições
- "Não há papel dono de partições de auditoria" e, três frases adiante, `migracao` é "dono das tabelas e das partições de auditoria e com `BYPASSRLS`". Reescrever: "o único dono é `migracao`, sem uso em tempo de execução, e o expurgo é a função `SECURITY DEFINER`". O teste de privilégios cita "papel de aplicação": dizer que a função não pode remover partição não vencida (teste) e que `migracao` é exercido só pelo pipeline.

### N-8 (B). Tags e datas
- Memlog (2026-10-21): "aperto de AD-7/9/10/11/12/13/15/17/19-27 confirmados pelo responsável". AD-9 e AD-10 seguem `[ASSUMPTION sobre a forma]`/`[ASSUMPTION]` embora carreguem snapshot e máquina de estados; só AD-14 está fora da lista confirmada. A legenda da tag diz sessões de "2026-10-13 a 2026-10-20", mas AD-29/30 foram adotados em 2026-10-21. Alinhar a legenda e as tags de AD-9 e AD-10.

## Cobertura contra o checklist

| Critério | Resultado |
| --- | --- |
| Pontos reais de divergência fixados | Maioria. Faltam: direção das portas no grafo (N-1), acesso a `empresa` (N-2), escopo da view (N-3), lugar da foto/exportação (N-4), local do contrato (R-5). |
| Rule aplicável que previne a divergência | Em geral sim, com teste por AD. Falham: AD-3 vs AD-20 vs AD-23 (N-5), AD-28 sequência vs AD-19 (N-6). |
| Adiado sem divergência | Quase: exportação e arquivos melhoraram; foto, relatório e "particionamento" (partição de auditoria já exigida pelo AD-21) ainda ambíguos. |
| Altitude (implantação, provedor, operação) | Provedor, RLS, RPO/RTO e LGPD têm posição ou pendência com dono. Hospedagem do SSR, TLS/ingress, rotação de segredos, configuração por ambiente e pools seguem sem posição (R-6). |
| Consistência interna (ADs citando ADs, grafo vs texto, tags) | Referências cruzadas conferidas e corretas (AD-3→20/29, AD-7→21, AD-9→23/26, AD-11→19/28, AD-13→21/22, AD-21→28/29, AD-24, AD-25, AD-27). Defeitos: N-1, N-2, N-5, N-7, N-8. |

## Pontos fortes confirmados

Portas de auditoria e acesso completas no grafo; `confirmarSenha` como funil único (AD-7/AD-8/D53); corte do consolidado sem contador; saída da auditoria por função `SECURITY DEFINER`; tabela de papéis (AD-29); LGPD com limites honestos (pseudonimização); AD-26 com `par_de_consenso` e `aceite_consenso` de dono único; ordem de locks com advisory de duas chaves (AD-19).
