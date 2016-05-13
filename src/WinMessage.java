
public class WinMessage extends Message {

    Item item;
    String userID;
    String response;

    public WinMessage(Item item, String userID) {
        this.item = item;
        this.userID = userID;
        this.setType("win");
    }

    public WinMessage(String response, Item item) {
        this.response = response;
        this.item = item;
        this.setType("win");
    }
    public WinMessage(String response) {
        this.response = response;
        this.setType("win");
    }


    public String getResponse() {
        return response;
    }

    public String getUserID() {
        return userID;
    }

    public Item getItem() {
        return item;
    }
}
