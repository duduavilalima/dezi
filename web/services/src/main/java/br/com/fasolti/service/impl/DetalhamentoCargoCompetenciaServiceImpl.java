package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.*;
import br.com.fasolti.avd.service.CargoCompetenciaService;
import br.com.fasolti.avd.service.CompetenciaDetalhamentoService;
import br.com.fasolti.avd.service.DetalhamentoCargoCompetenciaService;
import br.com.fasolti.avd.service.PreenchimentoService;
import br.com.fasolti.avd.transfer.CargoCompetenciaTO;
import br.com.fasolti.avd.transfer.CompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.CompetenciaTO;
import br.com.fasolti.avd.transfer.DetalhamentoCargoCompetenciaTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetalhamentoCargoCompetenciaServiceImpl
	extends BaseAvdService<DetalhamentoCargoCompetencia, DetalhamentoCargoCompetenciaTO, DetalhamentoCargoCompetenciaTO>
	implements DetalhamentoCargoCompetenciaService {

	private final QDetalhamentoCargoCompetencia qDetCargoComp = QDetalhamentoCargoCompetencia.detalhamentoCargoCompetencia;
	private final QCargoCompetencia qCargoCompetencia = QCargoCompetencia.cargoCompetencia;
	private final QCompetenciaDetalhamento qCompetenciaDetalhamento = QCompetenciaDetalhamento.competenciaDetalhamento;
	private final QGraduacao qGraduacao = QGraduacao.graduacao;

	@Autowired
	private DetalhamentoCargoCompetenciaRepository detalhamentoCargoCompetenciaRepository;

	@Autowired
	private CargoCompetenciaRepository cargoCompetenciaRepository;

	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private CompetenciaDetalhamentoService competenciaDetalhamentoService;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private ParticipanteRepository participanteRepository;

	@Autowired
	private GraduacaoRepository graduacaoRepository;

	@Autowired
	private GraduacaoCompetenciaRepository graduacaoCompetenciaRepository;

	@Autowired
	public DetalhamentoCargoCompetenciaServiceImpl(DetalhamentoCargoCompetenciaRepository repository) {
		super(repository);
	}

	/*Ao reativar a competencia, chama este metodo*/
	@Override
	public void updateDetalhamentoCCList(CargoCompetenciaTO cargoCompetenciaTO) {
		/*O cargoCompetencia foi REATIVADO
		 * Para isso, precisamos obter a lista de detalhamentos diretamente dos cadastros e
		 * preenchê-la novamente. */
		List<CompetenciaDetalhamentoTO> competenciaDetalhamentoTOList = competenciaDetalhamentoService.listActiveByCompetencia(cargoCompetenciaTO.getCompetencia().getId());

		List<DetalhamentoCargoCompetenciaTO> detalhamentoCargoCompetenciaTOList = competenciaDetalhamentoTOList
			.stream()
			.map(competenciaDetalhamentoTO -> createDetalhamentoCC(cargoCompetenciaTO, competenciaDetalhamentoTO))
			.collect(Collectors.toList());

		save(detalhamentoCargoCompetenciaTOList);
	}

	private DetalhamentoCargoCompetenciaTO createDetalhamentoCC(CargoCompetenciaTO cargoCompetenciaTO, CompetenciaDetalhamentoTO competenciaDetalhamentoTO) {
		DetalhamentoCargoCompetenciaTO detalhamentoCargoCompetenciaTO = new DetalhamentoCargoCompetenciaTO();
		detalhamentoCargoCompetenciaTO.setCompetenciaDetalhamento(competenciaDetalhamentoTO);
		detalhamentoCargoCompetenciaTO.setCargoCompetencia(cargoCompetenciaTO);
		detalhamentoCargoCompetenciaTO.setAplicavelAoCargo(Boolean.FALSE);
		return detalhamentoCargoCompetenciaTO;
	}

	@Override
	public List<DetalhamentoCargoCompetenciaTO> findOrCreateDetalhamentosCCByCompetencia(Long competenciaId, Optional<Long> cargoCompetenciaId) {
		CargoCompetenciaTO cargoCompetenciaTO = new CargoCompetenciaTO();
		if(cargoCompetenciaId.isPresent()) {
			cargoCompetenciaTO = cargoCompetenciaService.load(cargoCompetenciaId.get());
		}

		List<CompetenciaDetalhamentoTO> competenciaDetalhamentoTOList = competenciaDetalhamentoService.listActiveByCompetencia(competenciaId);

		//Usado no mapper, precisa ser algo imutável
		CargoCompetenciaTO finalCargoCompetenciaTO = cargoCompetenciaTO;
		return competenciaDetalhamentoTOList.stream().map(compDet -> createDetalhamentoCC(finalCargoCompetenciaTO, compDet)).collect(Collectors.toList());
	}

	@Override
	public List<DetalhamentoCargoCompetenciaTO> findOrCreateDetalhamentosCC(Long competenciaId, Long cargoCompetenciaId) {

		CargoCompetenciaTO cargoCompetenciaTO = cargoCompetenciaService.load(cargoCompetenciaId);
		/*
		if(cargoCompetenciaTO.getSituacao().equals(Situacao.INATIVA)) {
			return new ArrayList<>();
		}*/

		List<DetalhamentoCargoCompetenciaTO> detalhamentoCargoCompetenciaTOList = findAllDetalhamentosCCByCargoCompetencia(cargoCompetenciaTO.getId());
		List<CompetenciaDetalhamentoTO> competenciaDetalhamentoTOList = competenciaDetalhamentoService.listActiveByCompetencia(competenciaId);

		competenciaDetalhamentoTOList.forEach(compDet -> {
			boolean match = detalhamentoCargoCompetenciaTOList
				.stream()
				.anyMatch(detCC -> detCC.getCompetenciaDetalhamento().getId().equals(compDet.getId()));
			if(!match && compDet.getSituacao().equals(Situacao.ATIVA)) {
				DetalhamentoCargoCompetenciaTO detalhamentoCargoCompetenciaTO = createDetalhamentoCC(cargoCompetenciaTO, compDet);
				detalhamentoCargoCompetenciaTOList.add(detalhamentoCargoCompetenciaTO);
			}

		});
		detalhamentoCargoCompetenciaTOList.sort(Comparator.comparingInt(detCC -> detCC.getCompetenciaDetalhamento().getOrdem()));
		return detalhamentoCargoCompetenciaTOList;
	}


	//TODO: Fazer uma exclusao logica adicionando a situacao ao detalhamentoCC
	@Override
	public List<DetalhamentoCargoCompetenciaTO> findAllDetalhamentosCCByCargoCompetencia(Long cargoCompetenciaId) {
		return map(
			select(
				Projections.fields(
					DetalhamentoCargoCompetencia.class,
					qDetCargoComp.id,
					qDetCargoComp.peso,
					qDetCargoComp.aplicavelAoCargo,
					qDetCargoComp.atualizador,
					qDetCargoComp.dataAtualizacao,
					qDetCargoComp.criador,
					qDetCargoComp.dataCriacao,
					Projections.fields(CargoCompetencia.class,
						qCargoCompetencia.id,
						qCargoCompetencia.peso
					).as("cargoCompetencia"),
					Projections.fields(CompetenciaDetalhamento.class,
						qCompetenciaDetalhamento.id,
						qCompetenciaDetalhamento.descricao,
						qCompetenciaDetalhamento.ordem
					).as("competenciaDetalhamento")
				)
			)
				.join(qDetCargoComp.cargoCompetencia, qCargoCompetencia)
				.join(qDetCargoComp.competenciaDetalhamento, qCompetenciaDetalhamento)
				.where(qDetCargoComp.cargoCompetencia.id.eq(cargoCompetenciaId))
				.fetch()
		);
	}

	@Override
	public List<DetalhamentoCargoCompetenciaTO> findAllDetalhamentosCCByCompetenciaDetalhamento(Long compDetalhamentoId) {
		return map(
			select(
				Projections.fields(
					DetalhamentoCargoCompetencia.class,
					qDetCargoComp.id,
					qDetCargoComp.peso,
					qDetCargoComp.aplicavelAoCargo,
					qDetCargoComp.atualizador,
					qDetCargoComp.dataAtualizacao,
					qDetCargoComp.criador,
					qDetCargoComp.dataCriacao,
					Projections.fields(CargoCompetencia.class,
						qCargoCompetencia.id,
						qCargoCompetencia.peso
					).as("cargoCompetencia"),
					Projections.fields(CompetenciaDetalhamento.class,
						qCompetenciaDetalhamento.id,
						qCompetenciaDetalhamento.descricao,
						qCompetenciaDetalhamento.ordem
					).as("competenciaDetalhamento")
				)
			)
				.join(qDetCargoComp.cargoCompetencia, qCargoCompetencia)
				.join(qDetCargoComp.competenciaDetalhamento, qCompetenciaDetalhamento)
				.where(qCompetenciaDetalhamento.id.eq(compDetalhamentoId))
				.fetch()
		);
	}


	@Override
	public DetalhamentoCargoCompetenciaTO findDetalhamentoCCByCargoCompetenciaAndCompetenciaDetalhamento(Long idCargoCompetencia, Long competenciaDetalhamentoId) {
		return map(detalhamentoCargoCompetenciaRepository.findByCargoCompetenciaIdAndCompetenciaDetalhamentoId(idCargoCompetencia, competenciaDetalhamentoId));
	}

	//TODO: Arrumar este método de salvar
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
//	@Override
//	public List<DetalhamentoCargoCompetenciaTO> salvarListaDetalhamentosCC(List<DetalhamentoCargoCompetenciaTO> listTO) {
//
//		CargoCompetencia CargoCompetenciaOriginal = cargoCompetenciaRepository.findOne(listTO.get(0).getCargoCompetencia().getId());
//		// Só deletar se houver mudança de competencia no relacionamento.
////		if (!CargoCompetenciaOriginal.getCompetencia().getId().equals(listTO.get(0).getCargoCompetencia().getCompetencia().getId())) {
//		// Deletar detalhamentos
////			deletarGabaritosDoCargoCompetencia(listTO.get(0).getCargoCompetencia());
////		}
//
//		for (DetalhamentoCargoCompetenciaTO detalhamentoCargoCompetenciaTO : listTO) {
////			if (detalhamentoCargoCompetenciaTO.getId() != null) {
////				detalhamentoCargoCompetenciaTO.setDataAtualizacao(new Date());
////				detalhamentoCargoCompetenciaTO.setAtualizador(getLoggedUser().getMatricula());
////				if (detalhamentoCargoCompetenciaTO.getAplicavelAoCargo() == Boolean.FALSE) {
////					detalhamentoCargoCompetenciaTO.setGraduacao(null);
////				}
////				detalhamentoCargoCompetenciaRepository.save(map(detalhamentoCargoCompetenciaTO));
////			} else {
////				if (detalhamentoCargoCompetenciaTO.getPeso() != null && detalhamentoCargoCompetenciaTO.getAplicavelAoCargo() != null) {
//			detalhamentoCargoCompetenciaTO.setDataCriacao(new Date());
//			detalhamentoCargoCompetenciaTO.setCriador(getLoggedUser().getUsername()); //FIXME Isso deveria ser feito pelo AuditedBean
//			if (detalhamentoCargoCompetenciaTO.getAplicavelAoCargo() == null) {
//				detalhamentoCargoCompetenciaTO.setAplicavelAoCargo(Boolean.FALSE);
//			}
//			DetalhamentoCargoCompetencia detalhamentoCargoCompetencia = detalhamentoCargoCompetenciaRepository.save(map(detalhamentoCargoCompetenciaTO));
//			detalhamentoCargoCompetenciaTO.setId(detalhamentoCargoCompetencia.getId());
////				}
////			}
//		}
//		return listTO;
//	}

	//TODO: FAZER EXCLUSAO LOGICA COM SITUACAO
	@Override
	public void removerDetalhamentosCCListByCargoCompetencia(CargoCompetenciaTO to) {
		List<DetalhamentoCargoCompetencia> listaDetalhamentoCargoCompetencias = detalhamentoCargoCompetenciaRepository.findByCargoCompetenciaId(to.getId());

		for (DetalhamentoCargoCompetencia detalhamentoCargoCompetencia : listaDetalhamentoCargoCompetencias) {
			detalhamentoCargoCompetenciaRepository.delete(detalhamentoCargoCompetencia);
		}
	}

	public void removerDetalhamentosCCListByCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamentoTO) {
		List<DetalhamentoCargoCompetenciaTO> detalhamentoCargoCompetenciaTOList = findAllDetalhamentosCCByCompetenciaDetalhamento(competenciaDetalhamentoTO.getId());

		for (DetalhamentoCargoCompetenciaTO detalhamentoCargoCompetencia : detalhamentoCargoCompetenciaTOList) {
			detalhamentoCargoCompetenciaRepository.delete(detalhamentoCargoCompetencia.getId());
		}
	}



	@Override
	public DetalhamentoCargoCompetenciaTO obterDetalhamentoCCProjections(Long idDetCC) {
		JPAQuery<DetalhamentoCargoCompetencia> detCargoCompQuery = select();
		detCargoCompQuery.where(qDetCargoComp.id.eq(idDetCC));
		detCargoCompQuery.join(qDetCargoComp.cargoCompetencia, qCargoCompetencia);
		detCargoCompQuery.join(qDetCargoComp.competenciaDetalhamento, qCompetenciaDetalhamento);
		detCargoCompQuery.select(Projections.fields(
			DetalhamentoCargoCompetencia.class,
			qDetCargoComp.id,
			qDetCargoComp.peso,
			qDetCargoComp.aplicavelAoCargo,
			qDetCargoComp.atualizador,
			qDetCargoComp.dataAtualizacao,
			qDetCargoComp.criador,
			qDetCargoComp.dataCriacao,
			Projections.fields(CargoCompetencia.class, qCargoCompetencia.id, qCargoCompetencia.peso).as("cargoCompetencia"),
			Projections.fields(CompetenciaDetalhamento.class, qCompetenciaDetalhamento.id).as("competenciaDetalhamento")
		));

		return map(detCargoCompQuery.fetchOne());
	}

	@Override
	public List<DetalhamentoCargoCompetenciaTO> findDetalhamentosAplicaveisAoCargo(Long idCargo, Situacao idSituacao) {
		return map(detalhamentoCargoCompetenciaRepository.findDetalhamentosAplicaveisAoCargo(idCargo, idSituacao));
	}
}

