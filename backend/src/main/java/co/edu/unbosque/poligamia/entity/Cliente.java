package co.edu.unbosque.poligamia.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {

	@Column(name = "cupo_total")
	private Double cupoTotal;
	@Column(name = "fecha_registro")
	private LocalDate fechaRegistro;
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	private List<Pareja> parejas;

	public Cliente() {
		// TODO Auto-generated constructor stub
	}

	public Cliente(Double cupoTotal, LocalDate fechaRegistro, List<Pareja> parejas) {
		super();
		this.cupoTotal = cupoTotal;
		this.fechaRegistro = fechaRegistro;
		this.parejas = parejas;
	}

	public Cliente(Long id, String primerNombre, String segundoNombre, @NotBlank String primerApellido,
			String segundoApellido, String numeroDocumento, @Email @NotBlank String correoElectronico,
			String contrasenia, Role role, Double cupoTotal, LocalDate fechaRegistro, List<Pareja> parejas) {
		super(id, primerNombre, segundoNombre, primerApellido, segundoApellido, numeroDocumento, correoElectronico,
				contrasenia, role);
		this.cupoTotal = cupoTotal;
		this.fechaRegistro = fechaRegistro;
		this.parejas = parejas;
	}

	public Double getCupoTotal() {
		return cupoTotal;
	}

	public void setCupoTotal(Double cupoTotal) {
		this.cupoTotal = cupoTotal;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public List<Pareja> getParejas() {
		return parejas;
	}

	public void setParejas(List<Pareja> parejas) {
		this.parejas = parejas;
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
