package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

import java.util.Date;

public class BiParametroUsuarioTO extends BaseTO implements SearchObject {

	private Long idEmpresa;
	private Long idFuncinario;
	private Long idCampanha;
	private String campanhaDescricao;
	private Date dataCricao;
	private String chave;
	private String nomeGestor;
	private String nomeFuncionario;

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdFuncinario() {
		return idFuncinario;
	}

	public void setIdFuncinario(Long idFuncinario) {
		this.idFuncinario = idFuncinario;
	}

	public Long getIdCampanha() {
		return idCampanha;
	}

	public void setIdCampanha(Long idCampanha) {
		this.idCampanha = idCampanha;
	}

	public String getCampanhaDescricao() {
		return campanhaDescricao;
	}

	public void setCampanhaDescricao(String campanhaDescricao) {
		this.campanhaDescricao = campanhaDescricao;
	}

	public Date getDataCricao() {
		return dataCricao;
	}

	public void setDataCricao(Date dataCricao) {
		this.dataCricao = dataCricao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getNomeGestor() {
		return nomeGestor;
	}

	public void setNomeGestor(String nomeGestor) {
		this.nomeGestor = nomeGestor;
	}

	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
}
