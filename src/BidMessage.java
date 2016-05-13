
public class BidMessage extends Message {

    int itemID;
    double bidAmount;
    String userID;
    String response;
    Item item;

    public BidMessage(int itemID, double bidAmount, String userID) {
        this.itemID = itemID;
        this.bidAmount = bidAmount;
        this.userID = userID;
        this.setType("bid");
    }

    public BidMessage(String response, double bidAmount, Item item ) {
        this.response = response;
        this.bidAmount = bidAmount;
        this.item = item;
        this.setType("bid");
    }

    public Item getItem() {
        return item;
    }

    public BidMessage(String response) {
        this.response = response;
        this.setType("bid");
    }

    public String getResponse() {
        return response;
    }

    public int getItemID() {
        return itemID;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public String getUserID() {
        return userID;
    }
}
