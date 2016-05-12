
public class SignInMessage extends Message {

    String userID;
    String password;

    public SignInMessage(String userID, String password){
        this.userID = userID;
        this.password = password;
        this.setType("sign in");
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }
}
