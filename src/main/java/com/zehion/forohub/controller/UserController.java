package com.zehion.forohub.controller;

// Importa las clases necesarias para manejar usuarios y autenticación.
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zehion.forohub.dto.SigninRequest;
import com.zehion.forohub.dto.SignupRequest;
import com.zehion.forohub.auth.JwtTokenUtil;
import com.zehion.forohub.model.Signin;
import com.zehion.forohub.service.SigninService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController // Marca esta clase como un controlador REST.
@RequestMapping("/users") // Define la ruta base para las solicitudes relacionadas con usuarios.
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final SigninService signinService;

    @Autowired // Inyección de dependencias a través del constructor.
    public UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, SigninService signinService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.signinService = signinService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> signin(@RequestBody @Valid SigninRequest signinRequest) {
        try {
            // Autenticar al usuario utilizando el AuthenticationManager.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signinRequest.email(), // Email del usuario.
                            signinRequest.password() // Contraseña del usuario.
                    )
            );

            // Obtener los detalles del usuario autenticado.
            UserDetails user = (UserDetails) authentication.getPrincipal();
            // Generar el token JWT para el usuario.
            Map<String, String> tokenResponse = jwtTokenUtil.generateToken(user);

            return ResponseEntity.ok(tokenResponse); // Devuelve el token en la respuesta con el estado HTTP 200 OK.
        } catch (BadCredentialsException ex) {
            // Lanzar una excepción si las credenciales son incorrectas.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos", ex);
        }
    }

    // Manejo de la solicitud de registro (signup) de usuarios.
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Object signupRequest) {
        if (signupRequest instanceof List<?> signupRequests) {
            // Manejo de lista de usuarios.
            @SuppressWarnings("unchecked")
            List<SignupRequest> requests = (List<SignupRequest>) signupRequests;
            if (requests.isEmpty()) {
                return ResponseEntity.badRequest().body("La lista de usuarios no puede estar vacía.");
            }
            // Registrar múltiples usuarios y devolver las respuestas.
            List<String> responses = signinService.registerUsers(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } else {
            // Manejo de un único usuario.
            try {
                // Convertir la solicitud a un objeto SignupRequest.
                SignupRequest singleRequest = new ObjectMapper().convertValue(signupRequest, SignupRequest.class);
                // Registrar el usuario y devolver el resultado.
                Signin user = signinService.registerUser(singleRequest);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Usuario '" + user.getAlias() + "' registrado con éxito.");
            } catch (IllegalArgumentException e) {
                // Manejar el caso de formato de solicitud inválido.
                return ResponseEntity.badRequest().body("Formato de solicitud inválido: " + e.getMessage());
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Signin> getUserById(@PathVariable Long id) {
        // Obtener el usuario por su ID.
        Signin user = signinService.getUserById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(user); // Devuelve el usuario con el estado HTTP 200 OK.
    }

    @GetMapping
    public ResponseEntity<List<Signin>> getAllUsers() {
        // Obtener todos los usuarios.
        List<Signin> users = signinService.getAllUsers();
        return ResponseEntity.ok(users); // Devuelve la lista de usuarios con el estado HTTP 200 OK.
    }

    @PutMapping("/{id}")
    public ResponseEntity<Signin> updateUser(@PathVariable Long id, @RequestBody @Valid SignupRequest signupRequest) {
        // Actualizar el usuario con los datos proporcionados.
        Signin user = signinService.updateUser(id, signupRequest);
        return ResponseEntity.ok(user); // Devuelve el usuario actualizado con el estado HTTP 200 OK.
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Eliminar el usuario por su ID.
        signinService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Devuelve el estado HTTP 204 No Content.
    }
}
