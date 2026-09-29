package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoFuncFormacao;
import br.com.fasolti.avd.types.SituacaoFuncIdioma;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("situacao")
public class SituacaoResource {

	@RequestMapping(method = RequestMethod.GET, value = "funcionais")
	public SituacaoFuncionario[] funcionais() throws Exception {
		return SituacaoFuncionario.values();
	}

	@RequestMapping(method = RequestMethod.GET, value = "conclusoes")
	public SituacaoFuncFormacao[] conclusoes() throws Exception {
		return SituacaoFuncFormacao.values();
	}

	@RequestMapping(method = RequestMethod.GET, value = "niveis")
	public SituacaoFuncIdioma[] niveis() throws Exception {
		return SituacaoFuncIdioma.values();
	}

	@RequestMapping(method = RequestMethod.GET, value = "ativoorinativo")
	public Situacao[] ativoOrInativo() throws Exception {
		return Situacao.values();
	}

}
