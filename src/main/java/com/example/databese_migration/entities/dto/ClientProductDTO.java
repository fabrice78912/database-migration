package com.example.databese_migration.entities.dto;

import lombok.NoArgsConstructor;
import lombok.ToString;

//@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientProductDTO {

    // Champs client
    private String nameClient;
    private String email;

    // Champs produit
    private Long productId;
    private String nameProduct;
    private String codeProduct;

    // Champs achat
    private Integer quantity;

    public String getNameClient() {
        return nameClient;
    }

    public String getEmail() {
        return email;
    }

    public Long getProductId() {
        return productId;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getCodeProduct() {
        return codeProduct;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setCodeProduct(String codeProduct) {
        this.codeProduct = codeProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ClientProductDTO(String nameClient, String email, Long productId, String nameProduct, String codeProduct, Integer quantity) {
        this.nameClient = nameClient;
        this.email = email;
        this.productId = productId;
        this.nameProduct = nameProduct;
        this.codeProduct = codeProduct;
        this.quantity = quantity;
    }
}

