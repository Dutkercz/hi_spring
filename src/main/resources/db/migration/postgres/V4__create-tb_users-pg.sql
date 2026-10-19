CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');

CREATE TABLE tb_users
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    full_name     VARCHAR(255) NOT NULL,
    file_name     VARCHAR(255),
    is_active     BOOLEAN,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at     TIMESTAMP,
    role          user_role             DEFAULT 'USER',
    refresh_token VARCHAR(255)
);