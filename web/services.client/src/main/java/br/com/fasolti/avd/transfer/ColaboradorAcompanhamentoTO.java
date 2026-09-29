package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.types.SituacaoPreenchimento;

import java.util.List;

public class ColaboradorAcompanhamentoTO {

	private FuncionarioSimpleTO colaborador;
	private SituacaoPreenchimento situacao;
	private Boolean possuiAutoavaliacao;
	private Double percentualAutoavaliacao;
	private Double percentualAvaliacao;
	private Double totalPreenchimentos;
	private List<AvaliadoPreenchimentoTO> avaliadoConsensoList;

	public ColaboradorAcompanhamentoTO() {
	}

	public FuncionarioSimpleTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(FuncionarioSimpleTO colaborador) {
		this.colaborador = colaborador;
	}

	public Double getPercentualAutoavaliacao() {
		return percentualAutoavaliacao;
	}

	public void setPercentualAutoavaliacao(Double percentualAutoavaliacao) {
		this.percentualAutoavaliacao = percentualAutoavaliacao;
	}

	public Boolean getPossuiAutoavaliacao() {
		return possuiAutoavaliacao;
	}

	public void setPossuiAutoavaliacao(Boolean possuiAutoavaliacao) {
		this.possuiAutoavaliacao = possuiAutoavaliacao;
	}

	public Double getPercentualAvaliacao() {
		return percentualAvaliacao;
	}

	public void setPercentualAvaliacao(Double percentualAvaliacao) {
		this.percentualAvaliacao = percentualAvaliacao;
	}

	public Double getTotalPreenchimentos() {
		return totalPreenchimentos;
	}

	public void setTotalPreenchimentos(Double totalPreenchimentos) {
		this.totalPreenchimentos = totalPreenchimentos;
	}

	public SituacaoPreenchimento getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPreenchimento situacao) {
		this.situacao = situacao;
	}

	public List<AvaliadoPreenchimentoTO> getAvaliadoConsensoList() {
		return avaliadoConsensoList;
	}

	public void setAvaliadoConsensoList(List<AvaliadoPreenchimentoTO> avaliadoConsensoList) {
		this.avaliadoConsensoList = avaliadoConsensoList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ColaboradorAcompanhamentoTO that = (ColaboradorAcompanhamentoTO) o;

		return colaborador.getId().equals(that.colaborador.getId());
	}

	@Override
	public int hashCode() {
		return colaborador.getId().hashCode();
	}
}
