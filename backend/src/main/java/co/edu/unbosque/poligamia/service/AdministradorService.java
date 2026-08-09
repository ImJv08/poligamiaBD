package co.edu.unbosque.poligamia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.poligamia.dto.AdministradorDTO;
import co.edu.unbosque.poligamia.dto.ClienteDTO;
import co.edu.unbosque.poligamia.entity.Administrador;
import co.edu.unbosque.poligamia.entity.Cliente;
import co.edu.unbosque.poligamia.repository.AdministradorRepository;

@Service
public class AdministradorService {
	
	@Autowired
	private AdministradorRepository adminRepo;

	public Optional<AdministradorDTO> obtenerPorCorreo(String correo){
		return adminRepo.findByCorreoElectronico(correo).map(this::toDTO);
	}
	
	private AdministradorDTO toDTO(Administrador cliente) {
		AdministradorDTO dto = new AdministradorDTO();
		dto.setId(cliente.getId());
		dto.setPrimerNombre(cliente.getPrimerNombre());
		dto.setSegundoNombre(cliente.getSegundoNombre());
		dto.setPrimerApellido(cliente.getPrimerApellido());
		dto.setSegundoApellido(cliente.getSegundoApellido());
		dto.setNumeroDocumento(cliente.getNumeroDocumento());
		dto.setCorreoElectronico(cliente.getCorreoElectronico());
		dto.setContrasenia(cliente.getContrasenia());
		dto.setRole(cliente.getRole());
		return dto;
	}
}
