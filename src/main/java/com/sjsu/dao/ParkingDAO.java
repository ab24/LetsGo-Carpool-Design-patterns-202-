package com.sjsu.dao;

import com.sjsu.datastore.DataStore;
import com.sjsu.datastore.DummyData;
import com.sjsu.model.GeoLocation;
import com.sjsu.model.Parking;
import com.sjsu.request.ParkingRequest;
import org.joda.time.DateTime;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.collect.Maps.newHashMap;
import static com.sjsu.model.GeoLocation.calculateDistance;


public class ParkingDAO {

    public static Map<Parking, Double> filterParking(ParkingRequest parkingRequest) throws InvalidInputException {
        Map<Parking, Double> filteredParking = newHashMap();

        Map<String, Parking> parkingInventory = DummyData.getParkingInventory();

        Map<String, Double> availableParkingWithDistances = getAvailableParkingWithDistances(parkingRequest, parkingInventory);

        Map<String, Double> sortedAvailableParkingWithDistances = sortByValue(availableParkingWithDistances);

        for (Map.Entry<String, Double> entry : sortedAvailableParkingWithDistances.entrySet()) {
            filteredParking.put(parkingInventory.get(entry.getKey()), entry.getValue());
        }

        return filteredParking;
    }

    private static Map<String, Double> getAvailableParkingWithDistances(ParkingRequest parkingRequest, Map<String, Parking> parkingInventory) throws InvalidInputException {
        String city = parkingRequest.getCity();
        DateTime startDate = parkingRequest.getStartDate();
        DateTime endDate = parkingRequest.getEndDate();
        DateTime startTime = parkingRequest.getStartTime();
        int durationInHours = parkingRequest.getDurationInHours();

        GeoLocation requestGeoLocation = DummyData.resolveLocation(city);

        Map<String, Double> availableParkingWithDistances = newHashMap();

        for (Map.Entry<String, Parking> entry : parkingInventory.entrySet()) {
            if (entry.getValue().isSpotAvailable() && isRequestedDateInRange(startDate, endDate, entry.getValue()) && isRequestedTimeInRange(startTime, durationInHours, entry.getValue())) {
                availableParkingWithDistances.put(entry.getKey(), calculateDistance(requestGeoLocation, DummyData.resolveLocation(entry.getValue().getCity())));
            }
        }
        return availableParkingWithDistances;
    }

    private static boolean isRequestedTimeInRange(DateTime startTime, int durationInHours, Parking parking) {
        DateTime parkingStartTime = parking.getStartTime();
        DateTime parkingEndTime = parking.getEndTime();

        DateTime dateTimeAdded = startTime.plusHours(durationInHours);

        return isBetweenInclusive(parkingStartTime, parkingEndTime, startTime) && isBetweenInclusive(parkingStartTime, parkingEndTime, dateTimeAdded);
    }

    private static boolean isRequestedDateInRange(DateTime startDate, DateTime endDate, Parking parking) {
        DateTime parkingStartDate = parking.getStartDate();
        DateTime parkingEndDate = parking.getEndDate();

        return isBetweenInclusive(parkingStartDate, parkingEndDate, startDate) && isBetweenInclusive(parkingStartDate, parkingEndDate, endDate);
    }

    private static boolean isBetweenInclusive(DateTime start, DateTime end, DateTime target) {
        return !target.isBefore(start) && !target.isAfter(end);
    }

    private static GeoLocation getLocation(String postalCode) throws InvalidInputException {
        Map<String, GeoLocation> gpsLocation = DummyData.getGpsLocations();
        if (gpsLocation.containsKey(postalCode)) {
            return gpsLocation.get(postalCode);
        }
        throw new InvalidInputException("No matching location found for postal code" + postalCode);
    }

    private static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    public static void assignParking(ParkingRequest parkingRequest, Parking selectedParking) {
        DataStore.addToRequestToParkingMap(parkingRequest, selectedParking);
    }

    public static Parking getAssignedParkingForRequest(String requestId) {
        return DataStore.getRequestIdToParkingMap().get(requestId);
    }

    public static Parking getParkingById(String parkingId) {
        return DummyData.getParkingInventory().get(parkingId);
    }

}
