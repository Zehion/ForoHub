# Proyecto ForoHub

## Descripción del Proyecto

ForoHub es una aplicación de foro en línea construida con Java JDK 23, Spring Boot, MySQL, y Insomnia para pruebas de API. Esta aplicación permite a los usuarios registrarse, iniciar sesión, crear hilos, agregar comentarios y más.

## Tecnologías Empleadas

- **Java JDK 23**
- **Spring Boot**
- **Actuator**
- **Data JPA**
- **HATEOAS**
- **Integration**
- **Validation**
- **Web**
- **Security**
- **MySQL**
- **Insomnia**
- **Lombok**
- **Hibernate**
- **Jakarta Persistence**
- **Jakarta Annotation**
- **JWT (JSON Web Tokens)**
- **XMLUnit Core**

## Funcionalidad de la API

La API de ForoHub es una API RESTful que permite a los usuarios interactuar con la aplicación de foro a través de varias operaciones HTTP. Las principales funcionalidades incluyen:

- **Autenticación y Autorización**: Registro de usuarios, inicio de sesión, y manejo de roles.
- **Gestión de Hilos**: Creación, actualización, obtención y eliminación de hilos.
- **Gestión de Comentarios**: Agregar, obtener, actualizar y eliminar comentarios en los hilos.
- **Notificaciones**: Envío de notificaciones relacionadas con los comentarios.

## Uso de la API

La API se puede probar utilizando herramientas como Insomnia o Postman. Aquí hay algunos ejemplos de cómo usar la API:

### Registro de Usuario

```http
POST /users/signup
Content-Type: application/json

{
    "email": "usuario@example.com",
    "password": "contraseña123",
    "alias": "usuario123",
    "name": "Usuario Ejemplo"
}
```

### Inicio de Sesión

```http
POST /users/signin
Content-Type: application/json


{
    "email": "usuario@example.com",
    "password": "contraseña123"
}
```
### Crear un Hilo

```http
POST /threads
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
    "title": "Título del Hilo",
    "content": "Contenido del hilo..."
}
```

### Agregar un Comentario

```http
POST /comments
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
    "content": "Este es un comentario...",
    "threadId": 1
}
```

## Estructura del Proyecto

### Directorio de Archivos

#### Controladores

- **CommentController.java**
    - Métodos: `addComment()`, `getCommentsByThread()`, `deleteComment()`, `updateComment()`

- **ThreadController.java**
    - Métodos: `createThread()`, `getThreadById()`, `getAllThreads()`, `deleteThread()`, `updateThread()`

- **UserController.java**
    - Métodos: `signin()`, `signup()`, `getUserById()`, `getAllUsers()`, `updateUser()`, `deleteUser()`

#### DTOs (Data Transfer Objects)

- **CommentNotificationResponseDTO.java**
- **CommentRequest.java**
- **CommentResponseDTO.java**
- **SigninRequest.java**
- **SignupRequest.java**
- **ThreadRequest.java**
- **ThreadResponseDTO.java**

#### Errores

- **AppointmentConflictException.java**
- **ErrorMessage.java**
- **ErrorResponse.java**
- **HandlerError.java**
- **UserNotFoundException.java**

#### Modelos

- **Comment.java**
- **Signin.java**
- **Thread.java**

#### Repositorios

- **CommentRepository.java**
- **SigninRepository.java**
- **ThreadRepository.java**

#### Servicios

- **CommentService.java**
    - Métodos: `createComment()`, `getCommentsByThreadId()`, `deleteComment()`, `getCommentById()`, `updateComment()`

- **SigninService.java**
    - Métodos: `loadUserByUsername()`, `registerUser()`, `registerUsers()`, `getUserById()`, `getAllUsers()`, `updateUser()`, `deleteUser()`

- **ThreadService.java**
    - Métodos: `createThread()`, `getAllThreads()`, `getThreadById()`, `deleteThread()`, `updateThread()`, `getCommentsByThreadId()`

### Base de Datos

#### Descripción de las Tablas

**Tabla `authentication`**
```sql
-- Crear tabla para usuarios
CREATE TABLE authentication (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                email VARCHAR(255) NOT NULL UNIQUE,
                                password VARCHAR(255) NOT NULL,
                                alias VARCHAR(255),
                                name VARCHAR(255),
                                is_active BOOLEAN NOT NULL,
                                PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Crear tabla para roles de usuario
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role VARCHAR(255) NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES authentication(id)
) ENGINE=InnoDB;

-- Crear tabla para threads
CREATE TABLE thread (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        title VARCHAR(255) NOT NULL,
                        content VARCHAR(255) NOT NULL,
                        created_at DATETIME(6) NOT NULL,
                        updated_at DATETIME(6) NOT NULL,
                        user_id BIGINT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (user_id) REFERENCES authentication(id)
) ENGINE=InnoDB;

-- Crear tabla para comentarios (modificada para eliminación lógica)
CREATE TABLE comment (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         content VARCHAR(255) NOT NULL,
                         created_at DATETIME(6) NOT NULL,
                         updated_at DATETIME(6) NOT NULL,
                         thread_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         deleted BOOLEAN NOT NULL DEFAULT FALSE, -- Campo para eliminación lógica
                         PRIMARY KEY (id),
                         FOREIGN KEY (thread_id) REFERENCES thread(id),
                         FOREIGN KEY (user_id) REFERENCES authentication(id)
) ENGINE=InnoDB;


```
