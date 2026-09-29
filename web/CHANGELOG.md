# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [3.17.1](http://git.fasolti.com.br/avd/web/compare/v3.17.0...v3.17.1) (2021-09-27)


### Bug Fixes

* **Competência / Detalhamento:** correção ao inserir e salvar um novo detalhamento. ([cdf60e0](http://git.fasolti.com.br/avd/web/commit/cdf60e06a4dc8663794f2699f14ef4595d3da9cc))
* **Competência / Detalhamento:** correção ao salvar o detalhamento, quando ao associar uma nova escala de graduação ([03a1e3b](http://git.fasolti.com.br/avd/web/commit/03a1e3b7b4748b70bae248e45e5d1918d6916ab1))
* **Competência / Detalhamento:** inserir um novo detalhamento. ([8724f00](http://git.fasolti.com.br/avd/web/commit/8724f00a714239826b07ab9b1b12edef90e83c44))
* **Competência / Detalhamento:** testes para a correção do erro ao inserir um novo detalhamento. ([1551e3a](http://git.fasolti.com.br/avd/web/commit/1551e3ab5cd18358b9d9ce9653a77a56d5124494))

## [3.17.0](http://git.fasolti.com.br/avd/web/compare/v3.16.5...v3.17.0) (2021-09-16)


### Features

* **Administraçãoa - Mensagem:** permitir o envio  da mensagem de boas vindas por perfil ou por um colaborador específico, antes somente era possível por perfil. ([c367603](http://git.fasolti.com.br/avd/web/commit/c367603ff5ac64c5c0f62912baa3734ceb48d84a))
* **PDI:** ajuste na lista de subordinados. Além de exibir os subordinados diretos, também foram incluídos os avaliados pelo gestor, mesmo que de outro setor. ([906133d](http://git.fasolti.com.br/avd/web/commit/906133d48901f0f6809f1111db6f2ed7c10e1c73))
* **PDI:** inclusão da edição do pdi, pelo gestor, de um subordinado de outro setor. ([9b71526](http://git.fasolti.com.br/avd/web/commit/9b715262a4615af670ba4d6adc36d83cc74e90b9))


### Bug Fixes

* **Cadastro - Colaboradores:** correção da exclusão/inclusão na tabela unidade_departamento, quando o usuário possui o perfil Gestor de Área ([0f94bae](http://git.fasolti.com.br/avd/web/commit/0f94bae4ae59170c1fa5d25dc537561a00912a7d))
* **Detalhamento:** correção ao salvar o detalhamento, impedindo a duplicidade das graduações associadas e a marcação do campo de obrigatoriedade do comentário. ([09c6f6e](http://git.fasolti.com.br/avd/web/commit/09c6f6e488b8331b51bf8647236fb8ae3ce83189))
* **Detalhamento:** outro ajuste para impedir a inclusão em duplicidade na tabela "compet_detalhe_calibrahint" ([dc4e5d2](http://git.fasolti.com.br/avd/web/commit/dc4e5d2f69b7962d08b57e9a2ac8ef7d62bd1e45))
* **Funcionários - Service:** ajuste para que o resultado da pesquisa de funcionários retorne ordenada pelo nome dos funcionários. ([b8e6df3](http://git.fasolti.com.br/avd/web/commit/b8e6df371467f5322787009182dbcd7aa78a41e0))

### [3.16.5](http://git.fasolti.com.br/avd/web/compare/v3.16.4...v3.16.5) (2021-04-16)


### Bug Fixes

* **Avaliação:** correção do log da rotina de ajuste de pesos. ([f16333b](http://git.fasolti.com.br/avd/web/commit/f16333b0e20409c074738bebdac268a9a9b09fb6))

### [3.16.4](http://git.fasolti.com.br/avd/web/compare/v3.16.3...v3.16.4) (2021-03-30)

### [3.16.3](http://git.fasolti.com.br/avd/web/compare/v3.16.2...v3.16.3) (2021-03-16)


### Bug Fixes

* **Liberação:** correção na liberação sem autoavaliação ([a9a0262](http://git.fasolti.com.br/avd/web/commit/a9a02623d60f4b8371878718f472a505302bbba7))

### [3.16.2](http://git.fasolti.com.br/avd/web/compare/v3.16.1...v3.16.2) (2021-03-15)


### Bug Fixes

* **BI:** ajustes de login no pentaho ([a939994](http://git.fasolti.com.br/avd/web/commit/a939994baf4448254dabd463d857f4c1f00e0499))
* **BI:** ajustes na view base utilizada no ETL do BI ([69aef15](http://git.fasolti.com.br/avd/web/commit/69aef151587d627c1727dae1ab8b395388e2e29a))
* **BI:** issue [#126](http://git.fasolti.com.br/avd/web/issues/126) ([2c600a7](http://git.fasolti.com.br/avd/web/commit/2c600a7b1c845109f4e790538f2afa7ef474f998))

### [3.16.1](http://git.fasolti.com.br/avd/web/compare/v3.16.0...v3.16.1) (2021-03-15)

## [3.16.0](http://git.fasolti.com.br/avd/web/compare/v3.15.0...v3.16.0) (2021-02-24)


### Features

* **bi:** ajustes na classe de Graduação para grava o range de pontuação. ([85725c2](http://git.fasolti.com.br/avd/web/commit/85725c25e0ee283efe220d36c0ffa4dfb1efa848))
* **bi:** issue [#126](http://git.fasolti.com.br/avd/web/issues/126) ([ddae033](http://git.fasolti.com.br/avd/web/commit/ddae03349eb51b687f410ea3aaccf4801fa49459))
* **BI: migraçao para ETL:** criação da tabela de parâmetros. ([17664c9](http://git.fasolti.com.br/avd/web/commit/17664c9c28cd0335c3e9f78056e1027d43d014be))
* **parâmetros bi:** ajustes da parâmetros e remoção dos valores em hard code. ([babaaaa](http://git.fasolti.com.br/avd/web/commit/babaaaa0b5481b3e187094decff702161f03db9c))
* **Relatórios:** issue [#126](http://git.fasolti.com.br/avd/web/issues/126) ([bab5d50](http://git.fasolti.com.br/avd/web/commit/bab5d5086fdb98d6648b47a6ec68ed72fff8ce77))
* **Relatórios:** issue [#126](http://git.fasolti.com.br/avd/web/issues/126) ([ac8ad16](http://git.fasolti.com.br/avd/web/commit/ac8ad165f1c468885977ef1be0315672d2efad92))
* **Relatórios:** issue [#126](http://git.fasolti.com.br/avd/web/issues/126) - ajuste no login com o Pentaho. ([f07d9b8](http://git.fasolti.com.br/avd/web/commit/f07d9b817a4a88953da99020f9b7c01e79c89eca))


### Bug Fixes

* **avaliação:** ajustes na remoção de avaliadores ([1edde8c](http://git.fasolti.com.br/avd/web/commit/1edde8cd23a03087d5c29f837966285a7cfe2917))
* **bi:** ajustes de scripts de banco ([373537c](http://git.fasolti.com.br/avd/web/commit/373537c23cd53b8e16b4cf42e276270d7668373d))
* **bi:** ajustes de scripts de banco ([a89ba08](http://git.fasolti.com.br/avd/web/commit/a89ba08b542b2dae14921d4556e552fdebe0cddd))
* **bi:** issue [#126](http://git.fasolti.com.br/avd/web/issues/126) ([dd1899e](http://git.fasolti.com.br/avd/web/commit/dd1899e2a4b308d56da61b5558ca1c3ec6baeea3))
* **bi:** scripts para adequação ao BI. ([443149f](http://git.fasolti.com.br/avd/web/commit/443149f1235d534533732c622d45b70d697db9c8))
* **preenchimento:** ajuste temporário para corrigir o preenchimento da tabela aval_peso_cargo_comp ([701743b](http://git.fasolti.com.br/avd/web/commit/701743b9fd5656f43c26f7d18e63fd6b1c317a22))

## [3.15.0](http://git.fasolti.com.br/avd/web/compare/v3.14.0...v3.15.0) (2020-10-24)


### Features

* **migração aws:** desfazendo teste. ([98abc61](http://git.fasolti.com.br/avd/web/commit/98abc61e25f54aa2d174a735b26e504c266f20ed))

## [3.14.0](http://git.fasolti.com.br/avd/web/compare/v3.13.11...v3.14.0) (2020-10-24)


### Features

* **migração aws:** verificação do CI. ([c43ee0f](http://git.fasolti.com.br/avd/web/commit/c43ee0f2c4fc1d707e9e2fb95d08f2b40ee721af))

### [3.13.11](http://git.fasolti.com.br/avd/web/compare/v3.13.10...v3.13.11) (2020-10-01)


### Bug Fixes

* **gradução:** remoção da rotina de ordenação após o update. ([17bd1cb](http://git.fasolti.com.br/avd/web/commit/17bd1cb4b8f7ed370557ff3106ee9442a6102430))
* **mobile:** correção da gravação do preenchimento. O método graduacaoListMapper, na classe PreenchimentoMobileServiceImpl estava setando o valor incorreto para o id da gradução. ([527e81d](http://git.fasolti.com.br/avd/web/commit/527e81d067a63990076c4aa0cd9f89e20514e0b7))
* **mobile:** correção da gravação do preenchimento. O método graduacaoListMapper, na classe PreenchimentoMobileServiceImpl estava setando o valor incorreto para o id da gradução. ([cc74bee](http://git.fasolti.com.br/avd/web/commit/cc74beeba3a73323ed3e5b44385e3dbc3222daed))

### [3.13.10](http://git.fasolti.com.br/avd/web/compare/v3.13.9...v3.13.10) (2020-09-24)


### Bug Fixes

* **graduação:** correção da ordenação das graduações ao inativar, ativar ou incluir uma graduação. Esse ajuste também corrige o preenchimento no mobile, onde a opção "Não posso avaliar" era substituída "Não avaliado" ([d88f035](http://git.fasolti.com.br/avd/web/commit/d88f035db466e46907d0bd6ed31e0246502cc62f))

### [3.13.9](http://git.fasolti.com.br/avd/web/compare/v3.13.8...v3.13.9) (2020-09-22)


### Bug Fixes

* **avaliação:** ajuste na exibição na aba de avaliados, na edição de uma avaliação. Colobaradores já liberados apareciam em amarelo e não em cinza ([2f66663](http://git.fasolti.com.br/avd/web/commit/2f66663fb8e67174809ea0ea42074f67416295cd))
* **preenchimento:** issue [#322](http://git.fasolti.com.br/avd/web/issues/322) ([189f3de](http://git.fasolti.com.br/avd/web/commit/189f3deb930edd78a04d2226c8827e071337ecc3))

### [3.13.8](http://git.fasolti.com.br/avd/web/compare/v3.13.7...v3.13.8) (2020-09-10)


### Bug Fixes

* **graduação:** issue [#164](http://git.fasolti.com.br/avd/web/issues/164). Comentários do dia 08/09. ([ce4cf38](http://git.fasolti.com.br/avd/web/commit/ce4cf38cc6516e190d26e626185722f8cc36bfa3))

### [3.13.7](http://git.fasolti.com.br/avd/web/compare/v3.13.6...v3.13.7) (2020-09-08)


### Bug Fixes

* **acompanhamentos:** conlusão dos ajustes issues [#35](http://git.fasolti.com.br/avd/web/issues/35) e 175 ([9ddd055](http://git.fasolti.com.br/avd/web/commit/9ddd0557a79c74e30582b8d56185be343d73fc10))
* **acompanhamentos:** issue [#164](http://git.fasolti.com.br/avd/web/issues/164) ([f9df57f](http://git.fasolti.com.br/avd/web/commit/f9df57f96e0b544ae167f701f5dc52348746e2b3))
* **acompanhamentos:** issue [#175](http://git.fasolti.com.br/avd/web/issues/175) (comentários do dia 12/08). ([4bcd320](http://git.fasolti.com.br/avd/web/commit/4bcd320c164bac015899ee11470c9b4a6a433667))
* **acompanhamentos:** issue [#319](http://git.fasolti.com.br/avd/web/issues/319) ([fdfdcb0](http://git.fasolti.com.br/avd/web/commit/fdfdcb06c4e40ca2e59ea34d9fe37c329a8b594e))
* **ajustes diversos:** issues [#35](http://git.fasolti.com.br/avd/web/issues/35) e # 175 ([f288065](http://git.fasolti.com.br/avd/web/commit/f288065ba6841dc757e5fbf5140e0f995659f171))
* **avaliação:** ajuste na funcionaliadade "Validar Avaliados" da avaliação. Correção da duplicação de entradas na tabela avaliacao_participante. ([5403436](http://git.fasolti.com.br/avd/web/commit/5403436324ced9044222577c3740224807e7bb06))
* **exibicao autoavaliacao - acompanhamentos:** Ajusta a exibição de percentual de autoavaliacao no acompanhamento ([#175](http://git.fasolti.com.br/avd/web/issues/175)) ([182ff38](http://git.fasolti.com.br/avd/web/commit/182ff3887e30955e40e45b36dd82ff5789067534))
* **exibicao autoavaliacao - acompanhamentos:** Ajusta a exibição de percentual de autoavaliacao no acompanhamento ([#175](http://git.fasolti.com.br/avd/web/issues/175)) ([f431863](http://git.fasolti.com.br/avd/web/commit/f4318630f4b996296cdbbf3f634d2291714e83bf))
* **graduação:** issue [#164](http://git.fasolti.com.br/avd/web/issues/164) ([6d23893](http://git.fasolti.com.br/avd/web/commit/6d238937ae94892b68100d7e5cc3ba15ad13bf7d))
* **graduação:** setar a ordem da graduação "não posso avaliar" para 1000 ([573a69e](http://git.fasolti.com.br/avd/web/commit/573a69ea342697dd8c435485f0eb30bedc7176b7))
* **graduacao mobile:** Ajuste da listagem de graduações no mobile ([#107](http://git.fasolti.com.br/avd/web/issues/107)) ([f145093](http://git.fasolti.com.br/avd/web/commit/f14509397dca8bb5bb1c83370e16d3a07abcd3fc))
* **preenchimento:** issue [#320](http://git.fasolti.com.br/avd/web/issues/320) ([77d57d9](http://git.fasolti.com.br/avd/web/commit/77d57d9448d48706dfcf0dd01104efcaf03fede7))
* **preenchimento:** issue [#320](http://git.fasolti.com.br/avd/web/issues/320) ([30ffbac](http://git.fasolti.com.br/avd/web/commit/30ffbac0e731894cdf20b4a4f57e75c980d9aa5e))

### [3.13.6](http://git.fasolti.com.br/avd/web/compare/v3.13.5...v3.13.6) (2020-08-28)

### [3.13.5](http://git.fasolti.com.br/avd/web/compare/v3.13.4...v3.13.5) (2020-08-27)


### Bug Fixes

* **exclusão pdi ação:** issues [#316](http://git.fasolti.com.br/avd/web/issues/316) ([123829c](http://git.fasolti.com.br/avd/web/commit/123829c51f4d31a19e0a70261e9383d813b8f1c5))

### [3.13.4](http://git.fasolti.com.br/avd/web/compare/v3.13.3...v3.13.4) (2020-08-13)


### Bug Fixes

* **cadastro de colaboradores:** correção do erro ao salvar, após a troca de setor. ([375ea51](http://git.fasolti.com.br/avd/web/commit/375ea51742016639f6c49af1b3419d42b27ee11a))

### [3.13.3](http://git.fasolti.com.br/avd/web/compare/v3.13.2...v3.13.3) (2020-08-05)


### Bug Fixes

* **competências:** ajuste na troca de escala de graduação no ajuste das competências. Ajuste na exibição das cores associadas as graduações. ([2c84482](http://git.fasolti.com.br/avd/web/commit/2c84482f1c1f4ac3e421bf82fa814d7237f753ec))
* **graduação:** ajuste na troca de escala. Ainda apresenta problemas no preenchimento. ([f5d4424](http://git.fasolti.com.br/avd/web/commit/f5d442492c00b2a147635c72d35f4ee2003a1618))
* **preenchimento:** Correção na liberação da avaliação, após ocorrer uma troca de escala em algum detalhamento. ([1207b02](http://git.fasolti.com.br/avd/web/commit/1207b026ac51d51a50f5bcd2c70708fd2fa0c4d5))

### [3.13.2](http://git.fasolti.com.br/avd/web/compare/v3.13.1...v3.13.2) (2020-07-26)


### Bug Fixes

* **avaliação:** ajuste na criação da autoavaliação. Esta somente deve ser criada como uma entrada separada na seguinte condição: a avaliação contiver o tipo "Autoavaliacao" e não o tipo "Gestores". ([fd47ec8](http://git.fasolti.com.br/avd/web/commit/fd47ec8553936ae380a247070de40b17af34627d))
* **avaliadores - salvar e liberar:** ajuste na liberação da avaliação após a edição de avaliadores. ([02fa612](http://git.fasolti.com.br/avd/web/commit/02fa612be2a76ad541b69c03b619d5041080374e))
* **avaliadores - salvar e liberar:** ajuste na liberação da avaliação após a edição de avaliadores. Versão correta para o hotfix. ([74241d7](http://git.fasolti.com.br/avd/web/commit/74241d77a5cb20c5f143ef7ba88a82e3653772b0))
* **Graduacao:** Ajusta a "duplicidade" da graduacao no mobile (mobile [#107](http://git.fasolti.com.br/avd/web/issues/107)) ([416b109](http://git.fasolti.com.br/avd/web/commit/416b109e6a0b1e33a6f791105642d155e69fa203))
* **salvar e liberar avaliadores:** ajuste na criação das autoavaliações. ([4308bef](http://git.fasolti.com.br/avd/web/commit/4308bef47836c536d2f74f0954d12f9dc7e1fd44))
* Ajusta a inativação da avaliação, setando uma data inferior à de criação ([#312](http://git.fasolti.com.br/avd/web/issues/312)) ([e407851](http://git.fasolti.com.br/avd/web/commit/e4078516f427038f42d7d86f9138634225b6ac13))
* Ajusta a mensagem de validação de troca de senhas no primeiro acesso ([#311](http://git.fasolti.com.br/avd/web/issues/311)) ([01a439b](http://git.fasolti.com.br/avd/web/commit/01a439bd5860e753ba4158a8ea97afe4c87b16cf))
* **selecionar avaliadores:** ajuste na seleção de avaliadores. (issue [#313](http://git.fasolti.com.br/avd/web/issues/313)) ([21e49b9](http://git.fasolti.com.br/avd/web/commit/21e49b90abea8fc3ce22efe5f701f91b7d917fd0))
* **validacao-primeiro-acesso:** Ajusta a validação dos campos do primeiro acesso ([#311](http://git.fasolti.com.br/avd/web/issues/311)) ([09e1d16](http://git.fasolti.com.br/avd/web/commit/09e1d16a0f811ffd4cb953d2d40cbcc9180b752a))

### [3.13.1](http://git.fasolti.com.br/avd/web/compare/v3.13.0...v3.13.1) (2020-07-09)


### Bug Fixes

* **lock de login:** ajustado o bloqueio de IP. (Issue [#307](http://git.fasolti.com.br/avd/web/issues/307)) ([d6ed40e](http://git.fasolti.com.br/avd/web/commit/d6ed40e3ba17fd20ebcdbcb4c05760d8df6aca55))

## [3.13.0](http://git.fasolti.com.br/avd/web/compare/v3.12.1...v3.13.0) (2020-07-05)


### Features

* **relatórios:** remover da lista as campanhas que sejam exclusivamente de autoavaliação para os perfis de funcionário e gestor de área. ([ec9ccf4](http://git.fasolti.com.br/avd/web/commit/ec9ccf4c81f336d2f04982b5c38448b452e5283c))

### [3.12.1](http://git.fasolti.com.br/avd/web/compare/v3.12.0...v3.12.1) (2020-06-20)


### Bug Fixes

* **avaliacao inativa:** Ao inativar uma avaliação, coloca as datas limites como data de criação para não aparecer avaliações inativas no preenchimento ([78984eb](http://git.fasolti.com.br/avd/web/commit/78984ebea11116bc366bddb3c70711f473cb1c0c))
* **cor da graduação:** Cria um enum para cores da graduação e as coloca no objeto TO do front ([#164](http://git.fasolti.com.br/avd/web/issues/164)) ([af9cea6](http://git.fasolti.com.br/avd/web/commit/af9cea6d340417a9a4d74fd73a8d017ea7d5e2dc))
* **data avaliacao:** Ajusta a criação do participante para a inserção das datas limites da avaliacao no mesmo ([#283](http://git.fasolti.com.br/avd/web/issues/283)) ([d0090c6](http://git.fasolti.com.br/avd/web/commit/d0090c6101aaee10c4f9880798fb20e7c1c91d11))
* **graduação ordem:** Ajusta a ordem nos hooks da graduação ([375d5a9](http://git.fasolti.com.br/avd/web/commit/375d5a968d5d8abb971026cf8e761f2f288ef1d6))
* **graduacoes inativas:** Ao abrir detalhamentos com graduações, traz apenas as ativas. Ao atualizar/criar uma graduação que não possua nenhuma ativa em seu modelo (ordem null) valida a query que pega o valor máximo de ordem. ([763f00b](http://git.fasolti.com.br/avd/web/commit/763f00bf744775eb7eff00bbc31ea2649250ebfa))
* **mobile-comentario obrigatorio:** Mapeia o comentario obrigatorio da AvaliacaoCompDetHint para a graduação, para ser exibido e validado no mobile (issue mobile [#100](http://git.fasolti.com.br/avd/web/issues/100)) ([ab0431c](http://git.fasolti.com.br/avd/web/commit/ab0431c37c6f74782d792db82d744b25d5c3e6a6))
* **mobile-comentario obrigatorio e cor graduacao:** Ajusta o comentario obrigatorio apenas para o detalhamento que tem esse opcao marcada. Ajusta as cores para serem setadas ao criar o objeto de front (TO) ([#100](http://git.fasolti.com.br/avd/web/issues/100)-mobile) ([5939a8b](http://git.fasolti.com.br/avd/web/commit/5939a8b0063ab56fb0ec1fa9aea9ed5c8775e19b))
* **mobile-cor graduacao:** Mapeia as cores da graduação para o mobile ([#164](http://git.fasolti.com.br/avd/web/issues/164)) ([12b7bfa](http://git.fasolti.com.br/avd/web/commit/12b7bfa8e7c57fa6bec7932142e2b80e02676a74))
* **mobile-graduacao-hint:** Ajusta os campos da GraduacaoMobileTO, onde insere o comentarioObrigatorio e a situacao da graduacao (caso esteja inativa, nao aparece). (mobile issue - [#100](http://git.fasolti.com.br/avd/web/issues/100)) ([b68b305](http://git.fasolti.com.br/avd/web/commit/b68b3056cb7f2ee3645e6329306cc3fd46accc0d))
* **remoção avaliador:** Ajusta a remoção de um avaliador ([#296](http://git.fasolti.com.br/avd/web/issues/296)) ([9a33528](http://git.fasolti.com.br/avd/web/commit/9a335286fac8033bfa70bd836b56a08f125b68d8))
* **salvar avaliados:** Ajusta a rotina de salvar avaliados, caso ele seja um gestor e não tenha avaliadores (superiores), pega apenas os subordinados ([#296](http://git.fasolti.com.br/avd/web/issues/296)) ([ba881f2](http://git.fasolti.com.br/avd/web/commit/ba881f264b4b32832b145262298d946899e2cdb2))

## [3.12.0](http://git.fasolti.com.br/avd/web/compare/v3.10.2...v3.12.0) (2020-06-10)


### Features

* adicionar suporte a prereleases e staging-hotfix ([d18e28e](http://git.fasolti.com.br/avd/web/commit/d18e28e7d6d1360c76fc2f8564944c0ab941bd6a))
* **calculo de porcentagens:** Cálculo dos totalizadores e porcentagens de preenchimento dos gráficos do acompanhamento ([#35](http://git.fasolti.com.br/avd/web/issues/35)) ([2ba7b8b](http://git.fasolti.com.br/avd/web/commit/2ba7b8b7bd61b9c103df966431685a9506855343))
* Inclusão coluna em configuração ([ac62f5f](http://git.fasolti.com.br/avd/web/commit/ac62f5fedc9320798996a38359fc2b1bb9ab9ec4))
* **cargo:** Botão copiar cargo ([bb1b6f1](http://git.fasolti.com.br/avd/web/commit/bb1b6f1666d1dee3518df5d1362614dac460eb43))
* **cargo:** Copiar cargo com nome em sequencia ([e354eca](http://git.fasolti.com.br/avd/web/commit/e354eca89acee032948e7f555b38d5cf01f874bf))


### Bug Fixes

* **acompanhamentos:** Remoção de autoavaliação caso não exista na avaliação ([#175](http://git.fasolti.com.br/avd/web/issues/175)) ([eea605f](http://git.fasolti.com.br/avd/web/commit/eea605f3ad376ce18d37c506ab98b2f8b64fbf5e))
* **calculo consenso:** Ajuste no calculo de consenso em acompanhamentos ([89c46c6](http://git.fasolti.com.br/avd/web/commit/89c46c6c0b770c3f5a70dfaf6724321cb5fb07ec))
* **ci:** corrigir script de migração de BD ([bc18c3d](http://git.fasolti.com.br/avd/web/commit/bc18c3deefd2960f167cef64067f222998b89b21))
* **ci:** corrigor change-dir para a pasta correta para a migração do BD, desabilitar download de artefatos nos jobs de migração de BD ([02fdb01](http://git.fasolti.com.br/avd/web/commit/02fdb016f9006f9c013e9eb510890ff128a522e5))
* **funcionario:** Ajuste do endpoint do obter funcionario por situacao ([#234](http://git.fasolti.com.br/avd/web/issues/234)) ([3908d81](http://git.fasolti.com.br/avd/web/commit/3908d81e011996d2a23aa132147806a30dec57fa))
* **hints:** Cria hints para todas competenciasDetalhamento ([#299](http://git.fasolti.com.br/avd/web/issues/299)) ([855377b](http://git.fasolti.com.br/avd/web/commit/855377b97873cd33accaf5cbe02867f4608e61ef))
* **modelo graduacao competencia:** Ajusta a chamada de listByParticipante de modeloGraduacao para AvaliacaoCompetencia ([#299](http://git.fasolti.com.br/avd/web/issues/299)) ([9b5e4bd](http://git.fasolti.com.br/avd/web/commit/9b5e4bd3ac0e28ac8c47662ac3654568ebdea23b))
* **redefinir senha:** Mensagem ao errar senha ([#226](http://git.fasolti.com.br/avd/web/issues/226)) ([02ee32c](http://git.fasolti.com.br/avd/web/commit/02ee32c53bce1758243fb2e442865e4ff1959d13))

## [3.11.0](http://git.fasolti.com.br/avd/web/compare/v3.10.2...v3.11.0) (2020-06-10)


### Features

* adicionar suporte a prereleases e staging-hotfix ([d18e28e](http://git.fasolti.com.br/avd/web/commit/d18e28e7d6d1360c76fc2f8564944c0ab941bd6a))
* **calculo de porcentagens:** Cálculo dos totalizadores e porcentagens de preenchimento dos gráficos do acompanhamento ([#35](http://git.fasolti.com.br/avd/web/issues/35)) ([2ba7b8b](http://git.fasolti.com.br/avd/web/commit/2ba7b8b7bd61b9c103df966431685a9506855343))
* Inclusão coluna em configuração ([ac62f5f](http://git.fasolti.com.br/avd/web/commit/ac62f5fedc9320798996a38359fc2b1bb9ab9ec4))
* **cargo:** Botão copiar cargo ([bb1b6f1](http://git.fasolti.com.br/avd/web/commit/bb1b6f1666d1dee3518df5d1362614dac460eb43))
* **cargo:** Copiar cargo com nome em sequencia ([e354eca](http://git.fasolti.com.br/avd/web/commit/e354eca89acee032948e7f555b38d5cf01f874bf))


### Bug Fixes

* **acompanhamentos:** Remoção de autoavaliação caso não exista na avaliação ([#175](http://git.fasolti.com.br/avd/web/issues/175)) ([eea605f](http://git.fasolti.com.br/avd/web/commit/eea605f3ad376ce18d37c506ab98b2f8b64fbf5e))
* **calculo consenso:** Ajuste no calculo de consenso em acompanhamentos ([89c46c6](http://git.fasolti.com.br/avd/web/commit/89c46c6c0b770c3f5a70dfaf6724321cb5fb07ec))
* **ci:** corrigir script de migração de BD ([bc18c3d](http://git.fasolti.com.br/avd/web/commit/bc18c3deefd2960f167cef64067f222998b89b21))
* **ci:** corrigor change-dir para a pasta correta para a migração do BD, desabilitar download de artefatos nos jobs de migração de BD ([02fdb01](http://git.fasolti.com.br/avd/web/commit/02fdb016f9006f9c013e9eb510890ff128a522e5))
* **funcionario:** Ajuste do endpoint do obter funcionario por situacao ([#234](http://git.fasolti.com.br/avd/web/issues/234)) ([3908d81](http://git.fasolti.com.br/avd/web/commit/3908d81e011996d2a23aa132147806a30dec57fa))
* **hints:** Cria hints para todas competenciasDetalhamento ([#299](http://git.fasolti.com.br/avd/web/issues/299)) ([855377b](http://git.fasolti.com.br/avd/web/commit/855377b97873cd33accaf5cbe02867f4608e61ef))
* **modelo graduacao competencia:** Ajusta a chamada de listByParticipante de modeloGraduacao para AvaliacaoCompetencia ([#299](http://git.fasolti.com.br/avd/web/issues/299)) ([9b5e4bd](http://git.fasolti.com.br/avd/web/commit/9b5e4bd3ac0e28ac8c47662ac3654568ebdea23b))
* **redefinir senha:** Mensagem ao errar senha ([#226](http://git.fasolti.com.br/avd/web/issues/226)) ([02ee32c](http://git.fasolti.com.br/avd/web/commit/02ee32c53bce1758243fb2e442865e4ff1959d13))

### [3.10.2](http://git.fasolti.com.br/avd/web/compare/v3.10.1...v3.10.2) (2020-06-09)


### Bug Fixes

* **liberar avaliação:** ajuste do SecurityContext da thread de liberação da avaliação. ([ebf7d60](http://git.fasolti.com.br/avd/web/commit/ebf7d60cd972295ea06690203e942fead646ba87))
* **liberar avaliação:** log para verificar erro na liberação. ([b98fa69](http://git.fasolti.com.br/avd/web/commit/b98fa69d69bce09d1b3a98dc463c1a7162888a3d))
* **liberar simplificado:** mensagens de log para verificar problema na liberação no hotfix e producão. ([0c76965](http://git.fasolti.com.br/avd/web/commit/0c76965b5cbd9213b5b019e5384a95b835b20e7d))

### [3.10.1](http://git.fasolti.com.br/avd/web/compare/v3.10.0...v3.10.1) (2020-06-08)


### Bug Fixes

* **liberar avaliação:** ajuste na persistência da avaliação. O sistema estava gerando exceção ao persistir, devido a falta do controle de transação. ([26823ec](http://git.fasolti.com.br/avd/web/commit/26823ecf29d8c19f1ee07932f634f6ad5d451203))

## [3.10.0](http://git.fasolti.com.br/avd/web/compare/v3.9.0...v3.10.0) (2020-06-08)


### Features

* **liberar simplificado:** ajuste no fluxo de liberação e nos envios de e-mail. ([cf1373b](http://git.fasolti.com.br/avd/web/commit/cf1373b4237da07156e662584c4471c413de80c5))


### Bug Fixes

* **liberar simplificado:** remoção de teste que impedia a conclusão da liberação. ([edbc317](http://git.fasolti.com.br/avd/web/commit/edbc31730a21599483c11572bc4deeccbd5e1003))

## [3.9.0](http://git.fasolti.com.br/avd/web/compare/v3.8.5...v3.9.0) (2020-06-04)


### Features

* **libera:** no login do usuário, caso não exista a entrada deste usuário na tabela configuração, o sistema cria este registro. ([d9a24aa](http://git.fasolti.com.br/avd/web/commit/d9a24aab68667594cc058a9261a7f5cac08c5dda))
* **liberar simplificado:** ajuste na rotina de liberação de avaliações HomeOffice. ([e456658](http://git.fasolti.com.br/avd/web/commit/e45665826dc94e873a7ef181cb0347812ff14c49))
* **liberar simplificado:** correção do preenchimento por colaborador. ([d2d3f3a](http://git.fasolti.com.br/avd/web/commit/d2d3f3a5237f80626cc41c796d3ffe5bae3f4437))
* **liberar simplificado:** liberação funcional, porém sem os envios de e-mail. ([8441800](http://git.fasolti.com.br/avd/web/commit/84418004d6f4c2adf7b91258de8310a625288aab))
* **liberar simplificado:** pequeno ajuste para o fazer o deploy. ([423d84c](http://git.fasolti.com.br/avd/web/commit/423d84c681d8ed18ee31904cf2ec6c8d77150334))
* **status em liberacao:** Cria o SQL do status em liberacao na avaliacao e na campanha (avEmLiberacao) e em suas respectivas entidades ([#285](http://git.fasolti.com.br/avd/web/issues/285)) ([0c7803a](http://git.fasolti.com.br/avd/web/commit/0c7803ab0f11903328461fb74b4245a65829c9a4))


### Bug Fixes

* **envio mensagem avaliacao:** Ajusta o método de envio de mensagem pela avaliação e seus respectivos endpoints ([#295](http://git.fasolti.com.br/avd/web/issues/295)) ([8216504](http://git.fasolti.com.br/avd/web/commit/82165049d0540716a7aceb25426d36c6eea5040e))
* **funcionario matricula:** Ajusta a query de pesquisa com ignore case na matricula do colaborador ([#288](http://git.fasolti.com.br/avd/web/issues/288)) ([b82f512](http://git.fasolti.com.br/avd/web/commit/b82f51275d2cff66d6314c60182d62703436f68e))

### [3.8.5](http://git.fasolti.com.br/avd/web/compare/v3.8.4...v3.8.5) (2020-05-20)


### Bug Fixes

* **email by empresa:** Ajusta as consultas e validações de e-mail por empresa e situacao ATIVA ([#281](http://git.fasolti.com.br/avd/web/issues/281)) ([02a5b28](http://git.fasolti.com.br/avd/web/commit/02a5b2866b90fa75816c0e7c8c1c542c6925a8b5))

### [3.8.4](http://git.fasolti.com.br/avd/web/compare/v3.8.2...v3.8.4) (2020-05-19)


### Bug Fixes

* **email by empresa:** Ajusta consultas e validações de email por empresa ([#281](http://git.fasolti.com.br/avd/web/issues/281)) ([542aa6f](http://git.fasolti.com.br/avd/web/commit/542aa6ffeac14410509ace2736b19649eca3eef3))
* **template email:** Ajusta o template correto nos e-mails enviados ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([0fd84fe](http://git.fasolti.com.br/avd/web/commit/0fd84fe1d549018aa3427e74e2cd8432524e8fb7))
* **template email:** Coloca o template correto nos e-mails enviados ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([07bfdec](http://git.fasolti.com.br/avd/web/commit/07bfdec1b898d4150957fbcca592def6833ad636))

### [3.8.3](http://git.fasolti.com.br/avd/web/compare/v3.8.2...v3.8.3) (2020-05-19)


### Bug Fixes

* **email by empresa:** Ajusta consultas e validações de email por empresa ([#281](http://git.fasolti.com.br/avd/web/issues/281)) ([542aa6f](http://git.fasolti.com.br/avd/web/commit/542aa6ffeac14410509ace2736b19649eca3eef3))
* **template email:** Ajusta o template correto nos e-mails enviados ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([0fd84fe](http://git.fasolti.com.br/avd/web/commit/0fd84fe1d549018aa3427e74e2cd8432524e8fb7))
* **template email:** Coloca o template correto nos e-mails enviados ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([07bfdec](http://git.fasolti.com.br/avd/web/commit/07bfdec1b898d4150957fbcca592def6833ad636))

### [3.8.2](http://git.fasolti.com.br/avd/web/compare/v3.8.1...v3.8.2) (2020-05-18)


### Bug Fixes

* **envio email:** Corrige a validação das checkboxes ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([6541808](http://git.fasolti.com.br/avd/web/commit/654180861c83406f124b9f67a52dcf992cae3730))

### [3.8.1](http://git.fasolti.com.br/avd/web/compare/v3.8.0...v3.8.1) (2020-05-17)


### Bug Fixes

* **email homeoffice:** Ajusta o negrito do email de diagnostico. ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([6c028a8](http://git.fasolti.com.br/avd/web/commit/6c028a85148e88568e7bca83cff08e1cc9d17c0e))

## [3.8.0](http://git.fasolti.com.br/avd/web/compare/v3.7.5...v3.8.0) (2020-05-17)


### Features

* **template email:** Coloca o template correto nos e-mails enviados ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([e3578be](http://git.fasolti.com.br/avd/web/commit/e3578be9587410e84210514183bf61f69fc9d860))

### [3.7.5](http://git.fasolti.com.br/avd/web/compare/v3.7.4...v3.7.5) (2020-05-17)

### [3.7.4](http://git.fasolti.com.br/avd/web/compare/v3.7.3...v3.7.4) (2020-05-16)


### Bug Fixes

* **ci/migrate:** configurar jobs para serem executados na etapa "migrate" ([d05dee8](http://git.fasolti.com.br/avd/web/commit/d05dee8c0391f6fe2a1e0e22e417efebe0486f19))
* **ci/migrate:** remover obrigatoriedade de usar servidores internos para os jobs; remover dependências de artefatos de etapas anteriores ([11c17b5](http://git.fasolti.com.br/avd/web/commit/11c17b5e6e271c00f12ed78d96d289062ff2fdf0))

### [3.7.3](http://git.fasolti.com.br/avd/web/compare/v3.7.2...v3.7.3) (2020-05-15)


### Bug Fixes

* **email validation:** Ajusta a validação de duplicidade de email ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([665582e](http://git.fasolti.com.br/avd/web/commit/665582ec3c805288ab68e64ac7c97a8799977c3f))
* **liberacao avaliacao:** Ajusta a liberação da avaliação ao ter apenas autoavaliacao selecionada ([#279](http://git.fasolti.com.br/avd/web/issues/279)) ([f3eda42](http://git.fasolti.com.br/avd/web/commit/f3eda426d9aff7806c5c09d71711c450c29acb5a))

### [3.7.2](http://git.fasolti.com.br/avd/web/compare/v3.7.1...v3.7.2) (2020-05-13)


### Bug Fixes

* **HomeOfficeBrasil:** Valida o e-mail de cadastro do funcionário e ajusta a mensagem do e-mail do ambiente empresarial ([#267](http://git.fasolti.com.br/avd/web/issues/267)) ([ada9e14](http://git.fasolti.com.br/avd/web/commit/ada9e14f0560ad102a7462cd3e7e398c5f435c96))
* **mobile:** considerar perfil "autoavaliação" na obtenção dos dados relacionados à avaliação, obter dados de autoavaliação (AV1, etc) ao carregar as avaliações e detalhamentos (mobile[#99](http://git.fasolti.com.br/avd/web/issues/99)) ([9631911](http://git.fasolti.com.br/avd/web/commit/963191137a685709061beedae2db6c217bb42a50))

### [3.7.1](http://git.fasolti.com.br/avd/web/compare/v3.7.0...v3.7.1) (2020-05-08)

### Features

* **HomeOfficeBrasil:** Valida os checkboxes do frontend e faz as regras de negócio dependendo dos checks selecionados (#267) ([21dd45bfd](http://git.fasolti.com.br/avd/web/commit/21dd45bfd94bd665a27b00538494385ffb238d91))
* **HomeOfficeBrasil:** Quebra o método de criação do funcionário (#267) ([d4adc5f76](http://git.fasolti.com.br/avd/web/commit/d4adc5f762dc07358f3c5e69f4d67cd03399b7d7))
* **HomeOfficeBrasil:** Autentica o usuario e cria o arquivo de properties do homeoffice (#267) ([e4ecf74ec](http://git.fasolti.com.br/avd/web/commit/e4ecf74ec4ac56dea538afb4f5f4fee74d6d2951))
* **HomeOfficeBrasil:** Cria funcionário administrativo e seus respectivos campos, também cria o SQL do para o flyway. Efetua "logout" ao terminar a transação e parametriza os campos do properties home office (#267) ([0b49e07dc](http://git.fasolti.com.br/avd/web/commit/0b49e07dc22e170181f017b343d5026d0af67787))
* **HomeOfficeBrasil:** Troca os ID's das propriedades necessárias para a empresa home office brasil (#267) ([e514b099b](http://git.fasolti.com.br/avd/web/commit/e514b099bb5eb2631e3410e06a5460a3e05d2d9b))

### Bug Fixes

* **HomeOfficeBrasil, bd-ddl:** corrigir nome da coluna endereco.id_municipio ([21c940d](http://git.fasolti.com.br/avd/web/commit/21c940dc3df4f32ca7002b81e4b5c7d9bc6da6bb))
* **HomeOfficeBrasil:** Ajuste no template do e-mail de boas-vindas (#278) ([69691cacb](http://git.fasolti.com.br/avd/web/commit/69691cacb506859782d0d665162d0885180870af))

## [3.7.0](http://git.fasolti.com.br/avd/web/compare/v3.6.0...v3.7.0) (2020-04-29)


### Features

* Nova entidade endereço. ([dbda315](http://git.fasolti.com.br/avd/web/commit/dbda31546d01d870eeb38f589b3ea3c856644d2b))
* Novo endpoint para pesquisas de municipio. ([b2b911e](http://git.fasolti.com.br/avd/web/commit/b2b911e759a82d09eb62d5e36a4a7b2b10f5b2ea))
* Novo sql para a criação da tabela municípios com seus dados ([#272](http://git.fasolti.com.br/avd/web/issues/272)). ([16c080f](http://git.fasolti.com.br/avd/web/commit/16c080f27c6c5c16c9caf1ed1e82dec77eb03cb1))
* Referente aos issues [#258](http://git.fasolti.com.br/avd/web/issues/258) e [#273](http://git.fasolti.com.br/avd/web/issues/273). ([f94f39d](http://git.fasolti.com.br/avd/web/commit/f94f39de4fae5fffd151e1183b2f938f3a63b455))
* Referente aos issues [#261](http://git.fasolti.com.br/avd/web/issues/261). ([330a12b](http://git.fasolti.com.br/avd/web/commit/330a12b51dbc73fa35b008ec4df81194ecd544a8))

## [3.6.0](http://git.fasolti.com.br/avd/web/compare/v3.5.1...v3.6.0) (2020-04-10)


### Features

* **auth/mobile, pwa:** equiparar listagem de empresas com a realizada para o avd/web ([9f73433](http://git.fasolti.com.br/avd/web/commit/9f7343337c7b87770f9f7b3d3c1c9ac60e9681b5))


### Bug Fixes

* **auth, cadastros/colaboradores:** considerar a matrícula como única e case-insensitive para login e cadastro ([#257](http://git.fasolti.com.br/avd/web/issues/257)) ([861f1fe](http://git.fasolti.com.br/avd/web/commit/861f1fe9489efc4b6b099d07cd9aa5649d2cd947))

### [3.5.1](http://git.fasolti.com.br/avd/web/compare/v3.5.0...v3.5.1) (2020-03-28)


### Bug Fixes

* **logs:** gravar informações de alterações realizadas em "bulk" (recebidas via arrays de objetos em requisições) ( web/[#256](http://git.fasolti.com.br/avd/web/issues/256)) ([c1c7eac](http://git.fasolti.com.br/avd/web/commit/c1c7eacbea5d234d59e28feccb6f69fab5a36be1))
* **mobile:** equiparar listagem de participantes e detalhamentos com a realizada no projeto avd/webapp (mobile[#98](http://git.fasolti.com.br/avd/web/issues/98)) ([206ad1f](http://git.fasolti.com.br/avd/web/commit/206ad1f5a9d28aa5a8291bbbdea247225387f97a))

## [3.5.0](http://git.fasolti.com.br/avd/web/compare/v3.4.8...v3.5.0) (2020-03-16)


### Features

* **funcionario:** melhorar validação nas ações de persistência, permitir ativação e inativação em endpoints próprios (usando o BaseAvdService) (web[#252](http://git.fasolti.com.br/avd/web/issues/252)) ([5d92cf6](http://git.fasolti.com.br/avd/web/commit/5d92cf61e1c526877665db1a0a49a6e3af5c04dc))

### [3.4.8](http://git.fasolti.com.br/avd/web/compare/v3.4.7...v3.4.8) (2020-02-20)


### Bug Fixes

* **lista PDI:** erro ao trazer PDI's sem participantes, o projections cria um objeto com todos os atributos nulos ([#246](http://git.fasolti.com.br/avd/web/issues/246)) ([2b9f6db](http://git.fasolti.com.br/avd/web/commit/2b9f6db1a8ae424b5eaa95e1799e6fa836e9687b))

### [3.4.7](http://git.fasolti.com.br/avd/web/compare/v3.4.6...v3.4.7) (2020-02-20)


### Bug Fixes

* **Funcionario:** Ajuste ao alterar pontos focais dos departamentos ([#234](http://git.fasolti.com.br/avd/web/issues/234)) ([c7f4d15](http://git.fasolti.com.br/avd/web/commit/c7f4d156ecf0b92942e8f921e353c9c5d0fb9b90))

### [3.4.6](http://git.fasolti.com.br/avd/web/compare/v3.4.5...v3.4.6) (2020-02-20)


### Bug Fixes

* **Combo campanhas PDI:** Ajuste para trazer apenas as campanhas/avaliações pelo avaliador (funcionario logado) e o avaliado (funcionario selecionado) ([#242](http://git.fasolti.com.br/avd/web/issues/242)) ([659345b](http://git.fasolti.com.br/avd/web/commit/659345beb0ea15bb240e41a5d378fda1869186a7))
* **PDI:** Ajuste na query das avaliações/campanhas do PDI ([#245](http://git.fasolti.com.br/avd/web/issues/245)) ([4118a90](http://git.fasolti.com.br/avd/web/commit/4118a90283e711a9c8f65be0e24af7c0c4f42026))

### [3.4.5](http://git.fasolti.com.br/avd/web/compare/v3.4.4...v3.4.5) (2020-02-19)


### Bug Fixes

* **hierarquia deptos:** Exibir o gestor e seus subordinados diretos (com departamentos 1 nivel abaixo na hierarquia)  ([#4](http://git.fasolti.com.br/avd/web/issues/4)) ([14c875c](http://git.fasolti.com.br/avd/web/commit/14c875cae50763d246b129829eb4bdcab0ea4bda))

### [3.4.4](http://git.fasolti.com.br/avd/web/compare/v3.4.3...v3.4.4) (2020-02-17)


### Bug Fixes

* **unidade_departamento:** Ao alterar/criar um funcionário, não estava excluindo a unidade_departamento caso ele nao seja mais um gestor ([#239](http://git.fasolti.com.br/avd/web/issues/239)) ([075f9c0](http://git.fasolti.com.br/avd/web/commit/075f9c0bec3b4ba606de2246ab6936b3b09798ed))
* **unidade_departamento:** Erro ao criar um novo funcionário com perfil de gestor de área, e sendo um gestor/líder  ([#239](http://git.fasolti.com.br/avd/web/issues/239)) ([84df48b](http://git.fasolti.com.br/avd/web/commit/84df48b5cca94e55a319e9bdffe03ce06aa1bb15))

### [3.4.3](http://git.fasolti.com.br/avd/web/compare/v3.4.2...v3.4.3) (2020-02-15)


### Bug Fixes

* **erro de validação aba avaliados:** Validação ao verificar que um par de avaliado/avaliador ja se encontra previamente cadastrado ([#238](http://git.fasolti.com.br/avd/web/issues/238)) ([c49fc47](http://git.fasolti.com.br/avd/web/commit/c49fc4743d05d8a1d2111ebcf52f34584d76faeb))

### [3.4.2](http://git.fasolti.com.br/avd/web/compare/v3.4.1...v3.4.2) (2020-02-13)


### Bug Fixes

* **Campanha list PDI:** Ajuste no filtro, trazendo apenas as avaliações liberadas ([#237](http://git.fasolti.com.br/avd/web/issues/237)) ([27eba55](http://git.fasolti.com.br/avd/web/commit/27eba55cc7146e202c344219f3af57deeeae84f6))
* **Campanha list PDI:** Ajuste no projections de campanhas do PDI, removendo a necessidade de ter o PDI e fazendo o filtro no front ([#237](http://git.fasolti.com.br/avd/web/issues/237)) ([32bca5b](http://git.fasolti.com.br/avd/web/commit/32bca5b8e864d20d08e8aee709625524f30c3bea))
* **ci:** remover nome do container de build ([fa44372](http://git.fasolti.com.br/avd/web/commit/fa443729454b62e7decd9f04a2d993b616310141))
* **ci:** transformar a etapa de migração em falível, melhorar as descrições das etapas de migração do BD ([985a8c2](http://git.fasolti.com.br/avd/web/commit/985a8c2cca94a6ee6aea977acdd6f591126105df))

### [3.4.1](http://git.fasolti.com.br/avd/web/compare/v3.4.0...v3.4.1) (2020-02-05)


### Bug Fixes

* **CompDetalhamentoCalibraHint:** Ao atualizar uma graduação, atualiza o hint da competencia ([#229](http://git.fasolti.com.br/avd/web/issues/229)) ([d67a1da](http://git.fasolti.com.br/avd/web/commit/d67a1dada3330fa28ea89f4145e2bc47c7d9b844))
* **detalhamento, competencia e cargo :** Ajustes ao inativar/ativar as devidas entidades ([#229](http://git.fasolti.com.br/avd/web/issues/229)) ([eb439ce](http://git.fasolti.com.br/avd/web/commit/eb439ce3c0c640221a316082dd1da84df8aa1951))
* **mobile:** listar competências e tipos de competência mesmo que não haja nenhum apontamento funcional cadastrado (web/[#230](http://git.fasolti.com.br/avd/web/issues/230)) ([f98611d](http://git.fasolti.com.br/avd/web/commit/f98611d5aa0e2d0dfca37d1d2382ab00f0a6cd04))
* **update funcionario:** Atualizacao de funcionario ([#234](http://git.fasolti.com.br/avd/web/issues/234)) ([6511ace](http://git.fasolti.com.br/avd/web/commit/6511acedcacbd344b68e42f8e02a8a7f15430622))

## [3.4.0](http://git.fasolti.com.br/avd/web/compare/v3.3.0...v3.4.0) (2020-01-24)


### Features

* **ci:** efetuar migração do BD via imagem Docker oficial do Flyway, permitir geração de imagens localmente em fase "staging" ([5ddb217](http://git.fasolti.com.br/avd/web/commit/5ddb217c07aae172e212cf270d8868a127d2c05f))
* **ci:** efetuar migração do BD via SSH ([eb0e4ad](http://git.fasolti.com.br/avd/web/commit/eb0e4ad67b192d107f0bff09617756f8b4affa51))
* **ci:** mostrar informações de migração antes de efetuá-la, adicionar parâmetro DEZI_DB_MIGRATE_OPTS para customizar execução da migração de BD ([b5dbaab](http://git.fasolti.com.br/avd/web/commit/b5dbaab79b96326e00f62a3ac70a7d4d2f5354d2))
* adicionar suporte a prereleases e staging-hotfix ([bb33002](http://git.fasolti.com.br/avd/web/commit/bb3300276231a7ce958aee92cf1eb8e3d2d34854))


### Bug Fixes

* **ci:** corrigir typo no script de migração do BD ([d1b6d0e](http://git.fasolti.com.br/avd/web/commit/d1b6d0ee8b0b18a4b00d6f68a8dcbbdb220f2ecd))

## [3.3.0](http://git.fasolti.com.br/avd/web/compare/v3.2.1...v3.3.0) (2019-12-21)


### Features

* **log:** aprimorar logging em log_entity, usar TimeBasedRollingAppender ([2a92cfb](http://git.fasolti.com.br/avd/web/commit/2a92cfb56aebee4d46fdf67569b88db70e764d68))
* **log:** interceptar e analisar todas as requisições feitas para a API REST ([9df3603](http://git.fasolti.com.br/avd/web/commit/9df36035286150b8907aca419b36079935771a60))
* **log:** interceptar e analisar todas as requisições feitas para a API REST ([17aacef](http://git.fasolti.com.br/avd/web/commit/17aacef1e428d5811dd56ee5711ed5267a2ed660))


### Bug Fixes

* **log:** adicionar suporte a tipos JDK8 ao Dozer, aprimorar logging de falhas ao criar LogEntities ([2f013d3](http://git.fasolti.com.br/avd/web/commit/2f013d31bd5f468b6d4378ac0e00f9ac0b0eba21))

### [3.2.1](http://git.fasolti.com.br/avd/web/compare/v3.2.0...v3.2.1) (2019-12-20)


### Bug Fixes

* **ci,release:** atribuir nova versão a todas as tags "parent" dos módulos do projeto ao executar o set-pom-version ([adec209](http://git.fasolti.com.br/avd/web/commit/adec20969e37eaba5af8adfd6ab2d12253ad7aff))
* **log:** ativar logging para todas as URLs ([e20acc0](http://git.fasolti.com.br/avd/web/commit/e20acc014ebb8cab690b3c72b4bee8c815959d52))

## [3.2.0](http://git.fasolti.com.br/avd/web/compare/v0.9.0-Master...v3.2.0) (2019-12-18)


### Features

* **adjust, foto:** Troca do endpoint de update de  detalhamentos e obtenção das fotos descomentado ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([5eb8106](http://git.fasolti.com.br/avd/web/commit/5eb810621031d3c5b9e3735a82b101cc85c2ed1d))
* **adjust, merge:** Ajuste pós merge no GET da lista de avaliacoes ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([6ebc3f2](http://git.fasolti.com.br/avd/web/commit/6ebc3f22abe7ace5eb7bee5cf2ef8454a615c7ca))
* **adjust, query:** Ajuste nos filtros da query de detalhamentos/preenchimentos ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([2d81a46](http://git.fasolti.com.br/avd/web/commit/2d81a462b4c7cd2777a2935756b4d0413939dd47))
* **adjust, query:** Ajuste nos filtros da query de lista de avaliações ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([354082d](http://git.fasolti.com.br/avd/web/commit/354082dbc8530c027e9ba88ee955bd251b2e2801))
* **Ajuste endpoints; Participante; PDI:** Campos para salvar o PDI, ajuste no endpoint de save competencia ([#3](http://git.fasolti.com.br/avd/web/issues/3)) ([4dd6d94](http://git.fasolti.com.br/avd/web/commit/4dd6d94abb95d49aae9469e54e33c4db23274834))
* **apontamentoFuncional:** adicionar ServiceFilter, usar listagem de subordinados de acordo com o usado pelo Mobile, ordenar FF recebidos por data de forma decrescente (mobile/[#81](http://git.fasolti.com.br/avd/web/issues/81), mobile/[#3](http://git.fasolti.com.br/avd/web/issues/3), mobile/[#4](http://git.fasolti.com.br/avd/web/issues/4), mobile/[#33](http://git.fasolti.com.br/avd/web/issues/33)) ([1dcabe0](http://git.fasolti.com.br/avd/web/commit/1dcabe0878495bb7cf953939e1a7ba3f1381c918))
* **auth:** criar endpoint de listagem de empresas exclusivo para o app mobile, remover verificação do header Host da listagem de empresas do webapp ([f2df977](http://git.fasolti.com.br/avd/web/commit/f2df97790eba365d62da6f3b29e1a52862730f1c))
* **auth:** filtrar lista de empresas por Host ([4d3f4b7](http://git.fasolti.com.br/avd/web/commit/4d3f4b766f2a8934252fca2c5c6ea186a97d820f))
* **auth/empresas:** restringir listagem padrão (todas) por host (usando headers, e.g. Origin) em ambiente de produção ([d3cfd05](http://git.fasolti.com.br/avd/web/commit/d3cfd05733029cc93335cd8d742f4baa9c0c9722))
* **Campanha:** Quantidade de Avaliação por campanha (BaseService) ([8047e61](http://git.fasolti.com.br/avd/web/commit/8047e61b31db0fa76a16cf863aa7b6d1c53cb860))
* **campo acaoPDI, participante:** Ajuste no endpoint de salvar competencias, adição do campo acaoPDI na competencia ([#161](http://git.fasolti.com.br/avd/web/issues/161)) ([11b41c5](http://git.fasolti.com.br/avd/web/commit/11b41c5b6ae5575b4d112e53de378154cf20b363))
* **ci:** disponibilizar variáveis de ambiente do CI em tempo de execução ([0a49359](http://git.fasolti.com.br/avd/web/commit/0a4935992df74025002e2994a3d6a725a935decc))
* **ci:** remover worker, configurar JDBC do tomcat por sysprops, adicionar labels às imagens geradas via scripts de build ([52261fb](http://git.fasolti.com.br/avd/web/commit/52261fb533c2866cb13a6f7d59aae5c9846cd6f2))
* **ci:** usar imagem-base oficial do Tomcat ([7b26537](http://git.fasolti.com.br/avd/web/commit/7b2653795aaf34d4e33c0df4b867c01fcaaaaaa1))
* **ci/migrate:** adicionar host "db" apontando para o IP do host na configuração Docker padrão, tornar a execução do apt-get silenciosa ([0eb5bfc](http://git.fasolti.com.br/avd/web/commit/0eb5bfcf05ea9bf341165b4fae2344867f84e5b3))
* Ajustes PDI ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([cdc8544](http://git.fasolti.com.br/avd/web/commit/cdc8544939d744aa1426e6f64f255bfd2ec9195e))
* **comentarios:** método GET de avaliacaoMobile finalizado ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([56fd098](http://git.fasolti.com.br/avd/web/commit/56fd0981216b8520369dfe11b010885750ed5ee8))
* **compression:** Compressão de foto do funcionario ([#86](http://git.fasolti.com.br/avd/web/issues/86)) ([2aab834](http://git.fasolti.com.br/avd/web/commit/2aab834a57978b2938de04b77f2ef9107350c947))
* **exportação excel:** Exportação concluida([#147](http://git.fasolti.com.br/avd/web/issues/147)) ([e7507c8](http://git.fasolti.com.br/avd/web/commit/e7507c8789e9442450dc618be0ceaf88deac8dcd))
* **filter; roles:** Filtro de funcionarios a partir de sua role ([#4](http://git.fasolti.com.br/avd/web/issues/4)) ([4cb6b60](http://git.fasolti.com.br/avd/web/commit/4cb6b6038395b0ae80bb14aa427a1bebb407d17e))
* **Flyway:** Criaçao da tabela PDI_Acoes ([d3fa0eb](http://git.fasolti.com.br/avd/web/commit/d3fa0ebc07894efbce00c9fe014ba96d1b82ba87))
* **mobile; login:** adicionar dados de empresa, unidade e cargo ao REST do funcionário logado ([d6128ed](http://git.fasolti.com.br/avd/web/commit/d6128ed8b6aa92f3479e3bf16a06855305f37d0e))
* **PDI:** Ajuste Dialog iniciar e finalizar açao ([75c7cde](http://git.fasolti.com.br/avd/web/commit/75c7cdef79f05dfa7c3b9a06e670be55143b0f19))
* **PDI:** Ajuste Lista de PDI ([e257f1c](http://git.fasolti.com.br/avd/web/commit/e257f1cb9ff49c722b2038891a44b46aaa8b076f))
* **PDI:** Ajuste merge ([967a3d1](http://git.fasolti.com.br/avd/web/commit/967a3d10fda92e390c841877b2f0896dd60dde2e))
* **PDI:** Ajuste no modelo PDI, inclusao de funcionario ([9630bc4](http://git.fasolti.com.br/avd/web/commit/9630bc4fca1dce3598c89311684f818f61cad32f))
* **PDI:** Ajuste no modelo PDI, inclusao de funcionario ([f3c5c4f](http://git.fasolti.com.br/avd/web/commit/f3c5c4fd3902d23115ba93723d37836ad4e87ff9))
* **PDI:** Criaçao de modelo/entidade, TO, service, resource e repository ([b534e9f](http://git.fasolti.com.br/avd/web/commit/b534e9f5b8905290dd2ab476e64dcfd2d5b70466))
* **PDI:** Ordem id pdiAcao ([af5eff3](http://git.fasolti.com.br/avd/web/commit/af5eff37b941f4dde64988389ca68f55792d587a))
* **PDI:** Serviço de busca de avaliaçoes com PDI ([9d0c8be](http://git.fasolti.com.br/avd/web/commit/9d0c8be9f11de11653cd71f231501ad5906945c8))
* **preenchimento:** Percentual de preenchimento por preenchimento (mobile/[#89](http://git.fasolti.com.br/avd/web/issues/89)) ([45ba0f9](http://git.fasolti.com.br/avd/web/commit/45ba0f951b55607f53f742ecfa69024807cb4389))
* **reestruturação:** REST de FaleConosco e DeziBaseService ([#57](http://git.fasolti.com.br/avd/web/issues/57)) ([1a7aecf](http://git.fasolti.com.br/avd/web/commit/1a7aecf8a35964e559b65b7902d7a14866882b5f))
* **relatorio, filtro campanha e avaliacao:** Filtros do relatorio terminados ([#147](http://git.fasolti.com.br/avd/web/issues/147)) ([ecd75fb](http://git.fasolti.com.br/avd/web/commit/ecd75fb42748377c52e78c9471ffb347f095f02f))
* **save acao:** Método para salvar a ação e suas competencias/detalhamentos ([#131](http://git.fasolti.com.br/avd/web/issues/131)) ([a7b08af](http://git.fasolti.com.br/avd/web/commit/a7b08afde48ad87407b4cd0cdcb887297f861946))
* Ajustes em LogAuth ([a01eda4](http://git.fasolti.com.br/avd/web/commit/a01eda4c6f07d0d7b7833fabef8d9f4820b3b8ee))
* Classes LogAuth([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([268eb73](http://git.fasolti.com.br/avd/web/commit/268eb736e49c97f4a35340e9797066f0037dde61))
* criar serviço REST (create) para Feedforward  ([#55](http://git.fasolti.com.br/avd/web/issues/55)) ([f25ade4](http://git.fasolti.com.br/avd/web/commit/f25ade4805f849468847b2a9113740b55c6bce95))
* criar serviço REST para Apontamento (#mobile/[#53](http://git.fasolti.com.br/avd/web/issues/53)) ([2450db5](http://git.fasolti.com.br/avd/web/commit/2450db549b46232b627dcaecee8064c7fd57d969))
* criar serviço REST para Apontamento (#mobile/[#53](http://git.fasolti.com.br/avd/web/issues/53)) ([3d42656](http://git.fasolti.com.br/avd/web/commit/3d4265685d4cddd3ebb971e11adac645c95cd8d3))
* criar serviço REST para Apontamento (mobile/[#53](http://git.fasolti.com.br/avd/web/issues/53)) ([673590d](http://git.fasolti.com.br/avd/web/commit/673590d20cfce89b01c21a84711c5d4cc38b872d))
* Erro correto de login/senha apresentado ao usuário([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([b517bba](http://git.fasolti.com.br/avd/web/commit/b517bba661c43595c8d7d061a41cd0bc3e10cd58))
* **mobile; login:** adicionar dados de empresa, unidade e cargo ao REST do funcionário logado ([dc07f45](http://git.fasolti.com.br/avd/web/commit/dc07f45179666ee78b25ef0bf6407a1f883b00d7))
* **reestruturação:** método GET de avaliacao ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([8d6e4a9](http://git.fasolti.com.br/avd/web/commit/8d6e4a94e226cf1a6eb9fa2f33bda04b594bbca2))
* **reestruturação:** método GET de avaliacao ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([d98d5d7](http://git.fasolti.com.br/avd/web/commit/d98d5d75cec4ada1c2ac4403e4d6d86f0be5b58c))
* **reestruturação:** REST de FaleConosco e DeziBaseService ([#57](http://git.fasolti.com.br/avd/web/issues/57)) ([2ce47db](http://git.fasolti.com.br/avd/web/commit/2ce47dbb07815156f53e0134d75e8e67b7829e96))
* **reestruturação:** REST de FaleConosco e DeziBaseService ([#57](http://git.fasolti.com.br/avd/web/issues/57)) ([33fb1d1](http://git.fasolti.com.br/avd/web/commit/33fb1d10a70be1790d5e7e3bc0c3348e04930035))
* **reestruturação, ajuste:** método GET de todas as avaliações e ajuste na estrutura do FaleConosco ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([2b29200](http://git.fasolti.com.br/avd/web/commit/2b2920080b8f2fdca5a976f7b5587a05718222f8))
* **reestruturação, ajuste:** método GET de todas as avaliações e ajuste na estrutura do FaleConosco ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([2b00079](http://git.fasolti.com.br/avd/web/commit/2b00079208371b1bbb596c5e06ab6b8b42f2aecc))
* Abas "Por competências" e "Por Colaboradores", primeiro acesso tour específico (Bitrix 668). ([6107e1a](http://git.fasolti.com.br/avd/web/commit/6107e1a15a49139125de4eb8cd1a2f38f6063f14))
* Ajuste em hostController ([#99](http://git.fasolti.com.br/avd/web/issues/99)) ([9cbb3c4](http://git.fasolti.com.br/avd/web/commit/9cbb3c4133bcc64e414bc4f354005d57cd43106a))
* Ajuste na mensagem no tour de avaliações. ([78705d7](http://git.fasolti.com.br/avd/web/commit/78705d7d726933d28314197cb1bfc554c2d6465d))
* atualização controllers JS ([#92](http://git.fasolti.com.br/avd/web/issues/92) [#94](http://git.fasolti.com.br/avd/web/issues/94) [#95](http://git.fasolti.com.br/avd/web/issues/95)) ([0a97362](http://git.fasolti.com.br/avd/web/commit/0a97362bfedb4962208c3997b550cfbb993ac0ca))
* atualização controllers JS, resources e serviços de competencia ([#87](http://git.fasolti.com.br/avd/web/issues/87)) ([d35faa2](http://git.fasolti.com.br/avd/web/commit/d35faa2283400a8b42cdad8cf6b344b8294eaf24))
* Atualização do controller ([#94](http://git.fasolti.com.br/avd/web/issues/94)) ([a1b77ed](http://git.fasolti.com.br/avd/web/commit/a1b77ed8011b18fa33be378206e757cd5a1ef360))
* Atualização do controller ([#95](http://git.fasolti.com.br/avd/web/issues/95)) ([d939be0](http://git.fasolti.com.br/avd/web/commit/d939be07eaeaabe2a8e82bb4bb07afd0fb124c0b))
* Busca de pendencia por avaliacao usando projection ([#79](http://git.fasolti.com.br/avd/web/issues/79)) ([0856c36](http://git.fasolti.com.br/avd/web/commit/0856c369feb127ee856e87291a6902b9f6e0ebfa))
* Configuração conexão com Rabbitmq ([d049b37](http://git.fasolti.com.br/avd/web/commit/d049b37daf1711c53aeba64e17d10bab6d0ae0a2))
* Corrigida mensagem de erro para usuario ([#65](http://git.fasolti.com.br/avd/web/issues/65)) ([685fd71](http://git.fasolti.com.br/avd/web/commit/685fd711d594e630fa297237adace3f5aa6ce7f7))
* Criação de Entidade, TO, serviço e repositorio de LogAuth ([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([5025cc5](http://git.fasolti.com.br/avd/web/commit/5025cc5e978e43532ff29a26743fa08d1005cd9f))
* Criação dos serviços de logAuth ([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([fbc979f](http://git.fasolti.com.br/avd/web/commit/fbc979f120c75044a62bb8eeb606db7944a1919e))
* DEZI-14 > Avaliações > Aba "Por Competências" > Alterar Tela ([c503a62](http://git.fasolti.com.br/avd/web/commit/c503a629baf2a0c2304df64cc7eb91eb1962d556))
* DEZI-14 > Avaliações > Aba "Por Competências" > Alterar Tela ([ef5e76f](http://git.fasolti.com.br/avd/web/commit/ef5e76f9f6975bdd20fc1ec93124f1a25b0e756d))
* DEZI-14 > Avaliações > Aba "Por Competências" > Alterar Tela ([d09e546](http://git.fasolti.com.br/avd/web/commit/d09e5462b2babee0a419b55e4d76071737e2740b))
* DEZI-14 > Avaliações > Aba "Por Competências" > Alterar Tela ([c392eec](http://git.fasolti.com.br/avd/web/commit/c392eecc5f96bc64c14c707e80a847a35c78a2d1))
* Esqueci minha senha somente pesquisa por email ([#43](http://git.fasolti.com.br/avd/web/issues/43)) ([60a4db0](http://git.fasolti.com.br/avd/web/commit/60a4db0c23d074c66dc84f08adfe5aa1998f1ab8))
* Melhoria de senha ([#26](http://git.fasolti.com.br/avd/web/issues/26)) ([c58182c](http://git.fasolti.com.br/avd/web/commit/c58182cf311068afde72a0b0a5082951b795ae08))
* Melhorias na tela de esqueceu a senha ([#43](http://git.fasolti.com.br/avd/web/issues/43)) ([8a0069c](http://git.fasolti.com.br/avd/web/commit/8a0069c53bbce1bc82fc2eb89b72d6daa53cc9ae))
* Número de tentativas de login e bloqueio de IP ([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([e4539d7](http://git.fasolti.com.br/avd/web/commit/e4539d75e87cca7fcf0effe2da14dc25f6f1c5f9))
* organização dos imports ([1f82e15](http://git.fasolti.com.br/avd/web/commit/1f82e155972bbce1ac3d99b0741cce94b1b96262))
* Refatoracao Cadastros Escalas de Graduacao ([#85](http://git.fasolti.com.br/avd/web/issues/85)) ([f19d2de](http://git.fasolti.com.br/avd/web/commit/f19d2de6cb81db51a24c5fdbf474ca9c5099695c))
* Refatoracao funcionario para Avaliacao ([#80](http://git.fasolti.com.br/avd/web/issues/80)) ([c2797c4](http://git.fasolti.com.br/avd/web/commit/c2797c44165628c5fa89727d986c877bbbbfea15))
* Refatoracao metodos MensageriaServiceImpl ([#81](http://git.fasolti.com.br/avd/web/issues/81)) ([b2857ad](http://git.fasolti.com.br/avd/web/commit/b2857adfc053e4183aacac418f883a17ac641f73))
* Refatoracao parametroTO ([#98](http://git.fasolti.com.br/avd/web/issues/98)) ([5e59758](http://git.fasolti.com.br/avd/web/commit/5e59758c4a7510e456557b7aa86b7aa5fa11c137))
* Refatorado menu Acompanhamentos ([#75](http://git.fasolti.com.br/avd/web/issues/75)) ([77923c1](http://git.fasolti.com.br/avd/web/commit/77923c112d7b416b149cc6b523f4972d75c52dc1))
* Refatorado Serviços e js controller do tipoCompetencia ([#86](http://git.fasolti.com.br/avd/web/issues/86)) ([ded12c6](http://git.fasolti.com.br/avd/web/commit/ded12c6ccace10a418b2df1b1fa33b3d1c94a16b))
* Refatorar os TOs do menu Relatórios([#84](http://git.fasolti.com.br/avd/web/issues/84)) ([586aa6a](http://git.fasolti.com.br/avd/web/commit/586aa6a7adff3a9519c93cf8ab1ebc8f379f71f4))
* **RabbitmqConfig:** Configuração RabbitMq ([075b03a](http://git.fasolti.com.br/avd/web/commit/075b03a1ddd119fcd543d55c71c718b4480feeec))
* Utilização método serviço para prazer somente informação TO necessária  ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([629858f](http://git.fasolti.com.br/avd/web/commit/629858f81de441629847a86397faa33a5c156640))


### Bug Fixes

* **aba pendencias e aba avaliadores da avaliacao:** Ajuste na importação de pares na aba de avaliadores e ajuste no projections das pendencias da avaliação. ([dbfe4ae](http://git.fasolti.com.br/avd/web/commit/dbfe4ae934af2d7130cf2c6e034cd4dd918819ec))
* **acompanhamento, mensagem:** Ajuste no envio de mensagem do modulo de acompanhamentos ([#178](http://git.fasolti.com.br/avd/web/issues/178)) ([8fd7a72](http://git.fasolti.com.br/avd/web/commit/8fd7a72cfac5a8c79d324da49e7c5e27ba0476ea))
* **allowedMethod:** Metodo DELETE no allowedMethod ([56084d4](http://git.fasolti.com.br/avd/web/commit/56084d4e3f17ccdfe729d0652fefe481b1579bb2))
* **Avaliacao:** Estava usando save no lugar de update, duplicando a lista. ([556414f](http://git.fasolti.com.br/avd/web/commit/556414f20f498df9583492908864db0e2fe6d76c))
* **Avaliacao:** if que quando avaliação tem status concluida lançava erro ([4e5ad6e](http://git.fasolti.com.br/avd/web/commit/4e5ad6e711048f9a1acc44f934bef4b3cb4d029c))
* **Avaliacao:** if que quando avaliação tem status concluida lançava erro ([2da14cc](http://git.fasolti.com.br/avd/web/commit/2da14cc5c1e944a3df33f6ccb28131725e83c120))
* **bi:** construir corretamente o nome de usuário para acesso ao BI ([89258ae](http://git.fasolti.com.br/avd/web/commit/89258ae1a9ea0b4f1f75a58cb1e333fd58809396))
* **bi:** corrigir construção do nome de usuário para login, remover credenciais da mensagem de erro ([911b642](http://git.fasolti.com.br/avd/web/commit/911b642053d203c80d5dec6b77afdf2a2d26b172))
* **bi:** externalizar dados de acesso ao BI para arquivo de propriedades ([8b641fb](http://git.fasolti.com.br/avd/web/commit/8b641fb715e957c374f25d8635d7c451a4680d05))
* **bi:** externalizar dados de acesso ao BI para arquivo de propriedades ([3403698](http://git.fasolti.com.br/avd/web/commit/3403698009e459b4971be0998da5788269bff0d8))
* **ci:** adicionar tags ([81f277e](http://git.fasolti.com.br/avd/web/commit/81f277ed89d4892d9bd4cebcb60cebd7c4e0861b))
* **ci:** adicionar tags ([d009fad](http://git.fasolti.com.br/avd/web/commit/d009fad8457a32cb375046d1d6ce910e409be656))
* **ci:** diminuir verbosidade do build Maven ([55ba7a4](http://git.fasolti.com.br/avd/web/commit/55ba7a4c7784cd37807493b226e3d342a5a69bee))
* **ci:** transferir labels para o Dockerfile ([a564713](http://git.fasolti.com.br/avd/web/commit/a56471376790be7360bccfed7f3c2e4adf5b49e7))
* **ci/Dockerfile:** atribuir timezone correto às imagens ([6ba0d40](http://git.fasolti.com.br/avd/web/commit/6ba0d407554ed976c94cac6aa553a313a3e8122b))
* **ci/Dockerfile:** executar atualizações antes da cópia dos artefatos ([d78d5e6](http://git.fasolti.com.br/avd/web/commit/d78d5e6471fc5399ae3c2dd6c9944bbc53e642d9))
* **ci/Dockerfile:** remover configuração manual de timezone e hora local ([dc1f88d](http://git.fasolti.com.br/avd/web/commit/dc1f88d35fb022611549f4b03f6b98f9151d4f03))
* **ci/Dockerfile:** remover locale-gen e informações de linguagem ([8cd3168](http://git.fasolti.com.br/avd/web/commit/8cd316879194df98ea0ed3dabc91a7a4b299ca65))
* **cleanup:** Limpeza de codigo e ajuste na visualizacao do PDI ([#140](http://git.fasolti.com.br/avd/web/issues/140)) ([0fa7102](http://git.fasolti.com.br/avd/web/commit/0fa71023914f72d1a507fdcfd504a06acf52f290))
* **dependencies:** Ajuste nas dependencias do jasper ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([366604f](http://git.fasolti.com.br/avd/web/commit/366604f517aff92585b12beac8203fe1949b3cb2))
* **endpoints:** Ajuste nos endpoints de cargo, competencia, participante e detalhamentos, onde estavam recebendo objetos e não ID's e ajuste na requisição HTTP ([#131](http://git.fasolti.com.br/avd/web/issues/131)) ([1c3f132](http://git.fasolti.com.br/avd/web/commit/1c3f1324f5a3749ee8a0d49d9dd89423ae8af4ed))
* **filter; roles:** Ajuste no filtro de consultor de RH ([#4](http://git.fasolti.com.br/avd/web/issues/4)) ([e758250](http://git.fasolti.com.br/avd/web/commit/e7582504acfbb9ec42194ee7f8377483e2cf4d62))
* **foto pdi:** Usando o serviço de avatar do mobile para obter as fotos dos funcionarios ([#4](http://git.fasolti.com.br/avd/web/issues/4)) ([e93baf1](http://git.fasolti.com.br/avd/web/commit/e93baf1ed1d3586d7a57964f395ffd2f172cddc7))
* **funcionario:** Ao criar funcionario não cria configuracao ([451c194](http://git.fasolti.com.br/avd/web/commit/451c194d4fbf7820e9289a5e76fde0df37365372))
* **FuncionarioResource:** corrigir typo no nome da @PathVariable ([9cc229b](http://git.fasolti.com.br/avd/web/commit/9cc229b4568bc4a9d40ed309ed83eabe2dd57c63))
* **impressao relatorio PDI:** Impressão de relátorio do PDI em PDF ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([f2b7513](http://git.fasolti.com.br/avd/web/commit/f2b75135d7152f706d204856ae75ae65ba971a38))
* **internacionalizacao configuration:** ajuste ao salvar a configuração do usuario logado. ([f12eb4c](http://git.fasolti.com.br/avd/web/commit/f12eb4ce45a2e48388e9bd2028627a810b422d8c))
* **jasper PDF:** Ajuste no periodo de desenvolvimento ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([2e02d00](http://git.fasolti.com.br/avd/web/commit/2e02d00b6115b3f46ca1bbb3210d45726cb00cca))
* **liberar avaliacao:** Cargo do avaliado não estava sendo setado ao liberar pela lista de avaliações na campanha ([#186](http://git.fasolti.com.br/avd/web/issues/186)) ([391edf7](http://git.fasolti.com.br/avd/web/commit/391edf72951e07dd9fda3db00f2d152a77224e46))
* **liberar avaliacao:** Mensagem de validação ao liberar uma avaliação ([#184](http://git.fasolti.com.br/avd/web/issues/184)) ([dc7270c](http://git.fasolti.com.br/avd/web/commit/dc7270cd6537c3fe604ef867361e8ebdae25ae71))
* **login:** usar headers Origin, Referer e Host para determinar host da lista de empresas ([e622779](http://git.fasolti.com.br/avd/web/commit/e622779afba3ed8dc33060556a267993c671ed85))
* **maven dependency:** inserção do olap4j ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([5d0a628](http://git.fasolti.com.br/avd/web/commit/5d0a628b5131f81693059c21cfa9782b29f257cc))
* **Mensagens:** Retirado implementação de UserDetails de UserTo que causa erro. ([ae73b38](http://git.fasolti.com.br/avd/web/commit/ae73b38f9b789dadc773fd9720d879c387ff25b1))
* **MQ:** Variáveis rabbitmq externas ([c73ed21](http://git.fasolti.com.br/avd/web/commit/c73ed21f26c123bce1a40cfc8d499fe78a52f6fb))
* **notify duplicate:** Ajuste do toastr para informar erros de validação provenientes do backend ([#197](http://git.fasolti.com.br/avd/web/issues/197), [#198](http://git.fasolti.com.br/avd/web/issues/198), [#191](http://git.fasolti.com.br/avd/web/issues/191), [#190](http://git.fasolti.com.br/avd/web/issues/190), [#195](http://git.fasolti.com.br/avd/web/issues/195), [#189](http://git.fasolti.com.br/avd/web/issues/189)) ([1614450](http://git.fasolti.com.br/avd/web/commit/16144503ab54158a3e8555d3b6b74a9c5800c2d4))
* **participantes:** Estava criando novos participantes para uma avaliação sem modificação ([e0b4524](http://git.fasolti.com.br/avd/web/commit/e0b4524c68c6c4aadea25243f45fa3bf41ecaa77))
* **path report:** Ajuste no path do report ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([dbbef45](http://git.fasolti.com.br/avd/web/commit/dbbef459c2e73a0a1f5d18e10ac6373fb2feacf3))
* **pdi:** Ajuste de regras no pdi ([#140](http://git.fasolti.com.br/avd/web/issues/140)) ([7f22e02](http://git.fasolti.com.br/avd/web/commit/7f22e024399b7053302676bea20002f678157155))
* **PDI:** avaliacaoParticipante pode ser nulo ([a9f405b](http://git.fasolti.com.br/avd/web/commit/a9f405bec0c21029bb38e06ef1f18d69698e92c3))
* **PDI:** Correçao SQL flyway PDI ([5c483ee](http://git.fasolti.com.br/avd/web/commit/5c483ee77b840926e483375f6e06ec122f97fcf7))
* **PDI:** Corrigido erro de mapeamento ([a58c907](http://git.fasolti.com.br/avd/web/commit/a58c907512b4bccf7aa689c967d91e2a0a502a89))
* **PDI:** SQL flyway, avaliacaoParticipante pode ser nulo ([e5d5436](http://git.fasolti.com.br/avd/web/commit/e5d543614e7e11c56d5a2db5c88c4d8564ad4a12))
* **performance pdi:** Carregar apenas a aba selecionada ([#140](http://git.fasolti.com.br/avd/web/issues/140)) ([be3ead4](http://git.fasolti.com.br/avd/web/commit/be3ead4716b5c7f8836f01feee77e558c6195aef))
* **persistencia liberar avaliacao:** Propagation ao liberar uma avaliação ([#178](http://git.fasolti.com.br/avd/web/issues/178)) ([a0d3cd5](http://git.fasolti.com.br/avd/web/commit/a0d3cd5fb3e86c16ae1f032f64fc28c640ff766f))
* **relatorio, melhorias:** Ajuste na query e melhorias ao gerar um relatorio ([#147](http://git.fasolti.com.br/avd/web/issues/147)) ([923b03b](http://git.fasolti.com.br/avd/web/commit/923b03b935e06f435650304a474b6e5cfd46720e))
* **report:** Ajuste do relatório e adição da logo ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([a681d3d](http://git.fasolti.com.br/avd/web/commit/a681d3d33451c8cbb78ab8f3d9b5c5ad0d9fda0f))
* **report:** Ajuste nos objetos nulos da pesquisa do relatorio ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([79d6bf4](http://git.fasolti.com.br/avd/web/commit/79d6bf428e9954ad02e7fc08eb2f7e86eb0c8b31))
* **report:** Dia atual no rodape em portugues([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([9e4fc5a](http://git.fasolti.com.br/avd/web/commit/9e4fc5a61ee1ce9c38c4551653889dc35c41691e))
* **report:** Dia atual no rodape em portugues([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([3d5eeff](http://git.fasolti.com.br/avd/web/commit/3d5eeff4e08f2e450115c9ddb7987bfb40c49d22))
* **report:** Dia atual no rodape em portugues([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([760c405](http://git.fasolti.com.br/avd/web/commit/760c4056408558ff9275edeffa7c0b0daded4d55))
* **tomcat8:** apagar a pasta ROOT (para forçar o uso do ROOT.war) ([ba215bd](http://git.fasolti.com.br/avd/web/commit/ba215bd51244defe738a6b1957d3158243bd3f10))
* atualizar tzdata(e demais pacotesa nível de sistema operacional) ([f085cc2](http://git.fasolti.com.br/avd/web/commit/f085cc27bb442050151d1a627acbe339143d729b))
* **adjust:** Ajuste no pom que subiu sem querer ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([c578545](http://git.fasolti.com.br/avd/web/commit/c5785457cc79f43a92d7a5cebdaef4c8f2e7fe46))
* **adjusts:** Ajuste ao vir um objeto sem foto ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([45c4b2c](http://git.fasolti.com.br/avd/web/commit/45c4b2c61dd6e415d1d27ee1b2b4b727be7a8d25))
* **Email:** EmailControlerService não estava pegando usuário logado ([f3647aa](http://git.fasolti.com.br/avd/web/commit/f3647aa48eacd1e678bd036ba56f06fa8e7ee0f2))
* **Email:** Envio de eamil de esqueci minha senha não vai procurar por usuário logado ([5c7c8ed](http://git.fasolti.com.br/avd/web/commit/5c7c8ed0d1d9e9c0723e5a382ef689a9ddc08d27))
* Log erro envio de email ([1e382d6](http://git.fasolti.com.br/avd/web/commit/1e382d6b8cc7ce3cbbdf1a3c91ef0139ecc25e88))
* **adjusts, graduacao:** Ajuste na graduação mobile ([#92](http://git.fasolti.com.br/avd/web/issues/92)) ([2dabe65](http://git.fasolti.com.br/avd/web/commit/2dabe6571461e8c341a0f741de7bd71540101100))
* **Email:** Link montado com host e ID da empresa do funcionário ([1b64dd1](http://git.fasolti.com.br/avd/web/commit/1b64dd1aa36e39f31b6b37eb63f6f1ffba3a3ebb))
* **query, endpoint:** Ajustes em alguns nullPointers, na query de calculo da porcentagem da competencia e no endpoint do update detalhamentos ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([21f4a82](http://git.fasolti.com.br/avd/web/commit/21f4a82f3588a24f4af9a2dfb02f20870ac47d30))
* **sort:** Ordenação por nome do avaliado nos apontamentos/feedforwards ([#92](http://git.fasolti.com.br/avd/web/issues/92), [#4](http://git.fasolti.com.br/avd/web/issues/4), [#33](http://git.fasolti.com.br/avd/web/issues/33)) ([4a0e353](http://git.fasolti.com.br/avd/web/commit/4a0e353df35034c356a20b1cd17d65824afd58db))
* correção mensagem de erro na tela de login ([9225f3d](http://git.fasolti.com.br/avd/web/commit/9225f3d44ff8aaafdf74e47fba19f4035cdabf79))
* **adjust, participante:** Campo de nome do avaliado e do avaliador na query. Ajuste na ordenação da avTipoCompetencia ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([06f48c9](http://git.fasolti.com.br/avd/web/commit/06f48c9f898932c9cd6ae427a7042629a6f08d2b))
* **adjust, sort:** Ordenação por descrição em avaliacaoTipoCompetencia e competencias, e nos respectivos participantes e graduação, pelo campo de ordem, dada uma competencia. ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([f0e27cb](http://git.fasolti.com.br/avd/web/commit/f0e27cb19d6ee38eed4341dbfe58cd7769908d68))
* **consenso:** Ajuste no consenso liberado mobile. ([#92](http://git.fasolti.com.br/avd/web/issues/92), [#5](http://git.fasolti.com.br/avd/web/issues/5)) ([e1b9421](http://git.fasolti.com.br/avd/web/commit/e1b942119373970d36f51596759fec97fa484a47))
* **graduacao, endpoint:** Filtro na graduação, estava adicionando a mesma graduação na lista. Troca do endpoint de competencia ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([abf9b53](http://git.fasolti.com.br/avd/web/commit/abf9b5318085911a2198a7ec4258154a7cf744c2))
* **mobile:views/avaliacao/view:** ignorar preenchimento de dados de graduação se não houver AV1/AV2 associadas (mobile/[#90](http://git.fasolti.com.br/avd/web/issues/90)) ([b72265a](http://git.fasolti.com.br/avd/web/commit/b72265a2de37697d72b1151b840ab102021d9b44))
* **sort:** Ajuste na ordenação do detalhamento ([#92](http://git.fasolti.com.br/avd/web/issues/92)) ([83b7ab1](http://git.fasolti.com.br/avd/web/commit/83b7ab19166349fd0e09bb269be281cf20764955))
* **upload:** Adicionado, novamente, multipartResolver em applicationContext ([c718944](http://git.fasolti.com.br/avd/web/commit/c7189448ad85e7668c3f22529bb3ba79a4a1178d))
* **validation:** Validação ao errar o email no 'esqueci minha senha' ([b48bd36](http://git.fasolti.com.br/avd/web/commit/b48bd36e5b8005e0228f2ae6bb8f5c28415c9da4))
* **views/feedforwards:** adequar a exibição de dados às regras de apontamento/avaliação (mobile/[#55](http://git.fasolti.com.br/avd/web/issues/55)) ([0e19fb2](http://git.fasolti.com.br/avd/web/commit/0e19fb2a8f1911003473d648f268527ff1d0720d))
* Adicionado ordem na projections em listByCompetencia ([62f0ebd](http://git.fasolti.com.br/avd/web/commit/62f0ebd7549b3208d9ee4f9b37d8dc2d4b85d951))
* Adiconado atributo a projection e retirado validação de nulo para obj obrigatório ([09aed1a](http://git.fasolti.com.br/avd/web/commit/09aed1afa34022b3a111a2624eb37ece99a4956f))
* ajuste no deziService ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([0776b69](http://git.fasolti.com.br/avd/web/commit/0776b69d8dd68e0c995a3a7c27c2ac1b9b731025))
* Correção arquivo de migração do flyway ([45e5bc8](http://git.fasolti.com.br/avd/web/commit/45e5bc8f79f3f30c481789f4695bf2303d7e6777))
* correção da mensagem de erro para senha informada não confere com  atual cadastrada ([#65](http://git.fasolti.com.br/avd/web/issues/65)) ([8de457e](http://git.fasolti.com.br/avd/web/commit/8de457ef9c07c7e6fa3977f598868c246d7d9acd))
* Correção de erro ao desativar competencia ([b3be66d](http://git.fasolti.com.br/avd/web/commit/b3be66d70ea5b0ca455d754afcb4a3d504e2f315))
* Correção do '[$injector:nomod] Module 'app.reports' is not available!'. ([ec496c6](http://git.fasolti.com.br/avd/web/commit/ec496c6514426d8f353e4f8f7c04c4760c46c11e))
* Correção do nome da coluna ip da entity LogAuth ([a8abff0](http://git.fasolti.com.br/avd/web/commit/a8abff0c5c4e1e5e643a57cba3252dabe8d71a0a))
* Correção do nome validation ip da entity LogAuthTO ([cb5594f](http://git.fasolti.com.br/avd/web/commit/cb5594f08e1866a6ef440b69451f1a655d9ab9c2))
* Correção do script para o dialog de mensagem do freshdesk. ([fed4f53](http://git.fasolti.com.br/avd/web/commit/fed4f53d013bc02eb87f02c11e44706ccf6df17e))
* Correção na projections de detalhamento ([0c0657a](http://git.fasolti.com.br/avd/web/commit/0c0657a856befb4cec0cd88ad2c9acc66b92a302))
* Correção no tour de avaliação e consenso (bitrix 667). ([d03e05b](http://git.fasolti.com.br/avd/web/commit/d03e05bbc2c3a71f99d9ab99f01f240f43a5cbaf))
* Correção no tour de avaliação e consenso (bitrix 667). Para o campo  comentário. ([1568586](http://git.fasolti.com.br/avd/web/commit/1568586d666cd0461e2d73a14dd66ca0313c3f3c))
* Correção pom.xml ([ca5c67e](http://git.fasolti.com.br/avd/web/commit/ca5c67eb3f787937e8917ef4e2546eaee6667f7f))
* Corrigindo no tour na exibição da avaliação. ([5db9475](http://git.fasolti.com.br/avd/web/commit/5db9475037bfa31c86144f6308353a426ff0af9d))
* Corrigindo o caminho de pão da tela de avalição de consenso (661). ([d3f7114](http://git.fasolti.com.br/avd/web/commit/d3f71141c597863d8618ab2d8a57bdb29be28ee8))
* Erro ao enviar mensagem em cadastro de avaliação ([02b7c3e](http://git.fasolti.com.br/avd/web/commit/02b7c3eb21b56114e5fa62b8c85adbd355e0366c))
* Remocao coluna empresa visivel de configura ([ff25f90](http://git.fasolti.com.br/avd/web/commit/ff25f90e5864c2dfadbb2e87a3470107360f3916))
* **rest/mobile/FeedForwardRecebido:** adequar ao framework do Dezi (BaseDeziService, TransferObject) (mobile/[#56](http://git.fasolti.com.br/avd/web/issues/56)) ([dec2da9](http://git.fasolti.com.br/avd/web/commit/dec2da9f9a3e2b6592e95fe3b7c22da599ca5b2b))
* Correção na tela de preenchimento por competência e tour. ([c5449cc](http://git.fasolti.com.br/avd/web/commit/c5449cce7b55b52c56759a4418ad50e4789861b0))
* Correção no angular-slick-carousel, na tela de avaliações, erro de versionamento. ([5a0f402](http://git.fasolti.com.br/avd/web/commit/5a0f40295b2f5afe3d1139be710955820dbaff70))
* Correção no slick, na tela de avaliações. ([d6347ea](http://git.fasolti.com.br/avd/web/commit/d6347ea77a180d455d3578a1aabc6e72932136d4))
* Correção no tour na tela de avaliações aba funcionário ([cd1a013](http://git.fasolti.com.br/avd/web/commit/cd1a013c7cc84be50b8582705a212e366d7446c0))
* Correção no tour na tela de Campanhas > Campanha > Avaliação ([bdcec13](http://git.fasolti.com.br/avd/web/commit/bdcec13275f921f64939dc583a50a4e13dfdea83))
* Correção no tour na tela de consensos ([3cc9ef1](http://git.fasolti.com.br/avd/web/commit/3cc9ef14ccc96734db108ea0f9d8de51e3adcdf3))
* Correção no versionamento dos plugins bower angular-slick, angular-slick-carousel e slick-carousel ([c9e7a6f](http://git.fasolti.com.br/avd/web/commit/c9e7a6f0f59f2a46c51d52b4d5a9ce5cc07874f8))
* Correção tour na tela de acompanhamento. ([80d15f2](http://git.fasolti.com.br/avd/web/commit/80d15f2aaf52ac91aae892186a6b1a7f33df2b06))
* Corrigido 500 ao troca a senha ([#26](http://git.fasolti.com.br/avd/web/issues/26)) ([f8c916c](http://git.fasolti.com.br/avd/web/commit/f8c916c6f385a9279361fd8a7914c99f105a89b9))
* Corrigido status de competencia ausente ([867956b](http://git.fasolti.com.br/avd/web/commit/867956beb3a70a4ff9d78756a1c1d533e88fc6d3))
* Erro converter funcionarioSimpleTO ([867380d](http://git.fasolti.com.br/avd/web/commit/867380d1221d9eba8e986c3f3aefd6b428ffa3f8))
* issue [#137](http://git.fasolti.com.br/avd/web/issues/137) ([dacc6a4](http://git.fasolti.com.br/avd/web/commit/dacc6a4fc11a3fe290f2f9342880405f9150d4a4))
* merge com development ([e2175d1](http://git.fasolti.com.br/avd/web/commit/e2175d15436fd4b08619b206cb86fce17c8b9a30))
* Retirado classes causando erro no build ([f1619dc](http://git.fasolti.com.br/avd/web/commit/f1619dc8af27d2288ef197fc358555816dace4f3))
* Retirado coluna visualizar_por_unidade não usada ([94dbfe2](http://git.fasolti.com.br/avd/web/commit/94dbfe232e73b75dea22724c22cc69205ff4083b))
* Tarefa 662, correção do tour avaliação por competência. ([b511c11](http://git.fasolti.com.br/avd/web/commit/b511c1146c111dccdd403c3ab2c93841224db52e))
* Tarefa 663, correção do tour de avaliação do coloborador. ([24b0837](http://git.fasolti.com.br/avd/web/commit/24b083763b49a8735ff539a5a4829e3b9a5f8eb6))
* Tarefa 665, correção do tour de nova/edição/view de avaliação. ([52b80aa](http://git.fasolti.com.br/avd/web/commit/52b80aa6c7785b471d6828e06c67f07505e1ff3a))

# [3.1.0](http://git.fasolti.com.br/avd/web/compare/v0.9.0-Master...v3.1.0) (2019-11-08)


### Bug Fixes

* Adicionado ordem na projections em listByCompetencia ([62f0ebd](http://git.fasolti.com.br/avd/web/commits/62f0ebd))
* Adiconado atributo a projection e retirado validação de nulo para obj obrigatório ([09aed1a](http://git.fasolti.com.br/avd/web/commits/09aed1a))
* ajuste no deziService ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([0776b69](http://git.fasolti.com.br/avd/web/commits/0776b69))
* Correção arquivo de migração do flyway ([45e5bc8](http://git.fasolti.com.br/avd/web/commits/45e5bc8))
* correção da mensagem de erro para senha informada não confere com  atual cadastrada ([#65](http://git.fasolti.com.br/avd/web/issues/65)) ([8de457e](http://git.fasolti.com.br/avd/web/commits/8de457e))
* Correção de erro ao desativar competencia ([b3be66d](http://git.fasolti.com.br/avd/web/commits/b3be66d))
* Correção do '[$injector:nomod] Module 'app.reports' is not available!'. ([ec496c6](http://git.fasolti.com.br/avd/web/commits/ec496c6))
* Correção do nome da coluna ip da entity LogAuth ([a8abff0](http://git.fasolti.com.br/avd/web/commits/a8abff0))
* Correção do nome validation ip da entity LogAuthTO ([cb5594f](http://git.fasolti.com.br/avd/web/commits/cb5594f))
* Correção do script para o dialog de mensagem do freshdesk. ([fed4f53](http://git.fasolti.com.br/avd/web/commits/fed4f53))
* correção mensagem de erro na tela de login ([9225f3d](http://git.fasolti.com.br/avd/web/commits/9225f3d))
* **adjust, participante:** Campo de nome do avaliado e do avaliador na query. Ajuste na ordenação da avTipoCompetencia ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([06f48c9](http://git.fasolti.com.br/avd/web/commits/06f48c9))
* Correção na projections de detalhamento ([0c0657a](http://git.fasolti.com.br/avd/web/commits/0c0657a))
* Correção na tela de preenchimento por competência e tour. ([c5449cc](http://git.fasolti.com.br/avd/web/commits/c5449cc))
* Correção no angular-slick-carousel, na tela de avaliações, erro de versionamento. ([5a0f402](http://git.fasolti.com.br/avd/web/commits/5a0f402))
* Correção no slick, na tela de avaliações. ([d6347ea](http://git.fasolti.com.br/avd/web/commits/d6347ea))
* Correção no tour de avaliação e consenso (bitrix 667). ([d03e05b](http://git.fasolti.com.br/avd/web/commits/d03e05b))
* Correção no tour de avaliação e consenso (bitrix 667). Para o campo  comentário. ([1568586](http://git.fasolti.com.br/avd/web/commits/1568586))
* Correção no tour na tela de avaliações aba funcionário ([cd1a013](http://git.fasolti.com.br/avd/web/commits/cd1a013))
* Correção no tour na tela de Campanhas > Campanha > Avaliação ([bdcec13](http://git.fasolti.com.br/avd/web/commits/bdcec13))
* Correção no tour na tela de consensos ([3cc9ef1](http://git.fasolti.com.br/avd/web/commits/3cc9ef1))
* Correção no versionamento dos plugins bower angular-slick, angular-slick-carousel e slick-carousel ([c9e7a6f](http://git.fasolti.com.br/avd/web/commits/c9e7a6f))
* **aba pendencias e aba avaliadores da avaliacao:** Ajuste na importação de pares na aba de avaliadores e ajuste no projections das pendencias da avaliação. ([dbfe4ae](http://git.fasolti.com.br/avd/web/commits/dbfe4ae))
* **acompanhamento, mensagem:** Ajuste no envio de mensagem do modulo de acompanhamentos ([#178](http://git.fasolti.com.br/avd/web/issues/178)) ([8fd7a72](http://git.fasolti.com.br/avd/web/commits/8fd7a72))
* **adjust:** Ajuste no pom que subiu sem querer ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([c578545](http://git.fasolti.com.br/avd/web/commits/c578545))
* **adjust, sort:** Ordenação por descrição em avaliacaoTipoCompetencia e competencias, e nos respectivos participantes e graduação, pelo campo de ordem, dada uma competencia. ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([f0e27cb](http://git.fasolti.com.br/avd/web/commits/f0e27cb))
* **adjusts:** Ajuste ao vir um objeto sem foto ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([45c4b2c](http://git.fasolti.com.br/avd/web/commits/45c4b2c))
* **adjusts, graduacao:** Ajuste na graduação mobile ([#92](http://git.fasolti.com.br/avd/web/issues/92)) ([2dabe65](http://git.fasolti.com.br/avd/web/commits/2dabe65))
* **allowedMethod:** Metodo DELETE no allowedMethod ([56084d4](http://git.fasolti.com.br/avd/web/commits/56084d4))
* **Avaliacao:** Estava usando save no lugar de update, duplicando a lista. ([556414f](http://git.fasolti.com.br/avd/web/commits/556414f))
* **Avaliacao:** if que quando avaliação tem status concluida lançava erro ([4e5ad6e](http://git.fasolti.com.br/avd/web/commits/4e5ad6e))
* **Avaliacao:** if que quando avaliação tem status concluida lançava erro ([2da14cc](http://git.fasolti.com.br/avd/web/commits/2da14cc))
* **consenso:** Ajuste no consenso liberado mobile. ([#92](http://git.fasolti.com.br/avd/web/issues/92), [#5](http://git.fasolti.com.br/avd/web/issues/5)) ([e1b9421](http://git.fasolti.com.br/avd/web/commits/e1b9421))
* **Email:** EmailControlerService não estava pegando usuário logado ([f3647aa](http://git.fasolti.com.br/avd/web/commits/f3647aa))
* **Email:** Envio de eamil de esqueci minha senha não vai procurar por usuário logado ([5c7c8ed](http://git.fasolti.com.br/avd/web/commits/5c7c8ed))
* **Email:** Link montado com host e ID da empresa do funcionário ([1b64dd1](http://git.fasolti.com.br/avd/web/commits/1b64dd1))
* **endpoints:** Ajuste nos endpoints de cargo, competencia, participante e detalhamentos, onde estavam recebendo objetos e não ID's e ajuste na requisição HTTP ([#131](http://git.fasolti.com.br/avd/web/issues/131)) ([1c3f132](http://git.fasolti.com.br/avd/web/commits/1c3f132))
* **filter; roles:** Ajuste no filtro de consultor de RH ([#4](http://git.fasolti.com.br/avd/web/issues/4)) ([e758250](http://git.fasolti.com.br/avd/web/commits/e758250))
* **FuncionarioResource:** corrigir typo no nome da @PathVariable ([9cc229b](http://git.fasolti.com.br/avd/web/commits/9cc229b))
* **graduacao, endpoint:** Filtro na graduação, estava adicionando a mesma graduação na lista. Troca do endpoint de competencia ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([abf9b53](http://git.fasolti.com.br/avd/web/commits/abf9b53))
* **internacionalizacao configuration:** ajuste ao salvar a configuração do usuario logado. ([f12eb4c](http://git.fasolti.com.br/avd/web/commits/f12eb4c))
* **liberar avaliacao:** Mensagem de validação ao liberar uma avaliação ([#184](http://git.fasolti.com.br/avd/web/issues/184)) ([dc7270c](http://git.fasolti.com.br/avd/web/commits/dc7270c))
* **Mensagens:** Retirado implementação de UserDetails de UserTo que causa erro. ([ae73b38](http://git.fasolti.com.br/avd/web/commits/ae73b38))
* **mobile:views/avaliacao/view:** ignorar preenchimento de dados de graduação se não houver AV1/AV2 associadas (mobile/[#90](http://git.fasolti.com.br/avd/web/issues/90)) ([b72265a](http://git.fasolti.com.br/avd/web/commits/b72265a))
* **MQ:** Variáveis rabbitmq externas ([c73ed21](http://git.fasolti.com.br/avd/web/commits/c73ed21))
* **participantes:** Estava criando novos participantes para uma avaliação sem modificação ([e0b4524](http://git.fasolti.com.br/avd/web/commits/e0b4524))
* **PDI:** avaliacaoParticipante pode ser nulo ([a9f405b](http://git.fasolti.com.br/avd/web/commits/a9f405b))
* **PDI:** Correçao SQL flyway PDI ([5c483ee](http://git.fasolti.com.br/avd/web/commits/5c483ee))
* **PDI:** Corrigido erro de mapeamento ([a58c907](http://git.fasolti.com.br/avd/web/commits/a58c907))
* **PDI:** SQL flyway, avaliacaoParticipante pode ser nulo ([e5d5436](http://git.fasolti.com.br/avd/web/commits/e5d5436))
* **persistencia liberar avaliacao:** Propagation ao liberar uma avaliação ([#178](http://git.fasolti.com.br/avd/web/issues/178)) ([a0d3cd5](http://git.fasolti.com.br/avd/web/commits/a0d3cd5))
* **query, endpoint:** Ajustes em alguns nullPointers, na query de calculo da porcentagem da competencia e no endpoint do update detalhamentos ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([21f4a82](http://git.fasolti.com.br/avd/web/commits/21f4a82))
* **relatorio, melhorias:** Ajuste na query e melhorias ao gerar um relatorio ([#147](http://git.fasolti.com.br/avd/web/issues/147)) ([923b03b](http://git.fasolti.com.br/avd/web/commits/923b03b))
* **rest/mobile/FeedForwardRecebido:** adequar ao framework do Dezi (BaseDeziService, TransferObject) (mobile/[#56](http://git.fasolti.com.br/avd/web/issues/56)) ([dec2da9](http://git.fasolti.com.br/avd/web/commits/dec2da9))
* **sort:** Ajuste na ordenação do detalhamento ([#92](http://git.fasolti.com.br/avd/web/issues/92)) ([83b7ab1](http://git.fasolti.com.br/avd/web/commits/83b7ab1))
* **sort:** Ordenação por nome do avaliado nos apontamentos/feedforwards ([#92](http://git.fasolti.com.br/avd/web/issues/92), [#4](http://git.fasolti.com.br/avd/web/issues/4), [#33](http://git.fasolti.com.br/avd/web/issues/33)) ([4a0e353](http://git.fasolti.com.br/avd/web/commits/4a0e353))
* **upload:** Adicionado, novamente, multipartResolver em applicationContext ([c718944](http://git.fasolti.com.br/avd/web/commits/c718944))
* Correção pom.xml ([ca5c67e](http://git.fasolti.com.br/avd/web/commits/ca5c67e))
* Correção tour na tela de acompanhamento. ([80d15f2](http://git.fasolti.com.br/avd/web/commits/80d15f2))
* Corrigido 500 ao troca a senha ([#26](http://git.fasolti.com.br/avd/web/issues/26)) ([f8c916c](http://git.fasolti.com.br/avd/web/commits/f8c916c))
* Corrigido status de competencia ausente ([867956b](http://git.fasolti.com.br/avd/web/commits/867956b))
* Corrigindo no tour na exibição da avaliação. ([5db9475](http://git.fasolti.com.br/avd/web/commits/5db9475))
* Corrigindo o caminho de pão da tela de avalição de consenso (661). ([d3f7114](http://git.fasolti.com.br/avd/web/commits/d3f7114))
* Erro ao enviar mensagem em cadastro de avaliação ([02b7c3e](http://git.fasolti.com.br/avd/web/commits/02b7c3e))
* Erro converter funcionarioSimpleTO ([867380d](http://git.fasolti.com.br/avd/web/commits/867380d))
* issue [#137](http://git.fasolti.com.br/avd/web/issues/137) ([dacc6a4](http://git.fasolti.com.br/avd/web/commits/dacc6a4))
* Log erro envio de email ([1e382d6](http://git.fasolti.com.br/avd/web/commits/1e382d6))
* merge com development ([e2175d1](http://git.fasolti.com.br/avd/web/commits/e2175d1))
* Remocao coluna empresa visivel de configura ([ff25f90](http://git.fasolti.com.br/avd/web/commits/ff25f90))
* Retirado classes causando erro no build ([f1619dc](http://git.fasolti.com.br/avd/web/commits/f1619dc))
* Retirado coluna visualizar_por_unidade não usada ([94dbfe2](http://git.fasolti.com.br/avd/web/commits/94dbfe2))
* Tarefa 662, correção do tour avaliação por competência. ([b511c11](http://git.fasolti.com.br/avd/web/commits/b511c11))
* Tarefa 663, correção do tour de avaliação do coloborador. ([24b0837](http://git.fasolti.com.br/avd/web/commits/24b0837))
* Tarefa 665, correção do tour de nova/edição/view de avaliação. ([52b80aa](http://git.fasolti.com.br/avd/web/commits/52b80aa))
* **views/feedforwards:** adequar a exibição de dados às regras de apontamento/avaliação (mobile/[#55](http://git.fasolti.com.br/avd/web/issues/55)) ([0e19fb2](http://git.fasolti.com.br/avd/web/commits/0e19fb2))


### Features

* Abas "Por competências" e "Por Colaboradores", primeiro acesso tour específico (Bitrix 668). ([6107e1a](http://git.fasolti.com.br/avd/web/commits/6107e1a))
* Ajuste em hostController ([#99](http://git.fasolti.com.br/avd/web/issues/99)) ([9cbb3c4](http://git.fasolti.com.br/avd/web/commits/9cbb3c4))
* Ajuste na mensagem no tour de avaliações. ([78705d7](http://git.fasolti.com.br/avd/web/commits/78705d7))
* Ajustes em LogAuth ([a01eda4](http://git.fasolti.com.br/avd/web/commits/a01eda4))
* **reestruturação:** método GET de avaliacao ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([8d6e4a9](http://git.fasolti.com.br/avd/web/commits/8d6e4a9))
* Ajustes PDI ([#132](http://git.fasolti.com.br/avd/web/issues/132)) ([cdc8544](http://git.fasolti.com.br/avd/web/commits/cdc8544))
* atualização controllers JS ([#92](http://git.fasolti.com.br/avd/web/issues/92) [#94](http://git.fasolti.com.br/avd/web/issues/94) [#95](http://git.fasolti.com.br/avd/web/issues/95)) ([0a97362](http://git.fasolti.com.br/avd/web/commits/0a97362))
* atualização controllers JS, resources e serviços de competencia ([#87](http://git.fasolti.com.br/avd/web/issues/87)) ([d35faa2](http://git.fasolti.com.br/avd/web/commits/d35faa2))
* Atualização do controller ([#94](http://git.fasolti.com.br/avd/web/issues/94)) ([a1b77ed](http://git.fasolti.com.br/avd/web/commits/a1b77ed))
* Atualização do controller ([#95](http://git.fasolti.com.br/avd/web/issues/95)) ([d939be0](http://git.fasolti.com.br/avd/web/commits/d939be0))
* Busca de pendencia por avaliacao usando projection ([#79](http://git.fasolti.com.br/avd/web/issues/79)) ([0856c36](http://git.fasolti.com.br/avd/web/commits/0856c36))
* Classes LogAuth([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([268eb73](http://git.fasolti.com.br/avd/web/commits/268eb73))
* Configuração conexão com Rabbitmq ([d049b37](http://git.fasolti.com.br/avd/web/commits/d049b37))
* Corrigida mensagem de erro para usuario ([#65](http://git.fasolti.com.br/avd/web/issues/65)) ([685fd71](http://git.fasolti.com.br/avd/web/commits/685fd71))
* Criação de Entidade, TO, serviço e repositorio de LogAuth ([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([5025cc5](http://git.fasolti.com.br/avd/web/commits/5025cc5))
* Criação dos serviços de logAuth ([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([fbc979f](http://git.fasolti.com.br/avd/web/commits/fbc979f))
* criar serviço REST (create) para Feedforward  ([#55](http://git.fasolti.com.br/avd/web/issues/55)) ([f25ade4](http://git.fasolti.com.br/avd/web/commits/f25ade4))
* **reestruturação:** método GET de avaliacao ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([d98d5d7](http://git.fasolti.com.br/avd/web/commits/d98d5d7))
* criar serviço REST para Apontamento (#mobile/[#53](http://git.fasolti.com.br/avd/web/issues/53)) ([2450db5](http://git.fasolti.com.br/avd/web/commits/2450db5))
* criar serviço REST para Apontamento (#mobile/[#53](http://git.fasolti.com.br/avd/web/issues/53)) ([3d42656](http://git.fasolti.com.br/avd/web/commits/3d42656))
* criar serviço REST para Apontamento (mobile/[#53](http://git.fasolti.com.br/avd/web/issues/53)) ([673590d](http://git.fasolti.com.br/avd/web/commits/673590d))
* DEZI-14 > Avaliações > Aba "Por Competências" > Alterar Tela ([ef5e76f](http://git.fasolti.com.br/avd/web/commits/ef5e76f))
* DEZI-14 > Avaliações > Aba "Por Competências" > Alterar Tela ([c503a62](http://git.fasolti.com.br/avd/web/commits/c503a62))
* DEZI-14 > Avaliações > Aba "Por Competências" > Alterar Tela ([c392eec](http://git.fasolti.com.br/avd/web/commits/c392eec))
* DEZI-14 > Avaliações > Aba "Por Competências" > Alterar Tela ([d09e546](http://git.fasolti.com.br/avd/web/commits/d09e546))
* **adjust, foto:** Troca do endpoint de update de  detalhamentos e obtenção das fotos descomentado ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([5eb8106](http://git.fasolti.com.br/avd/web/commits/5eb8106))
* Refatorado Serviços e js controller do tipoCompetencia ([#86](http://git.fasolti.com.br/avd/web/issues/86)) ([ded12c6](http://git.fasolti.com.br/avd/web/commits/ded12c6))
* **adjust, merge:** Ajuste pós merge no GET da lista de avaliacoes ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([6ebc3f2](http://git.fasolti.com.br/avd/web/commits/6ebc3f2))
* **adjust, query:** Ajuste nos filtros da query de detalhamentos/preenchimentos ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([2d81a46](http://git.fasolti.com.br/avd/web/commits/2d81a46))
* **adjust, query:** Ajuste nos filtros da query de lista de avaliações ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([354082d](http://git.fasolti.com.br/avd/web/commits/354082d))
* **Ajuste endpoints; Participante; PDI:** Campos para salvar o PDI, ajuste no endpoint de save competencia ([#3](http://git.fasolti.com.br/avd/web/issues/3)) ([4dd6d94](http://git.fasolti.com.br/avd/web/commits/4dd6d94))
* **apontamentoFuncional:** adicionar ServiceFilter, usar listagem de subordinados de acordo com o usado pelo Mobile, ordenar FF recebidos por data de forma decrescente (mobile/[#81](http://git.fasolti.com.br/avd/web/issues/81), mobile/[#3](http://git.fasolti.com.br/avd/web/issues/3), mobile/[#4](http://git.fasolti.com.br/avd/web/issues/4), mobile/[#33](http://git.fasolti.com.br/avd/web/issues/33)) ([1dcabe0](http://git.fasolti.com.br/avd/web/commits/1dcabe0))
* **Campanha:** Quantidade de Avaliação por campanha (BaseService) ([8047e61](http://git.fasolti.com.br/avd/web/commits/8047e61))
* **campo acaoPDI, participante:** Ajuste no endpoint de salvar competencias, adição do campo acaoPDI na competencia ([#161](http://git.fasolti.com.br/avd/web/issues/161)) ([11b41c5](http://git.fasolti.com.br/avd/web/commits/11b41c5))
* **comentarios:** método GET de avaliacaoMobile finalizado ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([56fd098](http://git.fasolti.com.br/avd/web/commits/56fd098))
* **compression:** Compressão de foto do funcionario ([#86](http://git.fasolti.com.br/avd/web/issues/86)) ([2aab834](http://git.fasolti.com.br/avd/web/commits/2aab834))
* **exportação excel:** Exportação concluida([#147](http://git.fasolti.com.br/avd/web/issues/147)) ([e7507c8](http://git.fasolti.com.br/avd/web/commits/e7507c8))
* **filter; roles:** Filtro de funcionarios a partir de sua role ([#4](http://git.fasolti.com.br/avd/web/issues/4)) ([4cb6b60](http://git.fasolti.com.br/avd/web/commits/4cb6b60))
* **Flyway:** Criaçao da tabela PDI_Acoes ([d3fa0eb](http://git.fasolti.com.br/avd/web/commits/d3fa0eb))
* **mobile; login:** adicionar dados de empresa, unidade e cargo ao REST do funcionário logado ([d6128ed](http://git.fasolti.com.br/avd/web/commits/d6128ed))
* **mobile; login:** adicionar dados de empresa, unidade e cargo ao REST do funcionário logado ([dc07f45](http://git.fasolti.com.br/avd/web/commits/dc07f45))
* **PDI:** Ajuste Dialog iniciar e finalizar açao ([75c7cde](http://git.fasolti.com.br/avd/web/commits/75c7cde))
* **PDI:** Ajuste Lista de PDI ([e257f1c](http://git.fasolti.com.br/avd/web/commits/e257f1c))
* **PDI:** Ajuste merge ([967a3d1](http://git.fasolti.com.br/avd/web/commits/967a3d1))
* Refatoracao funcionario para Avaliacao ([#80](http://git.fasolti.com.br/avd/web/issues/80)) ([c2797c4](http://git.fasolti.com.br/avd/web/commits/c2797c4))
* **PDI:** Ajuste no modelo PDI, inclusao de funcionario ([9630bc4](http://git.fasolti.com.br/avd/web/commits/9630bc4))
* **PDI:** Ajuste no modelo PDI, inclusao de funcionario ([f3c5c4f](http://git.fasolti.com.br/avd/web/commits/f3c5c4f))
* **PDI:** Criaçao de modelo/entidade, TO, service, resource e repository ([b534e9f](http://git.fasolti.com.br/avd/web/commits/b534e9f))
* **PDI:** Ordem id pdiAcao ([af5eff3](http://git.fasolti.com.br/avd/web/commits/af5eff3))
* **PDI:** Serviço de busca de avaliaçoes com PDI ([9d0c8be](http://git.fasolti.com.br/avd/web/commits/9d0c8be))
* **preenchimento:** Percentual de preenchimento por preenchimento (mobile/[#89](http://git.fasolti.com.br/avd/web/issues/89)) ([45ba0f9](http://git.fasolti.com.br/avd/web/commits/45ba0f9))
* **RabbitmqConfig:** Configuração RabbitMq ([075b03a](http://git.fasolti.com.br/avd/web/commits/075b03a))
* **reestruturação:** REST de FaleConosco e DeziBaseService ([#57](http://git.fasolti.com.br/avd/web/issues/57)) ([2ce47db](http://git.fasolti.com.br/avd/web/commits/2ce47db))
* **reestruturação:** REST de FaleConosco e DeziBaseService ([#57](http://git.fasolti.com.br/avd/web/issues/57)) ([1a7aecf](http://git.fasolti.com.br/avd/web/commits/1a7aecf))
* **reestruturação:** REST de FaleConosco e DeziBaseService ([#57](http://git.fasolti.com.br/avd/web/issues/57)) ([33fb1d1](http://git.fasolti.com.br/avd/web/commits/33fb1d1))
* **reestruturação, ajuste:** método GET de todas as avaliações e ajuste na estrutura do FaleConosco ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([2b00079](http://git.fasolti.com.br/avd/web/commits/2b00079))
* **reestruturação, ajuste:** método GET de todas as avaliações e ajuste na estrutura do FaleConosco ([#77](http://git.fasolti.com.br/avd/web/issues/77)) ([2b29200](http://git.fasolti.com.br/avd/web/commits/2b29200))
* Erro correto de login/senha apresentado ao usuário([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([b517bba](http://git.fasolti.com.br/avd/web/commits/b517bba))
* Esqueci minha senha somente pesquisa por email ([#43](http://git.fasolti.com.br/avd/web/issues/43)) ([60a4db0](http://git.fasolti.com.br/avd/web/commits/60a4db0))
* Melhoria de senha ([#26](http://git.fasolti.com.br/avd/web/issues/26)) ([c58182c](http://git.fasolti.com.br/avd/web/commits/c58182c))
* Melhorias na tela de esqueceu a senha ([#43](http://git.fasolti.com.br/avd/web/issues/43)) ([8a0069c](http://git.fasolti.com.br/avd/web/commits/8a0069c))
* Número de tentativas de login e bloqueio de IP ([#64](http://git.fasolti.com.br/avd/web/issues/64)) ([e4539d7](http://git.fasolti.com.br/avd/web/commits/e4539d7))
* organização dos imports ([1f82e15](http://git.fasolti.com.br/avd/web/commits/1f82e15))
* Refatoracao Cadastros Escalas de Graduacao ([#85](http://git.fasolti.com.br/avd/web/issues/85)) ([f19d2de](http://git.fasolti.com.br/avd/web/commits/f19d2de))
* Refatoracao metodos MensageriaServiceImpl ([#81](http://git.fasolti.com.br/avd/web/issues/81)) ([b2857ad](http://git.fasolti.com.br/avd/web/commits/b2857ad))
* Refatoracao parametroTO ([#98](http://git.fasolti.com.br/avd/web/issues/98)) ([5e59758](http://git.fasolti.com.br/avd/web/commits/5e59758))
* Refatorado menu Acompanhamentos ([#75](http://git.fasolti.com.br/avd/web/issues/75)) ([77923c1](http://git.fasolti.com.br/avd/web/commits/77923c1))
* Refatorar os TOs do menu Relatórios([#84](http://git.fasolti.com.br/avd/web/issues/84)) ([586aa6a](http://git.fasolti.com.br/avd/web/commits/586aa6a))
* Utilização método serviço para prazer somente informação TO necessária  ([#54](http://git.fasolti.com.br/avd/web/issues/54)) ([629858f](http://git.fasolti.com.br/avd/web/commits/629858f))
* **relatorio, filtro campanha e avaliacao:** Filtros do relatorio terminados ([#147](http://git.fasolti.com.br/avd/web/issues/147)) ([ecd75fb](http://git.fasolti.com.br/avd/web/commits/ecd75fb))
* **save acao:** Método para salvar a ação e suas competencias/detalhamentos ([#131](http://git.fasolti.com.br/avd/web/issues/131)) ([a7b08af](http://git.fasolti.com.br/avd/web/commits/a7b08af))
