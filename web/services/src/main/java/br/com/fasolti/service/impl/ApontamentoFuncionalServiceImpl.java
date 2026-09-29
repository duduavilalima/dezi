package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.ApontamentoFuncional;
import br.com.fasolti.avd.domain.QApontamentoFuncional;
import br.com.fasolti.avd.repository.ApontamentoFuncionalRepository;
import br.com.fasolti.avd.service.ApontamentoFuncionalService;
import br.com.fasolti.avd.service.FotoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.ApontamentoFuncionalTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.mobile.ApontamentoFuncionalMobileTO;
import br.com.fasolti.avd.transfer.mobile.CompetenciaMobileTO;
import br.com.fasolti.avd.transfer.simple.CompetenciaSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.fasolti.commons.services.ServiceFilter;
import com.querydsl.core.BooleanBuilder;

import java.util.*;

@Service
public class ApontamentoFuncionalServiceImpl
	extends BaseAvdService<ApontamentoFuncional, ApontamentoFuncionalTO, ApontamentoFuncionalTO>
	implements ApontamentoFuncionalService {

	@Autowired
	private FotoService fotoService;

	@Autowired
	private ApontamentoFuncionalRepository apontamentoFuncionalRepository;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	public ApontamentoFuncionalServiceImpl(ApontamentoFuncionalRepository repository) {
		super(repository);
	}

	private static final QApontamentoFuncional qApontamentoFuncional = QApontamentoFuncional.apontamentoFuncional;


	/*O usuário logado É O AVALIADOR  */
	@Override
	protected ServiceFilter<ApontamentoFuncional> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		UserTO funcionario = getLoggedUser();
		switch (funcionario.getTipoPerfil()) {
			case ADMINISTRADOR:
			case GESTOR_RH:
				mandatoryFilter.and(qApontamentoFuncional.avaliador.empresa.id.eq(funcionario.getEmpresa().getId()));
				break;
//			case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
			case GESTOR_AREA:
			default:
				mandatoryFilter.and(qApontamentoFuncional.avaliador.id.eq(funcionario.getId()));
				break;
		}

		return ServiceFilter.create(qApontamentoFuncional, mandatoryFilter);
	}


	@Override
	public List<ApontamentoFuncionalTO> apontamentosPorFuncionario(FuncionarioTO funcionario) {
		List<ApontamentoFuncional> list = apontamentoFuncionalRepository.findByAvaliadoId(funcionario.getId());
		Collections.sort(list, new Comparator<ApontamentoFuncional>() {
			@Override
			public int compare(ApontamentoFuncional o1, ApontamentoFuncional o2) {
				return o2.getDataApontamento().compareTo(o1.getDataApontamento());
			}
		});
		return map(list);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void salvarApontamentoFuncionalMobile(ApontamentoFuncionalMobileTO apontamentoMobileTO) {

		ApontamentoFuncionalTO apontamentoFuncionalTO = new ApontamentoFuncionalTO();

		FuncionarioSimpleTO avaliado = new FuncionarioSimpleTO();
		avaliado.setId(apontamentoMobileTO.getAvaliadoId());
		apontamentoFuncionalTO.setAvaliado(avaliado);

		FuncionarioSimpleTO avaliador = new FuncionarioSimpleTO();
		avaliador.setId(apontamentoMobileTO.getAvaliadorId());
		apontamentoFuncionalTO.setAvaliador(avaliador);

		apontamentoFuncionalTO.setGraduacaoVisual(apontamentoMobileTO.getGraduacaoVisual());
		apontamentoFuncionalTO.setRegistroCritico(apontamentoMobileTO.getRegistroCritico());
		apontamentoFuncionalTO.setFeedforward(apontamentoMobileTO.getFeedforward());

		CompetenciaSimpleTO competenciaSimpleTO = new CompetenciaSimpleTO();
		competenciaSimpleTO.setId(apontamentoMobileTO.getCompetencia().getId());

		apontamentoFuncionalTO.setCompetencia(competenciaSimpleTO);

		apontamentoFuncionalTO.setDataApontamento(new Date());

		save(apontamentoFuncionalTO);
	}

	public List<ApontamentoFuncionalMobileTO> apontamentoPorFuncionarioMobile(Long idAvaliado, Long idAvaliador) {

		List<ApontamentoFuncional> apontamentoFuncionalList = new ArrayList<>();

		List<ApontamentoFuncionalMobileTO> apontamentoMobileList = new ArrayList<>();

		apontamentoFuncionalList = apontamentoFuncionalRepository.findByAvaliadoIdAndAvaliadorId(idAvaliado, idAvaliador);

		for(ApontamentoFuncional apontamentoFuncional : apontamentoFuncionalList) {
			//			apontamentoMobileList.add(mapper.map(apontamentoFuncional, ApontamentoMobileTO.class));

			ApontamentoFuncionalMobileTO apontamentoMobileTO = new ApontamentoFuncionalMobileTO();

			apontamentoMobileTO.setDataApontamento(apontamentoFuncional.getDataApontamento());
			apontamentoMobileTO.setFeedforward(apontamentoFuncional.getFeedforward());
			apontamentoMobileTO.setGraduacaoVisual(apontamentoFuncional.getGraduacaoVisual());
			apontamentoMobileTO.setAvaliadoId(apontamentoFuncional.getAvaliado().getId());
			apontamentoMobileTO.setAvaliadorId(apontamentoFuncional.getAvaliador().getId());
			apontamentoMobileTO.setRegistroCritico(apontamentoFuncional.getFeedforward());

			CompetenciaMobileTO competenciaTO = new CompetenciaMobileTO();
			competenciaTO.setId(apontamentoFuncional.getCompetencia().getId());
			competenciaTO.setDescricaoAbreviada(apontamentoFuncional.getCompetencia().getDescricaoAbreviada());

	/*		TipoCompetenciaMobileTO tipoCompetencia = new TipoCompetenciaMobileTO();
			tipoCompetencia.setId(apontamentoFuncional.getCompetencia().getTipo().getId());
			tipoCompetencia.setDescricao(apontamentoFuncional.getCompetencia().getTipo().getDescricao());
			tipoCompetencia.setPaiId(apontamentoFuncional.getCompetencia().getTipo().getIdPai());
			tipoCompetencia.setNome(apontamentoFuncional.getCompetencia().getTipo().getNome());
			tipoCompetencia.setNomeCurto(apontamentoFuncional.getCompetencia().getTipo().getNomeCurto());
			competenciaTO.setTipo(tipoCompetencia);*/

			apontamentoMobileTO.setCompetencia(competenciaTO);
			apontamentoMobileList.add(apontamentoMobileTO);
		}

		return apontamentoMobileList;
	}

	@Override
	public List<ApontamentoFuncionalMobileTO> apontamentoMobile(Long idUsuario) {
		List<ApontamentoFuncional> list = apontamentoFuncionalRepository.findByAvaliadoId(idUsuario);
		Collections.sort(list, new Comparator<ApontamentoFuncional>() {
			@Override
			public int compare(ApontamentoFuncional o1, ApontamentoFuncional o2) {
				return o2.getDataApontamento().compareTo(o1.getDataApontamento());
			}
		});
		List<ApontamentoFuncionalTO> apontamentoTOList = map(list);
		List<ApontamentoFuncionalMobileTO> apontamentoList = new ArrayList<>();
		apontamentoList = mapper.map(apontamentoTOList, ApontamentoFuncionalMobileTO.class);

		return apontamentoList;
	}

}
