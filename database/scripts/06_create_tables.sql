
USE magasin;

CREATE TABLE client (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    email VARCHAR(255)
);


CREATE TABLE achat_product(
    product_id BIGINT,
    client_id BIGINT,
    date_achat DATETIME,
    quantity INTEGER,
    PRIMARY KEY (client_id, product_id),
    FOREIGN KEY (client_id) REFERENCES client(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);