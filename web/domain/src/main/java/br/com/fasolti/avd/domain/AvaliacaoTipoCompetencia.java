package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "avaliacao_tipo_competencia")
public class AvaliacaoTipoCompetencia extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1679109708790484639L;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao", insertable = true, updatable = false)
	private Avaliacao avaliacao;

	@ManyToOne
	@JoinColumn(name = "id_tipo", insertable = true, updatable = false)
	private TipoCompetencia tipoCompetencia;

	@Column(name = "id_perfil_agente", updatable = false)
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoAvaliacaoPerfil perfilAgente;

	@Column(name = "peso", nullable = false)
	private Integer peso;

	public AvaliacaoTipoCompetencia() {
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public TipoCompetencia getTipoCompetencia() {
		return tipoCompetencia;
	}

	public void setTipoCompetencia(TipoCompetencia tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
	}

	public TipoAvaliacaoPerfil getPerfilAgente() {
		return perfilAgente;
	}

	public void setPerfilAgente(TipoAvaliacaoPerfil perfilAgente) {
		this.perfilAgente = perfilAgente;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}
}
