package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.domain.Participante;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipanteRepository extends QueryDslCrudRepository<Participante> {

	List<Participante> findByAvaliado(Funcionario avaliado);

	List<Participante> findByAvaliador(Funcionario avaliador);

	List<Participante> findByRevisor(Funcionario revisor);

	List<Participante> findByAvaliacaoId(Long avaliacaoId);

	List<Participante> findByAvaliadorIdAndAvaliacaoId(Long avaliadorId, Long avaliacaoId);

	List<Participante> findByAvaliacaoIdAndAvaliadorId(Long avaliacaoId, Long avaliadorId);

	List<Participante> findByAvaliacaoIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(Long idAvaliacao, Long id);

	List<Participante> findByAvaliacaoCampanhaIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(Long idCampanha, Long id);

	List<Participante> findByAvaliadorUnidadeIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(Long idUnidade, Long id);

	List<Participante> findByAvaliadorDepartamentoIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(Long idDepartamento, Long id);

	List<Participante> findByAvaliacaoIdAndAvaliadorUnidadeIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(Long idAvaliacao, Long idUnidade, Long id);

	List<Participante> findByAvaliacaoIdAndAvaliadorUnidadeIdAndAvaliadorDepartamentoIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(Long idAvaliacao, Long idUnidade, Long idDepartamento, Long id);

	List<Participante> findByAvaliacaoIdAndAvaliadorDepartamentoIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(Long idAvaliacao, Long idDepartamento, Long id);
}
