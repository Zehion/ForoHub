package com.zehion.forohub.dto;

// Esta clase define un DTO (Data Transfer Object) para notificaciones de comentarios.
import java.time.LocalDateTime;

public record CommentNotificationResponseDTO(
        String content,             // Contenido del comentario.
        LocalDateTime createdAt,    // Fecha y hora de creación del comentario.
        String userAlias,           // Alias del usuario que realizó el comentario.
        String threadTitle,         // Título del hilo en el que se realizó el comentario.
        String threadAuthorAlias    // Alias del autor del hilo.
) {
    // Record es una característica de Java que simplifica la creación de clases inmutables.
    // No es necesario declarar constructores, getters o setters; Java los genera automáticamente.
}

