package co.edu.unbosque.poligamia.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter  jwtAuthFilter;

//	@Autowired
//	private AuthenticationProvider authProvider;
//
	@Autowired
	private  UserDetailsService userDetailsService;

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
//		return httpSecurity
//				.authorizeHttpRequests(authRequest -> authRequest.requestMatchers("/auth/**").permitAll().anyRequest().authenticated());
//
//
//
//	}

	public SecurityConfig(
		      JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
		    this.jwtAuthFilter = jwtAuthFilter;
		    this.userDetailsService = userDetailsService;
		  }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http
	        .csrf(csrf -> csrf.disable())
	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            		.requestMatchers("/auth/**").permitAll()
            		.requestMatchers("/error").permitAll()
            		.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            		.requestMatchers("/supervisorController/obtenerPorCorreo").authenticated()
            		.requestMatchers("/clienteController/obtenerPorCorreo").authenticated()
            		.requestMatchers("/parejaController/obtenerPorCorreo").authenticated()
            		.requestMatchers("/administradorController/**").authenticated()

            		// Administrador
            		.requestMatchers("/almacenesController/**").hasAnyRole("ADMINISTRADOR", "PAREJA", "CLIENTE", "SUPERVISOR")

            		// Cliente
            		.requestMatchers("/clienteController/obtenerPorCorreo").hasAnyRole("SUPERVISOR", "PAREJA")
            		.requestMatchers("/clienteController/**").hasAnyRole("ADMINISTRADOR", "SUPERVISOR", "CLIENTE", "PAREJA")
            		.requestMatchers("/restriccionesController/**").hasAnyRole("CLIENTE", "ADMINISTRADOR", "PAREJA")

            		// Pareja
            		.requestMatchers("/parejaController/obtenerPorCorreo").hasAnyRole("SUPERVISOR", "CLIENTE")
            		.requestMatchers("/parejaController/**").hasAnyRole("CLIENTE", "PAREJA", "ADMINISTRADOR", "SUPERVISOR")

            		// Compras
            		.requestMatchers("/compraController/**").hasAnyRole("PAREJA", "ADMINISTRADOR", "CLIENTE", "SUPERVISOR")


            		// Sobrecupos
            		.requestMatchers("/sobrecupoController/**").hasAnyRole("CLIENTE", "SUPERVISOR", "ADMINISTRADOR")

            		// Supervisor
            		.requestMatchers("/supervisorController/obtenerPorCorreo").hasAnyRole("PAREJA", "CLIENTE")
            		.requestMatchers("/supervisorController/**").hasAnyRole("SUPERVISOR", "ADMINISTRADOR")

            		.anyRequest().authenticated()
	        )
	        .sessionManagement(session ->
	            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        )
	        .authenticationProvider(authenticationProvider())
	        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	        .build();


	}

	// 2. AuthenticationProvider — define el proveedor de autenticación

	@Bean
    public AuthenticationProvider authenticationProvider() {

    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    	System.out.println("contrasenia encriptada " + passwordEncoder());
        authProvider.setPasswordEncoder(passwordEncoder());   // Usa el método siguiente
        return authProvider;
    }

	 @Bean
	    public PasswordEncoder passwordEncoder() {
		 System.out.println("entra en el PassWordEncoder " );
	        return new BCryptPasswordEncoder();
	    }

	// 4. AuthenticationManager — gestiona la autenticación de usuarios
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    	 System.out.println("entra en el AuthenticationManager " );
	        return config.getAuthenticationManager();
	    }

	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
	        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        configuration.setAllowedHeaders(List.of("*"));
	        configuration.setAllowCredentials(true);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }

}
