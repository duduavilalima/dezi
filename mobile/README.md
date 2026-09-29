# AVD - Mobile App

Projeto Ionic v3 do App iOS/Android para preenchimento de avaliações e apontamentos.

# Índice

<!-- toc -->

- [Notas importantes aos desenvolvedores](#notas-importantes-aos-desenvolvedores)
- [Ambientes de execução do aplicativo](#ambientes-de-execucao-do-aplicativo)
- [Preparando o ambiente de desenvolvimento](#preparando-o-ambiente-de-desenvolvimento)
  * [Preparando o seu workspace](#preparando-o-seu-workspace)
  * [Executando o projeto em modo web (para desenvolvimento local)](#executando-o-projeto-em-modo-web-para-desenvolvimento-local)
  * [Emulando o projeto no Android Emulator (para desenvolvimento local)](#emulando-o-projeto-no-android-emulator-para-desenvolvimento-local)
  * [Executando em ambientes de execução específicos](#executando-em-ambientes-de-execucao-especificos)
- [Como construir o projeto e gerar os aplicativos](#como-construir-o-projeto-e-gerar-os-aplicativos)
  * [Pasta WWW](#pasta-www)
  * [Aplicativos (iOS e Android)](#aplicativos-ios-e-android)
    + [Construindo localmente](#construindo-localmente)
    + [Construindo em um container Docker](#construindo-em-um-container-docker)
    + [Ambientes-padrão para construção dos aplicativos móveis](#ambientes-padrao-para-construcao-dos-aplicativos-moveis)
    + [Assinatura dos aplicativos móveis](#assinatura-dos-aplicativos-moveis)
      - [Assinatura dos APKs](#assinatura-dos-apks)
  * [Aplicativo PWA](#aplicativo-pwa)
    + [Sobre o host do app PWA](#sobre-o-host-do-app-pwa)
    + [Construindo em um container Docker](#construindo-em-um-container-docker-1)
    + [Executando o PWA localmente](#executando-o-pwa-localmente)
- [Como lançar novas versões](#como-lancar-novas-versoes)
  * [Criando um release](#criando-um-release)
  * [Fazendo a publicação do projeto](#fazendo-a-publicacao-do-projeto)

<!-- tocstop -->

# Notas importantes aos desenvolvedores

- **Siga as orientações do [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0-beta.4/#specification)**! Isto é de extrema importância, já que nossos changelogs são gerados com base nas mensagens de commit, e mensagens fora do padrão são ignoradas.
- Como exceção à recomendação anterior, o componente **major** da versão do app é reservado para mudanças estruturais na solução DEZI como um todo, e é aplicado em todos os projetos (ou seja: web, webapp, mobile e queue). Por favor, **não coloque `BREAKING CHANGE` nas suas mensagens de commit**!.

# Ambientes de execução do aplicativo

Por padrão, o aplicativo possui os seguintes ambientes de execução, cada um com sua configuração específica:

- **`development`**, branch `development`: Ambiente de execução em **desenvolvimento**. Não possui branch associado, necessariamente, mas segue o andamento do branch `development` na medida do possível.
- **`staging`**, branch `development`: O ambiente de execução para **homologação**. É nosso ambiente de testes padrão. Alterações são realizadas apenas mediante autorização e/ou intervenção da equipe de testes ou pelo PO. Diretamente associado ao branch `development`.
- **`staging-hotfix`**, branch `hotfix`: O ambiente de execução para **homologação para hotfixes**. É nosso ambiente de testes com deploy automatizado, exclusivo para testes de correções de problemas ocorrendo em produção. Alterações são realizadas apenas mediante autorização e/ou intervenção da equipe de testes ou pelo PO. Associado ao branch `hotfix`.
- **`demo`**: O ambiente de execução para **demonstrações** do Dezi à atuais/futuros clientes. Este ambiente só é iniciado mediante solicitação da equipe de especificação/vendas.
- **`production`**: O ambiente de execução em **produção**.

As configurações básicas para o build dos APKs referentes a cada ambientes de execução (e.g. parametrização do build por ambiente, modo de build, debug vs release) estão no arquivo [`config/project-info.js`](config/project-info.js), em `ENV_CONFIG`. Altere-o caso necessário.

Já as configurações dos ambientes de execução propriamente ditos (e.g. _endpoint_ da API de backend do Mobile) estão em [`src/environments/environment.base.ts`](src/environments/environment.base.ts) (variável `environments`). Caso algum hostname, porta ou configuração similar tenha sido alterada, este é o arquivo a corrigir.

# Preparando o ambiente de desenvolvimento

## Preparando o seu workspace

Este projeto utiliza o `npm`:

```sh
git clone http://git.fasolti.com.br/avd/mobile
cd mobile
npm install
```

Se for necessário alterar a API do backend, faça o clone do projeto [`avd/web`](http://git.fasolti.com.br/avd/web) e rode o servidor localmente. Siga as instruções do README do projeto para preparar o seu ambiente.

## Executando o projeto em modo web (para desenvolvimento local)

Execute, em um terminal:

```sh
npm start
```

Em seguida, acesse a url `http://localhost:8100`.

Se for necessário acessar uma instância do [backend](http://git.fasolti.com.br/avd/web) rodando em uma máquina local de um dispositivo na rede (e.g. WIFI), deve-se informar o IP/host da sua máquina local. Do contrário, o aplicativo não terá como acessá-lo. Exemplo:

```sh
# Obter um IP da sua máquina local que esteja na mesma rede
# do dispositivo a ser usado nos testes.
# Por exemplo, se o seu dispositivo wi-fi for "wlp3s0",
# uma opção para obter um IP seria:
DEVICE="wlp3s0"
IP="$(ifconfig "$DEVICE" | grep inet | head -n 1 | tr -s '[:space:]' '\t' | cut -f 3)"

# Executar o projeto, indicando o IP/host a ser usado para acessar a API do avd/web (backend)
DEZI_MOBILE_API_HOST="$IP" npm start

# Então, o dispositivo poderá acessar a aplicação em "http://${IP}:8100"
xdg-open "http://${IP}:8100"
```

## Emulando o projeto no Android Emulator (para desenvolvimento local)

Antes de mais nada, certifique-se de que possui, no seu ambiente:

- JDK versão **8** (qualquer distribuição, e.g. AdoptOpenJDK);
- Gradle; e
- Android SDK com o Platform 28+ instalado.

Suas variáveis de ambiente também devem estar configuradas de acordo:

- `ANDROID_SDK_ROOT` (ou `ANDROID_HOME`) deve apontar para a pasta de instalação do seu Android SDK. _(Vide [documentação do Android SDK](https://developer.android.com/studio/command-line/variables.html#envar.) para maiores detalhes.)_
- `JAVA_HOME` deve apontar para a pasta de instalação do seu JDK.

Em seguida, execute, em um terminal:

```sh
npm run emulate <PLATAFORMA>

# Opcionalmente, para Android
# ( equivalente a `npm run emulate android`)
npm run emulate:android
```

Este script já está configurado para executar em modo _livereload_.

Se estiver executando para ambiente de desenvolvimento local, é importante configurar `DEZI_MOBILE_API_HOST` com o IP/hostname da sua máquina local.

## Executando em ambientes de execução específicos

Para forçar a execução do projeto em ambientes específicos, basta atribuir a variável de ambiente `DEZI_ENV` para o nome do ambiente no qual você deseja executar. Exemplos:

- **`development`**:
  - `npm start`
  - `DEZI_ENV=dev npm start`
  - `DEZI_ENV=development npm start`
- **`production`**:
  - `DEZI_ENV=prod npm start`
  - `DEZI_ENV=production npm start`
- **`staging`** (homologação padrão):
  - `DEZI_ENV=staging npm start`
- **`staging-hotfix`** (homologação hotfix):
  - `DEZI_ENV=staging-hotfix npm start`
- **`demo`**:
  - `DEZI_ENV=demo npm start`

Isto será útil para executar o app localmente, mas usando os endpoints da API de outros ambientes, ex. reproduzir bugs localmente acessando o ambiente Hotfix.


# Como construir o projeto e gerar os aplicativos

O DEZI Mobile é construído para cada um dos quatro ambientes de execução disponíveis. Pouco pode ser mudado em tempo de execução. Todos os recursos/arquivos pré-compilados são servidos de forma estática. _(No momento, o único recurso que é servido de forma diferente é o [`manifest.json`](src/manifest.json), que é customizado por ambiente e empresa. Vide [`server.js`](server.js) para maiores detalhes.)_

Alguns aspectos de execução do DEZI Mobile são parametrizados em tempo de compilação, a se basear pelo (em ordem de prioridade):

1. Nome do ambiente, fornecido através de `ENVIRONMENT_NAME`; 
2. Informações do ambiente de build:
   1. [Gitlab](https://docs.gitlab.com/ee/ci/variables/predefined_variables.html); ou
   2. [Ionic AppFlow](https://ionicframework.com/docs/appflow/automation/environments#predefined-environments).
3. Informações locais de commit via `git rev-parse`, `git symbolic-ref`, `git tag` e similares.

Se necessário, as seguintes variáveis de ambientes poderão opcionalmente ser atribuídas em tempo de compilação, para substituir os valores-padrão assumidos de acordo com o ambiente identificado:

- `DEZI_MOBILE_API_PROT`: Protocolo da API REST que o aplicativo consumirá.
- `DEZI_MOBILE_API_HOST`: Hostname da API REST que o aplicativo consumirá. Interessante para quando a publicação do aplicativo não é viável ou possível (ex. para testes em rede interna, com dispositivos da rede local; sugestão: `ip addr` ou comandos similares).
- `DEZI_MOBILE_API_PORT`: Porta da API REST que o aplicativo consumirá.
- `DEZI_MOBILE_API_CONTEXT`: Caminho (i.e. url.path) da API REST que o aplicativo consumirá.
- `DEZI_MOBILE_API_BASEURL`: URL-base completa da API REST. Se não for fornecida, uma URL-base será construída com base em `DEZI_MOBILE_API_PROT`, `DEZI_MOBILE_API_HOST`, `DEZI_MOBILE_API_PORT` e `DEZI_MOBILE_API_CONTEXT`.
- `DEZI_MOBILE_LOG_LEVEL`
- `DEZI_MOBILE_LOG_APPENDERS`


## Pasta WWW

Execute, em um terminal:

```sh
np run build:web
```

## Aplicativos (iOS e Android)

### Construindo localmente

Primeiramente, garanta que o seu ambiente tenha:

- JDK **8** (qualquer distribuição, e.g. AdoptOpenJDK);
- Gradle; e
- Android SDK com o Platform 27.

Execute, em um terminal, na pasta raíz do projeto:

```sh
np run build:app
```

Em seguida, será criado um app por ambiente e por plataforma (por enquanto, apenas Android). Todos os apps construídos estarão dentro de uma pasta com o seguinte padrão de nome:

`<BUILDS_DIR>/dezi-mobile-v<VERSÃO>-<AMBIENTE>.BUILD-<DATA_HORA_UTC>-<BRANCH>@<COMMIT>`

Onde:

- `BUILDS_DIR`: Pasta para onde os artefatos resultantes dos builds serão copiados. Esta pasta é **sempre** relativa ao raíz do projeto (i.e. onde está o `package.json`), e pode ser parametrizada pelas variáveis de ambiente `DEZI_MOBILE_BUILDS_DIR`, `DEZI_BUILDS_DIR` ou `BUILDS_DIR`, nesta mesma ordem de relevância. Por padrão, o diretório BUILDS_DIR fica em **`dist/apps`**.
- `VERSÃO`: Versão do app, assim como definida no `package.json`.
- `DATA_HORA_UTC`: Data e hora em que o build foi executado, em UTC.
- `BRANCH`: Seu branch atual do Git
- `COMMIT`: Seu _commit-id_ atual, versão _short_

Os nomes dos APKs terão a mesma nomenclatura, com a adição do **ambiente de execução**. Formato:

`dezi-mobile-v<VERSÃO>-<AMBIENTE>.BUILD-<DATA_HORA_UTC>-<BRANCH>@<COMMIT>[.unsigned].apk`

Por padrão, os APKs serão gerados apenas para alguns ambientes de execução padrão; vide seção "Ambientes-padrão para construção dos APKs" a seguir para maiores detalhes.

O sufixo `[.unsigned]` só deve ser usado quando não destinado à App Store (e.g. não-produção).

Se necessário, distribua a pasta inteira, ou copie/renomeie apenas os APKs necessários.

### Construindo em um container Docker

Opcionalmente, pode-se executar o seguinte script para compilar os aplicativos móveis (Android/iOS) em um container Docker:

```sh
./build/build-apps.sh
```

### Ambientes-padrão para construção dos aplicativos móveis

Por padrão, os APKs serão gerados apenas para os ambientes listados em `ENV_DEFAULTS`, em [`config/project-info.config.js`](config/project-info.config.js). Esta lista será desconsiderada caso o aplicativo esteja sendo executado em ambiente de CI (e.g. AppFlow, Gitlab CI).

Opcionalmente, pode-se declarar os ambientes de execução a usar na construção dos APKs, separados por vírgula, na variável `DEZI_MOBILE_ENV_DEFAULTS`. Estes valores sobrescreverão o comportamento padrão (i.e. a lista padrão em `ENV_DEFAULTS`).

Opcionalmente, pode-se definir `DEZI_MOBILE_ENV_DEFAULTS=none`, para inibir o comportamento padrão por completo, i.e. não construir nenhum ambiente por padrão. Nestes caso, o ambiente padrão a se construir será detectado pelo ambiente (e.g. informações do CI ou do Git).

Se nenhum ambiente padrão for detectado, o usuário deverá obrigatoriamente informar os ambientes a construir na chamada ao script de build, como a seguir:

```sh
# Para construção local
npm run build:app staging staging-hotfix

# Para construção em ambiente CI (i.e. `npm install` automático, recompilação do SASS, etc)
npm run ci:build:app staging staging-hotfix

# Para construção local (i.e. execução do `ci:build:app` em um container Docker)
./build/build-apps.sh staging staging-hotfix
```

### Assinatura dos aplicativos móveis

#### Assinatura dos APKs

O procedimento de _signing_ dos APKs é feito de forma automática, caso exista o keystore `build/dezi_mobike.jks`. O script de build espera as seguintes variáveis de ambiente:

- `DEZI_MOBILE_ANDROID_KEYSTORE_PASS` (ou `KEYSTORE_PASS`): A senha para acesso ao keystore. Se não possuir senha, este valor deve estar vazio.
- `DEZI_MOBILE_ANDROID_KEYSTORE_KEY` (ou `KEYSTORE_KEY`): O nome (_alias_) da chave a ser usada para assinar o APK. Se não for fornecida, ou for vazia, considera-se o valor padrão `dezi`.
- `DEZI_MOBILE_ANDROID_KEYSTORE_KEYPASS` (ou `KEYSTORE_KEYPASS`): A senha da chave privada. Se não possuir senha, este valor deve estar vazio.

Por padrão, serão construídos os seguintes tipos de APK:

- **Assinado, final `.signed.apk`**: APK assinado, com `zipalign 4`. Deve ser o APK padrão a se distribuir.
- **Assinado, final `.signed-align8.apk`**: APK assinado, com `zipalign 8`. Pode ser necessário usar este APK em alguns aparelhos 64-bit, caso a instalação do `.signed.apk` falhe.
- **Não-assinado, final `.unsigned.apk`**: APK não assinado. A ser usado como _fallback_ caso a instalação dos demais falhe, ou para assinar o APK com outra chave.

O nosso Gitlab CI já está configurado para criar APKs assinados.


## Aplicativo PWA


O Dezi Mobile também pode, opcionalmente, ser compilado para um PWA. Basta executar `npm run build:pwa`, ou atribuir a variável de ambiente `DEZI_MOBILE_BUILD_TYPE=pwa` ao executar o comando de execução (ex. `npm start`) ou de _build_. Exemplo:

```sh
DEZI_MOBILE_BUILD_TYPE=pwa npm start
DEZI_MOBILE_BUILD_TYPE=pwa npm run build

# Mesmo efeito
npm run start:pwa
npm run build:pwa
```

Neste modo de compilação, considera-se que a aplicação será executada em um host próprio para uma determinada empresa, de forma similar ao que acontece com o projeto [avd/web](http://git.fasolti.com.br/avd/web). Com isso, a aplicação fará um processo de login que obtém a empresa a partir do host pelo qual ela está sendo servida.

Adicionalmente, o serviço `EnvStore` poderá ser utilizado para identificar se a aplicação foi compilada ou está sendo executada em modo PWA:

```ts
const ENV = await this.envStore.get();
if (ENV.buildType === 'pwa') {
	// Do stuff...
}
```

### Sobre o host do app PWA

O host de uma determinada empresa em produção é informado na entidade `Empresa`, campo `host`. É este o host verificado pelo projeto [avd/webapp](http://git.fasolti.com.br/avd/webapp). Para o projeto Mobile, haverá um sufixo `mobile` no último subdomínio em nível. Ou seja, dada a empresa Fasolti, que possui host `fasolti.dezi.com.br`, espera-se que o app Mobile correspondente seja servido em `fasoltimobile.dezi.com.br`.

### Construindo em um container Docker

Para compilar a imagem Docker do PWA, é necessário dois passos:

```sh
# Executar o build do PWA.
# - Um arquivo build/target/dezi-mobile.tgz será gerado,
#   a ser usado na etapa seguinte.
# - A pasta "www" no raíz do projeto conterá o projeto compilado,
#   se for necessário utilizar em algum outro contexto/lugar.
./build/build.sh

# Executar o build da imagem docker.
./build/image.sh
```

Estes são exatamente os passos executados pelo CI.

### Executando o PWA localmente

Pode-se executar o PWA sem compilá-lo para uma imagem Docker com o script `serve`. Esta é a melhor forma de efetuar alterações em código e testá-las em um ambiente mais próximo do de execução de fato (ex. Produção), se necessário. 

Note que é necessário construir o projeto antes de servi-lo:

```sh
# Este comando aceita todas as variáveis de ambiente discutidas anteriormente,
# para customizar o build.
npm run build:pwa
```

Recompile o código sempre que necessário. Em seguida, execute:

```sh
npm run serve

# Opcionalmente, para definir a porta (3000 é o padrão)
PORT=3000 npm run serve
```

A aplicação poderá então ser então acessada em `http://localhost:3000`.

Note que este modo de execução não é o ideal para desenvolvimento. Use-o em casos extremos, ou para tentar simular erros em produção.


# Como lançar novas versões

## Criando um release

Este projeto utiliza o `standard-version`. Em caso de dúvidas, siga a [documentação da ferramenta](https://www.npmjs.com/package/standard-version).

Execute:

```sh
npm run release

# Opcionalmente, e QUANDO NECESSÁRIO, para determinar versões major
# (e.g. breaking changes, novas sprints)
npm run release:major

# Opcionalmente, e QUANDO NECESSÁRIO, para determinar versões minor
# (e.g. novas funcionalidades, novas sprints)
npm run release:minor

# Opcionalmente, e QUANDO NECESSÁRIO, para determinar versões patch
# (e.g. correções de bugs)
npm run release:minor

# Opcionalmente, e QUANDO NECESSÁRIO, para passar opções adicionais
npm run release -- --prerelease
```

Em seguida, um novo commit `chore(release)` será adicionado, com alterações no `CHANGELOG.md` e bump de versão no `package.json`. Revise o CHANGELOG ou altere qualquer outro aspecto do projeto se necessário, e faça um novo commit `chore(release)`.

## Fazendo a publicação do projeto

Depois de realizado o release (i.e. _version bump_), execute:

```sh
npm run publish
```

Para publicar o pacote para o NPM (Nexus), se necessário, execute:

```sh
npm run publish:npm
```
