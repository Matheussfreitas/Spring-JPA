package com.spring.project.ProjetoSpring.dto;

import com.spring.project.ProjetoSpring.model.Categoria;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacoes,
                       Categoria genero,
                       String atores,
                       String poster,
                       String sinopse) {
}
