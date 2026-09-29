[![pipeline status](http://git.fasolti.com.br/avd/web/badges/development/pipeline.svg)](http://git.fasolti.com.br/avd/web/commits/development)

# DEZI

O Dezi é um ambiente online para realizar avaliações de desempenho por competências, 
pequisas de clime e satisfação entre outras. Flexibilidade para 
aderir a qualquer metodologia. Avaliações 360º, 180º e 90º. Interface super 
moderna, escalas de graduação, detalhamento de competências com atribuição de pesos e muito mais.

## Getting Started

- BackEnd.

### Prerequisites

- JAVA etc.

### Installing

No IntelliJ:

- Edit configurations...

```sh
Name: Start
working directory: (Pasta root do projeto)
Command line: test
Profiles: debug.jetty
```
- Usando Profile de produção:
`debug.jetty build.prod`

## Running the tests

- Não possui testes.

## Deployment

## Versioning

* We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).

#### standard-version
* Sera usado standard-version para a criaçao do changelog:
  *  Instalar standard-version global(Recomendado):  `npm i -g standard-version`
  *  Teste: `standard-version --dry-run`
  *  Criando o changelog e tag (atualizando o minor version): `standard-version`

## Built With
### Spring Repository

http://docs.spring.io/spring-data/commons/docs/current/reference/html/

http://docs.spring.io/spring-data/data-commons/docs/1.12.3.RELEASE/reference/html/

http://projects.spring.io/spring-data/

### Spring com QueryDsl

http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/

https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/

http://www.querydsl.com/

### Dozer

http://dozer.sourceforge.net/documentation/gettingstarted.html

> Os dados vindos do front-end são transformado em TransferObject para depois, ao persistir, transformado em entity.
> Isso é para manter livre o object que representa a tela, do object o que representa entidade. O Object transferObject por ter qualquer atributo que a entidade não tenha, sem prejudicar a persistência.

### Integração com Spring

http://dozer.sourceforge.net/documentation/springintegration.html

### Migração de Banco de dados com Flyway

* https://flywaydb.org/

```sh
mvn flyway:migrate -Dflyway.url="jdbc:postgresql://localhost:5432/db" -Dflyway.user="user" -Dflyway.password="pass"
```
