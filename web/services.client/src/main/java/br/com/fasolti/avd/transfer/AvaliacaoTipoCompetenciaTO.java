package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class AvaliacaoTipoCompetenciaTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "avaliação", notNull = true)
	private AvaliacaoTO avaliacao;

	@Validation(name = "tipo competência", notNull = true)
	private TipoCompetenciaTO tipoCompetencia;

	@Validation(name = "agente", notNull = true)
	private TipoAvaliacaoPerfil perfilAgente;

	@Validation(name = "peso", notNull = true)
	private Integer peso;

	public AvaliacaoTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public TipoCompetenciaTO getTipoCompetencia() {
		return tipoCompetencia;
	}

	public void setTipoCompetencia(TipoCompetenciaTO tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
	}

	public TipoAvaliacaoPerfil getPerfilAgente() {
		return perfilAgente;
	}

	public void setPerfilAgente(TipoAvaliacaoPerfil perfilAgente) {
		this.perfilAgente = perfilAgente;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}
}
