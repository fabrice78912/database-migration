package com.example.databese_migration.repo;

import com.example.databese_migration.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tu peux ajouter des méthodes personnalisées ici si besoin, par exemple :
    // List<Product> findByIsActifTrue();
}
