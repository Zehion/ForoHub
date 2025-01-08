package com.zehion.forohub.auth;

// Importa las clases necesarias para el funcionamiento del filtro de autenticación.
import com.zehion.forohub.service.SigninService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta clase como un componente gestionado por Spring.
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Extiende OncePerRequestFilter para ejecutar el filtro una vez por solicitud.

    private final JwtTokenUtil jwtTokenUtil; // Utilidad para manejar tokens JWT.
    private final SigninService signinService; // Servicio para cargar los detalles del usuario.

    @Autowired // Inyección de dependencias a través del constructor.
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, SigninService signinService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.signinService = signinService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws IOException, ServletException {
        // Obtiene el encabezado de autorización de la solicitud.
        final String authorizationHeader = request.getHeader("Authorization");

        // Verifica si el encabezado está presente y comienza con "Bearer ".
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrae el token JWT eliminando el prefijo "Bearer ".
            String jwt = authorizationHeader.substring(7);
            // Extrae el email del token JWT.
            String email = jwtTokenUtil.extractUsername(jwt);

            // Si el email es válido y no hay autenticación previa en el contexto de seguridad.
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carga los detalles del usuario a partir del servicio de inicio de sesión.
                UserDetails userDetails = signinService.loadUserByUsername(email);

                // Valida el token JWT y verifica si es válido para el usuario.
                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    // Crea un objeto de autenticación con los detalles del usuario.
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // Establece detalles adicionales en la autenticación.
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Configura la autenticación en el contexto de seguridad.
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        // Continúa con el siguiente filtro en la cadena de filtros.
        chain.doFilter(request, response);
    }

}

