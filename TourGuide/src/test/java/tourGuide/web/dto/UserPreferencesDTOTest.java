package tourGuide.web.dto;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserPreferencesDTOTest {

    @Order(1)
    @Test
    public void userPreferencesDTOPojo(){
        //GIVEN
        final Class<?> classUnderTest = UserPreferencesDTO.class;
        //WHEN-THEN
        assertPojoMethodsFor(classUnderTest)
                .testing(SETTER)
                .testing(GETTER)
                .testing(CONSTRUCTOR)
                .areWellImplemented();
    }
}