package tourGuide.tool;

import tourGuide.user.User;

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
}
