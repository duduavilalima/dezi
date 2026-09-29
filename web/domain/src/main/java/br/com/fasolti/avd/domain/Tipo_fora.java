package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Tipo_fora extends BaseAuditedEntity {

	private static final long serialVersionUID = -779428588618513066L;

	private String descricao;

	/*
	 * Mantido o ID para não ficar muito pessado, pois não será persistido pela aplicação, e sim por script.
	 */
	@Column(name = "id_situacao")
	private Long idSituacao;

	@Column(name = "nome_curto")
	private String nomeCurto;

	private Integer ordem;

	@Column(name = "sub_tipo")
	private String subTipo;

	private String tabela;

	private String valor;

	public Tipo_fora() {
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getIdSituacao() {
		return idSituacao;
	}

	public void setIdSituacao(Long idSituacao) {
		this.idSituacao = idSituacao;
	}

	public String getNomeCurto() {
		return nomeCurto;
	}

	public void setNomeCurto(String nomeCurto) {
		this.nomeCurto = nomeCurto;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(String subTipo) {
		this.subTipo = subTipo;
	}

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
			+ ((nomeCurto == null) ? 0 : nomeCurto.hashCode());
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
		result = prime * result + ((subTipo == null) ? 0 : subTipo.hashCode());
		result = prime * result + ((tabela == null) ? 0 : tabela.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (!super.equals(obj)) {
//			return false;
//		}
//		if (!(obj instanceof Tipo)) {
//			return false;
//		}
//		Tipo other = (Tipo) obj;
//		if (nomeCurto == null) {
//			if (other.nomeCurto != null) {
//				return false;
//			}
//		} else if (!nomeCurto.equals(other.nomeCurto)) {
//			return false;
//		}
//		if (ordem == null) {
//			if (other.ordem != null) {
//				return false;
//			}
//		} else if (!ordem.equals(other.ordem)) {
//			return false;
//		}
//		if (subTipo == null) {
//			if (other.subTipo != null) {
//				return false;
//			}
//		} else if (!subTipo.equals(other.subTipo)) {
//			return false;
//		}
//		if (tabela == null) {
//			if (other.tabela != null) {
//				return false;
//			}
//		} else if (!tabela.equals(other.tabela)) {
//			return false;
//		}
//		if (valor == null) {
//			if (other.valor != null) {
//				return false;
//			}
//		} else if (!valor.equals(other.valor)) {
//			return false;
//		}
//		return true;
//	}

	@Override
	public String toString() {
		//TODO Criar anotação @ToString e generalizar usando reflexão
		return String.format(
			"%s [nomeCurto=%s, ordem=%s, subTipo=%s, tabela=%s, valor=%s]",
			getClass().getSimpleName(), nomeCurto, ordem, subTipo, tabela, valor);
	}

}
