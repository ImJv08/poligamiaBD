package co.edu.unbosque.poligamia.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import co.edu.unbosque.poligamia.entity.Role;
import co.edu.unbosque.poligamia.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AdministradorDTO extends Usuario{
	
	public AdministradorDTO() {
		// TODO Auto-generated constructor stub
	}


	public AdministradorDTO(Long id, String primerNombre, String segundoNombre, @NotBlank String primerApellido,
			String segundoApellido, String numeroDocumento, @Email @NotBlank String correoElectronico,
			String contrasenia, Role role) {
		super(id, primerNombre, segundoNombre, primerApellido, segundoApellido, numeroDocumento, correoElectronico,
				contrasenia, role);
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

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
