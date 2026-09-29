import { enumInfo } from 'utils/enum.utils';

const SituacaoFuncionario = {
	EM_ATIVIDADE: 'EM_ATIVIDADE',
	FERIAS: 'FERIAS',
	LICENCA_MATERNIDADE: 'LICENCA_MATERNIDADE',
	LICENCA_SEM_REMUNERACAO: 'LICENCA_SEM_REMUNERACAO',
	DISPENSA_MEDICA: 'DISPENSA_MEDICA',
	SERVICO_MILITAR_OBRIGATORIO: 'SERVICO_MILITAR_OBRIGATORIO',
	LICENCA_PATERINDADE: 'LICENCA_PATERINDADE',
	LUTO: 'LUTO',
	NUPCIAS: 'NUPCIAS',
	LICENCA_ADOCAO: 'LICENCA_ADOCAO',
	DESLIGADO: 'DESLIGADO',
	RECESSO: 'RECESSO',
};

const SituacaoFuncionarioInfo = enumInfo(SituacaoFuncionario, [
	// Ordenação natural
	SituacaoFuncionario.EM_ATIVIDADE,
	SituacaoFuncionario.FERIAS,
	SituacaoFuncionario.LICENCA_MATERNIDADE,
	SituacaoFuncionario.LICENCA_SEM_REMUNERACAO,
	SituacaoFuncionario.DISPENSA_MEDICA,
	SituacaoFuncionario.SERVICO_MILITAR_OBRIGATORIO,
	SituacaoFuncionario.LICENCA_PATERINDADE,
	SituacaoFuncionario.LUTO,
	SituacaoFuncionario.NUPCIAS,
	SituacaoFuncionario.LICENCA_ADOCAO,
	SituacaoFuncionario.DESLIGADO,
	SituacaoFuncionario.RECESSO,
])
	.className('SituacaoFuncionario')
	.propertiesPerValue({
		[SituacaoFuncionario.EM_ATIVIDADE]: { code: 1 },
		[SituacaoFuncionario.FERIAS]: { code: 2 },
		[SituacaoFuncionario.LICENCA_MATERNIDADE]: { code: 3 },
		[SituacaoFuncionario.LICENCA_SEM_REMUNERACAO]: { code: 4 },
		[SituacaoFuncionario.DISPENSA_MEDICA]: { code: 5 },
		[SituacaoFuncionario.SERVICO_MILITAR_OBRIGATORIO]: { code: 6 },
		[SituacaoFuncionario.LICENCA_PATERINDADE]: { code: 7 },
		[SituacaoFuncionario.LUTO]: { code: 8 },
		[SituacaoFuncionario.NUPCIAS]: { code: 9 },
		[SituacaoFuncionario.LICENCA_ADOCAO]: { code: 10 },
		[SituacaoFuncionario.DESLIGADO]: { code: 11 },
		[SituacaoFuncionario.RECESSO]: { code: 12 },
	})
	.property('trLabel', ({value}) => `SITUACAO.${value}`)
	.build();

export default SituacaoFuncionario;
export {
	SituacaoFuncionarioInfo,
};
