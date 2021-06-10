package tourGuide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Locale.setDefault(new Locale("en", "US"));
        Locale defaultLocale = Locale.getDefault();
        System.out.println(String.format(
                "TourGuide has been launched with %s localisation",
                defaultLocale));
        //System.out.println(locale.toString());
    }

}


//https://mkyong.com/java/how-to-change-the-jvm-default-locale/
