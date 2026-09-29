package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.DimensaoTO;
import br.com.fasolti.avd.types.Situacao;

import java.util.List;

public interface DimensaoService extends AvdService<DimensaoTO, DimensaoTO> {

	List<DimensaoTO> obterDimensaoPorSituacao(Situacao situacao);
}


