package com.zehion.forohub.dto;

import java.time.LocalDateTime; // Importa la clase LocalDateTime para manejar fechas y horas.
import java.util.List; // Importa la clase List para manejar listas.

public record ThreadResponseDTO(
        Long id,                        // Identificador único del hilo.
        String title,                   // Título del hilo.
        String content,                 // Contenido del hilo.
        LocalDateTime createdAt,        // Fecha y hora en que se creó el hilo.
        String authorAlias,             // Alias del autor del hilo.
        List<CommentResponseDTO> comments // Lista de comentarios asociados al hilo.
) {}




