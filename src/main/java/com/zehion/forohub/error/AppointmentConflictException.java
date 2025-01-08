package com.zehion.forohub.error;

// Define una excepción personalizada llamada AppointmentConflictException que extiende RuntimeException.
public class AppointmentConflictException extends RuntimeException {

    // Constructor de la excepción que acepta un mensaje como parámetro.
    public AppointmentConflictException(String message) {
        super(message); // Llama al constructor de la clase padre (RuntimeException) pasando el mensaje.
    }
}


