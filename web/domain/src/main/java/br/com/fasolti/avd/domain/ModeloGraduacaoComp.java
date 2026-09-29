package br.com.fasolti.avd.domain;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "modelo_graduacao_comp")
public class ModeloGraduacaoComp extends BaseAuditedEntity{

	private static final long serialVersionUID = 667120262614108214L;

	@Column(name = "id_modelo_graduacao")
	private Long idModeloGraduacao;

	@Column(name = "descricao_modelo")
	private String descricaoModelo;

	@Column(name = "nao_graduar")
	private Boolean naoGraduar;

	@Column(name = "id_dimensao")
	private Long idDimensao;

	@Column(name = "id_empresa")
	private Long idEmpresa;

	@Column(name = "atualizador_original")
	private String atualizadorOriginal;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_atualizacao_original")
	private Date dataAtualizacaoOriginal;

	@Column(name = "id_avaliacao")
	private Long idAvaliacao;


	public Long getIdModeloGraduacao() {
		return idModeloGraduacao;
	}

	public void setIdModeloGraduacao(Long idModeloGraduacao) {
		this.idModeloGraduacao = idModeloGraduacao;
	}

	public String getDescricaoModelo() {
		return descricaoModelo;
	}

	public void setDescricaoModelo(String descricaoModelo) {
		this.descricaoModelo = descricaoModelo;
	}

	public Boolean getNaoGraduar() {
		return naoGraduar;
	}

	public void setNaoGraduar(Boolean naoGraduar) {
		this.naoGraduar = naoGraduar;
	}

	public Long getIdDimensao() {
		return idDimensao;
	}

	public void setIdDimensao(Long idDimensao) {
		this.idDimensao = idDimensao;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getAtualizadorOriginal() {
		return atualizadorOriginal;
	}

	public void setAtualizadorOriginal(String atualizadorOriginal) {
		this.atualizadorOriginal = atualizadorOriginal;
	}

	public Date getDataAtualizacaoOriginal() {
		return dataAtualizacaoOriginal;
	}

	public void setDataAtualizacaoOriginal(Date dataAtualizacaoOriginal) {
		this.dataAtualizacaoOriginal = dataAtualizacaoOriginal;
	}

	public Long getIdAvaliacao() {
		return idAvaliacao;
	}

	public void setIdAvaliacao(Long idAvaliacao) {
		this.idAvaliacao = idAvaliacao;
	}
}
