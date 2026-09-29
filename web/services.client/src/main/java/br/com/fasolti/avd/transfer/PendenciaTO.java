package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.avd.types.TipoPendencia;
import br.com.fasolti.commons.client.transfer.SearchObject;

import java.util.Date;

public class PendenciaTO extends BaseAuditedTO implements SearchObject {

	private AvaliacaoTO avaliacao;

	private FuncionarioSimpleTO avaliado;

	private FuncionarioSimpleTO avaliador;

	private TipoAvaliacaoPerfil perfilAgente;

	private String codigoErro;

	private TipoPendencia tipoPendencia;

	private String mensagemErro;

	private CargoSimpleTO cargo;

	private Date dataUltimaAtualizacao;


	public PendenciaTO(String codigoErro, String mensagemErro, TipoPendencia tipoPendencia, CargoSimpleTO cargo, Date dataUltimaAtualizacao) {
		this.codigoErro = codigoErro;
		this.mensagemErro = mensagemErro;
		this.tipoPendencia = tipoPendencia;
		this.cargo = cargo;
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}

	public PendenciaTO(String codigoErro, String mensagemErro, TipoPendencia tipoPendencia, FuncionarioSimpleTO avaliado, FuncionarioSimpleTO avaliador, Date dataUltimaAtualizacao) {
		this.codigoErro = codigoErro;
		this.mensagemErro = mensagemErro;
		this.tipoPendencia = tipoPendencia;
		this.avaliado = avaliado;
		this.avaliador = avaliador;
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}

	public PendenciaTO(String codigoErro, String mensagemErro, TipoPendencia tipoPendencia, FuncionarioSimpleTO avaliado, Date dataUltimaAtualizacao) {
		this.codigoErro = codigoErro;
		this.mensagemErro = mensagemErro;
		this.tipoPendencia = tipoPendencia;
		this.avaliado = avaliado;
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}

	public PendenciaTO() {
	}


	public AvaliacaoTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public FuncionarioSimpleTO getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(FuncionarioSimpleTO avaliado) {
		this.avaliado = avaliado;
	}

	public FuncionarioSimpleTO getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(FuncionarioSimpleTO avaliador) {
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

	public CargoSimpleTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoSimpleTO cargo) {
		this.cargo = cargo;
	}

	public Date getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}
}
