package tourGuide.domain;

import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class VisitedLocation {
    public final UUID userId;
    public final Location location;
    public final Date timeVisited;
}
