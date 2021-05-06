package tourGuide.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

class NearByAttractionTest {

    @Test
    public void nearByAttractionPojo(){
        //GIVEN
        final Class<?> classUnderTest = NearByAttraction.class;
        //WHEN-THEN
        assertPojoMethodsFor(classUnderTest)
                .testing(SETTER)
                .testing(GETTER)
                .testing(CONSTRUCTOR)
                .areWellImplemented();
    }

}