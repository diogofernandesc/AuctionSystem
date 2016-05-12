import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;

public class ViewItemMessage extends Message {

    String itemID;
    String category;
    Date createdAfter;
    ArrayList<Item> searchedItems;

    public ViewItemMessage(String itemID, String category, Date createdAfter) {
        this.itemID = itemID;
        this.category = category;
        this.createdAfter = createdAfter;
        this.setType("view item");
    }

    public ViewItemMessage(ArrayList<Item> searchedItems) {
        this.searchedItems = searchedItems;
    }

    public String getItemID() {
        return itemID;
    }

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<Item> getSearchedItems() {
        return searchedItems;
    }
}
