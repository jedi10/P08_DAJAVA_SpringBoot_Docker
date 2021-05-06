package tourGuide.web.dto;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import rewardCentral.RewardCentral;
import tourGuide.domain.NearByAttraction;
import tourGuide.domain.User;
import tourGuide.domain.UserPreferences;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NearByAttractionDTOTest {

    @Mock
    private RewardCentral rewardCentral;

    @Mock
    private User user;

    @Mock
    private NearByAttraction nearByAttraction;

    @InjectMocks
    private NearByAttractionDTO nearByAttractionDTO;


    @Order(1)
    @Test
    public void nearByAttractionDTOPojo(){
        //GIVEN
        final Class<?> classUnderTest = NearByAttractionDTO.class;
        //WHEN-THEN
        assertPojoMethodsFor(classUnderTest)
                .testing(SETTER)
                .testing(GETTER)
                .testing(CONSTRUCTOR)
                .areWellImplemented();
    }

    @Order(2)
    @Test
    public void calculateAttractionRewards(){
        //GIVEN
        when(rewardCentral.getAttractionRewardPoints(any(), any())).thenReturn(321);

        //WHEN
        nearByAttractionDTO.calculateAttractionRewards(nearByAttraction, user, rewardCentral);

        //THEN
        verify(rewardCentral, Mockito.times(2)).getAttractionRewardPoints(any(), any());
        assertEquals(321 , nearByAttractionDTO.getRewardsPoints());
    }
}