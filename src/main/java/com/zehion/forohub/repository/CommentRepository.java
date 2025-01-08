package com.zehion.forohub.repository;

// Importa las clases necesarias para trabajar con JPA y consultas personalizadas.
import com.zehion.forohub.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Marca esta interfaz como un repositorio de Spring, lo que permite que sea detectada y gestionada automáticamente.
public interface CommentRepository extends JpaRepository<Comment, Long> { // Extiende JpaRepository para heredar métodos CRUD básicos.

    // Método comentado que encuentra comentarios por ID de hilo (no usado actualmente).
    // List<Comment> findByThreadId(Long threadId);

    // Consulta personalizada que encuentra comentarios por ID de hilo, excluyendo los que están marcados como eliminados.
    @Query("SELECT c FROM Comment c WHERE c.thread.id = :threadId AND c.deleted = false")
    List<Comment> findByThreadId(@Param("threadId") Long threadId);

    // Consulta personalizada que encuentra un comentario por su ID, excluyendo los que están marcados como eliminados.
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId AND c.deleted = false")
    Optional<Comment> findByIdAndNotDeleted(@Param("commentId") Long commentId);
}



