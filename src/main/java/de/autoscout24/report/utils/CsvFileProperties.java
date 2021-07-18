package de.autoscout24.report.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class CsvFileProperties {
    private String uploadDir;
    private String listingsName;
    private String contactsName;

    public Path getListingsFilePath(){
        return Paths.get(uploadDir, listingsName);
    }

    public Path getContactsFilePath(){
        return Paths.get(uploadDir, contactsName);
    }
}
