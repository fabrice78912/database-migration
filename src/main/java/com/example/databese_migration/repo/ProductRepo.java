package com.example.databese_migration.repo;

import com.example.databese_migration.entities.Product;
import com.example.databese_migration.entities.dto.ClientProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = """
    SELECT c.nom AS nameClient, c.email,
           p.id AS productId, p.name_product AS nameProduct, p.code_product AS codeProduct,
           ap.quantity
    FROM client c
    JOIN achat_product ap ON c.id = ap.client_id
    JOIN product p ON ap.product_id = p.id
    WHERE c.id = :clientId
""", nativeQuery = true)
    List<ClientProductDTO> findClientProductsByClientId(@Param("clientId") Long clientId);
}
