package co.edu.unbosque.poligamia.dto;

import co.edu.unbosque.poligamia.entity.Almacen;
import co.edu.unbosque.poligamia.entity.Role;

public class SupervisorDTO extends UsuarioDTO {


	private Almacen almacen;

	public SupervisorDTO() {
		// TODO Auto-generated constructor stub
	}

	public SupervisorDTO(Almacen almacen) {
		super();
		this.almacen = almacen;
	}

	public SupervisorDTO(Long id, String primerNombre, String segundoNombre, String primerApellido,
			String segundoApellido, String numeroDocumento, String correoElectronico, String contrasenia, Role role,
			Almacen almacen) {
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




}
