package com.mikolajprzybysz.busroute.logic.repository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

/**
 * Created by dev on 12/14/2016.
 */
@Singleton
public class RoutingRepository
{
    private Map<Integer, List<Integer>> routes;

    public RoutingRepository(Map<Integer, List<Integer>> routes) {
        this.routes = routes;
    }

    public Boolean isDirectBusRoute(int from, int to)
    {
        for (Map.Entry<Integer, List<Integer>> entry : routes.entrySet()) {
            List<Integer> route = entry.getValue();

            int departureIndex = getStationIndex(route, from);
            int arrivalIndex = getStationIndex(route, to);

            // assuming it is enough two have both station
            // on the same route as probably the route description
            // is bi direction as it describes bus route
            if (0 < departureIndex && 0 < arrivalIndex) {
                // direct connection exists in this route
                return true;
            }
        }

        return false;
    }

    private int getStationIndex(List<Integer> route, int stationId)
    {
        int routeSize = route.size();

        for(int i = 0 ; i < routeSize ; i++) {
            if (route.get(i) == stationId) {
                return i;
            }
        }

        return -1;
    }
}
