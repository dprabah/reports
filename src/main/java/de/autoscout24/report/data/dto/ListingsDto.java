package de.autoscout24.report.data.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingsDto {
    @CsvBindByPosition(position = 0)
    private int id;
    @NonNull
    @CsvBindByPosition(position = 1)
    private String make;
    @CsvBindByPosition(position = 2)
    private int price;
    @CsvBindByPosition(position = 3)
    private int mileage;
    @NonNull
    @CsvBindByPosition(position = 4)
    private SellerType sellerType;
}
