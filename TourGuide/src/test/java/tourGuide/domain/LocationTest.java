package tourGuide.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

class LocationTest {

    @Test
    public void locationPojo(){
        //GIVEN
        final Class<?> classUnderTest = Location.class;
        //WHEN-THEN
        assertPojoMethodsFor(classUnderTest)
                .testing(GETTER)
                .testing(CONSTRUCTOR)
                .areWellImplemented();
    }

}