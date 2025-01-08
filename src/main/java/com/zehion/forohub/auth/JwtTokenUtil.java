package com.zehion.forohub.auth;

// Importa las clases necesarias para manejar JWT (JSON Web Tokens) y componentes de seguridad.
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component // Marca esta clase como un componente gestionado por Spring.
public class JwtTokenUtil {

    @Value("${api.security.salt}")
    private String secretKeyString; // Clave secreta utilizada para firmar y verificar tokens JWT.

    // Genera la clave de firma utilizando la clave secreta decodificada.
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyString));
    }

    // Genera un token JWT para los detalles del usuario proporcionados.
    public Map<String, String> generateToken(UserDetails userDetails) {
        String jwt = Jwts.builder()
                .setSubject(userDetails.getUsername()) // Establece el sujeto del token con el nombre de usuario.
                .setIssuedAt(new Date()) // Establece la fecha de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Establece la fecha de expiración del token (10 horas a partir de ahora).
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma el token con la clave de firma y el algoritmo HS256.
                .compact();

        // Crea un mapa para devolver el token JWT generado.
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("token", jwt);
        return tokenResponse;
    }

    // Extrae el nombre de usuario (sujeto) del token JWT proporcionado.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae todas las reclamaciones (claims) del token JWT.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Configura la clave de firma para la verificación.
                .build()
                .parseClaimsJws(token)
                .getBody(); // Devuelve el cuerpo de las reclamaciones.
    }

    // Extrae una reclamación específica del token JWT utilizando un resolver de reclamaciones.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Valida el token JWT comprobando el nombre de usuario y la expiración del token.
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); // Verifica si el token no ha expirado y coincide con el usuario.
    }

    // Verifica si el token JWT ha expirado.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrae la fecha de expiración del token JWT.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
