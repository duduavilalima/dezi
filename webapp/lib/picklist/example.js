angular.module("picklisttest", ['picklist'])
.controller('testCtrl', ['$scope', function testCtrl($scope){
  $scope.toptions=new Array();

  for(var i=0; i<30; i++){
    $scope.toptions.push({
    name: " display name"+i,
    value: "value"+i,
    index: i
    });
  }

  $scope.tselected=[$scope.toptions[4], $scope.toptions[5]];

}])

.controller('testCtrl1', ['$scope', function testCtrl1($scope){
  $scope.toptions=new Array();

  for(var i=0; i<10; i++){
    $scope.toptions.push({
    name: " display name"+i,
    value: "value"+i,
    index: i
    });
  }

  $scope.tselected=[$scope.toptions[4].value, $scope.toptions[5].value];
}]);

