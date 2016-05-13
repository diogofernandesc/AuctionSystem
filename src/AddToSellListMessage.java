import java.util.Date;

public class AddToSellListMessage extends Message {
    int itemID;
    String itemTitle;
    double itemRP;
    Date itemCloseTime;
    String status;
    double currentBid;

    public AddToSellListMessage(int itemID, String itemTitle, String status, double itemRP, double currentBid, Date itemCloseTime) {
        this.itemID = itemID;
        this.itemTitle = itemTitle;
        this.status = status;
        this.itemRP = itemRP;
        this.currentBid = currentBid;
        this.itemCloseTime = itemCloseTime;
        this.setType("add to sell");
    }

    public AddToSellListMessage(String itemTitle, double itemRP,  Date itemCloseTime) {
        this.itemTitle = itemTitle;
        this.itemRP = itemRP;
        this.itemCloseTime = itemCloseTime;
        this.setType("add to sell");
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public double getItemRP() {
        return itemRP;
    }

    public Date getItemCloseTime() {
        return itemCloseTime;
    }

    public String getStatus() {
        return status;
    }

    public double getCurrentBid() {
        return currentBid;
    }
}
