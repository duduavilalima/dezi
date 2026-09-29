package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Departamento;
import br.com.fasolti.avd.domain.DepartamentoGestor;
import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.repository.DepartamentoGestorRepository;
import br.com.fasolti.avd.repository.FuncionarioRepository;
import br.com.fasolti.avd.service.DepartamentoGestorService;
import br.com.fasolti.avd.service.DepartamentoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.DepartamentoGestorTO;
import br.com.fasolti.avd.transfer.DepartamentoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DepartamentoGestorServiceImpl
	extends BaseAvdService<DepartamentoGestor, DepartamentoGestorTO, DepartamentoGestorTO>
	implements DepartamentoGestorService {

	@Autowired
	private DepartamentoGestorRepository departamentoGestorRepository;

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	public DepartamentoGestorServiceImpl(DepartamentoGestorRepository repository) {
		super(repository);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void atualizarDepartamentoGestor(Long idDepartamento, Long idFuncionario) {
		departamentoGestorRepository.deleteByFuncionarioId(idFuncionario);

		FuncionarioTO funcionarioTO = funcionarioService.findById(idFuncionario);
		if (TipoPerfil.GESTOR_AREA.equals(funcionarioTO.getTipoPerfil())) {
			Funcionario gestor = new Funcionario();
			gestor.setId(idFuncionario);

			Departamento departamento = new Departamento();
			departamento.setId(idDepartamento);

			DepartamentoGestor dpGestor = new DepartamentoGestor();
			dpGestor.setDepartamento(departamento);
			dpGestor.setFuncionario(gestor);
			dpGestor.setCreatedBy(getLoggedUser().getMatricula());
			dpGestor.setCreationDate(new Date(System.currentTimeMillis()));

			departamentoGestorRepository.save(dpGestor);

			DepartamentoTO dpTO = new DepartamentoTO();
			dpTO.setId(idDepartamento);
			List<DepartamentoTO> dpDescesdentes = departamentoService.obterDepartamentosDescendentes(dpTO);

			dpDescesdentes.forEach(departamentoTO -> {
				Departamento dp = new Departamento();
				departamento.setId(departamentoTO.getId());
				DepartamentoGestor dpGst = new DepartamentoGestor();
				dpGst.setDepartamento(departamento);
				dpGst.setFuncionario(gestor);
				dpGst.setCreatedBy(getLoggedUser().getMatricula());
				dpGst.setCreationDate(new Date(System.currentTimeMillis()));
				departamentoGestorRepository.save(dpGst);
			});
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void atualizarDepartamentoGestorEmpresa(Long idEmpresa) {
		List<Funcionario> funcionarios = funcionarioRepository.findByEmpresaIdAndSituacao(idEmpresa, Situacao.ATIVA);
		funcionarios.forEach(funcionarioTO -> {
			this.atualizarDepartamentoGestor(funcionarioTO.getDepartamento().getId(), funcionarioTO.getId());
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void atualizarDepartamentoGestorTodos() {
		String msg = "Processando gestor: '%s' da empresa '%s'";
		List<Funcionario> funcionarios = funcionarioRepository.findByTipoPerfilAndSituacao(TipoPerfil.GESTOR_AREA, Situacao.ATIVA);
		funcionarios.forEach(funcionarioTO -> {
			System.out.println(String.format(msg, funcionarioTO.getNome(), funcionarioTO.getEmpresa().getNome()));
			this.atualizarDepartamentoGestor(funcionarioTO.getDepartamento().getId(), funcionarioTO.getId());
		});
	}
}
