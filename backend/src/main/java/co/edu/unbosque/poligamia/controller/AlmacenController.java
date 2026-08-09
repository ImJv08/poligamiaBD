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

import co.edu.unbosque.poligamia.dto.AlmacenDTO;
import co.edu.unbosque.poligamia.service.AlmacenService;

@RestController
@RequestMapping("/almacenesController")
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    @GetMapping("/obtenerAlmacen")
    public ResponseEntity<List<AlmacenDTO>> getAll() {
        return ResponseEntity.ok(almacenService.getAll());
    }

    @GetMapping("/obtenerPorId/{id}")
    public ResponseEntity<AlmacenDTO> getById(@PathVariable Long id) {
        return almacenService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crearAlmacen")
    public ResponseEntity<?> create(@RequestBody AlmacenDTO dto) {
        int result = almacenService.create(dto);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Almacén creado");
        }
        return ResponseEntity.badRequest().body("Datos inválidos");
    }

    @PutMapping("/actualizarAlmacen/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AlmacenDTO dto) {
        int result = almacenService.updateById(id, dto);
        if (result == 0) {
            return ResponseEntity.ok("Almacén actualizado");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminarAlmacen/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        int result = almacenService.deleteById(id);
        if (result == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/filtrar")
    public ResponseEntity<List<AlmacenDTO>> filtrar(@RequestParam String filtro) {

        List<AlmacenDTO> almacenes = almacenService.filtrar(filtro);

        return ResponseEntity.ok(almacenes);
    }
}