package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.AvaliacaoSimpleTO;
import br.com.fasolti.avd.transfer.simple.CampanhaSimpleTO;
import br.com.fasolti.avd.transfer.simple.DepartamentoSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.types.SituacaoPreenchimento;

public class FiltroAcompanhamentoTO {

	private CampanhaSimpleTO campanha;
	private AvaliacaoSimpleTO avaliacao;
	private DepartamentoSimpleTO departamento;
	private FuncionarioSimpleTO colaborador;
	private SituacaoPreenchimento situacao;

	public FiltroAcompanhamentoTO() {
	}

	public CampanhaSimpleTO getCampanha() {
		return campanha;
	}

	public void setCampanha(CampanhaSimpleTO campanha) {
		this.campanha = campanha;
	}

	public AvaliacaoSimpleTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoSimpleTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public DepartamentoSimpleTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoSimpleTO departamento) {
		this.departamento = departamento;
	}

	public FuncionarioSimpleTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(FuncionarioSimpleTO colaborador) {
		this.colaborador = colaborador;
	}

	public SituacaoPreenchimento getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPreenchimento situacao) {
		this.situacao = situacao;
	}
}
