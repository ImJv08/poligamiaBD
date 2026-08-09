package co.edu.unbosque.poligamia.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.poligamia.dto.ClienteDTO;
import co.edu.unbosque.poligamia.dto.ParejaDTO;
import co.edu.unbosque.poligamia.entity.Cliente;
import co.edu.unbosque.poligamia.entity.Compra;
import co.edu.unbosque.poligamia.entity.Pareja;
import co.edu.unbosque.poligamia.repository.ClienteRepository;
import co.edu.unbosque.poligamia.repository.CompraRepository;
import co.edu.unbosque.poligamia.repository.ParejaRepository;

@Service
public class ClienteService implements CRUDOperation<ClienteDTO> {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ClienteRepository clienteRepo;

	@Autowired
	ParejaRepository parejaRepo;

	@Autowired
	private CompraRepository compraRepo;

	private ClienteDTO toDTO(Cliente cliente) {
		ClienteDTO dto = new ClienteDTO();
		dto.setId(cliente.getId());
		dto.setPrimerNombre(cliente.getPrimerNombre());
		dto.setSegundoNombre(cliente.getSegundoNombre());
		dto.setPrimerApellido(cliente.getPrimerApellido());
		dto.setSegundoApellido(cliente.getSegundoApellido());
		dto.setNumeroDocumento(cliente.getNumeroDocumento());
		dto.setCorreoElectronico(cliente.getCorreoElectronico());
		dto.setContrasenia(cliente.getContrasenia());
		dto.setRole(cliente.getRole());
		dto.setCupoTotal(cliente.getCupoTotal());
		dto.setFechaRegistro(cliente.getFechaRegistro());
		return dto;
	}

	@Override
	public int create(ClienteDTO data) {
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

			Cliente cliente = new Cliente();
			System.out.println("CLIENTE");

			cliente.setPrimerNombre(data.getPrimerNombre());
			cliente.setSegundoNombre(data.getSegundoNombre());
			cliente.setPrimerApellido(data.getPrimerApellido());
			cliente.setSegundoApellido(data.getSegundoApellido());
			cliente.setNumeroDocumento(data.getNumeroDocumento());
			cliente.setCorreoElectronico(data.getCorreoElectronico());
			cliente.setContrasenia(passwordEncoder.encode(data.getContrasenia()));

			cliente.setCupoTotal(0.0);
			cliente.setFechaRegistro(LocalDate.now());

			if (data.getRole() != null) {
				cliente.setRole(data.getRole());
			}

			if (findUsernameAlreadyTaken(cliente)) {
				System.out.println("ERROR: Usuario ya existe - " + data.getCorreoElectronico());
				return 1;
			}
			System.out.println("ANTES DE CREAR :) ");
			clienteRepo.save(cliente);
			System.out.println("despues DE CREAR :) ");
			return 0;

		} catch (Exception e) {
			System.err.println("ERROR en creación: " + e.getMessage());
			e.printStackTrace();
			return 2; // Error
		}
	}

	@Override
	public List<ClienteDTO> getAll() {
		return clienteRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public int deleteById(Long id) {
		if (clienteRepo.existsById(id)) {
			clienteRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	@Override
	@Transactional
	public int updateById(Long id, ClienteDTO newData) {
		Optional<Cliente> optional = clienteRepo.findById(id);
		if (optional.isEmpty()) {
			return 1;
		}
		Cliente existing = optional.get();

		existing.setPrimerNombre(newData.getPrimerNombre());
		existing.setSegundoNombre(newData.getSegundoNombre());
		existing.setPrimerApellido(newData.getPrimerApellido());
		existing.setSegundoApellido(newData.getSegundoApellido());
		existing.setNumeroDocumento(newData.getNumeroDocumento());
		existing.setCorreoElectronico(newData.getCorreoElectronico());
		existing.setCupoTotal(newData.getCupoTotal());
		existing.setRole(newData.getRole());
		clienteRepo.save(existing);
		return 0;
	}

	@Override
	public long count() {
		return clienteRepo.count();
	}

	@Override
	public boolean exist(Long id) {
		return clienteRepo.existsById(id);
	}

	public int asignarCupoTotal(Long idCliente, Double cupoTotal) {

		Optional<Cliente> found = clienteRepo.findById(idCliente);

		if (found.isEmpty()) {
			return 1;
		}
		Cliente cliente = found.get();


		cliente.setCupoTotal(cupoTotal + cliente.getCupoTotal());
		clienteRepo.save(cliente);
		return 0;

	}

	public Optional<ClienteDTO> obtenerPorCorreo(String correo) {
		return clienteRepo.findByCorreoElectronico(correo).map(this::toDTO);
	}

	public List<ParejaDTO> mostrarParejas(Long idCliente) {

		Optional<Cliente> found = clienteRepo.findById(idCliente);

		if (found.isEmpty()) {
			return null;
		}

		Cliente cliente = found.get();

		List<ParejaDTO> dtoList = new ArrayList<>();

		for (Pareja entity : cliente.getParejas()) {

			ParejaDTO dto = new ParejaDTO();

			dto.setId(entity.getId());
			dto.setPrimerNombre(entity.getPrimerNombre());
			dto.setSegundoNombre(entity.getSegundoNombre());
			dto.setPrimerApellido(entity.getPrimerApellido());
			dto.setSegundoApellido(entity.getSegundoApellido());
			dto.setNumeroDocumento(entity.getNumeroDocumento());
			dto.setCorreoElectronico(entity.getCorreoElectronico());
			dto.setCupoAsignado(entity.getCupoAsignado());
			dto.setPrimeraVez(entity.isPrimeraVez());
			dto.setIdCliente(entity.getCliente().getId());

			dtoList.add(dto);
		}

		return dtoList;
	}

	public ClienteDTO obtenerClientePorId(Long id) {
		Optional<Cliente> found = clienteRepo.findById(id);

		if (found.isEmpty()) {
			return null;
		}
		Cliente cliente = found.get();
		ClienteDTO dto = new ClienteDTO(cliente.getId(), cliente.getPrimerNombre(), cliente.getSegundoNombre(),
				cliente.getPrimerApellido(), cliente.getSegundoApellido(), cliente.getNumeroDocumento(),
				cliente.getCorreoElectronico(), cliente.getContrasenia(), cliente.getRole(), cliente.getCupoTotal(),
				cliente.getFechaRegistro());
		return dto;
	}

	private boolean findUsernameAlreadyTaken(Cliente cliente) {
		try {
			Optional<Cliente> found = clienteRepo.findByCorreoElectronico(cliente.getCorreoElectronico());
			boolean exists = found.isPresent();
			System.out.println("Usuario duplicado: " + exists);
			return exists;
		} catch (Exception e) {
			System.err.println("Error verificando duplicado: " + e.getMessage());
			return false;
		}
	}

	public long contarComprasHoy(Long idCliente) {
		Cliente cliente = clienteRepo.findById(idCliente)
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

		LocalDate hoy = LocalDate.now();

		long total = 0;
		for (Pareja pareja : cliente.getParejas()) {
			List<Compra> compras = compraRepo.findByParejaId(pareja.getId());
			total += compras.stream().filter(c -> c.getFecha().isEqual(hoy)).count();
		}

		return total;
	}
}