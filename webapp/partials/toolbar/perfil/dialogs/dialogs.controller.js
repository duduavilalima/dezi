// /* @ngInject */
// const dialogFuncionarioFormacao = (
//     $scope,
//     $mdDialog,
//     title,
//     fn_Save,
//     fn_Cancel,
//     obj,
//     notification,
//     escolaridadeResource,
//     tipoCursoResource,
//     cursoGraduacaoResource,
//     cursoEspecializacaoResource,
//     situacaoResource,
// ) => {
//     //O obj é a lista de formacoes!
//     $scope.object = obj;

//     if ($scope.object == null) {
//         $scope.object = {};
//         funcionarioFormacaoResource.get()
//             .$promise.then(function (data) {
//                 $scope.object = data;
//             }).catch(function (response) {
//                 notification.criticalError(response.status, response.statusText, response.data);
//             });
//     } else {
//         funcionarioFormacaoResource.get({ id: $scope.object.id })
//             .$promise.then(function (data) {
//                 $scope.object = data;
//             }).catch(function (response) {
//                 notification.criticalError(response.status, response.statusText, response.data);
//             });
//     }

//     $scope.title = title;
//     $scope.escolaridadeList = [];
//     $scope.tiposCursoList = [];
//     $scope.cursosGraduacaoList = [];
//     $scope.cursosEspecializacaoList = [];
//     $scope.situacoesList = [];

//     function escolaridadeResourceFN() {
//         escolaridadeResource.porContratanteUsuarioLogado()
//             .$promise.then(function (data) {
//                 $scope.escolaridadeList = data;
//             }).catch(function (response) {
//                 notification.criticalError(response.status, response.statusText, response.data);
//             });
//     }

//     escolaridadeResourceFN();

//     function tipoCursoResourceFN() {
//         tipoCursoResource.porContratanteUsuarioLogado()
//             .$promise.then(function (data) {
//                 $scope.tiposCursoList = data;
//             }).catch(function (response) {
//                 notification.criticalError(response.status, response.statusText, response.data);
//             });
//     }

//     tipoCursoResourceFN();

//     function cursoGraduacaoResourceFN() {
//         cursoGraduacaoResource.porContratanteUsuarioLogado()
//             .$promise.then(function (data) {
//                 $scope.cursosGraduacaoList = data;
//             }).catch(function (response) {
//                 notification.criticalError(response.status, response.statusText, response.data);
//             });
//     }

//     cursoGraduacaoResourceFN();

//     function cursoEspecializacaoResourceFN() {
//         cursoEspecializacaoResource.porContratanteUsuarioLogado()
//             .$promise.then(function (data) {
//                 $scope.cursosEspecializacaoList = data;
//             }).catch(function (response) {
//                 notification.criticalError(response.status, response.statusText, response.data);
//             });
//     }

//     cursoEspecializacaoResourceFN();

//     function situacaoResourceFN() {
//         situacaoResource.conclusoes()
//             .$promise.then(function (data) {
//                 $scope.situacoesList = data;
//             }).catch(function (response) {
//                 notification.criticalError(response.status, response.statusText, response.data);
//             });
//     }

//     situacaoResourceFN();

//     //Apagando os valores da combo de formação
//     $scope.changeEscolaridade = function () {
//         //FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
//         angular.forEach($scope.escolaridadeList, function (value) {
//             if (value.id == $scope.object.escolaridade.id) {
//                 $scope.object.escolaridade = angular.copy(value);
//             }
//         });
//         delete $scope.object.tipoCurso;
//         delete $scope.object.cursoGraduacao;
//         delete $scope.object.cursoEspecializacao;
//         delete $scope.object.outroCurso;
//     };

//     $scope.changeTipoCurso = function () {
//         //FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
//         angular.forEach($scope.tiposCursoList, function (value) {
//             if (value.id == $scope.object.tipoCurso.id) {
//                 $scope.object.tipoCurso = angular.copy(value);
//             }
//         });
//         delete $scope.object.cursoGraduacao;
//         delete $scope.object.cursoEspecializacao;
//         delete $scope.object.outroCurso;
//     };

//     $scope.changeGraduacao = function () {
//         //FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
//         angular.forEach($scope.cursosGraduacaoList, function (value) {
//             if (value.id == $scope.object.cursoGraduacao.id) {
//                 $scope.object.cursoGraduacao = angular.copy(value);
//             }
//         });
//         delete $scope.object.cursoEspecializacao;
//         delete $scope.object.outroCurso;
//     };

//     $scope.changeEspecializacao = function () {
//         //FIXME: Após atualização do angular-material 1.1.0, as combos pararam de aceitar object. O código abaixo é provisório até correção da versão.
//         angular.forEach($scope.cursosEspecializacaoList, function (value) {
//             if (value.id == $scope.object.cursoEspecializacao.id) {
//                 $scope.object.cursoEspecializacao = angular.copy(value);
//             }
//         });
//         delete $scope.object.outroCurso;
//     };

//     $scope.hide = function () {
//         $mdDialog.hide();
//     };

//     $scope.cancel = function () {
//         fn_Cancel($scope.hide);
//     };

//     $scope.save = function () {
//         fn_Save($scope.object, $scope.hide);
//     };
// }

// export { dialogFuncionarioFormacao, dialogMensagem, dialogMensagemExibicao };
