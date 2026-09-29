package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.service.domain.FaseGraficoAvaliacao;
import br.com.fasolti.avd.transfer.util.BaseTO;

import java.util.Date;

public class EstatisticaTO extends BaseTO {

	private FaseGraficoAvaliacao tipoGrafico;

	private CampanhaTO campanha;

	private AvaliacaoTO avaliacao;

	private DepartamentoTO departamento;

	private CargoTO cargo;

//	private TipoTO tipo;

	private FuncionarioTO avaliador;

	private FuncionarioTO revisor;

	private Integer idadeDe;

	private Integer idadeAte;

	private ConceitoTO conceito;

	private Date admissaoDe;

	private Date cargoDe;

	public FaseGraficoAvaliacao getTipoGrafico() {
		return tipoGrafico;
	}

	public void setTipoGrafico(FaseGraficoAvaliacao tipoGrafico) {
		this.tipoGrafico = tipoGrafico;
	}

	public CampanhaTO getCampanha() {
		return campanha;
	}

	public void setCampanha(CampanhaTO campanha) {
		this.campanha = campanha;
	}

	public AvaliacaoTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public DepartamentoTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoTO departamento) {
		this.departamento = departamento;
	}

	public CargoTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoTO cargo) {
		this.cargo = cargo;
	}

//	public TipoTO getTipo() {
//		return tipo;
//	}
//
//	public void setTipo(TipoTO tipo) {
//		this.tipo = tipo;
//	}

	public FuncionarioTO getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(FuncionarioTO avaliador) {
		this.avaliador = avaliador;
	}

	public FuncionarioTO getRevisor() {
		return revisor;
	}

	public void setRevisor(FuncionarioTO revisor) {
		this.revisor = revisor;
	}

	public Integer getIdadeDe() {
		return idadeDe;
	}

	public void setIdadeDe(Integer idadeDe) {
		this.idadeDe = idadeDe;
	}

	public Integer getIdadeAte() {
		return idadeAte;
	}

	public void setIdadeAte(Integer idadeAte) {
		this.idadeAte = idadeAte;
	}

	public ConceitoTO getConceito() {
		return conceito;
	}

	public void setConceito(ConceitoTO conceito) {
		this.conceito = conceito;
	}

	public Date getAdmissaoDe() {
		return admissaoDe;
	}

	public void setAdmissaoDe(Date admissaoDe) {
		this.admissaoDe = admissaoDe;
	}

	public Date getCargoDe() {
		return cargoDe;
	}

	public void setCargoDe(Date cargoDe) {
		this.cargoDe = cargoDe;
	}
}
