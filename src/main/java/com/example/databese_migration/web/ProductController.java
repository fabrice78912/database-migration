package com.example.databese_migration.web;

import com.example.databese_migration.entities.Product;
import com.example.databese_migration.entities.dto.ClientProductDTO;
import com.example.databese_migration.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


    @RestController
    //@RequiredArgsConstructor
    @RequestMapping("/v1/products")
    public class ProductController {

        private final ProductService productService;

        public ProductController(ProductService productService) {
            this.productService = productService;
        }


        @GetMapping
        public ResponseEntity<List<Product>> getAllProducts(@RequestParam(value = "name", required=false) String name , @RequestParam(value = "code", required=false) String code) {
            return new ResponseEntity<>(productService.getAllProducts(name,code), HttpStatus.OK);
        }


        @GetMapping("/client/{clientId}")
        public ResponseEntity<List<ClientProductDTO>> getClientProducts(@PathVariable Long clientId) {
            return new ResponseEntity<>(productService.getProductsByClientId(clientId), HttpStatus.OK);
        }

        @GetMapping("/client/")
        public ResponseEntity<List<ClientProductDTO>> getClientProductsWithoutId() {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        /*@GetMapping("/client")
        public ResponseEntity<List<ClientProductDTO>> getClientProducts(@RequestParam(required = false) Long clientId) {
            return new ResponseEntity<>(productService.getProductsByClientId(clientId), HttpStatus.OK);
        }*/

    }

