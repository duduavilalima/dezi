package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.EmpresaTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.simple.EmpresaAuthSimpleTO;
import br.com.fasolti.commons.lang.ServiceException;

import java.util.List;

public interface EmpresaService extends AvdService<EmpresaTO, EmpresaTO> {
	List<EmpresaTO> findAllEmpresasContratanteUsuarioLogado();

	EmpresaTO findById(Long id);

	List<EmpresaTO> findAllEmpresasContratanteUsuario(FuncionarioTO funcionarioTO);

	List<EmpresaTO> empresasAtivas();

	List<EmpresaAuthSimpleTO> empresasAuthAtivas();

	List<EmpresaAuthSimpleTO> empresasAuthAtivasByHost(String hostName);
	List<EmpresaAuthSimpleTO> empresasAuthAtivasByHost(String hostName, boolean fallbackTodasEmpresas);

	EmpresaTO empresasPorHost(String host) throws ServiceException;

	List<EmpresaTO> empresasTodasAsEmpresas() throws ServiceException;
}
