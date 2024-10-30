package nl._42.springai.hackathon.testdata.user;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VectorStoreUser {

    public final Long id;
    public final String name;
    public final LocalDate dateOfBirth;
    public final Long age;
    public final String address;
    public final List<String> activities;

    public static VectorStoreUser fromUser(User user, List<UserActivity> activities) {
        var mappedActivities = activities.stream()
                .map(activity -> String.join("|", activity.getActionTimestamp().toString(), activity.getAction().toString(), activity.getUserId().toString()))
                .toList();
        return new VectorStoreUser(user.getId(), user.getName(), user.getDateOfBirth(), user.getAge(), user.getAddress(), mappedActivities);
    }

}
