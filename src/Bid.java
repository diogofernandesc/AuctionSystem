import java.io.Serializable;

public class Bid implements Serializable{

    double amount;
    String userID;

    public Bid(double amount, String userID) {
        this.amount = amount;
        this.userID = userID;
    }

    public double getAmount() {
        return amount;
    }

    public String getUserID() {
        return userID;
    }
}
