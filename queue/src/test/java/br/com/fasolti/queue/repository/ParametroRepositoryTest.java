package br.com.fasolti.queue.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.fasolti.queue.entities.Parametro;
import br.com.fasolti.queue.entities.QParametro;
import br.com.fasolti.queue.repositories.ParametroRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ParametroRepositoryTest {

	@Autowired
	private ParametroRepository parametroRepository;

	QParametro qParametro = QParametro.parametro;

	private static final String EMAIL_BEMVINDO_FUNCIONARIO_KEY = "template.bemvindo";
	private static final String EMAIL_TEMPL_BEMVINDO_FUNCIONARIO = "mail.template.bemvindo.funcionario";
	private static final String EMAIL_TEMPL_ESQUECEU_SENHA_FUNCIONARIO = "mail.template.esqueceu.senha.funcionario";

	@Test
	public void testBuscarPorChave() {
		Optional<Parametro> param = this.parametroRepository
				.findOne(qParametro.chave.eq(EMAIL_BEMVINDO_FUNCIONARIO_KEY));
		assertEquals(EMAIL_TEMPL_BEMVINDO_FUNCIONARIO, param.get().getValor());
	}

	@Test
	public void testBuscarTodos() {
		List<Parametro> list = (List<Parametro>) this.parametroRepository.findAll();
		assertFalse(list.isEmpty());
	}

	@Test
	public void testBuscarListPorParametros() {
		List<String> listKeyTest = new ArrayList<>();
		listKeyTest.add("key01");
		listKeyTest.add("key02");
		List<Parametro> list = (List<Parametro>) this.parametroRepository.findAll(qParametro.chave.in(listKeyTest));
		assertFalse(list.isEmpty());
	}

	@Before
	public void setUp() throws Exception {
		parametroRepository
				.save(new Parametro.ParametroBuilder(EMAIL_BEMVINDO_FUNCIONARIO_KEY, EMAIL_TEMPL_BEMVINDO_FUNCIONARIO)
						.build());
		parametroRepository
				.save(new Parametro.ParametroBuilder("template.esqueceusenha", EMAIL_TEMPL_ESQUECEU_SENHA_FUNCIONARIO)
						.build());
		parametroRepository.save(new Parametro.ParametroBuilder("key01", "value01").build());
		parametroRepository.save(new Parametro.ParametroBuilder("key02", "value02").build());
	}

	@After
	public final void tearDown() {
		this.parametroRepository.deleteAll();
	}

}
