package com.spring.project.ProjetoSpring.repository;

import com.spring.project.ProjetoSpring.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

//Essa interface vai herdar do JPA Repository, que já abstrairá todos os métodos necessários para realizarmos as operações básicas no banco.
public interface SerieRepository extends JpaRepository<Serie, Long> {
}
