package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class GraduacaoTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Graduação", max = 40, notNull = true)
	private String label;

	private Integer ordem;

	@Validation(name = "Pontuação", notNull = true)
	private Integer ponto;

	private String hint;

	private String cor;

	private Integer pontuacaoInferior;

    /**
     * Para ser usado em uma avaliação.
     */
	private Boolean comentarioObrigatorio;

	@Validation(name = "Modelo de graduação", notNull = true)
	private ModeloGraduacaoTO modeloGraduacao;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

    public GraduacaoTO() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Integer getPonto() {
        return ponto;
    }

    public void setPonto(Integer ponto) {
        this.ponto = ponto;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Boolean getComentarioObrigatorio() {
        return comentarioObrigatorio;
    }

    public void setComentarioObrigatorio(Boolean comentarioObrigatorio) {
        this.comentarioObrigatorio = comentarioObrigatorio;
    }

    public ModeloGraduacaoTO getModeloGraduacao() {
        return modeloGraduacao;
    }

    public void setModeloGraduacao(ModeloGraduacaoTO modeloGraduacao) {
        this.modeloGraduacao = modeloGraduacao;
    }

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public Integer getPontuacaoInferior() {
		return pontuacaoInferior;
	}

	public void setPontuacaoInferior(Integer pontuacaoInferior) {
		this.pontuacaoInferior = pontuacaoInferior;
	}
}

