package de.autoscout24.report.service;

import de.autoscout24.report.utils.CsvFileProperties;
import de.autoscout24.report.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileUploadService {
    final Path uploadDir;

    @Autowired
    public FileUploadService(CsvFileProperties fileProperties) throws Exception {
        this.uploadDir = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (Exception ex) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public void storeFile(MultipartFile file, final String fileType) throws Exception {
        final String validationResults = ValidationUtils.validate(file, fileType);

        if (!validationResults.isEmpty()){
            throw new Exception(validationResults);
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            Path targetLocation = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
