package com.example.databese_migration.config;

 import com.example.databese_migration.entities.AchatProduct;
        import com.example.databese_migration.entities.Client;
        import com.example.databese_migration.entities.Product;
 import com.example.databese_migration.repo.AchatProductRepository;
 import com.example.databese_migration.repo.ClientRepository;
 import com.example.databese_migration.repo.ProductRepository;
        import jakarta.transaction.Transactional;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.stereotype.Component;

        import java.math.BigDecimal;
        import java.time.LocalDateTime;
        import java.util.List;

@Component
public class DataBaseInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final AchatProductRepository achatProductRepository;

    public DataBaseInitializer(ClientRepository clientRepository, ProductRepository productRepository, AchatProductRepository achatProductRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.achatProductRepository = achatProductRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (clientRepository.count() == 0 && productRepository.count() == 0 && achatProductRepository.count() == 0) {
            insertClients();
            insertProducts();
            insertAchatProducts();
        }
    }

    private void insertClients() {
        for (int i = 1; i <= 10; i++) {
            clientRepository.save(new Client(null, "Client " + i, "client" + i + "@example.com"));
        }
    }

    private void insertProducts() {
        for (int i = 1; i <= 20; i++) {
            productRepository.save(new Product(
                    null,
                    "Produit " + i,
                    BigDecimal.valueOf(5 + i * 1.25),
                    LocalDateTime.now(),
                    true,
                    "P" + String.format("%03d", i)
            ));
        }
    }

    private void insertAchatProducts() {
        List<Client> clients = clientRepository.findAll();
        List<Product> products = productRepository.findAll();

        int index = 0;
        for (int i = 1; i <= 50; i++) {
            Long clientId = clients.get(i % clients.size()).getId();
            Long productId = products.get(i % products.size()).getId();
            AchatProduct achat = new AchatProduct();
            achat.setClientId(clientId);
            achat.setProductId(productId);
            achat.setDateAchat(LocalDateTime.now().minusDays(i));
            achat.setQuantity((i % 5) + 1);
            achatProductRepository.save(achat);
        }
    }
}

