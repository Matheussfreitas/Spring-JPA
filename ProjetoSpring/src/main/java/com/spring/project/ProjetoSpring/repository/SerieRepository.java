package com.spring.project.ProjetoSpring.repository;

import com.spring.project.ProjetoSpring.model.Categoria;
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
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacoes >= :avaliacao")
    List<Serie> seriePorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);
}
