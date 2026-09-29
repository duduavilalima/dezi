package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.DadoPreenchimento;
import br.com.fasolti.avd.domain.QDadoPreenchimento;
import br.com.fasolti.avd.repository.DadoPreenchimentoRepository;
import br.com.fasolti.avd.service.DadoPreenchimentoService;
import br.com.fasolti.avd.service.DepartamentoService;
import br.com.fasolti.avd.transfer.DadoPreenchimentoTO;
import br.com.fasolti.avd.transfer.DadoPreenchimentoTotaisTO;
import br.com.fasolti.avd.transfer.DepartamentoTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.types.SituacaoPreenchimento;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.services.BaseService;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static br.com.fasolti.avd.types.SituacaoPreenchimento.*;

@Service
public class DadoPreenchimentoServiceImpl
		extends BaseService<DadoPreenchimento, DadoPreenchimentoTO>
		implements DadoPreenchimentoService {

	@Autowired
	public DadoPreenchimentoServiceImpl(DadoPreenchimentoRepository repository) {
		super(repository);
	}

	@Autowired
	public DepartamentoService departamentoService;

	private final QDadoPreenchimento qDadoPreenchimento = QDadoPreenchimento.dadoPreenchimento;

	@Override
	public DadoPreenchimentoTotaisTO totalizar(
			final Long campanhaId, final Long avaliacaoId,
			final Long departamentoId, final Long responsavelId) {

		List<DadoPreenchimentoTO> dadosGerais = listar(campanhaId, avaliacaoId, departamentoId, responsavelId);
		List<DadoPreenchimentoTO> dadosPorResponsavel = totalizarPorResponsavel(dadosGerais);

		DadoPreenchimentoTotaisTO totais = new DadoPreenchimentoTotaisTO();

		totais.setPreenchidasAuto(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoAuto, TOTALMENTE_PREENCH));
		totais.setParciaisAuto(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoAuto, PARCIALMENTE_PREENCH));
		totais.setNaoPreenchidasAuto(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoAuto, NAO_PREENCH));

		totais.setPreenchidasGestor(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoGestor, TOTALMENTE_PREENCH));
		totais.setParciaisGestor(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoGestor, PARCIALMENTE_PREENCH));
		totais.setNaoPreenchidasGestor(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoGestor, NAO_PREENCH));

		totais.setPreenchidasConsenso(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoConsenso, TOTALMENTE_PREENCH));
		totais.setParciaisConsenso(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoConsenso, PARCIALMENTE_PREENCH));
		totais.setNaoPreenchidasConsenso(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoConsenso, NAO_PREENCH));

		totais.setPreenchidasSubordinados(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoSubordinados, TOTALMENTE_PREENCH));
		totais.setParciaisSubordinados(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoSubordinados, PARCIALMENTE_PREENCH));
		totais.setNaoPreenchidasSubordinados(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoSubordinados, NAO_PREENCH));

		totais.setPreenchidasPares(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoPares, TOTALMENTE_PREENCH));
		totais.setParciaisPares(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoPares, PARCIALMENTE_PREENCH));
		totais.setNaoPreenchidasPares(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoPares, NAO_PREENCH));

		totais.setPreenchidasAvaliacoes(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoAvaliacoes, TOTALMENTE_PREENCH));
		totais.setParciaisAvaliacoes(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoAvaliacoes, PARCIALMENTE_PREENCH));
		totais.setNaoPreenchidasAvaliacoes(count(dadosPorResponsavel, DadoPreenchimentoTO::getSituacaoAvaliacoes, NAO_PREENCH));

		return totais;
	}

	@Override
	public List<DadoPreenchimentoTO> listarResponsaveis(Long campanhaId, Long avaliacaoId, Long departamentoId) {
		if (departamentoId != null && departamentoId.equals(0L))
			departamentoId = null;

		List<DadoPreenchimentoTO> dadosPorAvaliacao = listar(campanhaId, avaliacaoId, departamentoId, null);
		return totalizarPorResponsavel(dadosPorAvaliacao);
	}

	@Override
	public List<DadoPreenchimentoTO> listarPendencias(Long campanhaId, Long avaliacaoId, Long departamentoId) {
		if (departamentoId != null && departamentoId.equals(0L))
			departamentoId = null;

		List<DadoPreenchimentoTO> dadosPorAvaliacao = listar(campanhaId, avaliacaoId, departamentoId, null);

		Predicate<DadoPreenchimentoTO> temPendencia =
			d -> d.getSituacaoAuto() != TOTALMENTE_PREENCH
				|| d.getSituacaoAvaliacoes() != TOTALMENTE_PREENCH
				|| d.getSituacaoConsenso() != TOTALMENTE_PREENCH;

		return dadosPorAvaliacao.stream()
			.filter(temPendencia)
			.collect(Collectors.toList());
	}

	private List<DadoPreenchimentoTO> listar(Long campanhaId, Long avaliacaoId, Long departamentoId, Long responsavelId) {

		JPAQuery<DadoPreenchimento> query = select()
				.where(
					qDadoPreenchimento.campanhaId.eq(campanhaId),
					qDadoPreenchimento.avaliacaoId.eq(avaliacaoId));

		// Responsavel é opcional. Para o prefil GESTOR_AREA o filtro por departamento é obrigatório.
		if (responsavelId != null)
			query.where(qDadoPreenchimento.responsavelId.eq(responsavelId));

		UserTO user = departamentoService.getLoggedUser();

		if (TipoPerfil.GESTOR_AREA.equals(user.getTipoPerfil())) {
			if (departamentoId != null)
				query.where(qDadoPreenchimento.departamentoId.eq(departamentoId));
			else {
				List<Long> departamentos = departamentoService.obterDepartamentosUsuarioLogado().stream()
						.map(DepartamentoTO::getId).collect(Collectors.toList());
				query.where(qDadoPreenchimento.departamentoId.in(departamentos));
			}
		}
		else if (departamentoId != null)
			query.where(qDadoPreenchimento.departamentoId.eq(departamentoId));

		return map(query.fetch());
	}

	private List<DadoPreenchimentoTO> totalizarPorResponsavel(List<DadoPreenchimentoTO> dados) {
		Map<AvaliacaoParticipantes, List<DadoPreenchimentoTO>> porResponsavelMap = dados.stream()
			.collect(Collectors.groupingBy(this::createParticipantes));

		return
		 porResponsavelMap.values().stream()
			.map(d -> d.stream().reduce(this::somaDados).orElse(null))
			.collect(Collectors.toList());
	}

	private long count(
		List<DadoPreenchimentoTO> dados, Function<DadoPreenchimentoTO, SituacaoPreenchimento> supplier,
		SituacaoPreenchimento situacao) {

		return dados.stream()
			.map(supplier)
			.filter(s -> s == situacao)
			.count();
	}

	private DadoPreenchimentoTO somaDados(final DadoPreenchimentoTO dado1, final DadoPreenchimentoTO dado2) {
		DadoPreenchimentoTO result = new DadoPreenchimentoTO();

		// Para os dados comuns usa o primeiro
		result.setId(dado1.getId());
		result.setCampanha(dado1.getCampanha());
		result.setCampanhaId(dado1.getCampanhaId());
		result.setAvaliacao(dado1.getAvaliacao());
		result.setAvaliacaoId(dado1.getAvaliacaoId());
		result.setDepartamento(dado1.getDepartamento());
		result.setDepartamentoId(dado1.getDepartamentoId());
		result.setResponsavelId(dado1.getResponsavelId());
		result.setResponsavel(dado1.getResponsavel());
		result.setAvaliadoId(dado1.getAvaliadoId());
		result.setAvaliado(dado1.getAvaliado());
		result.setEmailAvaliado(dado1.getEmailAvaliado());
		result.setEmailResponsavel(dado1.getEmailResponsavel());
		result.setCargoResponsavel(dado1.getCargoResponsavel());
		result.setDepartamentoResponsavelId(dado1.getDepartamentoResponsavelId());
		result.setDepartamentoResponsavel(dado1.getDepartamentoResponsavel());

		return somaObjetos(dado1, dado2, result);
	}

	private <T> T somaObjetos(final T obj1, final T obj2, final T result) {
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(obj1.getClass()).getPropertyDescriptors()) {
				if (pd.getReadMethod() != null && pd.getWriteMethod() != null
						&& pd.getPropertyType() == long.class) {
					long value1 = (long) pd.getReadMethod().invoke(obj1);
					long value2 = (long) pd.getReadMethod().invoke(obj2);

					pd.getWriteMethod().invoke(result, value1 + value2);
				}
			}
		} catch (Exception e) {
			return null;
		}

		return result;
	}

	private static class AvaliacaoParticipantes {
		public final Long responsavelId;
		public final Long avaliadoId;

		public AvaliacaoParticipantes(Long responsavelId, Long avaliadoId) {
			this.responsavelId = responsavelId;
			this.avaliadoId = avaliadoId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			AvaliacaoParticipantes that = (AvaliacaoParticipantes) o;
			return responsavelId.equals(that.responsavelId)
					&& avaliadoId.equals(that.avaliadoId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(responsavelId, avaliadoId);
		}

		@Override
		public String toString() {
			return "AvaliacaoParticipantes{" +
					"responsavelId=" + responsavelId +
					", avaliadoId=" + avaliadoId +
					'}';
		}
	}

	private AvaliacaoParticipantes createParticipantes(DadoPreenchimentoTO dadoPreenchimento) {
		return new AvaliacaoParticipantes(dadoPreenchimento.getResponsavelId(), dadoPreenchimento.getAvaliadoId());
	}
}
