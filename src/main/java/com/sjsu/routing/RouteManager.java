package com.sjsu.routing;


import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.sjsu.datastore.CityNamesConstants.*;

public class RouteManager {
    private static List<String> route1 = newArrayList(SanJose, Sunnyvale, MountainView, PaloAlto, RedwoodCity, SanMateo, Millbrea, SanBruno, SanFrancisco);
    private static List<String> route2 = newArrayList(Fremont, UnionCity, Hayward, BayFair, Coliseum, WestOakland, SanFrancisco);

    private static List<List<String>> routes = newArrayList(route1, route2);

    public static List<String> getRouteDetails(String rideStartingAddress, String rideEndingAddress) {
        List<String> toReturn = Lists.newArrayList();

        if (route1.contains(rideStartingAddress) && route1.contains(rideEndingAddress)) {
            toReturn = getRouteDetails(rideStartingAddress, rideEndingAddress, route1);
        } else if (route2.contains(rideStartingAddress) && route2.contains(rideEndingAddress)) {
            toReturn = getRouteDetails(rideStartingAddress, rideEndingAddress, route2);
        }
        return toReturn;
    }

    private static List<String> getRouteDetails(String rideStartingAddress, String rideEndingAddress, List<String> route) {
        int startIndex = route.indexOf(rideStartingAddress);
        int endIndex = route.indexOf(rideEndingAddress);

        if (startIndex > endIndex) {
            return getRouteDetails(rideStartingAddress, rideEndingAddress, Lists.reverse(route));
        }

        return route.subList(startIndex, endIndex + 1);
    }
}

