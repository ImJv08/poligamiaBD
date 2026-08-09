package co.edu.unbosque.poligamia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.poligamia.entity.Pareja;

@Repository
public interface ParejaRepository extends JpaRepository<Pareja, Long> {

	Optional<Pareja> findByCorreoElectronico(String pareja);



	boolean existsByCorreoElectronico(String pareja);


	List<Pareja> findByClienteId(Long clienteId);
}