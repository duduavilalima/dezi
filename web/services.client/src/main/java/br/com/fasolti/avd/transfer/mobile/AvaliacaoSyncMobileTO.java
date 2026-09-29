package br.com.fasolti.avd.transfer.mobile;

import java.util.Date;
import java.util.List;

public class AvaliacaoSyncMobileTO {

	private List<AvaliacaoMobileTO> listaAvaliacoes;
	private Long idAvaliador;
	private Date dataUltimaSync;
	private Date dataInicioSync;
	private Date dataFimSync;

	public List<AvaliacaoMobileTO> getListaAvaliacoes() {
		return listaAvaliacoes;
	}

	public void setListaAvaliacoes(List<AvaliacaoMobileTO> listaAvaliacoes) {
		this.listaAvaliacoes = listaAvaliacoes;
	}

	public Long getIdAvaliador() {
		return idAvaliador;
	}

	public void setIdAvaliador(Long idAvaliador) {
		this.idAvaliador = idAvaliador;
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
}
