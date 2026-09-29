package br.com.fasolti.queue.dto;

public class ParametroDTO {

	private Long id;
	private String chave;
	private String valor;

	// Builder
	public static class ParametroDTOBuilder {
		private String chave;
		private String valor;

		public ParametroDTOBuilder(String chave, String valor) {
			this.chave = chave;
			this.valor = valor;
		}

		public ParametroDTO build() {
			return new ParametroDTO(this);
		}
	}

	private ParametroDTO(ParametroDTOBuilder builder) {
		this.chave = builder.chave;
		this.valor = builder.valor;
	}
	//

	public final Long getId() {
		return id;
	}

	public final void setId(Long id) {
		this.id = id;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
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
		int result = 1;
		result = prime * result + ((chave == null) ? 0 : chave.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		ParametroDTO other = (ParametroDTO) obj;
		if (chave == null) {
			if (other.chave != null) {
				return false;
			}
		} else if (!chave.equals(other.chave)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
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
		return "ParametroDTO [id=" + id + ", chave=" + chave + ", valor=" + valor + "]";
	}

}