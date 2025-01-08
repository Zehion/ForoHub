package com.zehion.forohub.dto;

// Importa la clase LocalDateTime para manejar fechas y horas.
import java.time.LocalDateTime;

// Define un record llamado CommentResponseDTO para transferir datos sobre comentarios.
public record CommentResponseDTO(
        Long id,                    // Identificador único del comentario.
        String content,             // Contenido del comentario.
        LocalDateTime createdAt,    // Fecha y hora en que se creó el comentario.
        String userAlias            // Alias del usuario que realizó el comentario.
) {}


