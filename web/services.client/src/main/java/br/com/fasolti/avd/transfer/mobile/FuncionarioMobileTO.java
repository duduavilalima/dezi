package br.com.fasolti.avd.transfer.mobile;

import java.util.List;

public class FuncionarioMobileTO implements Comparable {
	private Long id;

	private Long empresaId;

	private String fotoBase64;

	private String nome;

	private Long unidadeId;

	private CargoMobileTO cargo;

	private DepartamentoMobileTO departamento;

	private List<CompetenciaMobileTO> listaCompetencias;

	private List<ApontamentoFuncionalMobileTO> listaApontamentos;

	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(Long empresaId) {
		this.empresaId = empresaId;
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

	public CargoMobileTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoMobileTO cargo) {
		this.cargo = cargo;
	}

	public DepartamentoMobileTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoMobileTO departamento) {
		this.departamento = departamento;
	}

	public List<CompetenciaMobileTO> getListaCompetencias() {
		return listaCompetencias;
	}

	public void setListaCompetencias(List<CompetenciaMobileTO> listaCompetencias) {
		this.listaCompetencias = listaCompetencias;
	}

	public List<ApontamentoFuncionalMobileTO> getListaApontamentos() {
		return listaApontamentos;
	}

	public void setListaApontamentos(List<ApontamentoFuncionalMobileTO> listaApontamentos) {
		this.listaApontamentos = listaApontamentos;
	}

	public Long getUnidadeId() {
		return unidadeId;
	}

	public void setUnidadeId(Long unidadeId) {
		this.unidadeId = unidadeId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int compareTo(Object o) { return getNome().compareTo(((FuncionarioMobileTO) o).getNome()); }
}
