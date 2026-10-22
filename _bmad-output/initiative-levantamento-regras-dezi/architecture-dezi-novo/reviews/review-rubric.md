# Revisão (rubrica) da espinha de arquitetura do DEZI novo

Revisão independente do estado atual da espinha (AD-1..27). Não confia na revisão anterior. A espinha não foi editada.

## Veredito

A espinha cobre bem as divergências de segurança, tenancy e dados, e as CAP-1..10 estão mapeadas. Mas as AD-18..27 deixaram contradições internas que ainda permitem que duas equipes construam coisas incompatíveis: o grafo de módulos não bate com regras que o usam, e há três mecanismos de evento/fila descritos de formas conflitantes. Precisa de uma passada de consistência antes de ser base de implementação.

## Achados

Severidade: A = alta (gera divergência real), M = média, B = baixa.

### A-1 (A). Ciclo e aresta ausente no grafo: liberação e token de e-mail
- AD-19 diz que "a liberação chama `preenchimento.prepararRespostas`". `avaliacao` é quem libera. Mas o grafo tem `preenchimento --> avaliacao` e nenhuma aresta `avaliacao --> preenchimento`. Chamar `prepararRespostas` cria ciclo, que o AD-1 manda quebrar o build. Falta uma porta inversa, como a de `RespostasDoParticipante`, ou mover a criação das linhas vazias.
- AD-22 diz que o worker de `comunicacao` "pede a `acesso` a emissão do token". Não existe aresta `comunicacao --> acesso`. O AD-27 diz que `comunicacao` "não consulta outro módulo". São duas regras contraditórias, e nenhuma está no grafo.
- O grafo tem `resultados --> catalogo` e `pdi --> catalogo`, enquanto o AD-9 diz que `resultados` lê só o snapshot, "nunca `catalogo`". Ou a aresta de `resultados` está sobrando, ou o AD-9 está incompleto. Como o AD-27 diz que o grafo é a lista verificada, o diagrama deve ser corrigido ou as regras explicadas.

### A-2 (A). Quem grava a outbox de e-mail: contradição entre AD-13, AD-21 e AD-27
- AD-13: "a ação grava o e-mail pendente na mesma transação".
- AD-21 canal 3 e AD-27: `comunicacao` consome um `EventoDeDominio` depois do commit, com destinatários já resolvidos. Então a linha da outbox seria gravada em outra transação, pelo consumidor.
- O AD-14 diz que parcial, outbox e jobs usam a mesma fila Postgres com `SKIP LOCKED`. O AD-21 diz que o canal 3 usa o publication registry do Modulith. Isso são quatro mecanismos nomeados (registry, outbox, fila de parcial, ShedLock) para três categorias. Uma unidade implementa o worker de e-mail de um jeito e outra implementa o parcial de outro.
- Correção sugerida: dizer em uma frase que o evento depois do commit é o gatilho e que a linha de outbox é gravada pelo consumidor `comunicacao`, e retirar "na mesma transação" do AD-13. Alternativa: manter a regra e o canal 3 não ser usado por `comunicacao`.

### A-3 (A). `versao_de_corte` (AD-11) conflita com a guarda de estado (AD-19) e com o dono único (AD-2)
- O AD-11 incrementa um contador "a cada gravação de resposta", na avaliação. O AD-19 faz toda gravação de resposta tomar `FOR SHARE` na linha da avaliação, e concluir toma `FOR UPDATE`.
- Incrementar na mesma linha exige lock de escrita. Isso serializa todas as gravações de resposta por avaliação, contradiz o `FOR SHARE`, e faz `preenchimento` escrever em linha dona de `avaliacao` (viola AD-2 e AD-19).
- Falta decidir onde o contador mora, quem o incrementa e com que custo de contenção. É um ponto real de divergência entre as unidades `preenchimento` e `resultados`.

### M-1 (M). Texto obsoleto sobre auditoria depois do AD-21
- Parágrafo "Regra do desenho": "Nenhuma regra de negócio chama ... `auditoria`: elas recebem eventos". O AD-21 canal 1 diz o contrário: porta `RegistroDeAuditoria.registrar` chamada pelo dono da ação, e proíbe listener.
- Convenções, linha Eventos, e AD-8 ("evento síncrono"): mesma linguagem antiga.
- O diagrama desenha `-. evento .-> auditoria`, e só para `acesso`, `avaliacao`, `catalogo` e `preenchimento`. As arestas reais (porta síncrona) vêm de qualquer módulo que audita. Faltam `cadastro` (desligar, recesso) e `pdi` (aprovação, D22). Pelo AD-27, ausência de aresta significa proibido.
- O AD-8 não cita AD-21 em Binds, e a lista "Cobre" não menciona `AuditoriaDeNegado` (403, bloqueio, login falho).

### M-2 (M). Resquícios do bloqueio por usuário (AD-7 mudou para atraso progressivo)
- AD-7 ainda diz "desbloqueio manual pelo RH" e "administrador sem bloqueio permanente". Com bloqueio duro só por (IP, empresa, matrícula), não está dito o que o RH desbloqueia, nem se o desbloqueio zera o atraso. O AD-18 fala em alerta de "bloqueio em massa", sem definir o que conta.
- Os valores do atraso e do teto estão "a fixar". Sem eles a regra não é testável nem enforceável (decisão 4 deixa isso aberto). Um atacante ainda pode impor atraso máximo ao usuário legítimo até o teto: o risco residual de DoS não é citado no Prevents.
- Convenções diz "401 único no login", mas a resposta de bloqueio duro e de atraso (429? 401 com `Retry-After`?) não está definida. Isso é divergência entre backend e frontend (AD-16 trata 401/403).

### M-3 (M). Adiado deixa pontos de divergência
- "Armazenamento de arquivos" adiado, mas o AD-17 exige upload e processamento assíncrono. O arquivo precisa ficar em algum lugar entre o upload e o processamento. O AD-1 diz que a importação vive "em `cadastro` e `avaliacao`", dois módulos: cada um escolhe um jeito. Falta decidir, no mínimo, uma infraestrutura única (porta de armazenamento + pipeline de importação compartilhado) e a sua localização.
- "Relatórios e exportações PDF/Excel: cada módulo gera o seu". Isso é exatamente o que gera três bibliotecas e três aplicações do anonimato (AD-12) e do escopo (AD-4). O AD-17 já pressupõe exportação ("escape de fórmula ao exportar"). Falta uma regra mínima: biblioteca única e passagem obrigatória pela API que já aplica o escopo e o anonimato.
- Mobile (A3) pendente é aceitável, já está isolado.

### M-4 (M). Envelope operacional: provedor e RLS sem plano alternativo
- A AD-3 depende de RLS e diz "suporte do provedor a confirmar"; o AD-18 diz que o RLS "condiciona a escolha". Falta dizer o que acontece se nenhum provedor viável oferecer RLS (por exemplo, volta para filtro de repositório e teste, sem a segunda camada). Hoje é bloqueio de arquitetura sem saída declarada.
- Ambientes: "duas instâncias ... postura provisória", RPO/RTO a confirmar, orquestração/hospedagem do Node SSR, ingress/TLS, rotação do cofre, e gestão de configuração por ambiente só aparecem como intenção. Estão como "a confirmar", o que é aceitável como pergunta aberta, mas há muitos itens operacionais soltos. Sugere-se listá-los explicitamente na seção "Decisões que dependem do negócio" (hoje só o item 6 cobre RPO/RTO/provedor).
- Dados pessoais: não há menção a LGPD (base legal, retenção de dados de avaliação, eliminação de dados de desligados, criptografia em repouso, cifragem dos backups). É dado sensível de RH. Pelo menos uma decisão adiada com dono.

### M-5 (M). Stack não verificada como atual
- Spring Boot 4.1.0 "reportado por trackers"; Modulith "2.1.0 ou 2.0.x"; Flyway e ShedLock "a confirmar" (compatibilidade com Postgres 18 e Boot 4). Para o critério "tecnologia nomeada verificada" a tabela ainda tem 3 de 9 linhas sem versão confirmada. JDK 25 LTS, PostgreSQL 18 e Angular 22.1.1/TS 6.0.3 estão razoáveis.
- O AD-19 diz que "o teste de arquitetura impede escrita em tabela por classe fora do dono". O `verify()` do Modulith não faz isso sozinho. Precisa de regra ArchUnit customizada, que a espinha só cita como "regras extras". Declarar isso como trabalho de scaffolding evita que alguém assuma que já vem pronto.

### B-1 (B). Pequenas inconsistências
- AD-3 usa `set_config(..., true)` e AD-20 usa `SET LOCAL`. Equivalentes, mas o AD-20 diz "ambos fazem só SET LOCAL"; escolher uma forma só. O AD-3 fala em "papel de plataforma" e o AD-20 em "segundo papel de banco, somente leitura": são o mesmo papel ou dois? Há ainda "administrador de plataforma ... papel/política". Três papéis com nomes soltos.
- AD-3: o login recebe "empresa" e resolve a empresa antes da sessão, mas a leitura de `empresa` e `credencial_acesso` por nome de empresa ocorre antes de a variável estar definida. Dizer como (tabela `empresa` fora da RLS ou função `SECURITY DEFINER`).
- AD-14 mistura ShedLock (agendamento) com `SKIP LOCKED` (fila): esclarecer que ShedLock só dispara o varredor.
- Mapa Capacidade→Arquitetura: CAP-7 omite AD-9 e AD-26; CAP-8 omite AD-25 e AD-23; CAP-1 omite AD-21; CAP-6 omite AD-20. CAP-9 só tem AD-4 (sem máquina de estados nem dono de dados), embora "a confirmar".
- AD-8 repete duas vezes que o hash encadeado é adiado.
- AD-9, AD-10 e AD-14 são `[ASSUMPTION]` mas são carga estrutural (snapshot, estados, jobs); convém o responsável adotar ou corrigir.

## Cobertura contra o checklist

| Critério | Resultado |
| --- | --- |
| Pontos de divergência reais fixados | Em grande parte sim. Faltam: gravação da outbox (A-2), contador de corte (A-3), importação e armazenamento (M-3). |
| Regras enforceáveis e que previnem a divergência declarada | Maioria sim (testes por AD). Frágeis: AD-7 (valores a fixar), AD-19 (verify do Modulith não cobre escrita por tabela). |
| Adiado não causa divergência | Não: armazenamento de arquivos e relatórios (M-3). |
| Tecnologia verificada | Parcial (M-5). |
| CAP-1..10 cobertas | Sim no mapa; CAP-9 fina. |
| Envelope operacional (deploy, provedor, operação) | Decidido em parte, o resto aberto como "a confirmar"; falta plano para RLS indisponível e LGPD (M-4). |
| Consistência interna | Falha: A-1, A-2, A-3, M-1, M-2. |

## Pontos fortes

Isolamento em duas camadas com papéis separados e contexto fora de requisição (AD-3, AD-20); matriz declarativa com teste negativo por célula (AD-4); trilha com tratamento de auditoria de negado (AD-21); snapshot versionado do catálogo (AD-23); forma canônica de resposta e completude única (AD-26); decisões abertas rastreadas.
