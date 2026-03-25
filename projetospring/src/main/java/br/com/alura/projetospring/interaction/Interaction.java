package br.com.alura.projetospring.interaction;

import br.com.alura.projetospring.model.DadosEpisodio;
import br.com.alura.projetospring.model.DadosSerie;
import br.com.alura.projetospring.model.DadosTemporada;
import br.com.alura.projetospring.service.ConsumoApi;
import br.com.alura.projetospring.service.ConverteDados;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Interaction {

    Scanner leitura = new Scanner(System.in);
    ConsumoApi consumoApi = new ConsumoApi();

    private final String ENDERECO = "https://www.omdbapi.com/?t=" ;
    private final String APIKEY = "&apikey=f11e75e";

    public void exibeMenu() throws IOException, InterruptedException {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        String json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APIKEY);

        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);

        System.out.println(dados);

        List<DadosTemporada> listaTemporadas = new ArrayList<>();

        for (int i = 1; i < dados.totalTemporadas(); i++){
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+ "&Season=" + i + APIKEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

            listaTemporadas.add(dadosTemporada);

        }
        listaTemporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++){
//            List <DadosEpisodio> episodiosTemporada = listaTemporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        listaTemporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}
