package com.zehion.forohub.repository;

// Importa las clases necesarias para trabajar con JPA y consultas personalizadas.
import com.zehion.forohub.model.Signin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marca esta interfaz como un repositorio de Spring, lo que permite que sea detectada y gestionada automáticamente.
public interface SigninRepository extends JpaRepository<Signin, Long> { // Extiende JpaRepository para heredar métodos CRUD básicos.

    // Método para encontrar un usuario por su email.
    Optional<Signin> findByEmail(String email);

    // Método para encontrar un usuario por su alias.
    Optional<Signin> findByAlias(String alias);
}


