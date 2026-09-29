package br.com.fasolti.avd.transfer.mobile;

public class PreenchimentoSimpleMobileTO implements Comparable {

	private Long id;
	private Long idDetalhamento;
	private GraduacaoMobileTO graduacao;
	private FuncionarioMobileSimpleTO avaliado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GraduacaoMobileTO getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(GraduacaoMobileTO graduacao) {
		this.graduacao = graduacao;
	}

	public FuncionarioMobileSimpleTO getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(FuncionarioMobileSimpleTO avaliado) {
		this.avaliado = avaliado;
	}

	public Long getIdDetalhamento() {
		return idDetalhamento;
	}

	public void setIdDetalhamento(Long idDetalhamento) {
		this.idDetalhamento = idDetalhamento;
	}

	@Override
	public int compareTo(Object o) {
		int result = 0;
		if(getGraduacao() != null && ((PreenchimentoSimpleMobileTO) o).getGraduacao() != null) {
			result = (((PreenchimentoSimpleMobileTO) o).getGraduacao().getPonto()).compareTo(getGraduacao().getPonto());
		}

		if(getGraduacao() != null && ((PreenchimentoSimpleMobileTO) o).getGraduacao() == null){
			result = -1;
		}

		if(getGraduacao() == null && ((PreenchimentoSimpleMobileTO) o).getGraduacao() == null){
			result = getAvaliado().getNome().compareTo(((PreenchimentoSimpleMobileTO) o).getAvaliado().getNome());
		}

		return result;
	}
}
