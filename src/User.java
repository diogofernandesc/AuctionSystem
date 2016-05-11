import java.util.Random;

public class User {


    private String givenName;
    private String familyName;
    private String password;

    public User(String givenName, String familyName, String password) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.password = password;
    }

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