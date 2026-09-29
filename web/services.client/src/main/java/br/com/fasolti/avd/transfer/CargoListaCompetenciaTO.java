package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.CompetenciaBaseTO;

import java.util.Set;

public class CargoListaCompetenciaTO {

	private CargoTO cargo;

	private Set<CompetenciaBaseTO> competencias;

	public CargoListaCompetenciaTO() {

	}

	public CargoListaCompetenciaTO(CargoTO cargo, Set<CompetenciaBaseTO> competencias) {
		this.cargo = cargo;
		this.competencias = competencias;
	}

	public CargoTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoTO cargo) {
		this.cargo = cargo;
	}

	public Set<CompetenciaBaseTO> getCompetencias() {
		return competencias;
	}

	public void setCompetencias(Set<CompetenciaBaseTO> competencias) {
		this.competencias = competencias;
	}
}
