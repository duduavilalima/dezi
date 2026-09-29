# DEZI-IMPORT (0.11.0-SNAPSHOT)

Projeto que importa dados de planilhas xlsx.

## Instalação

Apenas execute o IDE STS Spring.

## Deployment

```sh
mvn clean package
```

```sh
java -jar nome.jar
```

obs:
* Os arquivos xlsx deve esta no raiz do jar.
* Ao criar o jar, existirá um arquivo chamado `application-config.properties` com as configuraçãos de banco, empresa e nomes das planilhas.

### Application-config.properties

A estrutura do arquivo segue abaixo:

* spring.datasource.url - Endereço do banco;
    * Ex.: "jdbc:postgresql://localhost:5432/deziTest".
* spring.datasource.username - usuário do banco de dados;
* spring.datasource.password - senha do banco de dados;
* dezi.empresa.name - Nome da empresa a ser importado os dados;
* dezi.createdby.name- Nome de que irá importa os dados;
* default.dimensao.nome - Nome de uma dimensão (Obrigatório);
* default.dimensao.id - Id no banco de dados de uma dimensão (FIX: corrigir e pegar somente pelo nome);
* funcionario.import - Nome da Planilha de importação de Funcionários;
* comp.import - Nome da Planilha de importação de Competência;
* associacoes.import - Nome da Planilha de importação de Associações;

## Versionamento

Usamos [SemVer](http://semver.org/) para controle de versão. Para as versões disponíveis, veja as [tags neste repositório](http://git.fasolti.com.br/denis.oliveira/deziQueue/tags).

## Gerando o CHANGELOG do projeto
```sh
npm run release -- --release-as 0.9.2-SNAPSHOT  --dry-run

npm run release -- --release-as patch --dry-run

npm run release -- --release-as minor  --dry-run

npm run release -- --release-as major  --dry-run
```

- --dry-run roda teste.

## Teste e Outros

Abaixo, sql para teste de importação de funcinário:

```sql
SELECT f.matricula,f.nome,f.email_funcional,
CASE
    WHEN f.id_tipo_sexo = 1 THEN 'M'
    ELSE 'F'
END AS Tipo_Sexo
,f.nascimento,f.admissao,
CASE
    WHEN f.id_situacao_funcional = 1 THEN 'EM_ATIVIDADE'
    WHEN f.id_situacao_funcional = 2 THEN 'FERIAS'
    WHEN f.id_situacao_funcional = 3 THEN 'LICENCA_MATERNIDADE'
    WHEN f.id_situacao_funcional = 4 THEN 'LICENCA_SEM_REMUNERACAO'
    WHEN f.id_situacao_funcional = 5 THEN 'DISPENSA_MEDICA'
    WHEN f.id_situacao_funcional = 6 THEN 'SERVICO_MILITAR_OBRIGATORIO'
    WHEN f.id_situacao_funcional = 7 THEN 'LICENCA_PATERINDADE'
    WHEN f.id_situacao_funcional = 8 THEN 'LUTO'
    WHEN f.id_situacao_funcional = 9 THEN 'NUPCIAS'
    WHEN f.id_situacao_funcional = 10 THEN 'LICENCA_ADOCAO'
    WHEN f.id_situacao_funcional = 11 THEN 'DESLIGADO'
    WHEN f.id_situacao_funcional = 12 THEN 'RECESSO'
END AS situacao_funcional,
f.data_inicio_recesso,f.data_fim_recesso,
CASE
    WHEN f.id_tipo_perfil_uso = 3 THEN 'Administrador'
    WHEN f.id_tipo_perfil_uso = 4 THEN 'Funcionario'
    WHEN f.id_tipo_perfil_uso = 14 THEN 'Consultor de RH'
    WHEN f.id_tipo_perfil_uso = 15 THEN 'Gestor de RH'
    WHEN f.id_tipo_perfil_uso = 16 THEN 'Gestor de Área'
END AS perfil_user,
u.nome,d.nome,c.nome,f.cargo,
CASE
    WHEN ud.id_gestor IS NULL THEN ''
    ELSE 'Sim'
END AS gestor,
CASE
    WHEN f.preenche_formacao_e_idiomas = false THEN 'Não'
    WHEN f.preenche_formacao_e_idiomas = true THEN 'Sim'
    ELSE ''
END AS Formacao_idioma
FROM funcionario AS f INNER JOIN unidade u ON f.id_unidade = u.id
INNER JOIN departamento d ON f.id_departamento = d.id
INNER JOIN cargo c ON f.id_cargo = c.id
LEFT JOIN unidade_departamento ud ON f.id = ud.id_gestor
WHERE f.id_empresa = ID_DA_EMPRESA;
```

Somente pode haver um gestor por departamento (setor), teste abaixo retorna caso exita unidade e departamento duplicado:

```sql
SELECT id_unidade,id_departamento, Count(*) FROM unidade_departamento
GROUP BY id_unidade,id_departamento
HAVING Count(*) > 1
```
