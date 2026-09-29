import angular from 'angular';
import textAngular from 'textangular';

export default angular.module('utils.textangular', [textAngular])
	.config(textangularConfig)
	.name;

/* @ngInject */
function textangularConfig($provide) {
	$provide.decorator('taOptions', ['taRegisterTool', '$delegate', function (taRegisterTool, taOptions) {
		taOptions.forceTextAngularSanitize = false;
		taOptions.keyMappings = [];
		taOptions.toolbar = [
			['h1', 'h2', 'h3', 'h4', 'h5', 'p', 'pre', 'quote'],
			['bold', 'italics', 'underline', 'ul', 'ol', 'clear'],
			['justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull', 'indent', 'outdent'],
			['html', 'insertImage', 'insertLink', 'insertVideo']
		];
		taOptions.classes = {
			focussed: '',
			toolbar: 'ta-toolbar',
			toolbarGroup: 'ta-button-group',
			toolbarButton: '',
			toolbarButtonActive: 'active',
			disabled: 'disabled',
			textEditor: 'ta-text-editor',
			htmlEditor: 'md-input-container'
		};

		taRegisterTool('fontColor', {
			display:
				'<spectrum-colorpicker trigger-id="{{trigger}}" style="margin-top: 5px;" ' +
				'ng-model="color" on-change="!!color && action(color)" format=""hex"" ' +
				'options="options"></spectrum-colorpicker>',
			action: function (color) {
				var me = this;
				if (!this.$editor().wrapSelection) {
					setTimeout(function () {
						me.action(color);
					}, 100);
				} else {
					return this.$editor().wrapSelection('foreColor', color);
				}
			},
			options: {
				replacerClassName: 'icon-pencil', showButtons: false
			},
			color: 'black'
		});

		taRegisterTool('fontName', {
			display:
				'<md-menu aria-label="Alterar fonte">' +
				'<md-button class="md-icon-button" ng-click="$mdMenu.open()">' +
				'<md-icon md-menu-origin md-font-icon="icon-format-color"></md-icon>' +
				'<md-tooltip class="h5">Alterar fonte</md-tooltip>' +
				'</md-button>' +
				'<md-menu-content width="4">' +
				'<md-menu-item ng-repeat="o in options">' +
				'<md-button style="font-family: {{o.css}}; width: 100%" ng-click="action($event, o.css)">' +
				'{{o.name}}' +
				'</md-button>' +
				'</md-menu-item>' +
				'</md-menu-content>' +
				'</md-menu>',
			action: function (event, font) {
				//Ask if event is really an event.
				if (event.stopPropagation) {
					//With this, you stop the event of textAngular.
					event.stopPropagation();
					//Then click in the body to close the dropdown.
					//FIXME $("body").trigger("click");
				}

				return this.$editor().wrapSelection('fontName', font);
			},
			options: [
				{ name: 'Sans-Serif', css: 'Arial, Helvetica, sans-serif' },
				{ name: 'Times new roman', css: '"times new roman", serif' },
				{ name: 'Wide', css: '"arial black", sans-serif' },
				{ name: 'Narrow', css: '"arial narrow", sans-serif' },
				{ name: 'Comic Sans MS', css: '"comic sans ms", sans-serif' },
				{ name: 'Courier New', css: '"courier new", monospace' },
				{ name: 'Garamond', css: 'garamond, serif' },
				{ name: 'Georgia', css: 'georgia, serif' },
				{ name: 'Tahoma', css: 'tahoma, sans-serif' },
				{ name: 'Trebuchet MS', css: '"trebuchet ms", sans-serif' },
				{ name: 'Helvetica', css: '"Helvetica Neue", Helvetica, Arial, sans-serif' },
				{ name: 'Verdana', css: 'verdana, sans-serif' }
			]
		});

		taRegisterTool('fontSize', {
			display: '<md-menu aria-label="Alterar tamanho da fonte">' +
				'<md-button class="md-icon-button" ng-click="$mdMenu.open()">' +
				'<md-icon md-menu-origin md-font-icon="icon-format-size"></md-icon>' +
				'<md-tooltip class="h5">Alterar tamanho da fonte</md-tooltip>' +
				'</md-button>' +
				'<md-menu-content width="4">' +
				'<md-menu-item ng-repeat="o in options">' +
				'<md-button style="font-size: {{o.css}}; width: 100%" ng-click="action($event, o.value)">' +
				'{{o.name}}' +
				'</md-button>' +
				'</md-menu-item>' +
				'</md-menu-content>' +
				'</md-menu>',
			action: function (event, size) {
				//Ask if event is really an event.
				if (event.stopPropagation) {
					//With this, you stop the event of textAngular.
					event.stopPropagation();
					//Then click in the body to close the dropdown.
					//FIXME $("body").trigger("click");
				}

				return this.$editor().wrapSelection('fontSize', parseInt(size));
			},
			options: [
				{ name: 'xx-small', css: 'xx-small', value: 1 },
				{ name: 'x-small', css: 'x-small', value: 2 },
				{ name: 'small', css: 'small', value: 3 },
				{ name: 'medium', css: 'medium', value: 4 },
				{ name: 'large', css: 'large', value: 5 },
				{ name: 'x-large', css: 'x-large', value: 6 },
				{ name: 'xx-large', css: 'xx-large', value: 7 }

			]
		});

		taOptions.toolbar[1].push('fontColor', 'fontName', 'fontSize');
		return taOptions;
	}]);

	/* eslint-disable */
	$provide.decorator('taTools', ['$delegate', function (taTools) {
		taTools.h1.display = '<md-button><md-icon md-font-icon="icon-format-header-1"></md-icon><md-tooltip class="h5">H1</md-tooltip></md-button>';
		taTools.h2.display = '<md-button><md-icon md-font-icon="icon-format-header-2"></md-icon><md-tooltip class="h5">H2</md-tooltip></md-button>';
		taTools.h3.display = '<md-button><md-icon md-font-icon="icon-format-header-3"></md-icon><md-tooltip class="h5">H3</md-tooltip></md-button>';
		taTools.h4.display = '<md-button><md-icon md-font-icon="icon-format-header-4"></md-icon><md-tooltip class="h5">H4</md-tooltip></md-button>';
		taTools.h5.display = '<md-button><md-icon md-font-icon="icon-format-header-5"></md-icon><md-tooltip class="h5">H5</md-tooltip></md-button>';
		taTools.p.display = '<md-button><md-icon md-font-icon="icon-format-paragraph"></md-icon><md-tooltip class="h5">Parágrafo</md-tooltip></md-button>';
		taTools.pre.display = '<md-button>PRE<md-tooltip class="h5">PRE</md-tooltip></md-button>';
		taTools.quote.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-quote"></md-icon><md-tooltip class="h5">Citação</md-tooltip></md-button>';
		taTools.bold.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-bold"></md-icon><md-tooltip class="h5">Negrito</md-tooltip></md-button>';
		taTools.italics.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-italic"></md-icon><md-tooltip class="h5">Itálico</md-tooltip></md-button>';
		taTools.underline.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-underline"></md-icon><md-tooltip class="h5">Sublinhado</md-tooltip></md-button>';
		taTools.ul.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-list-bulleted"></md-icon><md-tooltip class="h5">Lista com marcadores</md-tooltip></md-button>';
		taTools.ol.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-list-numbers"></md-icon><md-tooltip class="h5">Lista numerada</md-tooltip></md-button>';
		taTools.justifyLeft.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-align-left"></md-icon><md-tooltip class="h5">Alinhar a esquerda</md-tooltip></md-button>';
		taTools.justifyRight.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-align-right"></md-icon><md-tooltip class="h5">Alinhar a direita</md-tooltip></md-button>';
		taTools.justifyCenter.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-align-center"></md-icon><md-tooltip class="h5">Centralizar</md-tooltip></md-button>';
		taTools.justifyFull.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-align-justify"></md-icon><md-tooltip class="h5">Justificado</md-tooltip></md-button>';
		taTools.clear.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-clear"></md-icon><md-tooltip class="h5">Remover formatação</md-tooltip></md-button>';
		taTools.html.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-code-tags"></md-icon><md-tooltip class="h5">Mostrar HTML</md-tooltip></md-button>';
		taTools.insertLink.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-link"></md-icon><md-tooltip class="h5">Inserir link</md-tooltip></md-button>';
		taTools.insertImage.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-file-image-box"></md-icon><md-tooltip class="h5">Inserir foto</md-tooltip></md-button>';
		taTools.insertVideo.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-youtube-play"></md-icon><md-tooltip class="h5">Inserir video</md-tooltip></md-button>';
		taTools.indent.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-indent-increase"></md-icon><md-tooltip class="h5">Indentar</md-tooltip></md-button>';
		taTools.outdent.display = '<md-button class="md-icon-button"><md-icon md-font-icon="icon-format-indent-decrease"></md-icon><md-tooltip class="h5">Remover indentação</md-tooltip></md-button>';
		return taTools;
	}]);
}
