package co.edu.unbosque.poligamia.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "compras")
public class Compra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pareja_id", nullable = false)
	private Pareja pareja;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "almacen_id", nullable = false)
	private Almacen almacen;

	@NotNull
	@Column(name = "monto_transaccion", nullable = false)
	private double montoTransaccion;

	@NotNull
	@Column(nullable = false)
	private LocalDate fecha;

	@NotNull
	@Column(nullable = false)
	private LocalTime hora;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EstadoCompra estado;


	public Compra() {
	}

	public Compra(Pareja pareja, Almacen almacen, double montoTransaccion, LocalDate fecha, LocalTime hora,
			EstadoCompra estado) {
		this.pareja = pareja;
		this.almacen = almacen;
		this.montoTransaccion = montoTransaccion;
		this.fecha = fecha;
		this.hora = hora;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pareja getPareja() {
		return pareja;
	}

	public void setPareja(Pareja pareja) {
		this.pareja = pareja;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
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

	public EstadoCompra getEstado() {
		return estado;
	}

	public void setEstado(EstadoCompra estado) {
		this.estado = estado;
	}
}