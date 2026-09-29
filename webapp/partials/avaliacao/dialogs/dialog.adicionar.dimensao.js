/* @ngInject */
export default function dialogDimensaoCtrl($scope,$mdDialog, Dimensao, listDimensoes) {
	const $dialog = this;
	$scope.$dialog = $dialog;
	$dialog.dimensaoSelected;
	Dimensao.obterdimensaoativa().$promise.then(data => {
		$dialog.list = [];
		$dialog.list = data;
		listDimensoes.forEach(dimensao => {
			$dialog.list = $dialog.list.filter(value => {
				return dimensao.dimensao.id != value.id;
			});
		});
	});
	
	$dialog.saveDimensao = () => {
		listDimensoes.push($dialog.dimensaoSelected);
		$mdDialog.hide();
	};

	$dialog.cancelDimensao = $mdDialog.cancel;
}