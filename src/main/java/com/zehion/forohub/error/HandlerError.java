package com.zehion.forohub.error;

// Importa las clases necesarias para el manejo de excepciones y respuestas HTTP.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice // Marca esta clase como un controlador de excepciones global.
public class HandlerError {

    private static final Logger logger = LoggerFactory.getLogger(HandlerError.class);

    // Mensajes de error predefinidos
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Error interno del servidor";
    private static final String VALIDATION_ERROR_MESSAGE = "Error de validación";
    private static final String CONFLICT_ERROR_MESSAGE = "Registro ya existente";
    private static final String NOT_FOUND_ERROR_MESSAGE = "Ruta no encontrada";

    // Maneja todas las excepciones genéricas
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Error: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE, ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Maneja las excepciones de tipo ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<ErrorResponse> handleResponseStatusExceptions(ResponseStatusException ex, WebRequest request) {
        logger.error("Error: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getReason(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    // Maneja las excepciones de validación de argumentos del método
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        logger.error("Validation error: ", ex);
        List<ErrorMessage> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorMessage(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse(VALIDATION_ERROR_MESSAGE, errors, request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Maneja las excepciones de violación de integridad de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorResponse> handleDataIntegrityViolationExceptions(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Data integrity violation: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(CONFLICT_ERROR_MESSAGE, ex.getMostSpecificCause().getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Maneja las excepciones de usuario no encontrado
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        logger.error("User not found: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Maneja las excepciones de manejador no encontrado (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        logger.error("No handler found: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND_ERROR_MESSAGE, request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Maneja las excepciones de conflictos de citas
    @ExceptionHandler(AppointmentConflictException.class)
    public final ResponseEntity<ErrorResponse> handleAppointmentConflict(AppointmentConflictException ex, WebRequest request) {
        logger.error("Appointment conflict: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
