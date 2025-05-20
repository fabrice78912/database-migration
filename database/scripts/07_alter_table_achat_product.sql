use magasin

ALTER TABLE achat_product DROP FOREIGN KEY achat_product_ibfk_2;


ALTER TABLE achat_product
ADD CONSTRAINT achat_product_ibfk_2
FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE;
