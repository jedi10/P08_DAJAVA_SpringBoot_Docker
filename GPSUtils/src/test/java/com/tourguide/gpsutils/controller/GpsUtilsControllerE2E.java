package com.tourguide.gpsutils.controller;

import com.tourguide.gpsutils.domain.AttractionForTest;
import com.tourguide.gpsutils.domain.VisitedLocationForTest;
import gpsUtil.location.Attraction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GpsUtilsControllerE2E {

    @Autowired
    private TestRestTemplate template;
    private HttpHeaders headers;

    private final String rootUrl= "http://localhost:";

    @LocalServerPort
    private int port;

    @BeforeAll
    void setBeforeAll() throws MalformedURLException {
        //***********GIVEN*************
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void getUserLocation() {
        //***********GIVEN*************
        UUID userId = UUID.randomUUID();
        String urlTemplate = this.rootUrl + port + "/userLocation?userId=" + userId;
        //***********WHEN*************
        ResponseEntity<VisitedLocationForTest> result = template.getForEntity(
                urlTemplate,
                VisitedLocationForTest.class);
        //**************THEN***************
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());//200
        assertNotNull(result.getBody());

        VisitedLocationForTest visitedLocationResult =
                result.getBody();

        assertNotNull(visitedLocationResult);
        assertNotNull(visitedLocationResult.location);
        assertEquals(userId.toString(), visitedLocationResult.userId.toString());
    }

    @Order(2)
    @Test
    void getAttractions() {
        //GIVEN
        String urlTemplate = this.rootUrl + port + "/attractions";
        //***********WHEN*************
        ResponseEntity<AttractionForTest[]> result = template.getForEntity(
                urlTemplate,
                AttractionForTest[].class);
        //**************THEN***************
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());//200
        assertNotNull(result.getBody());

        List<AttractionForTest> attractionListResult = Arrays.asList(result.getBody());

        assertNotNull(attractionListResult);
        assertTrue(attractionListResult.size() > 1);
        assertNotNull(attractionListResult.get(0));
    }
}