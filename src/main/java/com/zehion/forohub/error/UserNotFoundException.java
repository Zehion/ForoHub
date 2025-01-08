package com.zehion.forohub.error;

// Define una excepción personalizada llamada UserNotFoundException que extiende RuntimeException.
public class UserNotFoundException extends RuntimeException {

    // Constructor de la excepción que acepta un mensaje como parámetro.
    public UserNotFoundException(String message) {
        super(message); // Llama al constructor de la clase padre (RuntimeException) pasando el mensaje.
    }
}


