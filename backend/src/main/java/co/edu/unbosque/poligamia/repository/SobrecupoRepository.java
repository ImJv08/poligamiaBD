package co.edu.unbosque.poligamia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.poligamia.entity.SobreCupo;

@Repository
public interface SobrecupoRepository extends JpaRepository<SobreCupo, Long> {

	@Override
	Optional<SobreCupo> findById(Long id);
	
	SobreCupo findByCompraId(Long idCompra);

}
