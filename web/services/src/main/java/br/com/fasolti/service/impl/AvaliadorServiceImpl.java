package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.domain.QFuncionario;
import br.com.fasolti.avd.repository.FotoRepository;
import br.com.fasolti.avd.repository.FuncionarioRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.DepartamentoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.UnidadeDepartamentoTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioFotoTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class AvaliadorServiceImpl extends
	BaseAvdService<Funcionario, FuncionarioTO, FuncionarioTO>
	implements AvaliadorService{

	@Autowired
	private FotoRepository fotoRepository;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private UnidadeDepartamentoService unidadeDepartamentoService;

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	public AvaliadorServiceImpl(FuncionarioRepository funcionarioRepository) {
		super(funcionarioRepository);
	}

	public List<FuncionarioFotoTO> fotosAvaliacoesAtivas(Long avaliadorId) {
		List<FuncionarioFotoTO> listaFoto = new ArrayList<>();

		if (getLoggedUser().getId().equals(avaliadorId)) {
			List<Object[]> registros = fotoRepository.obterFotoSubordinadoAvaliacaoAtiva(avaliadorId, getLoggedUser().getEmpresa().getId());

			for (Object[] colunas : registros) {
				FuncionarioFotoTO foto = new FuncionarioFotoTO();
				foto.setFuncionarioId(Long.parseLong(colunas[0].toString()));
				foto.setFoto(colunas[1].toString());
				listaFoto.add(foto);
			}
		}

		return listaFoto;
	}

	@Override
	public FuncionarioSimpleTO findAvaliador(FuncionarioSimpleTO avaliado) {
		FuncionarioSimpleTO avaliador = null;

		FuncionarioTO funcTO = funcionarioService.findById(avaliado.getId());

		//para descobrir o avaliador é verificado se o departamento/unidade do avaliado possui um gestor
		UnidadeDepartamentoTO unidadeDepartamento = null;
		if (funcTO.getUnidade() != null) {
			unidadeDepartamento = unidadeDepartamentoService.obterPorUnidadeEDepartamento(funcTO.getUnidade().getId(), funcTO.getDepartamento().getId());
		}

		TipoAvaliacaoPerfil perfilGestor = TipoAvaliacaoPerfil.GESTOR;

		//Se não for encontrado um gestor para o departamento/unidade do avaliado ou o gestor desse departamento/unidade
		//é o próprio avaliado, tem que ser encontrado o gestor do departamento/unidade acima dele.
		if (unidadeDepartamento == null || unidadeDepartamento.getGestor().getId().equals(funcTO.getId())) {

			Long idDeptoPai = null;

			/*Condição do if:
			* 		Obtem o id do departamento acima (depto pai) do departamento do gestor utilizando o objeto unidadeDepartamento.
			* Condição do else-if:
			* 		Obtem o id do departamento acima (depto pai) do departamento do gestor utilizando o objeto funcTO.
			* */
			if (unidadeDepartamento != null && unidadeDepartamento.getDepartamento() != null && unidadeDepartamento.getDepartamento().getDepartamentoPai() != null) {
				idDeptoPai = unidadeDepartamento.getDepartamento().getDepartamentoPai().getId();
			} else if (funcTO != null && funcTO.getDepartamento() != null && funcTO.getDepartamento().getDepartamentoPai() != null) {
				idDeptoPai = funcTO.getDepartamento().getDepartamentoPai().getId();
			}

			if (idDeptoPai != null) {
				//verifica qual é o primeiro departamento pai que possui um avaliador, de preferência na mesma unidade.
				List<UnidadeDepartamentoTO> unidadeDepartamentoLista = null;
				unidadeDepartamentoLista = unidadeDepartamentoService.obterDepartamentoUnidadePorDepartamento(idDeptoPai);
				boolean encontrouAvaliador = false;

				if (unidadeDepartamentoLista == null || unidadeDepartamentoLista.isEmpty()) {
					DepartamentoTO depto = new DepartamentoTO();
					depto.setId(idDeptoPai);
					List<DepartamentoTO> departamentosAscendentes = departamentoService.obterDepartamentosAscendentes(depto);

					for (DepartamentoTO dp : departamentosAscendentes) {
						unidadeDepartamentoLista = unidadeDepartamentoService.obterDepartamentoUnidadePorDepartamento(dp.getId());
						if (unidadeDepartamentoLista != null && !unidadeDepartamentoLista.isEmpty()) {

							avaliador = getAvaliador(funcTO, unidadeDepartamento, unidadeDepartamentoLista);

							if (avaliador != null) {
								break;
							}

						}
					}

				}

				if (avaliador == null) {
					avaliador = getAvaliador(funcTO, unidadeDepartamento, unidadeDepartamentoLista);
				}

			}

		} else if (unidadeDepartamento != null && !unidadeDepartamento.getGestor().getId().equals(funcTO.getId())) {
			avaliador = mapper.map(unidadeDepartamento.getGestor(), FuncionarioSimpleTO.class);
		}

		return avaliador;
	}

	private FuncionarioSimpleTO getAvaliador(FuncionarioTO funcTO, UnidadeDepartamentoTO unidadeDepartamento, List<UnidadeDepartamentoTO> unidadeDepartamentoLista) {
		FuncionarioSimpleTO avaliador = null;

		for (UnidadeDepartamentoTO unidDep : unidadeDepartamentoLista) {
			if (unidadeDepartamento == null) {
				if (unidDep.getGestor().getSituacaoFuncional() != SituacaoFuncionario.DESLIGADO) {
					avaliador = mapper.map(unidDep.getGestor(), FuncionarioSimpleTO.class);
					break;
				}
			} else if (unidadeDepartamento != null && !unidDep.getGestor().getId().equals(funcTO.getId())) {
				if (unidDep.getGestor().getSituacaoFuncional() != SituacaoFuncionario.DESLIGADO) {
					avaliador = mapper.map(unidDep.getGestor(), FuncionarioSimpleTO.class);
					break;
				}
			}
		}

		return avaliador;
	}

}
