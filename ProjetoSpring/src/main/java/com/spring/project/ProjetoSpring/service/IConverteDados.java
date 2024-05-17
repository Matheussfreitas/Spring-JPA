package com.spring.project.ProjetoSpring.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
