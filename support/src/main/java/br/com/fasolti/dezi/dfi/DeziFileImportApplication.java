package br.com.fasolti.dezi.dfi;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import br.com.fasolti.dezi.dfi.services.impl.ExcelReaderServiceImpl;

@SpringBootApplication
@EntityScan(basePackages = "br.com.fasolti.avd.domain")
@ComponentScan(basePackages = "br.com.fasolti.dezi.dfi")
@EnableJpaRepositories(basePackages = "br.com.fasolti.dezi.dfi.repositories")
public class DeziFileImportApplication {

	@Bean
	CommandLineRunner init(ExcelReaderServiceImpl fr) {
		return args -> {
			initT(fr);
			System.exit(0);
		};
	}

	private void initT(ExcelReaderServiceImpl fr) {
		try {
			fr.getFileXLSX();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DeziFileImportApplication.class, args);
	}

}
