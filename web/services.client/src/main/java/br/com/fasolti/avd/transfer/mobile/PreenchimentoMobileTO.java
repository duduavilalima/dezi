package br.com.fasolti.avd.transfer.mobile;

import br.com.fasolti.avd.types.TipoAvMobile;
import br.com.fasolti.avd.types.TipoClient;
import br.com.fasolti.commons.client.transfer.TransferObject;

public class PreenchimentoMobileTO implements TransferObject {
	private Long id;
	private GraduacaoMobileTO graduacao;
	private ParticipanteMobileTO participante;
	private TipoAvMobile tipoAv;
	private String observacao;
	private Long graduacaoCompAv1Id;
	private Long graduacaoCompAv2Id;
	private TipoClient tipoClient;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GraduacaoMobileTO getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(GraduacaoMobileTO graduacao) {
		this.graduacao = graduacao;
	}

	public ParticipanteMobileTO getParticipante() {
		return participante;
	}

	public void setParticipante(ParticipanteMobileTO participante) {
		this.participante = participante;
	}

	public TipoAvMobile getTipoAv() {
		return tipoAv;
	}

	public void setTipoAv(TipoAvMobile tipoAv) {
		this.tipoAv = tipoAv;
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

	public TipoClient getTipoClient() {
		return tipoClient;
	}

	public void setTipoClient(TipoClient tipoClient) {
		this.tipoClient = tipoClient;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PreenchimentoMobileTO that = (PreenchimentoMobileTO) o;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/*@Override
	public int compareTo(Object o) {
		return getParticipante().getAvaliado().getNome().compareTo(((PreenchimentoMobileTO) o).getParticipante().getAvaliado().getNome());
	}*/
}
