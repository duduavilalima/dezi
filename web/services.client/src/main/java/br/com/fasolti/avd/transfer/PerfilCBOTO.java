package br.com.fasolti.avd.transfer;


import br.com.fasolti.avd.transfer.util.BaseTO;

import java.util.List;

public class PerfilCBOTO extends BaseTO {

	private OcupacaoCBOTO ocupacao;

	private String perfil;

	private String descricao;

	private List<AtividadeCBOTO> listaDeAtividades;

	public PerfilCBOTO() {}

	public OcupacaoCBOTO getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(OcupacaoCBOTO ocupacao) {
		this.ocupacao = ocupacao;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<AtividadeCBOTO> getListaDeAtividades() {
		return listaDeAtividades;
	}

	public void setListaDeAtividades(List<AtividadeCBOTO> listaDeAtividades) {
		this.listaDeAtividades = listaDeAtividades;
	}
}
