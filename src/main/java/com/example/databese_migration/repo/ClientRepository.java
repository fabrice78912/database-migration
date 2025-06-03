package com.example.databese_migration.repo;

import com.example.databese_migration.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Tu peux ajouter des méthodes personnalisées ici si besoin, par exemple :
    // Optional<Client> findByEmail(String email);
}
