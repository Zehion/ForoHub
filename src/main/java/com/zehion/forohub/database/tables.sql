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
