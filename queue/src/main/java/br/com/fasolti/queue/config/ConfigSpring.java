package br.com.fasolti.queue.config;

import java.nio.charset.StandardCharsets;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

import br.com.fasolti.queue.services.impl.EmailServiceImpl;

@Configuration
public class ConfigSpring {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigSpring.class);
	
	@Value("${spring.thymeleaf.prefix}")
	private String thymeleafPrefix;
	
	@Value("${spring.thymeleaf.suffix}")
	private String thymeleafSuffix;

	@Bean
	public JPAQueryFactory jPAQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}

	@Bean
	public SpringTemplateEngine springTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		return templateEngine;
	}

	@Bean
	public SpringResourceTemplateResolver htmlTemplateResolver() {
		log.info(String.format("Thymeleaf Prefix: %s", thymeleafPrefix));
		log.info(String.format("Thymeleaf Suffix: %s", thymeleafSuffix));
		SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
		emailTemplateResolver.setPrefix(thymeleafPrefix);
		emailTemplateResolver.setSuffix(thymeleafSuffix);
		emailTemplateResolver.setTemplateMode("HTML");
		emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		return emailTemplateResolver;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

		return mapper;
	}
}
