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

import co.edu.unbosque.poligamia.dto.RestriccionDTO;
import co.edu.unbosque.poligamia.service.RestriccionService;

@RestController
@RequestMapping("/restriccionesController")
public class RestriccionController {

    @Autowired
    private RestriccionService restriccionService;

    @GetMapping("/mostrarTodo")
    public ResponseEntity<List<RestriccionDTO>> getAll() {
        return ResponseEntity.ok(restriccionService.getAll());
    }

    @GetMapping("/obtenerId/{id}")
    public ResponseEntity<RestriccionDTO> getById(@PathVariable Long id) {
        return restriccionService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/obtenerPorIdPareja/{parejaId}")
    public ResponseEntity<List<RestriccionDTO>> getByParejaId(@PathVariable Long parejaId) {
        return ResponseEntity.ok(restriccionService.getByParejaId(parejaId));
    }

    @PostMapping("/crearRestriccion")
    public ResponseEntity<?> create(@RequestBody RestriccionDTO dto) {
        int result = restriccionService.create(dto);
        switch (result) {
            case 0:
                return ResponseEntity.status(HttpStatus.CREATED).body("Restricción creada");
            case 2:
                return ResponseEntity.badRequest().body("Horario inválido (inicio > fin)");
            case 3:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe una restricción en ese horario para esa fecha");
            default:
                return ResponseEntity.badRequest().body("Error al crear la restricción");
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RestriccionDTO dto) {
        int result = restriccionService.updateById(id, dto);
        switch (result) {
            case 0:
                return ResponseEntity.ok("Restricción actualizada");
            case 1:
                return ResponseEntity.notFound().build();
            case 3:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflicto con otra restricción en ese horario/fecha");
            default:
                return ResponseEntity.badRequest().body("Error al actualizar");
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        int result = restriccionService.deleteById(id);
        if (result == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarRestriccion(
            @RequestParam Long parejaId,
            @RequestParam String fecha,
            @RequestParam String hora) {
        // Se espera formato ISO: fecha=2025-06-15, hora=14:30
        java.time.LocalDate date = java.time.LocalDate.parse(fecha);
        java.time.LocalTime time = java.time.LocalTime.parse(hora);
        boolean tieneRestriccion = restriccionService.tieneRestriccionActiva(parejaId, date, time);
        return ResponseEntity.ok(tieneRestriccion);
    }
    
    @GetMapping("/contarActivas")
    public ResponseEntity<?> contarActivas() {
        return ResponseEntity.ok(restriccionService.contarActivas());
    }
    
    @GetMapping("/pareja/{idPareja}")
    public ResponseEntity<List<RestriccionDTO>> obtenerPorPareja(
            @PathVariable Long idPareja) {

        return ResponseEntity.ok(
                restriccionService.obtenerPorPareja(idPareja)
        );
    }
}