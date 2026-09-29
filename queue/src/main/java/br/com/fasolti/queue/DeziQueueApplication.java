package br.com.fasolti.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EntityScan(basePackages = "br.com.fasolti.queue.entities")
@ComponentScan(basePackages = "br.com.fasolti.queue")
@EnableJpaRepositories(basePackages = "br.com.fasolti.queue.repositories")
@SpringBootApplication
@EnableScheduling
public class DeziQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeziQueueApplication.class, args);
	}
}
