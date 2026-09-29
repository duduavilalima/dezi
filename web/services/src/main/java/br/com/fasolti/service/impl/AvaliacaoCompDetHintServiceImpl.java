package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.AvaliacaoCompDetHintRepository;
import br.com.fasolti.avd.service.AvaliacaoCompDetHintService;
import br.com.fasolti.avd.transfer.AvaliacaoCompDetHintTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoCompDetHintServiceImpl
	extends BaseAvdService<AvaliacaoCompDetHint,AvaliacaoCompDetHintTO,AvaliacaoCompDetHintTO>
	implements AvaliacaoCompDetHintService{

	private final QAvaliacaoCompDetHint qAvaliacaoCompDetHint = QAvaliacaoCompDetHint.avaliacaoCompDetHint;
	private static final QAvaliacaoCompDet qAvaliacaoCompDet = QAvaliacaoCompDet.avaliacaoCompDet;

	@Autowired
	private AvaliacaoCompDetHintRepository avaliacaoCompDetHintRepository;

	@Autowired
	public AvaliacaoCompDetHintServiceImpl(AvaliacaoCompDetHintRepository avaliacaoCompDetHintRepository) {
		super(avaliacaoCompDetHintRepository);
	}


	@Override
	public List<AvaliacaoCompDetHintTO> listByAvaliacaoCompDetId(Long avaliacaoCompDetId) {
		return map(avaliacaoCompDetHintRepository.findByAvaliacaoCompDetId(avaliacaoCompDetId));
	}

	@Override
	public List<AvaliacaoCompDetHintTO> listByAvaliacaoCompDetIdProjections(Long avaliacaoCompDetId) {
		JPAQuery<AvaliacaoCompDetHint> avalHintQuery = select();
		avalHintQuery.where(qAvaliacaoCompDetHint.avaliacaoCompDet.id.eq(avaliacaoCompDetId));
		avalHintQuery.select(Projections.fields(
			AvaliacaoCompDetHint.class,
			qAvaliacaoCompDetHint.id,
			qAvaliacaoCompDetHint.idGraduacao,
			qAvaliacaoCompDetHint.idHint,
			qAvaliacaoCompDetHint.hint,
			Projections.fields(AvaliacaoCompDet.class, qAvaliacaoCompDet.id, qAvaliacaoCompDet.ordem).as("avaliacaoCompDet")
		));

		return map(avalHintQuery.fetch());
	}

	@Override
	public AvaliacaoCompDetHintTO obterAvaliacaoCompDetHintProjections(Long avaliacaoCompDetId, Long hintId) {
		JPAQuery<AvaliacaoCompDetHint> avalHintQuery = select();
		avalHintQuery.where(qAvaliacaoCompDetHint.avaliacaoCompDet.id.eq(avaliacaoCompDetId)
			.and(qAvaliacaoCompDetHint.idHint.eq(hintId)));
		avalHintQuery.select(Projections.fields(
			AvaliacaoCompDetHint.class,
			qAvaliacaoCompDetHint.id
		));

		return map(avalHintQuery.fetchOne());
	}

	@Override
	public List<AvaliacaoCompDetHintTO> findByGraduacaoId(Long graduacaoId) {
		return map(avaliacaoCompDetHintRepository.findByIdGraduacao(graduacaoId));
	}
}
