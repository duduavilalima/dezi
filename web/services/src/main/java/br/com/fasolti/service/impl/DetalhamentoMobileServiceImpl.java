package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.AvaliacaoCompDet;
import br.com.fasolti.avd.repository.AvaliacaoCompDetRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.mobile.DetalhamentoMobileTO;
import br.com.fasolti.avd.transfer.mobile.PreenchimentoMobileTO;
import br.com.fasolti.avd.types.TipoAvMobile;
import br.com.fasolti.service.util.BaseDeziService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Serviço de AvaliacaoCompDet no Mobile, ou seja, DetalhamentoMobile
 */
@Service
public class DetalhamentoMobileServiceImpl extends BaseDeziService<AvaliacaoCompDet, DetalhamentoMobileTO> implements DetalhamentoMobileService {

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private ModeloGraduacaoCompService modeloGraduacaoCompService;

	@Autowired
	private GraduacaoCompetenciaService graduacaoCompetenciaService;

	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	private ParticipanteEvolucaoService participanteEvolucaoService;


	@Autowired
	public DetalhamentoMobileServiceImpl(AvaliacaoCompDetRepository avaliacaoCompDetRepository) {
		super(avaliacaoCompDetRepository);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateDetalhamentosMobile(List<DetalhamentoMobileTO> detalhamentoMobileTOList) {

		//Recebe uma lista de detalhamentos e percorre seus respectivos preenchimentos para atualizá-los
		detalhamentoMobileTOList.forEach(detalhamentoMobileTO -> detalhamentoMobileTO.getListaPreenchimentos().forEach(preenchimentoMobileTO -> {
				/*Pega-se o preenchimento*/
				PreenchimentoTO preenchimentoTO = preenchimentoService.load(preenchimentoMobileTO.getId());

				/*Faz o mapeamento dos dados do mobileTO para o TO propriamente dito*/
				if (preenchimentoMobileTO.getGraduacao() != null && preenchimentoMobileTO.getGraduacao().getId() != null) {
					updatePreenchimento(preenchimentoTO, preenchimentoMobileTO, preenchimentoMobileTO.getTipoAv());
					updateParticipante(preenchimentoMobileTO.getTipoAv(), preenchimentoTO.getParticipante());
				}

			})
		);
	}

	private void updatePreenchimento(PreenchimentoTO preenchimentoTO, PreenchimentoMobileTO preenchimentoMobileTO, TipoAvMobile tipoAvMobile) {
		/*Data de gravação era usada para gravar localmente no Mobile, não temos mais a função offline!*/
		/*preenchimento.setDataGravacao(preenchimentoMobile.getDataGravacao() != null ? preenchimentoMobile.getDataGravacao() : null);*/

		preenchimentoTO.setDataPreenchimento(new Date().getTime());
		preenchimentoTO.setTipoClient(preenchimentoMobileTO.getTipoClient());
		ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(preenchimentoTO.getAvaliacaoCompDet().getIdModeloGraduacao(), preenchimentoTO.getParticipante().getAvaliacao().getId());
		/*Obtém a graduacaoCompAv*/
		GraduacaoCompetenciaTO graduacaoCompAv = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), preenchimentoMobileTO.getGraduacao().getId());

		if (tipoAvMobile.equals(TipoAvMobile.AV1)) {
			preenchimentoTO.setGraduacaoAv1Id(preenchimentoMobileTO.getGraduacao().getId());
			preenchimentoTO.setObservacaoAv1(preenchimentoMobileTO.getObservacao());
			preenchimentoTO.setGraduacaoCompAv1Id(graduacaoCompAv.getId());
		}
		if (tipoAvMobile.equals(TipoAvMobile.AV2)) {
			preenchimentoTO.setGraduacaoAv2Id(preenchimentoMobileTO.getGraduacao().getId());
			preenchimentoTO.setObservacaoAv2(preenchimentoMobileTO.getObservacao());
			preenchimentoTO.setGraduacaoCompAv2Id(graduacaoCompAv.getId());
		}

		preenchimentoService.saveOrUpdate(preenchimentoTO);

	}

	private void updateParticipante(TipoAvMobile tipoAvMobile, ParticipanteTO participanteTO) {

		//long total = preenchimentoService.count(QPreenchimento.preenchimento.participante.id.eq(participanteTO.getId()));
		ParticipanteEvolucaoTO participanteEvolucao = participanteEvolucaoService.calcularEvolucao(participanteTO);
		long total = participanteEvolucao.getTotal();

		long preenchimentoAv1 = participanteEvolucao.getPreenchimentoAv1();
		long preenchimentoAv2 = participanteEvolucao.getPreenchimentoAv2();

		if (tipoAvMobile.equals(TipoAvMobile.AV1)) {
		/*	long preenchidaAv1 = preenchimentoService.count(
				QPreenchimento.preenchimento.participante.id.eq(participanteTO.getId())
					.and(QPreenchimento.preenchimento.graduacaoAv1.isNotNull()));
*/
			if (total == preenchimentoAv1) {
				participanteTO.setPreenchidaAV1(true);
				participanteTO.setDataConclusaoAv1(new Date());
			}
			if (participanteTO.getAvaliacao().getPreencheAv1() && participanteTO.getDataInicioAv1() == null) {
				participanteTO.setDataInicioAv1(new Date());
			}
		}

		if (tipoAvMobile.equals(TipoAvMobile.AV2)) {
			/*long preenchidaAv2 = preenchimentoService.count(
				QPreenchimento.preenchimento.participante.id.eq(participanteTO.getId())
					.and(QPreenchimento.preenchimento.graduacaoAv2.isNotNull()));*/

			if (participanteTO.getAvaliacao().getPreencheAv2() && participanteTO.getDataInicioAv2() == null) {
				participanteTO.setDataInicioAv2(new Date());
			}
			if (total == preenchimentoAv2) {
				participanteTO.setPreenchidaAV2(true);
				participanteTO.setDataConclusaoAv2(new Date());
			}
		}
		participanteTO.setConsensoLiberado(participanteTO.getPreenchidaAV1() && participanteTO.getPreenchidaAV2());

		participanteService.saveOrUpdate(participanteTO);
	}


}
