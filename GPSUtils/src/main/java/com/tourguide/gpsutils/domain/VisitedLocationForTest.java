package com.tourguide.gpsutils.domain;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class VisitedLocationForTest {
    public final UUID userId;
    public final LocationForTest location;
    public final Date timeVisited;

    public VisitedLocationForTest(UUID userId, LocationForTest location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }
}
