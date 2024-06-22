package com.spring.project.ProjetoSpring.model;

import com.spring.project.ProjetoSpring.repository.SerieRepository;
import com.spring.project.ProjetoSpring.service.ConsumirAPI;
import com.spring.project.ProjetoSpring.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NovoMenu {
    private Scanner scanner = new Scanner(System.in);
    private ConsumirAPI consumir = new ConsumirAPI();
    private ConverteDados conversor = new ConverteDados();
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private final String ENDERECO = "https://www.omdbapi.com/?t="; //declara constantes
    private final String API_KEY = "&apikey=5ce2bbab"; //declara constantes
    private SerieRepository repositorio;

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
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumir.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
        List<Serie> series = repositorio.findAll(); //retorna automaticamente o que estiver no banco
//        series = dadosSeries.stream()
//                .map(d -> new Serie(d))
//                .collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
