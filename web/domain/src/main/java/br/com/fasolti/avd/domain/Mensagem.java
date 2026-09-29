package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Mensagem extends BaseAuditedEntity {

	private static final long serialVersionUID = -1613220498287763807L;

	private String icone;

	@Column(name="id_tipo_mensagem")
	private Long idTipoMensagem;

	private String mensagem;

	private String som;

	public Mensagem() {
	}

	public String getIcone() {
		return this.icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public Long getIdTipoMensagem() {
		return this.idTipoMensagem;
	}

	public void setIdTipoMensagem(Long idTipoMensagem) {
		this.idTipoMensagem = idTipoMensagem;
	}

	public String getMensagem() {
		return this.mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getSom() {
		return this.som;
	}

	public void setSom(String som) {
		this.som = som;
	}
}
