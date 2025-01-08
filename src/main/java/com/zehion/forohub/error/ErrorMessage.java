package com.zehion.forohub.error;

// Define un record llamado ErrorMessage para transferir datos relacionados con mensajes de error.
public record ErrorMessage(
        String field,    // Campo en el que ocurrió el error.
        String message   // Mensaje de error descriptivo.
) {
    // Record es una característica de Java que simplifica la creación de clases inmutables.
    // No es necesario declarar constructores, getters o setters; Java los genera automáticamente.
}



