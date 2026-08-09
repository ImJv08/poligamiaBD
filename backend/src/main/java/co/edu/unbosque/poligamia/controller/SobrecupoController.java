package co.edu.unbosque.poligamia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.poligamia.dto.ParejaDTO;
import co.edu.unbosque.poligamia.dto.SobreCupoDTO;
import co.edu.unbosque.poligamia.service.SobrecupoService;

@RestController
@RequestMapping("/sobrecupoController")
public class SobrecupoController {

	@Autowired
	private SobrecupoService sobrecupoService;

	@GetMapping("/contarSobrecupo")
	public long contarSobrecupo() {
		return sobrecupoService.count();
	}
	
	@GetMapping("/obtenerSobrecupo")
	public ResponseEntity<List<SobreCupoDTO>> getAllParejas() {
		return ResponseEntity.ok(sobrecupoService.getAll());
	}

	@GetMapping("/mostrarSobrecupo")
	public ResponseEntity<?> mostrarTodo() {

	    List<SobreCupoDTO> sobrecupos = sobrecupoService.mostrarTodo();

	    if (sobrecupos.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	    }

	    return ResponseEntity.ok(sobrecupos);
	}


	@GetMapping("/sobrecupo/{idSobrecupo}/estado")
	public ResponseEntity<?> estadoSobrecupo(@PathVariable Long idSobrecupo) {

	    int resultado = sobrecupoService.estadoSobrecupo(idSobrecupo);

	    switch (resultado) {

	        case 1:
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("El sobrecupo no existe.");

	        case 2:
	            return ResponseEntity.status(HttpStatus.ACCEPTED)
	                    .body("El sobrecupo se encuentra pendiente de decisión.");

	        case 3:
	            return ResponseEntity.status(HttpStatus.OK)
	                    .body("El sobrecupo fue aprobado.");

	        case 4:
	            return ResponseEntity.status(HttpStatus.OK)
	                    .body("El sobrecupo fue rechazado.");

	        default:
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Error inesperado.");
	    }
	}

	@PutMapping("/sobrecupo/{idSobrecupo}/autorizar/{idCliente}")
	public ResponseEntity<?> autorizarSobrecupo(@PathVariable Long idSobrecupo, @PathVariable Long idCliente,
			@RequestParam(required = false) Double montoAdicional) {

		int resultado = sobrecupoService.autorizarSobrecupo(idSobrecupo, idCliente, montoAdicional);

		switch (resultado) {

		case 0:
			return ResponseEntity.ok("Sobrecupo autorizado correctamente.");

		case 1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente o sobrecupo no encontrado.");

		case 2:
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El sobrecupo ya fue respondido.");

		case 3:
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("El cliente no tiene permisos para responder este sobrecupo.");

		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado.");
		}
	}

	@PutMapping("/sobrecupo/{idSobrecupo}/denegar/{idCliente}")
	public ResponseEntity<?> denegarSobrecupo(@PathVariable Long idSobrecupo, @PathVariable Long idCliente) {

		int resultado = sobrecupoService.cancelarSobrecupo(idSobrecupo, idCliente);

		if (resultado == 1) {
			return ResponseEntity.status(404).body("Cliente o sobrecupo no encontrado");
		}

		if (resultado == 2) {
			return ResponseEntity.badRequest().body("El sobrecupo ya fue respondido");
		}
		if (resultado == 3) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("El cliente no tiene permisos para responder este sobrecupo.");
		}

		return ResponseEntity.ok("Sobrecupo rechazado correctamente");
	}

	 @PutMapping("/sobrecupo/{idSobrecupo}/aprobar/{idSupervisor}")
	    public ResponseEntity<?> aprobarSobrecupo(
	            @PathVariable Long idSobrecupo,
	            @PathVariable Long idSupervisor) {

	        int resultado = sobrecupoService.aprobarSobrecupo(idSobrecupo, idSupervisor);

	        switch (resultado) {
	            case 1:
	                return ResponseEntity.status(404).body("Supervisor o sobrecupo no encontrado");

	            case 2:
	                return ResponseEntity.badRequest().body("El sobrecupo ya fue respondido");

	            case 3:
	                return ResponseEntity.badRequest().body("El cliente aún no ha autorizado el sobrecupo");

	            case 4:
	                return ResponseEntity.badRequest().body("El cliente rechazó el sobrecupo");

	            case 5:
	                return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                        .body("El supervisor no pertenece al almacén donde se realizó la compra");

	            case 0:
	                return ResponseEntity.ok("Sobrecupo aprobado correctamente");

	            default:
	                return ResponseEntity.badRequest()
	                        .body("Error desconocido");
	        }
	    }

	    @PutMapping("/sobrecupo/{idSobrecupo}/cancelar/{idSupervisor}")
	    public ResponseEntity<?> cancelarSobrecupo(
	            @PathVariable Long idSobrecupo,
	            @PathVariable Long idSupervisor) {

	        int resultado = sobrecupoService.denegarSobrecupo(idSobrecupo, idSupervisor);

	        switch (resultado) {

	            case 1:
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body("Supervisor o sobrecupo no encontrado");

	            case 2:
	                return ResponseEntity.status(HttpStatus.CONFLICT)
	                        .body("El sobrecupo ya fue respondido");

	            case 3:
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body("El cliente aún no ha autorizado el sobrecupo");

	            case 4:
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body("El cliente rechazó el sobrecupo, el supervisor no puede decidir");

	            case 5:
	                return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                        .body("El supervisor no pertenece al almacén donde se realizó la compra");
	            case 0:
	                return ResponseEntity.ok("Sobrecupo rechazado correctamente");

	            default:
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body("Error inesperado");
	        }
	    }


}
