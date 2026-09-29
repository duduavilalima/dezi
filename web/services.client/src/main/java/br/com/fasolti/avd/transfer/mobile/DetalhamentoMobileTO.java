package br.com.fasolti.avd.transfer.mobile;

import br.com.fasolti.commons.client.transfer.TransferObject;

import java.util.List;

public class DetalhamentoMobileTO implements Comparable, TransferObject {
	private Long id;
	private String descricao;
	private List<PreenchimentoMobileTO> listaPreenchimentos;
	private List<GraduacaoMobileTO> listaGraduacao;
	/*private List<PreenchimentoSimpleMobileTO> listaPreenchimentoSimple;*/
	private Integer ordem; /*Usado para fazer a ordenação*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<PreenchimentoMobileTO> getListaPreenchimentos() {
		return listaPreenchimentos;
	}

	public void setListaPreenchimentos(List<PreenchimentoMobileTO> listaPreenchimentos) {
		this.listaPreenchimentos = listaPreenchimentos;
	}

	public List<GraduacaoMobileTO> getListaGraduacao() {
		return listaGraduacao;
	}

	public void setListaGraduacao(List<GraduacaoMobileTO> listaGraduacao) {
		this.listaGraduacao = listaGraduacao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DetalhamentoMobileTO that = (DetalhamentoMobileTO) o;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public int compareTo(Object o) {
		return getDescricao().compareTo(((DetalhamentoMobileTO) o).getDescricao());
	}
}
