package co.edu.unbosque.poligamia.dto;

import co.edu.unbosque.poligamia.entity.Role;

public abstract class UsuarioDTO {

	private Long id;
	private String primerNombre;
	private String segundoNombre;

	private String primerApellido;

	private String segundoApellido;

	private String numeroDocumento;

	private String correoElectronico;

	private String contrasenia;

	private Role role;

	public UsuarioDTO() {
		// TODO Auto-generated constructor stub
	}

	public UsuarioDTO(Long id, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
			String numeroDocumento, String correoElectronico, String contrasenia, Role role) {
		super();
		this.id = id;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.numeroDocumento = numeroDocumento;
		this.correoElectronico = correoElectronico;
		this.contrasenia = contrasenia;
		this.role = role;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public String getContrasenia() {
		return contrasenia;
	}
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}



	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", primerNombre=" + primerNombre + ", segundoNombre=" + segundoNombre
				+ ", primerApellido=" + primerApellido + ", segundoApellido=" + segundoApellido + ", numeroDocumento="
				+ numeroDocumento + ", correoElectronico=" + correoElectronico + ", contrasenia=" + contrasenia + "]";
	}








}
