package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "bi_parametro_usuario")
public class BiParametroUsuario extends BaseEntity {

	@Column(name = "id_empresa")
	private Long idEmpresa;

	@Column(name = "id_funcionario")
	private Long idFuncionario;

	@Column(name = "id_perfil")
	private Integer idPerfil;

	@Column(name = "id_campanha")
	private Long idCampanha;

	@Column(name = "campanha_descricao")
	private String campanhaDescricao;

	@Column(name = "nome_gestor")
	private String nomeGestor;

	@Column(name = "nome_funcionario")
	private String nomeFuncionario;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao")
	private Date dataCriacao;

	@Column(name = "chave")
	private String chave;

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
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

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
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

	@Override
	public String toString() {
		return "BiParametroUsuario{" +
				"idEmpresa=" + idEmpresa +
				", idFuncionario=" + idFuncionario +
				", idPerfil=" + idPerfil +
				", idCampanha=" + idCampanha +
				", campanhaDescricao='" + campanhaDescricao + '\'' +
				", nomeGestor='" + nomeGestor + '\'' +
				", nomeFuncionario='" + nomeFuncionario + '\'' +
				", dataCriacao=" + dataCriacao +
				", chave='" + chave + '\'' +
				'}';
	}
}
