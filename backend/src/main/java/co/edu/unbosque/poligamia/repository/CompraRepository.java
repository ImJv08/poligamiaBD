package co.edu.unbosque.poligamia.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.poligamia.entity.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>{

	List<Compra> findByParejaId(Long idPareja);
	@Override
	Optional<Compra> findById(Long id);
	
	List<Compra> findByParejaClienteId(Long idCliente);

}
