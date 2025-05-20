-- Utiliser la base de données appropriée
USE magasin;

-- Met à jour chaque nom en le préfixant par "PRO+"
UPDATE product
SET name = CONCAT('PRO+', name);
