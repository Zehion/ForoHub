package com.zehion.forohub.service;

// Importa las clases necesarias para manejar las solicitudes relacionadas con hilos y comentarios.
import com.zehion.forohub.dto.CommentResponseDTO;
import com.zehion.forohub.dto.ThreadRequest;
import com.zehion.forohub.dto.ThreadResponseDTO;
import com.zehion.forohub.model.Thread;
import com.zehion.forohub.model.Signin;
import com.zehion.forohub.repository.ThreadRepository;
import com.zehion.forohub.repository.SigninRepository;
import com.zehion.forohub.repository.CommentRepository; // Asegúrate de tener el repositorio de comentarios.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service // Marca esta clase como un servicio gestionado por Spring.
public class ThreadService {

    private final ThreadRepository threadRepository;
    private final SigninRepository signinRepository;
    private final CommentRepository commentRepository; // Necesitamos acceder a los comentarios.

    @Autowired // Inyección de dependencias a través del constructor.
    public ThreadService(ThreadRepository threadRepository, SigninRepository signinRepository, CommentRepository commentRepository) {
        this.threadRepository = threadRepository;
        this.signinRepository = signinRepository;
        this.commentRepository = commentRepository;
    }

    public ThreadResponseDTO createThread(ThreadRequest threadRequest, Long userId) {
        Signin user = signinRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear un nuevo hilo.
        Thread thread = new Thread();
        thread.setTitle(threadRequest.getTitle());
        thread.setContent(threadRequest.getContent());
        thread.setUser(user);
        thread.setCreatedAt(LocalDateTime.now());
        thread.setUpdatedAt(LocalDateTime.now());

        // Guardar el hilo y devolver el DTO.
        Thread savedThread = threadRepository.save(thread);
        return mapToThreadResponseDTO(savedThread);
    }

    public List<ThreadResponseDTO> getAllThreads() {
        // Obtener todos los hilos.
        List<Thread> threads = threadRepository.findAll();
        return threads.stream()
                .map(this::mapToThreadResponseDTO)
                .collect(Collectors.toList());
    }

    public ThreadResponseDTO getThreadById(Long threadId) {
        // Obtener el hilo por su id.
        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("El hilo no existe"));

        // Crear el DTO para el hilo.
        List<CommentResponseDTO> commentResponses = commentRepository.findByThreadId(threadId)
                .stream()
                .map(comment -> new CommentResponseDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUser().getAlias())) // Exponer solo el alias del usuario.
                .collect(Collectors.toList());

        // Mapear el hilo y los comentarios al DTO.
        return mapToThreadResponseDTO(thread, commentResponses);
    }

    // Mapeo entre entidad Thread y DTO ThreadResponseDTO.
    private ThreadResponseDTO mapToThreadResponseDTO(Thread thread) {
        return new ThreadResponseDTO(
                thread.getId(),
                thread.getTitle(),
                thread.getContent(),
                thread.getCreatedAt(),
                thread.getUser().getAlias(), // Alias del autor del hilo.
                null // En este caso, no necesitamos comentarios, es un hilo sin comentarios.
        );
    }

    // Mapeo entre entidad Thread y DTO ThreadResponseDTO con comentarios.
    private ThreadResponseDTO mapToThreadResponseDTO(Thread thread, List<CommentResponseDTO> commentResponses) {
        return new ThreadResponseDTO(
                thread.getId(),
                thread.getTitle(),
                thread.getContent(),
                thread.getCreatedAt(),
                thread.getUser().getAlias(), // Alias del autor del hilo.
                commentResponses
        );
    }

    public void deleteThread(Long id) {
        // Obtener el hilo antes de eliminarlo.
        Thread thread = threadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hilo no encontrado"));

        // Eliminar el hilo.
        threadRepository.delete(thread);
    }

    public ThreadResponseDTO updateThread(Long id, ThreadRequest threadRequest) {
        // Obtener el hilo original antes de la actualización.
        Thread thread = threadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hilo no encontrado"));

        // Actualizar los campos del hilo.
        thread.setTitle(threadRequest.getTitle());
        thread.setContent(threadRequest.getContent());
        thread.setUpdatedAt(LocalDateTime.now()); // Actualizar la fecha de modificación.

        // Guardar el hilo actualizado.
        Thread updatedThread = threadRepository.save(thread);

        // Mapear el hilo actualizado a un DTO y devolverlo.
        return mapToThreadResponseDTO(updatedThread, getCommentsByThreadId(updatedThread.getId()));
    }

    private List<CommentResponseDTO> getCommentsByThreadId(Long threadId) {
        return commentRepository.findByThreadId(threadId)
                .stream()
                .map(comment -> new CommentResponseDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUser().getAlias())) // Exponer solo el alias del usuario.
                .collect(Collectors.toList());
    }
}
