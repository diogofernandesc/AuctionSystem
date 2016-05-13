import java.util.ArrayList;

/*
 * Reset the table when user presses reset search
 */
public class ResetTableMessage extends Message {

    String info;
    ArrayList<Item> resetList;

    public ResetTableMessage() {
        info = "reset table";
        this.setType("reset table");
    }

    public ResetTableMessage(ArrayList<Item> list) {
        resetList = list;
        this.setType("reset table");
    }

    public String getInfo() { return info; }

    public ArrayList<Item> getResetList() {
        return resetList;
    }
}
