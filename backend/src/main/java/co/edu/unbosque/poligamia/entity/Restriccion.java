package co.edu.unbosque.poligamia.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "restricciones")
public class Restriccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pareja_id", nullable = false)
    private Pareja pareja;


    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;


    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;


    @NotNull
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;


    @Column(nullable = false)
    private boolean activa = true;


    @Size(max = 255)
    @Column(length = 255)
    private String motivo;

    public Restriccion() {
    }

    public Restriccion(Pareja pareja, LocalDate fecha, LocalTime horaInicio,
                       LocalTime horaFin, String motivo) {
        this.pareja = pareja;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.motivo = motivo;
        this.activa = true;
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
