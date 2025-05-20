package com.example.databese_migration.service;


import com.example.databese_migration.entities.Product;
import com.example.databese_migration.entities.dto.ClientProductDTO;
import com.example.databese_migration.repo.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
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


    public List<ClientProductDTO> getProductsByClientId(Long clientId) {
        return (clientId == null)
                ? Collections.emptyList()
                : productRepo.findClientProductsByClientId(clientId);
    }

}
