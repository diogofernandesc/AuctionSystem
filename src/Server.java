import java.time.chrono.IsoChronology;
import java.util.*;

public class Server {

    ServerComms comms;
    ArrayList<User> activeUsersList;
    ArrayList<Item> itemsList;
    Map<String, User> users;
    Map<Integer, Item> items;



    public Server()  {

        try {
            comms = new ServerComms(this);
            //comms.start();
            users = new HashMap<>();
            items = new HashMap<>();
            itemsList = new ArrayList<Item>();

        } catch (Exception e) {e.printStackTrace();}
    }


    // Set the key value to be one more than the size
    // hashmap of userID to given and family name
    protected void receiveRegisterMessage(RegisterMessage message)  {
        try {
            if (users.containsKey(message.getUsername())) {
                sendReply("invalid");

            } else {
                User user = new User(message.getUsername(), message.getGivenName(), message.getFamilyName(), message.getPassword());
                users.put(user.getUserID(), user);
                sendReply(users.get(user.getUserID()).getUserID());
            }

        } catch (Exception e) {e.printStackTrace();}
    }

    protected void receiveSignInMessage(SignInMessage message) {
        String messageUserID = message.getUserID();
        String messagePassword = message.getPassword();

        if (users.containsKey(messageUserID)) {
            if (users.get(messageUserID).getPassword().equals(messagePassword)) {
                sendReply("success,"+messageUserID);
            } else {
                sendReply("wrong password");
            }
        } else {
            sendReply("wrong username");
        }
    }

    protected void receiveSellItemMessage(SellItemMessage message) {
        int itemID = items.size() + 1;
        String messageTitle = message.getTitle();
        String messageDescription = message.getDescription();
        String messageCatKeyword = message.getCatKeyword();
        String messageVendorID = message.getVendorID();
        Date messageStartTime = message.getStartTime();
        Date messageCloseTime = message.getCloseTime();
        int messageReservePrice = message.getReservePrice();
        ArrayList<Bid> messageBidList = new ArrayList<>();

        Item item = new Item(itemID,messageTitle,messageDescription, messageCatKeyword, messageVendorID, messageStartTime, messageCloseTime, messageReservePrice, messageBidList);
        items.put(itemID, item);
        itemsList.add(item);
        SellItemMessage messageOut = new SellItemMessage(itemID,messageTitle,messageDescription, messageCatKeyword, messageVendorID,
                messageStartTime, messageCloseTime, messageReservePrice, messageBidList);
        sendReply(messageOut);
    }

    protected void receiveViewItemMessage(ViewItemMessage viewItemMessage) {
        ArrayList<Item> searchedItemList = new ArrayList<Item>();
        String itemID = viewItemMessage.getItemID();
        String category = viewItemMessage.getCategory();
        Date createdAfterDate = viewItemMessage.getCreatedAfter();
        if (!viewItemMessage.getItemID().equals("")) {
            if (items.containsKey(Integer.parseInt(itemID))) {
                searchedItemList.add(items.get(itemID));
            }
        }

        for (Item item : itemsList) {
            if (item.getCatKeyword().equals(category)) {
                searchedItemList.add(item);
            }

            if (item.getStartTime().after(createdAfterDate)) {
                searchedItemList.add(item);
            }
        }

        ViewItemMessage searchedListMessage = new ViewItemMessage(searchedItemList);
        sendReply(searchedListMessage);
    }

    protected void sendReply(Object message) {
        comms.Response(message);
    }


    public static void main(String[] args) {
        new Server();
    }

}
