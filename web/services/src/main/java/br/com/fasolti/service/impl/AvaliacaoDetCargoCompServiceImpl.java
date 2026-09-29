package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.AvaliacaoDetCargoComp;
import br.com.fasolti.avd.domain.QAvaliacaoDetCargoComp;
import br.com.fasolti.avd.repository.AvaliacaoDetCargoCompRepository;
import br.com.fasolti.avd.service.AvaliacaoDetCargoCompService;
import br.com.fasolti.avd.transfer.AvaliacaoDetCargoCompTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoDetCargoCompServiceImpl
	extends BaseAvdService<AvaliacaoDetCargoComp, AvaliacaoDetCargoCompTO, AvaliacaoDetCargoCompTO>
	implements AvaliacaoDetCargoCompService {


	private final QAvaliacaoDetCargoComp qAvaliacaoDetCargoComp = QAvaliacaoDetCargoComp.avaliacaoDetCargoComp;

	@Autowired
	public AvaliacaoDetCargoCompServiceImpl(AvaliacaoDetCargoCompRepository repository) {
		super(repository);
	}

	@Override
	public AvaliacaoDetCargoCompTO obterAvaliacaoDetCargoCompProjections(Long avaliacaoCompDetId, Long detCargoCompId) {
		JPAQuery<AvaliacaoDetCargoComp> avalDetGabQuery = select();
		avalDetGabQuery.where(qAvaliacaoDetCargoComp.avaliacaoCompDet.id.eq(avaliacaoCompDetId)
			.and(qAvaliacaoDetCargoComp.idDetCargoComp.eq(detCargoCompId)));
		avalDetGabQuery.select(Projections.fields(
			AvaliacaoDetCargoComp.class,
			qAvaliacaoDetCargoComp.id
		));

		return map(avalDetGabQuery.fetchOne());
	}
}
