package com.spring.project.ProjetoSpring.model;

public enum Categoria {
    ACAO("Action"),
    DRAMA("Drama"),
    COMEDIA("Comedy"),
    CRIME("Crime"),
    ROMANCE("Romance");

    private String categoriaOmdb;

    Categoria(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
