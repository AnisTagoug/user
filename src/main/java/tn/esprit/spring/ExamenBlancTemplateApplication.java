package tn.esprit.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tn.esprit.spring.entities.AppRole;
import tn.esprit.spring.services.AccountService;

import java.util.stream.Stream;

@SpringBootApplication
@EnableScheduling
public class ExamenBlancTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamenBlancTemplateApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder getBCPE(){
		return new BCryptPasswordEncoder();
	}


}
