package br.com.fasolti.avd.transfer.util;

import br.com.fasolti.commons.client.transfer.TransferObject;

public class BaseTO implements TransferObject {
	private Long id;

	@Override
	public final Long getId() {
		return id;
	}

	@Override
	public final void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BaseTO baseTO = (BaseTO) o;

		return getId() != null ? getId().equals(baseTO.getId()) : baseTO.getId() == null;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
