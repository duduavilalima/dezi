package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="tipo_curso")
public class TipoCurso extends BaseAuditedEntity {

	private static final long serialVersionUID = -4511250443773753331L;

	private String nome;

	@Column(name="preenche_especializacao")
	private Boolean preencheEspecializacao;

	@Column(name="preenche_graduacao")
	private Boolean preencheGraduacao;

	@ManyToOne
	@JoinColumn(name="id_contratante_fasolti")
	private ContratanteFasolti contratante;


	@Column(name="id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public TipoCurso() {
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getPreencheEspecializacao() {
		return this.preencheEspecializacao;
	}

	public void setPreencheEspecializacao(Boolean preencheEspecializacao) {
		this.preencheEspecializacao = preencheEspecializacao;
	}

	public Boolean getPreencheGraduacao() {
		return this.preencheGraduacao;
	}

	public void setPreencheGraduacao(Boolean preencheGraduacao) {
		this.preencheGraduacao = preencheGraduacao;
	}

	public ContratanteFasolti getContratante() {
		return this.contratante;
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
