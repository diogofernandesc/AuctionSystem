import java.util.Random;

public class User {


    private String givenName;
    private String familyName;
    private String password;
    private String userID;

    public User(String userID, String givenName, String familyName, String password) {
        this.userID = userID;
        this.givenName = givenName;
        this.familyName = familyName;
        this.password = password;
    }

    public String getUserID() { return userID; }

    public String getPassword() {
        return password;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }
}