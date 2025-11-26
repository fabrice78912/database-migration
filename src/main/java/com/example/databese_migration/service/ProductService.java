package com.example.databese_migration.service;


import com.example.databese_migration.entities.Product;
import com.example.databese_migration.entities.dto.ClientProductDTO;
import com.example.databese_migration.payload.ApiResponse;
import com.example.databese_migration.repo.ClientRepository;
import com.example.databese_migration.repo.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final ClientRepository clientRepository;

    public ProductService(ProductRepo productRepo, ClientRepository clientRepository) {
        this.productRepo = productRepo;
        this.clientRepository = clientRepository;
    }

    public List<Product> getAllProducts(String name, String code) {
        List<Product> products = productRepo.findAll();

        products = products.stream()
                .filter(p ->
                        (name == null || (p.getName() != null && p.getName().toLowerCase().contains(name.toLowerCase()))) &&
                                (code == null || (p.getCodeProduct() != null && p.getCodeProduct().toLowerCase().contains(code.toLowerCase())))
                )
                .toList();
        return products;
    }


    private List<ClientProductDTO> getProductsByClientId(Long clientId) {
        return (clientId == null)
                ? Collections.emptyList()
                : productRepo.findClientProductsByClientId(clientId);
    }

    public ApiResponse<List<ClientProductDTO>> getClientProducts(Long clientId) {

        // Vérifie si le client existe
        if (!clientRepository.existsById(clientId)) {
            return ApiResponse.error(
                    "Client avec id " + clientId + " n'existe pas",
                    "CLIENT_NOT_FOUND",
                    HttpStatus.NOT_FOUND.value()
            );
        }

        // Récupère les produits du client
        List<ClientProductDTO> clientProductDTOS = getProductsByClientId(clientId);

        // Message selon la présence des produits
        String message = clientProductDTOS.isEmpty() ? "Aucun produit trouvé" : "Produits trouvés";

        return ApiResponse.success(message, clientProductDTOS, HttpStatus.OK.value());
    }

}
