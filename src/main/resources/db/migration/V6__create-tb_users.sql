CREATE TABLE tb_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_active TINYINT,
    created_at TIMESTAMP NOT NULL,
    update_at TIMESTAMP,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    refresh_token VARCHAR(255)
);