package co.edu.unbosque.poligamia.entity;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario implements UserDetails   {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@Column(name = "primer_nombre", nullable = false, length = 50)
	private String primerNombre;
	@Column(name = "segundo_nombre", length = 50)
	private String segundoNombre;

	@Column(name = "primer_apellido", nullable = false, length = 50)
	@NotBlank
	private String primerApellido;
	@Column(name = "segundo_apellido", length = 50)
	private String segundoApellido;
	@Column(name = "numero_documento", nullable = false, unique = true)
	private String numeroDocumento;
	@Email
	@NotBlank
	@Column(name = "correo_electronico", unique = true, nullable = false)
	protected String correoElectronico;
	@Column(nullable = false)
	protected String contrasenia;
	@Enumerated(EnumType.STRING)
	protected Role role;

	public Usuario() {
		// TODO Auto-generated constructor stub
	}


	public Usuario(Long id, String primerNombre, String segundoNombre, @NotBlank String primerApellido,
			String segundoApellido, String numeroDocumento, @Email @NotBlank String correoElectronico,
			String contrasenia, co.edu.unbosque.poligamia.entity.Role role) {
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
