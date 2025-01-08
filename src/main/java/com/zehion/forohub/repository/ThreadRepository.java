package com.zehion.forohub.repository;

// Importa las clases necesarias para trabajar con JPA.
import com.zehion.forohub.model.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marca esta interfaz como un repositorio de Spring, lo que permite que sea detectada y gestionada automáticamente.
public interface ThreadRepository extends JpaRepository<Thread, Long> { // Extiende JpaRepository para heredar métodos CRUD básicos.
    // No se requieren métodos adicionales, ya que JpaRepository proporciona los métodos CRUD básicos por defecto.
}


