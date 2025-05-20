package com.example.databese_migration.entities;


import com.example.databese_migration.entities.key.AchatProductId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "achat_product")
@IdClass(AchatProductId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchatProduct {


    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "date_achat")
    private LocalDateTime dateAchat;

    private Integer quantity;

}
