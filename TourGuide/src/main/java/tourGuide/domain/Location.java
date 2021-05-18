package tourGuide.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Location {
    public final double longitude;
    public final double latitude;
}
