package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.avd.types.SituacaoPreenchimento;
import br.com.fasolti.commons.client.validation.Validation;
import br.com.fasolti.commons.util.TransactionalObject;

import java.util.Date;
import java.util.List;

public class AvaliacaoTO extends BaseAuditedTO implements TransactionalObject<SituacaoAvaliacao> {

	@Validation(name = "descrição", notNull = true)
	private String descricao;

	@Validation(name = "descrição abreviada", max = 100, notNull = true)
	private String descricaoAbreviada;

	private String emailPadraoDaAvaliacao;

	private Date limiteAv1;

	private Date limiteAv2;

	private Date limiteAv3;

	private Date limiteAv4;

	private Boolean preencheAv1;

	private Boolean preencheAv2;

	private Boolean preencheAv3;

	private Boolean preencheAv4;

	private Boolean autoavaliacao;

	private Boolean gestores;

	private Boolean subordinados;

	private Boolean pares;

	private Boolean avHomeOffice;

	private Boolean emLiberacao;

	private EmpresaSimpleTO empresa;

	@Validation(name = "campanha", notNull = true)
	private CampanhaTO campanha;

	private SituacaoAvaliacao situacao;

	private SituacaoPreenchimento situacaoPreenchimento;

	private Long numParticipantes;

	private Long numAvaliacoes;

	//este atributo é usado para o gráfico de colaborador e/ou cargo.
	private FuncionarioTO colaborador;

	private List<ParticipanteTO> participantes;

	private List<TipoCompetenciaTO> tiposCompetencia;

	private List<AvaliacaoTipoCompetenciaTO> agentes;

	private List<PendenciaTO> pendencias;

	private List<AvaliacaoDimensaoTO> listaDimensao;

	private Long percentualTotalPreenchimento;

	public Long getPercentualTotalPreenchimento() {
		return percentualTotalPreenchimento;
	}

	public void setPercentualTotalPreenchimento(Long percentualTotalPreenchimento) {
		this.percentualTotalPreenchimento = percentualTotalPreenchimento;
	}

	public Boolean getEmLiberacao() {
		return emLiberacao;
	}

	public void setEmLiberacao(Boolean emLiberacao) {
		this.emLiberacao = emLiberacao;
	}

	public Boolean getAvHomeOffice() {
		return avHomeOffice;
	}

	public void setAvHomeOffice(Boolean avHomeOffice) {
		this.avHomeOffice = avHomeOffice;
	}

	public SituacaoAvaliacao getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoAvaliacao situacao) {
		this.situacao = situacao;
	}

	public AvaliacaoTO() {
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getEmailPadraoDaAvaliacao() {
		return emailPadraoDaAvaliacao;
	}

	public void setEmailPadraoDaAvaliacao(String emailPadraoDaAvaliacao) {
		this.emailPadraoDaAvaliacao = emailPadraoDaAvaliacao;
	}

	public Date getLimiteAv1() {
		return limiteAv1;
	}

	public void setLimiteAv1(Date limiteAv1) {
		this.limiteAv1 = limiteAv1;
	}

	public Date getLimiteAv2() {
		return limiteAv2;
	}

	public void setLimiteAv2(Date limiteAv2) {
		this.limiteAv2 = limiteAv2;
	}

	public Date getLimiteAv3() {
		return limiteAv3;
	}

	public void setLimiteAv3(Date limiteAv3) {
		this.limiteAv3 = limiteAv3;
	}

	public Date getLimiteAv4() {
		return limiteAv4;
	}

	public void setLimiteAv4(Date limiteAv4) {
		this.limiteAv4 = limiteAv4;
	}

	public Boolean getPreencheAv1() {
		return preencheAv1;
	}

	public void setPreencheAv1(Boolean preencheAv1) {
		this.preencheAv1 = preencheAv1;
	}

	public Boolean getPreencheAv2() {
		return preencheAv2;
	}

	public void setPreencheAv2(Boolean preencheAv2) {
		this.preencheAv2 = preencheAv2;
	}

	public Boolean getPreencheAv3() {
		return preencheAv3;
	}

	public void setPreencheAv3(Boolean preencheAv3) {
		this.preencheAv3 = preencheAv3;
	}

	public Boolean getPreencheAv4() {
		return preencheAv4;
	}

	public void setPreencheAv4(Boolean preencheAv4) {
		this.preencheAv4 = preencheAv4;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public CampanhaTO getCampanha() {
		return campanha;
	}

	public void setCampanha(CampanhaTO campanha) {
		this.campanha = campanha;
	}

	@Override
	public SituacaoAvaliacao getStatus() {
		return situacao;
	}

	@Override
	public void setStatus(SituacaoAvaliacao status) {
		this.situacao = status;
	}

	public SituacaoPreenchimento getSituacaoPreenchimento() {
		return situacaoPreenchimento;
	}

	public void setSituacaoPreenchimento(SituacaoPreenchimento situacaoPreenchimento) {
		this.situacaoPreenchimento = situacaoPreenchimento;
	}

	public Long getNumParticipantes() {
		return numParticipantes;
	}

	public void setNumParticipantes(Long numParticipantes) {
		this.numParticipantes = numParticipantes;
	}

	public FuncionarioTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(FuncionarioTO colaborador) {
		this.colaborador = colaborador;
	}

	public List<ParticipanteTO> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<ParticipanteTO> participantes) {
		this.participantes = participantes;
	}

	public List<TipoCompetenciaTO> getTiposCompetencia() {
		return tiposCompetencia;
	}

	public void setTiposCompetencia(List<TipoCompetenciaTO> tiposCompetencia) {
		this.tiposCompetencia = tiposCompetencia;
	}

	public List<AvaliacaoTipoCompetenciaTO> getAgentes() {
		return agentes;
	}

	public void setAgentes(List<AvaliacaoTipoCompetenciaTO> agentes) {
		this.agentes = agentes;
	}

	public Boolean getAutoavaliacao() {
		return autoavaliacao;
	}

	public void setAutoavaliacao(Boolean autoavaliacao) {
		this.autoavaliacao = autoavaliacao;
	}

	public Boolean getGestores() {
		return gestores;
	}

	public void setGestores(Boolean gestores) {
		this.gestores = gestores;
	}

	public Boolean getSubordinados() {
		return subordinados;
	}

	public void setSubordinados(Boolean subordinados) {
		this.subordinados = subordinados;
	}

	public Boolean getPares() {
		return pares;
	}

	public void setPares(Boolean pares) {
		this.pares = pares;
	}

	public Long getNumAvaliacoes() {
		return numAvaliacoes;
	}

	public void setNumAvaliacoes(Long numAvaliacoes) {
		this.numAvaliacoes = numAvaliacoes;
	}

	public List<PendenciaTO> getPendencias() {
		return pendencias;
	}

	public void setPendencias(List<PendenciaTO> pendencias) {
		this.pendencias = pendencias;
	}

	public List<AvaliacaoDimensaoTO> getListaDimensao() {
		return listaDimensao;
	}

	public void setListaDimensao(List<AvaliacaoDimensaoTO> listaDimensao) {
		this.listaDimensao = listaDimensao;
	}
}
