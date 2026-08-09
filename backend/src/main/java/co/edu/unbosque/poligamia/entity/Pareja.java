package co.edu.unbosque.poligamia.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "parejas")
public class Pareja extends Usuario {

	 @Column(name = "cupo_Asignado")
	private double cupoAsignado;


	 @ManyToOne
	 @JoinColumn(name = "id_cliente")
	 @JsonIgnoreProperties("parejas")
	 private Cliente cliente;

	 @Column(name = "primera_vez")
	 private boolean primeraVez;

	public Pareja() {
		// TODO Auto-generated constructor stub
	}

	public Pareja(Double cupoAsignado, Cliente cliente, boolean primeraVez) {
		super();
		this.cupoAsignado = cupoAsignado;
		this.cliente = cliente;
		this.primeraVez = primeraVez;
	}


	public Pareja(Long id, String primerNombre, String segundoNombre, @NotBlank String primerApellido,
			String segundoApellido, String numeroDocumento, @Email @NotBlank String correoElectronico,
			String contrasenia, Role role, double cupoAsignado, Cliente cliente, boolean primeraVez) {
		super(id, primerNombre, segundoNombre, primerApellido, segundoApellido, numeroDocumento, correoElectronico,
				contrasenia, role);
		this.cupoAsignado = cupoAsignado;
		this.cliente = cliente;
		this.primeraVez = primeraVez;
	}

	public double getCupoAsignado() {
		return cupoAsignado;
	}

	public void setCupoAsignado(double cupoAsignado) {
		this.cupoAsignado = cupoAsignado;
	}



	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public boolean isPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(boolean primeraVez) {
		this.primeraVez = primeraVez;
	}



	@Override
	public String toString() {
		return "Pareja [cupoAsignado=" + cupoAsignado;
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
