package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class TipoCompetenciaTO extends BaseAuditedTO implements SearchObject {

	public Long idPai;

	public TipoCompetenciaTO paiTO;

	private EmpresaSimpleTO empresa;

	private Situacao situacao;

	@Validation(name = "Nome", max = 80, notNull = true)
	private String nome;

	@Validation(name = "Descrição", max = 80, notNull = true)
	private String descricao;

	private String nomeCurto;

	private Integer numeroFilhos;

	private Integer numeroPais;

	public Integer getNumeroFilhos() {
		return numeroFilhos;
	}

	public TipoCompetenciaTO getPaiTO() {
		return paiTO;
	}

	public void setPaiTO(TipoCompetenciaTO paiTO) {
		this.paiTO = paiTO;
	}

	public void setNumeroFilhos(Integer numeroFilhos) {
		this.numeroFilhos = numeroFilhos;
	}

	public Integer getNumeroPais() {
		return numeroPais;
	}

	public void setNumeroPais(Integer numeroPais) {
		this.numeroPais = numeroPais;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdPai() {
		return idPai;
	}

	public void setIdPai(Long idPai) {
		this.idPai = idPai;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeCurto() {
		return nomeCurto;
	}

	public void setNomeCurto(String nomeCurto) {
		this.nomeCurto = nomeCurto;
	}
}
