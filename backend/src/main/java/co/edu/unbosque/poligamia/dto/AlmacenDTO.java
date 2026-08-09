
package co.edu.unbosque.poligamia.dto;

public class AlmacenDTO {


	    private Long id;

	    private String nombre;

	    private String direccion;
	    private String barrio;
	    private String ciudad;

	    //private List<Supervisor> supervisores = new ArrayList<>();

	    // Un ALMACEN tiene muchas COMPRAS

	    // List<Compra> compras = new ArrayList<>();

	    public AlmacenDTO() {
	    }

	    public AlmacenDTO(String nombre, String direccion, String barrio, String ciudad) {
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
