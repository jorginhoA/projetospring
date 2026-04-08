package br.com.alura.projetospring.interaction;

import br.com.alura.projetospring.model.DadosEpisodio;
import br.com.alura.projetospring.model.DadosSerie;
import br.com.alura.projetospring.model.DadosTemporada;
import br.com.alura.projetospring.model.Episodio;
import br.com.alura.projetospring.service.ConsumoApi;
import br.com.alura.projetospring.service.ConverteDados;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        System.out.println(dadosEpisodio);

        List<DadosTemporada> listaTemporadas = new ArrayList<>();

        for (int i = 1; i < dados.totalTemporadas(); i++){
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+ "&Season=" + i + APIKEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

            listaTemporadas.add(dadosTemporada);

        }
        listaTemporadas.forEach(System.out::println);

        List<DadosEpisodio> dadosEpisodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\n Top 5 episódios:");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemp(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getNumeroTempa() +
                                " Episódio: " + e.getTitulo() +
                                " Data de lançamento: " + e.getDataLancamento().format(formatador)
                ));


    }
}
