package com.zehion.forohub.error;

import lombok.Getter; // Importa la anotación Getter de Lombok para generar los getters automáticamente.

import java.time.LocalDateTime; // Importa la clase LocalDateTime para manejar fechas y horas.
import java.util.List; // Importa la clase List para manejar listas.

@Getter // Lombok genera automáticamente los métodos getter para todos los campos.
public class ErrorResponse {

    // Campos de la clase
    private LocalDateTime timestamp; // Marca temporal de cuándo se generó la respuesta de error.
    private String message; // Mensaje de error.
    private String details; // Detalles adicionales sobre el error.
    private String path; // Ruta en la que ocurrió el error.
    private List<ErrorMessage> errors; // Lista de mensajes de error adicionales.

    // Constructor para inicializar message y path
    public ErrorResponse(String message, String path) {
        this.timestamp = LocalDateTime.now(); // Establece la marca temporal a la hora actual.
        this.message = message;
        this.path = path;
    }

    // Constructor para inicializar message, details y path
    public ErrorResponse(String message, String details, String path) {
        this.timestamp = LocalDateTime.now(); // Establece la marca temporal a la hora actual.
        this.message = message;
        this.details = details;
        this.path = path;
    }

    // Constructor para inicializar message, errors y path
    public ErrorResponse(String message, List<ErrorMessage> errors, String path) {
        this.timestamp = LocalDateTime.now(); // Establece la marca temporal a la hora actual.
        this.message = message;
        this.errors = errors;
        this.path = path;
    }
}
