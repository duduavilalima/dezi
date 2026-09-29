package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.CargoTipoCompetenciaRepository;
import br.com.fasolti.avd.service.CargoCompetenciaService;
import br.com.fasolti.avd.service.CargoService;
import br.com.fasolti.avd.service.CargoTipoCompetenciaService;
import br.com.fasolti.avd.service.TipoCompetenciaService;
import br.com.fasolti.avd.transfer.CargoTO;
import br.com.fasolti.avd.transfer.CargoTipoCompetenciaTO;
import br.com.fasolti.avd.transfer.TipoCompetenciaTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoTipoCompetenciaServiceImpl
	extends BaseAvdService<CargoTipoCompetencia, CargoTipoCompetenciaTO, CargoTipoCompetenciaTO>
	implements CargoTipoCompetenciaService {

	private final QCargoTipoCompetencia qCargoTipoCompetencia = QCargoTipoCompetencia.cargoTipoCompetencia;
	private final QCargo qCargo = QCargo.cargo;
	private final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;

	@Autowired
	private CargoTipoCompetenciaRepository cargoTipoCompetenciaRepository;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private TipoCompetenciaService tipoCompetenciaService;

	@Autowired
	public CargoTipoCompetenciaServiceImpl(CargoTipoCompetenciaRepository repository) {
		super(repository);
	}

	@Override
	protected CargoTipoCompetenciaTO doAfterCreate(CargoTipoCompetenciaTO object) throws ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	protected void doBeforeSave(CargoTipoCompetencia entity, CargoTipoCompetenciaTO object) throws ServiceException {
		if (object.getSituacao().equals(Situacao.ATIVA)) {
			updateSituacaoCargo(object);
		}
	}

	@Override
	protected void doBeforeUpdate(CargoTipoCompetencia entity, CargoTipoCompetenciaTO object) throws ServiceException {
		//Ao inativar um tipo competencia, inativar também suas respectivas competências
		validar(object);
		if (entity.getSituacao().equals(Situacao.ATIVA) && object.getSituacao().equals(Situacao.INATIVA)) {
			cargoCompetenciaService.inativarCargoCompetenciaByCargoAndTipoCompetencia(object.getCargo().getId(), object.getTipoCompetencia().getId());
		}

		if (object.getSituacao().equals(Situacao.ATIVA)) {
			updateSituacaoCargo(object);
		}
	}

	private void validar(CargoTipoCompetenciaTO cargoTipoCompetenciaTO) {
		//Tem que dar load para obter o tipo de competencia
		TipoCompetenciaTO tipoCompetenciaTO = tipoCompetenciaService.load(cargoTipoCompetenciaTO.getTipoCompetencia().getId());

		//Ao ativar um cargo competencia, com a competencia inativada, dar erro!
		if(cargoTipoCompetenciaTO.getSituacao().equals(Situacao.ATIVA) && tipoCompetenciaTO.getSituacao().equals(Situacao.INATIVA)) {
			throw new ValidationException("validation.status", "tipo de competência", cargoTipoCompetenciaTO.getTipoCompetencia().getDescricao());
		}
	}

	private void updateSituacaoCargo(CargoTipoCompetenciaTO object) {
		CargoTO cargo = cargoService.load(object.getCargo().getId());
		cargo.setSituacao(Situacao.ATIVA);
		cargoService.update(cargo, cargo.getId());
	}

	@Override
	public List<CargoTipoCompetenciaTO> findAllCargoTipoCompetenciaByCargo(Long idCargo) {
		JPAQuery<CargoTipoCompetencia> cargoTipoCompQuery = select();
		cargoTipoCompQuery.join(qCargoTipoCompetencia.cargo, qCargo);
		cargoTipoCompQuery.join(qCargoTipoCompetencia.tipoCompetencia, qTipoCompetencia);
		cargoTipoCompQuery.where(qCargoTipoCompetencia.cargo.id.eq(idCargo));
		cargoTipoCompQuery.select(Projections.fields(
			CargoTipoCompetencia.class,
			qCargoTipoCompetencia.id,
			qCargoTipoCompetencia.peso,
			qCargoTipoCompetencia.situacao,
			Projections.fields(Cargo.class, qCargo.id).as("cargo"),
			Projections.fields(TipoCompetencia.class, qTipoCompetencia.id, qTipoCompetencia.nome).as("tipoCompetencia")
		));
		return map(cargoTipoCompQuery.fetch());
	}

	@Override
	public List<CargoTipoCompetenciaTO> findAllCargoTipoCompetenciaByTipoCompetencia(Long tipoCompetenciaId) {
		JPAQuery<CargoTipoCompetencia> cargoTipoCompQuery = select();
		cargoTipoCompQuery.join(qCargoTipoCompetencia.cargo, qCargo);
		cargoTipoCompQuery.join(qCargoTipoCompetencia.tipoCompetencia, qTipoCompetencia);
		cargoTipoCompQuery.where(qCargoTipoCompetencia.tipoCompetencia.id.eq(tipoCompetenciaId));
		cargoTipoCompQuery.select(Projections.fields(
			CargoTipoCompetencia.class,
			qCargoTipoCompetencia.id,
			qCargoTipoCompetencia.peso,
			qCargoTipoCompetencia.situacao,
			Projections.fields(Cargo.class, qCargo.id).as("cargo"),
			Projections.fields(TipoCompetencia.class, qTipoCompetencia.id, qTipoCompetencia.nome).as("tipoCompetencia")
		));
		return map(cargoTipoCompQuery.fetch());
	}

	private List<CargoTipoCompetenciaTO> findAllCargoTipoCompetenciaByCargoAndTipoCompetenciaAtivos(Long cargoId, Long tipoCompetenciaId) {
		JPAQuery<CargoTipoCompetencia> cargoTipoCompQuery = select();
		cargoTipoCompQuery.join(qCargoTipoCompetencia.cargo, qCargo);
		cargoTipoCompQuery.join(qCargoTipoCompetencia.tipoCompetencia, qTipoCompetencia);
		cargoTipoCompQuery.where(qCargoTipoCompetencia.tipoCompetencia.id.eq(tipoCompetenciaId)
			.and(qCargoTipoCompetencia.cargo.id.eq(cargoId))
			.and(qCargoTipoCompetencia.situacao.eq(Situacao.ATIVA)));
		cargoTipoCompQuery.select(Projections.fields(
			CargoTipoCompetencia.class,
			qCargoTipoCompetencia.id,
			qCargoTipoCompetencia.peso,
			qCargoTipoCompetencia.situacao,
			Projections.fields(Cargo.class, qCargo.id).as("cargo"),
			Projections.fields(TipoCompetencia.class, qTipoCompetencia.id, qTipoCompetencia.nome).as("tipoCompetencia")
		));
		return map(cargoTipoCompQuery.fetch());
	}

	/**
	 * Inativa TODOS OS cargoTipoCompetencia e suas respectivas competencias (dado um cargo)
	 *
	 * @param cargoId
	 * @param tipoCompetenciaId
	 */
	@Override
	public void inativarCargoTipoCompetenciaByCargo(Long cargoId, Long tipoCompetenciaId) {
		/*Ao inativar um TipoCompetencia, repassar para os cargos tipo competencia > competencias > detalhamentos*/
		List<CargoTipoCompetenciaTO> cargoTipoCompetenciaTOList = findAllCargoTipoCompetenciaByCargoAndTipoCompetenciaAtivos(cargoId, tipoCompetenciaId);
		cargoTipoCompetenciaTOList.forEach(cargoTipoCompetenciaTO -> {
			cargoTipoCompetenciaTO.setSituacao(Situacao.INATIVA);
			saveOrUpdate(cargoTipoCompetenciaTO);
		});
	}

	/**
	 * Inativa apenas um cargoTipoCompetencia e suas respectivas competencias
	 *
	 * @param cargoId
	 * @param tipoCompetenciaId
	 */
	@Override
	public void inativarCargoTipoCompetencia(Long cargoId, Long tipoCompetenciaId) {
		/*Ao inativar um TipoCompetencia, repassar para os cargos tipo competencia > competencias > detalhamentos*/
		CargoTipoCompetenciaTO cargoTipoCompetenciaTO = findCargoTipoCompetenciaByCargoAndTipoCompetencia(cargoId, tipoCompetenciaId);
		cargoTipoCompetenciaTO.setSituacao(Situacao.INATIVA);
		cargoCompetenciaService.inativarCargoCompetenciaByCargoAndTipoCompetencia(cargoTipoCompetenciaTO.getCargo().getId(), cargoTipoCompetenciaTO.getTipoCompetencia().getId());
		saveOrUpdate(cargoTipoCompetenciaTO);
	}

	/**
	 * Inativa todos os cargoTipoCompetencia, independente do cargo
	 *
	 * @param tipoCompetenciaId
	 */
	@Override
	public void inativarCargoTipoCompetenciaByTipoCompetencia(Long tipoCompetenciaId) {
		List<CargoTipoCompetenciaTO> cargoTipoCompetenciaTOList = findAllCargoTipoCompetenciaByTipoCompetencia(tipoCompetenciaId);
		cargoTipoCompetenciaTOList.forEach(cargoTipoCompetenciaTO -> {
			cargoTipoCompetenciaTO.setSituacao(Situacao.INATIVA);
			cargoCompetenciaService.inativarCargoCompetenciaByCargoAndTipoCompetencia(cargoTipoCompetenciaTO.getCargo().getId(), cargoTipoCompetenciaTO.getTipoCompetencia().getId());
			saveOrUpdate(cargoTipoCompetenciaTO);
		});
	}

	@Override
	public List<CargoTipoCompetenciaTO> findAllCargoTipoCompetenciaByCargoAndAtivos(Long cargoId) {
		return map(cargoTipoCompetenciaRepository.findByCargoIdAndSituacao(cargoId, Situacao.ATIVA));
	}

	@Override
	public List<CargoTipoCompetenciaTO> buscarTodosTiposAtivos() {
		return cargoTipoCompetenciaRepository.findBySituacao(Situacao.ATIVA);
	}

	@Override
	public CargoTipoCompetenciaTO findCargoTipoCompetenciaByCargoAndTipoCompetencia(Long idCargo, Long idCompetencia) {
		return map(cargoTipoCompetenciaRepository.findByCargoIdAndTipoCompetenciaId(idCargo, idCompetencia));
	}

}
