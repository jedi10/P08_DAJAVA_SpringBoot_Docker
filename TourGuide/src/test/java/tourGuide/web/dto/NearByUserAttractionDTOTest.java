package tourGuide.web.dto;

import tourGuide.domain.Attraction;
import tourGuide.domain.Location;
import tourGuide.domain.VisitedLocation;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import rewardCentral.RewardCentral;
import tourGuide.domain.NearByAttraction;
import tourGuide.domain.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NearByUserAttractionDTOTest {

    private NearByUserAttractionDTO nearByUserAttractionDTO;

    @Order(1)
    @Test
    public void nearByUserAttractionDTOPojo(){
        //GIVEN
        final Class<?> classUnderTest = NearByUserAttractionDTO.class;
        //WHEN-THEN
        assertPojoMethodsFor(classUnderTest)
                .testing(SETTER)
                .testing(GETTER)
                .testing(CONSTRUCTOR)
                .areWellImplemented();
    }

    @Order(2)
    @Test
    public void convertNearByLocationToDTO(){
        //GIVEN
        User user = new User(UUID.randomUUID(), "internalUser1", "000", "jon@tourGuide.com");
        Attraction attraction = new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
        NearByAttraction nearByAttraction = new NearByAttraction(attraction, 345.00);
        List<NearByAttraction> nearByAttractionListGiven = new ArrayList<>();
        nearByAttractionListGiven.add(nearByAttraction);
        nearByAttractionListGiven.add(nearByAttraction);
        NearByAttractionDTO nearByAttractionDTO = new NearByAttractionDTO(nearByAttraction, user, new RewardCentral());

        //Avoid to call real constructor
        //when(new NearByAttractionDTO(nearByAttraction, user, new RewardCentral())).thenReturn(nearByAttractionDTO);
        nearByUserAttractionDTO = new NearByUserAttractionDTO(user,
                new VisitedLocation(user.getUserId(), new Location(10.00, 20.00), new Date()),
                new ArrayList<NearByAttraction>(),
                new RewardCentral());

        //WHEN
        nearByUserAttractionDTO.convertNearByLocationToDTO(user, nearByAttractionListGiven, new RewardCentral());

        //THEN
        assertEquals(2, nearByUserAttractionDTO.getTourist_attractions().size());

    }

}