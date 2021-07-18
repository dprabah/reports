package de.autoscout24.report.data;

import com.opencsv.bean.CsvToBeanBuilder;
import de.autoscout24.report.data.dto.ContactsDto;
import de.autoscout24.report.data.dto.ListingsDto;
import de.autoscout24.report.utils.CsvFileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
@Service
public class DataProviderService {
    final CsvFileProperties csvFileProperties;

    @Autowired
    public DataProviderService(CsvFileProperties csvFileProperties) {
        this.csvFileProperties = csvFileProperties;
    }

    public List<ListingsDto> getListingData() throws FileNotFoundException {
        final String listingFilePath = csvFileProperties.getListingsFilePath().toString();
        return (List<ListingsDto>) new CsvToBeanBuilder(new FileReader(listingFilePath))
                .withType(ListingsDto.class)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .build()
                .parse();
    }

    public List<ContactsDto> getContactData() throws FileNotFoundException {
        final String contactsFilePath = csvFileProperties.getContactsFilePath().toString();

        return (List<ContactsDto>) new CsvToBeanBuilder(new FileReader(contactsFilePath))
                .withType(ContactsDto.class)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .build()
                .parse();
    }
}
