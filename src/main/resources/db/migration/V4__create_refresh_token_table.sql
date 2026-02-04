CREATE TABLE preco_fipe.refresh_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    token CHAR(36) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,

    CONSTRAINT Fk_refresh_token_user_id FOREIGN KEY (user_id)
      REFERENCES preco_fipe.user (id)
)