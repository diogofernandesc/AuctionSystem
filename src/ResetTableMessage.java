import java.util.ArrayList;

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
