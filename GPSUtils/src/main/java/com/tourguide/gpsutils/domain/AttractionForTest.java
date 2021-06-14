package com.tourguide.gpsutils.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class AttractionForTest extends LocationForTest {
    public final String attractionName;
    public final String city;
    public final String state;
    public final UUID attractionId;

    public AttractionForTest(String attractionName, String city, String state, double latitude, double longitude) {
        super(latitude, longitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttractionForTest)) return false;
        if (!super.equals(o)) return false;
        AttractionForTest that = (AttractionForTest) o;
        return attractionName.equals(that.attractionName) &&
                city.equals(that.city) &&
                state.equals(that.state) &&
                attractionId.equals(that.attractionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), attractionName, city, state, attractionId);
    }
}
