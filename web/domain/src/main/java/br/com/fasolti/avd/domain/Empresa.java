package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
public class Empresa extends BaseAuditedEntity {

	private static final long serialVersionUID = 8400087656249769084L;

	@Column(name="foto_nome_arquivo", length = 500)
	private String fotoNomeArquivo;

	@Column(name = "nome", length = 120, nullable = false)
	private String nome;

	@Column(name="tamanho_nome_arquivo")
	private Integer tamanhoNomeArquivo;

	@ManyToOne
	@JoinColumn(name="id_contratante_fasolti")
	private ContratanteFasolti contratante;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	@Column(name="feedforward", nullable = false)
	private Boolean feedforward;

	@Column(name="cbo", nullable = false)
	private Boolean cbo;

	@Column(name="label_gestores", nullable = false)
	private String labelGestores;

	@Column(name="label_subordinados", nullable = false)
	private String labelSubordinados;

	@Column(name="label_pares", nullable = false)
	private String labelPares;

	@Column(name = "host", length = 255, nullable = true)
	private String host;

	@Column(name = "identificador", length = 50)
	private String identificador;

	@Column(name = "versao_bi")
	private Integer versaoBi;

	public Empresa() {
	}

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

	public Integer getTamanhoNomeArquivo() {
		return tamanhoNomeArquivo;
	}

	public void setTamanhoNomeArquivo(Integer tamanhoNomeArquivo) {
		this.tamanhoNomeArquivo = tamanhoNomeArquivo;
	}

	public ContratanteFasolti getContratante() {
		return contratante;
	}

	public void setContratante(ContratanteFasolti contratante) {
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	@Override
	public String toString() {
		return "Empresa [getId()=" + getId() + "]";
	}

}
