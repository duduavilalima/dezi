package br.com.fasolti.queue.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.fasolti.queue.entities.EmailControle;
import br.com.fasolti.queue.repositories.EmailControleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmailControleServiceTest {

	@MockBean
	private EmailControleRepository emailControleRepository;

	@Autowired
	private EmailControleService emailControleService;

	private static final String emailteste = "email@email.com";

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.emailControleRepository.findByEmail(ArgumentMatchers.anyString()))
				.willReturn(new EmailControle());
		BDDMockito.given(this.emailControleRepository.save(ArgumentMatchers.any(EmailControle.class)))
				.willReturn(new EmailControle());
	}

	@Test
	public void testBuscaPorEmail() {
		Optional<EmailControle> emailControle = this.emailControleService.buscarPorEmail(emailteste);
		assertTrue(emailControle.isPresent());
	}

	@Test
	public void testPersistirEmail() {
		EmailControle emailControle = this.emailControleService.save(new EmailControle());
		assertNotNull(emailControle);
	}

}