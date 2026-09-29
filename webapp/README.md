[![pipeline status](http://git.fasolti.com.br/avd/webapp/badges/development/pipeline.svg)](http://git.fasolti.com.br/avd/webapp/commits/development)

# DEZI

O Dezi é um ambiente online para realizar avaliações de desempenho por competências, 
pequisas de clime e satisfação entre outras. Flexibilidade para 
aderir a qualquer metodologia. Avaliações 360º, 180º e 90º. Interface super 
moderna, escalas de graduação, detalhamento de competências com atribuição de pesos e muito mais.

## Getting Started

- FrontEnd.

### Prerequisites
- Instalar yarn ou node;
	- https://nodejs.org/en/download/
	- https://yarnpkg.com/lang/en/docs/install/#debian-stable


### Installing

- Executar `./build` na pasta build;
- Executar `yarn` ou `npm install` na pasta source;
- Executar `yarn` ou `npm install` na root do projeto;


## Running the tests

- Não possui testes.


## Deployment
```sh
yarn start
```
or
```sh
npm start
```
## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).

## Built With

[AngularJS 1.5.11](https://angularjs.org/).
#### standard-version
* Sera usado standard-version para a criaçao do changelog:
  *  Instalar standard-version global(Recomendado):  `npm i -g standard-version`
  *  Teste: `standard-version --dry-run`
  *  Criando o changelog e tag (atualizando o minor version): `standard-version`
