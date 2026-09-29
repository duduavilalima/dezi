# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [3.14.1](http://git.fasolti.com.br/avd/webapp/compare/v3.14.0...v3.14.1) (2021-09-27)


### Bug Fixes

* **Competência / Detalhamento:** correção na inserção de um novo detalhamento. ([fe2deb8](http://git.fasolti.com.br/avd/webapp/commit/fe2deb84eb86f359d700335b64c179d34b07827f))

## [3.14.0](http://git.fasolti.com.br/avd/webapp/compare/v3.13.3...v3.14.0) (2021-09-16)


### Features

* **Administração - Mensagem:** permitir o envio  da mensagem de boas vindas por perfil ou por um colaborador específico, antes somente era possível por perfil. Ainda há um pequeno ajuste de design de tela, mas a funcionalidade já foi concluída. ([809f3d0](http://git.fasolti.com.br/avd/webapp/commit/809f3d091d757f8066c585ff2ed95f023c79c82a))
* **PDI:** ajuste na lista de subordinados. Além de exibir os subordinados diretos, também foram incluídos os avaliados pelo gestor, mesmo que de outro setor. ([260afd1](http://git.fasolti.com.br/avd/webapp/commit/260afd197356505abf7f25d1dd7eb3102e32bc5c))
* **PDI:** inclusão da edição do pdi, pelo gestor, de um subordinado de outro setor. ([6532965](http://git.fasolti.com.br/avd/webapp/commit/6532965fe83df19fea893e89bf5bde3b27cbf918))


### Bug Fixes

* **Mensagem de boas vindas:** alteração da tela de mensagem de boas vindas, para permitir o envio de mensagens individuias e não apenas pelo perfil. ([53de9f9](http://git.fasolti.com.br/avd/webapp/commit/53de9f94b3a76f7a6c2e78eda5a3b4b6bb3a6bfa))
* **PDI:** remoção de parte dos ajustes de edição de colaboradores de setores diferentes do gestor. ([c6225c4](http://git.fasolti.com.br/avd/webapp/commit/c6225c495fb3b852fef93d05db2996f0a1565de5))
* **Preenchimento Avaliação:** ajuste na exibição das perguntas na avaliação, visando proporcionar a indetação para códigos de programação. ([44aba74](http://git.fasolti.com.br/avd/webapp/commit/44aba748581cb15d02affac2fd78dfdfbf6e2e6c))

### [3.13.3](http://git.fasolti.com.br/avd/webapp/compare/v3.13.2...v3.13.3) (2021-04-16)


### Bug Fixes

* **Avaliação:** correção na exibição do botão de de ajuste de pesos das avaliações. ([b3f0821](http://git.fasolti.com.br/avd/webapp/commit/b3f0821d358efc9fcee18a9feb49ddae927c9e6f))

### [3.13.2](http://git.fasolti.com.br/avd/webapp/compare/v3.13.1...v3.13.2) (2021-03-30)


### Bug Fixes

* **Relatórios:** issue [#355](http://git.fasolti.com.br/avd/webapp/issues/355) ([55fb103](http://git.fasolti.com.br/avd/webapp/commit/55fb1037323f644f4726f374a3b3607a3242231c))

### [3.13.1](http://git.fasolti.com.br/avd/webapp/compare/v3.13.0...v3.13.1) (2021-03-15)


### Bug Fixes

* **avaliação:** ajuste no botão "Salvar" na aba avaliadores ([a446999](http://git.fasolti.com.br/avd/webapp/commit/a446999e87f10397045b39520e09a0b8cf2f5612))
* **Avaliação:** inclusão do botão para execução de rotina de ajuste de pesos ([2294c26](http://git.fasolti.com.br/avd/webapp/commit/2294c2638003b46bbfca45ad29fba79c58c6c93a))
* **BI:** issue [#126](http://git.fasolti.com.br/avd/webapp/issues/126) ([21d915f](http://git.fasolti.com.br/avd/webapp/commit/21d915f1dc54e4803c8e30cfff1ca8a0552450bc))

## [3.13.0](http://git.fasolti.com.br/avd/webapp/compare/v3.12.0...v3.13.0) (2021-02-24)


### Features

* **BI: migraçao para ETL:** criação da tabela de parâmetros ([7478bdb](http://git.fasolti.com.br/avd/webapp/commit/7478bdb40d3478b3e7e99e7b3133657e84f44272))
* **parâmetros bi:** ajustes na chamada do relatório. ([bc6d621](http://git.fasolti.com.br/avd/webapp/commit/bc6d621be60bb8331fd6dd5f61babb3c1fe32a79))


### Bug Fixes

* **Avaliação:** ajuste no botão de edição ([a09f901](http://git.fasolti.com.br/avd/webapp/commit/a09f901839ef5bfe4ef87f64225fb6e0d555049e))
* **bi:** issue [#126](http://git.fasolti.com.br/avd/webapp/issues/126) ([b746782](http://git.fasolti.com.br/avd/webapp/commit/b746782baba6c9d37c37b99934cd19e4ab7c11e5))
* **preenchimento:** ajuste temporário para corrigir o preenchimento da tabela aval_peso_cargo_comp ([2725cc7](http://git.fasolti.com.br/avd/webapp/commit/2725cc7ead531620d8ea8f1ba576481076182948))

## [3.12.0](http://git.fasolti.com.br/avd/webapp/compare/v3.11.1...v3.12.0) (2020-10-24)


### Features

* **migração aws:** removendo o teste. ([e9eff96](http://git.fasolti.com.br/avd/webapp/commit/e9eff9638a9dc212253526af5b9224c62565775e))

### [3.11.1](http://git.fasolti.com.br/avd/webapp/compare/v3.11.0...v3.11.1) (2020-10-24)

## [3.11.0](http://git.fasolti.com.br/avd/webapp/compare/v3.10.8...v3.11.0) (2020-10-24)


### Features

* **migração aws:** verificação do CI. ([61a54cf](http://git.fasolti.com.br/avd/webapp/commit/61a54cf63e3a3efba16bcb71f0d681e5dc5140bc))

### [3.10.8](http://git.fasolti.com.br/avd/webapp/compare/v3.10.7...v3.10.8) (2020-10-01)


### Bug Fixes

* **graduação:** ajuste da exibição do botão descer, na lista de graduações. ([721a848](http://git.fasolti.com.br/avd/webapp/commit/721a848dead4aba891f35f2b0de8de932b7f5791))

### [3.10.7](http://git.fasolti.com.br/avd/webapp/compare/v3.10.6...v3.10.7) (2020-09-24)


### Bug Fixes

* **graduação:** correção na exibição das opções "Subir"/"Descer" na edição da graduação ([12d7930](http://git.fasolti.com.br/avd/webapp/commit/12d79302782be228eaaccf4fe4686f47f42812eb))

### [3.10.6](http://git.fasolti.com.br/avd/webapp/compare/v3.10.4...v3.10.6) (2020-09-22)


### Bug Fixes

* **avaliação:** correção na atualização da tela, após inativar uma avaliação. As datas alteradas no backend não eram exibidas no frontend. ([2f5f1f4](http://git.fasolti.com.br/avd/webapp/commit/2f5f1f47a78aa264c9c018902c91ee1e938506fa))
* **graduação:** ajuste na exbição dos botões subir e descer na edição das graduações ([d998380](http://git.fasolti.com.br/avd/webapp/commit/d9983800c784f842006bff03437e8f1dc0279499))

### [3.10.5](http://git.fasolti.com.br/avd/webapp/compare/v3.10.4...v3.10.5) (2020-09-22)


### Bug Fixes

* **avaliação:** correção na atualização da tela, após inativar uma avaliação. As datas alteradas no backend não eram exibidas no frontend. ([2f5f1f4](http://git.fasolti.com.br/avd/webapp/commit/2f5f1f47a78aa264c9c018902c91ee1e938506fa))
* **graduação:** ajuste na exbição dos botões subir e descer na edição das graduações ([d998380](http://git.fasolti.com.br/avd/webapp/commit/d9983800c784f842006bff03437e8f1dc0279499))

### [3.10.4](http://git.fasolti.com.br/avd/webapp/compare/v3.10.3...v3.10.4) (2020-09-08)


### Bug Fixes

* **acompanhamento:** ajustes listados no issues [#35](http://git.fasolti.com.br/avd/webapp/issues/35). ([766e254](http://git.fasolti.com.br/avd/webapp/commit/766e2541c7f9b5d28124d5b4cd221bf54bf08dff))
* **acompanhamentos:** ajusta o tamanho do gráfico e o tamanho dos campos de pesquisa ([#35](http://git.fasolti.com.br/avd/webapp/issues/35)) ([f71a8ef](http://git.fasolti.com.br/avd/webapp/commit/f71a8eff03f6c1094b9f0ea1beca9618fb515216))
* **acompanhamentos:** issue [#175](http://git.fasolti.com.br/avd/webapp/issues/175) (comentários do dia 12/08). ([b9f1be1](http://git.fasolti.com.br/avd/webapp/commit/b9f1be141435adf06925c7cc3396b7b9a0f5e359))
* **acompanhamentos:** issue [#319](http://git.fasolti.com.br/avd/webapp/issues/319) ([4ba8c45](http://git.fasolti.com.br/avd/webapp/commit/4ba8c454c5e1a90bcbdddb1db9ecdbc4ab860331))
* **cadastro de colaboradores:** remoção da listagem de avaliações na tela edição do colaborador. ([e6c05e8](http://git.fasolti.com.br/avd/webapp/commit/e6c05e89d5ae26206defefb577950ef3dff962f5))
* **graduação:** desabilitar o botão subir para graduações com ordem maior que 10 e o botão descer para as graduações com ordem maior ou igual a 10 ([4760b3b](http://git.fasolti.com.br/avd/webapp/commit/4760b3ba1aa13ea8262aa8171c3b07bd82d4fc68))

### [3.10.3](http://git.fasolti.com.br/avd/webapp/compare/v3.10.2...v3.10.3) (2020-08-27)


### Bug Fixes

* **toolbar:** remoção de log em console desnecessário. ([4055c65](http://git.fasolti.com.br/avd/webapp/commit/4055c650af51b6d5c989ca997463a438324ef2c3))

### [3.10.2](http://git.fasolti.com.br/avd/webapp/compare/v3.10.1...v3.10.2) (2020-07-26)


### Bug Fixes

* **mensagem de troca de senha:** as mensagens exibidas na troca de senha foram corrigidas em pt-BR. Falta fazer para os demais idiomas. ([b9b8b8c](http://git.fasolti.com.br/avd/webapp/commit/b9b8b8cd0f4fa632aca862b32fe5c0d19ba7617f))
* **mensagem de troca de senha:** complemento do commit anterior, pois faltou subir um arquivo ([2a5027f](http://git.fasolti.com.br/avd/webapp/commit/2a5027f369ecf1c96ca0874120924165bcee0b9f))
* **validacao-primeiro-acesso:** Ajusta a mensagem de erros de validação do primeiro acesso ([#311](http://git.fasolti.com.br/avd/webapp/issues/311)) ([8c7f558](http://git.fasolti.com.br/avd/webapp/commit/8c7f558af8fb2d168c7ba4a954593f72ede7908a))

### [3.10.1](http://git.fasolti.com.br/avd/webapp/compare/v3.10.0...v3.10.1) (2020-07-09)


### Bug Fixes

* **lock de login:** ajustado o bloqueio de IP. (Issue [#307](http://git.fasolti.com.br/avd/webapp/issues/307)) ([923dbca](http://git.fasolti.com.br/avd/webapp/commit/923dbcafaa602b75431c99e1f08dc2786c91416e))

## [3.10.0](http://git.fasolti.com.br/avd/webapp/compare/v3.9.2...v3.10.0) (2020-07-05)


### Features

* **relatórios:** remover da lista as campanhas que sejam exclusivamente de autoavaliação para os perfis de funcionário e gestor de área. ([f257b23](http://git.fasolti.com.br/avd/webapp/commit/f257b23580cf5065c98738ed70c07dab28649977))

### [3.9.2](http://git.fasolti.com.br/avd/webapp/compare/v3.9.0...v3.9.2) (2020-07-02)


### Bug Fixes

* **bi - reltório de autoavaliação:** filtragem do relatório por perfil. ([4d44d56](http://git.fasolti.com.br/avd/webapp/commit/4d44d56fca2e7fd10587334dabed8159217fad2c))
* **valida comentario:** Valida todos os comentarios obrigatórios ao salvar e finalizar ([4140f84](http://git.fasolti.com.br/avd/webapp/commit/4140f84415af9a23dc96a3e4910f126e2bb3e56e))

### [3.9.1](http://git.fasolti.com.br/avd/webapp/compare/v3.9.0...v3.9.1) (2020-07-02)


### Bug Fixes

* **bi - reltório de autoavaliação:** filtragem do relatório por perfil. ([4d44d56](http://git.fasolti.com.br/avd/webapp/commit/4d44d56fca2e7fd10587334dabed8159217fad2c))
* **valida comentario:** Valida todos os comentarios obrigatórios ao salvar e finalizar ([4140f84](http://git.fasolti.com.br/avd/webapp/commit/4140f84415af9a23dc96a3e4910f126e2bb3e56e))

## [3.9.0](http://git.fasolti.com.br/avd/webapp/compare/v3.8.3...v3.9.0) (2020-07-02)


### Features

* **acompanhamentos>preenchimento:** Envio de mensagens ([#175](http://git.fasolti.com.br/avd/webapp/issues/175)) ([d05c9b8](http://git.fasolti.com.br/avd/webapp/commit/d05c9b856e7ee4fbea94b23129e84ac268a62ff1))
* **cargo:** Botão Copiar Cargo ([fed6c9b](http://git.fasolti.com.br/avd/webapp/commit/fed6c9bc8816b69ed55631c443ecb07024355ac5))
* **cargo:** Impressão de nome de cargo no dialog  copiar ([99cfd68](http://git.fasolti.com.br/avd/webapp/commit/99cfd68189062166a89de5bd63bb829111ca7087))
* **tela de acompanhamentos:** Finalizacao da tela de acompanhamentos ([#35](http://git.fasolti.com.br/avd/webapp/issues/35)) ([5853e9c](http://git.fasolti.com.br/avd/webapp/commit/5853e9ca8eab321200191400347f155abe6d9017))
* **webpack:** Erro 404 para carregamento lento issue [#217](http://git.fasolti.com.br/avd/webapp/issues/217) ([ce67dd7](http://git.fasolti.com.br/avd/webapp/commit/ce67dd74df7607bdc33938cac1324c7efd515298))


### Bug Fixes

* **acompanhamentos:** Tamanho dos botões de limpar e gerar relatorio/merge cargo view ([#35](http://git.fasolti.com.br/avd/webapp/issues/35)) ([1001496](http://git.fasolti.com.br/avd/webapp/commit/10014962e406a1e43866b5d38dbd9c95626f0029))
* **bi - relatório de autoavaliação:** ajustar o relatório para filtrar de acordo com o perfil do usuário. ([2dcbf61](http://git.fasolti.com.br/avd/webapp/commit/2dcbf61f5d198a2b3bef242a83f77b7985350f04))
* **botao e filtro setor:** Ajusta o tamanho dos botoes e filtro por setores ativos nos acompanhamentos ([#35](http://git.fasolti.com.br/avd/webapp/issues/35)) ([97a98b0](http://git.fasolti.com.br/avd/webapp/commit/97a98b0d6658e6b62117316d0e036e73c69bd876))
* **cargo:** Menssagem correta de ValidationException do backend ([f780564](http://git.fasolti.com.br/avd/webapp/commit/f78056446150a4ef0db820a16bf774ca32c720dc))
* **funcionario:** Ajuste do endpoint do serviço de obter funcionario por situacao ([#234](http://git.fasolti.com.br/avd/webapp/issues/234)) ([3a054d1](http://git.fasolti.com.br/avd/webapp/commit/3a054d1acc95bc1d1c8b406ea4507d363fbd9cb7))
* **login:** Página de login não deve valida usuário logado ([baa0eed](http://git.fasolti.com.br/avd/webapp/commit/baa0eed087f5fba9d68a3a63e0378f2258918130))
* **redefinir senha:** Ajuste na mensagem de erro ([#226](http://git.fasolti.com.br/avd/webapp/issues/226)) ([9c4bf4f](http://git.fasolti.com.br/avd/webapp/commit/9c4bf4fe33710b178d2600f7199bc5bd087b6826))
* **relatórios:** ajustar código commitado em branch errado. ([a7d7b34](http://git.fasolti.com.br/avd/webapp/commit/a7d7b34031c7d2f4f804dc8f4c14e3855f58d6c0))
* **style:** prevenir erro de linhas duplicadas ([b3d591d](http://git.fasolti.com.br/avd/webapp/commit/b3d591d5833af7a66f986b4b29bb49e465402663))
* **valida comentario:** Valida todos os comentarios obrigatórios ao salvar e finalizar ([#303](http://git.fasolti.com.br/avd/webapp/issues/303)) ([7b6f004](http://git.fasolti.com.br/avd/webapp/commit/7b6f004bf433c93c29d3d7089daf3eb8ed569aaf))
* Ajustado caminho para a foto default do usuário issue 224 ([68bbaa7](http://git.fasolti.com.br/avd/webapp/commit/68bbaa7b3e920422c02ce6a98ff2c04ad9e39424))
* Retirado dialog de timeout ([be1ba2a](http://git.fasolti.com.br/avd/webapp/commit/be1ba2ad6249fcd279e8d1f36a63b03e77484a33))

### [3.8.3](http://git.fasolti.com.br/avd/webapp/compare/v3.8.2...v3.8.3) (2020-06-25)


### Bug Fixes

* **gráficos de acompanhamento:** correção da dialog que exibe os avaliados associados a um avaliador. ([4920db6](http://git.fasolti.com.br/avd/webapp/commit/4920db6d7ac804915a864ca1c4d9ac547704144f))
* **gráficos de acompanhamento:** correção na geração do gráfico. ([dc0580f](http://git.fasolti.com.br/avd/webapp/commit/dc0580f32e6192fd1093a7435510101318a959dd))
* **gráficos de acompanhamento:** correção na lista de detalhamentos do gráfico. ([f92d7b2](http://git.fasolti.com.br/avd/webapp/commit/f92d7b24b480ab916ad123851939751eb9706179))
* **gráficos de acompanhamento:** remoção do link para popup de avaliados. Ajuste para fechar a janela de envio de e-mail. ([6782e7f](http://git.fasolti.com.br/avd/webapp/commit/6782e7f4327e856ffa95613ec4930c4dff8a48e0))

### [3.8.2](http://git.fasolti.com.br/avd/webapp/compare/v3.8.1...v3.8.2) (2020-06-20)


### Bug Fixes

* **data avaliacao:** Ajusta das datas av1, av2 e av3 pela aba de colaboradores ([#283](http://git.fasolti.com.br/avd/webapp/issues/283)) ([d62b3c9](http://git.fasolti.com.br/avd/webapp/commit/d62b3c9ffcd705091c7265fb99f7e8d990ccefd0))
* **validacao-preenchimento-comentario-obrigatorio:** Valida os campos com comentario obrigatorio ([#303](http://git.fasolti.com.br/avd/webapp/issues/303)) ([9445b82](http://git.fasolti.com.br/avd/webapp/commit/9445b8212cf91926681d7a9a65a4ade67c380b9f))

### [3.8.1](http://git.fasolti.com.br/avd/webapp/compare/v3.8.0...v3.8.1) (2020-06-09)


### Bug Fixes

* **status em liberacao:** ajuste dos labels da liberação da avaliação e da mensagem popup. ([c3e2845](http://git.fasolti.com.br/avd/webapp/commit/c3e28458666cfb952cd9435f21439b746d66d3eb))

## [3.8.0](http://git.fasolti.com.br/avd/webapp/compare/v3.6.8...v3.8.0) (2020-06-04)


### Features

* **status em liberacao:** controle de exibição da opção "Liberar". ([6060106](http://git.fasolti.com.br/avd/webapp/commit/6060106af081c25916d05efe04185bcd166edb6d))
* **status em liberacao:** correção da mensagem de liberação da avaliação para o Gestor RH. Ajuste na exibição do setor do avaliado, no preenchimento por colaborador. ([fa27639](http://git.fasolti.com.br/avd/webapp/commit/fa27639758d0382fca46c2840cf1e4fd46e08f38))
* **status em liberacao:** correção do label "Em Liberação". Ajuste da exibição do cargo do colaborador, no preenchimento da avaliação. ([24ef326](http://git.fasolti.com.br/avd/webapp/commit/24ef326e1194a3b9d15f64ce0d1221c1dfce5be9))
* **status em liberacao:** Exibe o status em liberacao na avaliacao (tanto pela aba quanto pela lista de avaliacoes) ([#286](http://git.fasolti.com.br/avd/webapp/issues/286)) ([3462e26](http://git.fasolti.com.br/avd/webapp/commit/3462e26b155fe4803a9cd017ab3d19e3ee284aac))


### Bug Fixes

* **envio email aba mensagens:** Ajusta os serviços para chamar os endpoints corretos ([#295](http://git.fasolti.com.br/avd/webapp/issues/295)) ([33df7e6](http://git.fasolti.com.br/avd/webapp/commit/33df7e6e736b386a6209252b212057b00428f603))
* **select all avaliados:** Ajusta a checkbox de seleção de todos os avaliados (participantes) ([#284](http://git.fasolti.com.br/avd/webapp/issues/284)) ([08a8963](http://git.fasolti.com.br/avd/webapp/commit/08a8963cf85ba746a533cd4f53a779fc9b1ee8eb))
* **tipo perfil:** Ajusta combo de tipo perfil no mensagem route ([#288](http://git.fasolti.com.br/avd/webapp/issues/288)) ([d6a2568](http://git.fasolti.com.br/avd/webapp/commit/d6a2568978796e4561f1cc78a93d605d6e092cc8))
* **validacao primeiro acesso:** Ajusta o toastr de senha invalida/errada no dialog de primeiro acesso ([#282](http://git.fasolti.com.br/avd/webapp/issues/282)) ([d68870f](http://git.fasolti.com.br/avd/webapp/commit/d68870fb61cbfd37b8a47cb2ccac6489e4b04c9a))

## [3.7.0](http://git.fasolti.com.br/avd/webapp/compare/v3.6.8...v3.7.0) (2020-06-04)


### Features

* **status em liberacao:** controle de exibição da opção "Liberar". ([6060106](http://git.fasolti.com.br/avd/webapp/commit/6060106af081c25916d05efe04185bcd166edb6d))
* **status em liberacao:** correção da mensagem de liberação da avaliação para o Gestor RH. Ajuste na exibição do setor do avaliado, no preenchimento por colaborador. ([fa27639](http://git.fasolti.com.br/avd/webapp/commit/fa27639758d0382fca46c2840cf1e4fd46e08f38))
* **status em liberacao:** correção do label "Em Liberação". Ajuste da exibição do cargo do colaborador, no preenchimento da avaliação. ([24ef326](http://git.fasolti.com.br/avd/webapp/commit/24ef326e1194a3b9d15f64ce0d1221c1dfce5be9))
* **status em liberacao:** Exibe o status em liberacao na avaliacao (tanto pela aba quanto pela lista de avaliacoes) ([#286](http://git.fasolti.com.br/avd/webapp/issues/286)) ([3462e26](http://git.fasolti.com.br/avd/webapp/commit/3462e26b155fe4803a9cd017ab3d19e3ee284aac))


### Bug Fixes

* **envio email aba mensagens:** Ajusta os serviços para chamar os endpoints corretos ([#295](http://git.fasolti.com.br/avd/webapp/issues/295)) ([33df7e6](http://git.fasolti.com.br/avd/webapp/commit/33df7e6e736b386a6209252b212057b00428f603))
* **select all avaliados:** Ajusta a checkbox de seleção de todos os avaliados (participantes) ([#284](http://git.fasolti.com.br/avd/webapp/issues/284)) ([08a8963](http://git.fasolti.com.br/avd/webapp/commit/08a8963cf85ba746a533cd4f53a779fc9b1ee8eb))
* **tipo perfil:** Ajusta combo de tipo perfil no mensagem route ([#288](http://git.fasolti.com.br/avd/webapp/issues/288)) ([d6a2568](http://git.fasolti.com.br/avd/webapp/commit/d6a2568978796e4561f1cc78a93d605d6e092cc8))
* **validacao primeiro acesso:** Ajusta o toastr de senha invalida/errada no dialog de primeiro acesso ([#282](http://git.fasolti.com.br/avd/webapp/issues/282)) ([d68870f](http://git.fasolti.com.br/avd/webapp/commit/d68870fb61cbfd37b8a47cb2ccac6489e4b04c9a))

### [3.6.8](http://git.fasolti.com.br/avd/webapp/compare/v3.6.6...v3.6.8) (2020-05-19)

### [3.6.7](http://git.fasolti.com.br/avd/webapp/compare/v3.6.6...v3.6.7) (2020-05-19)

### [3.6.6](http://git.fasolti.com.br/avd/webapp/compare/v3.6.5...v3.6.6) (2020-05-17)


### Bug Fixes

* **layout:** hotsite layout ([#267](http://git.fasolti.com.br/avd/webapp/issues/267)) ([d37a667](http://git.fasolti.com.br/avd/webapp/commit/d37a6678daad19c28f477f74d2729604fdd25185))
* **layout:** max-width card ([#267](http://git.fasolti.com.br/avd/webapp/issues/267)) ([64d1904](http://git.fasolti.com.br/avd/webapp/commit/64d1904e072dde9ef6367a7674adfc06829e937a))

### [3.6.5](http://git.fasolti.com.br/avd/webapp/compare/v3.6.4...v3.6.5) (2020-05-16)

### [3.6.4](http://git.fasolti.com.br/avd/webapp/compare/v3.6.3...v3.6.4) (2020-05-15)


### Bug Fixes

* **email validation:** Ajusta a validação do email duplicado ([#267](http://git.fasolti.com.br/avd/webapp/issues/267)) ([d436a4f](http://git.fasolti.com.br/avd/webapp/commit/d436a4fdc7caedfd7c505ab1e25e55dfe71898bf))

### [3.6.3](http://git.fasolti.com.br/avd/webapp/compare/v3.6.2...v3.6.3) (2020-05-14)


### Bug Fixes

* **homeoffice:** corrigir layout da tela de cadastro ([3bfa280](http://git.fasolti.com.br/avd/webapp/commit/3bfa2800ad2079c4a7007ffd69287455c1975c2c))
* **liberacao avaliacao:** Ajusta a liberação da avaliação ao ter apenas autoavaliacao selecionada ([#279](http://git.fasolti.com.br/avd/webapp/issues/279)) ([b9e7319](http://git.fasolti.com.br/avd/webapp/commit/b9e73191f02178675385a6327d6677a534d1fded))

### [3.6.2](http://git.fasolti.com.br/avd/webapp/compare/v3.6.1...v3.6.2) (2020-05-13)


### Bug Fixes

* **homeOfficeBrasil:** Ajusta a mensagem de erro de duplicidade de email ([#267](http://git.fasolti.com.br/avd/webapp/issues/267)) ([2e04336](http://git.fasolti.com.br/avd/webapp/commit/2e04336673d164151546132d0b94de298b6a65ae))

### [3.6.1](http://git.fasolti.com.br/avd/webapp/compare/v3.6.0...v3.6.1) (2020-05-08)

### Features

* **homeOfficeBrasil**: Tela de cadastro funcional (#267) ([0e91e94](http://git.fasolti.com.br/avd/webapp/commit/0e91e94ef864627c262b9009db49d89a11f42bd8))
* **tela homeoffice**: Tela com os campos e com chamada do tipoSexo (#267)  ([f12ac53](http://git.fasolti.com.br/avd/webapp/commit/f12ac5316b5e0c944417f3d9049fc0e9f2b15238))

### Bug Fixes

* **homeOfficeBrasil**: Ajusta o comportamento ao remover o UF ([5156ef2](http://git.fasolti.com.br/avd/webapp/commit/5156ef2b6d35814599b221922cb2c966423cb744))
* **homeOfficeBrasil**: Ajusta mensagem de sucesso ao cadastrar (#267) ([76c68c0](http://git.fasolti.com.br/avd/webapp/commit/76c68c0ccafdb6adbc5e49c09cf878d21b5bb8cb))
* **homeOfficeBrasil**: Troca do link para 'cadastro' e ajuste nos comportamentos de página (#267) ([7ba1ea6](http://git.fasolti.com.br/avd/webapp/commit/7ba1ea6c6fc6d6bee2687c62085329aa173f74f1))

## [3.6.0](http://git.fasolti.com.br/avd/webapp/compare/v3.5.1...v3.6.0) (2020-04-29)


### Features

* Referente aos issues [#261](http://git.fasolti.com.br/avd/webapp/issues/261). ([18e8d7d](http://git.fasolti.com.br/avd/webapp/commit/18e8d7d77a94ffb31cfd85d1de97bf73b2b8c77f))

### [3.5.1](http://git.fasolti.com.br/avd/webapp/compare/v3.5.0...v3.5.1) (2020-04-10)


### Bug Fixes

* **login:** desabilitar autocorreção e auto-capitalização da matrícula do usuário (web[#257](http://git.fasolti.com.br/avd/webapp/issues/257)) ([7a18748](http://git.fasolti.com.br/avd/webapp/commit/7a18748f99f623828e47cdac4168bcad276681c3))
* **preenchimento:** incluir responsividade para as páginas de listagem de preenchimentos ([#258](http://git.fasolti.com.br/avd/webapp/issues/258)) ([85e16ff](http://git.fasolti.com.br/avd/webapp/commit/85e16ffdf79cc1fc863da2768f8ea4eb4b575a0b))

## [3.5.0](http://git.fasolti.com.br/avd/webapp/compare/v3.4.0...v3.5.0) (2020-03-28)


### Features

* **menu:** possibilitar ocultamento de menus e submenus de acordo com a empresa e perfil do usuário logado (web[#259](http://git.fasolti.com.br/avd/webapp/issues/259)) ([25deaa0](http://git.fasolti.com.br/avd/webapp/commit/25deaa02ca9b80ba05d45ed3c3bc00b2c9582e69))


### Bug Fixes

* **menuUtils/visibility:** refletir o caráter opcional dos campos "visibility", "onlyRoles" e "exceptRoles" na validação da configuração de visibilidade dos menus (web[#259](http://git.fasolti.com.br/avd/webapp/issues/259)) ([642ce25](http://git.fasolti.com.br/avd/webapp/commit/642ce253fc4b163c08ca501627700e06f26357f7))
* **preenchimento:** limitar o filtro de participantes com avaliação com consenso habilitado à tela de preenchimento de consenso ( web[#256](http://git.fasolti.com.br/avd/webapp/issues/256) ) ([ea56ee1](http://git.fasolti.com.br/avd/webapp/commit/ea56ee13d298bf13f7322e2a24cf3646d90a5e72))
* **preenchimento:** restaurar filtros nas telas de listagem de preenchimentos (avaliação, consensos, etc) ( #web[#262](http://git.fasolti.com.br/avd/webapp/issues/262) ) ([8bf9768](http://git.fasolti.com.br/avd/webapp/commit/8bf97687a0c7b071db10a8da076e541958351a67))
* **preenchimento/consenso:** não exibir os preenchimentos se a avaliação não estiver habilitada para a etapa de consenso ( web[#256](http://git.fasolti.com.br/avd/webapp/issues/256) ) ([8bafe9a](http://git.fasolti.com.br/avd/webapp/commit/8bafe9af57992a3a0e75869e08af47eb6477ed4f))
* **relatorios:** liberra acessos aos menus de relatórios para todos os usuários, independente de perfil (web[#259](http://git.fasolti.com.br/avd/webapp/issues/259)) ([2ac673c](http://git.fasolti.com.br/avd/webapp/commit/2ac673c76f19d568d456c169e19199eb3bf804f6)), closes [web#235](http://git.fasolti.com.br/avd/web/issues/235)

## [3.4.0](http://git.fasolti.com.br/avd/webapp/compare/v3.3.12...v3.4.0) (2020-03-16)


### Features

* **menu:** ocultar menus principais de acordo com o nome do menu e IDs de empresas (web[#213](http://git.fasolti.com.br/avd/webapp/issues/213)) ([ff0274b](http://git.fasolti.com.br/avd/webapp/commit/ff0274b0f1668e72de2c29f4a327bc0e1c10fa98))


### Bug Fixes

* **cadastros/colaboradores:** corrigir lógica de habilitação de campos de data (web[#252](http://git.fasolti.com.br/avd/webapp/issues/252)) ([af79165](http://git.fasolti.com.br/avd/webapp/commit/af79165667fa2fb885982e6602d3d2b20e5b73f5))
* **cadastros/colaboradores:** recarregar dados ao mudar a situação do colaborador na tela de visualização, definir datas-limite para períodos de contratação e de recesso (web[#252](http://git.fasolti.com.br/avd/webapp/issues/252)) ([a732ad4](http://git.fasolti.com.br/avd/webapp/commit/a732ad4ec77cfb45be03d797c0ada6b3d4120597))
* **cadastros/colaboradores:** rever obrigatoriedades dos campos (web[#249](http://git.fasolti.com.br/avd/webapp/issues/249)) ([80c9ffa](http://git.fasolti.com.br/avd/webapp/commit/80c9ffac13776a8f8888d7649b5f01b1b433c9c9))
* **cadastros/colaboradores:** usar diretiva correta para exibição de imagem substituta (anônima) (web[#252](http://git.fasolti.com.br/avd/webapp/issues/252)) ([b09d9dc](http://git.fasolti.com.br/avd/webapp/commit/b09d9dcf1b79ccb6640f78577839f24edfdf27cd))
* **cadastros/modelosgraduacao:** carregar componente form-table ao importar dinamicamente o módulo de cadastro (web[#253](http://git.fasolti.com.br/avd/webapp/issues/253)) ([1f371ff](http://git.fasolti.com.br/avd/webapp/commit/1f371ff01f00cf38d7f1feae54450cbca0d8478d))
* **cadastros/tipocompetencia:** atribuir título correto à coluna de nome, remover coluna inexistente (web[#254](http://git.fasolti.com.br/avd/webapp/issues/254)) ([f9da5f0](http://git.fasolti.com.br/avd/webapp/commit/f9da5f0cd3622e15c06ffe9e054c8cef41cec54a))
* **campanha:** Campo de dimensão removido ([#247](http://git.fasolti.com.br/avd/webapp/issues/247)) ([d7f05c6](http://git.fasolti.com.br/avd/webapp/commit/d7f05c6370fc1bf0f40e1ebc14753f80dc959ee8))
* **components/form-input:** rever lógica de tradução dos labels (web[#249](http://git.fasolti.com.br/avd/webapp/issues/249)) ([cf02391](http://git.fasolti.com.br/avd/webapp/commit/cf023910902fae785f95cfcc935c09f8f4aa7d58))
* **components/form-input/datepicker:** corrigir referência à propriedade maxDate (web[#252](http://git.fasolti.com.br/avd/webapp/issues/252)) ([0d62cf6](http://git.fasolti.com.br/avd/webapp/commit/0d62cf6634e9ed9aad0958059b661e8a223396de))
* **utils/dialogs/DialogSituacao:** adicionar por padrão mudança de situação via endpoint de ativação/inativação próprio (web[#252](http://git.fasolti.com.br/avd/webapp/issues/252)) ([2ced72f](http://git.fasolti.com.br/avd/webapp/commit/2ced72f897e9f24a6152c47175f51e4d740ae433))
* ignorar arquivos de report do node/webpack ([c8502ea](http://git.fasolti.com.br/avd/webapp/commit/c8502ea45e8ce5d31315a16dfda6e7f210745a59))

### [3.3.12](http://git.fasolti.com.br/avd/webapp/compare/v3.3.11...v3.3.12) (2020-02-20)


### Bug Fixes

* **PDI:** Ajuste na dialog de anotações e na transição de abas ([#242](http://git.fasolti.com.br/avd/webapp/issues/242)) ([647bfed](http://git.fasolti.com.br/avd/webapp/commit/647bfed57a7fe24ecd65497dc9e1094c027546a2))
* **PDI:** Ajuste no mapeamento das campanhas do PDI ([#245](http://git.fasolti.com.br/avd/webapp/issues/245)) ([1f0548f](http://git.fasolti.com.br/avd/webapp/commit/1f0548f5b2f3a68a18f46794da53769ef1c33d37))

### [3.3.11](http://git.fasolti.com.br/avd/webapp/compare/v3.3.10...v3.3.11) (2020-02-19)


### Bug Fixes

* **funcionario pdi ativos:** lista de funcionarios ativos (PDI) ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([5dd2267](http://git.fasolti.com.br/avd/webapp/commit/5dd22671d3319c6f8bceb20f61c8a014ebbe5588))

### [3.3.10](http://git.fasolti.com.br/avd/webapp/compare/v3.3.9...v3.3.10) (2020-02-17)

### [3.3.9](http://git.fasolti.com.br/avd/webapp/compare/v3.3.8...v3.3.9) (2020-02-15)


### Bug Fixes

* **erro de validação avaliados:** Ao validar, estoura o erro caso o par avaliado/avaliador ja esteja cadastrado ([#238](http://git.fasolti.com.br/avd/webapp/issues/238)) ([417a95c](http://git.fasolti.com.br/avd/webapp/commit/417a95c0abb6c7ada028cf9eb3777ed60d904528))

### [3.3.8](http://git.fasolti.com.br/avd/webapp/compare/v3.3.7...v3.3.8) (2020-02-13)


### Bug Fixes

* **aba avaliados:** Ajuste da validação de data limite ao remover/adicionar um avaliado ([#232](http://git.fasolti.com.br/avd/webapp/issues/232)) ([995dc1d](http://git.fasolti.com.br/avd/webapp/commit/995dc1da738de6365ff1b887470719e959636bcf))
* **abrir chamado:** Ajuste na mensagem de sucesso ao abrir um chamado ([#231](http://git.fasolti.com.br/avd/webapp/issues/231)) ([32730e1](http://git.fasolti.com.br/avd/webapp/commit/32730e191df21c901f49991bc624fb832f22b28f))
* **avaliadores filter:** Ajuste na pesquisa da aba de avaliadores ([#233](http://git.fasolti.com.br/avd/webapp/issues/233)) ([747d7f4](http://git.fasolti.com.br/avd/webapp/commit/747d7f4ba36c0074a019e7507386ce54f077764d))
* **ci:** remover nome do container de build, permitir builds de imagens localmente, corrigir labels das imagens ([31b00d8](http://git.fasolti.com.br/avd/webapp/commit/31b00d8030cf444dc21e08bc636202d91b27fa0e))
* **combo campanha PDI:** ajuste na combo de campanha ao criar uma acao de PDI ([#237](http://git.fasolti.com.br/avd/webapp/issues/237)) ([d883fe9](http://git.fasolti.com.br/avd/webapp/commit/d883fe91756b55a46a375b29a81b40c2cb689293))
* **login lowerCase:** Remoção do css que fazia texto em lowerCase ([#228](http://git.fasolti.com.br/avd/webapp/issues/228)) ([aca5279](http://git.fasolti.com.br/avd/webapp/commit/aca527917c8b3688a0016e37d214b2a550430c73))
* **upload pares, aba avaliados:** Ajuste de validação da planilha de pares ([#238](http://git.fasolti.com.br/avd/webapp/issues/238)) ([66e55b1](http://git.fasolti.com.br/avd/webapp/commit/66e55b1f9cff0489f752c82a552bae87c9f40d87))

### [3.3.7](http://git.fasolti.com.br/avd/webapp/compare/v3.3.6...v3.3.7) (2020-02-05)


### Bug Fixes

* **dialogs:** Ajuste nas dialogs de inativação/ativação ([#229](http://git.fasolti.com.br/avd/webapp/issues/229)) ([9881f6a](http://git.fasolti.com.br/avd/webapp/commit/9881f6a1e8c0f54a33becea403b7972b2eff1c55))
* **relatorio:** menu de relatorios comentado ([0f3c6e7](http://git.fasolti.com.br/avd/webapp/commit/0f3c6e708791d65609b16343b7648809c138d49a))
* **relatorio:** Retirada de acesso para  perfis de funcionario e gestor de area ([#235](http://git.fasolti.com.br/avd/webapp/issues/235)) ([d48fe0c](http://git.fasolti.com.br/avd/webapp/commit/d48fe0c9c8fdde74f296de350f9ea3daf5409efa))
* Menu relatorio retornando null pra bloqueio ([fa81a90](http://git.fasolti.com.br/avd/webapp/commit/fa81a90cefe35bf05ec57e6112af4229c670f8ed))
* Relatorios BI comentado ([864979f](http://git.fasolti.com.br/avd/webapp/commit/864979f36d2cdbab55123db6c6f40a9ede0533ea))
* **save/update detalhamentocalibrahint:** ao salvar uma competencia, atualiza os detalhamentos ([#229](http://git.fasolti.com.br/avd/webapp/issues/229)) ([11da804](http://git.fasolti.com.br/avd/webapp/commit/11da8042e4d9e80e2e022346ba9b0efff4ddbab3))

### [3.3.6](http://git.fasolti.com.br/avd/webapp/compare/v3.3.5...v3.3.6) (2020-01-24)


### Bug Fixes

* **escala de graduacao dialog:** Dialog de confirmação ao editar uma graduacao ([#222](http://git.fasolti.com.br/avd/webapp/issues/222)) ([239447b](http://git.fasolti.com.br/avd/webapp/commit/239447bbaf110bf5eee0940c88f1627335644586))
* Ajustado caminho para a foto default do usuário issue 224 ([12b84bd](http://git.fasolti.com.br/avd/webapp/commit/12b84bd133637dedd5f69970950670c8bf1edd06))

### [3.3.5](http://git.fasolti.com.br/avd/webapp/compare/v3.3.4...v3.3.5) (2020-01-06)


### Bug Fixes

* **datas aba resumo:** Data av2 nao pode ser menor que a atual ([#220](http://git.fasolti.com.br/avd/webapp/issues/220)) ([c9b0145](http://git.fasolti.com.br/avd/webapp/commit/c9b0145600ab22cd763b6b030b23b8d44698515c))
* **validacao aba resumo:** Validação de data ao criar uma avaliacao ([#220](http://git.fasolti.com.br/avd/webapp/issues/220)) ([8bcd40e](http://git.fasolti.com.br/avd/webapp/commit/8bcd40e842ff012dd42e6d33745a426377893b8f))
* Redirecionamento após timeout([#217](http://git.fasolti.com.br/avd/webapp/issues/217)) ([d3d7d82](http://git.fasolti.com.br/avd/webapp/commit/d3d7d82a2eb72afee2c42651f33b675d28a8f6bd))

### [3.3.4](http://git.fasolti.com.br/avd/webapp/compare/v3.3.3...v3.3.4) (2019-12-23)

### [3.3.3](http://git.fasolti.com.br/avd/webapp/compare/v3.3.2...v3.3.3) (2019-12-23)


### Bug Fixes

* **relatorios/historico,bi:** utilizar o mesmo método de login do BI usado na página de relatórios/resultados (avd/web[#214](http://git.fasolti.com.br/avd/webapp/issues/214)) ([a78e9a5](http://git.fasolti.com.br/avd/webapp/commit/a78e9a55118cbfa41e4130b1f9f1b5819cb46e27))

### [3.3.2](http://git.fasolti.com.br/avd/webapp/compare/v3.3.1...v3.3.2) (2019-12-21)


### Bug Fixes

* Menu relatório restrito pra Gestor RH ([17e1fde](http://git.fasolti.com.br/avd/webapp/commit/17e1fde2f50d60ef36257059617bc4ee46699d30))
* **toggle avaliados:** Ajuste no botão de selecionar todos os avaliados ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([4eb6e0a](http://git.fasolti.com.br/avd/webapp/commit/4eb6e0a105d6d08c39fec7706d4ff5239f108092))

### [3.3.1](http://git.fasolti.com.br/avd/webapp/compare/v3.3.0...v3.3.1) (2019-12-20)


### Bug Fixes

* **graduacao:** Erro da graduação (av2) sendo salva enquanto se preenchia uma auto avaliacao ([d0bacca](http://git.fasolti.com.br/avd/webapp/commit/d0bacca6ade0f2ef0ce757936c4fa7a77e1e3d8b))

## [3.3.0](http://git.fasolti.com.br/avd/webapp/compare/v3.2.0...v3.3.0) (2019-12-18)


### Features

* **ci,release:** adicionar informações de release e CI nas imagens Docker em tempo de execução via env-vars ([e243b7f](http://git.fasolti.com.br/avd/webapp/commit/e243b7fb59558a468d3604e51f5ffb84244b56ca))


### Bug Fixes

* **aba em andamento pdi:** Ajuste de remoção do time 'utc' no html ([5272f39](http://git.fasolti.com.br/avd/webapp/commit/5272f39ca9167bfdcb5f5e496b7a0eee7fd17f30))
* **dataInicio:** Ajuste no campo de data de inicio do relatorio ([#147](http://git.fasolti.com.br/avd/webapp/issues/147)) ([9e469c8](http://git.fasolti.com.br/avd/webapp/commit/9e469c82f8f26fe5e302f1600908ee5ca0157d31))
* **routes:** Rotas ao digitar uma URL invalida ([#207](http://git.fasolti.com.br/avd/webapp/issues/207)) ([b2f3c33](http://git.fasolti.com.br/avd/webapp/commit/b2f3c3377c66b54002d6b1ad70fd80d6c20e2d3a))

## 3.2.0 (2019-12-15)


### Features

* **ci:** reformular a construção do projeto via CI usando o ci/common do projeto deploy, reduzir tamanho da imagem final ([80aedc5](http://git.fasolti.com.br/avd/webapp/commit/80aedc52e37eeef0b8b15e5416fea85daecc70d5))
* **ci:** remover worker, configurar JDBC do tomcat por sysprops, adicionar labels às imagens geradas via scripts de build ([dc51d00](http://git.fasolti.com.br/avd/webapp/commit/dc51d002aa41c52e23458f479417af00f593b78b))
* **components/form-input:** adicionar componente labeled-text ([cf250e1](http://git.fasolti.com.br/avd/webapp/commit/cf250e1e50e1948332e9a1293d00b57d5fffa34a))
* **components/labeled-input:** adicionar campo opcional de nome de ID de tradução para valores em modo select ([da8c2aa](http://git.fasolti.com.br/avd/webapp/commit/da8c2aa02c45a7138bf7f68337cfbf373b9060d9))
* **components/labeled-input:** adicionar tipo 'date' ([7d0f37b](http://git.fasolti.com.br/avd/webapp/commit/7d0f37b00e298f7af594e7fa2308cc1a179edc1e))
* **components/labeled-input:** adicionar tipo de input 'select-search' ([57b3273](http://git.fasolti.com.br/avd/webapp/commit/57b3273219eb016e9393699618aedaa3f317077c))
* **dialog-utils/confirm:** adicionar callback onSuccess e notificação automática de sucesso ([1ac0111](http://git.fasolti.com.br/avd/webapp/commit/1ac01119935ba5e92e3ca47880b0aa24c1cd28c0))
* **dialog, add-acao:** Dialog de add ação ([#131](http://git.fasolti.com.br/avd/webapp/issues/131)) ([7f43454](http://git.fasolti.com.br/avd/webapp/commit/7f43454110482970dc66b2bb75373a004698bd71))
* **form-table:** adicionar melhorias ([e8f8d25](http://git.fasolti.com.br/avd/webapp/commit/e8f8d2568292228aa86ab339af01f9ef590bf20f))
* **login:** simplificar login de empresas ([777148c](http://git.fasolti.com.br/avd/webapp/commit/777148ce2bddaccf37337af613c81cfa2623a52e))
* **PDI:** Add estrutura dos dialogs ([42f8087](http://git.fasolti.com.br/avd/webapp/commit/42f80871a1ec85f33f7e19a1f83330fecf98bf5b))
* **PDI:** Ajuste do novo MenuItens ([33e1b9d](http://git.fasolti.com.br/avd/webapp/commit/33e1b9def369eaf1f9998f306e6e777432859b77))
* **PDI:** Ajuste em iniciar, finalizar acao ([b9e3dcc](http://git.fasolti.com.br/avd/webapp/commit/b9e3dcc30ea9b9aa6654c26a9ce8ea2012e41735))
* **PDI:** Ajuste foto e testo principal ([7bc625f](http://git.fasolti.com.br/avd/webapp/commit/7bc625f8989de58d28121703898828bbd0946e33))
* **PDI:** Ajuste merge ([436b781](http://git.fasolti.com.br/avd/webapp/commit/436b7810396d79d567230851343a96fe08b3d79c))
* **PDI:** Ajuste no menu para relatório PR ([e2cd779](http://git.fasolti.com.br/avd/webapp/commit/e2cd77907880c624ecadad77694a42b8f4f27eb7))
* **PDI:** Ajustes dialog inicar e finalizar ([d39b664](http://git.fasolti.com.br/avd/webapp/commit/d39b664433108cf3365a425a20936d8425bd2818))
* **PDI:** Ajustes Lista de pdi ([3ec96b5](http://git.fasolti.com.br/avd/webapp/commit/3ec96b5aecee9713a9af3ca9d75685c2c8891e8e))
* **PDI:** Ajustes Relatório ([aa289d2](http://git.fasolti.com.br/avd/webapp/commit/aa289d203c29db3fc81711b03f55cd2bfa31ef71))
* **PDI:** Ajustes tela, abas e dialogs ([f5c15bd](http://git.fasolti.com.br/avd/webapp/commit/f5c15bd5c43359221f539a5b638ec55d1f999607))
* **PDI:** Combos do dialog anotações ([a3d3046](http://git.fasolti.com.br/avd/webapp/commit/a3d304609a453c17c9f4db21a47d6b42ea1bf087))
* **PDI:** Dialog Iniciar Ação dev ([d520bee](http://git.fasolti.com.br/avd/webapp/commit/d520beef4af1034864e9ddc125d6d119ec76ef75))
* **PDI:** Estrutura Abas PDI Cards ([2a2efc5](http://git.fasolti.com.br/avd/webapp/commit/2a2efc59e98e6e28034dcf9bc03e9d636fa49b03))
* **PDI:** Estruturas de dialogs ([ac1e9cd](http://git.fasolti.com.br/avd/webapp/commit/ac1e9cd9b61b0bfcf4ce9bd4e041ea9bcc5d2f7e))
* **PDI:** Estrututra dialog ações e anotações ([22fa96d](http://git.fasolti.com.br/avd/webapp/commit/22fa96df22263d6bcaf79bc11e41bb9ae3abbecd))
* **PDI:** incrusão de funcionario no modelo PDI ([e1bff30](http://git.fasolti.com.br/avd/webapp/commit/e1bff30c851ef1cc98b54575ba7b3037405fad47))
* **PDI:** PDI - Relatórios - PR ([84e619f](http://git.fasolti.com.br/avd/webapp/commit/84e619fa292063ab46cefed955d1fb52a931ab03))
* **PDI:** PDI - Relatórios - PR ([b436ba0](http://git.fasolti.com.br/avd/webapp/commit/b436ba051a6c3850151f1c157d63c5254a05132a))
* **PDI:** Relatório individual([#132](http://git.fasolti.com.br/avd/webapp/issues/132)) ([dce7076](http://git.fasolti.com.br/avd/webapp/commit/dce7076799e0da5502f43afe637a70957aa6fb82))
* **PDI:** Serviço de PDI e preenchimento das Abas ([1521634](http://git.fasolti.com.br/avd/webapp/commit/1521634a76ab0a84beee06dc979f211c9256c92d))
* **PDI:** Serviços PDI ([02a5f93](http://git.fasolti.com.br/avd/webapp/commit/02a5f93b6e1f772ea52c77eea9a792869442e713))
* **relatorio, filters:** Filtro de campanha e avaliação ([#147](http://git.fasolti.com.br/avd/webapp/issues/147)) ([f2ba910](http://git.fasolti.com.br/avd/webapp/commit/f2ba91045f720ac7935341dd770226478a9ae2cf))
* **relatorio; exportar:** Exportação da ação para excel ([#147](http://git.fasolti.com.br/avd/webapp/issues/147)) ([75c64cd](http://git.fasolti.com.br/avd/webapp/commit/75c64cdc5bbc22b951d966206bedc7af8ea6ffd5))
* **switch dialog:** Dialog de troca de situação ([5d6aa17](http://git.fasolti.com.br/avd/webapp/commit/5d6aa171dab5144bfa183dd562fc55cc5db11f9f))
* Editar Dialog Ação de desenvolvimento ([fbe20a5](http://git.fasolti.com.br/avd/webapp/commit/fbe20a5258c02a8c6bfb0fd163c3a9ce51948029))
* **Avaliacao:** Ajustes tela de Avaliação ([1dbd991](http://git.fasolti.com.br/avd/webapp/commit/1dbd9918c02a2618fec3b939d8d03caf557fa7a6))
* **Avaliacao:** Nova e edição de Avaliacao ([ef47144](http://git.fasolti.com.br/avd/webapp/commit/ef4714463e1c43a57da2a75e81caf32b23d717f1))
* **Avaliacao:** View de Avaliação ([adba166](http://git.fasolti.com.br/avd/webapp/commit/adba166e08473414ce25499443665dc675ff51d5))
* **avaliadores/view-model:** criar loadAvaliadores ([117f967](http://git.fasolti.com.br/avd/webapp/commit/117f9677cef08dc7ede2c55c1981966c8e3a5504))
* **campanha:** Dialogo Detalhes ([41e8ed8](http://git.fasolti.com.br/avd/webapp/commit/41e8ed816e4121b023e763ea70312d542a57a5bc))
* **campanha:** Edição de campanha ([06b0c0e](http://git.fasolti.com.br/avd/webapp/commit/06b0c0e5a208b2bed917b0e9f9342de96addd0f8))
* **Campanha:** ação Repetir Avaliação ([8525f54](http://git.fasolti.com.br/avd/webapp/commit/8525f5497efd17dd0cc224932ef43dd2b42d1b1f))
* **Campanha:** Lista de Campanhas ([95deaa6](http://git.fasolti.com.br/avd/webapp/commit/95deaa6542991430496fb9b5d85f8c1c5dd19447))
* **campo acaoPDI, services:** Ajuste no serviço de salvar competencias e criação do campo acao para PDI([#161](http://git.fasolti.com.br/avd/webapp/issues/161)) ([93f5e06](http://git.fasolti.com.br/avd/webapp/commit/93f5e067ed3f9241d5dc10999209ea6f36d54d07))
* **campos de justificativa; salvar pedindo senha:** Campos de jusitificativa avaliado/avaliador e save pedindo a senha ([#3](http://git.fasolti.com.br/avd/webapp/issues/3)) ([a900bd7](http://git.fasolti.com.br/avd/webapp/commit/a900bd794aca19ba9dbf32ef180c939bdefd27d6))
* **DatePicker:** Adição de date Picker ([070cc11](http://git.fasolti.com.br/avd/webapp/commit/070cc113ddc067f2a62e655d4cd9df8baec560bc))
* **funcionario; roles:** Ajuste no menu lateral a partir da role do usuario logado ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([d7d82ee](http://git.fasolti.com.br/avd/webapp/commit/d7d82ee276f6e7dab8379383efdab430dc390ec3))
* **package.json:** Adicionado jquery-datetimepicker ([25b08f4](http://git.fasolti.com.br/avd/webapp/commit/25b08f45931150af03f5a54cfb83d5626f0a4eaf))
* **PDI:** Abas de PDI ([993cc2a](http://git.fasolti.com.br/avd/webapp/commit/993cc2a030195629329fcc1544630d5715729fee))
* **PDI:** Arquivos iniciais PDI ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([efecd9e](http://git.fasolti.com.br/avd/webapp/commit/efecd9e39b211f67bdbf0ded08248386d33e5b16))
* **PDI:** Estrutura das Abas ([b4e9a87](http://git.fasolti.com.br/avd/webapp/commit/b4e9a873e5083328f40fe9039471a92d67ca7abe))
* **PDI:** Id funcionário por parametro ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([61f1524](http://git.fasolti.com.br/avd/webapp/commit/61f15248aaec0d31ec27f29e6d6604b7266806a7))
* **PDI:** PDI lista de funcionário ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([810595a](http://git.fasolti.com.br/avd/webapp/commit/810595a3b56bd3ae2b861892096de29dab7dd83c))
* **refactor, participantes:** Transferencia da logica de seleção de um/todos funcionario(s) para o avaliacao-view-model ([#125](http://git.fasolti.com.br/avd/webapp/issues/125)) ([e89c04b](http://git.fasolti.com.br/avd/webapp/commit/e89c04bf40bef498163ad61b01870cfef3b722c5))
* **refactor, relatorios:** Refatoração de relatorios concluida ([#125](http://git.fasolti.com.br/avd/webapp/issues/125)) ([c88ddc0](http://git.fasolti.com.br/avd/webapp/commit/c88ddc0c69599194dc62c0070af22897bee9eca8))
* **utils:** adicionar routes.js ([594300c](http://git.fasolti.com.br/avd/webapp/commit/594300c052010fa64250186810d4c6c13104e0f4))
* **utils/MultiListSelection:** adicionar inicialização opcional ([1d5695a](http://git.fasolti.com.br/avd/webapp/commit/1d5695ac7541ec02b5a2b556a84713f1ae0e246e))
* **utils/MultiListSelection:** adicionar inicialização opcional ([52e01c0](http://git.fasolti.com.br/avd/webapp/commit/52e01c036f413ab5f7e61d3a044567bfc299f806))
* Ativação, Inativação e Remoção de Avaliação ([b889c4a](http://git.fasolti.com.br/avd/webapp/commit/b889c4a0c0dff4fc2202e91075b9a6859ee7d442))
* controller de campanha renomeados ([88fc0a2](http://git.fasolti.com.br/avd/webapp/commit/88fc0a2fa3f1ea63561b87aed617b9c3a987c12d))
* criar serviço AvaliacaoViewModel ([543e613](http://git.fasolti.com.br/avd/webapp/commit/543e613cbd82f2852cb65b9002eedcab0b098862))
* Esqueci minha senha somente pesquisa por email ([#43](http://git.fasolti.com.br/avd/webapp/issues/43)) ([943f1aa](http://git.fasolti.com.br/avd/webapp/commit/943f1aa0ddfb856144748262c5f1dab092250a97))
* importação e organização de CSS ([4285660](http://git.fasolti.com.br/avd/webapp/commit/428566071b8bd1e8781f1c9269de1c99efb0f572))
* Lista de cargos em Cadastro ([738b7a4](http://git.fasolti.com.br/avd/webapp/commit/738b7a475aee04784704aa7be02b702a31ddfc1a))
* Lista de competência em Cadastro ([155534c](http://git.fasolti.com.br/avd/webapp/commit/155534c0625f91d00a640dbfec6e9e58fcd96c9b))
* Lista de dimensão em Cadastro ([0b0dc14](http://git.fasolti.com.br/avd/webapp/commit/0b0dc14785387c544f75a1684b78447d42772382))
* Lista de Escala de Graduação em Cadastro ([e96f93b](http://git.fasolti.com.br/avd/webapp/commit/e96f93b339a23e05c7482d09543fac4a1cca7b74))
* Lista de Setores em Cadastro ([a0e6c49](http://git.fasolti.com.br/avd/webapp/commit/a0e6c4963e2e00eaa8e451b172a1fe17caf4b1ac))
* Lista de tipo de competência em Cadastro ([46c340b](http://git.fasolti.com.br/avd/webapp/commit/46c340b65bb804a628cfe9bf0ebd454b8b017af6))
* Mensagem de erro no login para 403 e 404([#64](http://git.fasolti.com.br/avd/webapp/issues/64)) ([4206779](http://git.fasolti.com.br/avd/webapp/commit/4206779384af91650c59f604827b7316f2a2d172))


### Bug Fixes

* **aba avaliados, aba avaliadores:** ordenação dos avaliadores, ajuste ao adicionar um avaliador, ajuste ao salvar aba avaliados ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([d56dc43](http://git.fasolti.com.br/avd/webapp/commit/d56dc43f3b90ddbe4f27b4f106a8c7c8e4da82b5))
* **ajuste relatorio PDI:** Ajuste na mensagem quando nao há pdi ([#132](http://git.fasolti.com.br/avd/webapp/issues/132)) ([055809f](http://git.fasolti.com.br/avd/webapp/commit/055809f1d23fdc5047963399f61beaf51a874010))
* **ajustes diversos:** Ajustes de layout, comportamentos de ativação/inativação, árvore hierarquia de cargos e labels ([#200](http://git.fasolti.com.br/avd/webapp/issues/200)) ([57d8050](http://git.fasolti.com.br/avd/webapp/commit/57d805095c32eed38d31ccc1223332ea1c15fc9f))
* **ajustes pdi:** Diversos ajustes PDI ([#140](http://git.fasolti.com.br/avd/webapp/issues/140)) ([c0c7190](http://git.fasolti.com.br/avd/webapp/commit/c0c7190565a96ebff49586b59d4311227ae82982))
* **app.js:** Imports comentados ([7a0ea3f](http://git.fasolti.com.br/avd/webapp/commit/7a0ea3f05d8b9c01157157e7b58bb81d19730c3a))
* **auth:** permissao de visualização do botão de 'detalhes' ([#203](http://git.fasolti.com.br/avd/webapp/issues/203)) ([07ef40c](http://git.fasolti.com.br/avd/webapp/commit/07ef40c9b85b410c46cf4456059929fff1e504c4))
* **auth PDI:** Ajuste no funcionario que pode aprovar e revisar o PDI ([#140](http://git.fasolti.com.br/avd/webapp/issues/140)) ([512b599](http://git.fasolti.com.br/avd/webapp/commit/512b5991ba28a7936ed1ae49eecfadb87e5dec39))
* **Avaliadores:** erro ao selecionar avaliadores ([e1eda93](http://git.fasolti.com.br/avd/webapp/commit/e1eda936652577cb850572febfea06dbfb20fcc0))
* **bi:** adicionar chamada fetch (GET) para atribuir cookies de sessão do BI ([bdb44b4](http://git.fasolti.com.br/avd/webapp/commit/bdb44b40b9dbcc7fd50b9bac5da40f0f7781d3ed))
* **cargo competencia:** Ajuste no componente de autocomplete de competencias em cargo ([5031cf0](http://git.fasolti.com.br/avd/webapp/commit/5031cf05d1a833e9366b212278235b92db226813))
* **Colaborador:** Ajustes tela ([9cdc554](http://git.fasolti.com.br/avd/webapp/commit/9cdc554f45b2168a63425c38f11b3fe238a56759))
* **components/labeled-input:** desfazer mudanças realizadas para a refatoração do cadastro de colaboradores ([826bff9](http://git.fasolti.com.br/avd/webapp/commit/826bff9fe85db2105fa2d0eeb313075665bac16a))
* **gerar relatorio em aprovacao:** Retirada do botao de gerar relatorio da aba em aprovacao ([#132](http://git.fasolti.com.br/avd/webapp/issues/132)) ([7581ff4](http://git.fasolti.com.br/avd/webapp/commit/7581ff4fe52dee55791c3abdfdf428ab6016b4ca))
* **impressao:** Impressao de relatorios do PDI ([#140](http://git.fasolti.com.br/avd/webapp/issues/140)) ([07cbff6](http://git.fasolti.com.br/avd/webapp/commit/07cbff61e6185ddd1f4ba6e7e910ebc43ca59368))
* **impressao relatorio PDI:** Impressão de relatórios do PDI em PDF ([#132](http://git.fasolti.com.br/avd/webapp/issues/132)) ([5984b26](http://git.fasolti.com.br/avd/webapp/commit/5984b2659fad7efa4444ee764d8e0008ef844b9b))
* **injector, delete:** erro de injeção e serviço para deletar o PDI ([#140](http://git.fasolti.com.br/avd/webapp/issues/140)) ([5325b44](http://git.fasolti.com.br/avd/webapp/commit/5325b444b3600a3f7c6f1431e8cd60d68154fb37))
* **layout:** Ajuste diversos nos layouts do pdi ([#140](http://git.fasolti.com.br/avd/webapp/issues/140)) ([ac5582f](http://git.fasolti.com.br/avd/webapp/commit/ac5582f1db265499551b212a092f219624820886))
* **layout add acao:** Ajuste no dialog de add acao PDI ([#131](http://git.fasolti.com.br/avd/webapp/issues/131)) ([f3ef649](http://git.fasolti.com.br/avd/webapp/commit/f3ef64954bc35901ac8e83a3f9b8d43e5177949d))
* **layout anotacoes:** Ajuste no layout do dialog de anotações ([#146](http://git.fasolti.com.br/avd/webapp/issues/146)) ([7761835](http://git.fasolti.com.br/avd/webapp/commit/77618358a3141245ead2cef231a572f9c799dbd3))
* **login:** Msg de erro de login ([9b55e74](http://git.fasolti.com.br/avd/webapp/commit/9b55e74970b607f2010e95419b652f1ce879bba7))
* **login:** redirecionar de /login para a /login/{empresa} automaticamente se houver apenas uma empresa ativa para o host ([908522b](http://git.fasolti.com.br/avd/webapp/commit/908522b1f7935bc54f45df74871842df0ec0447c))
* **mensageria, notify:** Notificações do mensageria ao chegar uma mensagem nova ([#196](http://git.fasolti.com.br/avd/webapp/issues/196)) ([9ee4dcb](http://git.fasolti.com.br/avd/webapp/commit/9ee4dcbaed08f6931a4e75331705e7c9f0aa5a4b))
* **pagination avaliadores:** Ajuste de paginacao da aba de avaliadores ([a8babd1](http://git.fasolti.com.br/avd/webapp/commit/a8babd167522a50def82b5b8ffbd427453497fdd))
* **pdi:** término do ajuste em pdi ([#140](http://git.fasolti.com.br/avd/webapp/issues/140)) ([2fd0424](http://git.fasolti.com.br/avd/webapp/commit/2fd0424821b3b0929af07bf1f4e1104fa0e97529))
* **pdi list:** Ajuste nos filtros, ordenações e obtenção da foto do funcionario em avatar ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([962e0dc](http://git.fasolti.com.br/avd/webapp/commit/962e0dc3cbfffcaa185ea1f8c63c5a1351f0c0f2))
* **perfil avaliacaoList:** Accordion da lista de avaliações no perfil ([#179](http://git.fasolti.com.br/avd/webapp/issues/179)) ([b48daf1](http://git.fasolti.com.br/avd/webapp/commit/b48daf1426bba3de81a8ec07d76fead6cae3a06e))
* **redefinir senha:** Ao redefinir senha, o usuário é deslogado ([e3e8690](http://git.fasolti.com.br/avd/webapp/commit/e3e8690e93620ce89f65e113c3be6c1955188d66))
* **refactor:** Refatoração do esqueci minha senha ([1925cf9](http://git.fasolti.com.br/avd/webapp/commit/1925cf955ba026a879cbc95ea51c7033f8de6383))
* **refactor:** Refatoração do perfil do usuario logado ([19e32d7](http://git.fasolti.com.br/avd/webapp/commit/19e32d719b6055e40f38297e87b9c1e2ab89618c))
* **relatorio pdi:** Troca das combos por auto complete ([#132](http://git.fasolti.com.br/avd/webapp/issues/132)) ([fb514a5](http://git.fasolti.com.br/avd/webapp/commit/fb514a5cdc18a2911bf39fe1bb7696977302cada))
* **relatorio PDI:** Ajuste no layout de relatorio do PDI ([#147](http://git.fasolti.com.br/avd/webapp/issues/147)) ([1ce898a](http://git.fasolti.com.br/avd/webapp/commit/1ce898a001a3d5d21336cf99e4185cfe0a88a60b))
* **rotas:** Rota das pendencias para cargo e colaboradores ([#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([28d335d](http://git.fasolti.com.br/avd/webapp/commit/28d335d283f5ecaebf3037c6fd78c3a15a883a4e))
* **sort:** Ordenação da lista por situação ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([8a2acf9](http://git.fasolti.com.br/avd/webapp/commit/8a2acf9457ba0aca67d136fc6fd7feb9b19cf37f))
* Ajustes ([caa7e69](http://git.fasolti.com.br/avd/webapp/commit/caa7e6912520d05a93af931dd5f739594d862e13))
* Ativa Inativa colaborador ([#201](http://git.fasolti.com.br/avd/webapp/issues/201)) ([be3aca7](http://git.fasolti.com.br/avd/webapp/commit/be3aca73468e722b959c7925f5be6a8d957a1fdd))
* c ([5d29527](http://git.fasolti.com.br/avd/webapp/commit/5d295270be0775071e30bc4f521413af67f89033))
* Correção env production ([93582b8](http://git.fasolti.com.br/avd/webapp/commit/93582b8bd1ed4e228074cd208e6ab0eb9fbffe8e))
* **aba preenchimento por colaboradores:** Auto avaliação não aparecia ([#187](http://git.fasolti.com.br/avd/webapp/issues/187)) ([584629b](http://git.fasolti.com.br/avd/webapp/commit/584629b0f3c2da313e00af0dcce26a53685cb3f4))
* **acao pdi:** Ajuste ao exibir textArea apenas em av2 ([#161](http://git.fasolti.com.br/avd/webapp/issues/161)) ([2f5b950](http://git.fasolti.com.br/avd/webapp/commit/2f5b95087256a576793f9b1bb501eed146d9e3d6))
* **acao pdi consenso e por colaborador:** Ajuste no botao e textArea, tanto do consenso quanto da aba por colaboradores ([#161](http://git.fasolti.com.br/avd/webapp/issues/161)) ([40741b4](http://git.fasolti.com.br/avd/webapp/commit/40741b4ada91240787c6b5753cb15d2b564c2a89))
* **acompanhamentos:** Ajustes no chart e espaçamento da pesquisa ([#188](http://git.fasolti.com.br/avd/webapp/issues/188)) ([5c15683](http://git.fasolti.com.br/avd/webapp/commit/5c15683a3ffc60d5751d3d719c8fbc89a6b2c59b))
* **cadastros:** introduzir ngInject nos factories ([5518c32](http://git.fasolti.com.br/avd/webapp/commit/5518c324cb069e9756b441c92282deaa5e0cd4cc))
* **cadastros:** remover botão de header "Voltar" duplicado ([f7bc5a2](http://git.fasolti.com.br/avd/webapp/commit/f7bc5a23e2b0d2dd2c9c508888a61a020ab83b53))
* **cadastros/modelograduacao:** passar targetEvent ao DialogDetalhes (para animar a popup corretamente) ([34aa0e7](http://git.fasolti.com.br/avd/webapp/commit/34aa0e72efa92aa18e8abdbcf5aaf5073bbcc992))
* **cadastros/modelograduacao, cadastros/tipocompetencia:** informar ngInject nos factories para transpilar estas funções corretamente ([672e19c](http://git.fasolti.com.br/avd/webapp/commit/672e19c7277cf9215de4c66aa10980c05e16d34b))
* **competencia, escala graduacao, tipo competencia:** Ajuste dos erros encontrados nos testes ([#191](http://git.fasolti.com.br/avd/webapp/issues/191), [#189](http://git.fasolti.com.br/avd/webapp/issues/189), [#190](http://git.fasolti.com.br/avd/webapp/issues/190)) ([e0701cc](http://git.fasolti.com.br/avd/webapp/commit/e0701cc3801261006f2dedca8e2408809e7661da))
* **cores, preenchimento:** Cores nos avatares do preenchimento ([#186](http://git.fasolti.com.br/avd/webapp/issues/186)) ([0a5b7c4](http://git.fasolti.com.br/avd/webapp/commit/0a5b7c4567bdd6177ff491b19e40b8dd68223c10))
* **departamento:** ajuste nas rotas e comportamentos de tela ([#197](http://git.fasolti.com.br/avd/webapp/issues/197)) ([17e2bbf](http://git.fasolti.com.br/avd/webapp/commit/17e2bbf5aa1b4da9ab5c11e48aea80077283236d))
* **dependencies:** Ajuste na versao do jQuery, erro no datetimepicker ([#186](http://git.fasolti.com.br/avd/webapp/issues/186)) ([b9386ab](http://git.fasolti.com.br/avd/webapp/commit/b9386abbb0080c53a43fe2e6223c492b92b4f067))
* **depto edit:** Ajuste ao clicar no botao de edição de um depto ([#197](http://git.fasolti.com.br/avd/webapp/issues/197)) ([6dfa88b](http://git.fasolti.com.br/avd/webapp/commit/6dfa88bd22e12da45dd0de4873236182a330090c))
* **dialog-utils:** remover parâmetros de função duplicados ([acff2dc](http://git.fasolti.com.br/avd/webapp/commit/acff2dca17673229009470db694dfc61f38b2b3e))
* **dialog-utils:** tornar onAction e onCancel opcionais ([4f89ef9](http://git.fasolti.com.br/avd/webapp/commit/4f89ef938fc04010aac438e6354d0fba9490605d))
* **dialog-utils/confirm:** eliminar problemas de injeção de dependência opcionais ao Controller do dialog ([fb65c3d](http://git.fasolti.com.br/avd/webapp/commit/fb65c3d6d8055b585f6d06a8138d9e9d675b9086))
* **dialog-utils/confirm:** retornar `{canceled:false}` ao cancelar ([f4f6c2b](http://git.fasolti.com.br/avd/webapp/commit/f4f6c2bdb9d48d13f7f4505e5b299eec7d501a37))
* **dialog-utils/detalhes:** tornar o segundo argumento de opções opcional ([df6d21d](http://git.fasolti.com.br/avd/webapp/commit/df6d21d297ec141433f4cb167081ddcab3f89a78))
* **dialogs:** Erro de injeção ao abrir uma dialog ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([51b1b1f](http://git.fasolti.com.br/avd/webapp/commit/51b1b1f0dc1bd15a3dd6dd041d4f4a68f2e4028b))
* **grafico:** Ajuste nos totalizadores dos gráficos e inserção do percentual nos labels ([#188](http://git.fasolti.com.br/avd/webapp/issues/188)) ([7eb0d6d](http://git.fasolti.com.br/avd/webapp/commit/7eb0d6d02a132b177d822a4f9ef3cf188021b22d))
* **modelo graduacao:** Ajuste na dialog de graduação e notify de sucesso ([#189](http://git.fasolti.com.br/avd/webapp/issues/189)) ([5f3dd55](http://git.fasolti.com.br/avd/webapp/commit/5f3dd55747a5192cda0135cd746dab390347c2ac))
* **new unidade:** Ajuste de labels ao criar uma nova unidade ([#195](http://git.fasolti.com.br/avd/webapp/issues/195)) ([1b38a29](http://git.fasolti.com.br/avd/webapp/commit/1b38a29e1cb03be916d55e82d3bb8b976e5817e1))
* **ngInject:** Ajuste de injeção do controller ([#192](http://git.fasolti.com.br/avd/webapp/issues/192)) ([a2a5582](http://git.fasolti.com.br/avd/webapp/commit/a2a5582591a20308b2f30afe2796f718b03c01a4))
* **notification msg:** Ajuste da mensagem de notificacao de unidade e modelo graduacao ([#189](http://git.fasolti.com.br/avd/webapp/issues/189)) ([b802565](http://git.fasolti.com.br/avd/webapp/commit/b802565a7ac905fddc0ffde4a51eb93fb803369b))
* **notify duplicate:** Ajuste do toastr para informar erros de validação provenientes do backend ([#197](http://git.fasolti.com.br/avd/webapp/issues/197), [#198](http://git.fasolti.com.br/avd/webapp/issues/198), [#191](http://git.fasolti.com.br/avd/webapp/issues/191), [#190](http://git.fasolti.com.br/avd/webapp/issues/190), [#195](http://git.fasolti.com.br/avd/webapp/issues/195), [#189](http://git.fasolti.com.br/avd/webapp/issues/189)) ([38010fe](http://git.fasolti.com.br/avd/webapp/commit/38010fe28cd8d156a46b8ab292ea2344ecbe0f6b))
* **PDI:** Dialog de edição de PDI ([430fcb0](http://git.fasolti.com.br/avd/webapp/commit/430fcb0443a56b7da35cd842bce8d0f478dccea3))
* **pdi por colaborador:** Ajuste para retirada do textarea e do botao do accordion ([#161](http://git.fasolti.com.br/avd/webapp/issues/161)) ([7271c79](http://git.fasolti.com.br/avd/webapp/commit/7271c7980c67506440122f322ffef3a0b5cb60c4))
* **primeiro acesso:** Ajuste ao abrir a dialog de redefinicao de senha no primeiro acesso ([1fcdf66](http://git.fasolti.com.br/avd/webapp/commit/1fcdf665d45178a4363405432bb810fcb777e9e6))
* **refactor, mensageria:** Refatoração de mensageria ([#196](http://git.fasolti.com.br/avd/webapp/issues/196)) ([b5f02b9](http://git.fasolti.com.br/avd/webapp/commit/b5f02b9d012a5bd362c1d6bdec62ac962bf0726e))
* **remoção:** Remoção do dezi tour, 'formações e idiomas' e 'dimensões' do menu lateral ([#193](http://git.fasolti.com.br/avd/webapp/issues/193), [#194](http://git.fasolti.com.br/avd/webapp/issues/194)) ([e8082ef](http://git.fasolti.com.br/avd/webapp/commit/e8082eff524957732cd2c9d9a34191447f179cbc))
* **situacao, switch:** Ajuste no switch de situacao da competencia ([#191](http://git.fasolti.com.br/avd/webapp/issues/191)) ([b2f08f5](http://git.fasolti.com.br/avd/webapp/commit/b2f08f5f14891301dcb4a5fdb1d4c2f29174303b))
* **styling, logs, login:** Remoção de logs e botão de salvar o preenchimento e ajuste no redirecionamento ao logar-se ([#3](http://git.fasolti.com.br/avd/webapp/issues/3), [#199](http://git.fasolti.com.br/avd/webapp/issues/199)) ([a7b05d1](http://git.fasolti.com.br/avd/webapp/commit/a7b05d1c974cfce203d34837c21878b053611862))
* **styling, setoresPai:** Ajuste na lista de setoresPai e estlização dos componentes do vini ([#197](http://git.fasolti.com.br/avd/webapp/issues/197)) ([04d7778](http://git.fasolti.com.br/avd/webapp/commit/04d777810d5f95c66ca736e82ffd1f1610206b58))
* **success msg:** Mensagem de sucesso ([#197](http://git.fasolti.com.br/avd/webapp/issues/197)) ([764b411](http://git.fasolti.com.br/avd/webapp/commit/764b41168bc1dfa35b0e7d2ab750c5a411ca90e2))
* **translateInterpolation:** alertar sobre dependências circulares entre objetos ([c835fbc](http://git.fasolti.com.br/avd/webapp/commit/c835fbcede06ea001329a477c1a43588a92c6e72))
* **unidade, modelosgraduacao:** Ajuste nos erros de unidade e limpeza de codigo em modelo graduacao ([#195](http://git.fasolti.com.br/avd/webapp/issues/195)) ([bea0f95](http://git.fasolti.com.br/avd/webapp/commit/bea0f95c1ea0870db481c01af9c2ebbfdf5e5c73))
* $mdOpenMenu corrigido para $mdMenu.Open ([62e2fb6](http://git.fasolti.com.br/avd/webapp/commit/62e2fb641b3e3ed110bfb2d38b97ef319ee80da8))
* **Angular:** Package.json Material 1.1.20 ([5475a5e](http://git.fasolti.com.br/avd/webapp/commit/5475a5e1b7bf96cc70abdae5161f4a99eb9c8bcb))
* **componente slick:** Ajuste no componente de Slick dos preenchimentos ([#186](http://git.fasolti.com.br/avd/webapp/issues/186)) ([fb47b6b](http://git.fasolti.com.br/avd/webapp/commit/fb47b6b55a56fa9616825c598075a19886a19c7f))
* Ajuste entity pdi ([708a591](http://git.fasolti.com.br/avd/webapp/commit/708a59107ab5a153a80e93604a5251c9c163a2ad))
* **aba participantes 'avaliados':** Cor da linha de registro ajustada (cinza apenas ao liberar uma avaliacao com aquele avaliado) ([#178](http://git.fasolti.com.br/avd/webapp/issues/178)) ([e2195a0](http://git.fasolti.com.br/avd/webapp/commit/e2195a05991404f439f73f9c72c49f7d6fb679f2))
* **aba pendencias:** Ultima validacao nas pendencias ([#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([87b4066](http://git.fasolti.com.br/avd/webapp/commit/87b406626153e9e65f3942756683a6b797d1fb29))
* **acompanhamento:** Ajuste na ordem dos campos de pesquisa ([#178](http://git.fasolti.com.br/avd/webapp/issues/178)) ([5fbe933](http://git.fasolti.com.br/avd/webapp/commit/5fbe933d29dce360ac056f5f2d0b3cc052b9d839))
* **acompanhamento, mensagem:** Ajuste no envio de mensagem e rotas ([#178](http://git.fasolti.com.br/avd/webapp/issues/178)) ([4d2d398](http://git.fasolti.com.br/avd/webapp/commit/4d2d398f9d6a08a8a7ce5ed1da0e0f03aa605645))
* **add tipo competencia, total percentual:** Ajuste no erro ao adicionar um tipo competencia e ajuste no total percentual ao editar a aba resumo ([#184](http://git.fasolti.com.br/avd/webapp/issues/184), [#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([0de93bd](http://git.fasolti.com.br/avd/webapp/commit/0de93bdbb10a3251fbbcf82eefa6525280de6947))
* **Avaliação:** Ajuste avaliação view model ([14d12b8](http://git.fasolti.com.br/avd/webapp/commit/14d12b817a7064d77025e9b51739e70c15d09b6d))
* **avaliacao-view-model:** remover variável não utilizada ([3679523](http://git.fasolti.com.br/avd/webapp/commit/36795232fa80113604e9e98323945e133d880584))
* **avaliacao, avaliados:** Erro da aba de avaliados ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([37eabb1](http://git.fasolti.com.br/avd/webapp/commit/37eabb1eb09e298fb13c499e5aab4802cbff2f18))
* **botao dialog confirm:** ajuste na cor do botao de cancelar do botao do dialogConfirm ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([472eb0a](http://git.fasolti.com.br/avd/webapp/commit/472eb0a276e9d8447fff3d349bc3285027646b50))
* **cadastros/modelosgraduacao/edit:** corrigir botão "Cancelar" para voltar para a página correta ([3532895](http://git.fasolti.com.br/avd/webapp/commit/3532895fea36fc5996faded7dcb9630398bffe2d))
* **Campanha:** Correção filtro em lista ([847d17b](http://git.fasolti.com.br/avd/webapp/commit/847d17b7c5772cff8e9044adeca27e4f961e7a88))
* **checkboxes e datalimite aba resumo, mensagem aba pendencias:** Ajuste na mensagem caso haja pendencias. Ajustes nos comportamentos das checkboxes dos agentes e ajuste na mensagem de validação nos componentes de data ([#176](http://git.fasolti.com.br/avd/webapp/issues/176), [#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([05e1707](http://git.fasolti.com.br/avd/webapp/commit/05e1707cb2a84e1b75368a6181a26440522ce566))
* **ci:** adaptar script de build ao webpack ([0b56ec9](http://git.fasolti.com.br/avd/webapp/commit/0b56ec9e9f284b550fd3b38b240e7fdab87f0d6a))
* **ci:** servir arquivos corretos no web server ([8e0e02d](http://git.fasolti.com.br/avd/webapp/commit/8e0e02d3aa0f5019fe49eb2e9011ffa4c61dad9e))
* **component, labeled-input:** Ajuste para aceitar a lista de booleanos ([0e2a1e1](http://git.fasolti.com.br/avd/webapp/commit/0e2a1e1350d2983dfdef10b55badefe534d2599a))
* **componente data:** Ajuste no componente de data do resumo da avaliacao ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([1f458db](http://git.fasolti.com.br/avd/webapp/commit/1f458db533cd4ac3a3a9dde6b1fec7b2f8976b59))
* **confirmDialog, aba avaliados:** Ajuste no confirmDialog e ao remover um participante das pendencias ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([3d383ac](http://git.fasolti.com.br/avd/webapp/commit/3d383ac54d307f69cfc111b02b7ea9cb6d6f416e))
* **dialog add acao:** Ajuste ao trazer o participante ([da5514e](http://git.fasolti.com.br/avd/webapp/commit/da5514e5166c569a701344f4acae497f34c23562))
* **dialog participantes 'avaliados':** Ajuste na dialog de contagem de avaliados inseridos/removidos ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([7051e32](http://git.fasolti.com.br/avd/webapp/commit/7051e320ac017dfeb901b94f2a89ef772d501309))
* **dialog participantes 'avaliados':** Dialog com o contador apenas para situação liberada ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([7771c6c](http://git.fasolti.com.br/avd/webapp/commit/7771c6cc011acbbe855d96b1bb91747f1f348a89))
* **dialog, avaliadores:** Injeção da Dialog de adição de avaliadores ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([d8c5364](http://git.fasolti.com.br/avd/webapp/commit/d8c5364a17b9f70b8a98c32ebaed17c27c2d5d17))
* **erro dialogConfirm:** Erro de injeção no dialogConfirm.show ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([84fa3db](http://git.fasolti.com.br/avd/webapp/commit/84fa3db1d7dcd46a902f87d6e83b896a6284705f))
* **internacionalizacao:** Menu de idiomas no funcionario. ([8fbab17](http://git.fasolti.com.br/avd/webapp/commit/8fbab172d4da1c6257f520b50865ca03dd0eae01))
* **label, data limite:** Retirada do label de data limite ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([b359c4c](http://git.fasolti.com.br/avd/webapp/commit/b359c4cc7d05de81ba529a00f06646e19b10fb13))
* **liberacao avaliacao:** ajuste na dialog de liberação da avaliacao ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([a7efaff](http://git.fasolti.com.br/avd/webapp/commit/a7efaff25d483c8a16f1d73b8e8b4e58f7033a05))
* **liberacao de participantes:** Ajuste na liberação de participantes e nas mensagens de validação ([#184](http://git.fasolti.com.br/avd/webapp/issues/184)) ([7c070b8](http://git.fasolti.com.br/avd/webapp/commit/7c070b8f519861da61d85c94d62192dda1638e40))
* **list participante, labeled input:** Ajuste na requisição de list participante e required no labeled-input ([#131](http://git.fasolti.com.br/avd/webapp/issues/131)) ([cf106fb](http://git.fasolti.com.br/avd/webapp/commit/cf106fb5529b419f75ae2aefa8fe73d60132543e))
* **Mensagens:** Renomeado funcionario para usuario. ([3b61b07](http://git.fasolti.com.br/avd/webapp/commit/3b61b07bd44855c80697ce69e70b8c9d2dc32596))
* **Mensagens:** Retirado implementação de UserDetails de UserTo que causa erro. ([cb99cad](http://git.fasolti.com.br/avd/webapp/commit/cb99cadf85dab5627f6cdf18ffca0faad6f65259))
* **participantes 'avaliados' contador:** contador de participantes inseridos/removidos da avaliação ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([121c60b](http://git.fasolti.com.br/avd/webapp/commit/121c60b01b20c28e0c1177545d81d1faa8e9e1fb))
* **PDI:** Ajustes merge ([3c09deb](http://git.fasolti.com.br/avd/webapp/commit/3c09deb3768b2873ef3510516e158d78741fc1d4))
* **pendencias:** Ajuste de regras na aba de pendencias ([#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([06491ea](http://git.fasolti.com.br/avd/webapp/commit/06491eac054a57b5b1f3a089c1b3a1bec54aaf46))
* **pendencias, mensagem:** Ajuste na mensagem ao remover uma pendencia ([#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([6dc89a2](http://git.fasolti.com.br/avd/webapp/commit/6dc89a2888965bac97b22df61ec8a01782f476c0))
* **provider error:** Erro de injeção do provider ([#176](http://git.fasolti.com.br/avd/webapp/issues/176),  [#181](http://git.fasolti.com.br/avd/webapp/issues/181), [#177](http://git.fasolti.com.br/avd/webapp/issues/177)) ([27eaef4](http://git.fasolti.com.br/avd/webapp/commit/27eaef43fa0c044073256fb8b9ec31594f2aef48))
* **provider error:** Erro de provider, ajuste no preenchimento e inject nos routeConfigs ([#180](http://git.fasolti.com.br/avd/webapp/issues/180), [#181](http://git.fasolti.com.br/avd/webapp/issues/181)) ([a56ca44](http://git.fasolti.com.br/avd/webapp/commit/a56ca44c04a6cb8e82e38cdd1e56a56a2df70b01))
* **refactor, participantes:** imutabilidade nos participantes ([#125](http://git.fasolti.com.br/avd/webapp/issues/125)) ([76d9b31](http://git.fasolti.com.br/avd/webapp/commit/76d9b31b80883fed1c082882bcf5adfc8cd7ade9))
* **relatorio, melhorias:** Ajustes ao gerar um relatório e ajuste no serviço do PDI ([#147](http://git.fasolti.com.br/avd/webapp/issues/147)) ([bb9bd56](http://git.fasolti.com.br/avd/webapp/commit/bb9bd5658e746ee207d84f14bd5b9bdea6f20bd1))
* **table, relatorios:** Ajuste nas tabelas do relatorio ([#125](http://git.fasolti.com.br/avd/webapp/issues/125)) ([05944b0](http://git.fasolti.com.br/avd/webapp/commit/05944b02fb0c48b3f7c9c633d42517ef4d104ebb))
* **Troca Senha:** Logout em toda troca de senha ([74b8a02](http://git.fasolti.com.br/avd/webapp/commit/74b8a02bf2062837ca58ce1c5117bf32411aa733))
* Adicioinado css do fuse ([8bcb2b9](http://git.fasolti.com.br/avd/webapp/commit/8bcb2b93b9e642a51c555459f0fcc6e947e26d50))
* adicionado diretório do intellij ([b4b7881](http://git.fasolti.com.br/avd/webapp/commit/b4b78816697e5cc65d929d994711ea5ac64e6956))
* Ajsute de senha forte e fraca ([7bfa48e](http://git.fasolti.com.br/avd/webapp/commit/7bfa48ec597f0aa24c419b6c1b2e82c201c00c4c))
* Ajustes Eslint ([302180e](http://git.fasolti.com.br/avd/webapp/commit/302180ed9a49de7af062c10fdf81435e6e9e2399))
* Arquivo não usado ([6d4176e](http://git.fasolti.com.br/avd/webapp/commit/6d4176e8a256445bbac41f2334aebeb13902ae0a))
* Configuração para production ([507c7d8](http://git.fasolti.com.br/avd/webapp/commit/507c7d85a6e914a8f09e6f6d268aa623fc309bea))
* Correção arquivo de traduçao do dialogo Dimensão ([588f91b](http://git.fasolti.com.br/avd/webapp/commit/588f91b69952c8b0f278a378228296f1114fbdee))
* correção css fuse faltando ([740c66c](http://git.fasolti.com.br/avd/webapp/commit/740c66cd7b47eb847686cda00dcdded0d4fa6514))
* correção da class css header_fixo ([278f9d1](http://git.fasolti.com.br/avd/webapp/commit/278f9d149761b2689fdea00e81ec99722c9d7e77))
* Correção da flags.png botão tradução ([d3a9470](http://git.fasolti.com.br/avd/webapp/commit/d3a9470e6d92e4089d158aac85e67f7374b67738))
* Correção da ordem dos links em cadastros ([026be54](http://git.fasolti.com.br/avd/webapp/commit/026be54a9ced701f3a6f6552b1149784cdaba407))
* Correção de function e const/var para let ([2a4dd59](http://git.fasolti.com.br/avd/webapp/commit/2a4dd59b1ddb3e8f4ec11c74fcbd05277dda90b3))
* Correção de tradução Escala de Graduação ([d52aa88](http://git.fasolti.com.br/avd/webapp/commit/d52aa88c5dbef53dc84779545428e4527bc0ebe0))
* correção mensagem de erro na tela de login ([8992c89](http://git.fasolti.com.br/avd/webapp/commit/8992c89c6a8c5cc4c29c464a3ba981ef206bcb62))
* Correção na função ativar competencia ([ac04a5a](http://git.fasolti.com.br/avd/webapp/commit/ac04a5ab2c275830d7423697dd6b4ad6dc19004b))
* Correção na função ativar escala graduação ([fd20cbf](http://git.fasolti.com.br/avd/webapp/commit/fd20cbf0cefe2ebf70a62998bbe1508150129669))
* Correção nome de atributo e retirada de log ([8e24bc6](http://git.fasolti.com.br/avd/webapp/commit/8e24bc6806c226e455c086b95442b10ff67baa17))
* Correção para datapicker ([f0f1db6](http://git.fasolti.com.br/avd/webapp/commit/f0f1db6d5ceb6a422b76a54111648f176bcb25c4))
* Correção serviços $restServer ([0cff786](http://git.fasolti.com.br/avd/webapp/commit/0cff786d1a1a94a868abe3bfaf401a6b23ca0ffd))
* Correção tradução de dimensão ([cbf455f](http://git.fasolti.com.br/avd/webapp/commit/cbf455f2f2dc5734c83646d7e1365326a42d118f))
* Correções arquivo de config producao ([b8d1600](http://git.fasolti.com.br/avd/webapp/commit/b8d1600742d8f3d9ea1f835394e68b00ad6a75cb))
* Dialog trocar senha ([5fcc219](http://git.fasolti.com.br/avd/webapp/commit/5fcc219918a258ccd976ae4fbe09536f6b4e6960))
* Erro ao enviar mensagem em cadastro de avaliação ([c9f985b](http://git.fasolti.com.br/avd/webapp/commit/c9f985b03b1d64bea507a088eda36b86c4cc594b))
* Erro de obj chamado id substituindo [@id](http://git.fasolti.com.br/id) do re ([94b7f39](http://git.fasolti.com.br/avd/webapp/commit/94b7f39eab49dbd211e860403b5f793654069b3b))
* Fuse já importado no main.scss ([42a1ed6](http://git.fasolti.com.br/avd/webapp/commit/42a1ed61b22e16bc654c41b389cafec80fa79449))
* melhorias .ignore do git ([63821d6](http://git.fasolti.com.br/avd/webapp/commit/63821d62ded339ecde051dde68f1dc3f25252b99))
* Não editar avaliação concluída ([8e8bbca](http://git.fasolti.com.br/avd/webapp/commit/8e8bbcad986b370251d9cac68b8522a20117612b))
* Não funciona com a extensão .json ([7127acd](http://git.fasolti.com.br/avd/webapp/commit/7127acd8d937b874915362119414f5957e09222e))
* Remover somente avaliação ag. liberação ([62b74d1](http://git.fasolti.com.br/avd/webapp/commit/62b74d148f83ef1e012391189ef6ab6b798b5a6a))
* Rest não apontando mais para localhost ([6fad624](http://git.fasolti.com.br/avd/webapp/commit/6fad624f493fff693e7fed4d1790940d93f8d2ac))
* Retirada js não utilizado ([186b95d](http://git.fasolti.com.br/avd/webapp/commit/186b95d674adcbe50ce91880f116f0b11a87b04f))
* retirado comentário ([2392b84](http://git.fasolti.com.br/avd/webapp/commit/2392b84429d25727a53aa1d84cde871c64ecba1a))
* Validação de senha forte em primeiro  acesso ([5588ef7](http://git.fasolti.com.br/avd/webapp/commit/5588ef7738685f91c3421fccb5033924af3e69d6))
* Validação de senha forte em primeiro  acesso ([4fd4392](http://git.fasolti.com.br/avd/webapp/commit/4fd4392f0b2f46d413e2ba6fa473ee7dbdf3c85f))
* variáveis const trocadas para let para receber valor ([e24aff7](http://git.fasolti.com.br/avd/webapp/commit/e24aff7b898c1b998e715d640d4c27234a3dfe96))

# 3.1.0 (2019-11-08)


### Bug Fixes

* Adicioinado css do fuse ([8bcb2b9](http://git.fasolti.com.br/avd/webapp/commits/8bcb2b9))
* adicionado diretório do intellij ([b4b7881](http://git.fasolti.com.br/avd/webapp/commits/b4b7881))
* **ci:** servir arquivos corretos no web server ([8e0e02d](http://git.fasolti.com.br/avd/webapp/commits/8e0e02d))
* Ajsute de senha forte e fraca ([7bfa48e](http://git.fasolti.com.br/avd/webapp/commits/7bfa48e))
* **avaliacao-view-model:** remover variável não utilizada ([3679523](http://git.fasolti.com.br/avd/webapp/commits/3679523))
* Ajuste entity pdi ([708a591](http://git.fasolti.com.br/avd/webapp/commits/708a591))
* **aba participantes 'avaliados':** Cor da linha de registro ajustada (cinza apenas ao liberar uma avaliacao com aquele avaliado) ([#178](http://git.fasolti.com.br/avd/webapp/issues/178)) ([e2195a0](http://git.fasolti.com.br/avd/webapp/commits/e2195a0))
* **aba pendencias:** Ultima validacao nas pendencias ([#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([87b4066](http://git.fasolti.com.br/avd/webapp/commits/87b4066))
* **acompanhamento:** Ajuste na ordem dos campos de pesquisa ([#178](http://git.fasolti.com.br/avd/webapp/issues/178)) ([5fbe933](http://git.fasolti.com.br/avd/webapp/commits/5fbe933))
* **acompanhamento, mensagem:** Ajuste no envio de mensagem e rotas ([#178](http://git.fasolti.com.br/avd/webapp/issues/178)) ([4d2d398](http://git.fasolti.com.br/avd/webapp/commits/4d2d398))
* **add tipo competencia, total percentual:** Ajuste no erro ao adicionar um tipo competencia e ajuste no total percentual ao editar a aba resumo ([#184](http://git.fasolti.com.br/avd/webapp/issues/184), [#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([0de93bd](http://git.fasolti.com.br/avd/webapp/commits/0de93bd))
* **Angular:** Package.json Material 1.1.20 ([5475a5e](http://git.fasolti.com.br/avd/webapp/commits/5475a5e))
* **Avaliação:** Ajuste avaliação view model ([14d12b8](http://git.fasolti.com.br/avd/webapp/commits/14d12b8))
* **avaliacao, avaliados:** Erro da aba de avaliados ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([37eabb1](http://git.fasolti.com.br/avd/webapp/commits/37eabb1))
* **botao dialog confirm:** ajuste na cor do botao de cancelar do botao do dialogConfirm ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([472eb0a](http://git.fasolti.com.br/avd/webapp/commits/472eb0a))
* **cadastros/modelosgraduacao/edit:** corrigir botão "Cancelar" para voltar para a página correta ([3532895](http://git.fasolti.com.br/avd/webapp/commits/3532895))
* **Campanha:** Correção filtro em lista ([847d17b](http://git.fasolti.com.br/avd/webapp/commits/847d17b))
* **checkboxes e datalimite aba resumo, mensagem aba pendencias:** Ajuste na mensagem caso haja pendencias. Ajustes nos comportamentos das checkboxes dos agentes e ajuste na mensagem de validação nos componentes de data ([#176](http://git.fasolti.com.br/avd/webapp/issues/176), [#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([05e1707](http://git.fasolti.com.br/avd/webapp/commits/05e1707))
* **ci:** adaptar script de build ao webpack ([0b56ec9](http://git.fasolti.com.br/avd/webapp/commits/0b56ec9))
* **component, labeled-input:** Ajuste para aceitar a lista de booleanos ([0e2a1e1](http://git.fasolti.com.br/avd/webapp/commits/0e2a1e1))
* **componente data:** Ajuste no componente de data do resumo da avaliacao ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([1f458db](http://git.fasolti.com.br/avd/webapp/commits/1f458db))
* **confirmDialog, aba avaliados:** Ajuste no confirmDialog e ao remover um participante das pendencias ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([3d383ac](http://git.fasolti.com.br/avd/webapp/commits/3d383ac))
* **dialog add acao:** Ajuste ao trazer o participante ([da5514e](http://git.fasolti.com.br/avd/webapp/commits/da5514e))
* **dialog participantes 'avaliados':** Ajuste na dialog de contagem de avaliados inseridos/removidos ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([7051e32](http://git.fasolti.com.br/avd/webapp/commits/7051e32))
* **dialog participantes 'avaliados':** Dialog com o contador apenas para situação liberada ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([7771c6c](http://git.fasolti.com.br/avd/webapp/commits/7771c6c))
* **dialog, avaliadores:** Injeção da Dialog de adição de avaliadores ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([d8c5364](http://git.fasolti.com.br/avd/webapp/commits/d8c5364))
* **erro dialogConfirm:** Erro de injeção no dialogConfirm.show ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([84fa3db](http://git.fasolti.com.br/avd/webapp/commits/84fa3db))
* **internacionalizacao:** Menu de idiomas no funcionario. ([8fbab17](http://git.fasolti.com.br/avd/webapp/commits/8fbab17))
* **label, data limite:** Retirada do label de data limite ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([b359c4c](http://git.fasolti.com.br/avd/webapp/commits/b359c4c))
* **liberacao avaliacao:** ajuste na dialog de liberação da avaliacao ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([a7efaff](http://git.fasolti.com.br/avd/webapp/commits/a7efaff))
* **liberacao de participantes:** Ajuste na liberação de participantes e nas mensagens de validação ([#184](http://git.fasolti.com.br/avd/webapp/issues/184)) ([7c070b8](http://git.fasolti.com.br/avd/webapp/commits/7c070b8))
* **list participante, labeled input:** Ajuste na requisição de list participante e required no labeled-input ([#131](http://git.fasolti.com.br/avd/webapp/issues/131)) ([cf106fb](http://git.fasolti.com.br/avd/webapp/commits/cf106fb))
* **Mensagens:** Renomeado funcionario para usuario. ([3b61b07](http://git.fasolti.com.br/avd/webapp/commits/3b61b07))
* **Mensagens:** Retirado implementação de UserDetails de UserTo que causa erro. ([cb99cad](http://git.fasolti.com.br/avd/webapp/commits/cb99cad))
* **participantes 'avaliados' contador:** contador de participantes inseridos/removidos da avaliação ([#176](http://git.fasolti.com.br/avd/webapp/issues/176)) ([121c60b](http://git.fasolti.com.br/avd/webapp/commits/121c60b))
* **PDI:** Ajustes merge ([3c09deb](http://git.fasolti.com.br/avd/webapp/commits/3c09deb))
* **pendencias:** Ajuste de regras na aba de pendencias ([#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([06491ea](http://git.fasolti.com.br/avd/webapp/commits/06491ea))
* **pendencias, mensagem:** Ajuste na mensagem ao remover uma pendencia ([#183](http://git.fasolti.com.br/avd/webapp/issues/183)) ([6dc89a2](http://git.fasolti.com.br/avd/webapp/commits/6dc89a2))
* **provider error:** Erro de injeção do provider ([#176](http://git.fasolti.com.br/avd/webapp/issues/176),  [#181](http://git.fasolti.com.br/avd/webapp/issues/181), [#177](http://git.fasolti.com.br/avd/webapp/issues/177)) ([27eaef4](http://git.fasolti.com.br/avd/webapp/commits/27eaef4))
* **provider error:** Erro de provider, ajuste no preenchimento e inject nos routeConfigs ([#180](http://git.fasolti.com.br/avd/webapp/issues/180), [#181](http://git.fasolti.com.br/avd/webapp/issues/181)) ([a56ca44](http://git.fasolti.com.br/avd/webapp/commits/a56ca44))
* **refactor, participantes:** imutabilidade nos participantes ([#125](http://git.fasolti.com.br/avd/webapp/issues/125)) ([76d9b31](http://git.fasolti.com.br/avd/webapp/commits/76d9b31))
* **relatorio, melhorias:** Ajustes ao gerar um relatório e ajuste no serviço do PDI ([#147](http://git.fasolti.com.br/avd/webapp/issues/147)) ([bb9bd56](http://git.fasolti.com.br/avd/webapp/commits/bb9bd56))
* retirado comentário ([2392b84](http://git.fasolti.com.br/avd/webapp/commits/2392b84))
* **table, relatorios:** Ajuste nas tabelas do relatorio ([#125](http://git.fasolti.com.br/avd/webapp/issues/125)) ([05944b0](http://git.fasolti.com.br/avd/webapp/commits/05944b0))
* Configuração para production ([507c7d8](http://git.fasolti.com.br/avd/webapp/commits/507c7d8))
* Correção arquivo de traduçao do dialogo Dimensão ([588f91b](http://git.fasolti.com.br/avd/webapp/commits/588f91b))
* correção css fuse faltando ([740c66c](http://git.fasolti.com.br/avd/webapp/commits/740c66c))
* correção da class css header_fixo ([278f9d1](http://git.fasolti.com.br/avd/webapp/commits/278f9d1))
* Correção da flags.png botão tradução ([d3a9470](http://git.fasolti.com.br/avd/webapp/commits/d3a9470))
* Correção da ordem dos links em cadastros ([026be54](http://git.fasolti.com.br/avd/webapp/commits/026be54))
* Correção de function e const/var para let ([2a4dd59](http://git.fasolti.com.br/avd/webapp/commits/2a4dd59))
* Correção de tradução Escala de Graduação ([d52aa88](http://git.fasolti.com.br/avd/webapp/commits/d52aa88))
* correção mensagem de erro na tela de login ([8992c89](http://git.fasolti.com.br/avd/webapp/commits/8992c89))
* Correção na função ativar competencia ([ac04a5a](http://git.fasolti.com.br/avd/webapp/commits/ac04a5a))
* Correção na função ativar escala graduação ([fd20cbf](http://git.fasolti.com.br/avd/webapp/commits/fd20cbf))
* Correção nome de atributo e retirada de log ([8e24bc6](http://git.fasolti.com.br/avd/webapp/commits/8e24bc6))
* Correção para datapicker ([f0f1db6](http://git.fasolti.com.br/avd/webapp/commits/f0f1db6))
* Correção serviços $restServer ([0cff786](http://git.fasolti.com.br/avd/webapp/commits/0cff786))
* Correção tradução de dimensão ([cbf455f](http://git.fasolti.com.br/avd/webapp/commits/cbf455f))
* Correções arquivo de config producao ([b8d1600](http://git.fasolti.com.br/avd/webapp/commits/b8d1600))
* Dialog trocar senha ([5fcc219](http://git.fasolti.com.br/avd/webapp/commits/5fcc219))
* Erro ao enviar mensagem em cadastro de avaliação ([c9f985b](http://git.fasolti.com.br/avd/webapp/commits/c9f985b))
* Erro de obj chamado id substituindo [@id](http://git.fasolti.com.br/id) do re ([94b7f39](http://git.fasolti.com.br/avd/webapp/commits/94b7f39))
* Fuse já importado no main.scss ([42a1ed6](http://git.fasolti.com.br/avd/webapp/commits/42a1ed6))
* melhorias .ignore do git ([63821d6](http://git.fasolti.com.br/avd/webapp/commits/63821d6))
* Não editar avaliação concluída ([8e8bbca](http://git.fasolti.com.br/avd/webapp/commits/8e8bbca))
* **Troca Senha:** Logout em toda troca de senha ([74b8a02](http://git.fasolti.com.br/avd/webapp/commits/74b8a02))
* Não funciona com a extensão .json ([7127acd](http://git.fasolti.com.br/avd/webapp/commits/7127acd))
* Remover somente avaliação ag. liberação ([62b74d1](http://git.fasolti.com.br/avd/webapp/commits/62b74d1))
* Rest não apontando mais para localhost ([6fad624](http://git.fasolti.com.br/avd/webapp/commits/6fad624))
* Retirada js não utilizado ([186b95d](http://git.fasolti.com.br/avd/webapp/commits/186b95d))
* Validação de senha forte em primeiro  acesso ([5588ef7](http://git.fasolti.com.br/avd/webapp/commits/5588ef7))
* Validação de senha forte em primeiro  acesso ([4fd4392](http://git.fasolti.com.br/avd/webapp/commits/4fd4392))
* variáveis const trocadas para let para receber valor ([e24aff7](http://git.fasolti.com.br/avd/webapp/commits/e24aff7))


### Features

* Ativação, Inativação e Remoção de Avaliação ([b889c4a](http://git.fasolti.com.br/avd/webapp/commits/b889c4a))
* controller de campanha renomeados ([88fc0a2](http://git.fasolti.com.br/avd/webapp/commits/88fc0a2))
* criar serviço AvaliacaoViewModel ([543e613](http://git.fasolti.com.br/avd/webapp/commits/543e613))
* Editar Dialog Ação de desenvolvimento ([fbe20a5](http://git.fasolti.com.br/avd/webapp/commits/fbe20a5))
* **PDI:** Arquivos iniciais PDI ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([efecd9e](http://git.fasolti.com.br/avd/webapp/commits/efecd9e))
* Esqueci minha senha somente pesquisa por email ([#43](http://git.fasolti.com.br/avd/webapp/issues/43)) ([943f1aa](http://git.fasolti.com.br/avd/webapp/commits/943f1aa))
* **Avaliacao:** Ajustes tela de Avaliação ([1dbd991](http://git.fasolti.com.br/avd/webapp/commits/1dbd991))
* **Avaliacao:** Nova e edição de Avaliacao ([ef47144](http://git.fasolti.com.br/avd/webapp/commits/ef47144))
* **Avaliacao:** View de Avaliação ([adba166](http://git.fasolti.com.br/avd/webapp/commits/adba166))
* **avaliadores/view-model:** criar loadAvaliadores ([117f967](http://git.fasolti.com.br/avd/webapp/commits/117f967))
* **campanha:** Dialogo Detalhes ([41e8ed8](http://git.fasolti.com.br/avd/webapp/commits/41e8ed8))
* **campanha:** Edição de campanha ([06b0c0e](http://git.fasolti.com.br/avd/webapp/commits/06b0c0e))
* **Campanha:** ação Repetir Avaliação ([8525f54](http://git.fasolti.com.br/avd/webapp/commits/8525f54))
* **Campanha:** Lista de Campanhas ([95deaa6](http://git.fasolti.com.br/avd/webapp/commits/95deaa6))
* **campo acaoPDI, services:** Ajuste no serviço de salvar competencias e criação do campo acao para PDI([#161](http://git.fasolti.com.br/avd/webapp/issues/161)) ([93f5e06](http://git.fasolti.com.br/avd/webapp/commits/93f5e06))
* **campos de justificativa; salvar pedindo senha:** Campos de jusitificativa avaliado/avaliador e save pedindo a senha ([#3](http://git.fasolti.com.br/avd/webapp/issues/3)) ([a900bd7](http://git.fasolti.com.br/avd/webapp/commits/a900bd7))
* **DatePicker:** Adição de date Picker ([070cc11](http://git.fasolti.com.br/avd/webapp/commits/070cc11))
* **dialog, add-acao:** Dialog de add ação ([#131](http://git.fasolti.com.br/avd/webapp/issues/131)) ([7f43454](http://git.fasolti.com.br/avd/webapp/commits/7f43454))
* **funcionario; roles:** Ajuste no menu lateral a partir da role do usuario logado ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([d7d82ee](http://git.fasolti.com.br/avd/webapp/commits/d7d82ee))
* **package.json:** Adicionado jquery-datetimepicker ([25b08f4](http://git.fasolti.com.br/avd/webapp/commits/25b08f4))
* **PDI:** Abas de PDI ([993cc2a](http://git.fasolti.com.br/avd/webapp/commits/993cc2a))
* **PDI:** Add estrutura dos dialogs ([42f8087](http://git.fasolti.com.br/avd/webapp/commits/42f8087))
* **PDI:** Ajuste do novo MenuItens ([33e1b9d](http://git.fasolti.com.br/avd/webapp/commits/33e1b9d))
* **PDI:** Ajuste em iniciar, finalizar acao ([b9e3dcc](http://git.fasolti.com.br/avd/webapp/commits/b9e3dcc))
* **PDI:** Ajuste foto e testo principal ([7bc625f](http://git.fasolti.com.br/avd/webapp/commits/7bc625f))
* **PDI:** Ajuste merge ([436b781](http://git.fasolti.com.br/avd/webapp/commits/436b781))
* **PDI:** Ajuste no menu para relatório PR ([e2cd779](http://git.fasolti.com.br/avd/webapp/commits/e2cd779))
* **PDI:** Ajustes dialog inicar e finalizar ([d39b664](http://git.fasolti.com.br/avd/webapp/commits/d39b664))
* **PDI:** Ajustes Lista de pdi ([3ec96b5](http://git.fasolti.com.br/avd/webapp/commits/3ec96b5))
* **PDI:** Ajustes Relatório ([aa289d2](http://git.fasolti.com.br/avd/webapp/commits/aa289d2))
* **PDI:** Ajustes tela, abas e dialogs ([f5c15bd](http://git.fasolti.com.br/avd/webapp/commits/f5c15bd))
* **PDI:** Combos do dialog anotações ([a3d3046](http://git.fasolti.com.br/avd/webapp/commits/a3d3046))
* **PDI:** Dialog Iniciar Ação dev ([d520bee](http://git.fasolti.com.br/avd/webapp/commits/d520bee))
* **PDI:** Estrutura Abas PDI Cards ([2a2efc5](http://git.fasolti.com.br/avd/webapp/commits/2a2efc5))
* **PDI:** Estrutura das Abas ([b4e9a87](http://git.fasolti.com.br/avd/webapp/commits/b4e9a87))
* **PDI:** Estruturas de dialogs ([ac1e9cd](http://git.fasolti.com.br/avd/webapp/commits/ac1e9cd))
* **PDI:** Estrututra dialog ações e anotações ([22fa96d](http://git.fasolti.com.br/avd/webapp/commits/22fa96d))
* **PDI:** Id funcionário por parametro ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([61f1524](http://git.fasolti.com.br/avd/webapp/commits/61f1524))
* **PDI:** incrusão de funcionario no modelo PDI ([e1bff30](http://git.fasolti.com.br/avd/webapp/commits/e1bff30))
* **PDI:** PDI - Relatórios - PR ([b436ba0](http://git.fasolti.com.br/avd/webapp/commits/b436ba0))
* **PDI:** PDI - Relatórios - PR ([84e619f](http://git.fasolti.com.br/avd/webapp/commits/84e619f))
* **PDI:** PDI lista de funcionário ([#4](http://git.fasolti.com.br/avd/webapp/issues/4)) ([810595a](http://git.fasolti.com.br/avd/webapp/commits/810595a))
* **PDI:** Relatório individual([#132](http://git.fasolti.com.br/avd/webapp/issues/132)) ([dce7076](http://git.fasolti.com.br/avd/webapp/commits/dce7076))
* **PDI:** Serviço de PDI e preenchimento das Abas ([1521634](http://git.fasolti.com.br/avd/webapp/commits/1521634))
* **PDI:** Serviços PDI ([02a5f93](http://git.fasolti.com.br/avd/webapp/commits/02a5f93))
* **refactor, participantes:** Transferencia da logica de seleção de um/todos funcionario(s) para o avaliacao-view-model ([#125](http://git.fasolti.com.br/avd/webapp/issues/125)) ([e89c04b](http://git.fasolti.com.br/avd/webapp/commits/e89c04b))
* **refactor, relatorios:** Refatoração de relatorios concluida ([#125](http://git.fasolti.com.br/avd/webapp/issues/125)) ([c88ddc0](http://git.fasolti.com.br/avd/webapp/commits/c88ddc0))
* **relatorio, filters:** Filtro de campanha e avaliação ([#147](http://git.fasolti.com.br/avd/webapp/issues/147)) ([f2ba910](http://git.fasolti.com.br/avd/webapp/commits/f2ba910))
* **relatorio; exportar:** Exportação da ação para excel ([#147](http://git.fasolti.com.br/avd/webapp/issues/147)) ([75c64cd](http://git.fasolti.com.br/avd/webapp/commits/75c64cd))
* **utils:** adicionar routes.js ([594300c](http://git.fasolti.com.br/avd/webapp/commits/594300c))
* **utils/MultiListSelection:** adicionar inicialização opcional ([1d5695a](http://git.fasolti.com.br/avd/webapp/commits/1d5695a))
* **utils/MultiListSelection:** adicionar inicialização opcional ([52e01c0](http://git.fasolti.com.br/avd/webapp/commits/52e01c0))
* importação e organização de CSS ([4285660](http://git.fasolti.com.br/avd/webapp/commits/4285660))
* Lista de cargos em Cadastro ([738b7a4](http://git.fasolti.com.br/avd/webapp/commits/738b7a4))
* Lista de competência em Cadastro ([155534c](http://git.fasolti.com.br/avd/webapp/commits/155534c))
* Lista de dimensão em Cadastro ([0b0dc14](http://git.fasolti.com.br/avd/webapp/commits/0b0dc14))
* Lista de Escala de Graduação em Cadastro ([e96f93b](http://git.fasolti.com.br/avd/webapp/commits/e96f93b))
* Lista de Setores em Cadastro ([a0e6c49](http://git.fasolti.com.br/avd/webapp/commits/a0e6c49))
* Lista de tipo de competência em Cadastro ([46c340b](http://git.fasolti.com.br/avd/webapp/commits/46c340b))
* Mensagem de erro no login para 403 e 404([#64](http://git.fasolti.com.br/avd/webapp/issues/64)) ([4206779](http://git.fasolti.com.br/avd/webapp/commits/4206779))
