USE magasin;

INSERT INTO client (nom, email) VALUES
('Alice Dupont', 'alice.dupont@example.com'),
('Bob Martin', 'bob.martin@example.com'),
('Claire Bernard', 'claire.bernard@example.com'),
('David Petit', 'david.petit@example.com'),
('Emma Leroy', 'emma.leroy@example.com'),
('Fabien Marchand', 'fabien.marchand@example.com'),
('Géraldine Moreau', 'geraldine.moreau@example.com'),
('Hugo Lefevre', 'hugo.lefevre@example.com'),
('Isabelle Richard', 'isabelle.richard@example.com'),
('Julien Gauthier', 'julien.gauthier@example.com');



INSERT INTO magasin.achat_product (client_id, product_id, date_achat, quantity) VALUES
(1, 1, '2025-05-10 10:30:00', 2),
(1, 3, '2025-05-11 09:15:00', 1),
(2, 2, '2025-05-12 14:45:00', 1),
(2, 5, '2025-05-13 16:00:00', 2),
(3, 4, '2025-05-13 11:00:00', 1),
(4, 6, '2025-05-14 13:30:00', 1),
(5, 7, '2025-05-15 12:15:00', 3),
(5, 1, '2025-05-15 16:40:00', 1),
(6, 8, '2025-05-16 17:20:00', 1),
(7, 9, '2025-05-17 10:00:00', 2),
(8, 10, '2025-05-18 09:00:00', 1),
(9, 2, '2025-05-19 11:15:00', 1),
(10, 3, '2025-05-20 15:45:00', 2),
(10, 7, '2025-05-20 16:10:00', 1);

