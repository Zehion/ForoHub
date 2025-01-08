package com.zehion.forohub.controller;

// Importa las clases necesarias para manejar solicitudes relacionadas con hilos.
import com.zehion.forohub.dto.ThreadRequest;
import com.zehion.forohub.dto.ThreadResponseDTO;
import com.zehion.forohub.model.Signin;
import com.zehion.forohub.service.ThreadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // Marca esta clase como un controlador REST.
@RequestMapping("/threads") // Define la ruta base para las solicitudes relacionadas con hilos.
public class ThreadController {

    private final ThreadService threadService;

    @Autowired // Inyección de dependencias a través del constructor.
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    // Crear un hilo
    @PostMapping
    public ResponseEntity<ThreadResponseDTO> createThread(@RequestBody @Valid ThreadRequest threadRequest,
                                                          @AuthenticationPrincipal Signin user) {
        // Obtener el user_id desde el usuario autenticado (debe estar en el JWT).
        Long userId = user.getId();

        // Crear el hilo y asociarlo con el user_id.
        ThreadResponseDTO createdThread = threadService.createThread(threadRequest, userId);

        // Devuelve la respuesta con el estado HTTP 200 OK.
        return ResponseEntity.ok(createdThread);
    }

    // Obtener un hilo por su id (con comentarios)
    @GetMapping("/{id}")
    public ResponseEntity<ThreadResponseDTO> getThreadById(@PathVariable Long id) {
        // Llamar al servicio para obtener el hilo y sus comentarios.
        ThreadResponseDTO threadResponseDTO = threadService.getThreadById(id);
        // Devuelve la respuesta con el estado HTTP 200 OK.
        return ResponseEntity.ok(threadResponseDTO);
    }

    // Obtener todos los hilos
    @GetMapping
    public ResponseEntity<List<ThreadResponseDTO>> getAllThreads() {
        // Obtener todos los hilos.
        List<ThreadResponseDTO> threads = threadService.getAllThreads();
        // Devuelve la lista de hilos con el estado HTTP 200 OK.
        return ResponseEntity.ok(threads);
    }

    // Eliminar un hilo
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteThread(@PathVariable Long id) {
        // Obtener el hilo original antes de la eliminación (con sus comentarios).
        ThreadResponseDTO originalThread = threadService.getThreadById(id);

        // Eliminar el hilo.
        threadService.deleteThread(id);

        // Crear una respuesta con el mensaje de éxito, incluyendo el texto antes de la eliminación.
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hilo eliminado con éxito");
        response.put("status", "success");
        response.put("before", originalThread); // Mostrar el DTO antes de la eliminación.
        response.put("after", "El hilo ha sido eliminado");

        // Devuelve la respuesta con el estado HTTP 200 OK.
        return ResponseEntity.ok(response);
    }

    // Actualizar un hilo
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateThread(@PathVariable Long id,
                                                            @RequestBody @Valid ThreadRequest threadRequest) {
        // Obtener el hilo original antes de la actualización (con sus comentarios).
        ThreadResponseDTO originalThread = threadService.getThreadById(id);

        // Actualizar el hilo.
        ThreadResponseDTO updatedThread = threadService.updateThread(id, threadRequest);

        // Crear una respuesta con el mensaje de éxito, incluyendo el texto antes y después.
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hilo actualizado con éxito");
        response.put("status", "success");
        response.put("before", originalThread); // Mostrar el DTO antes de la actualización.
        response.put("after", updatedThread); // Mostrar el DTO después de la actualización.

        // Devuelve la respuesta con el estado HTTP 200 OK.
        return ResponseEntity.ok(response);
    }
}
