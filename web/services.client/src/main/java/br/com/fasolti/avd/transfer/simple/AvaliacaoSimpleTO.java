package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.avd.types.SituacaoPreenchimento;

import java.util.Date;

public class AvaliacaoSimpleTO extends BaseAuditedTO {

	private String descricao;

	private String descricaoAbreviada;

	private String emailPadraoDaAvaliacao;

	private CampanhaSimpleTO campanha;

	private SituacaoAvaliacao situacao;

	private SituacaoPreenchimento situacaoPreenchimento;

	private Boolean preencheAv1;

	private Boolean preencheAv2;

	private Boolean preencheAv3;

	private Boolean preencheAv4;

	private Boolean gestores;

	private Boolean subordinados;

	private Boolean pares;

	private Date limiteAv1;

	private Date limiteAv2;

	private Date limiteAv3;

	private Date limiteAv4;

	public AvaliacaoSimpleTO() {
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getEmailPadraoDaAvaliacao() {
		return emailPadraoDaAvaliacao;
	}

	public void setEmailPadraoDaAvaliacao(String emailPadraoDaAvaliacao) {
		this.emailPadraoDaAvaliacao = emailPadraoDaAvaliacao;
	}

	public CampanhaSimpleTO getCampanha() {
		return campanha;
	}

	public void setCampanha(CampanhaSimpleTO campanha) {
		this.campanha = campanha;
	}

	public SituacaoAvaliacao getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoAvaliacao situacao) {
		this.situacao = situacao;
	}

	public SituacaoPreenchimento getSituacaoPreenchimento() {
		return situacaoPreenchimento;
	}

	public void setSituacaoPreenchimento(SituacaoPreenchimento situacaoPreenchimento) {
		this.situacaoPreenchimento = situacaoPreenchimento;
	}

	public Boolean getPreencheAv1() {
		return preencheAv1;
	}

	public void setPreencheAv1(Boolean preencheAv1) {
		this.preencheAv1 = preencheAv1;
	}

	public Boolean getPreencheAv2() {
		return preencheAv2;
	}

	public void setPreencheAv2(Boolean preencheAv2) {
		this.preencheAv2 = preencheAv2;
	}

	public Boolean getPreencheAv3() {
		return preencheAv3;
	}

	public void setPreencheAv3(Boolean preencheAv3) {
		this.preencheAv3 = preencheAv3;
	}

	public Boolean getPreencheAv4() {
		return preencheAv4;
	}

	public void setPreencheAv4(Boolean preencheAv4) {
		this.preencheAv4 = preencheAv4;
	}

	public Date getLimiteAv1() {
		return limiteAv1;
	}

	public void setLimiteAv1(Date limiteAv1) {
		this.limiteAv1 = limiteAv1;
	}

	public Date getLimiteAv2() {
		return limiteAv2;
	}

	public void setLimiteAv2(Date limiteAv2) {
		this.limiteAv2 = limiteAv2;
	}

	public Date getLimiteAv3() {
		return limiteAv3;
	}

	public void setLimiteAv3(Date limiteAv3) {
		this.limiteAv3 = limiteAv3;
	}

	public Date getLimiteAv4() {
		return limiteAv4;
	}

	public void setLimiteAv4(Date limiteAv4) {
		this.limiteAv4 = limiteAv4;
	}

	public Boolean getGestores() {
		return gestores;
	}

	public void setGestores(Boolean gestores) {
		this.gestores = gestores;
	}

	public Boolean getSubordinados() {
		return subordinados;
	}

	public void setSubordinados(Boolean subordinados) {
		this.subordinados = subordinados;
	}

	public Boolean getPares() {
		return pares;
	}

	public void setPares(Boolean pares) {
		this.pares = pares;
	}
}
