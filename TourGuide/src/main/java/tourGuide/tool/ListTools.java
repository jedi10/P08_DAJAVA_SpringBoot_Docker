package tourGuide.tool;

import tourGuide.domain.Attraction;
import tourGuide.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListTools {

    public static List<List<User>> switchUserList(List<User> allUsers ){
        List<User> finalAllUsers = allUsers;
        int midIndex = (((allUsers.size()) / 2) - 1);
        List<List<User> > lists = new ArrayList<>(
                allUsers.stream()
                        .collect(Collectors.partitioningBy(s -> finalAllUsers.indexOf(s) > midIndex))  //https://www.geeksforgeeks.org/split-a-list-into-two-halves-in-java/
                        .values());
        return lists;
    }

    public static List<Attraction> getAttractions() {
        List<Attraction> attractions = new ArrayList();
        attractions.add(new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D));
        attractions.add(new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767D, -110.821999D));
        attractions.add(new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689D, -115.510399D));
        attractions.add(new Attraction("Joshua Tree National Park", "Joshua Tree National Park", "CA", 33.881866D, -115.90065D));
        attractions.add(new Attraction("Buffalo National River", "St Joe", "AR", 35.985512D, -92.757652D));
        attractions.add(new Attraction("Hot Springs National Park", "Hot Springs", "AR", 34.52153D, -93.042267D));
        attractions.add(new Attraction("Kartchner Caverns State Park", "Benson", "AZ", 31.837551D, -110.347382D));
        attractions.add(new Attraction("Legend Valley", "Thornville", "OH", 39.937778D, -82.40667D));
        attractions.add(new Attraction("Flowers Bakery of London", "Flowers Bakery of London", "KY", 37.131527D, -84.07486D));
        attractions.add(new Attraction("McKinley Tower", "Anchorage", "AK", 61.218887D, -149.877502D));
        attractions.add(new Attraction("Flatiron Building", "New York City", "NY", 40.741112D, -73.989723D));
        attractions.add(new Attraction("Fallingwater", "Mill Run", "PA", 39.906113D, -79.468056D));
        attractions.add(new Attraction("Union Station", "Washington D.C.", "CA", 38.897095D, -77.006332D));
        attractions.add(new Attraction("Roger Dean Stadium", "Jupiter", "FL", 26.890959D, -80.116577D));
        attractions.add(new Attraction("Texas Memorial Stadium", "Austin", "TX", 30.283682D, -97.732536D));
        attractions.add(new Attraction("Bryant-Denny Stadium", "Tuscaloosa", "AL", 33.208973D, -87.550438D));
        attractions.add(new Attraction("Tiger Stadium", "Baton Rouge", "LA", 30.412035D, -91.183815D));
        attractions.add(new Attraction("Neyland Stadium", "Knoxville", "TN", 35.955013D, -83.925011D));
        attractions.add(new Attraction("Kyle Field", "College Station", "TX", 30.61025D, -96.339844D));
        attractions.add(new Attraction("San Diego Zoo", "San Diego", "CA", 32.735317D, -117.149048D));
        attractions.add(new Attraction("Zoo Tampa at Lowry Park", "Tampa", "FL", 28.012804D, -82.469269D));
        attractions.add(new Attraction("Franklin Park Zoo", "Boston", "MA", 42.302601D, -71.086731D));
        attractions.add(new Attraction("El Paso Zoo", "El Paso", "TX", 31.769125D, -106.44487D));
        attractions.add(new Attraction("Kansas City Zoo", "Kansas City", "MO", 39.007504D, -94.529625D));
        attractions.add(new Attraction("Bronx Zoo", "Bronx", "NY", 40.852905D, -73.872971D));
        attractions.add(new Attraction("Cinderella Castle", "Orlando", "FL", 28.419411D, -81.5812D));
        return attractions;
    }
}
