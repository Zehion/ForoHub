package com.zehion.forohub.dto;

import jakarta.validation.constraints.NotNull; // Importa anotaciones para validaciones.
import jakarta.validation.constraints.Size;
import lombok.Getter; // Importa anotaciones de Lombok para generar getters.
import lombok.Setter; // Importa anotaciones de Lombok para generar setters.

@Getter // Genera automáticamente los métodos getter para todos los campos.
@Setter // Genera automáticamente los métodos setter para todos los campos.
public class CommentRequest {

    @NotNull // Asegura que este campo no sea nulo.
    @Size(min = 1, max = 255) // Restringe el tamaño del contenido entre 1 y 255 caracteres.
    private String content; // Campo para el contenido del comentario.
}


