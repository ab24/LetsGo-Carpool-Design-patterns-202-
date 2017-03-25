package com.sjsu.datastore;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sjsu.client.FileUtil;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.RequestDAO;
import com.sjsu.member.Driver;
import com.sjsu.member.Member;
import com.sjsu.member.Rider;
import com.sjsu.model.GeoLocation;
import com.sjsu.model.MemberId;
import com.sjsu.model.Parking;
import com.sjsu.request.DriveRequest;
import com.sjsu.request.ParkingRequest;
import com.sjsu.request.RideRequest;
import com.sjsu.request.state.RequestWaitingState;
import com.sjsu.routing.RouteManager;
import com.sjsu.scheduler.Algorithm1;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.sjsu.datastore.CityNamesConstants.*;


public class DummyData {
    private static Map<MemberId, Member> memberDetails = new HashMap<>();
    private static Map<String, Parking> parkingInventory = newHashMap();
    private static Map<String, GeoLocation> gpsLocations = newHashMap();
    private static List<MemberId> driverIds = Lists.newArrayList();

    private static DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
    private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yy");


    public static void init() throws FileNotFoundException {
        memberDetails = loadMemberDetails();
        //driverIds = addDrivers(memberDetails);
        parkingInventory = loadParkingInventory();
        gpsLocations = loadLocations();

        createRideRequests();
        createDriveRequests();
        createParkingRequests();
        createOfferParkingRequest();

    }

    private static void createOfferParkingRequest() {

    }

    private static void createParkingRequests() {
        ParkingRequest parkingRequest = new ParkingRequest(new Algorithm1(new RouteManager()));
        parkingRequest.setDurationInHours(2);

    }

    private static void createDriveRequests() {
        DriveRequest driveRequest1 = new DriveRequest(new Algorithm1(new RouteManager()));
        driveRequest1.setNoOfSeats(0);
        driveRequest1.setStartingAddress(SanFrancisco);
        driveRequest1.setEndingAddress(SanJose);
        driveRequest1.setStartDate(dateFormatter.parseDateTime("08/17/16"));
        driveRequest1.setEndDate(dateFormatter.parseDateTime("09/29/16"));
        driveRequest1.setStartTime(timeFormatter.parseDateTime("09:55"));
        driveRequest1.setMemberId(MemberId.newId(10003 + ""));
        driveRequest1.setRequestState(new RequestWaitingState(driveRequest1));

        RequestDAO.getRequestDAO().save(driveRequest1);

        DriveRequest driveRequest2 = new DriveRequest(new Algorithm1(new RouteManager()));
        driveRequest2.setNoOfSeats(0);
        driveRequest2.setStartingAddress(SanJose);
        driveRequest2.setEndingAddress(SanFrancisco);
        driveRequest2.setStartDate(dateFormatter.parseDateTime("10/18/16"));
        driveRequest2.setEndDate(dateFormatter.parseDateTime("10/28/16"));
        driveRequest2.setStartTime(timeFormatter.parseDateTime("09:00"));
        driveRequest2.setMemberId(MemberId.newId(10005 + ""));
        driveRequest2.setRequestState(new RequestWaitingState(driveRequest2));

        RequestDAO.getRequestDAO().save(driveRequest2);

        DriveRequest driveRequest3 = new DriveRequest(new Algorithm1(new RouteManager()));
        driveRequest3.setNoOfSeats(0);
        driveRequest3.setStartingAddress(Millbrea);
        driveRequest3.setEndingAddress(SanJose);
        driveRequest3.setStartDate(dateFormatter.parseDateTime("08/17/16"));
        driveRequest3.setEndDate(dateFormatter.parseDateTime("09/29/16"));
        driveRequest3.setStartTime(timeFormatter.parseDateTime("09:55"));
        driveRequest3.setMemberId(MemberId.newId(10005 + ""));
        driveRequest3.setRequestState(new RequestWaitingState(driveRequest3));

        RequestDAO.getRequestDAO().save(driveRequest3);

    }

    private static void createRideRequests() {

        RideRequest rideRequest1 = new RideRequest(new Algorithm1(new RouteManager()));
        rideRequest1.setMemberId(MemberId.newId(10001 + ""));
        rideRequest1.setStartingAddress(CityNamesConstants.SanMateo);
        rideRequest1.setEndingAddress(CityNamesConstants.Sunnyvale);
        rideRequest1.setStartDate(dateFormatter.parseDateTime("08/18/16"));
        rideRequest1.setEndDate(dateFormatter.parseDateTime("09/24/16"));
        rideRequest1.setStartTime(timeFormatter.parseDateTime("10:00"));
        rideRequest1.setNoOfPassengers(2);
        rideRequest1.setRequestState(new RequestWaitingState(rideRequest1));

        RequestDAO.getRequestDAO().save(rideRequest1);

        RideRequest rideRequest2 = new RideRequest(new Algorithm1(new RouteManager()));
        rideRequest2.setMemberId(MemberId.newId(10002 + ""));
        rideRequest2.setStartingAddress(CityNamesConstants.PaloAlto);
        rideRequest2.setEndingAddress(CityNamesConstants.SanJose);
        rideRequest2.setStartDate(dateFormatter.parseDateTime("08/18/16"));
        rideRequest2.setEndDate(dateFormatter.parseDateTime("09/24/16"));
        rideRequest2.setStartTime(timeFormatter.parseDateTime("10:10"));
        rideRequest2.setNoOfPassengers(2);
        rideRequest2.setRequestState(new RequestWaitingState(rideRequest2));

        RequestDAO.getRequestDAO().save(rideRequest2);

        RideRequest rideRequest3 = new RideRequest(new Algorithm1(new RouteManager()));
        rideRequest3.setMemberId(MemberId.newId(10004 + ""));
        rideRequest3.setStartingAddress(CityNamesConstants.SanFrancisco);
        rideRequest3.setEndingAddress(CityNamesConstants.SanJose);
        rideRequest3.setStartDate(dateFormatter.parseDateTime("08/16/16"));
        rideRequest3.setEndDate(dateFormatter.parseDateTime("09/24/16"));
        rideRequest3.setStartTime(timeFormatter.parseDateTime("09:50"));
        rideRequest3.setNoOfPassengers(2);
        rideRequest3.setRequestState(new RequestWaitingState(rideRequest3));

        RequestDAO.getRequestDAO().save(rideRequest3);

        RideRequest rideRequest4 = new RideRequest(new Algorithm1(new RouteManager()));
        rideRequest4.setMemberId(MemberId.newId(10001 + ""));
        rideRequest4.setStartingAddress(CityNamesConstants.MountainView);
        rideRequest4.setEndingAddress(CityNamesConstants.SanBruno);
        rideRequest4.setStartDate(dateFormatter.parseDateTime("10/18/16"));
        rideRequest4.setEndDate(dateFormatter.parseDateTime("10/28/16"));
        rideRequest4.setStartTime(timeFormatter.parseDateTime("09:00"));
        rideRequest4.setNoOfPassengers(2);
        rideRequest4.setRequestState(new RequestWaitingState(rideRequest4));

        RequestDAO.getRequestDAO().save(rideRequest4);

        RideRequest rideRequest5 = new RideRequest(new Algorithm1(new RouteManager()));
        rideRequest5.setMemberId(MemberId.newId(10002 + ""));
        rideRequest5.setStartingAddress(CityNamesConstants.PaloAlto);
        rideRequest5.setEndingAddress(CityNamesConstants.Millbrea);
        rideRequest5.setStartDate(dateFormatter.parseDateTime("10/20/16"));
        rideRequest5.setEndDate(dateFormatter.parseDateTime("10/27/16"));
        rideRequest5.setStartTime(timeFormatter.parseDateTime("09:10"));
        rideRequest5.setNoOfPassengers(2);
        rideRequest5.setRequestState(new RequestWaitingState(rideRequest5));

        RequestDAO.getRequestDAO().save(rideRequest5);
    }


    private static Map<String, GeoLocation> loadLocations() {
        GeoLocation sunnyvale = new GeoLocation();
        sunnyvale.setLatitude(37.3817410);
        sunnyvale.setLongitude(-122.0293660);
        gpsLocations.put(Sunnyvale, sunnyvale);

        GeoLocation sanJose = new GeoLocation();
        sanJose.setLatitude(37.3382080);
        sanJose.setLongitude(-121.8863290);
        gpsLocations.put(SanJose, sanJose);

        GeoLocation mountainView = new GeoLocation();
        mountainView.setLatitude(37.3860520);
        mountainView.setLongitude(-122.0838510);
        gpsLocations.put(MountainView, mountainView);

        GeoLocation paloAlto = new GeoLocation();
        paloAlto.setLatitude(37.4418830);
        paloAlto.setLongitude(-122.1430190);
        gpsLocations.put(PaloAlto, paloAlto);

        GeoLocation milbrea = new GeoLocation();
        milbrea.setLatitude(37.5985470);
        milbrea.setLongitude(-122.3871940);
        gpsLocations.put(Millbrea, milbrea);

        GeoLocation redwoodCity = new GeoLocation();
        redwoodCity.setLatitude(37.4854210);
        redwoodCity.setLongitude(-122.2319200);
        gpsLocations.put(RedwoodCity, redwoodCity);

        GeoLocation sanMateo = new GeoLocation();
        sanMateo.setLatitude(37.5629920);
        sanMateo.setLongitude(-122.3255250);
        gpsLocations.put(SanMateo, sanMateo);

        GeoLocation sanBruno = new GeoLocation();
        sanBruno.setLatitude(37.6304900);
        sanBruno.setLongitude(-122.4110840);
        gpsLocations.put(SanBruno, sanBruno);

        GeoLocation sanFrancisco = new GeoLocation();
        sanFrancisco.setLatitude(37.7749300);
        sanFrancisco.setLongitude(-122.4194160);
        gpsLocations.put(SanFrancisco, sanFrancisco);

        return gpsLocations;
    }

    private static Map<String, Parking> loadParkingInventory() throws FileNotFoundException {
        List<Parking> parkings = FileUtil.fromFile(Parking.class);

        parkingInventory = Maps.uniqueIndex(parkings, new Function<Parking, String>() {
            @Override
            public String apply(Parking parking) {
                return parking.getParkingId();
            }
        });

        return parkingInventory;

    }

    private static Map<MemberId, Member> loadMemberDetails() throws FileNotFoundException {
        Map<MemberId, Member> memberIdRiderMap = loadRiderDetails();
        Map<MemberId, Member> memberIdDriverMap = loadDriverDetails();

        memberDetails.putAll(memberIdDriverMap);
        memberDetails.putAll(memberIdRiderMap);
        return memberDetails;
    }

    private static Map<MemberId, Member> loadRiderDetails() throws FileNotFoundException {
        List<Rider> riders = FileUtil.fromFile(Rider.class);

        List<Rider> members = newArrayList(riders);

        ImmutableMap<MemberId, Rider> memberIdMemberImmutableMap = Maps.uniqueIndex(members, new Function<Rider, MemberId>() {
            @Override
            public MemberId apply(Rider member) {
                return member.getMemberId();
            }
        });

        return Maps.newHashMap(memberIdMemberImmutableMap);
    }

    private static Map<MemberId, Member> loadDriverDetails() throws FileNotFoundException {
        List<Driver> drivers = FileUtil.fromFile(Driver.class);

        List<Driver> members = newArrayList(drivers);

        ImmutableMap<MemberId, Driver> memberIdMemberImmutableMap = Maps.uniqueIndex(members, new Function<Driver, MemberId>() {
            @Override
            public MemberId apply(Driver member) {
                return member.getMemberId();
            }
        });

        return Maps.newHashMap(memberIdMemberImmutableMap);
    }

    public static Map<MemberId, Member> getMemberDetails() {
        return memberDetails;
    }

    public static Map<String, Parking> getParkingInventory() {
        return parkingInventory;
    }

    public static Map<String, GeoLocation> getGpsLocations() {
        return gpsLocations;
    }

    public static GeoLocation resolveLocation(String cityName) throws InvalidInputException {
        Map<String, GeoLocation> gpsLocation = DummyData.getGpsLocations();
        if (gpsLocation.containsKey(cityName)) {
            return gpsLocation.get(cityName);
        }
        throw new InvalidInputException("No matching location found for " + cityName);
    }
}
