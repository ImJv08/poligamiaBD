package co.edu.unbosque.poligamia.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.poligamia.dto.SupervisorDTO;
import co.edu.unbosque.poligamia.entity.Supervisor;
import co.edu.unbosque.poligamia.repository.ClienteRepository;
import co.edu.unbosque.poligamia.repository.SobrecupoRepository;
import co.edu.unbosque.poligamia.repository.SupervisorRepository;

@Service
public class SupervisorService implements CRUDOperation<SupervisorDTO> {

	@Autowired
	private SupervisorRepository supervisorRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ClienteRepository clienteRepo;

	@Autowired
	private SobrecupoRepository sobrecupoRepo;

	private SupervisorDTO toDTO(Supervisor supervisor) {
		SupervisorDTO dto = new SupervisorDTO();
		dto.setId(supervisor.getId());
		dto.setPrimerNombre(supervisor.getPrimerNombre());
		dto.setSegundoNombre(supervisor.getSegundoNombre());
		dto.setPrimerApellido(supervisor.getPrimerApellido());
		dto.setSegundoApellido(supervisor.getSegundoApellido());
		dto.setNumeroDocumento(supervisor.getNumeroDocumento());
		dto.setCorreoElectronico(supervisor.getCorreoElectronico());
		dto.setContrasenia(supervisor.getContrasenia());
		dto.setRole(supervisor.getRole());
		dto.setAlmacen(supervisor.getAlmacen());
		return dto;
	}

	private Supervisor toEntity(SupervisorDTO dto) {
		Supervisor supervisor = new Supervisor();
		supervisor.setId(dto.getId());
		supervisor.setPrimerNombre(dto.getPrimerNombre());
		supervisor.setSegundoNombre(dto.getSegundoNombre());
		supervisor.setPrimerApellido(dto.getPrimerApellido());
		supervisor.setSegundoApellido(dto.getSegundoApellido());
		supervisor.setNumeroDocumento(dto.getNumeroDocumento());
		supervisor.setCorreoElectronico(dto.getCorreoElectronico());
		supervisor.setContrasenia(dto.getContrasenia());
		supervisor.setRole(dto.getRole());
		supervisor.setAlmacen(dto.getAlmacen());
		return supervisor;
	}

	@Override
	@Transactional
	public int create(SupervisorDTO data) {
		try {
			if (data.getPrimerNombre() == null || data.getPrimerApellido() == null
					|| data.getCorreoElectronico() == null || data.getNumeroDocumento() == null) {
				return 2;
			}
			if (data.getContrasenia() == null || data.getContrasenia().trim().isEmpty()) {
				return 2;
			}
			if (supervisorRepo.existsByCorreoElectronico(data.getCorreoElectronico())) {
				return 1;
			}
			Supervisor supervisor = toEntity(data);
			supervisor.setContrasenia(passwordEncoder.encode(data.getContrasenia()));
			supervisorRepo.save(supervisor);
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	@Override
	public List<SupervisorDTO> getAll() {
		return supervisorRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	public Optional<SupervisorDTO> getById(Long id) {
		return supervisorRepo.findById(id).map(this::toDTO);
	}

	@Override
	@Transactional
	public int deleteById(Long id) {
		if (supervisorRepo.existsById(id)) {
			supervisorRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	@Override
	@Transactional
	public int updateById(Long id, SupervisorDTO newData) {
		Optional<Supervisor> optional = supervisorRepo.findById(id);
		if (optional.isEmpty()) {
			return 1;
		}
		Supervisor existing = optional.get();
		existing.setPrimerNombre(newData.getPrimerNombre());
		existing.setSegundoNombre(newData.getSegundoNombre());
		existing.setPrimerApellido(newData.getPrimerApellido());
		existing.setSegundoApellido(newData.getSegundoApellido());
		existing.setNumeroDocumento(newData.getNumeroDocumento());
		existing.setCorreoElectronico(newData.getCorreoElectronico());
		existing.setRole(newData.getRole());
		if (newData.getAlmacen() != null) {
			existing.setAlmacen(newData.getAlmacen());
		}
		supervisorRepo.save(existing);
		return 0;
	}

	public Optional<SupervisorDTO> obtenerPorCorreo(String correo) {
		return supervisorRepo.findByCorreoElectronico(correo).map(this::toDTO);

	}

	@Override
	public long count() {
		return supervisorRepo.count();
	}

	@Override
	public boolean exist(Long id) {
		return supervisorRepo.existsById(id);
	}

	public List<SupervisorDTO> filtrar(String filtro) {

		List<Supervisor> supervisores = supervisorRepo.findAll();

		return supervisores.stream()
				.filter(s -> s.getPrimerNombre().toLowerCase().contains(filtro.toLowerCase())
						|| s.getPrimerApellido().toLowerCase().contains(filtro.toLowerCase())
						|| s.getNumeroDocumento().contains(filtro)
						|| s.getAlmacen().getNombre().toLowerCase().contains(filtro.toLowerCase()))
				.map(s -> new SupervisorDTO(s.getId(), s.getPrimerNombre(), s.getSegundoNombre(), s.getPrimerApellido(),
						s.getSegundoApellido(), s.getNumeroDocumento(), s.getCorreoElectronico(), s.getContrasenia(),
						s.getRole(), s.getAlmacen()))
				.toList();
	}
}