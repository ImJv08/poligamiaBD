package co.edu.unbosque.poligamia.dto;

import java.time.LocalDate;

import co.edu.unbosque.poligamia.entity.Role;

public class ClienteDTO extends UsuarioDTO {

	private Double cupoTotal;
	private LocalDate fechaRegistro;

	public ClienteDTO() {
	}

	public ClienteDTO(Long id, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
			String numeroDocumento, String correoElectronico, String contrasenia, Role role, Double cupoTotal,
			LocalDate fechaRegistro) {
		super(id, primerNombre, segundoNombre, primerApellido, segundoApellido, numeroDocumento, correoElectronico,
				contrasenia, role);
		this.cupoTotal = cupoTotal;
		this.fechaRegistro = fechaRegistro;
	}

	// Getters y setters
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
}