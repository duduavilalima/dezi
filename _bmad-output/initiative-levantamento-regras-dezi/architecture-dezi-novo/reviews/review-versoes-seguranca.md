# Revisão: versões e segurança/conformidade da espinha de arquitetura do DEZI novo

Data: 2026-10-20. Alvo: `architecture-dezi-novo.md` (status draft, atualizado em 2026-10-20). Revisão refeita do zero contra a espinha atual; substitui a anterior.

Método: leitura integral da espinha; WebSearch/WebFetch contra fontes primárias quando possível (spring.io, angular.dev, documentação Redgate/Flyway, README do ShedLock, documentação do Spring Modulith, OWASP). Itens que não consegui confirmar estão marcados como tal.

## Veredito

**Aprovada com ressalvas.** A espinha é rigorosa em isolamento, sessão, auditoria e segredos, e quase todas as versões da tabela "Stack" foram de fato checadas e estão corretas. Os problemas são: (a) a tabela de Stack está defasada em um patch (Boot 4.1.0 vs 4.1.1) e ainda deixa quatro linhas como "a confirmar" que são resolvíveis agora; (b) o Spring Modulith publication registry (AD-21) é uma tabela fora do modelo de RLS/LGPD que a espinha não trata; (c) lacunas de LGPD (base legal, direitos do titular, retenção, cifra em repouso, DPA/operador) praticamente ausentes para dados de RH e de avaliação de desempenho.

## Lente 1: Versões e existência das tecnologias

| Item na espinha | Verificação em 2026-10-20 | Situação |
| --- | --- | --- |
| Spring Boot "4.x; 4.1.0 reportado por trackers, confirmar" | 4.1.0 GA em 2026-06-10 (blog spring.io); **4.1.1 em 2026-08-20** (98 correções). A espinha cita 4.1.0 "por trackers" | Defasado em um patch; a fonte primária existe e a frase "confirmar no GitHub releases" já pode ser fechada. Fixar 4.1.1 (ou o último 4.1.x no scaffolding). |
| Spring Modulith "2.1.0 (2026-06-11) ou 2.0.x; exigência de Boot do 2.1 a confirmar" | 2.1.0 GA em junho; **2.1.1 existe** (blog de 2026-08-26, junto com 2.2 M1 que mira Boot 4.2 M1). A linha 2.1 acompanha Boot 4.1 (milestones e RC mapeados para 4.1). Exigência exata de Boot não consegui ler (a doc não a declara; o mapa está em spring.io/projects/generations) | Inferência forte de compatibilidade com Boot 4.1; fixar 2.1.1. Remover a alternativa "2.0.x" (linha para Boot 4.0, sem ganho). |
| Publication registry (AD-21) | Existe e está maduro em 2.x: repositórios JPA/JDBC, modos de conclusão `UPDATE`/`DELETE`/`ARCHIVE`, estados `PUBLISHED/PROCESSING/COMPLETED/FAILED/RESUBMITTED`, `FailedEventPublications`, staleness monitor, republicação no restart | Confirmado. Ver achados S-2 e V-3 sobre o que a espinha não decidiu. |
| JDK 25 (LTS) | Páginas Oracle listam 25 como LTS atual e atualizações gratuitas (NFTC) até setembro de 2028. A busca devolveu cache antigo sobre JDK 26/27; não confirmei o estado de JDK 27 | Correto. Nota: o legado/AGENTS.md usa JDK 11; o salto é intencional (sistema novo), mas as imagens base e o runtime do provedor precisam de 25. A confirmar com o provedor. |
| PostgreSQL 18 (18.6 em 2026-08) | Neon documenta 18.6 em agosto/2026; calendário oficial: próxima menor em 2026-11-12. **PostgreSQL 19 prevista para setembro/outubro de 2026** (não confirmei se já saiu) | 18 é uma escolha válida e suportada até 2030. Registrar que 19 saiu ou está saindo e que 18 foi escolhido por maturidade nos provedores gerenciados (decisão, não omissão). |
| Flyway "a confirmar" | Última versão publicada: **13.10.2 (2026-10-15)** (Redgate). Suporte a PostgreSQL 18 consta na matriz "Latest" da Redgate (12 a 18). Spring Boot 4 não verificado nas notas (a página truncou) | Linha deveria estar preenchida: Flyway 13.x, módulo `flyway-database-postgresql` separado. A compatibilidade do starter com Boot 4.1 deve ser validada no scaffolding (Boot 4 modularizou autoconfigurações). |
| ShedLock "a confirmar" | README: **7.10.1**; matriz lista 7.x como testada com Spring 7.0 e Boot 4.x; exige JVM 17+ | Resolvido: fixar 7.x. Sem pendência real. |
| Argon2id via Spring Security + Bouncy Castle | `Argon2PasswordEncoder` existe e exige Bouncy Castle em runtime (confirmado em fontes secundárias; não consegui abrir a doc do Spring Security 7). Padrão do encoder (4 MiB, t=3) é fraco | Existe e serve. **Parâmetros "a fixar" é lacuna**: OWASP (fonte primária lida) dá mínimo de 19 MiB, t=2, p=1 (ou 46 MiB, t=1, p=1). Fixar já. |
| Angular 22.1.1 / TypeScript 6.0.3 / Node "^22.22.3 \|\| ^24.15.0 \|\| >=26" | angular.dev: Angular 22.0.x exige Node `^22.22.3 \|\| ^24.15.0 \|\| ^26.0.0` e TypeScript `>=6.0.0 <6.1.0`. Conferem com a espinha (exceto `>=26`, que é mais frouxo que `^26.0.0`; usar `^26.0.0`). 22.1.1 e TS 6.0.3 não foram reconfirmados por mim, só "existem no npm" segundo a espinha | Coerente. Ressalva: TypeScript 6.0.x é faixa estreita; dependências de terceiros (Storybook, ng-packagr) já tiveram peers que rejeitam TS 6. |
| OpenAPI generator (AD-15: "cliente Angular gerado") | **A espinha não nomeia o gerador nem a versão.** Pesquisa não achou confirmação oficial de que o `typescript-angular` do OpenAPITools suporta Angular 22; há alternativas de terceiros (ng-openapi-gen, geradores com `httpResource` para Angular 22+) | Lacuna V-1. |
| TOTP (AD-7) | **A espinha não nomeia biblioteca.** Spring Security não traz TOTP pronto para este caso | Lacuna V-2. |
| RLS no Postgres gerenciado | Marcado "a confirmar" e condicionando a escolha de provedor. RLS é recurso nativo do Postgres e os serviços gerenciados comuns o suportam; o risco real não é "suporta RLS" e sim: papéis sem `BYPASSRLS`/`SUPERUSER`, `FORCE ROW LEVEL SECURITY`, múltiplos papéis (AD-8, AD-20) e `REVOKE` em tabelas, que alguns provedores restringem; e pooler gerenciado em modo transação | Item aberto, mas mal enunciado. Reformular como checklist de capacidades do provedor. |
| Fuse (Envato), Material, Tailwind 4, Transloco 8, ApexCharts | "as do Fuse"; não verifiquei | Aceitável por delegação; a versão efetiva é a do template adquirido. Licença A4 pendente. |

### Achados de versão

**V-1 (Média). Gerador de cliente OpenAPI não escolhido nem validado contra Angular 22/TS 6.** AD-15 faz do build quebrar se o contrato divergir, mas o gerador é um ponto de falha de compatibilidade (Angular 22 + TS 6.0 estreito). Escolher e fazer um spike de geração no scaffolding; registrar versão exata e como o SSR (`HttpClient` no servidor) usa o cliente. Para o lado do Spring, nomear a lib (springdoc-openapi ou contrato-primeiro) e a versão compatível com Boot 4.

**V-2 (Média). Biblioteca TOTP e política de cifra do segredo não nomeadas.** AD-7 exige TOTP com códigos de recuperação e segredo cifrado, sem dizer a lib (ex.: java-totp/otp-java), o algoritmo (SHA-1 padrão de apps), a janela de tolerância, nem a chave de cifra (cofre, envelope). Sem isso a versão "confirmada" não existe. Códigos de recuperação devem ser guardados com hash (Argon2id), não cifrados.

**V-3 (Média). Linhas "a confirmar" da Stack já resolvíveis.** Fechar agora: Boot 4.1.1, Modulith 2.1.1, Flyway 13.10.x, ShedLock 7.10.x, parâmetros Argon2id. A tabela diz "versões a fixar no scaffolding", mas decisões "Adopted" (AD-14, AD-21, AD-8) já dependem delas. Sobram genuinamente abertas: provedor/RLS, JDK 25 no runtime do provedor, e a exigência de Boot do Modulith 2.1 (validar no POM).

**V-4 (Baixa). Fonte "trackers" para Boot.** A espinha cita 4.1.0 por trackers; a fonte primária (spring.io) confirma 4.1.0 e 4.1.1. Citar fonte primária e data.

**V-5 (Baixa). Ausência de política de atualização de patch/CVE.** AD-16 diz que Angular/TS são atualizados por decisão do time em ciclo planejado, mas nada obriga a aplicar patches de segurança (Boot, Node SSR, Fuse copiado, dependências npm). Como o Fuse vira "código nosso", as correções do fornecedor deixam de chegar sozinhas. Incluir verificação de dependências (SCA/Dependabot) no pipeline AD-18.

**V-6 (Baixa). Debito de Spring Modulith no `verify()` e 2.2 M1 já mirando Boot 4.2.** A linha 2.1 deve sair de suporte aberto em alguns meses; planejar a migração para 2.2/Boot 4.2 após o GA.

## Lente 2: Segurança e conformidade (LGPD)

Pontos fortes (sem ressalva): sessão opaca com hash no banco e cookie `__Host-` (AD-5); RLS com `set_config(..., true)`/`SET LOCAL`, fail-closed, `FORCE`, sem `BYPASSRLS` e papel de plataforma separado (AD-3, AD-20); 404 entre empresas; matriz de autorização com teste negativo por célula (AD-4); auditoria só de inserção com `REQUIRES_NEW` para negados (AD-8, AD-21); segredo fora do corpo da outbox (AD-22); anonimato mínimo no servidor (AD-12); defesa contra enumeração de conta no login e reset; bloqueio sem DoS por usuário (AD-7); XXE/zip bomb/injeção de fórmula na importação (AD-17).

### Achados

**S-1 (Alta). LGPD quase inexistente na espinha.** Os dados são pessoais de RH e de avaliação de desempenho (opiniões sobre pessoas, notas, PDI, comentários livres, possivelmente dados sensíveis em comentários). A palavra LGPD não aparece. Faltam, como decisões ou itens "a confirmar" explícitos:
- base legal por tratamento (execução de contrato/legítimo interesse) e papel da plataforma: **operador** em nome das empresas clientes (controladoras), o que exige contrato/DPA, subprocessadores (provedor de nuvem, SMTP) e registro de operações;
- direitos do titular: acesso, correção, eliminação e portabilidade por funcionário; como atender com exclusão lógica (AD "Estado e exclusão") e com resultado consolidado imutável (AD-11) e auditoria imutável (AD-8); definir anonimização em vez de exclusão;
- política de retenção por categoria (respostas, consolidado, PDI, auditoria, outbox, sessões, e-mails): hoje só "a confirmar" para a auditoria;
- encarregado (DPO) e plano de resposta a incidentes com notificação à ANPD;
- minimização e finalidade: o anonimato mínimo (AD-12) cobre exibição, mas respostas individuais por avaliador ficam em claro no banco; definir quem pode ler dado bruto (DBA, suporte, administrador de plataforma).
Recomendação: criar AD de "Proteção de dados pessoais" ou seção própria; ao menos um registro de decisão.

**S-2 (Alta). Publication registry do Spring Modulith (AD-21) fora do modelo de isolamento e de LGPD.** AD-21 adota o registro persistente de eventos, mas a espinha não diz:
- a tabela `event_publication` não tem `empresa_id` nem política RLS por padrão (é criada pelo Modulith, ou por Flyway se você a definir); AD-2 exige `empresa_id` em toda tabela de negócio e AD-3 exige RLS. A republicação no restart (`republish-outstanding-events-on-restart`) e a `IncompleteEventPublications` rodam **fora de requisição**, sem `paraEmpresa` (AD-20), então ou o papel sem RLS lê tudo, ou a republicação falha em silêncio (o modo de falha que AD-20 quer evitar). Definir donos, `empresa_id` na tabela, papel e como cada listener reabre o contexto da empresa a partir do `empresa_id` do evento (AD-27 já o inclui);
- o evento é serializado (Jackson) na tabela e AD-27 manda levar destinatários resolvidos (e-mail, nome): PII em JSON, no padrão `UPDATE` as publicações concluídas **permanecem** até expurgo. Definir `completion-mode` (`DELETE` ou `ARCHIVE` com expurgo), prazo e inclusão na retenção; testar que nenhum token aparece (AD-22 cobre só `comunicacao_*` e `auditoria_*`; incluir `event_publication`);
- AD-14 afirma "mesma infraestrutura de fila no Postgres" para parcial, outbox e jobs, enquanto AD-21 usa o registro para parcial e comunicação. Há duas filas de fato (registry e tabela de trabalho com `SKIP LOCKED`). Esclarecer qual é qual para não ter duas fontes de reentrega e duplicidade de efeito.

**S-3 (Média). Cifra em repouso e em trânsito não decididas.** Só se menciona cifra do segredo TOTP. Faltam: TLS entre ingress, SSR, backend e banco (`sslmode=verify-full`); criptografia em repouso do banco e dos backups (gerenciada pelo provedor, CMK?); cifra no nível de campo para comentários livres e PDI, se a análise de risco pedir; onde ficam as chaves e como giram. Backups contêm todas as empresas: política de restauração por empresa (para o direito de eliminação) e retenção de backup.

**S-4 (Média). Política de senha e login.** Boa base (12+, complexidade, lista de senhas vazadas, Argon2id). Ressalvas:
- troca forçada a cada 90 dias contraria NIST SP 800-63B (rotação periódica sem suspeita de comprometimento não é recomendada) e as regras de composição também; se é exigência do cliente/legado, registrar como decisão com motivo;
- "lista de senhas vazadas": fonte e atualização não definidas (HIBP k-anonymity expõe tráfego a terceiro; lista local é preferível);
- histórico de senhas (reuso), tamanho máximo (evitar DoS no Argon2 com entradas enormes) e custo do hash sob carga (Argon2 com 46 MiB × login concorrente: dimensionar memória das duas instâncias e limitar concorrência);
- TOTP só para administrador de plataforma. Administrador de RH da empresa e perfis que veem resultados de terceiros ficam só com senha: avaliar TOTP opcional/obrigatório para RH;
- política de reautenticação por ação e de logout global está definida; falta política de "sessões concorrentes" e visibilidade de sessões ao usuário.

**S-5 (Média). CSRF/SSR/CSP: detalhes a fixar.**
- `SameSite=Lax` + token XSRF + `Origin` é adequado. O padrão `XSRF-TOKEN` (cookie legível por JS, double-submit) está em tensão com o `__Host-` HttpOnly da sessão apenas se o cookie XSRF não tiver o mesmo tratamento; definir `Secure`, `Path=/`, sem `Domain` também para ele, e se o token é por sessão (preferível, Spring Security 6/7 usa mascaramento BREACH);
- SSR "só faz GET autenticado" e repassa o cookie: definir lista de cabeçalhos repassados (só `Cookie`, `X-Forwarded-*` controlado), proteção contra SSRF no `HttpClient` do SSR (URL base fixa, nunca vinda de cabeçalho `Host`), `allowedHosts` (já previsto) e cabeçalho `Vary: Cookie`/`no-store` verificado por teste contra mistura de respostas entre usuários (cache poisoning/vazamento no ingress ou CDN);
- CSP com nonce em SSR: mostrar que nonce por requisição funciona com o template Fuse (estilos inline do Material/Tailwind costumam exigir `style-src` relaxado); sem CSP efetiva, o XSS armazenado em comentários vira roubo de ação (cookie HttpOnly não protege contra ações feitas pela vítima);
- cabeçalhos de segurança completos (HSTS, `X-Content-Type-Options`, `Referrer-Policy`, `frame-ancestors`/`X-Frame-Options`, `Permissions-Policy`) não listados;
- IP real "a confirmar na implantação" é dependência de segurança direta do bloqueio por (IP, empresa, matrícula): atrás de balanceador/ingress, `X-Forwarded-For` forjado permite escapar do bloqueio; definir lista de proxies confiáveis.

**S-6 (Média). Auditoria: limites e cobertura de leitura.** AD-8 cobre escritas privilegiadas e acessos negados. Não cobre **leitura** de dado sensível (quem consultou resultado/PDI/comentário de quem, exportações, acesso do administrador de plataforma entre empresas), o que a LGPD (prestação de contas, art. 37/46) e investigações de incidente pedem. O administrador de plataforma cruza empresas (AD-3) sem que a espinha exija trilha, motivo e aprovação específicos por acesso (acesso "break-glass" com justificativa). Também: log de aplicação "sem dado pessoal" (Observabilidade, `[ASSUMPTION]`) precisa de teste/filtro, e a trilha deve ter retenção compatível com prescrição (a confirmar). A trilha mensalmente particionada com expurgo (AD-21) conflita com "imutável" se o expurgo roda por papel que "se registra": aceitável, desde que o registro do expurgo seja exportado fora do banco.

**S-7 (Média). Segredos e cadeia de suprimentos.** "Segredos no cofre do provedor" sem: rotação definida (só SMTP), segredo de assinatura/HMAC (hash de sessão: usar SHA-256 simples de 256 bits aleatórios é suficiente, ok), credenciais do banco (três ou quatro papéis: app, plataforma, leitor de fila, manutenção/expurgo, dono) e como o app as obtém sem cair em variáveis de ambiente em claro; Flyway roda com papel dono, distinto do papel da app (explicitar, pois AD-3 e AD-8 dependem disso); scan de segredos no repositório; imagem de contêiner e SBOM; assinatura de artefatos; SCA de npm (o Fuse traz muitas dependências) e Maven.

**S-8 (Média). Importação (AD-17) bem pensada, falta completar.** XXE, zip bomb e escape de fórmula estão. Faltam: tipo/magic bytes e extensão do arquivo, limite de linhas e de tempo de processamento, varredura de malware se o arquivo for armazenado (Armazenamento de arquivos está em Adiado), proteção contra XML bomb (billion laughs) além de XXE (`SecureProcessing`/limites de expansão em `XMLInputFactory`; ZIP interno do xlsx limitado em razão de compressão e total descompactado), uso de lib (POI/streaming) com versão corrigida de CVEs de zip bomb, isolamento (limite de memória) do worker para não derrubar o monólito, e escape de fórmula também na **leitura** (valor que começa com `=`, `+`, `-`, `@` que depois volta em exportação). A planilha contém PII em massa: retenção/exclusão do arquivo enviado e do relatório de erros por linha (que repete dados pessoais).

**S-9 (Baixa). RLS: pontos finos.** Bem coberto. Acrescentar: `SECURITY DEFINER` e views (executam com privilégios do dono e podem contornar RLS; proibir ou testar), `LEAKPROOF`/funções de política, índices e planos que vazam por canal lateral (contagens/erros de unicidade entre empresas: a unicidade global de matrícula/e-mail em `credencial_acesso` deve ser por empresa e a mensagem de erro de violação não pode revelar existência em outra empresa), `pg_dump`/réplica de leitura sem RLS, e o papel "plataforma" que lê `empresa_id, id, estado`: confirmar com `GRANT` por coluna (a espinha já diz "lista enumerada").

**S-10 (Baixa). Rate limiting geral e proteção da API.** Só o login tem limites. Faltam limite por usuário/IP nas demais rotas (especialmente exportação, importação, busca de resultados), limite de tamanho de corpo, paginação máxima (AD-15 cita), e proteção contra enumeração de ids (UUID já mitiga).

**S-11 (Baixa). Link de senha e e-mail.** Bom desenho (uso único, expiração, resposta uniforme, token emitido no envio). Falta: o token é entregue por e-mail em link; definir `Referrer-Policy: no-referrer` na página de redefinição, TTL concreto, e o que acontece se o e-mail da empresa for comprometido (aceite do consenso D53 exige a senha, bom). SPF/DKIM/DMARC do remetente (anti-phishing) não citados.

**S-12 (Baixa). Mobile/PWA.** A opção 2 menciona política de cache segura; a opção 3 exige tokens com refresh rotativo. Sem pendência de segurança no escopo atual; se a opção 2 for escolhida, o service worker não pode cachear respostas autenticadas.

## Resumo de prioridades

1. S-1: decisão de LGPD (operador, direitos do titular, retenção, DPA, incidente). Alta.
2. S-2: tratar o publication registry sob RLS/`paraEmpresa`/PII/retenção e esclarecer a dupla fila AD-14 x AD-21. Alta.
3. V-3 / V-1 / V-2: fixar Boot 4.1.1, Modulith 2.1.1, Flyway 13.x, ShedLock 7.x, parâmetros Argon2id (OWASP: >=19 MiB, t=2, p=1), gerador OpenAPI e lib TOTP. Média.
4. S-3, S-5, S-6, S-7, S-8: cifra em repouso/trânsito, CSP/cabeçalhos/proxy confiável, auditoria de leitura e acesso de plataforma, cofre/papéis do banco, endurecimento da importação. Média.
5. S-4: revisar rotação de senha de 90 dias contra NIST 800-63B (decisão consciente). Média.

## Não confirmado por mim

- Exigência exata de versão de Boot do Spring Modulith 2.1.x (a doc não declara; ver spring.io/projects/generations).
- Compatibilidade do starter Flyway com Boot 4.1 e a menção a PostgreSQL 18 nas notas de versão (a matriz Redgate indica suporte; a página de notas truncou).
- Estado de JDK 27 e de PostgreSQL 19 (podem ter saído; resultados da busca eram caches antigos).
- Angular 22.1.1 e TypeScript 6.0.3 exatos; apenas as faixas de Node/TS do Angular 22.0.x foram lidas em angular.dev.
- Documentação do Spring Security 7 sobre `Argon2PasswordEncoder` (não abri; apoio em fontes secundárias).
- Suporte do provedor de nuvem final a papéis e `FORCE RLS` (provedor não escolhido).

Fontes consultadas: spring.io/blog (Boot 4.1.0, 4.1.1; Modulith 2.1.x, 2.2 M1), docs.spring.io/spring-modulith (eventos), angular.dev/reference/versions, documentation.red-gate.com (Flyway), github.com/lukas-krecan/ShedLock, cheatsheetseries.owasp.org (Password Storage), neon.com/docs/postgresql/postgres-version-support, postgresql.org roadmap.
