package br.com.fasolti.avd.domain;

import br.com.fasolti.commons.domain.DomainObject;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity implements DomainObject {

	private static final long serialVersionUID = 7643030563973061177L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "ID", nullable = false)
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public boolean isNew() {
		return null == this.id;
	}

	@Override
	public String toString() {
		return String.format("Entidade do tipo %s com codigo: %s", this.getClass().getName(), this.id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		BaseEntity other = (BaseEntity) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;

		return true;
	}
}
