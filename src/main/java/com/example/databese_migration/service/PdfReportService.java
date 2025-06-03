package com.example.databese_migration.service;

import com.example.databese_migration.entities.Product;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PdfReportService {

    public byte[] generatePdfReport(String clientName, List<Product> products) throws JRException {
        InputStream input = getClass().getResourceAsStream("/reports/facture.jrxml");
        InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");

        JasperReport jasperReport = JasperCompileManager.compileReport(input);

        Map<String, Object> params = new HashMap<>();
        params.put("clientName", clientName);
        params.put("tel", "4563787388");
        params.put("logoPath", logoStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


/*    public byte[] generatePdfReport(String clientName, List<Product> products) throws JRException {
        try (InputStream input = getClass().getResourceAsStream("/reports/facture.jrxml")) {
            if (input == null) {
                throw new IllegalArgumentException("Le fichier facture.jrxml est introuvable dans /reports");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            Map<String, Object> params = new HashMap<>();
            params.put("clientName", clientName);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        }*/
    }



