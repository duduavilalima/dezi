package br.com.fasolti.avd.transfer;

import br.com.fasolti.commons.client.transfer.TransferObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DadoPreenchimentoTotaisTO implements TransferObject {
	@JsonIgnore
	private Long id;

	private String campanha;
	private String avaliacao;

	private String responsavel;

	private String tipoCompetencia;
	private String competencia;

	private long preenchidasAuto;
	private long parciaisAuto;
	private long naoPreenchidasAuto;

	private long preenchidasGestor;
	private long parciaisGestor;
	private long naoPreenchidasGestor;

	private long preenchidasConsenso;
	private long parciaisConsenso;
	private long naoPreenchidasConsenso;

	private long preenchidasSubordinados;
	private long parciaisSubordinados;
	private long naoPreenchidasSubordinados;

	private long preenchidasPares;
	private long parciaisPares;
	private long naoPreenchidasPares;

	private long preenchidasAvaliacoes;
	private long parciaisAvaliacoes;
	private long naoPreenchidasAvaliacoes;
}
