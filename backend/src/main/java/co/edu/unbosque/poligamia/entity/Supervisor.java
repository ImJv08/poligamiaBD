package co.edu.unbosque.poligamia.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name = "supervisores")
public class Supervisor extends Usuario {

	@ManyToOne
	@JoinColumn(name = "id_almacen")
	private Almacen almacen;

	public Supervisor() {
		// TODO Auto-generated constructor stub
	}

	public Supervisor(Almacen almacen) {
		super();
		this.almacen = almacen;
	}

	public Supervisor(Long id, String primerNombre, String segundoNombre, @NotBlank String primerApellido,
			String segundoApellido, String numeroDocumento, @Email @NotBlank String correoElectronico,
			String contrasenia, Role role, Almacen almacen) {
		super(id, primerNombre, segundoNombre, primerApellido, segundoApellido, numeroDocumento, correoElectronico,
				contrasenia, role);
		this.almacen = almacen;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	@Override
	public String toString() {
		return "Supervisor [almacen=" + almacen + "]";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return List.of(
	        new SimpleGrantedAuthority("ROLE_" + role.name())
	    );
	}


	@Override
	public String getPassword() {
		return contrasenia;
	}

	@Override
	public String getUsername() {
		return correoElectronico;
	}




}
