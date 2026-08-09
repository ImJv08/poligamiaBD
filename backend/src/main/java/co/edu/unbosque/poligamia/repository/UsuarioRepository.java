package co.edu.unbosque.poligamia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.poligamia.entity.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	public Optional<Usuario> findByCorreoElectronico(String usuario);
	public boolean existsByCorreoElectronico(String usuario);

}
