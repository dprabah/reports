package de.autoscout24.report.service;

import de.autoscout24.report.data.DataProviderService;
import de.autoscout24.report.data.dto.ContactsDto;
import de.autoscout24.report.data.dto.FrequentlyContactedResult;
import de.autoscout24.report.data.dto.ListingsDto;
import de.autoscout24.report.data.dto.SellerType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
    @Mock
    DataProviderService dataProviderService;
    ReportService subject;

    @Before
    public void setUp() {
        subject = new ReportService(dataProviderService);
    }

    @Test
    public void testGetAvgListingSellingPrice() throws Exception {
        // arrange
        final Double dealerCount = 2.0;
        final Double privateCount = 1.0;
        final Double otherCount = 1.0;
        Mockito.when(dataProviderService.getListingData()).thenReturn(getListingTestData());

        // act + assert
        assertEquals(dealerCount, subject.getAvgListingSellingPrice().get(SellerType.DEALER));
        assertEquals(privateCount, subject.getAvgListingSellingPrice().get(SellerType.PRIVATE));
        assertEquals(otherCount, subject.getAvgListingSellingPrice().get(SellerType.OTHER));
    }

    @Test
    public void getPercentualDistribution() throws Exception {
        // arrange
        final Double makeA = 50.0;
        final Double makeB = 50.0;
        Mockito.when(dataProviderService.getListingData()).thenReturn(getListingTestData());

        // act + assert
        assertEquals(makeA, subject.getPercentualDistribution().get("A"));
        assertEquals(makeB, subject.getPercentualDistribution().get("B"));
    }

    @Test
    public void getOftenContactedListings() throws Exception {
        final Long oftenContacted = 2L;
        Mockito.when(dataProviderService.getListingData()).thenReturn(getListingTestData());
        Mockito.when(dataProviderService.getContactData()).thenReturn(getContactsTestData());

        // act + assert
        assertEquals(oftenContacted, subject.getOftenContactedListings());
    }

    @Test
    public void getMonthlyFrequentlyContactedListings() throws Exception {
        // arrange
        final long countFirst = 4L;
        final long countLast = 1L;
        final int resultCount = 4;
        final String make = "B";
        Mockito.when(dataProviderService.getListingData()).thenReturn(getListingTestData());
        Mockito.when(dataProviderService.getContactData()).thenReturn(getContactsTestData());
        System.out.println(subject.getMonthlyFrequentlyContactedListings());

        // act
        List<FrequentlyContactedResult> actual = subject.getMonthlyFrequentlyContactedListings().get(6);

        // assert
        assertEquals(resultCount, actual.size());
        assertEquals(countFirst, actual.get(0).getCount());
        assertEquals(make, actual.get(0).getListing().getMake());
        assertEquals(countLast, actual.get(3).getCount());
        assertEquals(make, actual.get(3).getListing().getMake());
    }

    private List<ListingsDto> getListingTestData(){
        List<ListingsDto> listingsData = new ArrayList<>();
        listingsData.add(new ListingsDto(1, "A", 2, 1, SellerType.DEALER));
        listingsData.add(new ListingsDto(2, "A", 1, 1, SellerType.OTHER));
        listingsData.add(new ListingsDto(3, "B", 1, 1, SellerType.PRIVATE));
        listingsData.add(new ListingsDto(4, "B", 2, 1, SellerType.DEALER));

        return listingsData;
    }

    private List<ContactsDto> getContactsTestData(){
        List<ContactsDto> contactsData = new ArrayList<>();
        contactsData.add(new ContactsDto(1, new Date(Long.parseLong("1592498493000")).toLocalDate()));
        contactsData.add(new ContactsDto(2, new Date(Long.parseLong("1592498493000")).toLocalDate()));
        contactsData.add(new ContactsDto(3, new Date(Long.parseLong("1592498493000")).toLocalDate()));
        contactsData.add(new ContactsDto(4, new Date(Long.parseLong("1592498493000")).toLocalDate()));
        contactsData.add(new ContactsDto(4, new Date(Long.parseLong("1592498493000")).toLocalDate()));
        contactsData.add(new ContactsDto(4, new Date(Long.parseLong("1592498493000")).toLocalDate()));
        contactsData.add(new ContactsDto(4, new Date(Long.parseLong("1592498493000")).toLocalDate()));

        return contactsData;
    }
}