package com.zehion.forohub.config;

// Importa las clases necesarias para la configuración de seguridad.
import com.zehion.forohub.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica que esta clase contiene configuraciones de Spring.
@EnableMethodSecurity // Habilita la seguridad basada en anotaciones para métodos.
public class SecurityConfig {

    // Define el filtro de seguridad para la cadena de filtros HTTP.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita la protección CSRF.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/signin", "/users/signup").permitAll() // Permite el acceso a estas rutas sin autenticación.
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Requiere el rol de ADMIN para acceder a rutas que comiencen con /admin.
                        .anyRequest().authenticated()) // Requiere autenticación para cualquier otra solicitud.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura la política de sesión para que sea sin estado.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Añade el filtro de autenticación JWT antes del filtro de autenticación por nombre de usuario y contraseña.

        return httpSecurity.build(); // Construye y devuelve la cadena de filtros de seguridad.
    }

    // Define el codificador de contraseñas que usará BCrypt para el hashing de contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define el administrador de autenticación con la configuración de autenticación proporcionada.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
