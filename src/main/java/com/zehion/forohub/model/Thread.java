package com.zehion.forohub.model;

import jakarta.persistence.*; // Importa las anotaciones y clases de persistencia de Jakarta.
import lombok.*; // Importa las anotaciones de Lombok.
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importa la anotación JsonIgnoreProperties para evitar la recursión infinita.
import java.time.LocalDateTime; // Importa la clase LocalDateTime para manejar fechas y horas.
import java.util.List; // Importa la clase List para manejar listas.

@Entity // Marca esta clase como una entidad JPA.
@Getter // Genera automáticamente los métodos getter para todos los campos.
@Setter // Genera automáticamente los métodos setter para todos los campos.
@NoArgsConstructor // Genera automáticamente un constructor sin argumentos.
@AllArgsConstructor // Genera automáticamente un constructor con argumentos para todos los campos.
@EqualsAndHashCode(of = "id") // Genera automáticamente los métodos equals y hashCode basados en el campo "id".
public class Thread {

    @Id // Marca este campo como el identificador único de la entidad.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que el valor de la clave principal se generará automáticamente.
    private Long id;

    @Column(nullable = false) // Especifica que esta columna no puede ser nula.
    private String title;

    @Column(nullable = false) // Especifica que esta columna no puede ser nula.
    private String content;

    @ManyToOne // Define una relación de muchos a uno con la entidad Signin.
    @JoinColumn(name = "user_id", nullable = false) // Especifica la columna de unión y que no puede ser nula.
    private Signin user;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true) // Define una relación de uno a muchos con la entidad Comment.
    private List<Comment> comments; // Lista de comentarios asociados al hilo.

    @Column(nullable = false) // Especifica que esta columna no puede ser nula.
    private LocalDateTime createdAt;

    @Column(nullable = false) // Especifica que esta columna no puede ser nula.
    private LocalDateTime updatedAt;

    // Evita la recursión infinita al serializar el objeto Comment.
    @JsonIgnoreProperties("thread")
    public List<Comment> getComments() {
        return comments;
    }
}
