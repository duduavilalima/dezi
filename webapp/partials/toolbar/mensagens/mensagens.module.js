import angular from 'angular';
import filters from 'lib/filter';
import dialogs from 'utils/dialog.utils';

import Participante from 'services/participante';
import UserService from 'services/user.service';
import CompetenciaAvaliador from 'services/preenchimento';
import CompetenciaPreenchimento from 'services/preenchimento';
import ApontamentoFuncionalResource from 'services/apontamento';

import mensagensController from './mensagens';
import './mensagens.html';
import './tabs/mensagens.todas.html';
import './tabs/mensagens.recebidas.html';
import './tabs/mensagens.enviadas.html';

import {dialogMensagemResponder, dialogMensagem, dialogMensagemExibicao} from './dialogs/dialogs.controller';
import './dialogs/dialog.adicionar-mensagem.html';
import './dialogs/dialog.encaminhar-mensagem.html';
import './dialogs/dialog.exibir-mensagem.html';
import './dialogs/dialog.responder-mensagem.html';

export default angular
	.module('app.mensagens', [	
		filters,
		dialogs,
		Participante,
		UserService,
		CompetenciaAvaliador,
		ApontamentoFuncionalResource,
		CompetenciaPreenchimento
	])
	.controller('MensagensCtrl', mensagensController)
	.controller('dialogMensagemResponder', dialogMensagemResponder)
	.controller('dialogMensagemExibicao', dialogMensagemExibicao)
	.controller('dialogMensagem', dialogMensagem)
	.name;
