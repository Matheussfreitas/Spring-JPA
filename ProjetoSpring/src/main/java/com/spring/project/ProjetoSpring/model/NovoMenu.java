package com.spring.project.ProjetoSpring.model;

import com.spring.project.ProjetoSpring.repository.SerieRepository;
import com.spring.project.ProjetoSpring.service.ConsumirAPI;
import com.spring.project.ProjetoSpring.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class NovoMenu {
    private Scanner scanner = new Scanner(System.in);
    private ConsumirAPI consumir = new ConsumirAPI();
    private ConverteDados conversor = new ConverteDados();
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private final String ENDERECO = "https://www.omdbapi.com/?t="; //declara constantes
    private final String API_KEY = "&apikey=5ce2bbab"; //declara constantes
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();

    public NovoMenu(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibirMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar séries por título
                    5 - Buscar séries por ator
                    6 - Top 5 séries
                    7 - Buscar série por categoria
                    8 - Filtrar séries
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    filtraSeriePorTemporadaEAvaliacao();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = scanner.nextLine();
        var json = consumir.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome");
        var nomeSerie = scanner.nextLine();

//        Optional<Serie> serie = series.stream()
//                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
//                .findFirst();
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumir.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll(); //retorna automaticamente o que estiver no banco
//        series = dadosSeries.stream()
//                .map(d -> new Serie(d))
//                .collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha um série pelo nome: ");
        var nomeSerie = scanner.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());

        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Informe o nome para busca: ");
        var nomeAtor = scanner.nextLine();
        System.out.println("Avaliação a partir de qual valor? ");
        var avaliacao = scanner.nextDouble();
        List<Serie> serieEncontrada = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacoesGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        serieEncontrada.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacoes()) );
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacoesDesc();
        serieTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacoes()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Informe a categoria/gênero: ");
        var nomeGenero = scanner.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria: " + nomeGenero);
        seriePorCategoria.forEach(System.out::println);
    }

//    private void filtraSeriePorTemporadaEAvaliacao() {
//        System.out.println("informe a quantidade de temporadas: ");
//        var temporadas = scanner.nextInt();
//        System.out.println("Informe a avaliação mínima: ");
//        var avaliacoes = scanner.nextDouble();
//        List<Serie> filtroSerie = repositorio.findByTotalTemporadasLessThanEqualAndAvaliacoesGreaterThanEqual(temporadas, avaliacoes);
//        filtroSerie.forEach(System.out::println);
//    }

    private void filtraSeriePorTemporadaEAvaliacao() {
        System.out.println("informe a quantidade de temporadas: ");
        var temporadas = scanner.nextInt();
        System.out.println("Informe a avaliação mínima: ");
        var avaliacoes = scanner.nextDouble();
        List<Serie> filtroSerie = repositorio.seriePorTemporadaEAvaliacao(temporadas, avaliacoes);
        filtroSerie.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacoes()));
    }
}
