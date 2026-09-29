package br.com.fasolti.avd.transfer;


import br.com.fasolti.avd.transfer.util.BaseTO;

public class DetalhamentoParticipanteTO extends BaseTO {

	private Long idCompetencia;
	private String descricaoAbreviada;
	private Long idTipoCompetencia;
	private String nomeCurto;
	private Long idDetalhamento;
	private String descricaoDetalhamento;
	private Long idAvaliacao;
	private String descricaoAvaliacao;
	private Long idPerfilAgente;
	private Long idCargo;
	private String nomeCargo;
	private Long idDetCargoComp;


	public Long getIdCompetencia() {
		return idCompetencia;
	}

	public void setIdCompetencia(Long idCompetencia) {
		this.idCompetencia = idCompetencia;
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public Long getIdTipoCompetencia() {
		return idTipoCompetencia;
	}

	public void setIdTipoCompetencia(Long idTipoCompetencia) {
		this.idTipoCompetencia = idTipoCompetencia;
	}

	public String getNomeCurto() {
		return nomeCurto;
	}

	public void setNomeCurto(String nomeCurto) {
		this.nomeCurto = nomeCurto;
	}

	public Long getIdDetalhamento() {
		return idDetalhamento;
	}

	public void setIdDetalhamento(Long idDetalhamento) {
		this.idDetalhamento = idDetalhamento;
	}

	public String getDescricaoDetalhamento() {
		return descricaoDetalhamento;
	}

	public void setDescricaoDetalhamento(String descricaoDetalhamento) {
		this.descricaoDetalhamento = descricaoDetalhamento;
	}

	public Long getIdAvaliacao() {
		return idAvaliacao;
	}

	public void setIdAvaliacao(Long idAvaliacao) {
		this.idAvaliacao = idAvaliacao;
	}

	public String getDescricaoAvaliacao() {
		return descricaoAvaliacao;
	}

	public void setDescricaoAvaliacao(String descricaoAvaliacao) {
		this.descricaoAvaliacao = descricaoAvaliacao;
	}

	public Long getIdPerfilAgente() {
		return idPerfilAgente;
	}

	public void setIdPerfilAgente(Long idPerfilAgente) {
		this.idPerfilAgente = idPerfilAgente;
	}

	public Long getIdCargo() {
		return idCargo;
	}

	public void setIdCargo(Long idCargo) {
		this.idCargo = idCargo;
	}

	public String getNomeCargo() {
		return nomeCargo;
	}

	public void setNomeCargo(String nomeCargo) {
		this.nomeCargo = nomeCargo;
	}

	public Long getIdDetCargoComp() {
		return idDetCargoComp;
	}

	public void setIdDetCargoComp(Long idDetCargoComp) {
		this.idDetCargoComp = idDetCargoComp;
	}
}
