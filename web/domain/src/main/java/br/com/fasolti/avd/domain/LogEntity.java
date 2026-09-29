package br.com.fasolti.avd.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_entity")
public class LogEntity extends BaseEntity {

	@Column(name = "id_entidade")
	private Long entidadeId;

	@Column(name = "nome_entidade")
	private String nomeEntidade;

	@Column(name = "usuario")
	private String usuario;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;

	@Column(name = "objeto")
	private String objeto;

	@Column(name = "uri")
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
