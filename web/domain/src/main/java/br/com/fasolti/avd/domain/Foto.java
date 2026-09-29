package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.TipoFoto;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class Foto extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 594380626229257545L;

	private String base64;

	@Column(name = "id_tipo")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoFoto tipo;

	@ManyToOne
	@JoinColumn(name="id_funcionario")
	private Funcionario funcionario;

	public Foto() {
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public TipoFoto getTipo() {
		return tipo;
	}

	public void setTipo(TipoFoto tipo) {
		this.tipo = tipo;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
}
