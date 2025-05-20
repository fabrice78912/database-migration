package com.example.databese_migration.entities.key;

import java.io.Serializable;
import java.util.Objects;

public class AchatProductId implements Serializable {

    private Long clientId;
    private Long productId;

    public AchatProductId() {}

    public AchatProductId(Long clientId, Long productId) {
        this.clientId = clientId;
        this.productId = productId;
    }

    // equals et hashCode sont obligatoires pour les clés composites
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AchatProductId that)) return false;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, productId);
    }
}
