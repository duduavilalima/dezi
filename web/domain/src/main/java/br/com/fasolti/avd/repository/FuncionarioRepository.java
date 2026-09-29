package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FuncionarioRepository extends QueryDslCrudRepository<Funcionario> {

	Funcionario findByEmailFuncional(String emailFuncional);

	Funcionario findByEmailFuncionalAndEmpresaIdAndSituacao(String emailFuncional, Long empresaId, Situacao situacao);

	Funcionario findByEmpresaIdAndMatricula(Long empresaId, String matricula);

	Funcionario findByNome(String nome);

	Funcionario findByNomeIgnoreCaseAndEmpresaId(String nome, Long empresaId);

    //Funcionario findByEmpresaIdAndMatriculaAndSituacao(Long empresaId, String matricula, Situacao situacao);

	Funcionario findByEmpresaIdAndMatriculaIgnoreCaseAndSituacao(Long empresaId, String matricula, Situacao situacao);

	@Query("select f from Funcionario f where f.empresa.id = ?1 and lower(f.matricula) = lower(?2) and f.situacao = ?3")
	Funcionario findForLogin(Long empresaId, String matricula, Situacao situacao);

	List<Funcionario> findByEmpresaIdAndSituacao(Long empresaId, Situacao situacao);

	List<Funcionario> findByDepartamentoIdAndSituacaoAndEmpresaId(Long idDepartamento, Situacao situacao, Long idEmpresa);

    List<Funcionario> findByCargoIdAndSituacao(Long idCargo, Situacao situacao);

	List<Funcionario> findByTipoPerfilAndSituacao(TipoPerfil perfil, Situacao situacao);

	List<Funcionario> findByTipoPerfilAndSituacaoAndEmpresaId(TipoPerfil perfil, Situacao situacao, Long idEmpresa);

	List<Funcionario> findByDepartamentoId(Long idDepartamento);

	@Modifying
	@Transactional()
	@Query("update Funcionario f set f.senhaCriptografada = ?2, f.primeiroAcesso = ?3 where f.id = ?1")
	void updateSenha(Long id, String senha, Boolean acesso);
}
