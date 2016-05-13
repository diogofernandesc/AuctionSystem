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
        double messageReservePrice = message.getReservePrice();
        ArrayList<Bid> messageBidList = new ArrayList<>();

        Item item = new Item(itemID,messageTitle,messageDescription, messageCatKeyword, messageVendorID, messageStartTime, messageCloseTime, messageReservePrice, messageBidList);
        items.put(itemID, item);
        itemsList.add(item);
        SellItemMessage messageOut = new SellItemMessage(itemID,messageTitle,messageDescription, messageCatKeyword, messageVendorID,
                messageStartTime, messageCloseTime, messageReservePrice, messageBidList);
        sendReply(messageOut);
    }

    protected void receiveViewItemMessage(ViewItemMessage viewItemMessage) {
        ArrayList<Item> searchedItemList = new ArrayList<>();
        String itemID = viewItemMessage.getItemID();
        String vendorID = viewItemMessage.getVendorID();
        String category = viewItemMessage.getCategory();
        System.out.println("gotten category is: " + category);
        Date createdAfterDate = viewItemMessage.getCreatedAfter();
        if (!viewItemMessage.getItemID().equals("")) {
            if (items.containsKey(Integer.parseInt(itemID))) {
                searchedItemList.add(items.get(itemID));
            }
        }

        for (Item item : itemsList) {
            if (item.getCatKeyword() == (category)) {
                searchedItemList.add(item);

            } else if (item.getStartTime().after(createdAfterDate)) {
                searchedItemList.add(item);

            } else if (!vendorID.equals("")) {
                if (item.getVendorID().equals(vendorID)) {
                    searchedItemList.add(item);
                }
            }
        }

        ViewItemMessage searchedListMessage = new ViewItemMessage(searchedItemList);
        sendReply(searchedListMessage);
    }

    protected void receiveResetTableMessage(ResetTableMessage resetTableMessage) {
        if (resetTableMessage.getInfo().equals("reset table")) {
            ArrayList<Item> itemResetList = itemsList;
            ResetTableMessage resetListMessage = new ResetTableMessage(itemResetList);
            sendReply(resetListMessage);
        }
    }

    protected void receiveBidMessage(BidMessage bidMessage) {
        boolean highest = false; // To start off with the bid is not the highest, has to be checked
        int itemID = bidMessage.getItemID();
        double bidAmount = bidMessage.getBidAmount();
        String userID = bidMessage.getUserID();
        Item item = items.get(itemID);
        double itemReservePrice = item.getReservePrice();
        if(bidAmount > itemReservePrice) {
            if (item.getBidList().size() < 1) {
                highest = true;
            }
            for(Bid bid: item.getBidList()) {
                if (bidAmount > bid.getAmount()) {
                    highest = true;
                } else if (bidAmount < bid.getAmount()){
                    highest = false;
                }
            }
        }

        if (highest) {
            item.getBidList().add(new Bid(bidAmount, userID));
            BidMessage message = new BidMessage("success", bidAmount,item);
            sendReply(message);
        } else {
            BidMessage message = new BidMessage("fail");
            sendReply(message);
        }
    }

    protected void receiveAddToSellMessage(AddToSellListMessage addToSellListMessage) {
        Item itemToAdd = null;
        Bid highestBid = null;
        for (Item item: itemsList) {
           if (item.getTitle().equals(addToSellListMessage.getItemTitle())) {
               itemToAdd = item;
           }
        }
        if (itemToAdd.getBidList().size() == 0) {
            highestBid = new Bid(0,itemToAdd.getVendorID());
        } else {
            highestBid = itemToAdd.getBidList().get(itemToAdd.getBidList().size());
        }

        String status = null;
        Date currentDate = new Date();
        if (currentDate.before(itemToAdd.getStartTime())) {
            status = "Starting soon";
        } else if (currentDate.after(itemToAdd.getStartTime())) {
            status = "In progress";

        } else if (currentDate.after(itemToAdd.getCloseTime())) {
            status = "finished";
        }

        AddToSellListMessage message = new AddToSellListMessage(itemToAdd.getItemID(), itemToAdd.getTitle(), status, itemToAdd.getReservePrice(), highestBid.getAmount(), itemToAdd.getCloseTime());
        sendReply(message);
    }

    protected void sendReply(Object message) {
        comms.Response(message);
    }


    public static void main(String[] args) {
        new Server();
    }

}
