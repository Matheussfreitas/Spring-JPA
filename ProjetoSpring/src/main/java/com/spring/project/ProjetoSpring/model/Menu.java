package com.spring.project.ProjetoSpring.model;

import com.spring.project.ProjetoSpring.service.ConsumirAPI;
import com.spring.project.ProjetoSpring.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private ConsumirAPI consumir = new ConsumirAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t="; //declara constantes
    private final String API_KEY = "&apikey=5ce2bbab"; //declara constantes

    public void exibirMenu() {
        //realiza a requisição da api com base no nome da serie
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = scanner.nextLine();
        var json = consumir.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        System.out.println("-------------------");

        List<DadosTemporada> temporadas = new ArrayList<>();
        //printa uma lista de temporadas com todas as informações da temporada
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumir.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + "&apikey=5ce2bbab");
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        System.out.println("-------------------");

        //printa o nome de todos os titulos dos episodios
        System.out.println("Lista de todos os episódios da série: ");
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //coleta todos os episodios da serie e joga em uma lista
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("-------------------");

        //identifica e printa os 5 melhores episodios da serie
        System.out.println("Top 5 episódios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                //.peek(e -> System.out.println("Primeiro filtro N/A " + e))  mostra um passo a passo (debug) de um stream
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                //.peek(e -> System.out.println("Ordenação " + e))
                .limit(5)
                //.peek(e -> System.out.println("Limite " + e))
                .forEach(System.out::println);

        System.out.println("-------------------");

        //lista de todos os episodios junto com os detalhes de cada um
        System.out.println("Lista de episódios detalhada: ");
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemporada(), d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);

        System.out.println("-------------------");

        //busca por um trecho de episodio no meio de todos os episodios
        System.out.println("Digite o trecho do nome de um episódio que queira encontrar: ");
        var trechoTitulo = scanner.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado!");
            System.out.println("Episódio: " + episodioBuscado.get().getTitulo() + (" | Temporada: ") + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }

        System.out.println("-------------------");

        //traz estatisticas de avaliação das temporadas
        DoubleSummaryStatistics estatisticas = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Estatísticas de " + nomeSerie);
        System.out.println("Média: " + estatisticas.getAverage());
        System.out.println("Melhor episódio: " + estatisticas.getMax());
        System.out.println("Pior episódio: " + estatisticas.getMin());
        System.out.println("Total de episódios avaliados: " + estatisticas.getCount());

        System.out.println("-------------------");

        //busca todos os episodios lançados a partir do ano informado
        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var ano = scanner.nextInt();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                ", Episódio: " + e.getTitulo() +
                                ", Data de lançamento: " + e.getDataLancamento().format(formatador)
                ));
    }
}

