package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.avd.types.SituacaoPreenchimento;
import br.com.fasolti.commons.util.TransactionalObject;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Avaliacao extends BaseAuditedEntity implements TransactionalObject<SituacaoAvaliacao>, Comparable {

	private static final long serialVersionUID = 667120262614108214L;

	private String descricao;

	@Column(name = "descricao_abreviada")
	private String descricaoAbreviada;

	@Column(name = "email_padrao_da_avaliacao")
	private String emailPadraoDaAvaliacao;

	@Column(name = "preenche_av1")
	private Boolean preencheAv1;

	@Temporal(TemporalType.DATE)
	@Column(name = "limite_av1")
	private Date limiteAv1;

	@Column(name = "preenche_av2")
	private Boolean preencheAv2;

	@Temporal(TemporalType.DATE)
	@Column(name = "limite_av2")
	private Date limiteAv2;

	@Column(name = "preenche_av3")
	private Boolean preencheAv3;

	@Temporal(TemporalType.DATE)
	@Column(name = "limite_av3")
	private Date limiteAv3;

	@Column(name = "preenche_av4")
	private Boolean preencheAv4;

	@Temporal(TemporalType.DATE)
	@Column(name = "limite_av4")
	private Date limiteAv4;

	@ManyToOne
	@JoinColumn(name = "id_campanha")
	private Campanha campanha;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoAvaliacao situacao;

	@Column(name = "id_situacao_preenchimento")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoPreenchimento situacaoPreenchimento;

	@Column(name = "autoavaliacao")
	private Boolean autoavaliacao;

	@Column(name = "gestores")
	private Boolean gestores;

	@Column(name = "subordinados")
	private Boolean subordinados;

	@Column(name = "pares")
	private Boolean pares;

	@Column(name = "em_liberacao")
	private Boolean emLiberacao;

	@Column(name = "av_homeoffice")
	private Boolean avHomeOffice;

	public Avaliacao() {
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

	public Boolean getPreencheAv1() {
		return preencheAv1;
	}

	public void setPreencheAv1(Boolean preencheAv1) {
		this.preencheAv1 = preencheAv1;
	}

	public Date getLimiteAv1() {
		return limiteAv1;
	}

	public void setLimiteAv1(Date limiteAv1) {
		this.limiteAv1 = limiteAv1;
	}

	public Boolean getPreencheAv2() {
		return preencheAv2;
	}

	public void setPreencheAv2(Boolean preencheAv2) {
		this.preencheAv2 = preencheAv2;
	}

	public Date getLimiteAv2() {
		return limiteAv2;
	}

	public void setLimiteAv2(Date limiteAv2) {
		this.limiteAv2 = limiteAv2;
	}

	public Boolean getPreencheAv3() {
		return preencheAv3;
	}

	public void setPreencheAv3(Boolean preencheAv3) {
		this.preencheAv3 = preencheAv3;
	}

	public Date getLimiteAv3() {
		return limiteAv3;
	}

	public void setLimiteAv3(Date limiteAv3) {
		this.limiteAv3 = limiteAv3;
	}

	public Boolean getPreencheAv4() {
		return preencheAv4;
	}

	public void setPreencheAv4(Boolean preencheAv4) {
		this.preencheAv4 = preencheAv4;
	}

	public Date getLimiteAv4() {
		return limiteAv4;
	}

	public void setLimiteAv4(Date limiteAv4) {
		this.limiteAv4 = limiteAv4;
	}

	public Campanha getCampanha() {
		return campanha;
	}

	public void setCampanha(Campanha campanha) {
		this.campanha = campanha;
	}

	public SituacaoPreenchimento getSituacaoPreenchimento() {
		return situacaoPreenchimento;
	}

	public void setSituacaoPreenchimento(SituacaoPreenchimento situacaoPreenchimento) {
		this.situacaoPreenchimento = situacaoPreenchimento;
	}

	public Boolean getAutoavaliacao() {
		return autoavaliacao;
	}

	public Boolean getEmLiberacao() {
		return emLiberacao;
	}

	public void setEmLiberacao(Boolean emLiberacao) {
		this.emLiberacao = emLiberacao;
	}

	public void setAutoavaliacao(Boolean autoavaliacao) {
		this.autoavaliacao = autoavaliacao;
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

	@Override
	public SituacaoAvaliacao getStatus() {
		return situacao;
	}

	@Override
	public void setStatus(SituacaoAvaliacao situacao) {
		this.situacao = situacao;
	}

	public Boolean getAvHomeOffice() {
		return avHomeOffice;
	}

	public void setAvHomeOffice(Boolean avHomeOffice) {
		this.avHomeOffice = avHomeOffice;
	}

	@Override
	public String toString() {
		return "Avaliacao [limiteAv1=" + limiteAv1 + ", limiteAv2=" + limiteAv2
			+ ", limiteAv3=" + limiteAv3 + ", limiteAv4=" + limiteAv4
			+ ", getId()=" + getId() + "]";
	}

	@Override
	public int compareTo(Object o) {
		return getDescricaoAbreviada().compareTo(((Avaliacao)o).getDescricaoAbreviada());
	}
}
