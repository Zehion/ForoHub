package com.zehion.forohub.controller;

// Importa las clases necesarias para manejar comentarios y respuestas en la API.
import com.zehion.forohub.dto.CommentNotificationResponseDTO;
import com.zehion.forohub.dto.CommentRequest;
import com.zehion.forohub.model.Comment;
import com.zehion.forohub.model.Signin;
import com.zehion.forohub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // Marca esta clase como un controlador REST de Spring.
@RequestMapping("/comments") // Define la ruta base para las solicitudes relacionadas con comentarios.
public class CommentController {

    private final CommentService commentService;

    @Autowired // Inyección de dependencias a través del constructor.
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Agregar un comentario
    @PostMapping("/{threadId}/{userId}")
    public ResponseEntity<CommentNotificationResponseDTO> addComment(@RequestBody CommentRequest commentRequest,
                                                                     @PathVariable Long threadId,
                                                                     @PathVariable Long userId,
                                                                     @AuthenticationPrincipal Signin authenticatedUser) {
        // Crear el comentario con el usuario autenticado
        Comment createdComment = commentService.createComment(commentRequest, threadId, userId);

        // Crear el DTO con la información relevante
        CommentNotificationResponseDTO response = new CommentNotificationResponseDTO(
                createdComment.getContent(),
                createdComment.getCreatedAt(),
                authenticatedUser.getAlias(),  // Usar el alias del usuario autenticado
                createdComment.getThread().getTitle(),
                createdComment.getThread().getUser().getAlias()  // Autor del hilo
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Devuelve la respuesta con el estado HTTP 201 Created.
    }

    // Listar los comentarios de un hilo
    @GetMapping("/{threadId}")
    public ResponseEntity<List<Comment>> getCommentsByThread(@PathVariable Long threadId) {
        List<Comment> comments = commentService.getCommentsByThreadId(threadId);
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve un estado HTTP 204 No Content si no hay comentarios.
        }
        return ResponseEntity.ok(comments); // Devuelve la lista de comentarios con el estado HTTP 200 OK.
    }

    // Eliminar un comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id); // Llamada al servicio para eliminar lógicamente el comentario

        // Crear una respuesta con un mensaje de éxito
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Comentario eliminado con éxito");
        response.put("status", "success");

        return ResponseEntity.ok(response); // Devuelve la respuesta con el estado HTTP 200 OK.
    }

    // Actualizar un comentario
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long id,
                                                             @RequestBody CommentRequest commentRequest) {
        // Obtener el comentario original antes de la actualización
        Comment originalComment = commentService.getCommentById(id);

        // Verificar si el contenido ha cambiado antes de la actualización
        String originalContent = originalComment.getContent();
        String newContent = commentRequest.getContent();

        // Si el contenido ha cambiado, actualizar el comentario
        if (!originalContent.equals(newContent)) {
            // Actualizar el comentario
            Comment updatedComment = commentService.updateComment(id, commentRequest);

            // Crear una respuesta con el mensaje de éxito, incluyendo el texto antes y después
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Comentario actualizado con éxito");
            response.put("status", "success");
            response.put("before", originalContent);  // Mostrar el contenido antes de la actualización
            response.put("after", updatedComment.getContent());  // Mostrar el contenido después de la actualización

            return ResponseEntity.ok(response); // Devuelve la respuesta con el estado HTTP 200 OK.
        } else {
            // Si el contenido no ha cambiado, indicar que no hubo cambios
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No hubo cambios en el comentario");
            response.put("status", "no_change");
            response.put("before", originalContent);
            response.put("after", originalContent);

            return ResponseEntity.ok(response); // Devuelve la respuesta con el estado HTTP 200 OK.
        }
    }
}
