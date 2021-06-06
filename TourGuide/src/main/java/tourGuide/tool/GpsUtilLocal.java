package tourGuide.tool;

import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tourGuide.domain.Location;
import tourGuide.domain.VisitedLocation;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
public class GpsUtilLocal {

    private static final RateLimiter rateLimiter = RateLimiter.create(1000.0D);

    public VisitedLocation getUserLocation(UUID userId) {
        rateLimiter.acquire();
        this.sleep();
        double longitude = ThreadLocalRandom.current().nextDouble(-180.0D, 180.0D);
        longitude = Double.parseDouble(String.format("%.6f", longitude));
        double latitude = ThreadLocalRandom.current().nextDouble(-85.05112878D, 85.05112878D);
        latitude = Double.parseDouble(String.format("%.6f", latitude));
        VisitedLocation visitedLocation = new VisitedLocation(userId, new Location(latitude, longitude), new Date());
        return visitedLocation;
    }

    private void sleep() {
        int random = ThreadLocalRandom.current().nextInt(30, 100);

        try {
            TimeUnit.MILLISECONDS.sleep((long)random);
        } catch (InterruptedException var3) {
        }
    }

}
