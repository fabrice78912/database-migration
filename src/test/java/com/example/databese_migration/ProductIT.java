package com.example.databese_migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.example.databese_migration.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class ProductIT {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllProductsMatchesDatabase() throws Exception {
        // 1. On récupère l'état réel de la base
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
        FROM magasin.product
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
                p.setPrice(rs.getBigDecimal("price"));              // suppose que price est BigDecimal
                Timestamp ts = rs.getTimestamp("lastModified");
                p.setLastModified(ts != null ? ts.toLocalDateTime() : null);  // suppose que lastModified est LocalDateTime
                p.setIsActif(rs.getBoolean("isActif"));
                products.add(p);
            }
        }
        // Ici on ne lance plus d'exception, on retourne la liste (vide si aucun produit)
        return products;
    }
}
