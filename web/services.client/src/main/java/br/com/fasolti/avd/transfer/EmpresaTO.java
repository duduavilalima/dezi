package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class EmpresaTO extends BaseAuditedTO implements SearchObject {


	@Validation(name = "Foto nome do arquivo", max = 500)
	private String fotoNomeArquivo;

	@Validation(name = "Nome", max = 120, notNull = true)
	private String nome;

	private String host;

	private Integer tamanhoNomeArquivo;

	@Validation(name = "Contratante", notNull = true)
	private ContratanteFasoltiTO contratante;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	private Boolean feedforward;

	private Boolean cbo;

	private String labelGestores;

	private String labelSubordinados;

	private String labelPares;

	private String identificador;

	private Integer versaoBi;

	public String getFotoNomeArquivo() {
		return fotoNomeArquivo;
	}

	public void setFotoNomeArquivo(String fotoNomeArquivo) {
		this.fotoNomeArquivo = fotoNomeArquivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getTamanhoNomeArquivo() {
		return tamanhoNomeArquivo;
	}

	public void setTamanhoNomeArquivo(Integer tamanhoNomeArquivo) {
		this.tamanhoNomeArquivo = tamanhoNomeArquivo;
	}

	public ContratanteFasoltiTO getContratante() {
		return contratante;
	}

	public void setContratante(ContratanteFasoltiTO contratante) {
		this.contratante = contratante;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public Boolean getFeedforward() {
		return feedforward;
	}

	public void setFeedforward(Boolean feedforward) {
		this.feedforward = feedforward;
	}

	public Boolean getCbo() {
		return cbo;
	}

	public void setCbo(Boolean cbo) {
		this.cbo = cbo;
	}

	public String getLabelGestores() {
		return labelGestores;
	}

	public void setLabelGestores(String labelGestores) {
		this.labelGestores = labelGestores;
	}

	public String getLabelSubordinados() {
		return labelSubordinados;
	}

	public void setLabelSubordinados(String labelSubordinados) {
		this.labelSubordinados = labelSubordinados;
	}

	public String getLabelPares() {
		return labelPares;
	}

	public void setLabelPares(String labelPares) {
		this.labelPares = labelPares;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Integer getVersaoBi() {
		return versaoBi;
	}

	public void setVersaoBi(Integer versaoBi) {
		this.versaoBi = versaoBi;
	}
}
