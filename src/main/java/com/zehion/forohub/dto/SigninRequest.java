package com.zehion.forohub.dto;

// Importa las anotaciones necesarias para la validación.
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Define un record llamado SigninRequest para transferir datos relacionados con la solicitud de inicio de sesión.
public record SigninRequest(
        @NotBlank(message = "El email es obligatorio") // Asegura que el campo no esté en blanco y proporciona un mensaje de error personalizado.
        @Email(message = "El email debe ser válido") // Valida que el campo sea un email válido y proporciona un mensaje de error personalizado.
        String email,

        @NotBlank(message = "La contraseña es obligatoria") // Asegura que el campo no esté en blanco y proporciona un mensaje de error personalizado.
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") // Valida que la contraseña tenga al menos 6 caracteres y proporciona un mensaje de error personalizado.
        String password
) {}


