import java.util.ArrayList;

public class Item {

    String title;
    String description;
    String catKeyword;
    String vendorID;
    String startTime;
    String closeTime;
    int reservePrice;
    ArrayList<Bid> bidList = new ArrayList<Bid>();

    public Item(String title, String description, String catKeyword, String vendorID, String startTime, String closeTime, int reservePrice, ArrayList<Bid> bidList) {
        this.title = title;
        this.description = description;
        this.catKeyword = catKeyword;
        this.vendorID = vendorID;
        this.startTime = startTime;
        this.closeTime = closeTime;
        this.reservePrice = reservePrice;
        this.bidList = bidList;
    }

    
}
