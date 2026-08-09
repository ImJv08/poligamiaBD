package co.edu.unbosque.poligamia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.poligamia.entity.Administrador;
import co.edu.unbosque.poligamia.entity.Cliente;

public interface AdministradorRepository extends JpaRepository<Administrador, Long>{
	
	public Optional<Administrador> findByCorreoElectronico(String correoElectronico);

}
