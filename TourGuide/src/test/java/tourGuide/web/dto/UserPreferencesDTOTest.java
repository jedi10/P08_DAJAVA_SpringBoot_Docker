package tourGuide.web.dto;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import rewardCentral.RewardCentral;
import tourGuide.domain.NearByAttraction;
import tourGuide.domain.User;
import tourGuide.domain.UserPreferences;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Order(2)
    @Test
    public void convertUserPreferencesToDTO(){
        //GIVEN
        CurrencyUnit currency = Monetary.getCurrency("USD");
        Money moneyLow = Money.of(0, Monetary.getCurrency("USD"));
        Money moneyHigh = Money.of(5000, Monetary.getCurrency("USD"));
        UserPreferences userPreferences = new UserPreferences(1000, currency,
                moneyLow, moneyHigh,
                5, 5, 2,3);

        int userHighPrice = userPreferences.getHighPricePoint().getNumber().intValue();
        int userLowPrice = userPreferences.getLowerPricePoint().getNumber().intValue();
        assertNotNull(userHighPrice);
        assertNotEquals(0, userHighPrice);

        //WHEN
        UserPreferencesDTO userPreferenceDTOResult = UserPreferencesDTO.convertToDTO(userPreferences);

        //THEN
        assertNotNull(userPreferenceDTOResult);
        assertEquals(userHighPrice, userPreferenceDTOResult.getHighPricePoint());
        assertEquals(userLowPrice, userPreferenceDTOResult.getLowerPricePoint());
    }

    @Order(3)
    @Test
    public void convertUserPreferencesFromDTO(){
        //GIVEN
        UserPreferencesDTO userPreferencesGiven = new UserPreferencesDTO(1000, "USD",
                0, 2500,
                5, 5, 2,3);

        int userHighPrice = userPreferencesGiven.getHighPricePoint();
        int userLowPrice = userPreferencesGiven.getLowerPricePoint();
        assertNotNull(userHighPrice);
        assertNotEquals(0, userHighPrice);

        //WHEN
        UserPreferences userPreferenceResult = UserPreferencesDTO.convertFromDTO(userPreferencesGiven);

        //THEN
        assertNotNull(userPreferenceResult);
        assertEquals(userHighPrice, userPreferenceResult.getHighPricePoint().getNumber().intValue());
        assertEquals(userLowPrice, userPreferenceResult.getLowerPricePoint().getNumber().intValue());
    }
}