package co.edu.unbosque.poligamia.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

    	System.out.println("🔍 Ejecutando filtro JWT  2...");

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization: " + request.getHeader("Authorization"));

        String correo = null;
        String jwt = null;



        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        	System.out.println("ENTRA AL IF 1");
            jwt = authorizationHeader.substring(7);
            try {
                correo = jwtUtil.extractUsername(jwt);
                System.out.println("Usuario del token: " + correo);
             //   username = AESUtil.decrypt(correoEncriptado);
            } catch (Exception e) {
                logger.error("Error extracting username from token", e);
            }
        }

        if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	System.out.println("ENTRA AL IF 2");
        	System.out.println("Usuario autenticado: " + SecurityContextHolder.getContext().getAuthentication());
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(correo);
            System.out.println("userDetails: " +  userDetails);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println(userDetails.getAuthorities());
            }
        }

        filterChain.doFilter(request, response);
    }
}