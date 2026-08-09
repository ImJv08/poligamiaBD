package co.edu.unbosque.poligamia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.poligamia.entity.Supervisor;


@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Long>{

	@Override
	public Optional<Supervisor> findById(Long id);
	public Optional<Supervisor> findByCorreoElectronico(String usuario);
	public boolean existsByCorreoElectronico(String usuario);




}
