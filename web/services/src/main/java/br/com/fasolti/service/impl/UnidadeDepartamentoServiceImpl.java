package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Departamento;
import br.com.fasolti.avd.domain.QUnidadeDepartamento;
import br.com.fasolti.avd.domain.UnidadeDepartamento;
import br.com.fasolti.avd.repository.UnidadeDepartamentoRepository;
import br.com.fasolti.avd.service.CargoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.service.UnidadeDepartamentoService;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.UnidadeDepartamentoTO;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeDepartamentoServiceImpl extends BaseAvdService<UnidadeDepartamento, UnidadeDepartamentoTO, UnidadeDepartamentoTO> implements UnidadeDepartamentoService {

	private final QUnidadeDepartamento qUnidadeDepartamento = QUnidadeDepartamento.unidadeDepartamento;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	public UnidadeDepartamentoServiceImpl(UnidadeDepartamentoRepository repository) {
		super(repository);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void atualizaGestorUnidadeDepartamento(UnidadeDepartamentoTO unidadeDeptoExistente, FuncionarioTO funcionarioTO) {
		//Para simplificar o código, sempre será realizada a exclusão/inclusão na tabela unidade_departamento. A inclusão somente ocorrerá caso o colaborador seja Gestor de Área ou Gestor de RH
		if (unidadeDeptoExistente != null) {
			delete(unidadeDeptoExistente);
		}

		if (funcionarioTO.getTipoPerfil().equals(TipoPerfil.GESTOR_AREA)  || funcionarioTO.getTipoPerfil().equals(TipoPerfil.GESTOR_RH)  ) {
			UnidadeDepartamentoTO unidadeDepartamento = new UnidadeDepartamentoTO();
			unidadeDepartamento.setDepartamento(funcionarioTO.getDepartamento());
			unidadeDepartamento.setUnidade(funcionarioTO.getUnidade());
			unidadeDepartamento.setGestor(funcionarioTO);
			save(unidadeDepartamento);
		}

	}

	@Override
	public UnidadeDepartamentoTO obterPorUnidadeEDepartamento(Long unidadeId, Long departamentoId) {
		JPAQuery<UnidadeDepartamento> unidDepQuery = select();
		unidDepQuery.where(qUnidadeDepartamento.unidade.id.eq(unidadeId).and(qUnidadeDepartamento.departamento.id.eq(departamentoId)));
		List<UnidadeDepartamento> lista = unidDepQuery.fetch();
//		return map(unidDepQuery.fetchOne());
		if (lista.isEmpty())
			return null;
		return map(lista.get(0));
	}

	@Override
	public List<UnidadeDepartamentoTO> obterDepartamentosUnidadePorUnidade(Long id) {
		return map(select().where(qUnidadeDepartamento.unidade.id.eq(id)).orderBy(qUnidadeDepartamento.departamento.nome.asc()).fetch());
	}

	@Override
	public UnidadeDepartamentoTO obterDepartamentoUnidadePorGestorId(Long gestorId) {
		JPAQuery<UnidadeDepartamento> unidDepQuery = select();
		unidDepQuery.where(qUnidadeDepartamento.gestor.id.eq(gestorId));
		return map(unidDepQuery.fetchOne());
	}

	@Override
	public List<UnidadeDepartamentoTO> obterDepartamentoUnidadePorDepartamento(Long departamentoId) {
		JPAQuery<UnidadeDepartamento> unidDepQuery = select();
		unidDepQuery.where(qUnidadeDepartamento.departamento.id.eq(departamentoId));
		return map(unidDepQuery.fetch());
	}

}
