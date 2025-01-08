package com.zehion.forohub.model;

import jakarta.persistence.*; // Importa las anotaciones y clases de persistencia de Jakarta.
import lombok.*; // Importa las anotaciones de Lombok.
import org.springframework.security.core.GrantedAuthority; // Importa la clase GrantedAuthority.
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importa la clase SimpleGrantedAuthority.
import org.springframework.security.core.userdetails.UserDetails; // Importa la interfaz UserDetails.

import java.util.Collection; // Importa la clase Collection.
import java.util.List; // Importa la clase List.

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "authentication") // Especifica la tabla en la base de datos para esta entidad.
@Getter // Genera automáticamente los métodos getter para todos los campos.
@Setter // Genera automáticamente los métodos setter para todos los campos.
@NoArgsConstructor // Genera automáticamente un constructor sin argumentos.
@AllArgsConstructor // Genera automáticamente un constructor con argumentos para todos los campos.
@EqualsAndHashCode(of = "id") // Genera automáticamente los métodos equals y hashCode basados en el campo "id".
public class Signin implements UserDetails {

    @Id // Marca este campo como el identificador único de la entidad.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que el valor de la clave principal se generará automáticamente.
    private Long id;

    @Column(unique = true, nullable = false) // Especifica que esta columna debe ser única y no puede ser nula.
    private String email;

    @Column(nullable = false) // Especifica que esta columna no puede ser nula.
    private String password;

    @Column // Alias del usuario (puede ser nulo).
    private String alias;

    @Column // Nombre del usuario (puede ser nulo).
    private String name;

    @ElementCollection(fetch = FetchType.EAGER) // Define una colección de elementos y especifica la estrategia de carga.
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id")) // Especifica la tabla de colección y la columna de unión.
    @Column(name = "role") // Especifica la columna en la tabla de colección.
    private List<String> roles; // Lista de roles del usuario.

    @Column(nullable = false) // Especifica que esta columna no puede ser nula.
    private boolean isActive;

    // Implementación del método getAuthorities() de la interfaz UserDetails.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new) // Convierte los roles a GrantedAuthority.
                .toList();
    }

    // Implementación del método isEnabled() de la interfaz UserDetails.
    @Override
    public boolean isEnabled() {
        return isActive;
    }

    // Implementación del método getUsername() de la interfaz UserDetails.
    @Override
    public String getUsername() {
        return email;
    }

    // Implementación de los métodos de la interfaz UserDetails.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
