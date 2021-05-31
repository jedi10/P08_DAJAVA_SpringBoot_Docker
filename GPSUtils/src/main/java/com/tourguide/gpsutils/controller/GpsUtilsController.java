package com.tourguide.gpsutils.controller;

import com.tourguide.gpsutils.service.IGpsUtilsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * <b>GPSUtils Microservice EndPoints</b>
 */
@RestController
public class GpsUtilsController {
    private static final Logger logger = LoggerFactory.getLogger(GpsUtilsController.class);

    @Autowired
    private IGpsUtilsService gpsUtilsService;

    /**
     * <b>getUserLocation for a given user</b>
     * @param userId mandatory userId
     * @return get VisitedLocation for given user
     */
    @GetMapping("/userLocation")
    public VisitedLocation getUserLocation(@RequestParam UUID userId)
    {
        logger.info("GPSUtil Microservice: call to gpsUtils.getUSerLocation({})", userId );
        return gpsUtilsService.getUserLocation(userId);
    }

    /**
     * <b>get list of all Attractions</b>
     * @return list of attractions
     */
    @GetMapping("/attractions")
    public List<Attraction> getAttractions()
    {
        logger.info("GPSUtil Microservice: call to gpsUtils.getAttractions()");
        return gpsUtilsService.getAttractions();
    }
}
