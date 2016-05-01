import java.util.Random;

public class User {

    private Random random = new Random();

    private String givenName;
    private String familyName;
    private int userID;
    private String password;

    public User(String givenName, String familyName, String password) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.userID = generateID();
        this.password = password;
    }

    protected int generateID() {
        int n = random.nextInt(1001);
        return n;
    }
}