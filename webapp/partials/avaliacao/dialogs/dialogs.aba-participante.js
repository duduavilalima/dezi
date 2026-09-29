/* @ngInject */
const ValidarParticipantesDialog = (
	$scope,
	$mdDialog,
	title,
	toolBarClassDialog,
	msg,
	mdActionClassDialog,
	titleButtonCancel,
	titleButtonConfirm,
	fn_salvarParticipantes,
) => {
	$scope.title = title;
	$scope.toolBarClassDialog = toolBarClassDialog;
	$scope.msg = msg;
	$scope.mdActionClassDialog = mdActionClassDialog;
	$scope.titleButtonCancel = titleButtonCancel;
	$scope.titleButtonConfirm = titleButtonConfirm;

	$scope.hide = function() {
		$mdDialog.hide();
	};

	$scope.cancel = function() {
		$mdDialog.cancel();
	};

	$scope.salvar = function() {
		fn_salvarParticipantes('validar');
		$mdDialog.cancel();
	};
};

/* @ngInject */
const ImportarParesDialog = (
	$scope,
	$mdDialog,
	title,
	toolBarClassDialog,
	msg,
	mdActionClassDialog,
	fn_upload,
) => {
	$scope.title = title;
	$scope.toolBarClassDialog = toolBarClassDialog;
	$scope.msg = msg;
	$scope.mdActionClassDialog = mdActionClassDialog;

	$scope.hide = function() {
		$mdDialog.hide();
	};

	$scope.cancel = function() {
		$mdDialog.cancel();
	};

	$scope.importar = function() {
		fn_upload(document.getElementById('file').files[0], $scope.hide);
	};
};

/* @ngInject */
const ValidarPendenciasDialog = (
	$scope,
	$mdDialog,
	title,
	toolBarClassDialog,
	msg,
	mdActionClassDialog,
	fn_goPendencias,
) => {
	$scope.title = title;
	$scope.toolBarClassDialog = toolBarClassDialog;
	$scope.msg = msg;
	$scope.mdActionClassDialog = mdActionClassDialog;

	$scope.hide = function() {
		$mdDialog.hide();
	};

	$scope.cancel = function() {
		$mdDialog.cancel();
	};

	$scope.salvar = function() {
		fn_goPendencias();
		$mdDialog.cancel();
	};
};

export { ValidarParticipantesDialog, ImportarParesDialog, ValidarPendenciasDialog };
