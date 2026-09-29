package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.avd.types.TipoPendencia;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "avaliacao_pendencia")
public class Pendencia extends BaseEntity implements Serializable {


	@ManyToOne
	@JoinColumn(name = "id_avaliacao")
	private Avaliacao avaliacao;

	@ManyToOne
	@JoinColumn(name = "id_avaliado")
	private Funcionario avaliado;

	@ManyToOne
	@JoinColumn(name = "id_avaliador")
	private Funcionario avaliador;

	@Column(name = "id_perfil_agente")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoAvaliacaoPerfil perfilAgente;

	@Column(name = "codigo_erro")
	private String codigoErro;

	@Column(name = "tipo_pendencia")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoPendencia tipoPendencia;

	@Column(name = "mensagem_erro")
	private String mensagemErro;

	@ManyToOne
	@JoinColumn(name = "id_cargo")
	private Cargo cargo;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_ultima_atualizacao")
	private Date dataUltimaAtualizacao;

	public Pendencia(String codigoErro, String mensagemErro, TipoPendencia tipoPendencia, Cargo cargo) {
		this.codigoErro = codigoErro;
		this.mensagemErro = mensagemErro;
		this.tipoPendencia = tipoPendencia;
		this.cargo = cargo;
	}

	public Pendencia() {
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public Funcionario getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(Funcionario avaliado) {
		this.avaliado = avaliado;
	}

	public Funcionario getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(Funcionario avaliador) {
		this.avaliador = avaliador;
	}

	public TipoAvaliacaoPerfil getPerfilAgente() {
		return perfilAgente;
	}

	public void setPerfilAgente(TipoAvaliacaoPerfil perfilAgente) {
		this.perfilAgente = perfilAgente;
	}

	public String getCodigoErro() {
		return codigoErro;
	}

	public void setCodigoErro(String codigoErro) {
		this.codigoErro = codigoErro;
	}

	public TipoPendencia getTipoPendencia() {
		return tipoPendencia;
	}

	public void setTipoPendencia(TipoPendencia tipoPendencia) {
		this.tipoPendencia = tipoPendencia;
	}

	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Date getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}
}
