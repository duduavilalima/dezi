package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.domain.util.DomainUtils;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "avaliacao_participante")
public class Participante extends BaseAuditedEntity {

	private static final long serialVersionUID = 7625151812439663553L;

	@Column(name = "consenso_liberado")
	private Boolean consensoLiberado;

	@Column(name = "revisao_liberada")
	private Boolean revisaoLiberada;

	@Column(name = "revisado")
	private Boolean revisado;

	@Column
	private String pdi;

	@Column(name = "comentario_final_avaliado")
	private String comentarioFinalAvaliado;

	@Column(name = "comentario_final_avaliador")
	private String comentarioFinalAvaliador;

	@Column(name = "comentario_final_revisor")
	private String comentarioFinalRevisor;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao")
	private Avaliacao avaliacao;

	@ManyToOne
	@JoinColumn(name = "id_avaliado")
	private Funcionario avaliado;

	@ManyToOne
	@JoinColumn(name = "id_avaliador")
	private Funcionario avaliador;

	@ManyToOne
	@JoinColumn(name = "id_revisor")
	private Funcionario revisor;

	@Temporal(TemporalType.DATE)
	@Column(name = "limite_av1")
	private Date limiteAv1;

	@Temporal(TemporalType.DATE)
	@Column(name = "limite_av2")
	private Date limiteAv2;

	@Temporal(TemporalType.DATE)
	@Column(name = "limite_av3")
	private Date limiteAv3;

	@Temporal(TemporalType.DATE)
	@Column(name = "limite_av4")
	private Date limiteAv4;

	@Column(name = "preenchida_av1")
	private Boolean preenchidaAV1;

	@Column(name = "preenchida_av2")
	private Boolean preenchidaAV2;

	@Column(name = "preenchida_av3")
	private Boolean preenchidaAV3;

	@Column(name = "preenchida_av4")
	private Boolean preenchidaAV4;

	@Column(name = "consenso_iniciado")
	private Boolean consensoIniciado;

	@Column(name = "revisao_iniciada")
	private Boolean revisaoIniciada;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_conclusao_av1")
	private Date dataConclusaoAv1;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_conclusao_av2")
	private Date dataConclusaoAv2;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_conclusao_av3")
	private Date dataConclusaoAv3;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_conclusao_av4")
	private Date dataConclusaoAv4;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio_av1")
	private Date dataInicioAv1;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio_av2")
	private Date dataInicioAv2;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio_av3")
	private Date dataInicioAv3;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio_av4")
	private Date dataInicioAv4;

	@Column(name = "id_perfil_agente")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoAvaliacaoPerfil perfilAgente;

	@Column(name = "id_cargo_avaliado")
	private Long idCargoAvaliado;

	@Column(name = "nome_cargo_avaliado")
	private String nomeCargoAvaliado;

	@Column(name = "id_depto_avaliado")
	private Long idDeptoAvaliado;

	@Column(name = "nome_depto_avaliado")
	private String nomeDeptoAvaliado;


	public Participante() {
	}

	public Boolean getConsensoLiberado() {
		return consensoLiberado;
	}

	public void setConsensoLiberado(Boolean consensoLiberado) {
		this.consensoLiberado = consensoLiberado;
	}

	public Boolean getRevisaoLiberada() {
		return revisaoLiberada;
	}

	public void setRevisaoLiberada(Boolean revisaoLiberada) {
		this.revisaoLiberada = revisaoLiberada;
	}

	public String getPdi() {
		return pdi;
	}

	public void setPdi(String pdi) {
		this.pdi = pdi;
	}

	public String getComentarioFinalAvaliado() {
		return comentarioFinalAvaliado;
	}

	public void setComentarioFinalAvaliado(String comentarioFinalAvaliado) {
		this.comentarioFinalAvaliado = comentarioFinalAvaliado;
	}

	public String getComentarioFinalAvaliador() {
		return comentarioFinalAvaliador;
	}

	public void setComentarioFinalAvaliador(String comentarioFinalAvaliador) {
		this.comentarioFinalAvaliador = comentarioFinalAvaliador;
	}

	public String getComentarioFinalRevisor() {
		return comentarioFinalRevisor;
	}

	public void setComentarioFinalRevisor(String comentarioFinalRevisor) {
		this.comentarioFinalRevisor = comentarioFinalRevisor;
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

	public Funcionario getRevisor() {
		return revisor;
	}

	public void setRevisor(Funcionario revisor) {
		this.revisor = revisor;
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

	public Boolean getPreenchidaAV1() {
		return DomainUtils.safeBoolean(preenchidaAV1);
	}

	public void setPreenchidaAV1(Boolean preenchidaAV1) {
		this.preenchidaAV1 = preenchidaAV1;
	}

	public Boolean getPreenchidaAV2() {
		return DomainUtils.safeBoolean(preenchidaAV2);
	}

	public void setPreenchidaAV2(Boolean preenchidaAV2) {
		this.preenchidaAV2 = preenchidaAV2;
	}

	public Boolean getPreenchidaAV3() {
		return DomainUtils.safeBoolean(preenchidaAV3);
	}

	public void setPreenchidaAV3(Boolean preenchidaAV3) {
		this.preenchidaAV3 = preenchidaAV3;
	}

	public Boolean getPreenchidaAV4() {
		return DomainUtils.safeBoolean(preenchidaAV4);
	}

	public void setPreenchidaAV4(Boolean preenchidaAV4) {
		this.preenchidaAV4 = preenchidaAV4;
	}

	public Boolean getConsensoIniciado() {
		return consensoIniciado;
	}

	public void setConsensoIniciado(Boolean consensoIniciado) {
		this.consensoIniciado = consensoIniciado;
	}

	public Boolean getRevisaoIniciada() {
		return revisaoIniciada;
	}

	public void setRevisaoIniciada(Boolean revisaoIniciada) {
		this.revisaoIniciada = revisaoIniciada;
	}

	public Date getDataConclusaoAv1() {
		return dataConclusaoAv1;
	}

	public void setDataConclusaoAv1(Date dataConclusaoAv1) {
		this.dataConclusaoAv1 = dataConclusaoAv1;
	}

	public Date getDataConclusaoAv2() {
		return dataConclusaoAv2;
	}

	public void setDataConclusaoAv2(Date dataConclusaoAv2) {
		this.dataConclusaoAv2 = dataConclusaoAv2;
	}

	public Date getDataConclusaoAv3() {
		return dataConclusaoAv3;
	}

	public void setDataConclusaoAv3(Date dataConclusaoAv3) {
		this.dataConclusaoAv3 = dataConclusaoAv3;
	}

	public Date getDataConclusaoAv4() {
		return dataConclusaoAv4;
	}

	public void setDataConclusaoAv4(Date dataConclusaoAv4) {
		this.dataConclusaoAv4 = dataConclusaoAv4;
	}

	public Date getDataInicioAv1() {
		return dataInicioAv1;
	}

	public void setDataInicioAv1(Date dataInicioAv1) {
		this.dataInicioAv1 = dataInicioAv1;
	}

	public Date getDataInicioAv2() {
		return dataInicioAv2;
	}

	public void setDataInicioAv2(Date dataInicioAv2) {
		this.dataInicioAv2 = dataInicioAv2;
	}

	public Date getDataInicioAv3() {
		return dataInicioAv3;
	}

	public void setDataInicioAv3(Date dataInicioAv3) {
		this.dataInicioAv3 = dataInicioAv3;
	}

	public Date getDataInicioAv4() {
		return dataInicioAv4;
	}

	public void setDataInicioAv4(Date dataInicioAv4) {
		this.dataInicioAv4 = dataInicioAv4;
	}

	public TipoAvaliacaoPerfil getPerfilAgente() {
		return perfilAgente;
	}

	public void setPerfilAgente(TipoAvaliacaoPerfil perfilAgente) {
		this.perfilAgente = perfilAgente;
	}

	public Boolean getRevisado() {
		return revisado;
	}

	public void setRevisado(Boolean revisado) {
		this.revisado = revisado;
	}

	public Long getIdCargoAvaliado() {
		return idCargoAvaliado;
	}

	public void setIdCargoAvaliado(Long idCargoAvaliado) {
		this.idCargoAvaliado = idCargoAvaliado;
	}

	public String getNomeCargoAvaliado() {
		return nomeCargoAvaliado;
	}

	public void setNomeCargoAvaliado(String nomeCargoAvaliado) {
		this.nomeCargoAvaliado = nomeCargoAvaliado;
	}

	public Long getIdDeptoAvaliado() {
		return idDeptoAvaliado;
	}

	public void setIdDeptoAvaliado(Long idDeptoAvaliado) {
		this.idDeptoAvaliado = idDeptoAvaliado;
	}

	public String getNomeDeptoAvaliado() {
		return nomeDeptoAvaliado;
	}

	public void setNomeDeptoAvaliado(String nomeDeptoAvaliado) {
		this.nomeDeptoAvaliado = nomeDeptoAvaliado;
	}

}
