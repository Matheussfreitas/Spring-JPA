package com.spring.project.ProjetoSpring;

import com.spring.project.ProjetoSpring.model.NovoMenu;

import com.spring.project.ProjetoSpring.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoSpringApplication implements CommandLineRunner {

//Essa notação é uma injeção de dependência para que seja possivel instanciar a interface do repositorio
//Ela precisa está sobre controle direto do spring
	@Autowired
	private SerieRepository repositorio;

	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringApplication.class, args);
	}

	@Override
	public void run(String... args) {
		NovoMenu menu = new NovoMenu(repositorio);
		menu.exibirMenu();
	}
}
