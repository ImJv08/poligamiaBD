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

import co.edu.unbosque.poligamia.dto.SupervisorDTO;
import co.edu.unbosque.poligamia.service.SupervisorService;

@RestController
@RequestMapping("/supervisorController")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @GetMapping("/mostrarTodo")
    public ResponseEntity<List<SupervisorDTO>> getAll() {
        return ResponseEntity.ok(supervisorService.getAll());
    }

    @GetMapping("/obtenerPorId/{id}")
    public ResponseEntity<SupervisorDTO> getById(@PathVariable Long id) {
        return supervisorService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<?> create(@RequestBody SupervisorDTO dto) {
        int result = supervisorService.create(dto);
        switch (result) {
            case 0:
                return ResponseEntity.status(HttpStatus.CREATED).body("Supervisor creado exitosamente");
            case 1:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
            default:
                return ResponseEntity.badRequest().body("Datos inválidos");
        }
    }

    @GetMapping("/obtenerPorCorreo")
    public ResponseEntity<?> obtenerPorCorreo(@RequestParam String correo){
    	return supervisorService.obtenerPorCorreo(correo)
    			.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SupervisorDTO dto) {
        int result = supervisorService.updateById(id, dto);
        if (result == 0) {
            return ResponseEntity.ok("Supervisor actualizado exitosamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        int result = supervisorService.deleteById(id);
        if (result == 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/filtrar")
    public ResponseEntity<List<SupervisorDTO>> filtrar(@RequestParam String filtro) {

        List<SupervisorDTO> supervisores = supervisorService.filtrar(filtro);

        return ResponseEntity.ok(supervisores);
    }
}