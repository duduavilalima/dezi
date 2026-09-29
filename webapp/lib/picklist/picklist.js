angular.module('fxpicklist', [])
.directive('picklist', ['$compile', '$templateCache', function($compile, $templateCache){

    /*
     * what do we need
     * 1. source options: the ngoptiond
     * 2. dest inst to put the select values: ng-model
     */
    return {
        replace:true,
        priority:99,
        templateUrl:"picklist.html",
        restrict: 'E',
        terminal:false,
        scope:true, //do not pollute parent scope
        controller: function($scope, $element, $attrs, $parse){

            //size
            $scope.size=$attrs.size;
            if (!angular.isDefined($scope.size)){
                $scope.size=10;
            }

            //name
            $scope.formname=$attrs.name;
            if (!angular.isDefined($scope.formname)){
                $scope.formname="pick_list_test";
            }

            //id
            $scope.formid=$attrs.id;
            if (!angular.isDefined($scope.formid)){
                $scope.formid="pick_list_test";
            }

            //flex da div do form
            $scope.formflex=$attrs.pickFlex;
            if (!angular.isDefined($scope.formflex)){
                $scope.formflex=100;
            }

            //label do src
            $scope.labelSrc=$attrs.pickLabelSrc;
            if (!angular.isDefined($scope.labelSrc)){
                $scope.labelSrc="Origem";
            }

            //label do dest
            $scope.labelDest=$attrs.pickLabelDest;
            if (!angular.isDefined($scope.labelDest)){
                $scope.myMarginTop = '15px;';
            }
            else{
                $scope.myMarginTop = '0px;';
            }

            //label do button all add
            $scope.buttonAllAdd=$attrs.pickButtonAllAdd;
            if (!angular.isDefined($scope.buttonAllAdd)){
                $scope.buttonAllAdd="Selecionar todas";
            }

            //label do button add
            $scope.buttonAdd=$attrs.pickButtonAdd;
            if (!angular.isDefined($scope.buttonAdd)){
                $scope.buttonAdd="Selecionar";
            }

            //label do button all rev
            $scope.buttonAllRev=$attrs.pickButtonAllRev;
            if (!angular.isDefined($scope.buttonAllRev)){
                $scope.buttonAllRev="Remover todas";
            }

            //label do button rev
            $scope.buttonRev=$attrs.pickButtonRev;
            if (!angular.isDefined($scope.buttonRev)){
                $scope.buttonRev="Remover";
            }

            //screenModel
            $scope.screenModel=$attrs.pickScreenModel;
            if (!angular.isDefined($scope.screenModel)){
                $scope.screenModel="EDIT";
            }

            if($scope.screenModel === "EDIT"){
                $scope.display=true;
            }
            else{
                $scope.display=false;
            }

//            console.log('$scope.display');
//            console.log($scope.display);
//
//            console.log('$scope.myMarginTop');
//            console.log($scope.myMarginTop);

            $scope.ngOptions=$attrs.pickOptions;

            $scope.ngModel=$attrs.ngModel;
            if (!angular.isDefined($attrs.ngModel)){
                $scope.ngModel=$attrs.pickModel;
            }
            /*
             * model directive is never processed.
             * The new ctrl is the subform.
             */
             delete($attrs['ngModel']);
             $element.removeAttr('ng-model');
             $element.removeAttr('ng-model');


            //hold the selection.
            $scope.picklist_src=[];
            $scope.picklist_dest=[];

            // match parsing is from angular js
            var NG_OPTIONS_REGEXP = /^\s*([\s\S]+?)(?:\s+as\s+([\s\S]+?))?(?:\s+group\s+by\s+([\s\S]+?))?\s+for\s+(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+([\s\S]+?)(?:\s+track\s+by\s+([\s\S]+?))?$/;
            var match= $scope.ngOptions.match(NG_OPTIONS_REGEXP);
            $scope.srcoptionsExp = match[7];
            var displayFn = $parse(match[2] || match[1]),
            valueName = match[4] || match[6],
            keyName = match[5],
            groupByFn = $parse(match[3] || ''),
            valueFn = $parse(match[2] ? match[1] : valueName),
            valuesFn = $parse(match[7]),
            track = match[8],
            trackFn = track ? $parse(match[8]) : null;

            var hasValueFn=match[2]?true:false;

            var modelFn=$parse($scope.ngModel);
            var models=modelFn($scope);
            //give models an empty array if needed.
            if (!angular.isDefined(models) || models==null){
                modelFn.assign($scope, []);
            }

              $scope.srcoptions=[];
              $scope.destoptions=[];


             $scope.valueToItem=function(value){
                if (!hasValueFn){
                    return value;
                }
                return $scope.valueMap[value];
             };

            $scope.itemToValue=function(item){
                 if (!hasValueFn){
                        return item;
                   }
                 var local={};
                 local[valueName]=item;
                return valueFn($scope, local);
            };
            //set it to $scope.srcoptions
            $scope.init=function(){
                $scope.srcoptions.length = 0;
                $scope.destoptions.length = 0;
                $scope.valueMap={};


                var getter = $parse($scope.srcoptionsExp);
                var options=getter($scope);
                if (!options){
                    return;
                }
                for(var i = 0; i<options.length; i++){
                    $scope.srcoptions.push(options[i]);
                    //map option value to object
                    if (hasValueFn){
                        var value=$scope.itemToValue(options[i]);
                        $scope.valueMap[value]=options[i];
                    }
                }
                var models=$parse($scope.ngModel)($scope);
                angular.forEach(models, function(v){
                    $scope.destoptions.push($scope.valueToItem(v));
                });
                $scope.removeLeftFromRight($scope.destoptions, $scope.srcoptions);
            };


            $scope.$watchCollection($scope.srcoptionsExp, $scope.init);
            $scope.$watchCollection($scope.ngModel, $scope.init);

            //always assign to model and expect destoptions is updated from model.
            //do not update desttoptions to model.
            $scope.rightShift=function(){
                var models=modelFn($scope);
                for (var i = 0; i<$scope.picklist_src.length; i++){
                    models.push($scope.itemToValue($scope.picklist_src[i]));
                }
                $scope.removeLeftFromRight($scope.picklist_src, $scope.srcoptions);
                $scope.picklist_src.length = 0;
            };
            $scope.rightShiftAll=function(){
                var models=modelFn($scope);
                for (var i = 0; i<$scope.srcoptions.length; i++){
                    models.push($scope.itemToValue($scope.srcoptions[i]));
                }
                $scope.removeLeftFromRight($scope.picklist_src, $scope.srcoptions);
                $scope.picklist_src.length = 0;
                $scope.srcoptions.length = 0;
            };
            $scope.leftShift=function(){
                var models=modelFn($scope);
                for (var i = 0; i<$scope.picklist_dest.length; i++){
                    $scope.srcoptions.push($scope.picklist_dest[i]);
                }
                models.length = 0;
//                while(models.length>0){
//                    models.pop();
//                }
                $scope.removeLeftFromRight($scope.picklist_dest, $scope.destoptions);
                $scope.picklist_dest.length = 0;
                angular.forEach($scope.destoptions, function(item){
                    models.push($scope.itemToValue(item));
                });
            };
            $scope.leftShiftAll=function(){
                var models=modelFn($scope);
                for (var i = 0; i<$scope.destoptions.length; i++){
                    $scope.srcoptions.push($scope.destoptions[i]);
                }
                $scope.picklist_dest.length = 0;
                $scope.destoptions.length = 0;
                models.length = 0
//                while(models.length>0){
//                    models.pop();
//                }
            };
            $scope.arrowUp=function(){
                var idxs=new Array();
                for (var i = 0; i<$scope.picklist_dest.length; i++){
                    for (var j = 0; j<$scope.destoptions.length; j++){
                        if($scope.picklist_dest[i]==$scope.destoptions[j]){
                            idxs.push(j);
                        }
                    }
                }
                idxs.sort();
                for (var i = 0; i<idxs.length; i++){
                    var idx=idxs[i];
                    if (idx>0){
                        var temp=$scope.destoptions[idx-1];
                        $scope.destoptions[idx-1]=$scope.destoptions[idx];
                        $scope.destoptions[idx]=temp;
                    }
                }
                while(models.length>0){
                    models.pop();
                }
                angular.forEach($scope.destoptions, function(item){
                    models.push($scope.itemToValue(item));
                });
            };
            $scope.arrowDown=function(){
                var idxs=new Array();
                for (var i = 0; i<$scope.picklist_dest.length; i++){
                    for (var j = 0; j<$scope.destoptions.length; j++){
                        if($scope.picklist_dest[i]==$scope.destoptions[j]){
                            idxs.push(j);
                        }
                    }
                }
                idxs.sort();
                for (var i=idxs.length-1; i >= 0; i--){
                    var idx=idxs[i];
                    if (idx<$scope.destoptions.length-1){
                        var temp=$scope.destoptions[idx+1];
                        $scope.destoptions[idx+1]=$scope.destoptions[idx];
                        $scope.destoptions[idx]=temp;
                    }
                }
                while(models.length>0){
                    models.pop();
                }
                angular.forEach($scope.destoptions, function(item){
                    models.push($scope.itemToValue(item));
                });
            };


            /**
             * Remove from right all elements in left;
             */
            $scope.removeLeftFromRight=function(left, right){
                for(var i = 0; i<left.length; i++){
                    for (var j = 0; j<right.length; j++){
                        if (left[i]===right[j]){
                            right.splice(j, 1);
                            break;
                        }
                    }
                }
            };


        },
    };
}])

.directive('picklistForm', ['$compile', '$templateCache', function($compile, $templateCache){

	/*
	 * what do we need
	 * 1. source options: the ngoptiond
	 * 2. dest inst to put the select values: ng-model
	 */
	return {
		require: "^picklist",
		priority:99,
		restrict: 'A',
		controller: function($scope, $element, $attrs){
			if (angular.isDefined($attrs.ngForm) && angular.isDefined($scope.formname)){
				$attrs.ngForm=$scope.formname;
				$element.attr("ng-form", $scope.formname+"_form");
			}

			$element.attr("flex", $scope.formflex);
		}
	};
}])

.directive('picklistSrc', ['$compile', '$templateCache', function($compile, $templateCache){

	/*
	 * what do we need
	 * 1. source options: the ngoptiond
	 * 2. dest inst to put the select values: ng-model
	 */
	return {
		require: "^picklist",
		priority:99,
		restrict: 'A',
		controller: function($scope, $element, $attrs){
			$attrs.ngOptions=$scope.ngOptions;
			$attrs.size=$scope.size;
			$element.attr("size", $scope.size);
			$attrs.id=$scope.formid+"_src";
			$element.attr("id", $scope.formid+"_src");
			$attrs.name=$scope.formname+"_src";
			$element.attr("name", $scope.formname+"_src");
			//always use item as value.
			$attrs.ngOptions=$scope.ngOptions.replace($scope.srcoptionsExp, "srcoptions").replace(/^.+\s+as\s+/, "");
			$element.attr("ng-options", $scope.ngOptions);
		}
	};
}])

.directive('picklistDest', ['$compile', '$templateCache', function($compile, $templateCache){

	/*
	 * what do we need
	 * 1. source options: the ngoptiond
	 * 2. dest inst to put the select values: ng-model
	 */
	return {
		require: "^picklist",
		priority:99,
		restrict: 'A',
		controller: function($scope, $element, $attrs){
			$attrs.size=$scope.size;
			$element.attr("size", $scope.size);
			$attrs.id=$scope.formid+"_dest";
			$element.attr("id", $scope.formid+"_dest");
			$attrs.name=$scope.formname+"_dest";
			$element.attr("name", $scope.formname+"_dest");
			//always use item as value. real valye is synchoinized to model by watcher function
			$attrs.ngOptions=$scope.ngOptions.replace($scope.srcoptionsExp, "destoptions").replace(/^.+\s+as\s+/, "");
			$element.attr("ng-options", $scope.ngOptions);
		}
	};
}])
.run(["$templateCache", function($templateCache) {
	 $templateCache.put("picklist.html", fxPickListTpl);
}]);

var fxPickListTpl=
"	<div picklist-form ng-form=\"pick_list_form\" class=\"picklist-main\">"+
"		<div layout=\"row\" layout-align=\"end center\" ng-show=\"display\">"+
"			<div flex=\"40\">"+
"      			<div class=\"div-label-picklist\">"+
"                       <label id=\"lbl_{{formid}}_src\" name=\"lbl_{{formid}}_src\" class=\"label-picklist md-input-container md-sgi-theme:not(md-input-invalid) md-input-has-value label\">{{labelSrc}}</label>"+
"	        	</div>"+
"   			<select multiple picklist-src size=\"2\" class=\"source-picklist\" ng-options=\"fake\" ng-model=\"picklist_src\" >"+
"	    		</select>"+
"			</div>"+
"			<div layout=\"column\" flex=\"20\" layout-align=\"end center\" hide-gt-sm hide-sm hide-gt-xs hide-xs show-md show-gt-md show-lg show-gt-lg show-xl class=\"div-buttons-picklist\">"+
"					<md-button id=\"button_all_add_{{formid}}\" name=\"button_all_add_{{formid}}\" aria-label=\"{{buttonAllAdd}}\" type=\"button\" class=\"md-raised md-accent\" ng-click=\"rightShiftAll();\" style=\"width: 90%;\">"+
"                       <md-icon md-font-icon=\"icon-chevron-double-right\" class=\"white-500-fg\" ></md-icon>"+
"                       {{buttonAllAdd}}"+
"					</md-button>"+
"					<md-button id=\"button_add_{{formid}}\" name=\"button_add_{{formid}}\" aria-label=\"{{buttonAdd}}\" type=\"button\" class=\"md-raised md-accent\" ng-click=\"rightShift();\" style=\"width: 90%;\">"+
"                       <md-icon md-font-icon=\"icon-chevron-right\" class=\"white-500-fg\" ></md-icon>"+
"                       {{buttonAdd}}"+
"					</md-button>"+
"					<md-button id=\"button_rev_{{formid}}\" name=\"button_rev_{{formid}}\" aria-label=\"{{buttonRev}}\" type=\"button\" class=\"md-raised md-accent\" ng-click=\"leftShift();\" style=\"width: 90%;\">"+
"                       <md-icon md-font-icon=\"icon-chevron-left\" class=\"white-500-fg\" ></md-icon>"+
"                       {{buttonRev}}"+
"					</md-button>"+
"                   <md-button id=\"button_all_rev_{{formid}}\" name=\"button_all_rev_{{formid}}\" aria-label=\"{{buttonAllRev}}\" type=\"button\" class=\"md-raised md-accent\" ng-click=\"leftShiftAll();\" style=\"width: 90%;\">"+
"                       <md-icon md-font-icon=\"icon-chevron-double-left\" class=\"white-500-fg\" ></md-icon>"+
"                       {{buttonAllRev}}"+
"					</md-button>"+
"			</div>"+
"			<div layout=\"column\" flex=\"20\" layout-align=\"end center\" hide-md hide-gt-md hide-lg hide-gt-lg hide-xl class=\"div-buttons-picklist\">"+
"					<md-button id=\"button_all_add_{{formid}}\" name=\"button_all_add_{{formid}}\" aria-label=\"{{buttonAllAdd}}\" type=\"button\" class=\"md-raised md-accent\" ng-click=\"rightShiftAll();\" style=\"min-width: 44px;\">"+
"                       <md-icon md-font-icon=\"icon-chevron-double-right\" class=\"white-500-fg\" ></md-icon>"+
"					</md-button>"+
"					<md-button id=\"button_add_{{formid}}\" name=\"button_add_{{formid}}\" aria-label=\"{{buttonAdd}}\" type=\"button\" class=\"md-raised md-accent\" ng-click=\"rightShift();\" style=\"min-width: 44px;\">"+
"                       <md-icon md-font-icon=\"icon-chevron-right\" class=\"white-500-fg\" ></md-icon>"+
"					</md-button>"+
"					<md-button id=\"button_rev_{{formid}}\" name=\"button_rev_{{formid}}\" aria-label=\"{{buttonRev}}\" type=\"button\" class=\"md-raised md-accent\" ng-click=\"leftShift();\" style=\"min-width: 44px;\">"+
"                       <md-icon md-font-icon=\"icon-chevron-left\" class=\"white-500-fg\" ></md-icon>"+
"					</md-button>"+
"					<md-button id=\"button_all_rev_{{formid}}\" name=\"button_all_rev_{{formid}}\" aria-label=\"{{buttonAllRev}}\" type=\"button\" class=\"md-raised md-accent\" ng-click=\"leftShiftAll();\" style=\"min-width: 44px;\">"+
"                       <md-icon md-font-icon=\"icon-chevron-double-left\" class=\"white-500-fg\" ></md-icon>"+
"					</md-button>"+
"			</div>"+
"			<div flex=\"40\">"+
"   			<div class=\"div-label-picklist\" ng-if=\"labelDest!= null\">"+
"                  <label id=\"lbl_pick_list_dest\" name=\"lbl_pick_list_dest\" class=\"label-picklist\">{{labelDest}}</label>"+
"   			</div>"+
"				<select multiple picklist-dest size=\"2\" class=\"dest-picklist\" ng-options=\"fake\" ng-model=\"picklist_dest\" style=\"margin-top:{{myMarginTop}}\">"+
"				</select>"+
"			</div>"+
"		</div>"+
"		<div ng-show=\"display===false\">"+
"			<div flex=\"100\">"+
"   			<div class=\"div-label-picklist\">"+
"                  <label id=\"lbl_pick_list_display\" name=\"lbl_pick_list_display\" class=\"label-picklist\">{{labelSrc}}</label>"+
"   			</div>"+
"				<select multiple disabled=\"true\" picklist-dest size=\"2\" class=\"dest-picklist\" ng-options=\"fake\" ng-model=\"picklist_dest\" >"+
"				</select>"+
"			</div>"+
"		</div>"+
"	</div>	  "

;








