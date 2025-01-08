package com.zehion.forohub.service;

// Importa las clases necesarias para manejar las solicitudes de registro y autenticación.
import com.zehion.forohub.dto.SignupRequest;
import com.zehion.forohub.model.Signin;
import com.zehion.forohub.repository.SigninRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un servicio gestionado por Spring.
public class SigninService implements UserDetailsService {

    private final SigninRepository signinRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired // Inyección de dependencias a través del constructor.
    public SigninService(SigninRepository signinRepository, PasswordEncoder passwordEncoder) {
        this.signinRepository = signinRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrAlias) throws UsernameNotFoundException {
        // Busca el usuario por email o alias.
        Optional<Signin> user = signinRepository.findByEmail(emailOrAlias);
        if (user.isEmpty()) {
            user = signinRepository.findByAlias(emailOrAlias);
        }
        // Lanza una excepción si el usuario no es encontrado o no está activo.
        if (user.isEmpty() || !user.get().isActive()) {
            throw new UsernameNotFoundException("Usuario no encontrado con el email o alias: " + emailOrAlias);
        }
        return user.get();
    }

    public Signin registerUser(SignupRequest signupRequest) {
        // Verifica si el email ya está registrado.
        if (signinRepository.findByEmail(signupRequest.email()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crea un nuevo usuario.
        Signin user = new Signin();
        user.setEmail(signupRequest.email());
        user.setPassword(passwordEncoder.encode(signupRequest.password()));
        user.setAlias(signupRequest.alias());
        user.setName(signupRequest.name());
        user.setActive(true);
        user.setRoles(List.of("ROLE_MEMBER"));

        // Guarda el usuario en la base de datos.
        return signinRepository.save(user);
    }

    /**
     * Método para registrar una lista de usuarios.
     * @param signupRequests Lista de solicitudes de registro.
     * @return Lista de usuarios registrados o mensajes de error.
     */
    public List<String> registerUsers(List<SignupRequest> signupRequests) {
        // Procesa cada solicitud de registro.
        return signupRequests.stream()
                .map(request -> {
                    try {
                        Signin user = registerUser(request);
                        return String.format("Usuario '%s' registrado con éxito.", user.getAlias());
                    } catch (RuntimeException e) {
                        return String.format("Error registrando al usuario '%s': %s", request.alias(), e.getMessage());
                    }
                })
                .toList();
    }

    public Optional<Signin> getUserById(Long id) {
        return signinRepository.findById(id);
    }

    public List<Signin> getAllUsers() {
        return signinRepository.findAll();
    }

    public Signin updateUser(Long id, SignupRequest signupRequest) {
        // Busca el usuario por ID.
        Optional<Signin> userOptional = signinRepository.findById(id);
        if (userOptional.isPresent()) {
            // Actualiza los datos del usuario.
            Signin user = userOptional.get();
            user.setAlias(signupRequest.alias());
            user.setName(signupRequest.name());
            if (signupRequest.password() != null) {
                user.setPassword(passwordEncoder.encode(signupRequest.password()));
            }
            return signinRepository.save(user);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    public void deleteUser(Long id) {
        // Busca el usuario por ID.
        Optional<Signin> userOptional = signinRepository.findById(id);
        if (userOptional.isPresent()) {
            // Marca el usuario como inactivo.
            Signin user = userOptional.get();
            user.setActive(false);
            signinRepository.save(user);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}
