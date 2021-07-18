package de.autoscout24.report.data.dto;

public enum SellerType {
    PRIVATE("private"),
    DEALER("dealer"),
    OTHER("other");

    private final String name;
    SellerType(String name) {
        this.name = name;
    }
}
