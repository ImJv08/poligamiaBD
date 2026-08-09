package co.edu.unbosque.poligamia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.poligamia.dto.AlmacenDTO;
import co.edu.unbosque.poligamia.dto.SobreCupoDTO;
import co.edu.unbosque.poligamia.entity.Cliente;
import co.edu.unbosque.poligamia.entity.Pareja;
import co.edu.unbosque.poligamia.entity.SobreCupo;
import co.edu.unbosque.poligamia.entity.Supervisor;
import co.edu.unbosque.poligamia.repository.ClienteRepository;
import co.edu.unbosque.poligamia.repository.ParejaRepository;
import co.edu.unbosque.poligamia.repository.SobrecupoRepository;
import co.edu.unbosque.poligamia.repository.SupervisorRepository;

@Service
public class SobrecupoService {

	@Autowired
	private SobrecupoRepository sobrecupoRepo;

	@Autowired
	private SupervisorRepository supervisorRepo;

	@Autowired
	private ClienteRepository clienteRepo;

	@Autowired
	ParejaRepository parejaRepo;

	public List<SobreCupoDTO> mostrarTodo() {

		List<SobreCupo> entityList = sobrecupoRepo.findAll();
		List<SobreCupoDTO> dtoList = new ArrayList<>();

		for (SobreCupo entity : entityList) {
			SobreCupoDTO dto = new SobreCupoDTO();

			dto.setId(entity.getId());
			dto.setMontoAutorizado(entity.getMontoAutorizado());
			dto.setAutorizadoCliente(entity.getAutorizadoCliente());
			if (entity.getClienteTitular() != null) {
				dto.setIdClienteTitular(entity.getClienteTitular().getId());
			}

			if (entity.getSupervisor() != null) {
				dto.setIdSupervisor(entity.getSupervisor().getId());
			}
			dto.setIdCompra(entity.getCompra().getId());
			dto.setAprobadoSupervisor(entity.getAprobadoSupervisor());

			dtoList.add(dto);
		}

		return dtoList;

	}
	

	public int estadoSobrecupo(Long idSobrecupo) {
		Optional<SobreCupo> found = sobrecupoRepo.findById(idSobrecupo);
		if (found.isEmpty()) {
			return 1;
		}

		SobreCupo sobrecupo = found.get();

		if (sobrecupo.getAutorizadoCliente() == null || sobrecupo.getAprobadoSupervisor() == null) {
			return 2; // pendiente
		}

		if (sobrecupo.getAutorizadoCliente() && sobrecupo.getAprobadoSupervisor()) {
			return 3; // aprobado
		}

		return 4; // rechazado
	}

	public int aprobarSobrecupo(Long idSobrecupo, Long idSupervisor) {

		Optional<Supervisor> found = supervisorRepo.findById(idSupervisor);
		if (found.isEmpty()) {
			return 1;
		}
		Supervisor supervisor = found.get();

		Optional<SobreCupo> foundSobrecupo = sobrecupoRepo.findById(idSobrecupo);
		if (foundSobrecupo.isEmpty()) {
			return 1;
		}
		SobreCupo sobrecupo = foundSobrecupo.get();

		if (sobrecupo.getAprobadoSupervisor() != null) {
			return 2; // ya fue respondido
		}

		if (sobrecupo.getAutorizadoCliente() == null) {
			return 3; // el cliente aún no responde
		}

		if (!sobrecupo.getAutorizadoCliente()) {
			return 4; // el cliente rechazó el sobrecupo
		}
		if (!sobrecupo.getCompra().getAlmacen().getId().equals(supervisor.getAlmacen().getId())) {
			return 5; // el supervisor no pertenece al almacén de la compra
		}



		Cliente cliente = sobrecupo.getCompra().getPareja().getCliente();

		Pareja pareja = sobrecupo.getCompra().getPareja();

		System.out.println("Cupo cliente antes: " + cliente.getCupoTotal());
		System.out.println("Monto autorizado: " + sobrecupo.getMontoAutorizado());


		System.out.println("=== VALORES ANTES DE MODIFICAR PAREJA ===");
		System.out.println("cupoAsignado actual: " + pareja.getCupoAsignado());
		System.out.println("montoAdicional: " + sobrecupo.getMontoAdicional());
		System.out.println("montoAutorizado: " + sobrecupo.getMontoAutorizado());

		double aportePareja = sobrecupo.getCompra().getMontoTransaccion() - sobrecupo.getMontoAutorizado();
		pareja.setCupoAsignado(pareja.getCupoAsignado() - aportePareja + sobrecupo.getMontoAdicional());
		System.out.println("cupoAsignado NUEVO calculado: " + pareja.getCupoAsignado());
		
		System.out.println("cupoAsignado NUEVO calculado: " + pareja.getCupoAsignado());

		cliente.setCupoTotal(cliente.getCupoTotal() - sobrecupo.getMontoAutorizado() - sobrecupo.getMontoAdicional());
		parejaRepo.save(pareja);

		Pareja verificacion = parejaRepo.findById(pareja.getId()).get();
		System.out.println("cupoAsignado RELEÍDO de la BD justo después: " + verificacion.getCupoAsignado());

		sobrecupo.setSupervisor(supervisor);
		sobrecupo.setAprobadoSupervisor(true);

		clienteRepo.save(cliente);
		sobrecupoRepo.save(sobrecupo);
		return 0;

	}

	public int denegarSobrecupo(Long idSobrecupo, Long idSupervisor) {
		Optional<Supervisor> found = supervisorRepo.findById(idSupervisor);
		if (found.isEmpty()) {
			return 1;
		}
		Supervisor supervisor = found.get();

		Optional<SobreCupo> foundSobrecupo = sobrecupoRepo.findById(idSobrecupo);
		if (foundSobrecupo.isEmpty()) {
			return 1;
		}
		SobreCupo sobrecupo = foundSobrecupo.get();

		if (sobrecupo.getAprobadoSupervisor() != null) {
			return 2; // ya fue respondido
		}
		if (sobrecupo.getAutorizadoCliente() == null) {
			return 3; // el cliente aún no responde
		}

		if (!sobrecupo.getAutorizadoCliente()) {
			return 4; // el cliente rechazó el sobrecupo
		}
		if (!sobrecupo.getCompra().getAlmacen().getId().equals(supervisor.getAlmacen().getId())) {
			return 5; // el supervisor no pertenece al almacén de la compra
		}

		sobrecupo.setSupervisor(supervisor);
		sobrecupo.setAprobadoSupervisor(false);

		sobrecupoRepo.save(sobrecupo);
		return 0;

	}

	public int autorizarSobrecupo(Long idSobrecupo, Long idCliente, Double montoAdicional) {

		Optional<Cliente> found = clienteRepo.findById(idCliente);
		if (found.isEmpty()) {
			return 1;
		}

		Cliente cliente = found.get();

		Optional<SobreCupo> foundSobrecupo = sobrecupoRepo.findById(idSobrecupo);
		if (foundSobrecupo.isEmpty()) {
			return 1;
		}

		SobreCupo sobrecupo = foundSobrecupo.get();

		if (sobrecupo.getAutorizadoCliente() != null) {
			return 2;
		}

		if (!sobrecupo.getCompra().getPareja().getCliente().getId().equals(cliente.getId())) {
			return 3;
		}

		sobrecupo.setAutorizadoCliente(true);

		double limite = cliente.getCupoTotal() * 0.20;

		if (sobrecupo.getCompra().getMontoTransaccion() <= limite && montoAdicional != null && montoAdicional > 0
				&& montoAdicional < cliente.getCupoTotal()) {

			sobrecupo.setMontoAdicional(montoAdicional);

		} else {

			sobrecupo.setMontoAdicional(0);
		}

		sobrecupoRepo.save(sobrecupo);

		return 0;
	}

	public int cancelarSobrecupo(Long idSobrecupo, Long idCliente) {
		Optional<Cliente> found = clienteRepo.findById(idCliente);
		if (found.isEmpty()) {
			return 1;
		}
		Cliente cliente = found.get();

		Optional<SobreCupo> foundSobrecupo = sobrecupoRepo.findById(idSobrecupo);
		if (foundSobrecupo.isEmpty()) {
			return 1;
		}
		SobreCupo sobrecupo = foundSobrecupo.get();

		if (sobrecupo.getAutorizadoCliente() != null) {
			return 2; // ya fue respondido
		}

		if (!sobrecupo.getCompra().getPareja().getCliente().getId().equals(cliente.getId())) {
			return 3;
		}

		sobrecupo.setAutorizadoCliente(false);

		sobrecupoRepo.save(sobrecupo);
		return 0;

	}
	
	public long count() {
		return sobrecupoRepo.count();
	}
	
	private SobreCupoDTO toDTO(SobreCupo s) {

	    SobreCupoDTO dto = new SobreCupoDTO();

	    dto.setId(s.getId());
	    dto.setMontoAutorizado(s.getMontoAutorizado());
	    dto.setMontoAdicional(s.getMontoAdicional());
	    dto.setAprobadoSupervisor(s.getAprobadoSupervisor());
	    dto.setAutorizadoCliente(s.getAutorizadoCliente());

	    if(s.getClienteTitular() != null){
	        dto.setIdClienteTitular(s.getClienteTitular().getId());
	    }

	    // Compra
	    if(s.getCompra() != null){
	        dto.setIdCompra(s.getCompra().getId());
	    }

	    // Supervisor
	    if(s.getSupervisor() != null){
	        dto.setIdSupervisor(s.getSupervisor().getId());
	           
	        
	    }
	    return dto;
	}
	
	public List<SobreCupoDTO> getAll() {
        return sobrecupoRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}
