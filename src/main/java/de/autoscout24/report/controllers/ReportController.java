package de.autoscout24.report.controllers;

import de.autoscout24.report.data.dto.FrequentlyContactedResult;
import de.autoscout24.report.data.dto.SellerType;
import de.autoscout24.report.service.FileUploadService;
import de.autoscout24.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class ReportController {
    final FileUploadService fileUploadService;
    final ReportService reportService;

    @Autowired
    public ReportController(FileUploadService fileUploadService,
                            ReportService reportService) {
        this.fileUploadService = fileUploadService;
        this.reportService = reportService;
    }

    @PostMapping("/upload-csv")
    public void contacts(@RequestParam("file") MultipartFile contactsFile,
                         @RequestParam("file-type") String type) throws Exception {

        fileUploadService.storeFile(contactsFile, type);
    }

    @GetMapping("/avg-listing-selling-price")
    public Map<SellerType, Double> avgListingSellingPrice() throws Exception {
        return reportService.getAvgListingSellingPrice();
    }

    @GetMapping("/percentual-distribution")
    public Map<String, Double> percentualDistribution() throws Exception {
        return reportService.getPercentualDistribution();
    }

    @GetMapping("/often-contacted")
    public Long oftenContacted() throws Exception {
        return reportService.getOftenContactedListings();
    }

    @GetMapping("/monthly-report")
    public Map<Integer, List<FrequentlyContactedResult>> monthlyReport() throws Exception {
        return reportService.getMonthlyFrequentlyContactedListings();
    }
}
