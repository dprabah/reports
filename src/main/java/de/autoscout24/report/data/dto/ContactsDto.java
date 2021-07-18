package de.autoscout24.report.data.dto;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsDto {
    @CsvBindByPosition(position = 0)
    private long listingId;
    @NonNull
    @CsvCustomBindByPosition(position = 1, converter = LocalDateConverter.class)
    private LocalDate contactDate;

    public static class LocalDateConverter extends AbstractBeanField {
        @Override
        protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
            return new Date(Long.parseLong(s)).toLocalDate();
        }
    }
}
