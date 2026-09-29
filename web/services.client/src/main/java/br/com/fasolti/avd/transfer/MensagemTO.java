package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class MensagemTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Icone", max = 500)
	private String icone;

	@Validation(name = "Tipo de mensagem", notNull = true)
	private Long idTipoMensagem;

	@Validation(name = "Mensagem", max = 200, notNull = true)
	private String mensagem;

	@Validation(name = "Som", max = 500)
	private String som;

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public Long getIdTipoMensagem() {
		return idTipoMensagem;
	}

	public void setIdTipoMensagem(Long idTipoMensagem) {
		this.idTipoMensagem = idTipoMensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getSom() {
		return som;
	}

	public void setSom(String som) {
		this.som = som;
	}
}
