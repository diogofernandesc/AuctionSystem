import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class SellItemMessage extends Message {

    private int itemID;
    private String title;
    private String description;
    private String catKeyword;
    private String vendorID;
    private Date startTime;
    private Date closeTime;
    private double reservePrice;
    private ArrayList<Bid> bidList;

    public SellItemMessage(String title, String description, String catKeyword, String vendorID, Date startTime, Date closeTime, double reservePrice) {
        this.title = title;
        this.description = description;
        this.catKeyword = catKeyword;
        this.vendorID = vendorID;
        this.startTime = startTime;
        this.closeTime = closeTime;
        this.reservePrice = reservePrice;
        this.setType("sell item");
    }

    // Overloaded constructors for sell item message sent from server to client
    // Server allocates a bid list
    public SellItemMessage(int itemID, String title, String description, String catKeyword, String vendorID,
                           Date startTime, Date closeTime, double reservePrice, ArrayList<Bid> bidList) {
        this.itemID = itemID;
        this.title = title;
        this.description = description;
        this.catKeyword = catKeyword;
        this.vendorID = vendorID;
        this.startTime = startTime;
        this.closeTime = closeTime;
        this.reservePrice = reservePrice;
        this.bidList = bidList;
        this.setType("sell item");
    }

    public int getItemID() {
        return itemID;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCatKeyword() {
        return catKeyword;
    }

    public String getVendorID() {
        return vendorID;
    }


    public Date getStartTime() {
        return startTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }


    public double getReservePrice() {
        return reservePrice;
    }

    public ArrayList<Bid> getBidList() {
        return bidList;
    }
}
