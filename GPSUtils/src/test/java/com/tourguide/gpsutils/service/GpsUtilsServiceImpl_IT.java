package com.tourguide.gpsutils.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GpsUtilsServiceImpl_IT {

    private GpsUtil gpsUtil;

    private IGpsUtilsService gpsUtilsService;

    @BeforeEach
    void setUp() {
        this.gpsUtil = new GpsUtil();
        this.gpsUtilsService = new GpsUtilsServiceImpl(gpsUtil);
    }

    @AfterEach
    void tearDown() {
        this.gpsUtil = null;
        this.gpsUtilsService = null;
    }

    @Order(1)
    @Test
    void testResourceIntegration() {
        //given
        assertNotNull(gpsUtil);
        assertNotNull(gpsUtilsService);

    }

    @Order(2)
    @Test
    void getUserLocation() {
        //GIVEN
        UUID userId = UUID.randomUUID();
        //WHEN
        VisitedLocation visitedLocationResult = gpsUtil.getUserLocation(userId);
        //THEN
        assertNotNull(visitedLocationResult);
        assertSame(userId, visitedLocationResult.userId);
        assertNotNull(visitedLocationResult.location);
    }

    @Order(3)
    @Test
    void getAttractions() {
        //WHEN
        List<Attraction> listResult = gpsUtil.getAttractions();
        //THEN
        assertNotNull(listResult);
        assertTrue(listResult.size() > 1);
    }
}