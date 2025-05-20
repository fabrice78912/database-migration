
-- Utiliser la base de données "magasin"
USE magasin;
ALTER TABLE product
ADD COLUMN is_actif BOOLEAN;
