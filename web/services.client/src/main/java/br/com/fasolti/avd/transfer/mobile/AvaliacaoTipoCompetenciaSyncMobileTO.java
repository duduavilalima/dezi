package br.com.fasolti.avd.transfer.mobile;

import java.util.Date;
import java.util.List;

public class AvaliacaoTipoCompetenciaSyncMobileTO {
	private List<AvaliacaoTipoCompetenciaMobileTO> listaAvaliacaoTipoCompetencias;
	private Date dataUltimaSync;
	private Date dataInicioSync;
	private Date dataFimSync;
	private Long idAvaliador;
	private Long idAvaliacao;

	public List<AvaliacaoTipoCompetenciaMobileTO> getListaAvaliacaoTipoCompetencias() {
		return listaAvaliacaoTipoCompetencias;
	}

	public void setListaAvaliacaoTipoCompetencias(List<AvaliacaoTipoCompetenciaMobileTO> listaAvaliacaoTipoCompetencias) {
		this.listaAvaliacaoTipoCompetencias = listaAvaliacaoTipoCompetencias;
	}

	public Date getDataUltimaSync() {
		return dataUltimaSync;
	}

	public void setDataUltimaSync(Date dataUltimaSync) {
		this.dataUltimaSync = dataUltimaSync;
	}

	public Date getDataInicioSync() {
		return dataInicioSync;
	}

	public void setDataInicioSync(Date dataInicioSync) {
		this.dataInicioSync = dataInicioSync;
	}

	public Date getDataFimSync() {
		return dataFimSync;
	}

	public void setDataFimSync(Date dataFimSync) {
		this.dataFimSync = dataFimSync;
	}

	public Long getIdAvaliador() {
		return idAvaliador;
	}

	public void setIdAvaliador(Long idAvaliador) {
		this.idAvaliador = idAvaliador;
	}

	public Long getIdAvaliacao() {
		return idAvaliacao;
	}

	public void setIdAvaliacao(Long idAvaliacao) {
		this.idAvaliacao = idAvaliacao;
	}
}
