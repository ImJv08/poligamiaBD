package co.edu.unbosque.poligamia.dto;

import co.edu.unbosque.poligamia.entity.Role;

public class ParejaDTO extends UsuarioDTO {

	private Double cupoAsignado;
	private Long idCliente;
	private boolean primeraVez;

	public ParejaDTO() {
	}

	public ParejaDTO(Long id, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
			String numeroDocumento, String correoElectronico, String contrasenia, Role role, Double cupoAsignado,
			Long idCliente, boolean primeraVez) {
		super(id, primerNombre, segundoNombre, primerApellido, segundoApellido, numeroDocumento, correoElectronico,
				contrasenia, role);
		this.cupoAsignado = cupoAsignado;
		this.idCliente = idCliente;
		this.primeraVez = primeraVez;
	}

	// Getters y setters
	public Double getCupoAsignado() {
		return cupoAsignado;
	}

	public void setCupoAsignado(Double cupoAsignado) {
		this.cupoAsignado = cupoAsignado;
	}



	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public boolean isPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(boolean primeraVez) {
		this.primeraVez = primeraVez;
	}
}