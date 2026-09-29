package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.TipoClient;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "avaliacao_preenchimento")
public class Preenchimento extends BaseAuditedEntity {

	@ManyToOne
	@JoinColumn(name = "id_avaliacao_comp_det")
	private AvaliacaoCompDet avaliacaoCompDet;

	@ManyToOne
	@JoinColumn(name = "id_participante")
	private Participante participante;

	@ManyToOne
	@JoinColumn(name = "id_graduacao_av1")
	private Graduacao graduacaoAv1;

	@ManyToOne
	@JoinColumn(name = "id_graduacao_av2")
	private Graduacao graduacaoAv2;

	@ManyToOne
	@JoinColumn(name = "id_graduacao_av3")
	private Graduacao graduacaoAv3;

	@Column(name="concordancia_revisor")
	private Boolean concordanciaRevisor;

	@Column(name="justificativa_revisor")
	private String justificativaRevisor;

	@Column(name="observacao_av1")
	private String observacaoAv1;

	@Column(name="observacao_av2")
	private String observacaoAv2;

	@Column(name="observacao_av3")
	private String observacaoAv3;

	@Column(name = "id_graduacao_comp_av1")
	private Long graduacaoCompAv1Id;

	@Column(name = "id_graduacao_comp_av2")
	private Long graduacaoCompAv2Id;

	@Column(name = "id_graduacao_comp_av3")
	private Long graduacaoCompAv3Id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_preenchimento")
	private Date dataPreenchimento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_gravacao")
	private Date dataGravacao;

	@Column(name = "id_tipo_client")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoClient tipoClient;

	public Preenchimento() {
	}

	public AvaliacaoCompDet getAvaliacaoCompDet() {
		return avaliacaoCompDet;
	}

	public void setAvaliacaoCompDet(AvaliacaoCompDet avaliacaoCompDet) {
		this.avaliacaoCompDet = avaliacaoCompDet;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Graduacao getGraduacaoAv1() {
		return graduacaoAv1;
	}

	public void setGraduacaoAv1(Graduacao graduacaoAv1) {
		this.graduacaoAv1 = graduacaoAv1;
	}

	public Graduacao getGraduacaoAv2() {
		return graduacaoAv2;
	}

	public void setGraduacaoAv2(Graduacao graduacaoAv2) {
		this.graduacaoAv2 = graduacaoAv2;
	}

	public Graduacao getGraduacaoAv3() {
		return graduacaoAv3;
	}

	public void setGraduacaoAv3(Graduacao graduacaoAv3) {
		this.graduacaoAv3 = graduacaoAv3;
	}

	public Boolean getConcordanciaRevisor() {
		return concordanciaRevisor;
	}

	public void setConcordanciaRevisor(Boolean concordanciaRevisor) {
		this.concordanciaRevisor = concordanciaRevisor;
	}

	public String getJustificativaRevisor() {
		return justificativaRevisor;
	}

	public void setJustificativaRevisor(String justificativaRevisor) {
		this.justificativaRevisor = justificativaRevisor;
	}

	public String getObservacaoAv1() {
		return observacaoAv1;
	}

	public void setObservacaoAv1(String observacaoAv1) {
		this.observacaoAv1 = observacaoAv1;
	}

	public String getObservacaoAv2() {
		return observacaoAv2;
	}

	public void setObservacaoAv2(String observacaoAv2) {
		this.observacaoAv2 = observacaoAv2;
	}

	public String getObservacaoAv3() {
		return observacaoAv3;
	}

	public void setObservacaoAv3(String observacaoAv3) {
		this.observacaoAv3 = observacaoAv3;
	}

	public Long getGraduacaoCompAv1Id() {
		return graduacaoCompAv1Id;
	}

	public void setGraduacaoCompAv1Id(Long graduacaoCompAv1Id) {
		this.graduacaoCompAv1Id = graduacaoCompAv1Id;
	}

	public Long getGraduacaoCompAv2Id() {
		return graduacaoCompAv2Id;
	}

	public void setGraduacaoCompAv2Id(Long graduacaoCompAv2Id) {
		this.graduacaoCompAv2Id = graduacaoCompAv2Id;
	}

	public Long getGraduacaoCompAv3Id() {
		return graduacaoCompAv3Id;
	}

	public void setGraduacaoCompAv3Id(Long graduacaoCompAv3Id) {
		this.graduacaoCompAv3Id = graduacaoCompAv3Id;
	}

	public Date getDataPreenchimento() {
		return dataPreenchimento;
	}

	public void setDataPreenchimento(Date dataPreenchimento) {
		this.dataPreenchimento = dataPreenchimento;
	}

	public Date getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(Date dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public TipoClient getTipoClient() {
		return tipoClient;
	}

	public void setTipoClient(TipoClient tipoClient) {
		this.tipoClient = tipoClient;
	}
}
