package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CampanhaTO;

import java.util.List;

public interface CampanhaService extends AvdService<CampanhaTO, CampanhaTO> {

	CampanhaTO findByEmpresaAndDescricaoIgnoreCase(Long idEmpresa, String descricao);

	List<CampanhaTO> findByEmpresa();

	List<CampanhaTO> findByEmpresaPerfil();

	List<CampanhaTO> findByEmpresaActiveProjections();

}
