package br.com.fasolti.avd.transfer.mobile;

import br.com.fasolti.avd.types.Situacao;

public class GraduacaoMobileTO implements Comparable {
	private Long id;
	private String label;
	private Integer ordem;
	private String hint;
	private Integer ponto;
	private String cor;

	//Proveniente da Graduacao
	private Situacao situacao;
	private Boolean comentarioObrigatorio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public Boolean getComentarioObrigatorio() {
		return comentarioObrigatorio;
	}

	public void setComentarioObrigatorio(Boolean comentarioObrigatorio) {
		this.comentarioObrigatorio = comentarioObrigatorio;
	}

	@Override
	public int compareTo(Object o) {
		return getOrdem().compareTo(((GraduacaoMobileTO) o).getOrdem());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GraduacaoMobileTO that = (GraduacaoMobileTO) o;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
