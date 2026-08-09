package co.edu.unbosque.poligamia.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.poligamia.dto.CompraDTO;
import co.edu.unbosque.poligamia.service.CompraService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/compraController")
public class CompraController {

	@Autowired
	private CompraService compraService;

	@PostMapping("/registrar/{idPareja}/{idAlmacen}/{montoTransaccion}")
	public ResponseEntity<?> registrarCompra(@PathVariable Long idPareja, @PathVariable Long idAlmacen,
			@PathVariable double montoTransaccion) {

		int resultado = compraService.registrarCompra(idPareja, idAlmacen, montoTransaccion);

		switch (resultado) {

		case 0:
			return ResponseEntity.status(HttpStatus.CREATED).body("Compra registrada correctamente.");

		case 1:
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("La compra requiere autorización de sobrecupo.");

		case 2:
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La pareja no existe.");

		case 3:
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El almacén no existe.");
//cambio
		case 4:
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Compra bloqueada: la pareja tiene una restricción activa en esta fecha y hora");

		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado.");
		}
	}

	@PutMapping("/actualizarEstado/{idCompra}/{idSobrecupo}/{idPareja}")
	public ResponseEntity<?> actualizarEstadoCompra(@PathVariable Long idCompra, @PathVariable Long idSobrecupo,
			@PathVariable Long idPareja) {

		int resultado = compraService.actualizarEstadoCompra(idCompra, idSobrecupo, idPareja);

		switch (resultado) {

		case 0:
			return ResponseEntity.ok("Estado de la compra actualizado correctamente.");

		case 1:
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Aún falta la decisión del cliente o del supervisor.");

		case 2:
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compra o sobrecupo no encontrados.");

		case 3:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El sobrecupo no corresponde a la compra indicada.");

		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado.");
		}
	}

	@GetMapping("/filtrarCompra")
	public ResponseEntity<List<CompraDTO>> filtrarCompras(@RequestParam(required = false) String cliente,
			@RequestParam(required = false) String pareja, @RequestParam(required = false) String almacen,
			@RequestParam(required = false) LocalDate fechaDesde, @RequestParam(required = false) LocalDate fechaHasta,
			@RequestParam(required = false) String estado) {

		List<CompraDTO> resultados = compraService.filtrar(cliente, pareja, almacen, fechaDesde, fechaHasta, estado);
		return ResponseEntity.ok(resultados);
	}
	
	@GetMapping("/comprasHoy")
	public long comprasHoy() {
	    return compraService.contarComprasHoy();
	}
	
	@GetMapping("/obtenerCompras")
	public ResponseEntity<List<CompraDTO>> obtenerTodas() {
	    return ResponseEntity.ok(compraService.getAll());
	}
	
	@GetMapping("/cliente/{idCliente}")
	public ResponseEntity<List<CompraDTO>> obtenerComprasCliente(
	        @PathVariable Long idCliente) {

	    return ResponseEntity.ok(
	            compraService.obtenerComprasCliente(idCliente)
	    );
	}
	
	@GetMapping("/pareja/{idPareja}")
	public ResponseEntity<List<CompraDTO>> obtenerComprasPareja(
	        @PathVariable Long idPareja) {

	    return ResponseEntity.ok(
	            compraService.obtenerComprasPareja(idPareja)
	    );
	}
	
	@PutMapping("/actualizarCompra/{id}")
	public ResponseEntity<?> actualizarCompra(
	        @PathVariable Long id,
	        @RequestBody CompraDTO dto) {

	    int resultado = compraService.actualizarCompra(id, dto);

	    switch (resultado) {
	        case 0:
	            return ResponseEntity.ok("Compra actualizada correctamente.");
	        case 2:
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Compra no encontrada.");
	        case 3:
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("Pareja no encontrada.");
	        case 4:
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("Almacén no encontrado.");
	        default:
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Error interno.");
	    }
	}
	
}


