package co.edu.unbosque.poligamia.dto;

public class SobreCupoDTO {


    private Long id;


    private Long idCompra;

    private Long idSupervisor;


    private Long IdClienteTitular;


    private double montoAutorizado;


    private Boolean autorizadoCliente;

    private Boolean aprobadoSupervisor;
    
    private double  montoAdicional;



    public SobreCupoDTO() {
    }



    public SobreCupoDTO(Long idCompra, Long idSupervisor, Long idClienteTitular, double montoAutorizado,
			Boolean autorizadoCliente, Boolean aprobadoSupervisor) {
		super();
		this.idCompra = idCompra;
		this.idSupervisor = idSupervisor;
		IdClienteTitular = idClienteTitular;
		this.montoAutorizado = montoAutorizado;
		this.autorizadoCliente = autorizadoCliente;
		this.aprobadoSupervisor = aprobadoSupervisor;
	}



	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public double getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(double d) {
        this.montoAutorizado = d;
    }



	public Long getIdCompra() {
		return idCompra;
	}



	public void setIdCompra(Long idCompra) {
		this.idCompra = idCompra;
	}



	public Long getIdSupervisor() {
		return idSupervisor;
	}



	public void setIdSupervisor(Long idSupervisor) {
		this.idSupervisor = idSupervisor;
	}



	public Long getIdClienteTitular() {
		return IdClienteTitular;
	}



	public void setIdClienteTitular(Long idClienteTitular) {
		IdClienteTitular = idClienteTitular;
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



	public double getMontoAdicional() {
		return montoAdicional;
	}



	public void setMontoAdicional(double montoAdicional) {
		this.montoAdicional = montoAdicional;
	}




}
