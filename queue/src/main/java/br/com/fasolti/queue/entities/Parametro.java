package br.com.fasolti.queue.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parametros")
public class Parametro implements Serializable {

	private static final long serialVersionUID = -2057472633004096981L;

	@Id
	private String chave;

	private String valor;

	// Builder
	public static class ParametroBuilder {
		private String chave;
		private String valor;

		public ParametroBuilder(String chave, String valor) {
			this.chave = chave;
			this.valor = valor;
		}

		public Parametro build() {
			return new Parametro(this);
		}
	}

	private Parametro(ParametroBuilder builder) {
		this.chave = builder.chave;
		this.valor = builder.valor;
	}
	//

	public Parametro() {
	}

	public String getChave() {
		return this.chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chave == null) ? 0 : chave.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Parametro)) {
			return false;
		}
		Parametro other = (Parametro) obj;
		if (chave == null) {
			if (other.chave != null) {
				return false;
			}
		} else if (!chave.equals(other.chave)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Parametro [chave=" + chave + ", valor=" + valor + "]";
	}

}
