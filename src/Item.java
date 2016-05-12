import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Item {

    private int itemID;
    private String title;
    private String description;
    private String catKeyword;
    private String vendorID;
    private Date startTime;
    private Date closeTime;
    private int reservePrice;
    private ArrayList<Bid> bidList;

    public Item(int itemID, String title, String description, String catKeyword, String vendorID,Date startTime, Date closeTime, int reservePrice, ArrayList<Bid> bidList) {
        this.itemID = itemID;
        this.title = title;
        this.description = description;
        this.catKeyword = catKeyword;
        this.vendorID = vendorID;
        this.startTime = new Date();
        this.closeTime = closeTime;
        this.reservePrice = reservePrice;
        this.bidList = bidList;
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

    public int getReservePrice() {
        return reservePrice;
    }

    public ArrayList<Bid> getBidList() {
        return bidList;
    }
}
