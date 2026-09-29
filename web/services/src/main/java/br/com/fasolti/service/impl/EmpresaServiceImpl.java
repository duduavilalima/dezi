package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.QEmpresa;
import br.com.fasolti.avd.repository.EmpresaRepository;
import br.com.fasolti.avd.service.EmpresaService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.EmpresaTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.simple.EmpresaAuthSimpleTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmpresaServiceImpl
	extends BaseAvdService<Empresa, EmpresaTO, EmpresaTO>
	implements EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private FuncionarioService funcionarioService;

	private final QEmpresa qEmpresa = QEmpresa.empresa;

	@Autowired
	public EmpresaServiceImpl(EmpresaRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<Empresa> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		mandatoryFilter.or(qEmpresa.id.eq(getLoggedUser().getEmpresa().getId()));

		return ServiceFilter.create(qEmpresa, mandatoryFilter);
	}

	@Override
	protected void doBeforeSave(EmpresaTO object) throws ServiceException {
		// Sempre salva com a empresa do usuário logado.
		// TODO Criar uma interface para os objetos que tem contratante
		object.setContratante(getLoggedUser().getEmpresa().getContratante());
	}

	@Override
	protected void doBeforeUpdate(EmpresaTO object) throws ServiceException {
		// Sempre salva com a empresa do usuário logado.
		object.setContratante(getLoggedUser().getEmpresa().getContratante());
	}

	@Override
	public List<EmpresaTO> findAllEmpresasContratanteUsuarioLogado() {
		return map(empresaRepository.findEmpresaByContratanteId(getLoggedUser().getEmpresa().getContratante().getId()));
	}

	@Override
	public EmpresaTO findById(Long id) {
		return map(empresaRepository.findOne(id));
	}

	@Override
	public List<EmpresaTO> findAllEmpresasContratanteUsuario(FuncionarioTO funcionarioTO) {
		FuncionarioTO func = funcionarioService.load(funcionarioTO.getId());
		EmpresaTO empresa = findById(func.getEmpresa().getId());
		List<EmpresaTO> listaTOs = map(empresaRepository.findEmpresaByContratanteId(empresa.getContratante().getId()));

		listaTOs.sort(Comparator.comparing(EmpresaTO::getNome));

		return listaTOs;
	}

	@Override
	public List<EmpresaTO> empresasAtivas() {
		List<Empresa> listaEmpresas = empresaRepository.findBySituacao(Situacao.ATIVA);
		listaEmpresas.sort(Comparator.comparing(Empresa::getNome));
		return map(listaEmpresas);
	}

	@Override
	public List<EmpresaAuthSimpleTO> empresasAuthAtivasByHost(String hostName) {
		return empresasAuthAtivasByHost(hostName, false);
	}

	@Override
	public List<EmpresaAuthSimpleTO> empresasAuthAtivasByHost(String hostName, boolean fallbackTodasEmpresas) {
		List<EmpresaAuthSimpleTO> todasEmpresas = empresasAuthAtivas();
		List<EmpresaAuthSimpleTO> empresas = todasEmpresas.stream()
			.filter(empresa -> Objects.equals(empresa.getHost(), hostName))
			.collect(Collectors.toList());

		if (empresas.isEmpty() && fallbackTodasEmpresas) {
			return todasEmpresas;
		}

		return empresas;
	}

	@Override
	public List<EmpresaAuthSimpleTO> empresasAuthAtivas() {
		List<EmpresaTO> list = empresasAtivas();
		List<EmpresaAuthSimpleTO> listSimple = new ArrayList<>();
		//FIXME: Provisório, refatorar o MAIS rápido possível!!!!!!
		for (EmpresaTO to : list) {
			EmpresaAuthSimpleTO empresaAuthSimpleTO = new EmpresaAuthSimpleTO();
			empresaAuthSimpleTO.setId(to.getId());
			empresaAuthSimpleTO.setNome(to.getNome());
			empresaAuthSimpleTO.setFeedforward(to.getFeedforward());
			empresaAuthSimpleTO.setHost(to.getHost());
			listSimple.add(empresaAuthSimpleTO);
		}

		return listSimple;
	}

	@Override
	public EmpresaTO empresasPorHost(String host) throws ServiceException {
		try {
			return map(empresaRepository.findByHostAndSituacao(host, Situacao.ATIVA));
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<EmpresaTO> empresasTodasAsEmpresas() throws ServiceException {
		try {
			if (TipoPerfil.ADMINISTRADOR.equals(getLoggedUser().getTipoPerfil())) {
				return map(empresaRepository.findAll());
			}
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	protected EmpresaTO doAfterCreate(EmpresaTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}
}
