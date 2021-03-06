package de.autoscout24.report.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrequentlyContactedResult {
    private long count;
    private int month;
    private ListingsDto listing;
    private int rank;
}
