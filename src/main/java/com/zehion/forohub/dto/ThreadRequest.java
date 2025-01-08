package com.zehion.forohub.dto;

import jakarta.validation.constraints.NotBlank; // Importa la anotación NotBlank para validaciones.
import lombok.Getter; // Importa la anotación Getter de Lombok para generar los getters.
import lombok.Setter; // Importa la anotación Setter de Lombok para generar los setters.

@Setter // Lombok genera automáticamente los métodos setter para todos los campos.
@Getter // Lombok genera automáticamente los métodos getter para todos los campos.
public class ThreadRequest {

    @NotBlank(message = "El título es obligatorio") // Asegura que el campo title no esté en blanco y proporciona un mensaje de error personalizado.
    private String title;

    @NotBlank(message = "El contenido es obligatorio") // Asegura que el campo content no esté en blanco y proporciona un mensaje de error personalizado.
    private String content;

    // Constructor por defecto.
    public ThreadRequest() {
    }

    // Constructor que inicializa los campos title y content.
    public ThreadRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}



