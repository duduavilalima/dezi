package br.com.fasolti.avd.transfer;


import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class AvalDimComDetTO extends BaseTO implements SearchObject {

	private Long idAvaliacao;

	private Long idDimensao;

	private Long idCompDet;

	private String dimensaoNome;

	private String dimensaoDescricao;

	public Long getIdAvaliacao() {
		return idAvaliacao;
	}

	public void setIdAvaliacao(Long idAvaliacao) {
		this.idAvaliacao = idAvaliacao;
	}

	public Long getIdDimensao() {
		return idDimensao;
	}

	public void setIdDimensao(Long idDimensao) {
		this.idDimensao = idDimensao;
	}

	public Long getIdCompDet() {
		return idCompDet;
	}

	public void setIdCompDet(Long idCompDet) {
		this.idCompDet = idCompDet;
	}

	public String getDimensaoNome() {
		return dimensaoNome;
	}

	public void setDimensaoNome(String dimensaoNome) {
		this.dimensaoNome = dimensaoNome;
	}

	public String getDimensaoDescricao() {
		return dimensaoDescricao;
	}

	public void setDimensaoDescricao(String dimensaoDescricao) {
		this.dimensaoDescricao = dimensaoDescricao;
	}
}
