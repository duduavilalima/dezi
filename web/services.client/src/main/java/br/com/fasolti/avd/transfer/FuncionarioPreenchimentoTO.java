package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseTO;

public class FuncionarioPreenchimentoTO extends BaseTO {

	private String nome;
	private CargoSimpleTO cargo;
	private String foto;
	private AvaliacaoCompetenciaTO avaliacaoCompetencia;


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public CargoSimpleTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoSimpleTO cargo) {
		this.cargo = cargo;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public AvaliacaoCompetenciaTO getAvaliacaoCompetencia() {
		return avaliacaoCompetencia;
	}

	public void setAvaliacaoCompetencia(AvaliacaoCompetenciaTO avaliacaoCompetencia) {
		this.avaliacaoCompetencia = avaliacaoCompetencia;
	}
}
