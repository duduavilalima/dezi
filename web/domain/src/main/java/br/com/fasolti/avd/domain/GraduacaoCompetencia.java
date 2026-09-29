package br.com.fasolti.avd.domain;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "graduacao_competencia")
public class GraduacaoCompetencia extends BaseAuditedEntity {

	private static final long serialVersionUID = 667120262614108214L;

	@ManyToOne
	@JoinColumn(name = "id_modelo_graduacao_comp")
	private ModeloGraduacaoComp modeloGraduacao;

	@Column(name = "id_graduacao")
	private Long idGraduacao;

	@Column(name = "ordem")
	private Integer ordem;

	@Column(name = "label")
	private String label;

	@Column(name = "hint")
	private String hint;

	@Column(name = "ponto")
	private Integer ponto;

	@Column(name = "cor")
	private String cor;

	@Column(name = "atualizador_original")
	private String atualizadorOriginal;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_atualizacao_original")
	private Date dataAtualizacaoOriginal;

	public ModeloGraduacaoComp getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacaoComp modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}

	public Long getIdGraduacao() {
		return idGraduacao;
	}

	public void setIdGraduacao(Long idGraduacao) {
		this.idGraduacao = idGraduacao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public Integer getPonto() {
		return ponto;
	}

	public void setPonto(Integer ponto) {
		this.ponto = ponto;
	}

	public String getAtualizadorOriginal() {
		return atualizadorOriginal;
	}

	public void setAtualizadorOriginal(String atualizadorOriginal) {
		this.atualizadorOriginal = atualizadorOriginal;
	}

	public Date getDataAtualizacaoOriginal() {
		return dataAtualizacaoOriginal;
	}

	public void setDataAtualizacaoOriginal(Date dataAtualizacaoOriginal) {
		this.dataAtualizacaoOriginal = dataAtualizacaoOriginal;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
}
