package com.spring.project.ProjetoSpring.repository;

import com.spring.project.ProjetoSpring.model.Categoria;
import com.spring.project.ProjetoSpring.model.Episodio;
import com.spring.project.ProjetoSpring.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//Essa interface vai herdar do JPA Repository, que já abstrairá todos os métodos necessários para realizarmos as operações básicas no banco.
public interface SerieRepository extends JpaRepository<Serie, Long> {
    //derived queries ou consultas derivadas facilitam a forma de trabalhar com consultas personalizadas ao banco de dados utilizando JPA
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacoesGreaterThanEqual(String nomeAtor, double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacoesDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacoesGreaterThanEqual(int totalTemporadas, double avaliacao);

    //introdução a JPQL
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacoes >= :avaliacao")
    List<Serie> seriePorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
