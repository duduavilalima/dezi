package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "curso_especializacao")
public class CursoEspecializacao extends BaseAuditedEntity {

	private static final long serialVersionUID = -7463243906674293983L;

	@Column(name = "nome", length = 80, nullable = false)
	private String nome;

	@Column(name = "preenche_outros_cursos", nullable = false)
	private Boolean preencheOutrosCursos;

	@ManyToOne
	@JoinColumn(name = "id_contratante_fasolti")
	private ContratanteFasolti contratante;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public CursoEspecializacao() {
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
}
