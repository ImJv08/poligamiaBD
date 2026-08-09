package co.edu.unbosque.poligamia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.poligamia.service.AdministradorService;

@RestController
@RequestMapping("/administradorController")
public class AdministradorController {

	@Autowired
	private AdministradorService adminService;
	
	@GetMapping("/obtenerPorCorreo")
	public ResponseEntity<?> obtenerPorCorreo(@RequestParam String correo){
		return adminService.obtenerPorCorreo(correo)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
