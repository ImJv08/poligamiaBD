package co.edu.unbosque.poligamia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class RestriccionDTO {

	private Long id;
	private Long parejaId;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private boolean activa;
	private String motivo;

	public RestriccionDTO() {
	}

	public RestriccionDTO(Long id, Long parejaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
			boolean activa, String motivo) {
		this.id = id;
		this.parejaId = parejaId;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.activa = activa;
		this.motivo = motivo;
	}

	// Getters y setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParejaId() {
		return parejaId;
	}

	public void setParejaId(Long parejaId) {
		this.parejaId = parejaId;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}