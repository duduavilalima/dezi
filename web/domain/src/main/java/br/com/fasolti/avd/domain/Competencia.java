package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.util.TransactionalObject;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Competencia extends BaseAuditedEntity implements TransactionalObject<Situacao>, Comparable {

	private static final long serialVersionUID = -3098123680026653804L;

	@Column(name = "descricao_abreviada")
	private String descricaoAbreviada;

	@Column(name = "direcionamento_gestor")
	private String direcionamentoGestor;

	@Column(name = "direcionamento_nao_gestor")
	private String direcionamentoNaoGestor;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	@ManyToOne
	@JoinColumn(name = "id_modelo_graduacao")
	private ModeloGraduacao modeloGraduacao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tipo_competencia")
	private TipoCompetencia tipo;

	public Competencia() {
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getDirecionamentoGestor() {
		return direcionamentoGestor;
	}

	public void setDirecionamentoGestor(String direcionamentoGestor) {
		this.direcionamentoGestor = direcionamentoGestor;
	}

	public String getDirecionamentoNaoGestor() {
		return direcionamentoNaoGestor;
	}

	public void setDirecionamentoNaoGestor(String direcionamentoNaoGestor) {
		this.direcionamentoNaoGestor = direcionamentoNaoGestor;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public ModeloGraduacao getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacao modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}

	public TipoCompetencia getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompetencia tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Object o) {
		return getDescricaoAbreviada().compareTo(((Competencia) o).getDescricaoAbreviada());
	}

	@Override
	public Situacao getStatus() {
		return situacao;
	}

	@Override
	public void setStatus(Situacao situacao) {
		this.situacao = situacao;
	}
}
