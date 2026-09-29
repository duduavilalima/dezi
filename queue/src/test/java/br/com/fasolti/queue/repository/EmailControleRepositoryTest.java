package br.com.fasolti.queue.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.fasolti.queue.entities.EmailControle;
import br.com.fasolti.queue.enuns.TipoEmailStatus;
import br.com.fasolti.queue.repositories.EmailControleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmailControleRepositoryTest {

	@Autowired
	private EmailControleRepository emailControleRepository;

	private static final String EMAIL = "email@email.com1";

	@Test
	public void testBuscarPorEmail() {
		EmailControle ec = this.emailControleRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, ec.getEmail());
	}

	@Test
	public void testBuscarPorEmailComErro() {
		List<EmailControle> list = this.emailControleRepository.findByStatus(TipoEmailStatus.ERRO);
		assertFalse(list.isEmpty());
	}

	@Test
	public void testBuscarPorEmailPendente() {
		List<EmailControle> list = this.emailControleRepository.findByStatus(TipoEmailStatus.PENDENTE);
		assertFalse(list.isEmpty());
	}

	@Before
	public void setUp() throws Exception {

		EmailControle ec = new EmailControle();

		ec.setAssunto("Teste assunto");
		ec.setCreatedBy("Criado Teste");
		ec.setCreationDate(new Date());
		ec.setEmail(EMAIL);
		ec.setIdDestinatario(10L);
		ec.setMensagem("Mensagem teste !");
		ec.setStatus(TipoEmailStatus.PENDENTE);
		emailControleRepository.save(ec);
		ec = new EmailControle();

		ec.setAssunto("Teste assunto1");
		ec.setCreatedBy("Criado Teste1");
		ec.setCreationDate(new Date());
		ec.setEmail(EMAIL + "1");
		ec.setIdDestinatario(11L);
		ec.setMensagem("Mensagem teste 1 !");
		ec.setMsgErro("Erro msg 1");
		ec.setStatus(TipoEmailStatus.ERRO);
		emailControleRepository.save(ec);
		ec = new EmailControle();

	}

	@After
	public final void tearDown() {
		this.emailControleRepository.deleteAll();
	}

}
