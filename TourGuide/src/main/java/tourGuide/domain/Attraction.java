package tourGuide.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Attraction extends Location {
    public final String attractionName;
    public final String city;
    public final String state;
    public final UUID attractionId;

    public Attraction(String attractionName, String city, String state, double latitude, double longitude) {
        super(latitude, longitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = UUID.randomUUID();
    }
}
