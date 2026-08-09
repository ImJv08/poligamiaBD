package co.edu.unbosque.poligamia.configuration;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.poligamia.entity.Administrador;
import co.edu.unbosque.poligamia.entity.Almacen;
import co.edu.unbosque.poligamia.entity.Role;
import co.edu.unbosque.poligamia.entity.Supervisor;
import co.edu.unbosque.poligamia.entity.Usuario;
import co.edu.unbosque.poligamia.repository.AlmacenRepository;
import co.edu.unbosque.poligamia.repository.UsuarioRepository;


@Configuration
public class LoadDatabase {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AlmacenRepository almacenRepo;

	@Bean
	CommandLineRunner initDatabase(UsuarioRepository userRepo) {

	    return args -> {



	        Optional<Usuario> foundAdmin = userRepo.findByCorreoElectronico("admin@poligamia.com");

	        if (foundAdmin.isEmpty()) {

	            Administrador admin = new Administrador();

	            admin.setPrimerNombre("Carlos");
	            admin.setSegundoNombre("Andrés");
	            admin.setPrimerApellido("Gómez");
	            admin.setSegundoApellido("López");
	            admin.setNumeroDocumento("1001234567");
	            admin.setCorreoElectronico("admin@poligamia.com");
	            admin.setContrasenia(passwordEncoder.encode("Admin123*"));
	            admin.setRole(Role.ADMINISTRADOR);

	            userRepo.save(admin);

	            System.out.println("Administrador precargado.");
	        } else {
	            System.out.println("El administrador ya existe.");
	        }

	        Optional<Usuario> foundSupervisor =
	                userRepo.findByCorreoElectronico("supervisor@poligamia.com");

	        if (foundSupervisor.isEmpty()) {

	            Optional<Almacen> almacen = almacenRepo.findById(1L);

	            if (almacen.isEmpty()) {
	                System.out.println("No existe el almacén con id 1.");
	                return;
	            }

	            Supervisor supervisor = new Supervisor();

	            supervisor.setPrimerNombre("Laura");
	            supervisor.setSegundoNombre("Marcela");
	            supervisor.setPrimerApellido("Rodríguez");
	            supervisor.setSegundoApellido("Torres");
	            supervisor.setNumeroDocumento("1019876543");
	            supervisor.setCorreoElectronico("supervisor@poligamia.com");
	            supervisor.setContrasenia(passwordEncoder.encode("Supervisor123*"));
	            supervisor.setRole(Role.SUPERVISOR);
	            supervisor.setAlmacen(almacen.get());

	            userRepo.save(supervisor);

	            System.out.println("Supervisor precargado.");
	        } else {
	            System.out.println("El supervisor ya existe.");
	        }
	    };
	}
}
