package com.spring.project.ProjetoSpring.main;

import com.spring.project.ProjetoSpring.model.DadosSerie;
import com.spring.project.ProjetoSpring.service.ConsumirAPI;
import com.spring.project.ProjetoSpring.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoSpringApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumirApi = new ConsumirAPI();
		var json = consumirApi.obterDados("https://www.omdbapi.com/?t=white+collar&apikey=5ce2bbab");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
