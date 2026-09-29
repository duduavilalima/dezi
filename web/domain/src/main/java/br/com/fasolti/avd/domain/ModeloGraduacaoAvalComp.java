package br.com.fasolti.avd.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "modelo_graduacao_aval_comp")
public class ModeloGraduacaoAvalComp extends BaseEntity {

	private static final long serialVersionUID = 667120262614108214L;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao_competencia")
	private AvaliacaoCompetencia avaliacaoCompetencia;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao_comp_det")
	private AvaliacaoCompDet avaliacaoCompDet;

	@ManyToOne
	@JoinColumn(name = "id_modelo_graduacao_comp")
	private ModeloGraduacaoComp modeloGraduacaoComp;


	public AvaliacaoCompetencia getAvaliacaoCompetencia() {
		return avaliacaoCompetencia;
	}

	public void setAvaliacaoCompetencia(AvaliacaoCompetencia avaliacaoCompetencia) {
		this.avaliacaoCompetencia = avaliacaoCompetencia;
	}

	public ModeloGraduacaoComp getModeloGraduacaoComp() {
		return modeloGraduacaoComp;
	}

	public void setModeloGraduacaoComp(ModeloGraduacaoComp modeloGraduacaoComp) {
		this.modeloGraduacaoComp = modeloGraduacaoComp;
	}


	public AvaliacaoCompDet getAvaliacaoCompDet() {
		return avaliacaoCompDet;
	}

	public void setAvaliacaoCompDet(AvaliacaoCompDet avaliacaoCompDet) {
		this.avaliacaoCompDet = avaliacaoCompDet;
	}
}
