package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.MunicipioTO;

import java.util.List;

public interface MunicipioService {

	List<MunicipioTO> byUf(String uf);

	List<MunicipioTO> containsNome(String nome);

    List<MunicipioTO> containsNomeByUf(String uf, String nome);
}
