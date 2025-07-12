package com.example.databese_migration.repo;

import com.example.databese_migration.entities.AchatProduct;
import com.example.databese_migration.entities.key.AchatProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchatProductRepository extends JpaRepository<AchatProduct, AchatProductId> {
    // Tu peux ajouter ici des méthodes personnalisées si nécessaire, par exemple :
    // List<AchatProduct> findByClientId(Long clientId);
}
