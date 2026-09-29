package br.com.fasolti.avd.transfer;

public class RelatorioAvaliacaoTO {
	private String funcionario;
	private String cargoFuncionario;
	private String dataCargo;
	private String nomeAvaliador;
	private String numeroAvaliacao;
	private String descricaoAvaliacao;
	private String cargoAvaliador;
	private String A1;
	private String A2;
	private String A3;
	private Long idCompetencia;
	private Integer ordemDetalhamento;
	private String tipoCompetencia;
	private String nomeCompetencia;
	private String descricaoDetalhamento;

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

	public String getCargoFuncionario() {
		return cargoFuncionario;
	}

	public void setCargoFuncionario(String cargoFuncionario) {
		this.cargoFuncionario = cargoFuncionario;
	}

	public String getDataCargo() {
		return dataCargo;
	}

	public void setDataCargo(String dataCargo) {
		this.dataCargo = dataCargo;
	}

	public String getNomeAvaliador() {
		return nomeAvaliador;
	}

	public void setNomeAvaliador(String nomeAvaliador) {
		this.nomeAvaliador = nomeAvaliador;
	}

	public String getNumeroAvaliacao() {
		return numeroAvaliacao;
	}

	public void setNumeroAvaliacao(String numeroAvaliacao) {
		this.numeroAvaliacao = numeroAvaliacao;
	}

	public String getDescricaoAvaliacao() {
		return descricaoAvaliacao;
	}

	public void setDescricaoAvaliacao(String descricaoAvaliacao) {
		this.descricaoAvaliacao = descricaoAvaliacao;
	}

	public String getCargoAvaliador() {
		return cargoAvaliador;
	}

	public void setCargoAvaliador(String cargoAvaliador) {
		this.cargoAvaliador = cargoAvaliador;
	}

	public String getA1() {
		return A1;
	}

	public void setA1(String a1) {
		A1 = a1;
	}

	public String getA2() {
		return A2;
	}

	public void setA2(String a2) {
		A2 = a2;
	}

	public String getA3() {
		return A3;
	}

	public void setA3(String a3) {
		A3 = a3;
	}

	public String getTipoCompetencia() {
		return tipoCompetencia;
	}

	public void setTipoCompetencia(String tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
	}

	public String getNomeCompetencia() {
		return nomeCompetencia;
	}

	public void setNomeCompetencia(String nomeCompetencia) {
		this.nomeCompetencia = nomeCompetencia;
	}

	public String getDescricaoDetalhamento() {
		return descricaoDetalhamento;
	}

	public void setDescricaoDetalhamento(String descricaoDetalhamento) {
		this.descricaoDetalhamento = descricaoDetalhamento;
	}

	public Long getIdCompetencia() {
		return idCompetencia;
	}

	public void setIdCompetencia(Long idCompetencia) {
		this.idCompetencia = idCompetencia;
	}

	public Integer getOrdemDetalhamento() {
		return ordemDetalhamento;
	}

	public void setOrdemDetalhamento(Integer ordemDetalhamento) {
		this.ordemDetalhamento = ordemDetalhamento;
	}
}
