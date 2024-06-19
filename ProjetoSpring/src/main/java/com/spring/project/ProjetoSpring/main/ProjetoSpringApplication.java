package com.spring.project.ProjetoSpring.main;

import com.spring.project.ProjetoSpring.model.NovoMenu;
import com.spring.project.ProjetoSpring.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.spring.project.ProjetoSpring.repository")
public class ProjetoSpringApplication implements CommandLineRunner {

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
