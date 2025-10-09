CREATE TABLE preco_fipe.user
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    roles    VARCHAR(10)  NOT NULL,

    CONSTRAINT UQ_user_email UNIQUE (email)
);

CREATE TABLE preco_fipe.vehicle_type
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(20) NOT NULL,

    CONSTRAINT UQ_vehicle_type_type_name UNIQUE (type_name)
);

CREATE TABLE preco_fipe.fuel
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    fuel         VARCHAR(10) NOT NULL,
    fuel_acronym VARCHAR(1)  NOT NULL,

    CONSTRAINT CK_fuel_fuel CHECK (fuel <> ''),
    CONSTRAINT CK_fuel_fuel_acronym CHECK (fuel.fuel_acronym <> '')
);

CREATE TABLE preco_fipe.vehicle_data
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    code_fipe       VARCHAR(50)  NOT NULL,
    fuel_id         INT          NOT NULL,
    brand           VARCHAR(50)  NOT NULL,
    model           VARCHAR(100) NOT NULL,
    model_year      VARCHAR(5)   NOT NULL,
    vehicle_type_id INT          NOT NULL,

    CONSTRAINT UQ_vehicle_data_code_fipe UNIQUE (code_fipe),
    CONSTRAINT CK_vehicle_data_code_fipe CHECK (code_fipe <> ''),
    CONSTRAINT CK_vehicle_data_brand CHECK (brand <> ''),
    CONSTRAINT CK_vehicle_data_model CHECK (model <> ''),
    CONSTRAINT CK_vehicle_data_model_year CHECK (model_year <> ''),
    CONSTRAINT FK_vehicle_data_vehicle_type_id FOREIGN KEY (vehicle_type_id)
        REFERENCES vehicle_type (id),
    CONSTRAINT FK_vehicle_data_fuel_id FOREIGN KEY (fuel_id)
        REFERENCES preco_fipe.fuel (id)
);

CREATE TABLE preco_fipe.consultation
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    user_id         INT         NOT NULL,
    vehicle_data_id INT         NOT NULL,
    created_at      DATETIME    NOT NULL DEFAULT NOW(),
    price           VARCHAR(20) NOT NULL,
    reference_month VARCHAR(50) NOT NULL,

    #CONSTRAINT CK_consultation_price CHECK (price <> 1),
    CONSTRAINT CK_consultation_reference_month CHECK (reference_month <> ''),

    CONSTRAINT FK_consultation_user_id FOREIGN KEY (user_id)
        REFERENCES preco_fipe.user (id),

    CONSTRAINT FK_consultation_vehicle_data_id FOREIGN KEY (vehicle_data_id)
        REFERENCES preco_fipe.vehicle_data (id)
);

CREATE TABLE favorite
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    user_id         INT      NOT NULL,
    vehicle_data_id INT      NOT NULL,
    created_at      DATETIME NOT NULL DEFAULT NOW(),

    CONSTRAINT FK_favorite_user_id FOREIGN KEY (user_id)
        REFERENCES preco_fipe.user (id),

    CONSTRAINT FK_favorite_vehicle_data_id FOREIGN KEY (vehicle_data_id)
        REFERENCES preco_fipe.vehicle_data (id)
);

CREATE TABLE preco_fipe.comparison
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    user_id    INT NOT NULL,
    created_at DATETIME DEFAULT NOW(),

    CONSTRAINT FK_comparison_user_id FOREIGN KEY (user_id)
        REFERENCES preco_fipe.user (id)
);

CREATE TABLE comparison_consultation
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    comparison_id   INT NOT NULL,
    consultation_id INT NOT NULL,

    CONSTRAINT FK_consultation_comparison_id FOREIGN KEY (comparison_id)
        REFERENCES preco_fipe.comparison (id),

    CONSTRAINT FK_consultation_consultation_id FOREIGN KEY (consultation_id)
        REFERENCES preco_fipe.consultation (id),

    CONSTRAINT UQ_consultation_comparison_consultation UNIQUE (comparison_id, consultation_id)
);



