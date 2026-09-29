package br.com.fasolti.avd.transfer;


import br.com.fasolti.avd.transfer.util.BaseTO;

public class AtividadeCBOTO extends BaseTO {

	private PerfilCBOTO perfil;

	private Integer ordem;

	private String descricao;

	public PerfilCBOTO getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilCBOTO perfil) {
		this.perfil = perfil;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
