package br.com.fasolti.avd.transfer.search;

import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.simple.*;
import br.com.fasolti.avd.types.SituacaoPDI;

import java.util.Date;

public class PDIAcaoSearchTO {

	private FuncionarioSimpleTO avaliado;

	private AvaliacaoSimpleTO avaliacao;

	private CampanhaSimpleTO campanha;

	private CargoSimpleTO cargo;

	private DepartamentoSimpleTO departamento;

	private CompetenciaSimpleTO competencia;

	private SituacaoPDI situacao;

	private Date dataInicio;

	private Date dataFim;

	public FuncionarioSimpleTO getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(FuncionarioSimpleTO avaliado) {
		this.avaliado = avaliado;
	}

	public AvaliacaoSimpleTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoSimpleTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public CampanhaSimpleTO getCampanha() {
		return campanha;
	}

	public void setCampanha(CampanhaSimpleTO campanha) {
		this.campanha = campanha;
	}

	public CargoSimpleTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoSimpleTO cargo) {
		this.cargo = cargo;
	}

	public DepartamentoSimpleTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoSimpleTO departamento) {
		this.departamento = departamento;
	}

	public CompetenciaSimpleTO getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaSimpleTO competencia) {
		this.competencia = competencia;
	}

	public SituacaoPDI getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPDI situacao) {
		this.situacao = situacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
}
