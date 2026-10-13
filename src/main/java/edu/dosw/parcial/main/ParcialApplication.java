package edu.dosw.parcial.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

// Completado por Claude siguiendo la sección 4 de los prerrequisitos del parcial
// La clase vive en el subpaquete main, así que se indica el paquete base para que
// Spring encuentre controller, core, persistence y config.
@SpringBootApplication(scanBasePackages = "edu.dosw.parcial")
@EnableMongoRepositories(basePackages = "edu.dosw.parcial.persistence.repositories")
public class ParcialApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParcialApplication.class, args);
	}

}
