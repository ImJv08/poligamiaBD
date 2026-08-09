package co.edu.unbosque.poligamia.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.poligamia.dto.ParejaDTO;
import co.edu.unbosque.poligamia.dto.SupervisorDTO;
import co.edu.unbosque.poligamia.entity.Cliente;
import co.edu.unbosque.poligamia.entity.Pareja;
import co.edu.unbosque.poligamia.entity.Supervisor;
import co.edu.unbosque.poligamia.repository.ClienteRepository;
import co.edu.unbosque.poligamia.repository.ParejaRepository;

@Service
public class ParejaService implements CRUDOperation<ParejaDTO> {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ParejaRepository parejaRepo;

	@Autowired
	private ClienteRepository clienteRepo;


	private ParejaDTO toDTO(Pareja pareja) {
		ParejaDTO dto = new ParejaDTO();
		dto.setId(pareja.getId());
		dto.setPrimerNombre(pareja.getPrimerNombre());
		dto.setSegundoNombre(pareja.getSegundoNombre());
		dto.setPrimerApellido(pareja.getPrimerApellido());
		dto.setSegundoApellido(pareja.getSegundoApellido());
		dto.setNumeroDocumento(pareja.getNumeroDocumento());
		dto.setCorreoElectronico(pareja.getCorreoElectronico());
		dto.setContrasenia(pareja.getContrasenia());
		dto.setRole(pareja.getRole());
		dto.setCupoAsignado(pareja.getCupoAsignado());
		dto.setPrimeraVez(pareja.isPrimeraVez());
		if (pareja.getCliente() != null) {
			dto.setIdCliente(pareja.getCliente().getId());
		}
		return dto;
	}


	@Override
	public int create(ParejaDTO data) {
	    try {

	        if (data.getPrimerNombre() == null || data.getPrimerApellido() == null
	                || data.getCorreoElectronico() == null || data.getNumeroDocumento() == null) {
	            System.out.println("ERROR: Usuario vacío");
	            return 2;
	        }

	        if (data.getContrasenia() == null || data.getContrasenia().trim().isEmpty()) {
	            System.out.println("ERROR: Contraseña vacía");
	            return 2;
	        }


	        Cliente cliente = clienteRepo.findById(data.getIdCliente())
	                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));


	        Double cupoAsignado = data.getCupoAsignado();

	        if (cupoAsignado == null || cupoAsignado <= 0) {
	            System.out.println("ERROR: Cupo inválido");
	            return 3;
	        }


	        if (cupoAsignado > cliente.getCupoTotal()) {
	            System.out.println("ERROR: El cupo supera el disponible del cliente");
	            return 4;
	        }


	        // descontar cupo del cliente
	        cliente.setCupoTotal(cliente.getCupoTotal() - cupoAsignado);
	        clienteRepo.save(cliente);


	        Pareja pareja = new Pareja();

	        pareja.setPrimerNombre(data.getPrimerNombre());
	        pareja.setSegundoNombre(data.getSegundoNombre());
	        pareja.setPrimerApellido(data.getPrimerApellido());
	        pareja.setSegundoApellido(data.getSegundoApellido());
	        pareja.setNumeroDocumento(data.getNumeroDocumento());
	        pareja.setCorreoElectronico(data.getCorreoElectronico());
	        pareja.setContrasenia(passwordEncoder.encode(data.getNumeroDocumento()));

	        pareja.setCupoAsignado(cupoAsignado);
	        pareja.setPrimeraVez(true);
	        pareja.setCliente(cliente);


	        if (data.getRole() != null) {
	            pareja.setRole(data.getRole());
	        }


	        if (findUsernameAlreadyTaken(pareja)) {
	            System.out.println("ERROR: Usuario ya existe - " + data.getCorreoElectronico());
	            return 1;
	        }


	        parejaRepo.save(pareja);


	        return 0;


	    } catch (Exception e) {

	        System.err.println("ERROR en creación: " + e.getMessage());
	        e.printStackTrace();

	        return 2;
	    }
	}

	public int create(ParejaDTO data, Long idCliente) {
		try {

			if (data.getPrimerNombre() == null || data.getPrimerApellido() == null
					|| data.getCorreoElectronico() == null || data.getNumeroDocumento() == null) {
				System.out.println("ERROR: Usuario vacío");
				return 2;
			}

			if (data.getContrasenia() == null || data.getContrasenia().trim().isEmpty()) {
				System.out.println("ERROR: Contraseña vacía");
				return 2;
			}

			Cliente cliente = clienteRepo.findById(idCliente)
					.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

			Pareja pareja = new Pareja();

			pareja.setPrimerNombre(data.getPrimerNombre());
			pareja.setSegundoNombre(data.getSegundoNombre());
			pareja.setPrimerApellido(data.getPrimerApellido());
			pareja.setSegundoApellido(data.getSegundoApellido());
			pareja.setNumeroDocumento(data.getNumeroDocumento());
			pareja.setCorreoElectronico(data.getCorreoElectronico());
			pareja.setContrasenia(passwordEncoder.encode(data.getNumeroDocumento()));

			pareja.setCupoAsignado(data.getCupoAsignado());
			pareja.setPrimeraVez(true);
			pareja.setCliente(cliente);

			if (data.getRole() != null) {
				pareja.setRole(data.getRole());
			}

			if (findUsernameAlreadyTaken(pareja)) {
				System.out.println("ERROR: Usuario ya existe - " + data.getCorreoElectronico());
				return 1;
			}

			parejaRepo.save(pareja);

			return 0;

		} catch (Exception e) {
			System.err.println("ERROR en creación: " + e.getMessage());
			e.printStackTrace();
			return 2; // Error
		}
	}

	@Override
	public List<ParejaDTO> getAll() {
		return parejaRepo.findAll().stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public int deleteById(Long id) {
		if (parejaRepo.existsById(id)) {
			parejaRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	@Override
	@Transactional
	public int updateById(Long id, ParejaDTO newData) {
		Optional<Pareja> optional = parejaRepo.findById(id);
		Cliente cliente = clienteRepo.findById(newData.getIdCliente())
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
		if (optional.isEmpty()) {
			return 1;
		}
		Pareja existing = optional.get();

		existing.setPrimerNombre(newData.getPrimerNombre());
		existing.setSegundoNombre(newData.getSegundoNombre());
		existing.setPrimerApellido(newData.getPrimerApellido());
		existing.setSegundoApellido(newData.getSegundoApellido());
		existing.setNumeroDocumento(newData.getNumeroDocumento());
		existing.setCorreoElectronico(newData.getCorreoElectronico());
		existing.setRole(newData.getRole());
		existing.setCliente(cliente);
		

		parejaRepo.save(existing);
		return 0;
	}

	public Optional<ParejaDTO> getById(Long id) {
	    return parejaRepo.findById(id).map(this::toDTO);
	}

	@Override
	public long count() {
		return parejaRepo.count();
	}

	@Override
	public boolean exist(Long id) {
		return parejaRepo.existsById(id);
	}



	private boolean findUsernameAlreadyTaken(Pareja pareja) {
		try {
			Optional<Pareja> found = parejaRepo.findByCorreoElectronico(pareja.getCorreoElectronico());
			boolean exists = found.isPresent();
			System.out.println("Usuario duplicado: " + exists);
			return exists;
		} catch (Exception e) {
			System.err.println("Error verificando duplicado: " + e.getMessage());
			return false;
		}
	}

	public int actualizarContrasenia(Long  id, String contrasenia) {

		Optional<Pareja> found = parejaRepo.findById(id);
		Pareja pareja = found.get();

		if (found.isEmpty()) {
			return 1;
		}

		if (pareja.isPrimeraVez()) {
			pareja.setContrasenia(passwordEncoder.encode(contrasenia));
			pareja.setPrimeraVez(false);
			parejaRepo.save(pareja);

		}
		return 0;
	}

	public int asignarCupoIndividual(Long id, Double nuevoCupo) {

	    Optional<Pareja> found = parejaRepo.findById(id);

	    if (found.isEmpty()) {
	        return 1;
	    }

	    Pareja pareja = found.get();
	    Cliente cliente = pareja.getCliente();


	    if (nuevoCupo > cliente.getCupoTotal()) {
	        return 2;
	    }

	    // actualizamos valores
	    cliente.setCupoTotal(cliente.getCupoTotal() - nuevoCupo);
	    pareja.setCupoAsignado(nuevoCupo + pareja.getCupoAsignado());
	    clienteRepo.save(cliente);
	    parejaRepo.save(pareja);

	    return 0;
	}

	public Optional<ParejaDTO> obtenerPorCorreo(String correo){
		return parejaRepo.findByCorreoElectronico(correo).map(this::toDTO);
	}
	
	public List<ParejaDTO> obtenerPorCliente(Long idCliente) {
	    return parejaRepo.findByClienteId(idCliente).stream()
	            .map(this::toDTO)
	            .toList();
	}
	
	public List<ParejaDTO> filtrar(String filtro) {

		List<Pareja> pareja = parejaRepo.findAll();

		return pareja.stream()
				.filter(s -> s.getPrimerNombre().toLowerCase().contains(filtro.toLowerCase())
						|| s.getPrimerApellido().toLowerCase().contains(filtro.toLowerCase())
						|| s.getNumeroDocumento().contains(filtro)
						|| s.getCliente().getPrimerNombre().toLowerCase().contains(filtro.toLowerCase()))
				.map(s -> new ParejaDTO(s.getId(), s.getPrimerNombre(), s.getSegundoNombre(), s.getPrimerApellido(),
						s.getSegundoApellido(), s.getNumeroDocumento(), s.getCorreoElectronico(), s.getContrasenia(),
						s.getRole(), s.getCupoAsignado(), s.getCliente().getId(), s.isPrimeraVez()))
				.toList();
	}


	
}