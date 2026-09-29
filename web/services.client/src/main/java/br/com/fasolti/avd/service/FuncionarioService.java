package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.FuncionarioMensagemTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.TrocaDeSenha;
import br.com.fasolti.avd.transfer.mobile.FaleConoscoMobileTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioMobileTO;
import br.com.fasolti.avd.transfer.mobile.UsuarioMobileTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.lang.ValidationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FuncionarioService extends AvdService<FuncionarioTO, FuncionarioTO> {

	FuncionarioTO findByEmailFuncionalAndEmpresaIdAndSituacao(String emailFuncional, Long empresaId, Situacao situacao);

	@Transactional(propagation = Propagation.REQUIRED)
	void forgotPassword(String email, Long empresaId) throws ValidationException;

	void trocarSenha(TrocaDeSenha trocaDeSenha) throws ValidationException;

	FuncionarioTO findById(Long id);

	FuncionarioTO findByNome(String nome);

	FuncionarioTO findByEmpresaIdAndMatricula(Long idEmpresa, String matricula);

	List<FuncionarioTO> funcionariosParaAvaliacao(AvaliacaoTO to);

	List<FuncionarioTO> obterSubordinados(FuncionarioTO funcionario);

	List<FuncionarioTO> obterSubordinadosNaoSelecionadosPorAvaliadoEAvaliacao(FuncionarioTO avaliado, AvaliacaoTO avaliacao);

	List<FuncionarioMensagemTO> obterFuncionarioParticipantesDaAvaliacao(Long idAvaliacao);

	FuncionarioTO obterFuncionarioProjections(Long idFuncionario);

	List<FuncionarioTO> obterFuncionariosPorDepartamentoEUnidade(Long departamentoId, Long unidadeId);

	FuncionarioMobileTO obterFuncionarioMobile(FuncionarioMobileTO usuario);

	List<FuncionarioMobileTO> obterAvaliadosAtivosPorFuncionarioDaEmpresa(UsuarioMobileTO avaliador);

	List<FuncionarioTO> obterFuncionariosPorPerfilAndEmpresa(TipoPerfil perfil, Long idEmpresa);

	void faleConosco(FaleConoscoMobileTO to);

	List<FuncionarioTO> obterFuncionariosAtivosDoDepartamento(Long idDepartamento);

	List<FuncionarioTO> findByDepartamentoIdProjections(Long idDepartamento);

	List<FuncionarioTO> findByCargoIdAndSituacaodProjections(Long idCargo, Situacao situacao);

	List<FuncionarioTO> findBySituacaoProjections(SituacaoFuncionario situacao);

	List<FuncionarioTO> obterColaboradoresPDI();

	FuncionarioTO loadColaboradorPDI(Long funcionarioId);
}
