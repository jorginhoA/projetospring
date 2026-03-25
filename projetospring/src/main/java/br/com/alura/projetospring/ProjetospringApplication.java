package br.com.alura.projetospring;

import br.com.alura.projetospring.interaction.Interaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetospringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProjetospringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Interaction interaction = new Interaction();
		interaction.exibeMenu();

	}
}
