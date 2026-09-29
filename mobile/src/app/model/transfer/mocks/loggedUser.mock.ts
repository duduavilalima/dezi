import { LoggedUserTO } from "../loggedUser.transfer";

export const LOGGED_USERS: LoggedUserTO[] = [
	{
		id: 1,
		matricula: "carolina.campos",
		nome: "ANA CAROLINA CAMPOS PEREIRA",
		emailFuncional: "denis.oliveira@fasolti.com.br",
		tipoPerfil: "FUNCIONARIO",
		primeiroAcesso: false,
		cargo: {
			id: 613,
			nome: "ANALISTA DE MARKETING - PLANEJAMENTO PL",
			situacao: "ATIVA",
		},
		empresa: {
			id: 100,
			nome: "RTM",
			identificador: "rtm",
			host: "rtm.dezi.com.br",
			contratante: {
				id: 1,
				nome: "FASOLTI",
				situacao: "ATIVA",
			},
		},
		situacao: "ATIVA",
		configuracao: {
			id: 102,
			linguagem: "pt_BR",
		},
		username: "100%carolina.campos",
	},
];
