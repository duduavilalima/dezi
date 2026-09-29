package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;

import java.time.LocalDateTime;

public class LogEntityTO extends BaseTO {
	private Long entidadeId;

	private String nomeEntidade;

	private String usuario;

	private LocalDateTime dataCriacao;

	private String objeto;

	private String uri;

	public Long getEntidadeId() {
		return entidadeId;
	}

	public void setEntidadeId(Long entidadeId) {
		this.entidadeId = entidadeId;
	}

	public String getNomeEntidade() {
		return nomeEntidade;
	}

	public void setNomeEntidade(String nomeEntidade) {
		this.nomeEntidade = nomeEntidade;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
