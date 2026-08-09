package co.edu.unbosque.poligamia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CompraDTO {


    private Long id;


    private Long idPareja;


    private Long idAlmacen;


    private double montoTransaccion;

    private LocalDate fecha;

    private LocalTime hora;

    private String estado;


    public CompraDTO() {
    }

    public CompraDTO(Long idPareja, Long idAlmacen, double montoTransaccion,
                  LocalDate fecha, LocalTime hora) {
        this.idPareja = idPareja;
        this.idAlmacen = idAlmacen;
        this.montoTransaccion = montoTransaccion;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPareja() {
        return idPareja;
    }

    public void setIdPareja(Long idPareja) {
        this.idPareja = idPareja;
    }

    public Long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public double getMontoTransaccion() {
        return montoTransaccion;
    }

    public void setMontoTransaccion(double montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	@Override
	public String toString() {
	    return "CompraDTO{id=" + id + ", idPareja=" + idPareja + ", idAlmacen=" + idAlmacen +
	           ", montoTransaccion=" + montoTransaccion + ", fecha=" + fecha + ", hora=" + hora +
	           ", estado=" + estado + "}";
	}

}