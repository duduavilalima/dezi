package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.UnidadeTO;
import br.com.fasolti.avd.transfer.UnidadeTreeTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UnidadeService extends AvdService<UnidadeTO, UnidadeTO> {
	
	List<UnidadeTO> findByEmpresaIdAndNomeIgnoreCase(Long idEmpresa, String nome);

	List<UnidadeTO> obterSubunidadesDeUnidade(Long idUnidade);

	List<UnidadeTreeTO> listarArvoreDeHierarquiaDeUnidade();

	List<UnidadeTO> listarUnidadesAtivas();


}
