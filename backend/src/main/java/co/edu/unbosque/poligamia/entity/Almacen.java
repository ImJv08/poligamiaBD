
package co.edu.unbosque.poligamia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "almacenes")
public class Almacen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String nombre;

	@NotBlank
	@Size(max = 200)
	@Column(nullable = false, length = 200)
	private String direccion;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String barrio;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String ciudad;


	public Almacen() {
	}

	public Almacen(String nombre, String direccion, String barrio, String ciudad) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.barrio = barrio;
		this.ciudad = ciudad;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

}
