package br.com.alura.projetospring;

import br.com.alura.projetospring.model.DadosEpisodio;
import br.com.alura.projetospring.model.DadosSerie;
import br.com.alura.projetospring.service.ConsumoApi;
import br.com.alura.projetospring.service.ConverteDados;
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
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=peaky+blinders&Season=1&Episode=1&apikey=f11e75e");
		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);

		System.out.println(dados);
		System.out.println(dadosEpisodio);
	}
}
