package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Preenchimento;
import br.com.fasolti.avd.domain.QPreenchimento;
import br.com.fasolti.avd.repository.PreenchimentoRepository;
import br.com.fasolti.avd.service.ParticipanteEvolucaoService;
import br.com.fasolti.avd.transfer.ParticipanteEvolucaoTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.PreenchimentoTO;
import br.com.fasolti.commons.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipanteEvolucaoServiceImpl
	extends BaseService<Preenchimento, PreenchimentoTO>
	implements ParticipanteEvolucaoService
{
	private static QPreenchimento preenchimento = QPreenchimento.preenchimento;

	@Autowired
	public ParticipanteEvolucaoServiceImpl(PreenchimentoRepository repository) {
		super(repository);
	}

	@Override
	public ParticipanteEvolucaoTO calcularEvolucao(ParticipanteTO participante) {
		ParticipanteEvolucaoTO participanteEvolucao = new ParticipanteEvolucaoTO();

		List<Preenchimento> preenchimentos = select().where(preenchimento.participante.id.eq(participante.getId())).fetch();

		participanteEvolucao.setTotal((long) preenchimentos.size());

		participanteEvolucao.setPreenchimentoAv1(preenchimentos.stream().filter(p -> p.getGraduacaoAv1() != null).count());
		participanteEvolucao.setPreenchimentoAv2(preenchimentos.stream().filter(p -> p.getGraduacaoAv2() != null).count());
		participanteEvolucao.setPreenchimentoAv3(preenchimentos.stream().filter(p -> p.getGraduacaoAv3() != null).count());

		return participanteEvolucao;
	}
}
