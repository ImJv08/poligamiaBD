package co.edu.unbosque.poligamia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.poligamia.dto.AuthResponse;
import co.edu.unbosque.poligamia.dto.ClienteDTO;
import co.edu.unbosque.poligamia.dto.LoginRequest;
import co.edu.unbosque.poligamia.security.JwtUtil;
import co.edu.unbosque.poligamia.service.ClienteService;



@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {

	@Autowired
	 private  AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private ClienteService clienteService;

//	@PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UsuarioDTO loginRequest) {
//    	System.out.println("entraaa");
//        try {
//        	System.out.println("➡️ Intentando login con correo: " + loginRequest.getCorreoElectronico());
//        	System.out.println("➡️ Contraseña ingresada: " + loginRequest.getContrasenia());
//
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken( loginRequest.getCorreoElectronico(), loginRequest.getContrasenia()));
//
//            System.out.println("🎯 Autenticación completada: " + authentication.isAuthenticated());
//
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//            System.out.println("credenciales: " + userDetails);
//            System.out.println("login: " + loginRequest.getCorreoElectronico() + "contrasenia" + loginRequest.getContrasenia() );
//            String jwt = jwtUtil.generateToken(userDetails);
//
//            return ResponseEntity.ok(new AuthResponse(jwt));
//        } catch (AuthenticationException e) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("el correo y/o contraseña no coinciden");
//
//        	System.out.println("❌ Tipo de excepción: " + e.getClass().getSimpleName());
//            System.out.println("📄 Mensaje: " + e.getMessage());
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
//        }
//    }

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

	    System.out.println("➡️ Intentando login con correo: " + loginRequest.getCorreoElectronico());
	    System.out.println("➡️ Contraseña ingresada: " + loginRequest.getContrasenia());

	    try {

	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        loginRequest.getCorreoElectronico(),
	                        loginRequest.getContrasenia())
	        );

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	        String jwt = jwtUtil.generateToken(userDetails);

	        return ResponseEntity.ok(new AuthResponse(jwt));

	    } catch (AuthenticationException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
	    }
	}


    @PostMapping("/registrarCliente")
    public ResponseEntity<?> register(@RequestBody ClienteDTO registerRequest) {
    	System.out.println("📩 JSON recibido: " + registerRequest);

    		int result = clienteService.create(registerRequest);


         switch (result) {
             case 0:
                 return ResponseEntity.status(HttpStatus.CREATED)
                         .body("✅ Usuario registrado correctamente.");
             case 1:
                 return ResponseEntity.status(HttpStatus.CONFLICT)
                         .body("⚠️ El usuario ya existe con ese correo electrónico.");
             case 2:
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body("❌ Datos incompletos o error interno al registrar el usuario.");
             default:
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body("❗ Error desconocido al registrar el usuario.");
         }


    }


}
