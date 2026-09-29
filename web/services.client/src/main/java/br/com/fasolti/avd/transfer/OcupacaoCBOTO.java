package br.com.fasolti.avd.transfer;


import br.com.fasolti.avd.transfer.util.BaseTO;

public class OcupacaoCBOTO extends BaseTO {

	private String codigo;

	private String descricao;

	public OcupacaoCBOTO() {
	}

	public OcupacaoCBOTO(Long id, String codigo, String descricao) {
        setId(id);
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
