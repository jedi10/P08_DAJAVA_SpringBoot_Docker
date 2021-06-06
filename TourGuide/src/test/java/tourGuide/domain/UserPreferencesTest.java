package tourGuide.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pl.pojo.tester.api.FieldPredicate.exclude;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

class UserPreferencesTest {

    @Test
    public void userPreferencesPojo(){
        //GIVEN
        final Class<?> classUnderTest = UserPreferences.class;
        //WHEN-THEN
        assertPojoMethodsFor(classUnderTest, exclude("lowerPricePoint", "highPricePoint"))
                .testing(SETTER)
                .areWellImplemented();
        assertPojoMethodsFor(classUnderTest)
                .testing(GETTER)
                .testing(CONSTRUCTOR)
                .areWellImplemented();
    }
}