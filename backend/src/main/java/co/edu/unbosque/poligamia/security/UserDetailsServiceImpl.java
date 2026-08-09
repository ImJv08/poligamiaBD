package co.edu.unbosque.poligamia.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.poligamia.entity.Usuario;
import co.edu.unbosque.poligamia.repository.UsuarioRepository;





@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository userRepository;


    public UserDetailsServiceImpl(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }




    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

    	Usuario usuario = userRepository.findByCorreoElectronico(correo)
    	            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    	System.out.println("correo: " + correo);
    	System.out.println("correoEncriptado: " + correo);
//        return userRepository.findByCorreo(correoEncriptado)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + correo));

    	return usuario;
    }
}