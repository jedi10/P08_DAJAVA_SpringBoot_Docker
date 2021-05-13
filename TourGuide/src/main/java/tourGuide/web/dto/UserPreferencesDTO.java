package tourGuide.web.dto;

import org.javamoney.moneta.Money;
import tourGuide.domain.UserPreferences;

import javax.money.Monetary;

public class UserPreferencesDTO {
    private int attractionProximity = Integer.MAX_VALUE;
    private String currency = "USD";
    private int lowerPricePoint = 0;
    private int highPricePoint = Integer.MAX_VALUE;
    private int tripDuration = 1;
    private int ticketQuantity = 1;
    private int numberOfAdults = 1;
    private int numberOfChildren = 0;

    public UserPreferencesDTO(int attractionProximity, String currency, int lowerPricePoint, int highPricePoint, int tripDuration, int ticketQuantity, int numberOfAdults, int numberOfChildren) {
        this.attractionProximity = attractionProximity;
        this.currency = currency;
        this.lowerPricePoint = lowerPricePoint;
        this.highPricePoint = highPricePoint;
        this.tripDuration = tripDuration;
        this.ticketQuantity = ticketQuantity;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public static UserPreferences convertFromDTO(UserPreferencesDTO userPreferencesDTO){
        return new UserPreferences(
                userPreferencesDTO.getAttractionProximity(),
                Monetary.getCurrency(userPreferencesDTO.getCurrency()),
                Money.of(userPreferencesDTO.getLowerPricePoint(), Monetary.getCurrency(userPreferencesDTO.getCurrency())),
                Money.of(userPreferencesDTO.getHighPricePoint(), Monetary.getCurrency(userPreferencesDTO.getCurrency())),
                userPreferencesDTO.getTripDuration(),
                userPreferencesDTO.getTicketQuantity(),
                userPreferencesDTO.getNumberOfAdults(),
                userPreferencesDTO.getNumberOfChildren());
    }

    public static UserPreferencesDTO convertToDTO(UserPreferences userPreferences){
        return new UserPreferencesDTO(
                userPreferences.getAttractionProximity(),
                userPreferences.getCurrency().toString(),
                userPreferences.getLowerPricePoint().getNumber().intValue(),
                userPreferences.getHighPricePoint().getNumber().intValue(),
                userPreferences.getTripDuration(),
                userPreferences.getTicketQuantity(),
                userPreferences.getNumberOfAdults(),
                userPreferences.getNumberOfChildren());
    }

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public void setAttractionProximity(int attractionProximity) {
        this.attractionProximity = attractionProximity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getLowerPricePoint() {
        return lowerPricePoint;
    }

    public void setLowerPricePoint(int lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    public int getHighPricePoint() {
        return highPricePoint;
    }

    public void setHighPricePoint(int highPricePoint) {
        this.highPricePoint = highPricePoint;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
