CREATE TABLE tb_payments
(
    id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    method ENUM('CREDIT_CARD','DEBIT_CARD','PIX','CASH','WITH_FINANCE_DEPT'),
    amount DECIMAL(10,2),
    stay_id BIGINT NOT NULL,
    creation_date DATE,

    CONSTRAINT tb_payments__stay FOREIGN KEY (stay_id) REFERENCES tb_stays(id)
);
