package de.autoscout24.report.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class ValidationUtils {
    public static String validate(MultipartFile file, final String fileType){
        if (file.isEmpty()){
            return "Select a valid non empty file";
        }
        if(!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")){
            return "Select a valid csv file";
        }
        return "";
    }
}
