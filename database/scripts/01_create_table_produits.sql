-- Utiliser la base de données "magasin"
USE magasin;

-- Crée la table "product" avec 4 colonnes
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    last_modified DATETIME
); 