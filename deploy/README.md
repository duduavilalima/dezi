# DEZI Deploy

Projeto utilizado para implantar o DEZI via Docker Compose em todos os seus ambientes de execução, internos e externos.

> Ambientes de desenvolvimento local não inclusos! Vide seção [Ambiente de desenvolvimento local](#ambiente-de-desenvolvimento-local) para maiores informações.

# Índice

<!-- toc -->

- [DEZI Deploy](#dezi-deploy)
- [Índice](#%c3%8dndice)
- [Quickstart](#quickstart)
  - [Configurar o ambiente de execução](#configurar-o-ambiente-de-execu%c3%a7%c3%a3o)
  - [Controlar o ambiente de execução](#controlar-o-ambiente-de-execu%c3%a7%c3%a3o)
  - [Destruir o ambiente de execução](#destruir-o-ambiente-de-execu%c3%a7%c3%a3o)
  - [Reconfigurar o ambiente de execução](#reconfigurar-o-ambiente-de-execu%c3%a7%c3%a3o)
  - [Fazer dump e restore do banco de dados](#fazer-dump-e-restore-do-banco-de-dados)
    - [Dump](#dump)
    - [Restore](#restore)
    - [Migrar Banco de dados](#migrar-banco-de-dados)
  - [Controlando os serviços via `docker-compose`](#controlando-os-servi%c3%a7os-via-docker-compose)
- [Arquitetura](#arquitetura)
  - [Modos de execução](#modos-de-execu%c3%a7%c3%a3o)
  - [Ambientes de execução](#ambientes-de-execu%c3%a7%c3%a3o)
  - [Projetos e Serviços](#projetos-e-servi%c3%a7os)
  - [Gerenciamento dos projetos](#gerenciamento-dos-projetos)
    - [Fases de projeto](#fases-de-projeto)
    - [Estágios e tarefas configuradas no Gitlab CI/CD](#est%c3%a1gios-e-tarefas-configuradas-no-gitlab-cicd)
    - [Branches, tags e suas associações com as fases de projeto](#branches-tags-e-suas-associa%c3%a7%c3%b5es-com-as-fases-de-projeto)
    - [Nomenclatura das imagens Docker](#nomenclatura-das-imagens-docker)
    - [Ambientes de execução e suas associações com as fases de projeto](#ambientes-de-execu%c3%a7%c3%a3o-e-suas-associa%c3%a7%c3%b5es-com-as-fases-de-projeto)
- [Ambiente de desenvolvimento local](#ambiente-de-desenvolvimento-local)
- [Estrutura do projeto Deploy](#estrutura-do-projeto-deploy)
  - [Sobre o `compose.sh`](#sobre-o-composesh)
    - [Comandos do `compose.sh`](#comandos-do-composesh)
  - [Instanciando arquivos e pastas `.sample`](#instanciando-arquivos-e-pastas-sample)
    - [Pastas](#pastas)
  - [Configurações-base do CI](#configura%c3%a7%c3%b5es-base-do-ci)
  - [Pasta de Configurações](#pasta-de-configura%c3%a7%c3%b5es)
  - [Pasta de Servidores (projetos Compose)](#pasta-de-servidores-projetos-compose)
  - [Pasta do Servidor (Projeto Compose) Principal (dezi)](#pasta-do-servidor-projeto-compose-principal-dezi)
    - [Sobre _dump_, _restore_ e a pasta `db-scripts`](#sobre-dump-restore-e-a-pasta-db-scripts)

<!-- tocstop -->

# Quickstart

Primeiramente, clone o [`avd/deploy`](http://git.fasolti.com.br/avd/deploy) em uma pasta de sua escolha:

```sh
#!/bin/sh
git clone http://git.fasolti.com.br/avd/deploy
cd deploy
```

Todo o deploy do DEZI é baseado em imagens Docker previamente compiladas via Gitlab CI/CD.


## Configurar o ambiente de execução

Na pasta do projeto Deploy, execute: 

```sh
./compose.sh setup <NOME_DO_AMBIENTE>
```

Onde `NOME_DO_AMBIENTE` pode ser:
- `production` (ou `prod`);
- `staging`;
- `staging-hotfix`; ou
- `demo`.

Um arquivo `run/.env` será criado com as variáveis de ambiente a serem utilizadas para o ambiente escolhido. 

> Vide seção sobre a [Arquitetura do DEZI](#arquitetura) para maiores detalhes sobre cada ambiente.

Dependendo do ambiente, o script poderá gerenciar mais de um projeto Compose.

> Vide seção sobre a [Estrutura do projeto Deploy](#estrutura-do-projeto-deploy) para maiores detalhes sobre os projetos Compose existentes e organização geral do script de deploy.


## Controlar o ambiente de execução

Uma vez executada a etapa de _setup_, os serviços poderão ser iniciados.

Para inicializar: 

```sh
./compose.sh up
```

Para parar: 

```sh
./compose.sh down
```

Para reiniciar: 

```sh
./compose.sh redeploy
```


## Destruir o ambiente de execução

O comando a seguir destruirá os serviços e todas as redes e volumes associados a eles:

```sh
./compose.sh destroy
```

> Uma confirmação será solicitada antes de destruir o ambiente.


## Reconfigurar o ambiente de execução

Para reconfigurar o ambiente (e.g. mudar de `production` para `staging`):

1. Certifique-se de que todos os serviços tenham sido parados:
   ```sh
   ./compose.sh down
   # Ou, opcionalmente:
   ./compose.sh destroy
   ```
1. Execute:
   ```sh
   ./compose.sh clean
   ```
   `clean` apagará todos os `.env` e demais arquivos instanciados a partir de arquivos `.sample`.
1. Reconfigure o ambiente:
   ```sh
   ./compose.sh setup <NOVO_AMBIENTE>
   ```

> Esta funcionalidade é providenciada para fins de testes.


## Fazer dump e restore do banco de dados

### Dump

Para fazer um dump (via `pg_dump`, compactado via `xz`) do banco de dados do Compose principal (`servers/dezi`):

```sh
./compose.sh dump [NOME_ARQUIVO]
```

Se `NOME_ARQUIVO` não for fornecido, um arquivo no formato `dezi-${ENVIRONMENT_NAME}.${TIMESTAMP}.nolog.dmp.xz` será gerado na pasta atual (i.e. `pwd`).

`TIMESTAMP` é a data e hora atual em _localtime_, incluindo o _offset_.

O nome do arquivo gerado será sempre impresso na saída padrão; se desejável, é possível utilizar [substituição de comando](https://www.gnu.org/software/bash/manual/html_node/Command-Substitution.html#Command-Substitution) para atuar sobre o nome do arquivo recém-gerado, e.g. `mv "$(./compose.sh dump)" ./backups`.

Características da operação de _dump_:

- O _dump_ terá apenas o DDL das tabelas `log_auth` e `log_entity`, mas não os seus dados.


### Restore

Para restaurar de um dump pré-existente:

```sh
./compose.sh restore <NOME_ARQUIVO_DUMP_XZ>
```

Características da operação de _restore_:

1. Se houver uma imagem de migração de BD para a versão do projeto WEB atual, e.g. `docker.fasolti.com.br/dezi-db-migration:${DEZI_VERSION_SERVICES}` (onde `DEZI_VERSION_SERVICES` é atribuída e obtida do arquivo `run/.env`), haverá migração do BD automaticamente.
1. O usuário padrão do sistema (para acesso ao banco de dados; indicado em `DB_USER`) será criado automaticamente, se não existir.
1. O _database_ (indicado em `DB_NAME`) será criado, se não existir.
1. O dono do database será alterado para o usuário padrão do sistema (indicado em `DB_USER`) (e.g. `deziuser`).
1. As tabelas de log `log_auth` e `log_entity` serão criadas se não existirem.
   > _**FIXME:** Fazer com que o script de backup em produção gere dumps com o DDL destas tabelas, para que esta erapa não seja mais necessária!_
1. Para os ambientes de homologação (i.e. `staging` e `staging-hotfix`):
   - A senha de todos os usuários será alterada para uma senha padrão.
   - O flag `primeiro_acesso` de todos os funcionários será revertido para `false`.
   - O e-mail de todos os funcionários será alterado para um e-mail válido, mas inexistente.


Vide maiores informações na seção [Estrutura do projeto Deploy](#estrutura-do-projeto-deploy).

### Migrar Banco de dados

Para migrar o BD manualmente, seguindo a versão do projeto WEB atual:

```sh
./compose.sh migrate
```


## Controlando os serviços via `docker-compose`

O projeto Docker Compose se encontra na pasta `run`. Se em algum momento for necessário mnanipular os serviços do Docker Compose, seja manualmente ou de forma automatizada, basta entrar nesta pasta e executar os comandos.

Note que alguns ambientes podem 

Alguns exemplos:

```sh
# Reiniciar serviço
docker-compose up --force-recreate --delete-orphans --detached <NOME_SERVICO>

# Parar/iniciar serviço (sem excluir o container)
docker-compose stop <NOME_SERVICO>
docker-compose start <NOME_SERVICO>

# Terminar serviço (removendo containers e redes)
docker-compose down <NOME_SERVICO>

# Terminar serviço (removendo containers, redes e volumes)
docker-compose down -v <NOME_SERVICO>
```

> Vide seção [Projetos e Serviços](#projetos-e-servicos) para ver a lista de serviços do projeto Compose principal.

# Arquitetura

## Modos de execução

Para os projetos do DEZI, há dois **modos de execução** (configurado via variáveis de ambiente `ENVIRONMENT`):

- **`development`**: Atribuído em ambientes de execução que rodem em modo de desenvolvimento local.
- **`production`**: Atribuído em ambientes de execução que rodem em modo de produção ou homologação, ou que tenham como público-alvo o cliente final.

Em geral, o modo de execução dita a forma como o projeto será compilado e/ou executado. Por exemplo, em modo de produção, minificação e compressão são ativadas para o projeto webapp. O efeito desta variável de ambiente varia de acordo com o projeto; portanto, veja a documentação de cada um para maiores informações.

Por padrão, no CI, é utilizado `ENVIRONMENT=production`, para todos os projetos.

> A escolha dos valores de `ENVIRONMENT` (e o que cada um significa) se baseia em práticas adotadas por projetos como [Express](http://expressjs.com/en/advanced/best-practice-performance.html#set-node_env-to-production), [Webpack](https://webpack.js.org/guides/production/#specify-the-mode) e Ionic.


## Ambientes de execução

Foram padronizados os seguintes **ambientes de execução** (configurado via variável de ambiente `ENVIRONMENT_NAME`):

- **`production`** (alias: `prod`): Ambiente de produção.
  - Host: `<SIGLA_EMPRESA>.dezi.com.br`
  - Nome da configuração no `ci/gitlab-ci.common.yml`: `.only-production-remotes`
  - Variáveis de ambiente exportadas:
    - `ENVIRONMENT=production` 
    - `ENVIRONMENT_NAME=production`
    - `PROJECT_ENVIRONMENT=production`
- **`demo`**: Ambiente de demonstração.
  - Host: `demo.dezi.com.br`
  - Nome da configuração no `ci/gitlab-ci.common.yml`: `.only-demo-remotes`
  - Variáveis de ambiente exportadas:
    - `ENVIRONMENT=production` 
    - `ENVIRONMENT_NAME=demo`
    - `PROJECT_ENVIRONMENT=demo`
- **`staging-hotfix`**: Ambiente de homologação para hotfixes.
  - Host: `hotfix.dezi.com.br`
  - Nome da configuração no `ci/gitlab-ci.common.yml`: `.only-staging-hotfix-remotes`
  - Variáveis de ambiente exportadas:
    - `ENVIRONMENT=production` 
    - `ENVIRONMENT_NAME=staging-hotfix`
    - `PROJECT_ENVIRONMENT=staging-hotfix`
- **`staging`**: Ambiente de homologação padrão.
  - Host: `homo.dezi.com.br`
  - Nome da configuração no `ci/gitlab-ci.common.yml`: `.only-staging-remotes`
  - Variáveis de ambiente exportadas:
    - `ENVIRONMENT=production` 
    - `ENVIRONMENT_NAME=staging`
    - `PROJECT_ENVIRONMENT=staging`


## Projetos e Serviços

![Arquitetura geral do DEZI](/docs/img/dezi-arquitetura-servicos.png)

A arquitetura do DEZI é composta por três serviços principais:

- **Projeto [avd/web](http://git.fasolti.com.br/avd/web)**
    - API REST utilizada para servir os aplicativos web e mobile.
    - **Nome do serviço Docker-Compose**: `rest`
    - **Nome da Imagem do Docker**: `docker.fasolti.com.br/dezi-services`
    - **Dependências**:
        - `queue` (RabbitMQ)
        - `db`
    - **Endpoint Web**:
        - Produção: `https://EMPRESA.dezi.com.br/rest`
        - Homologação: https://homo.dezi.com.br/rest
        - Desenvolvimento local: http://localhost:8080
- **Projeto [avd/webapp](http://git.fasolti.com.br/avd/webapp)**
    - O aplicativo web (i.e. webapp) do DEZI.
    - **Nome do serviço Docker-Compose**: `web`
    - **Nome da Imagem do Docker**: `docker.fasolti.com.br/dezi-web`
    - **Dependências**: nenhuma (mas é necessário que o `rest` esteja em execução, para que a aplicação funcione).
    - **Endpoint Web**:
        - Produção: `https://EMPRESA.dezi.com.br`
        - Homologação: https://homo.dezi.com.br
        - Desenvolvimento local: http://localhost:3000
- **Projeto [avd/queue](http://git.fasolti.com.br/avd/queue)**
    - Aplicação Spring Boot para execução de tarefas em fila (e.g. envio de e-mail, geração de relatório).
    - **Nome do serviço Docker-Compose**: `queue`
    - **Nome da Imagem do Docker**: `docker.fasolti.com.br/dezi-queue`
    - **Dependências**:
        - `queue`
        - `db`
    - Endpoint Web: nenhum (não é um projeto web)
- **Projeto [avd/bi](http://git.fasolti.com.br/avd/bi)**
    - Servidor de Aplicação Pentaho, para prover as funcionalidades do BI e relatórios.
    - **Nome do serviço Docker-Compose**: `bi` (ou `biserver`)
    - **Nome da Imagem do Docker**: (no momento, não possui)
      > **FIXME**: Criar imagens do Docker via CI para o BI.
    - **Dependências**:
        - `db`
    - **Endpoint Web**: nenhum (não é um projeto web)

Outros serviços adicionais são necessários, mas não são projetos da Fasolti:

- **Serviço de Proxy `proxy`: [HAProxy](http://www.haproxy.org/) ([imagem própria](http://git.fasolti.com.br/docker/images/tree/master/haproxy), baseada no [phusion/baseimage](https://hub.docker.com/r/phusion/baseimage/))**
    - Proxy reverso para as aplicações e serviços do DEZI.
    - **Nome do serviço Docker-Compose**: `proxy`
    - **Nome da Imagem do Docker**: `docker.fasolti.com.br/haproxy`
    - **Dependências**:
        - `rest`
        - `web`
        - `bi`
- **Serviço de Banco de Dados `db`: [PostgreSQL](https://www.postgresql.org/) ([imagem própria](http://git.fasolti.com.br/docker/images/tree/master/pgsql), baseada no [phusion/baseimage](https://hub.docker.com/r/phusion/baseimage/))**
    - Proxy reverso para as aplicações e serviços do DEZI.
    - **Nome do serviço Docker-Compose**: `proxy`
    - **Nome da Imagem do Docker**: `docker.fasolti.com.br/pgsql`
    - **Dependências**:
        - `rest`
        - `web`
        - `bi`
- **Serviço de Mensageria `mq`: [RabbitMQ](https://www.rabbitmq.com/) ([imagem própria](http://git.fasolti.com.br/docker/images/tree/master/rabbitmq), baseada na [imagem oficial](https://hub.docker.com/_/rabbitmq))**
    - Servidor AMQP, para mensageria, fila e agendamento de mensagens.
    - **Nome do serviço Docker-Compose**: `mq`
    - **Nome da Imagem do Docker**: `docker.fasolti.com.br/rabbitmq`
    - **Dependências**: nenhuma


## Gerenciamento dos projetos

Todos os projetos do DEZI são gerenciados de forma similar. O que segue é um breve resumo de como o gerenciamento dos projetos é realizado, e como a metodologia a seguir possibilita a configuração do CI/CD para os projetos (i.e. implantação automática, construção de artefatos e imagens Docker).

### Fases de projeto

Uma **fase de projeto**, no contexto do deploy (CI/CD), representa o nível de maturidade e qualidade do código. A fase de um projeto está diretamente associada à sua versão atual. Com base em fases pré-estipuladas, é possível determinar como o CI será configurado, e.g. quais serão os _triggers_ utilizados, como as imagens Docker geradas serão rotuladas, etc.

No momento, o DEZI possui duas fases de projeto:

- **`staging`** (`unstable`):
  - Representa um estado **instável**.
  - Projeto/código não testado, ou parcialmente testado.
  - Projeto não está pronto para ser entregue ao cliente (i.e. não é permitido realizar deploy para ambientes de produção).
- **`prerelease`**:
  - Representa um estado **parcialmente estável**.
  - Projeto/código parcialmente homologado, ou para o qual é necessário feedback do cliente.
  - Hotfixes de código estável (i.e. correções no `master`; deploy automatizado implementado como sub-fase `prerelease-hotfix` no Gitlab CI, mas a fase ainda é nomeada `prerelease`).
  - Projeto não está pronto para ser entregue ao cliente (i.e. não é permitido realizar deploy para ambientes de produção), mas pode-se possivelmente apresentá-lo ao cliente em ambiente de pré-produção (e.g. `demo`).
- **`production`** (`stable`):
  - Representa um estado **estável**.
  - Projeto/código já foi testado e homologado.
  - Projeto está pronto para ser entregue ao cliente, em ambiente de produção ou similares.

> FIXME Renomear `staging` para `unstable`.

> FIXME Renomear `production` para `stable`.


### Estágios e tarefas configuradas no Gitlab CI/CD

![Arquitetura do CI/CD](/docs/img/dezi-arquitetura-ci.png)


### Branches, tags e suas associações com as fases de projeto

Há um mapeamento direto entre _branch_ e/ou _tag_ e uma **única** fase associada; este mapeamento é realizado via configuração do Gitlab CI/CD.

Todos os arquivos `.gitlab-ci.yml` dos projetos DEZI incluem o arquivo [`ci/gitlab-ci.common.yml`](/ci/gitlab-ci.common.yml) do projeto [`avd/deploy`](/). Nele são estipulados os estágios e os _jobs_ para os _pipelines_ do DEZI (vide [documentação do Gitlab CI](https://docs.gitlab.com/ee/ci/introduction/#how-gitlab-cicd-works) para maiores informações sobre os conceitos citados).

Segue as configurações de _pipeline_ por fase de projeto:

- **`staging`** (`unstable`):
  - Nome da configuração no `ci/gitlab-ci.common.yml`: `.only-staging-phases`
  - Representa um estado **instável**.
  - Projeto/código não testado, ou parcialmente testado.
  - Projeto não está pronto para ser entregue ao cliente (i.e. não é permitido realizar deploy para ambientes de produção).
- **`prerelease`**:
  - Nome da configuração no `ci/gitlab-ci.common.yml`: `.only-prerelease-phases`
  - Representa um estado **parcialmente estável**.
  - Projeto/código parcialmente homologado, ou para o qual é necessário feedback do cliente.
  - Hotfixes de código estável (i.e. correções no `master`; deploy automatizado implementado como sub-fase `prerelease-hotfix` no Gitlab CI, mas a fase ainda é nomeada `prerelease`).
  - Projeto não está pronto para ser entregue ao cliente (i.e. não é permitido realizar deploy para ambientes de produção), mas pode-se possivelmente apresentá-lo ao cliente em ambiente de pré-produção (e.g. `demo`).
- **`production`** (`stable`):
  - Nome da configuração no `ci/gitlab-ci.common.yml`: `.only-production-phases`
  - Representa um estado **estável**.
  - Projeto/código já foi testado e homologado.
  - Projeto está pronto para ser entregue ao cliente, em ambiente de produção ou similares.


### Nomenclatura das imagens Docker

As imagens docker terão o formato a seguir:

- Repositórios: `docker.fasolti.com.br/dezi-${NOME}`
- Tags: `${FASE}-${CI_COMMIT_SHORT_SHA}` e `${FASE}-${CI_COMMIT_REF_NAME}` (se em fase `production`)

Onde `NOME` será:
- `services` para o projeto `avd/web`
- `web` para o projeto `avd/webapp`
- `queue` para o projeto `avd/queue`

`CI_COMMIT_REF_NAME`, neste caso, será sempre o nome da _tag_ (e nunca um _branch_), que por sua vez será igual à versão do projeto. `CI_COMMIT_SHORT_SHA` é o formato curto do hash de _commit_.

Todas as imagens Docker geradas possuem as seguintes variáveis de ambiente pré-configuradas e fixas (i.e. atribuídas em tempo de compilação):

- **`DEZI_CI_PIPELINE_ID`**: O ID do pipeline (i.e. `CI_PIPELINE_ID`)
- **`DEZI_CI_PIPELINE_IID`**: O ID único/global do pipeline (i.e. `CI_PIPELINE_IID`)
- **`DEZI_CI_USER_LOGIN`**: O _username__ do usuário que disparou o CI (direta ou indiretamente) (i.e. `GITLAB_USER_LOGIN`)
- **`DEZI_CI_PIPELINE_SOURCE`**: O modo de disparo do pipeline (i.e. `CI_PIPELINE_SOURCE`)
- **`DEZI_PROJECT_GROUP`**: O nome lógico do grupo; sempre será igual a `dezi`
- **`DEZI_PROJECT_NAME`**: O nome lógico do projeto (usado para nomear a imagem Docker)
- **`DEZI_PROJECT_PHASE`**: A fase atual do projeto (usado para nomear as tags da imagem Docker)
- **`DEZI_GIT_NAMESPACE`**: O nome do grupo no Gitlab (i.e. `CI_PROJECT_NAMESPACE`)
- **`DEZI_GIT_NAME`**: O nome do repositório do projeto no Gitlab (i.e. `CI_PROJECT_NAME`)
- **`DEZI_GIT_REF_TYPE`**: O tipo de referência do Git usada pelo pipeline; pode ser `tag` (se `CI_COMMIT_TAG` não for vazio) ou `branch`.
- **`DEZI_GIT_REF_NAME`**: O nome da referência do Git usada pelo pipeline; pode referenciar tanto um _branch_  quanto um _tag_, a depender de como o _pipeline_ foi disparado.
- **`DEZI_GIT_COMMIT_SHA`**: O _hash_ do _commit_ do Git (i.e. `CI_COMMIT_SHA`)
- **`DEZI_GIT_COMMIT_SHORT_SHA`**: O _hash_ do _commit_ do Git em formato curto (i.e. `CI_COMMIT_SHORT_SHA`)

Os seguintes _labels_ também são criados para as imagens:

- **`fasolti.ci`**: `true` se gerado pelo Gitlab CI, `false` se gerado em outro ambiente (i.e. localmente)
- **`fasolti.ci.id`**: Igual a `DEZI_CI_PIPELINE_IID` se gerado pelo Gitlab CI, vazio se gerado em outro ambiente (i.e. localmente)
- **`fasolti.ci.iid`**: Igual a `DEZI_CI_PIPELINE_IID` se gerado pelo Gitlab CI, vazio se gerado em outro ambiente (i.e. localmente)
- **`fasolti.ci.user`**: Igual a `DEZI_CI_PIPELINE_SOURCE` se gerado pelo Gitlab CI, vazio se gerado em outro ambiente (i.e. localmente)
- **`fasolti.ci.source`**: Igual a `true` se gerado pelo Gitlab CI, vazio se gerado em outro ambiente (i.e. localmente)
- **`fasolti.project.group`**: Igual a `DEZI_PROJECT_GROUP`
- **`fasolti.project.name`**: Igual a `DEZI_PROJECT_NAME`
- **`fasolti.project.phase`**: Igual a `DEZI_PROJECT_PHASE`
- **`fasolti.project.git.group`**: Igual a `DEZI_GIT_NAMESPACE`
- **`fasolti.project.git.name`**: Igual a `DEZI_GIT_NAME`
- **`fasolti.project.git.ref-type`**: Igual a `DEZI_GIT_REF_TYPE`
- **`fasolti.project.git.ref`**: Igual a `DEZI_GIT_REF_NAME`
- **`fasolti.project.git.commit`**: Igual a `DEZI_GIT_COMMIT_SHA`
- **`fasolti.project.git.commit-short`**: Igual a `DEZI_GIT_COMMIT_SHORT_SHA`


### Ambientes de execução e suas associações com as fases de projeto

- **`production`**:
  - Deploy exclusivamente **manual**
  - Recebe apenas imagens em fase `production`
- **`demo`**:
  - Deploy exclusivamente **manual**
  - Recebe imagens em fase `production` e `prerelease`
- **`staging-hotfix`**:
  - Deploy **automático** para a fase `prerelease-hotfix` (mas nome real da fase ainda é `prerelease`)
  - Opcionalmente, recebe imagens em qualquer outra fase via deploy manual
- **`staging`**:
  - Deploy **automático** para a fase `staging`
  - Opcionalmente, recebe imagens em qualquer outra fase via deploy manual


# Ambiente de desenvolvimento local

O projeto `avd/deploy` foi criado para automatizar a implantação dos ambientes de execução planejados para o DEZI.

# Estrutura do projeto Deploy
TODO.
## Sobre o `compose.sh`
TODO Falar sobre o .compose.conf e variáveis de ambiente atribuídas pelo script.
TODO Falar sobre a pasta scripts.
### Comandos do `compose.sh`
TODO
## Instanciando arquivos e pastas `.sample`
TODO Falar sobre comparações de data de criação/modificação.
### Pastas
TODO Falar sobre composição de arquivos.
## Configurações-base do CI
TODO
## Pasta de Configurações
TODO
## Pasta de Servidores (projetos Compose)
TODO Falar sobre os arquivos .env e padrão de nomes dos arquivos `docker-compose.yml`.
## Pasta do Servidor (Projeto Compose) Principal (dezi)
TODO
### Sobre _dump_, _restore_ e a pasta `db-scripts`
TODO
