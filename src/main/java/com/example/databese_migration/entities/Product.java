package com.example.databese_migration.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@Data
//@Getter
//@Setter
//@ToString
public class Product {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, name = "name_product")
    @JsonProperty("name_product")
    private String name;

    @EqualsAndHashCode.Include
    @Column(nullable = false, precision = 10, scale = 2)
    @JsonProperty("price")
    private BigDecimal price;

    @EqualsAndHashCode.Include
    @Column(name = "last_modified")
    @JsonProperty("last_modified")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModified;

    @EqualsAndHashCode.Include
    @Column(name = "is_actif")
    @JsonProperty("is_actif")
    private Boolean isActif;

    @EqualsAndHashCode.Include
    @Column(nullable = false, name = "code_product")
    @JsonProperty("code_product")
    private String codeProduct;


    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) {
        if (lastModified == null) {
            this.lastModified = null;
        } else {
            this.lastModified = lastModified.toLocalDate().atStartOfDay();
        }
    }
    public Boolean getIsActif() { return isActif; }
    public void setIsActif(Boolean isActif) { this.isActif = isActif; }

    public String getCodeProduct() { return codeProduct; }
    public void setCodeProduct(String codeProduct) { this.codeProduct = codeProduct; }


   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product p = (Product) o;
        return Objects.equals(id, p.id) &&
                Objects.equals(codeProduct, p.codeProduct) &&
                Objects.equals(name, p.name) &&
                Objects.equals(price, p.price) &&
                Objects.equals(lastModified, p.lastModified) &&
                Objects.equals(isActif, p.isActif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codeProduct, name, price, lastModified, isActif);
    }*/

}



