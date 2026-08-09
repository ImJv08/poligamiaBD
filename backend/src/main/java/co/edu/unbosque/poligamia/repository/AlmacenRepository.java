package co.edu.unbosque.poligamia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.poligamia.entity.Almacen;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {

	@Override
	Optional<Almacen> findById(Long id);
}