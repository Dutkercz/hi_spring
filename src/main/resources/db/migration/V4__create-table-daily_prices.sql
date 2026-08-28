CREATE TABLE daily_prices(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    one_guest_price DECIMAL(6,2) NOT NULL,
    two_guest_price DECIMAL(6,2) NOT NULL ,
    three_guest_price DECIMAL(6,2) NOT NULL ,
    four_guest_price DECIMAL(6,2) NOT NULL
);
