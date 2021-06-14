package com.tourguide.gpsutils.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface IGpsUtilsService {

    /**
     * <b>getUserLocation for a given user</b>
     * @param userId mandatory userId
     * @return get VisitedLocation for given user
     */
    VisitedLocation getUserLocation(UUID userId);

    /**
     * <b>get list of all Attractions</b>     *
     * @return list of attractions
     */
    List<Attraction> getAttractions();
}
