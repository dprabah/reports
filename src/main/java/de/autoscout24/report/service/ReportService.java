package de.autoscout24.report.service;

import de.autoscout24.report.data.DataProviderService;
import de.autoscout24.report.data.dto.ContactsDto;
import de.autoscout24.report.data.dto.FrequentlyContactedResult;
import de.autoscout24.report.data.dto.ListingsDto;
import de.autoscout24.report.data.dto.SellerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final DataProviderService dataProviderService;

    @Autowired
    public ReportService(DataProviderService dataProviderService) {
        this.dataProviderService = dataProviderService;
    }

    public Map<SellerType, Double> getAvgListingSellingPrice() throws Exception {
        final Map<SellerType, Double> averageResult = new HashMap<>();
        final List<ListingsDto> listings = dataProviderService.getListingData();
        Arrays.stream(SellerType.values()).forEach(sellerType -> averageResult.put(sellerType, round(listings
                .stream()
                .filter(listing -> listing.getSellerType().equals(sellerType))
                .mapToInt(ListingsDto::getPrice)
                .average().orElse(.0d), 2)));

        return averageResult;
    }

    public Map<String, Double> getPercentualDistribution() throws Exception {
        final Map<String, Double> percentualDistribution = new HashMap<>();
        final List<ListingsDto> listings = dataProviderService.getListingData();
        final Set<String> uniqueMake = listings.stream().map(ListingsDto::getMake).collect(Collectors.toSet());
        final Double totalDistribution = listings
                .stream()
                .mapToDouble(ListingsDto::getPrice)
                .sum();

        uniqueMake.forEach(make -> percentualDistribution.put(make,
                round(computePercentage(totalDistribution, listings
                        .stream()
                        .filter(listing -> listing.getMake().equals(make))
                        .mapToDouble(ListingsDto::getPrice)
                        .sum()), 2)));

        return sortHashMapByValue(percentualDistribution);
    }

    public Long getOftenContactedListings() throws Exception {
        final List<ContactsDto> contacts = dataProviderService.getContactData();
        final List<ListingsDto> listings = dataProviderService.getListingData();
        final Map<Long, Double> oftenContactedUnsorted = new HashMap<>();
        final List<Integer> totalCost = new ArrayList<>();
        final Set<Long> uniqueListings = contacts.stream().map(ContactsDto::getListingId).collect(Collectors.toSet());
        final Double totalContacts = (double) contacts.size();
        uniqueListings.forEach(make -> oftenContactedUnsorted.put(make,
                computePercentage(totalContacts, (double) contacts
                        .stream()
                        .filter(listing -> listing.getListingId() == make)
                        .count())));
        final int firstThirtyPercentage = computeXPercentage(oftenContactedUnsorted.size(), 30);
        final Map<Long, Double> oftenContactedSorted = sortHashMapByValue(oftenContactedUnsorted);
        final Map<Long, Double> topThirty = trimHashMap(oftenContactedSorted, firstThirtyPercentage);
        topThirty.keySet()
                .forEach(contactId -> {totalCost.add(listings
                        .stream()
                        .filter(x -> contactId == x.getId())
                        .findFirst()
                        .get()
                        .getPrice());
                });

        return totalCost.stream().mapToLong(x -> x).sum() / firstThirtyPercentage;
    }

    public Map<Integer, List<FrequentlyContactedResult>> getMonthlyFrequentlyContactedListings() throws Exception {

        final List<ContactsDto> contacts = dataProviderService.getContactData();
        final List<ListingsDto> listings = dataProviderService.getListingData();
        final Set<Integer> uniqueMonths = contacts.stream().map(date -> date.getContactDate().getMonthValue()).collect(Collectors.toSet());
        final Set<Long> uniqueListings = contacts.stream().map(ContactsDto::getListingId).collect(Collectors.toSet());
        final Map <Integer, List<FrequentlyContactedResult>> finalResult = new HashMap<>();
        final Map<Integer, Map<Long, Long>> monthWiseCountMap = new HashMap<>();
        uniqueMonths.forEach(month -> {
            final Map<Long, Long> listingsCount = new HashMap<>();
            uniqueListings.forEach(listing -> {
                long count = contacts.stream().filter(cl -> cl.getListingId() == listing).filter(cm -> cm.getContactDate().getMonthValue() == month).count();
                listingsCount.put(listing, count);
            });
            monthWiseCountMap.put(month, trimHashMap(sortHashMapByValue(listingsCount), 5));
        });

        monthWiseCountMap.forEach((key, value) -> {
            final List<FrequentlyContactedResult> frequentlyContactedResults = new ArrayList<>();
            final AtomicInteger counter = new AtomicInteger(0);
            value.forEach((key1, value1) -> {
                ListingsDto currentListing = listings.stream().filter(cl -> key1 == cl.getId()).findFirst().get();
                FrequentlyContactedResult current = FrequentlyContactedResult.builder()
                        .listing(currentListing)
                        .count(value1)
                        .rank(counter.incrementAndGet())
                        .build();
                frequentlyContactedResults.add(current);
            });
            finalResult.put(key, frequentlyContactedResults);
        });

        return finalResult;
    }

    private <K, V> Map<K, V> trimHashMap(Map<K, V> mapToTrim, int trimSize) {
        return mapToTrim.entrySet()
                .stream()
                .limit(trimSize)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    private <K, V> Map<K, V> sortHashMapByValue(Map<K, V> toSort){
        return toSort.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((Comparator<? super V>) Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private Double computePercentage(final Double total, final Double current){
        return current * 100 / total;
    }

    private int computeXPercentage(final int total, final int percentage){
        return (int) (total* (percentage/100.0f) );
    }

    public double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
