package br.com.fasolti.avd.transfer;

public class CompetenciaAvaliacao {

	private Long idCompetencia;
	private String descricaoCompetencia;
	private String tipoCompetencia;
	private Long idAvaliacao;
	private String descricaoAvaliacao;
	private Long idAvaliador;
	private Long idRevisor;
	private Long idPerfil;
	private Integer qtdAvaliados;
	private Integer qtdRespostas;
	private Integer qtdPerguntas;
	private Boolean todosConsensosLiberados;

	public Long getIdCompetencia() {
		return idCompetencia;
	}

	public void setIdCompetencia(Long idCompetencia) {
		this.idCompetencia = idCompetencia;
	}

	public String getDescricaoCompetencia() {
		return descricaoCompetencia;
	}

	public void setDescricaoCompetencia(String descricaoCompetencia) {
		this.descricaoCompetencia = descricaoCompetencia;
	}

	public String getTipoCompetencia() {
		return tipoCompetencia;
	}

	public void setTipoCompetencia(String tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
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

	public Long getIdAvaliador() {
		return idAvaliador;
	}

	public void setIdAvaliador(Long idAvaliador) {
		this.idAvaliador = idAvaliador;
	}

	public Long getIdRevisor() {
		return idRevisor;
	}

	public void setIdRevisor(Long idRevisor) {
		this.idRevisor = idRevisor;
	}

	public Long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public Integer getQtdAvaliados() {
		return qtdAvaliados;
	}

	public void setQtdAvaliados(Integer qtdAvaliados) {
		this.qtdAvaliados = qtdAvaliados;
	}

	public Integer getQtdRespostas() {
		return qtdRespostas;
	}

	public void setQtdRespostas(Integer qtdRespostas) {
		this.qtdRespostas = qtdRespostas;
	}

	public Integer getQtdPerguntas() {
		return qtdPerguntas;
	}

	public void setQtdPerguntas(Integer qtdPerguntas) {
		this.qtdPerguntas = qtdPerguntas;
	}

	public Boolean getTodosConsensosLiberados() {
		return todosConsensosLiberados;
	}

	public void setTodosConsensosLiberados(Boolean todosConsensosLiberados) {
		this.todosConsensosLiberados = todosConsensosLiberados;
	}

	@Override
	public String toString() {
		return "CompetenciaAvaliacao{" +
			"idCompetencia=" + idCompetencia +
			", descricaoCompetencia='" + descricaoCompetencia + '\'' +
			", tipoCompetencia='" + tipoCompetencia + '\'' +
			", idAvaliacao=" + idAvaliacao +
			", descricaoAvaliacao='" + descricaoAvaliacao + '\'' +
			", qtdAvaliados=" + qtdAvaliados +
			", qtdRespostas=" + qtdRespostas +
			", qtdPerguntas=" + qtdPerguntas +
			'}';
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CompetenciaAvaliacao that = (CompetenciaAvaliacao) o;

		if (!getIdCompetencia().equals(that.getIdCompetencia())) return false;
		if (!getDescricaoCompetencia().equals(that.getDescricaoCompetencia())) return false;
		return getIdAvaliacao().equals(that.getIdAvaliacao());
	}

	@Override
	public int hashCode() {
		int result = getIdCompetencia().hashCode();
		result = 31 * result + getDescricaoCompetencia().hashCode();
		result = 31 * result + getIdAvaliacao().hashCode();
		return result;
	}
}
