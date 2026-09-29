package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "competencia_detalhamento")
public class CompetenciaDetalhamento extends BaseAuditedEntity {

	private static final long serialVersionUID = -7058431693749613354L;

	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false)
	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "id_competencia")
	private Competencia competencia;


	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	@ManyToOne
	@JoinColumn(name = "id_modelo_graduacao")
	private ModeloGraduacao modeloGraduacao;

	// FIXME Mover para o serviço
	@OneToMany(mappedBy = "competenciaDetalhamento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CompDetalhamentoHint> competenciaDetalhamentosCalibrahint;

	public CompetenciaDetalhamento() {
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public List<CompDetalhamentoHint> getCompetenciaDetalhamentosCalibrahint() {
		return competenciaDetalhamentosCalibrahint;
	}

	public void setCompetenciaDetalhamentosCalibrahint(List<CompDetalhamentoHint> competenciaDetalhamentosCalibrahint) {
		this.competenciaDetalhamentosCalibrahint = competenciaDetalhamentosCalibrahint;
	}

	public ModeloGraduacao getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacao modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}
}
