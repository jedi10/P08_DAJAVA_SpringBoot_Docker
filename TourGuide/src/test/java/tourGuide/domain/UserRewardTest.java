package tourGuide.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pl.pojo.tester.api.FieldPredicate.exclude;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

class UserRewardTest {

    @Test
    public void userRewardPojo(){
        //GIVEN
        final Class<?> classUnderTest = UserReward.class;
        //WHEN-THEN
        assertPojoMethodsFor(classUnderTest, exclude("visitedLocation", "attraction"))
                .testing(SETTER)
                .areWellImplemented();

        assertPojoMethodsFor(classUnderTest)
                .testing(GETTER)
                .testing(CONSTRUCTOR)
                .areWellImplemented();
    }

}