package co.edu.unbosque.poligamia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sobrecupos")
public class SobreCupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compra_id", nullable = false, unique = true)
    private Compra compra;



    @ManyToOne(optional = true)
    @JoinColumn(name = "supervisor_id", nullable = true)
    private Supervisor supervisor;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente clienteTitular;

    @Column(name = "monto_adicional")
    private double  montoAdicional;

    @NotNull
    @Column(name = "monto_autorizado", nullable = false)
    private double montoAutorizado;

    @Column(name = "autorizado_cliente")
    private Boolean autorizadoCliente;

    @Column(name = "aprobado_supervisor")
    private Boolean aprobadoSupervisor;



    public SobreCupo() {
    }






	public SobreCupo(@NotNull Compra compra, Supervisor supervisor, Cliente clienteTitular, double montoAdicional,
			@NotNull double montoAutorizado, Boolean autorizadoCliente, Boolean aprobadoSupervisor) {
		super();
		this.compra = compra;
		this.supervisor = supervisor;
		this.clienteTitular = clienteTitular;
		this.montoAdicional = montoAdicional;
		this.montoAutorizado = montoAutorizado;
		this.autorizadoCliente = autorizadoCliente;
		this.aprobadoSupervisor = aprobadoSupervisor;
	}






	public double  getMontoAdicional() {
		return montoAdicional;
	}






	public double getMontoAutorizado() {
		return montoAutorizado;
	}






	public void setMontoAdicional(double  montoAdicional) {
		this.montoAdicional = montoAdicional;
	}






	public void setMontoAutorizado(double montoAutorizado) {
		this.montoAutorizado = montoAutorizado;
	}






	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public Cliente getClienteTitular() {
        return clienteTitular;
    }

    public void setClienteTitular(Cliente clienteTitular) {
        this.clienteTitular = clienteTitular;
    }






	public Boolean getAutorizadoCliente() {
		return autorizadoCliente;
	}

	public void setAutorizadoCliente(Boolean autorizadoCliente) {
		this.autorizadoCliente = autorizadoCliente;
	}

	public Boolean getAprobadoSupervisor() {
		return aprobadoSupervisor;
	}

	public void setAprobadoSupervisor(Boolean aprobadoSupervisor) {
		this.aprobadoSupervisor = aprobadoSupervisor;
	}




}
