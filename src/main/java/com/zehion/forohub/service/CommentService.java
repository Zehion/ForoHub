package com.zehion.forohub.service;

// Importa las clases necesarias para manejar los comentarios y las solicitudes relacionadas.
import com.zehion.forohub.dto.CommentRequest;
import com.zehion.forohub.model.Comment;
import com.zehion.forohub.repository.CommentRepository;
import com.zehion.forohub.repository.ThreadRepository;
import com.zehion.forohub.repository.SigninRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service // Marca esta clase como un servicio gestionado por Spring.
public class CommentService {

    private final CommentRepository commentRepository;
    private final ThreadRepository threadRepository;
    private final SigninRepository signinRepository;

    @Autowired // Inyección de dependencias a través del constructor.
    public CommentService(CommentRepository commentRepository, ThreadRepository threadRepository, SigninRepository signinRepository) {
        this.commentRepository = commentRepository;
        this.threadRepository = threadRepository;
        this.signinRepository = signinRepository;
    }

    // Método para crear un nuevo comentario
    @Transactional // Indica que este método se ejecuta dentro de una transacción.
    public Comment createComment(CommentRequest commentRequest, Long threadId, Long userId) {
        // Verificar si el hilo existe
        var thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("El hilo no existe"));

        // Verificar si el usuario existe
        var user = signinRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        // Crear el comentario
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setThread(thread);
        comment.setUser(user);

        // Guardar el comentario en la base de datos
        return commentRepository.save(comment);
    }

    // Obtener todos los comentarios de un hilo
    public List<Comment> getCommentsByThreadId(Long threadId) {
        return commentRepository.findByThreadId(threadId);
    }

    // Eliminar un comentario
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findByIdAndNotDeleted(commentId)
                .orElseThrow(() -> new IllegalArgumentException("El comentario no existe o ya fue eliminado"));
        comment.setDeleted(true); // Marcar como eliminado
        commentRepository.save(comment); // Guardar el cambio
    }

    // Obtener un comentario por ID
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
    }

    // Actualizar un comentario
    public Comment updateComment(Long id, CommentRequest commentRequest) {
        // Obtener el comentario original antes de la actualización
        Comment comment = getCommentById(id);

        // Si el contenido ha cambiado, actualízalo
        String newContent = commentRequest.getContent();
        comment.setContent(newContent);

        // Guardar el comentario actualizado
        return commentRepository.save(comment);
    }
}
