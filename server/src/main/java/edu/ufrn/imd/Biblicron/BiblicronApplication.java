package edu.ufrn.imd.Biblicron;

import edu.ufrn.imd.Biblicron.controller.LivroController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BiblicronApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiblicronApplication.class, args);

	}

}
