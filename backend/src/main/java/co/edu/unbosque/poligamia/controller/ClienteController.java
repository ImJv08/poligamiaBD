package co.edu.unbosque.poligamia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.poligamia.dto.ClienteDTO;
import co.edu.unbosque.poligamia.dto.ParejaDTO;
import co.edu.unbosque.poligamia.service.ClienteService;

@RestController
@RequestMapping("/clienteController")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/obtenerCliente")
	public ResponseEntity<List<ClienteDTO>> getAllClientes() {
		return ResponseEntity.ok(clienteService.getAll());
	}

	@PostMapping("/crearCliente")
	public ResponseEntity<?> createCliente(@RequestBody ClienteDTO dto) {
		int result = clienteService.create(dto);
		switch (result) {
		case 0:
			return ResponseEntity.status(HttpStatus.CREATED).body("Cliente creado exitosamente");
		case 1:
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
		default:
			return ResponseEntity.badRequest().body("Datos inválidos");
		}
	}

	@PutMapping("/actualizarCliente/{id}")
	public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody ClienteDTO dto) {
		int result = clienteService.updateById(id, dto);
		if (result == 0) {
			return ResponseEntity.ok("Cliente actualizado exitosamente");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/obtenerPorCorreo")
	public ResponseEntity<?> obtenerPorCorreo(@RequestParam String correo){
		return clienteService.obtenerPorCorreo(correo)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/eliminarCliente/{id}")
	public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
		int result = clienteService.deleteById(id);
		if (result == 0) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/cliente/{idCliente}/cupo")
	public ResponseEntity<?> asignarCupoTotal(@PathVariable Long idCliente, @RequestParam Double cupoTotal) {

		int result = clienteService.asignarCupoTotal(idCliente, cupoTotal);

		if (result == 1) {
			return ResponseEntity.notFound().build();
		}
		

		return ResponseEntity.ok("Cupo asignado correctamente");
	}


	@GetMapping("/contarCliente")
	public long contarCliente() {
		return clienteService.count();
	}

	@GetMapping("/mostrarParejas/{idCliente}")
	public ResponseEntity<List<ParejaDTO>> mostrarParejas(@PathVariable Long idCliente) {

		List<ParejaDTO> parejas = clienteService.mostrarParejas(idCliente);

		if (parejas == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(parejas);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Long id) {
	    return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
	}
	
	
}
