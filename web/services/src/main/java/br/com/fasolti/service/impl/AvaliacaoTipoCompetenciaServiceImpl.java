package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.AvaliacaoTipoCompetencia;
import br.com.fasolti.avd.domain.QAvaliacaoTipoCompetencia;
import br.com.fasolti.avd.domain.QTipoCompetencia;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.avd.repository.AvaliacaoTipoCompetenciaRepository;
import br.com.fasolti.avd.service.AvaliacaoTipoCompetenciaService;
import br.com.fasolti.avd.service.ParticipanteService;
import br.com.fasolti.avd.transfer.AvaliacaoTipoCompetenciaTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoTipoCompetenciaServiceImpl
	extends BaseAvdService<AvaliacaoTipoCompetencia, AvaliacaoTipoCompetenciaTO, AvaliacaoTipoCompetenciaTO>
	implements AvaliacaoTipoCompetenciaService {


	private final QAvaliacaoTipoCompetencia qAvaliacaoTipoCompetencia = QAvaliacaoTipoCompetencia.avaliacaoTipoCompetencia;
	private final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;

	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	public AvaliacaoTipoCompetenciaServiceImpl(AvaliacaoTipoCompetenciaRepository repository) {
		super(repository);
	}

	@Override
	@Cacheable("tipos_avaliacao")
	public List<AvaliacaoTipoCompetenciaTO> obterAvaliacaoTipoCompetenciaProjections(Long idAvaliacao) {
		JPAQuery<AvaliacaoTipoCompetencia> avalTipoQuery = select();
		avalTipoQuery.where(qAvaliacaoTipoCompetencia.avaliacao.id.eq(idAvaliacao));
		avalTipoQuery.join(qAvaliacaoTipoCompetencia.tipoCompetencia, qTipoCompetencia);
		avalTipoQuery.select(Projections.fields(
			AvaliacaoTipoCompetencia.class,
			qAvaliacaoTipoCompetencia.id,
			qAvaliacaoTipoCompetencia.perfilAgente,
			Projections.fields(TipoCompetencia.class, qTipoCompetencia.id).as("tipoCompetencia")
		));

		return map(avalTipoQuery.fetch());
	}

	@Override
	public List<AvaliacaoTipoCompetenciaTO> findIdsTiposCompetenciaByAvaliacaoId(Long idAvaliacao) {
		JPAQuery<AvaliacaoTipoCompetencia> avalTipoQuery = select();
		avalTipoQuery.where(qAvaliacaoTipoCompetencia.avaliacao.id.eq(idAvaliacao));
		avalTipoQuery.join(qAvaliacaoTipoCompetencia.tipoCompetencia, qTipoCompetencia);
		avalTipoQuery.distinct();
		avalTipoQuery.select(Projections.fields(
			AvaliacaoTipoCompetencia.class,
			Projections.fields(TipoCompetencia.class, qTipoCompetencia.id).as("tipoCompetencia")
		));
		return map(avalTipoQuery.fetch());
	}

}
