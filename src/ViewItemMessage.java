import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;

public class ViewItemMessage extends Message {

    String itemID;
    String vendorID;
    String category;
    Date createdAfter;
    ArrayList<Item> searchedItems;
    String info;

    public ViewItemMessage(String itemID, String vendorID, String category, Date createdAfter) {
        this.itemID = itemID;
        this.vendorID = vendorID;
        this.category = category;
        this.createdAfter = createdAfter;
        this.setType("view item");
    }

    public ViewItemMessage(ArrayList<Item> searchedItems) {
        this.searchedItems = searchedItems;
        this.setType("view item");
    }

    public String getItemID() {
        return itemID;
    }

    public String getVendorID() { return vendorID; }

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<Item> getSearchedItems() {
        return searchedItems;
    }

    public String getInfo() { return info; }
}
