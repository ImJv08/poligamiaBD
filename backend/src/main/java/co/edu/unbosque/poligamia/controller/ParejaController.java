package co.edu.unbosque.poligamia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.poligamia.dto.ParejaDTO;
import co.edu.unbosque.poligamia.entity.Pareja;
import co.edu.unbosque.poligamia.entity.Usuario;
import co.edu.unbosque.poligamia.service.ParejaService;

@RestController
@RequestMapping("/parejaController")
public class ParejaController {

	@Autowired
	private ParejaService parejaService;



	@GetMapping("/obtenerParejas")
	public ResponseEntity<List<ParejaDTO>> getAllParejas() {
		return ResponseEntity.ok(parejaService.getAll());
	}

	@GetMapping("/obtenerPorId/{id}")
	public ResponseEntity<ParejaDTO> getParejaById(@PathVariable Long id) {
		return parejaService.getById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}


	@PostMapping("/crearPareja")
	public ResponseEntity<?> createPareja(@RequestBody ParejaDTO dto) {
		int resultado = parejaService.create(dto);

		switch (resultado) {
			case 0:
				return ResponseEntity.status(HttpStatus.CREATED).body("Pareja registrada correctamente.");
			case 1:
				return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
			case 2:
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos inválidos.");
			 case 3:
		            return ResponseEntity.badRequest()
		                    .body("El cupo asignado debe ser mayor a cero");

		        case 4:
		            return ResponseEntity.badRequest()
		                    .body("El cupo asignado supera el cupo disponible del cliente");

		        default:
		            return ResponseEntity.internalServerError()
		                    .body("Error desconocido");
		    }
	}

	@PutMapping("/actualizar/{id}")
	public ResponseEntity<?> updatePareja(@PathVariable Long id, @RequestBody ParejaDTO dto) {
		int resultado = parejaService.updateById(id, dto);
		if (resultado == 0) {
			return ResponseEntity.ok("Pareja actualizada correctamente.");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/contarPareja")
	public long contarPareja(){
		return parejaService.count();
	}
	
	@DeleteMapping("/eliminarPareja/{id}")
	public ResponseEntity<?> deletePareja(@PathVariable Long id) {
		int resultado = parejaService.deleteById(id);
		if (resultado == 0) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}



	@PostMapping("/registrarPareja")
	public ResponseEntity<?> register(@RequestBody ParejaDTO dto, Authentication authentication) {
		Long idCliente = ((Usuario) authentication.getPrincipal()).getId();
		int resultado = parejaService.create(dto, idCliente);

		switch (resultado) {
			case 0:
				return ResponseEntity.status(HttpStatus.CREATED).body("Pareja registrada correctamente.");
			case 1:
				return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
			case 2:
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos inválidos.");
			default:
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
		}
	}

	@PutMapping("/cambiarContrasenia")
	public ResponseEntity<?> cambiarContrasenia(@RequestBody Map<String, String> datos) {

	    Long id = Long.valueOf(datos.get("id"));
	    String contrasenia = datos.get("contrasenia");
		int resultado = parejaService.actualizarContrasenia(id, contrasenia);

		switch (resultado) {
			case 0:
				return ResponseEntity.ok("Contraseña actualizada correctamente.");
			case 1:
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pareja no encontrada.");
			default:
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar la contraseña.");
		}
	}

	@PutMapping("/{id}/cupo")
	public ResponseEntity<?> asignarCupoIndividual(@PathVariable Long id, @RequestParam Double cupoIndividual) {
		int resultado = parejaService.asignarCupoIndividual(id, cupoIndividual);

		switch (resultado) {
			case 1:
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La pareja no existe");
			case 2:
				return ResponseEntity.badRequest().body("El cupo supera el cupo total disponible del cliente");
			case 0:
				return ResponseEntity.ok("Cupo asignado correctamente");
			default:
				return ResponseEntity.internalServerError().body("Error desconocido");
		}
	}

	@GetMapping("/obtenerPorCorreo")
	public ResponseEntity<?> obtenerPorCorreo(@RequestParam String correo){
		return parejaService.obtenerPorCorreo(correo)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	 @GetMapping("/cliente/{idCliente}")
	    public ResponseEntity<List<ParejaDTO>> obtenerParejasCliente(
	            @PathVariable Long idCliente){

	        return ResponseEntity.ok(
	                parejaService.obtenerPorCliente(idCliente)
	        );
	    }
	 
	 @GetMapping("/filtrar")
	 public ResponseEntity<List<ParejaDTO>> filtrar(@RequestParam String filtro) {

	     return ResponseEntity.ok(parejaService.filtrar(filtro));

	 }
}
