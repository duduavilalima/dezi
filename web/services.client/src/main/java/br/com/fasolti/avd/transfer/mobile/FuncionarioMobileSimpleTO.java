package br.com.fasolti.avd.transfer.mobile;

public class FuncionarioMobileSimpleTO {
	private Long id;
	private String nome;
	private String fotoBase64;
	private Long idCompetencia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFotoBase64() {
		return fotoBase64;
	}

	public void setFotoBase64(String fotoBase64) {
		this.fotoBase64 = fotoBase64;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FuncionarioMobileSimpleTO that = (FuncionarioMobileSimpleTO) o;

		return id.equals(that.id);
	}

	public Long getIdCompetencia() {
		return idCompetencia;
	}

	public void setIdCompetencia(Long idCompetencia) {
		this.idCompetencia = idCompetencia;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
