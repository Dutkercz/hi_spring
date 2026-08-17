CREATE TABLE tb_rooms(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    room_number VARCHAR(255),
    single_beds INT,
    double_beds INT,
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'RESERVED') NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE tb_clients(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_client VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    cnpj VARCHAR(18) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE tb_address(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    client_id BIGINT,
    zip_code VARCHAR(20),
    street VARCHAR(255),
    number VARCHAR(50),
    city VARCHAR(100),
    state VARCHAR(50),
    CONSTRAINT fk_address_client FOREIGN KEY (client_id) REFERENCES tb_clients(id) ON DELETE CASCADE
);

CREATE TABLE tb_stays(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in TIMESTAMP NOT NULL,
    check_out TIMESTAMP,
    daily_price DECIMAL(10, 2),
    partial_price DECIMAL(10, 2),
    total_price DECIMAL(10, 2),
    total_guests int,
    total_daily int,
    stay_status VARCHAR(50) NOT NULL,

    CONSTRAINT fk_stays_client FOREIGN KEY (client_id) REFERENCES tb_clients(id),
    CONSTRAINT fk_stays_room FOREIGN KEY (room_id) REFERENCES tb_rooms(id)
);

CREATE TABLE stay_guest(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    stay_id BIGINT not null,

    constraint stay_guests__stay foreign key (stay_id) REFERENCES tb_stays(id)
)
