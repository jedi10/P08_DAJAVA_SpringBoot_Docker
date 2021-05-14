package tourGuide.domain;

import org.junit.jupiter.api.Test;

import static pl.pojo.tester.api.FieldPredicate.exclude;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;
import static pl.pojo.tester.api.assertion.Method.*;

class UserTest {

    @Test
    public void userPojo(){
        //GIVEN
        final Class<?> classUnderTest = User.class;
        //WHEN-THEN
        assertPojoMethodsFor(classUnderTest, exclude("userId", "userName"))
                .testing(SETTER)
                .areWellImplemented();

        assertPojoMethodsFor(classUnderTest)
                .testing(GETTER)
                .testing(CONSTRUCTOR)
                .areWellImplemented();
    }

}