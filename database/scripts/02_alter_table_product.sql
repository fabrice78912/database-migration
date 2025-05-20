
-- Utiliser la base de données "magasin"
USE magasin;

ALTER TABLE product
CHANGE COLUMN name name_product VARCHAR(255) NOT NULL;
