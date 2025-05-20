USE magasin;

UPDATE product
SET code_product = CONCAT('CODE', LPAD(id, 3, '0'));