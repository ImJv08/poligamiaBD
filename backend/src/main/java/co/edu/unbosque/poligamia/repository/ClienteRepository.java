package co.edu.unbosque.poligamia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.poligamia.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	@Override
	public Optional<Cliente> findById(Long id);

	public Optional<Cliente> findByCorreoElectronico(String cliente);

	public boolean existsByCorreoElectronico(String cliente);

}
