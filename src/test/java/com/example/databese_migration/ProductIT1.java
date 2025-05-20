package com.example.databese_migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.example.databese_migration.entities.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductIT1 {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MockMvc mockMvc;

    // 1. Déclaration du container MariaDB
    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:10.11")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // 2. Injection dynamique des propriétés Spring
    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDB::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDB::getUsername);
        registry.add("spring.datasource.password", mariaDB::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.mariadb.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeAll
    static void initDb() throws Exception {
        // Chargement de données initiales si nécessaire
        try (Connection conn = DriverManager.getConnection(
                mariaDB.getJdbcUrl(), mariaDB.getUsername(), mariaDB.getPassword());
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE product (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      code_product VARCHAR(100),
                      name_product VARCHAR(255),
                      price DECIMAL(10,2),
                      last_modified TIMESTAMP,
                      is_actif BOOLEAN
                    );
                    """);
            // Insert d'exemple
            stmt.execute("""
                    INSERT INTO product(code_product, name_product, price, last_modified, is_actif)
                      VALUES 
                      ('P001','Produit Un',10.50,NOW(),TRUE),
                      ('P002','Produit Deux',20.00,NOW(),FALSE);
                    """);
        }
    }

    @Test
    void testGetAllProductsMatchesDatabase() throws Exception {
        // 1. On récupère l'état réel de la base (Testcontainers)
        List<Product> expected = getAllProductsFromDb();

        // 2. Appel au contrôleur et récupération de la réponse JSON
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        // 3. Conversion JSON → List<Product> via Jackson
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<Product> actual = mapper.readValue(
                jsonResponse,
                new TypeReference<List<Product>>() {}
        );

        // 4. Assertions JUnit
        assertEquals(expected.size(), actual.size(), "Le nombre de produits doit correspondre");
        assertIterableEquals(expected, actual, "Les listes de produits doivent être identiques");
    }

    private List<Product> getAllProductsFromDb() throws Exception {
        String sql = """
        SELECT 
          id,
          code_product    AS codeProduct,
          name_product    AS name,
          price,
          last_modified   AS lastModified,
          is_actif        AS isActif
        FROM product
        ORDER BY id
        """;

        List<Product> products = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getLong("id"));
                p.setCodeProduct(rs.getString("codeProduct"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getBigDecimal("price"));
                Timestamp ts = rs.getTimestamp("lastModified");
                p.setLastModified(ts != null ? ts.toLocalDateTime() : null);
                p.setIsActif(rs.getBoolean("isActif"));
                products.add(p);
            }
        }
        return products;
    }
}
