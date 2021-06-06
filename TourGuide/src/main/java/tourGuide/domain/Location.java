package tourGuide.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Location {
    public final double latitude;
    public final double longitude;
}
