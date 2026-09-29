package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.TipoClient;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class PreenchimentoTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Detalhamento", notNull = true)
	private AvaliacaoCompDetTO avaliacaoCompDet;

	@Validation(name = "Participante", notNull = true)
	private ParticipanteTO participante;

	private Long graduacaoAv1Id;

	private Long graduacaoAv2Id;

	private Long graduacaoAv3Id;

	private Boolean concordanciaRevisor;

	private String justificativaRevisor;

	private String observacaoAv1;

	private String observacaoAv2;

	private String observacaoAv3;

	private Long graduacaoCompAv1Id;

	private Long graduacaoCompAv2Id;

	private Long graduacaoCompAv3Id;

	private Long dataPreenchimento;

	private Long dataGravacao;

	private TipoClient tipoClient;

	public PreenchimentoTO(){
	}

	public AvaliacaoCompDetTO getAvaliacaoCompDet() {
		return avaliacaoCompDet;
	}

	public void setAvaliacaoCompDet(AvaliacaoCompDetTO avaliacaoCompDet) {
		this.avaliacaoCompDet = avaliacaoCompDet;
	}

	public ParticipanteTO getParticipante() {
		return participante;
	}

	public void setParticipante(ParticipanteTO participante) {
		this.participante = participante;
	}

	public Long getGraduacaoAv1Id() {
		return graduacaoAv1Id;
	}

	public void setGraduacaoAv1Id(Long graduacaoAv1Id) {
		this.graduacaoAv1Id = graduacaoAv1Id;
	}

	public Long getGraduacaoAv2Id() {
		return graduacaoAv2Id;
	}

	public void setGraduacaoAv2Id(Long graduacaoAv2Id) {
		this.graduacaoAv2Id = graduacaoAv2Id;
	}

	public Long getGraduacaoAv3Id() {
		return graduacaoAv3Id;
	}

	public void setGraduacaoAv3Id(Long graduacaoAv3Id) {
		this.graduacaoAv3Id = graduacaoAv3Id;
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

	public Long getDataPreenchimento() {
		return dataPreenchimento;
	}

	public void setDataPreenchimento(Long dataPreenchimento) {
		this.dataPreenchimento = dataPreenchimento;
	}

	public Long getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(Long dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public TipoClient getTipoClient() {
		return tipoClient;
	}

	public void setTipoClient(TipoClient tipoClient) {
		this.tipoClient = tipoClient;
	}
}
