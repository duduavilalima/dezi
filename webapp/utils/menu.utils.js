import angular from 'angular';
import { TipoPerfilInfo } from 'model/TipoPerfil';

export default angular.module('utils.menu', [])
	.factory('MenuUtils', menuUtilsProvider)
	.name;

/* @ngInject */
function menuUtilsProvider(msNavigationService) {
	const service = this;

	const menus = [];

	service.addMenu = addMenu;
	service.configureMenuVisibilities = configureMenuVisibilities;
	service.refresh = refresh;

	function addMenu(menu) {
		menus.push(menu);
	}

	function configureMenuVisibilities(
		configurations, {
			resolve = defaultResolveConfiguration,
			force = false,
		} = {},
	) {
		validateConfigurationsObject(configurations);

		Object.keys(configurations)
			.map(stateName => ({ stateName, configuration: configurations[stateName] }))
			.forEach(({ stateName, configuration }) => {
				menus
					.filter(menu => {
						const contains = _stateNameContains(stateName, menu.stateName);
						return contains;
					})
					.forEach(menu => {
						const newVisibilityConfig = resolve(configuration, { stateName, force, from: menu.visibilityConfig });
						menu.visibilityConfig = newVisibilityConfig;
					});
			});
	}

	function refresh(userRoles) {
		menus
			.forEach(menu => {
				const visibilityConfig = menu.visibilityConfig || {};
				const { onlyRoles, exceptRoles } = visibilityConfig;
				const visibility = visibilityConfig.visibility || 'restricted';
				const defaultOnlyRoles = menu.roles;

				const isHiddenByConfigVisibility = visibility === 'hidden';
				const canBeHiddenByConfig = !isHiddenByConfigVisibility && visibility === 'restricted';

				menu.hidden = () => Boolean(
					isHiddenByConfigVisibility
					|| ( canBeHiddenByConfig && isNonEmptyArray(onlyRoles)   && !isUserRolePermitted(userRoles, onlyRoles) )
					|| ( canBeHiddenByConfig && isNonEmptyArray(exceptRoles) &&  isUserRolePermitted(userRoles, exceptRoles) )
					|| ( isNonEmptyArray(defaultOnlyRoles) && !isUserRolePermitted(userRoles, defaultOnlyRoles) )
				);

				msNavigationService.saveItem(menu.stateName, menu);
			});

		msNavigationService.sort();
	}

	return service;
}

const statePartSep = '.';

/**
 * Verifica se o nome de estado `a` contém o nome de estado `b`, hierarquicamente.
 *
 * A hierarquia é determinada através de componentes de nome separados por um caractere de ponto (`.`).
 *
 * @param {string} a
 * @param {string} b
 */
function _stateNameContains(a, b) {
	const aParts = a.split(statePartSep);
	const bParts = b.split(statePartSep);

	if (aParts.length > bParts.length) return false;
	return aParts.every((aPart, aIdx) => bParts[aIdx] === aPart);
}

function defaultResolveConfiguration(to, { from, stateName, force }) {
	if (from && !force) {
		stateName;
		// console.warn(`Menu "${stateName} já foi configurado anteriormente.`);
	}

	return to;
}

function isNonEmptyArray(array) {
	return Array.isArray(array) && array.length > 0;
}

function isUserRolePermitted(userRoles, permittedRoleNames) {
	return userRoles.some(userRole => permittedRoleNames.indexOf(userRole.authority) >= 0);
}

const validVisibilities = [ 'restricted', 'hidden', 'defaults' ];
const validRoleNames = TipoPerfilInfo.elements.map(el => el.roleName);

function validateConfigurationsObject(configurations) {
	const errorMsgPrefix = 'Configuração de visibilidade de menu inválida';
	const errorMessages = [];

	Object.keys(configurations)
		.forEach(stateName => {
			const configuration = configurations[stateName];
			const { visibility, onlyRoles, exceptRoles } = configuration;

			if (visibility !== undefined && !validVisibilities.some(validVisibility => visibility === validVisibility)) {
				errorMessages.push(`${errorMsgPrefix}: valor "${visibility}" inválido para a propriedade "visibility"`);
			}

			[{ name: 'onlyRoles', roles: onlyRoles }, { name: 'exceptRoles', roles: exceptRoles }]
				.forEach(rolesConfig => {
					const { name, roles } = rolesConfig;

					if (roles !== undefined && Array.isArray(roles)) {
						const invalidRoleNames = roles.filter(roleName => {
							return !validRoleNames.some(validRoleName => roleName === validRoleName);
						});
						if (invalidRoleNames.length > 0) {
							errorMessages.push(`${errorMsgPrefix}: nomes de perfil (role) inválido na propriedade "${name}": ${invalidRoleNames.join(',')}`);
						}
					}
				});
		});

	if (errorMessages.length > 0) {
		throw new Error(errorMessages.join('; '));
	}
}
