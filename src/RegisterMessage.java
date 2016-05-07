public class RegisterMessage extends Message {

    String givenName;
    String familyName;
    String password;

    public RegisterMessage(String givenName, String familyName, String password) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.password = password;
    }

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
