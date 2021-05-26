package tourGuide.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Component
@Configuration
@ConfigurationProperties(prefix = "microservice")
@Getter
@Setter
public class MicroserviceProperties {

    private String address = "http://localhost:";

    private String gpsutilPort = "8090";

}

//https://stackoverflow.com/questions/32058814/spring-boot-custom-variables-in-application-properties
//https://www.baeldung.com/intellij-resolve-spring-boot-configuration-properties
