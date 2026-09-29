package br.com.fasolti.queue.commons;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailParametro implements Serializable {

	private static final long serialVersionUID = -7333716783800122677L;

	public EmailParametro() {
	}

	public EmailParametro(String id, int tentativa) {
		super();
		this.id = id;
		this.tentativa = tentativa;
	}

	@JsonProperty("id")
	private String id;

	@JsonProperty("tentativa")
	private int tentativa;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTentativa() {
		return tentativa;
	}

	public void setTentativa(int tentativa) {
		this.tentativa = tentativa;
	}

	@Override
	public String toString() {
		return "EmailParametro [id=" + id + ", tentativa=" + tentativa + "]";
	}

}
