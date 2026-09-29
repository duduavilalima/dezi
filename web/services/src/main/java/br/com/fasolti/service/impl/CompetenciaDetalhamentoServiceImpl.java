package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.CompDetalhamentoHintRepository;
import br.com.fasolti.avd.repository.CompetenciaDetalhamentoRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CompetenciaDetalhamentoServiceImpl
	extends BaseAvdService<CompetenciaDetalhamento, CompetenciaDetalhamentoTO, CompetenciaDetalhamentoTO>
	implements CompetenciaDetalhamentoService {

	private final QCompetenciaDetalhamento qCompetenciaDetalhamento = QCompetenciaDetalhamento.competenciaDetalhamento;

	private final QModeloGraduacao qModeloGraduacao = QModeloGraduacao.modeloGraduacao;

	@Autowired
	private CompetenciaDetalhamentoRepository competenciaDetalhamentoRepository;

	@Autowired
	private CompDetalhamentoHintRepository detalhamentoCalibrahintRepository;

	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private AvaliacaoCompetenciaService avalCompService;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private DetalhamentoCargoCompetenciaService detalhamentoCargoCompetenciaService;

	@Autowired
	private GraduacaoService graduacaoService;

	@Autowired
	private CompDetalhamentoHintService compDetalhamentoHintService;

	@Autowired
	public CompetenciaDetalhamentoServiceImpl(
		CompetenciaDetalhamentoRepository competenciaDetalhamentoRepository) {
		super(competenciaDetalhamentoRepository);
	}

	@Override
	protected void setDefaultValues(CompetenciaDetalhamentoTO object) throws ServiceException {
		object.setSituacao(Situacao.ATIVA);
	}

	@Override
	protected void doBeforeSave(CompetenciaDetalhamento entity, CompetenciaDetalhamentoTO object) throws ServiceException {
		Iterable<CompetenciaDetalhamento> competenciaDetalhamentos = competenciaDetalhamentoRepository.findByCompetenciaId(entity.getCompetencia().getId());
		if (competenciaDetalhamentos != null) {
			for (CompetenciaDetalhamento competencia : competenciaDetalhamentos) {
				if (competencia != null && competencia.getDescricao() != null) {
					if (object.getDescricao().equalsIgnoreCase(competencia.getDescricao()))
						//FIXME: Passar a entidade para internacionalização
					    throw new ValidationException("validation.duplicate", "detalhamento", "field.detalhamento");
				}
			}
		}
	}

	@Override
	protected void doBeforeUpdate(CompetenciaDetalhamento entity, CompetenciaDetalhamentoTO object) throws ServiceException {
		if(object.getSituacao().equals(Situacao.INATIVA)) {
			detalhamentoCargoCompetenciaService.removerDetalhamentosCCListByCompetenciaDetalhamento(object);
		}
		else {
			List<GraduacaoTO> graduacoes = graduacaoService.findAllGraduacoesByModeloAndSituacao(object.getModeloGraduacao(), Situacao.ATIVA);
			object.getCompetenciaDetalhamentosCalibrahint().forEach(compDetalhamentoHintTO -> {
				boolean match = graduacoes.stream().anyMatch(graduacaoTO -> graduacaoTO.getId().equals(compDetalhamentoHintTO.getGraduacao().getId()));
				if (match)
					detalhamentoCalibrahintRepository.save(mapper.map(compDetalhamentoHintTO, CompDetalhamentoHint.class));
//					compDetalhamentoHintService.saveOrUpdate(compDetalhamentoHintTO);
			});
		}
	}

	@Override
	protected void doAfterSave(CompetenciaDetalhamento entity, CompetenciaDetalhamentoTO object) throws ServiceException {
		CompetenciaTO competencia = competenciaService.load(object.getCompetencia().getId());
		competencia.setSituacao(Situacao.ATIVA);
		competenciaService.update(competencia, competencia.getId());

		List<GraduacaoTO> graduacoes = graduacaoService.findAllGraduacoesByModeloAndSituacao(object.getModeloGraduacao(), Situacao.ATIVA);
		object.getCompetenciaDetalhamentosCalibrahint().forEach(compDetalhamentoHintTO -> {
			compDetalhamentoHintTO.setCompetenciaDetalhamento(object);
			if (graduacoes.contains(compDetalhamentoHintTO.getGraduacao()))
				compDetalhamentoHintService.saveOrUpdate(compDetalhamentoHintTO);
		});
	}

	@Override
	public List<CompetenciaDetalhamentoTO> listByCompetencia(Long competenciaId) {
		return map(
			select(
				Projections.fields(
					CompetenciaDetalhamento.class,
					qCompetenciaDetalhamento.id,
					qCompetenciaDetalhamento.descricao,
					qCompetenciaDetalhamento.situacao,
					qCompetenciaDetalhamento.ordem,
					Projections.fields(
						Competencia.class,
						qCompetenciaDetalhamento.competencia.id,
						qCompetenciaDetalhamento.competencia.descricaoAbreviada,
						qCompetenciaDetalhamento.competencia.situacao,
						Projections.fields(
							TipoCompetencia.class,
							qCompetenciaDetalhamento.competencia.tipo.id,
							qCompetenciaDetalhamento.competencia.tipo.nome,
							qCompetenciaDetalhamento.competencia.tipo.descricao
						).as("tipo")
					).as("competencia"),
					Projections.fields(
						ModeloGraduacao.class,
						qCompetenciaDetalhamento.modeloGraduacao.id,
						qCompetenciaDetalhamento.modeloGraduacao.descricaoModelo,
						qCompetenciaDetalhamento.modeloGraduacao.situacao
					).as("modeloGraduacao")
				)
			)
				.join(qCompetenciaDetalhamento.competencia, QCompetencia.competencia)
				.join(qCompetenciaDetalhamento.modeloGraduacao, QModeloGraduacao.modeloGraduacao)
				.join(qCompetenciaDetalhamento.competencia.tipo, QTipoCompetencia.tipoCompetencia)
				.where(qCompetenciaDetalhamento.competencia.id.eq(competenciaId))
				.fetch()
		);
	}

	@Override
	public List<CompetenciaDetalhamentoTO> listByParticipante(ParticipanteTO participante) {
		List<AvaliacaoCompetenciaTO> avaliacaoCompetencias = avalCompService.listByParticipante(participante);

		return new ArrayList<>(avaliacaoCompetencias.size());
	}

	@Override
	public List<CompetenciaDetalhamentoTO> listActiveByCompetencia(Long competenciaId) {
		return map(competenciaDetalhamentoRepository.findByCompetenciaIdAndSituacao(competenciaId, Situacao.ATIVA));
	}

	@Override
	public Set<CompetenciaDetalhamentoTO> listByCompetenciaAndCargoAndSituacao(CompetenciaTO competencia, CargoTO cargo, Situacao situacao) {
		Set<CompetenciaDetalhamentoTO> detalhamentos = new HashSet<CompetenciaDetalhamentoTO>();

		// Obter o cargo competência para posteriormente obter o gabarito.
		CargoCompetenciaTO cargoCompetenciaTO = cargoCompetenciaService.findCargoCompetenciaByCargoAndCompetencia(cargo.getId(), competencia.getId());

		List<CompetenciaDetalhamentoTO> listaDetalhamentos = listActiveByCompetencia(competencia.getId());

		listaDetalhamentos.forEach(competenciaDetalhamento -> {
			DetalhamentoCargoCompetenciaTO gabarito = detalhamentoCargoCompetenciaService.findDetalhamentoCCByCargoCompetenciaAndCompetenciaDetalhamento(cargoCompetenciaTO.getId(), competenciaDetalhamento.getId());

			//No gabarito do cargo associado a uma competência, é definido se o detalhamento é aplicável ao cargo e se o mesmo será adicionado a avaliação.
			if (gabarito != null && gabarito.getAplicavelAoCargo()) {
				// Apenas ativos.
				if (situacao.equals(competenciaDetalhamento.getSituacao())) {
					detalhamentos.add(competenciaDetalhamento);
				}
			}
		});

		return detalhamentos;
	}

	@Override
	public CompetenciaDetalhamentoTO obterCompetenciaDetalhamentoProjections(Long idCompDet) {
		return map(
			select(
				Projections.fields(
					CompetenciaDetalhamento.class,
					qCompetenciaDetalhamento.id,
					qCompetenciaDetalhamento.ordem,
					qCompetenciaDetalhamento.descricao,
					qCompetenciaDetalhamento.atualizador,
					qCompetenciaDetalhamento.dataAtualizacao,
					qCompetenciaDetalhamento.criador,
					qCompetenciaDetalhamento.dataCriacao,
					Projections.fields(ModeloGraduacao.class,
						qModeloGraduacao.id,
						qModeloGraduacao.descricaoModelo,
						qModeloGraduacao.situacao,
						qModeloGraduacao.naoGraduar,
						qModeloGraduacao.qtdGraduacoes
					).as("modeloGraduacao"),
					Projections.fields(Competencia.class,
						QCompetencia.competencia.id,
						QCompetencia.competencia.situacao,
						QCompetencia.competencia.descricaoAbreviada,
						Projections.fields(TipoCompetencia.class,
							QCompetencia.competencia.tipo.id,
							QCompetencia.competencia.tipo.nome,
							QCompetencia.competencia.tipo.descricao,
							QCompetencia.competencia.tipo.nomeCurto
						).as("tipo")
					).as("competencia")
				)
			)
			.leftJoin(qCompetenciaDetalhamento.modeloGraduacao, qModeloGraduacao)
			.leftJoin(qCompetenciaDetalhamento.competencia, QCompetencia.competencia)
			.where(qCompetenciaDetalhamento.id.eq(idCompDet))
			.fetchOne()
		);
	}
}
