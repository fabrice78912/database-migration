package com.example.databese_migration.web;

import com.example.databese_migration.entities.Product;
import com.example.databese_migration.entities.dto.ClientProductDTO;
import com.example.databese_migration.service.PdfReportService;
import com.example.databese_migration.service.ProductService;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/v1/products")
    public class ProductController {

        private final ProductService productService;
        private final PdfReportService pdfReportService;

        public ProductController(ProductService productService, PdfReportService pdfReportService) {
            this.productService = productService;
            this.pdfReportService = pdfReportService;
        }


        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<Product>> getAllProducts(@RequestParam(value = "name", required=false) String name , @RequestParam(value = "code", required=false) String code) {
            return new ResponseEntity<>(productService.getAllProducts(name,code), HttpStatus.OK);
        }


        //@GetMapping("/client/{clientId}")
        @GetMapping(value = "/client/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
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


        @GetMapping("/api/pdf")
        public ResponseEntity<byte[]> getFacture(@RequestParam(value = "name", required=false) String name , @RequestParam(value = "code", required=false) String code) throws Exception {


            byte[] pdf = pdfReportService.generatePdfReport("Jean Dupont", productService.getAllProducts(name, code));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=facture.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        }


    }

