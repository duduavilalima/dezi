package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.AvaliacaoCompDetRepository;
import br.com.fasolti.avd.repository.PreenchimentoRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.types.GraduacaoCorEnum;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AvaliacaoCompDetServiceImpl
	extends BaseAvdService<AvaliacaoCompDet, AvaliacaoCompDetTO, AvaliacaoCompDetTO>
	implements AvaliacaoCompDetService {

	private final QAvaliacaoCompDet qAvaliacaoCompDet = QAvaliacaoCompDet.avaliacaoCompDet;

	private final QAvaliacaoCompetencia qAvaliacaoCompetencia = QAvaliacaoCompetencia.avaliacaoCompetencia;

	@Autowired
	private AvaliacaoCompDetRepository avaliacaoCompDetRepository;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private AvaliacaoCompDetHintService  avaliacaoCompDetHintService;

	@Autowired
	private GraduacaoCompetenciaService graduacaoCompetenciaService;

	@Autowired
	private ModeloGraduacaoCompService modeloGraduacaoCompService;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	public AvaliacaoCompDetServiceImpl(AvaliacaoCompDetRepository avaliacaoCompDetRepository) {
		super(avaliacaoCompDetRepository);
	}


	@Override
	public List<AvaliacaoCompDetTO> listByAvaliacaoCompetenciaId(Long avaliacaoCompetenciaId) {
		return map(avaliacaoCompDetRepository.findByAvaliacaoCompetenciaId(avaliacaoCompetenciaId));
	}

	@Override
	public List<AvaliacaoCompDetTO> listByAvaliacaoCompetenciaAndAvaliadorWithPreenchimentoAndHint(Long avaliacaoCompetenciaId, Long avaliadorId) {
		List<AvaliacaoCompDetTO> listaDetalhamentos = new ArrayList<>();

		List<PreenchimentoTO> autoavaliacoes = preenchimentoService.listByAvaliadoAndLimiteAndCompetenciaProjections(avaliadorId, new Date(), SituacaoAvaliacao.CONCLUIDA, avaliacaoCompetenciaId);
		List<PreenchimentoTO> avaliacoes = preenchimentoService.listByAvaliadorAndLimiteAndCompetenciaProjections(avaliadorId, new Date(), SituacaoAvaliacao.CONCLUIDA, avaliacaoCompetenciaId);
		List<PreenchimentoTO> listaPreenchimento = new ArrayList<>();
		listaPreenchimento.addAll(autoavaliacoes);
		listaPreenchimento.addAll(avaliacoes);

		for (PreenchimentoTO preenchimentoTO : listaPreenchimento) {
			AvaliacaoCompDetTO detalhamento = mapper.map(preenchimentoTO.getAvaliacaoCompDet(), AvaliacaoCompDetTO.class);

			preenchimentoTO.getParticipante().getAvaliado().setFoto(getFotoAvaliado(preenchimentoTO.getParticipante().getAvaliado().getId()));

			if (!listaDetalhamentos.contains(detalhamento)) {
				detalhamento.setPreenchimentos(new ArrayList<>());
				detalhamento.getPreenchimentos().add(preenchimentoTO);

				List<AvaliacaoCompDetHintTO> listaHints = avaliacaoCompDetHintService.listByAvaliacaoCompDetId(detalhamento.getId());

				ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(detalhamento.getIdModeloGraduacao(), preenchimentoTO.getParticipante().getAvaliacao().getId());

				//FIXME: Trocar esse setCor posteriormente!
				for (AvaliacaoCompDetHintTO hintTO : listaHints) {
					GraduacaoCompetenciaTO graduacaoTO = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), hintTO.getIdGraduacao());
					graduacaoTO.setCor(GraduacaoCorEnum.valueOf((graduacaoTO.getOrdem()==null || graduacaoTO.getOrdem()>10)?11:graduacaoTO.getOrdem()).getNome());
					hintTO.setGraduacao(graduacaoTO);
				}

				Collections.sort(listaHints, new Comparator<AvaliacaoCompDetHintTO>() {
					@Override
					public int compare(AvaliacaoCompDetHintTO o1, AvaliacaoCompDetHintTO o2) {
						return ObjectUtils.compare(o1.getGraduacao().getOrdem(), o2.getGraduacao().getOrdem());
					}
				});

				detalhamento.setHints(listaHints);

				listaDetalhamentos.add(detalhamento);
			} else {

				int indiceDetalhamento = listaDetalhamentos.indexOf(detalhamento);

				AvaliacaoCompDetTO detalhamentoExistente = listaDetalhamentos.get(indiceDetalhamento);
				detalhamentoExistente.getPreenchimentos().add(preenchimentoTO);
			}

		}

		return listaDetalhamentos;
	}

	@Override
	public AvaliacaoCompDetTO obterAvaliacaoCompDetProjections(Long avalCompId, Long idCompDet, Long avaliacaoCompDetId) {
		JPAQuery<AvaliacaoCompDet> avalCompDetQuery = select();

		BooleanBuilder predicate = new BooleanBuilder();

		if (avalCompId != null)
			predicate.and(qAvaliacaoCompDet.avaliacaoCompetencia.id.eq(avalCompId));

		if (idCompDet != null)
			predicate.and(qAvaliacaoCompDet.idCompetenciaDetalhamento.eq(idCompDet));

		if (avaliacaoCompDetId != null)
			predicate.and(qAvaliacaoCompDet.id.eq(avaliacaoCompDetId));

		avalCompDetQuery.where(predicate);
		avalCompDetQuery.select(Projections.fields(
			AvaliacaoCompDet.class,
			qAvaliacaoCompDet.id,
			qAvaliacaoCompDet.idCompetenciaDetalhamento,
			qAvaliacaoCompDet.idModeloGraduacao,
			Projections.fields(AvaliacaoCompetencia.class, qAvaliacaoCompetencia.id).as("avaliacaoCompetencia")
		));

		return map(avalCompDetQuery.fetchOne());
	}


	private String getFotoAvaliado (Long avaliadoId){
		FotoTO foto = fotoService.obterFotoPerfilFuncionario(avaliadoId);
		if(foto != null && foto.getBase64() != null && !foto.getBase64().trim().equals(""))
			return foto.getBase64();
		else
			return "../img/avatars/anon.png";
	}



}
