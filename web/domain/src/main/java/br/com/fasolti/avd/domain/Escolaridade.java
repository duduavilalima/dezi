package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Escolaridade extends BaseAuditedEntity {

	private static final long serialVersionUID = 1209181361632254768L;

	@Column(name = "nome", length = 35, nullable = false)
	private String nome;

	@Column(name = "preenche_outros_cursos", nullable = false)
	private Boolean preencheOutrosCursos;

	@Column(name = "preenche_tipo_curso", nullable = false)
	private Boolean preencheTipoCurso;

	@ManyToOne
	@JoinColumn(name = "id_contratante_fasolti")
	private ContratanteFasolti contratante;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public Escolaridade() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getPreencheOutrosCursos() {
		return preencheOutrosCursos;
	}

	public void setPreencheOutrosCursos(Boolean preencheOutrosCursos) {
		this.preencheOutrosCursos = preencheOutrosCursos;
	}

	public Boolean getPreencheTipoCurso() {
		return preencheTipoCurso;
	}

	public void setPreencheTipoCurso(Boolean preencheTipoCurso) {
		this.preencheTipoCurso = preencheTipoCurso;
	}

	public ContratanteFasolti getContratante() {
		return contratante;
	}

	public void setContratante(ContratanteFasolti contratante) {
		this.contratante = contratante;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public String toString() {
		return "Escolaridade [getId()=" + getId() + "]";
	}

}
