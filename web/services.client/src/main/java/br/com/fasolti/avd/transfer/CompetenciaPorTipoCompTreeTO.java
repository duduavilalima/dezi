package br.com.fasolti.avd.transfer;

import java.util.Comparator;
import java.util.List;

public class CompetenciaPorTipoCompTreeTO implements Comparator<CompetenciaPorTipoCompTreeTO> {

	private String nome;

	private Long idCompetencia;

	private Long idTipoCompetencia;

	private Boolean isTipoCompetencia;

	private List<CompetenciaPorTipoCompTreeTO> children;

	public CompetenciaPorTipoCompTreeTO() {
	}

	public CompetenciaPorTipoCompTreeTO(String nome, Long idCompetencia, Long idTipoCompetencia, Boolean isTipoCompetencia) {
		this.nome = nome;
		this.idCompetencia = idCompetencia;
		this.idTipoCompetencia = idTipoCompetencia;
		this.isTipoCompetencia = isTipoCompetencia;
	}

	public CompetenciaPorTipoCompTreeTO(String nome, Long idCompetencia) {
		this.nome = nome;
		this.idCompetencia = idCompetencia;
		this.isTipoCompetencia = Boolean.FALSE;
		this.children = children;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdCompetencia() {
		return idCompetencia;
	}

	public void setIdCompetencia(Long idCompetencia) {
		this.idCompetencia = idCompetencia;
	}

	public Long getIdTipoCompetencia() {
		return idTipoCompetencia;
	}

	public void setIdTipoCompetencia(Long idTipoCompetencia) {
		this.idTipoCompetencia = idTipoCompetencia;
	}

	public Boolean getTipoCompetencia() {
		return isTipoCompetencia;
	}

	public void setTipoCompetencia(Boolean tipoCompetencia) {
		isTipoCompetencia = tipoCompetencia;
	}

	public List<CompetenciaPorTipoCompTreeTO> getChildren() {
		return children;
	}

	public void setChildren(List<CompetenciaPorTipoCompTreeTO> children) {
		this.children = children;
	}

	@Override
	public int compare(CompetenciaPorTipoCompTreeTO o1, CompetenciaPorTipoCompTreeTO o2) {
		return o1.getNome().compareTo(o2.getNome());
	}
}
