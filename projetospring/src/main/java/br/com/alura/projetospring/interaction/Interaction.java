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
import java.util.*;
import java.util.stream.Collectors;

public class Interaction {

    Scanner leitura = new Scanner(System.in);
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=" ;
    private final String APIKEY = "&apikey=f11e75e";

    public void exibeMenu() throws IOException, InterruptedException {

        var opcao = -1;
        while (opcao != 0 ){
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    
                    0 - Sair
                    """;

            System.out.println(menu);

            switch (opcao){
                case 1:
                    consultaDadosSerie();
                    break;
                case 2:
                    consultaEpisodiosSerie();
                    break;
                case 0:
                    System.out.println("Finalizando operação...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }




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


        // flatmap -> O flatMap é usado quando cada item gera uma coleção/lista
        // map -> O map pega um elemento e transforma em outro.
        List<DadosEpisodio> dadosEpisodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

//        System.out.println("\n Top 10 episódios:");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A ) " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);

        List<Episodio> episodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemp(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite o nome do episódio: ");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if (episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getNumeroTemp());
            System.out.println("Titulo do episódio: " + episodioBuscado.get().getTitulo());
        } else{
            System.out.println("Episódio não encontrado");
        }
//
//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getNumeroTempa() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data de lançamento: " + e.getDataLancamento().format(formatador)
//                ));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getNumeroTemp,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println("Avaliação por temporada: ");
        System.out.println(avaliacoesPorTemporada);

    }


    private DadosSerie consultaDadosSerie() throws IOException, InterruptedException {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace("", "+") +APIKEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void consultaEpisodiosSerie() throws IOException, InterruptedException {
        DadosSerie dadosSerie = consultaDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++){
            var json = consumoApi.obterDados(ENDERECO + dadosSerie.title().replace(" ", "+") + "&season=" + i + APIKEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }
}
