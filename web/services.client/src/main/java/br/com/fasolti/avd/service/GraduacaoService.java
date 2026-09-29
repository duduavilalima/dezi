package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.GraduacaoTO;
import br.com.fasolti.avd.transfer.ModeloGraduacaoTO;
import br.com.fasolti.avd.types.Situacao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GraduacaoService extends AvdService<GraduacaoTO, GraduacaoTO> {

	List<GraduacaoTO> findAllGraduacoesByModelo(ModeloGraduacaoTO modelo);

	List<GraduacaoTO> findAllGraduacoesAtivasByModelo(ModeloGraduacaoTO modelo);

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	List<GraduacaoTO> findAllGraduacoesByModeloAndSituacao(ModeloGraduacaoTO modelo, Situacao situacao);

	List<GraduacaoTO> findAllGraduacoesByModeloAtivos(Long modeloId);

	GraduacaoTO findByModeloGraduacaoIdAndLabelIgnoreCase(Long idEmpresa, String label);

	List<GraduacaoTO> obterGraduacoesProjections(Long modeloGraduacaoId);

	GraduacaoTO alterarSituacaoGraduacao(Long id, Situacao situacao);

}
