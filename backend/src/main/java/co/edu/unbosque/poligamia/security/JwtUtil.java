package co.edu.unbosque.poligamia.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtUtil {

	@Value("${spring.jwt.secret}")
	private String secretKey;
	@Value("${spring.jwt.expirationtime}")
	private long expirationTime;




//	public String generarToken(UserDetails user) {
//        return obtenerToken(new HashMap<>(), user);
//
//	}



	public String obtenerToken(Map<String, Object> extraClaims,  String subject ) {
		 long now = System.currentTimeMillis();
		return Jwts
		.builder()
		//informacion adicional que se quiere dar en la clave(Token)
		.setSubject(subject) //informacion adicional que se quiere dar en la clave(Token)
//		.setSubject(AESUtil.decrypt(usuario.getCorreo())) //Como sera identificado el token del usuario(se usa el email para se unico)
		.setIssuedAt(new Date(now))//Cuando se creo
        .setExpiration(new Date(now + expirationTime)) //cuando se va a expirar
        .signWith( obtenerKey(), SignatureAlgorithm.HS256)
        .compact(); //Generar la clave
	}



	private Key obtenerKey() {
		 byte[] keyBytes = Decoders.BASE64.decode(secretKey); //se decodifica la clave (secretKey)
		    return Keys.hmacShaKeyFor(keyBytes); //nos genera una calve secreta
	}

	public Boolean validateToken(String token, UserDetails userDetails) {

        final String username = extractUsername(token);
        System.out.println("token " + token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

	 public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(obtenerKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }


	    public String generateToken(UserDetails userDetails) {
	        Map<String, Object> claims = new HashMap<>();
	        claims.put("authorities", userDetails.getAuthorities());
//	        String correoEncriptado = AESUtil.encrypt(userDetails.getUsername());
	        String usuario = userDetails.getUsername();
	        return obtenerToken(claims, usuario);
	    }

}
