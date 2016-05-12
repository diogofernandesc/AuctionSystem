import java.io.Serializable;

public class RegisterMessage extends Message {

    String username;
    String givenName;
    String familyName;
    String password;

    public RegisterMessage(String username, String givenName, String familyName, String password) {
        this.username = username;
        this.givenName = givenName;
        this.familyName = familyName;
        this.password = password;
        this.setType("register");
    }

    public String getUsername() { return username; }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getPassword() {
        return password;
    }
}
