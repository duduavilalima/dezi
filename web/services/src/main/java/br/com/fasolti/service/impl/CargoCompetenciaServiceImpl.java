package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.CargoCompetenciaRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoCompetenciaServiceImpl
	extends BaseAvdService<CargoCompetencia, CargoCompetenciaTO, CargoCompetenciaTO>
	implements CargoCompetenciaService {

	private final static QCargoCompetencia qCargoCompetencia = QCargoCompetencia.cargoCompetencia;

	@Autowired
	private CargoCompetenciaRepository cargoCompetenciaRepository;

	@Autowired
	private DetalhamentoCargoCompetenciaService detalhamentoCargoCompetenciaService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private CargoTipoCompetenciaService cargoTipoCompetenciaService;

	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private TipoCompetenciaService tipoCompetenciaService;

	@Autowired
	private CompetenciaDetalhamentoService competenciaDetalhamentoService;

	@Autowired
	public CargoCompetenciaServiceImpl(CargoCompetenciaRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<CargoCompetencia> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		UserTO user = getLoggedUser();
		switch (user.getTipoPerfil()) {
			case ADMINISTRADOR:
			case GESTOR_RH:
				mandatoryFilter.and(qCargoCompetencia.cargo.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
				mandatoryFilter.and(qCargoCompetencia.cargo.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case GESTOR_AREA:
				mandatoryFilter.and(qCargoCompetencia.cargo.departamento.id.eq(user.getDepartamento().getId()));
				break;
			default:
				mandatoryFilter.and(qCargoCompetencia.cargo.id.eq(user.getCargo().getId()));
				break;
		}

		return ServiceFilter.create(qCargoCompetencia, mandatoryFilter);
	}

	@Override
	protected CargoCompetenciaTO doAfterCreate(CargoCompetenciaTO object) throws ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	protected void doAfterSave(CargoCompetencia entity, CargoCompetenciaTO object) throws ServiceException {
		if (object.getSituacao().equals(Situacao.ATIVA)) {
			updateSituacaoCargo(object);
		}
	}

	@Override
	protected void doBeforeUpdate(CargoCompetencia entity, CargoCompetenciaTO object) throws ServiceException {
		validar(object);
		if (object.getSituacao().equals(Situacao.ATIVA)) {
			updateSituacaoCargo(object);
		}
		if (object.getSituacao().equals(Situacao.INATIVA)) {
			detalhamentoCargoCompetenciaService.removerDetalhamentosCCListByCargoCompetencia(object);
			//SE INATIVAR O CARGO COMPETENCIA. REMOVER AS COMPS DETALHAMENTOS!!
		}
	}


	private void validar(CargoCompetenciaTO cargoCompetenciaTO) {
		//Ao ativar um cargo competencia, com a competencia inativada, dar erro!
		CompetenciaTO competenciaTO = competenciaService.load(cargoCompetenciaTO.getCompetencia().getId());
		//Tem que dar load para obter a competencia
		if (cargoCompetenciaTO.getSituacao().equals(Situacao.ATIVA) && competenciaTO.getSituacao().equals(Situacao.INATIVA)) {
			throw new ValidationException("validation.status", "competência", cargoCompetenciaTO.getCompetencia().getDescricaoAbreviada());
		}
	}

	private void updateSituacaoCargo(CargoCompetenciaTO object) {
		CargoTO cargo = cargoService.load(object.getCargo().getId());
		//FIXME: Por algum motivo, o object não está trazendo o tipo de competencia
		CargoCompetenciaTO cargoCompetenciaTO = load(object.getId());
		CargoTipoCompetenciaTO cargoTipoCompetenciaTO = cargoTipoCompetenciaService.findCargoTipoCompetenciaByCargoAndTipoCompetencia(cargo.getId(), cargoCompetenciaTO.getCompetencia().getTipo().getId());
		cargoTipoCompetenciaTO.setSituacao(Situacao.ATIVA);
		cargo.setSituacao(Situacao.ATIVA);
		cargoService.update(cargo, cargo.getId());
		cargoTipoCompetenciaService.update(cargoTipoCompetenciaTO, cargoTipoCompetenciaTO.getId());
	}

	@Override
	public List<CargoCompetenciaTO> findAllCargoCompetenciaByCargo(Long cargoId) {
		return map(cargoCompetenciaRepository.findByCargoId(cargoId));
	}

	@Override
	public List<CargoCompetenciaTO> findAllCargoCompetenciaByCargoAndAtivos(Long cargoId) {
		return map(cargoCompetenciaRepository.findByCargoIdAndSituacao(cargoId, Situacao.ATIVA));
	}

	@Override
	public List<CargoCompetenciaTO> obterCargoCompetenciaPorCargoAndTipoCompetencia(Long idCargo, Long idTipoCompetencia) {
		QCargo qCargo = QCargo.cargo;
		QCompetencia qCompetencia = QCompetencia.competencia;

		JPAQuery<CargoCompetencia> cargoCompQuery = select();
		cargoCompQuery.join(qCargoCompetencia.cargo, qCargo);
		cargoCompQuery.join(qCargoCompetencia.competencia, qCompetencia);
		cargoCompQuery.where(qCargo.id.eq(idCargo).and(qCompetencia.tipo.id.eq(idTipoCompetencia)));
		cargoCompQuery.select(Projections.fields(
			CargoCompetencia.class,
			qCargoCompetencia.id,
			qCargoCompetencia.peso,
			qCargoCompetencia.situacao,
			qCargoCompetencia.ordem,
			Projections.fields(Competencia.class,
				qCompetencia.id,
				qCompetencia.descricaoAbreviada,
				qCompetencia.direcionamentoGestor,
				qCompetencia.direcionamentoNaoGestor)
				.as("competencia"),
			Projections.fields(Cargo.class, qCargo.id, qCargo.nome).as("cargo")
		));
		return map(cargoCompQuery.fetch());
	}

	/*//TODO: Melhorar o projections
	@Override
	public List<CargoCompetenciaTO> obterCargoCompetenciaPorTipoCompetencia(Long idTipoCompetencia) {
		QCargo qCargo = QCargo.cargo;
		QCompetencia qCompetencia = QCompetencia.competencia;

		JPAQuery<CargoCompetencia> cargoCompQuery = select();
		cargoCompQuery.join(qCargoCompetencia.cargo, qCargo);
		cargoCompQuery.join(qCargoCompetencia.competencia, qCompetencia);
		cargoCompQuery.where(qCompetencia.tipo.id.eq(idTipoCompetencia));
		cargoCompQuery.select(Projections.fields(
			CargoCompetencia.class,
			qCargoCompetencia.id,
			qCargoCompetencia.peso,
			qCargoCompetencia.situacao,
			qCargoCompetencia.ordem,
			Projections.fields(Competencia.class,
				qCompetencia.id,
				qCompetencia.descricaoAbreviada,
				qCompetencia.direcionamentoGestor,
				qCompetencia.direcionamentoNaoGestor)
				.as("competencia"),
			Projections.fields(Cargo.class, qCargo.id, qCargo.nome).as("cargo")
		));
		return map(cargoCompQuery.fetch());
	}
*/

	@Override
	public List<CargoCompetenciaTO> findCargoCompetenciaByCargoAndTipoCompetenciaAtivos(Long idCargo, Long idTipoCompetencia) {
		QCargo qCargo = QCargo.cargo;
		QCompetencia qCompetencia = QCompetencia.competencia;

		JPAQuery<CargoCompetencia> cargoCompQuery = select();
		cargoCompQuery.join(qCargoCompetencia.cargo, qCargo);
		cargoCompQuery.join(qCargoCompetencia.competencia, qCompetencia);
		cargoCompQuery.where(qCompetencia.tipo.id.eq(idTipoCompetencia)
			.and(qCargo.id.eq(idCargo))
			.and(qCargoCompetencia.situacao.eq(Situacao.ATIVA)));
		cargoCompQuery.select(Projections.fields(
			CargoCompetencia.class,
			qCargoCompetencia.id,
			qCargoCompetencia.peso,
			qCargoCompetencia.situacao,
			qCargoCompetencia.ordem,
			Projections.fields(Competencia.class,
				qCompetencia.id,
				qCompetencia.descricaoAbreviada,
				qCompetencia.direcionamentoGestor,
				qCompetencia.direcionamentoNaoGestor)
				.as("competencia"),
			Projections.fields(Cargo.class, qCargo.id, qCargo.nome).as("cargo")
		));
		return map(cargoCompQuery.fetch());
	}

	@Override
	public List<CargoCompetenciaTO> findCargoCompetenciaByCompetenciaAtivos(Long idCompetencia) {
		QCargo qCargo = QCargo.cargo;
		QCompetencia qCompetencia = QCompetencia.competencia;

		JPAQuery<CargoCompetencia> cargoCompQuery = select();
		cargoCompQuery.join(qCargoCompetencia.cargo, qCargo);
		cargoCompQuery.join(qCargoCompetencia.competencia, qCompetencia);
		cargoCompQuery.where(qCompetencia.id.eq(idCompetencia)
			.and(qCargoCompetencia.situacao.eq(Situacao.ATIVA)));
		cargoCompQuery.select(Projections.fields(
			CargoCompetencia.class,
			qCargoCompetencia.id,
			qCargoCompetencia.peso,
			qCargoCompetencia.situacao,
			qCargoCompetencia.ordem,
			Projections.fields(Competencia.class,
				qCompetencia.id,
				qCompetencia.descricaoAbreviada,
				qCompetencia.direcionamentoGestor,
				qCompetencia.direcionamentoNaoGestor)
				.as("competencia"),
			Projections.fields(Cargo.class, qCargo.id, qCargo.nome).as("cargo")
		));
		return map(cargoCompQuery.fetch());
	}

	@Override
	public CargoCompetenciaTO findCargoCompetenciaByCargoAndCompetencia(Long idCargo, Long idCompetencia) {
		return map(cargoCompetenciaRepository.findByCargoIdAndCompetenciaId(idCargo, idCompetencia));
	}

	@Override
	public void inativarCargoCompetenciaByCargoAndTipoCompetencia(Long idCargo, Long idTipoCompetencia) {
		List<CargoCompetenciaTO> cargoCompetenciaTOList = findCargoCompetenciaByCargoAndTipoCompetenciaAtivos(idCargo, idTipoCompetencia);
		cargoCompetenciaTOList.forEach(cargoCompetenciaTO -> {
			cargoCompetenciaTO.setSituacao(Situacao.INATIVA);
			saveOrUpdate(cargoCompetenciaTO);
		});
	}
}
