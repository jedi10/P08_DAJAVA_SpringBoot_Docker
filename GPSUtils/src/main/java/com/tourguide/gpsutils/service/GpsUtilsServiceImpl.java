package com.tourguide.gpsutils.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * <b>Interaction with gpsUtil external libs</b>
 * <p>gpsUtil.jar has been added to project as an external library</p>
 */
@Service
public class GpsUtilsServiceImpl implements IGpsUtilsService {

    private static final Logger logger = LoggerFactory.getLogger(GpsUtilsServiceImpl.class);

    /**
     * <b>External Libs incorporated in project as Jar</b>
     */
    private GpsUtil gpsUtil;

    /**
     * Default Constructor
     */
    @Autowired
    public GpsUtilsServiceImpl(GpsUtil gpsUtil)
    {
        Locale.setDefault(Locale.US);
        this.gpsUtil = gpsUtil;
    }


    @Override
    public VisitedLocation getUserLocation(UUID userId) {
        logger.debug("Call to gpsUtils.getUserLocation({})", userId );
        return gpsUtil.getUserLocation(userId);
    }

    @Override
    public List<Attraction> getAttractions() {
        logger.debug("Call to gpsUtils.getAttractions()");
        return gpsUtil.getAttractions();
    }
}
