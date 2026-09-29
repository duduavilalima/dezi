package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.types.SituacaoPreenchimento;
import br.com.fasolti.commons.client.transfer.TransferObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DadoPreenchimentoTO implements TransferObject {
	@JsonIgnore
	private Long id;

	private Long campanhaId;
	private String campanha;

	private Long avaliacaoId;
	private String avaliacao;

	private Long departamentoId;
	private String departamento;

	private Long departamentoResponsavelId;
	private String departamentoResponsavel;

	private Long responsavelId;
	private String responsavel;

	private Long avaliadoId;
	private String avaliado;

	private String tipoCompetencia;
	private String competencia;

	private long totalAuto;
	private long preenchAuto;

	private long totalGestor;
	private long preenchGestor;

	private long totalConsenso;
	private long preenchConsenso;

	private long totalSubordinados;
	private long preenchSubordinados;

	private long totalPares;
	private long preenchPares;

	private String emailResponsavel;
	private String emailAvaliado;

	private String cargoResponsavel;

	public long getTotalAvaliacoes() {
		return totalGestor + totalSubordinados + totalPares;
	}

	public long getPreenchAvaliacoes() {
		return preenchGestor + preenchSubordinados + preenchPares;
	}

	public SituacaoPreenchimento getSituacaoAuto() {
		if (totalAuto == 0) return SituacaoPreenchimento.NAO_APLICAVEL;
		if (preenchAuto == 0) return SituacaoPreenchimento.NAO_PREENCH;
		if (preenchAuto == totalAuto) return SituacaoPreenchimento.TOTALMENTE_PREENCH;
		return SituacaoPreenchimento.PARCIALMENTE_PREENCH;
	}

	public SituacaoPreenchimento getSituacaoGestor() {
		if (totalGestor == 0) return SituacaoPreenchimento.NAO_APLICAVEL;
		if (preenchGestor == 0) return SituacaoPreenchimento.NAO_PREENCH;
		if (preenchGestor == totalGestor) return SituacaoPreenchimento.TOTALMENTE_PREENCH;
		return SituacaoPreenchimento.PARCIALMENTE_PREENCH;
	}

	public SituacaoPreenchimento getSituacaoConsenso() {
		if (totalConsenso == 0) return SituacaoPreenchimento.NAO_APLICAVEL;
		if (preenchConsenso == 0) return SituacaoPreenchimento.NAO_PREENCH;
		if (preenchConsenso == totalConsenso) return SituacaoPreenchimento.TOTALMENTE_PREENCH;
		return SituacaoPreenchimento.PARCIALMENTE_PREENCH;
	}

	public SituacaoPreenchimento getSituacaoSubordinados() {
		if (totalSubordinados == 0) return SituacaoPreenchimento.NAO_APLICAVEL;
		if (preenchSubordinados == 0) return SituacaoPreenchimento.NAO_PREENCH;
		if (preenchSubordinados == totalSubordinados) return SituacaoPreenchimento.TOTALMENTE_PREENCH;
		return SituacaoPreenchimento.PARCIALMENTE_PREENCH;
	}

	public SituacaoPreenchimento getSituacaoPares() {
		if (totalPares == 0) return SituacaoPreenchimento.NAO_APLICAVEL;
		if (preenchPares == 0) return SituacaoPreenchimento.NAO_PREENCH;
		if (preenchPares == totalPares) return SituacaoPreenchimento.TOTALMENTE_PREENCH;
		return SituacaoPreenchimento.PARCIALMENTE_PREENCH;
	}

	public SituacaoPreenchimento getSituacaoAvaliacoes() {
		if (getTotalAvaliacoes() == 0) return SituacaoPreenchimento.NAO_APLICAVEL;
		if (getPreenchAvaliacoes() == 0) return SituacaoPreenchimento.NAO_PREENCH;
		if (getPreenchAvaliacoes() == getTotalAvaliacoes()) return SituacaoPreenchimento.TOTALMENTE_PREENCH;
		return SituacaoPreenchimento.PARCIALMENTE_PREENCH;
	}

	@Override
	public String toString() {
		return "DadoPreenchimentoTO{" +
				"id=" + id +
				", campanha='" + campanha + '\'' +
				", avaliacao='" + avaliacao + '\'' +
				", responsavel='" + responsavel + '\'' +
				", avaliado='" + avaliado + '\'' +
				", tipoCompetencia='" + tipoCompetencia + '\'' +
				", competencia='" + competencia + '\'' +
				'}';
	}
}
