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
            users = new HashMap<>();
            items = new HashMap<>();
            itemsList = new ArrayList<Item>();

        } catch (Exception e) {e.printStackTrace();}
    }


    /*
     * Set the key  to be the userID of the user
     * The value is the item object representing that userID
     * This method checks to see that the username in the message object
     * Is not a username that is stored in the users hashmap
     * If so the user is already registered and an invalid message is given out
     * If not registered a new user object is created and added to the users hashmap
     */

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

    /*
     * Checks to see the user hashmap contains the user using their ID
     * If so and the password matches the user, login successful
     * Otherwise a wrong username message is given, this can only indicate the user
     * has not yet registered
     */
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

    /*
     * This creates a new item based on the information in the sellItemMessage
     * It will always create a new item because the server generates a new ID
     * The message has all the components an item would need
     * However the server generates a new bid list and a new item ID for the item
     */
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

    /*
     * Gets the information from the fields the user clicked/typed/chose in the client
     * In then checks all items (given by the itemsList which is an arraylist of all the items for sale)
     * And matches category keyword, whether the start time of the time is after the user's specified time
     * Checks the vendor and item IDs
     * If so it adds these items to a searchList which is passed to a JTable to populate it with the new results
     * Giving in essence a new table with only the items matching the criteria the user chose
     */

    protected void receiveViewItemMessage(ViewItemMessage viewItemMessage) {
        ArrayList<Item> searchedItemList = new ArrayList<>();
        String itemID = viewItemMessage.getItemID();
        String vendorID = viewItemMessage.getVendorID();
        String category = viewItemMessage.getCategory();
        Date createdAfterDate = viewItemMessage.getCreatedAfter();

        for (Item item : itemsList) {
            if (item.getCatKeyword() == (category)) {
                searchedItemList.add(item);

            } else if (item.getStartTime().after(createdAfterDate)) {
                searchedItemList.add(item);

            } else if (!vendorID.equals("")) {
                if (item.getVendorID().equals(vendorID)) {
                    searchedItemList.add(item);
                }
            } else if (!itemID.equals("")) {
                if (String.valueOf(item.getItemID()).equals(itemID)){
                    searchedItemList.add(item);
                }
            }
        }

        ViewItemMessage searchedListMessage = new ViewItemMessage(searchedItemList);
        sendReply(searchedListMessage);
    }

    /*
     * Simply resets the item search table back to see all the items up for sale
     * This is done by passing the array list of all the items up for sale to the jtable in client
     */
    protected void receiveResetTableMessage(ResetTableMessage resetTableMessage) {
        if (resetTableMessage.getInfo().equals("reset table")) {
            ArrayList<Item> itemResetList = itemsList;
            ResetTableMessage resetListMessage = new ResetTableMessage(itemResetList);
            sendReply(resetListMessage);
        }
    }

    /*
     * Checks if the bid amount given in the bid message is higher than the reserve price
     * If so it checks to see that no bids have been made, if none made the new bid is the highest bid
     * It also checks the new bid amount against all the bids in the bidList for that item, if the bid is higher then
     * them all, it is set to highest
     * If the bid is highest, a new bid is added to the bidList of that item
     * A message is also sent back with the amount that was bid and what item
     * This is used to update the individual items bid page
     */

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

    /*
     * This is used to populate the " My Auctions table"
     * A status is given by the server depending on whether the current date is before, after the start date
     * or whether the current date is after the close time
     */
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
            highestBid = itemToAdd.getBidList().get(itemToAdd.getBidList().size() - 1);
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

    /*
     * This is used to tell the user whether they've won an auction or not
     * First checks to see if the currentDate is before the close time if so,
     * It will check to see after the auction has ended, if the highest Bid's userID matches that
     * of the user given in the message, then they get a "won" reply message
     * It also checks whether the bid list is empty or not, if so it sends a message saying no bids were made on the auction
     * Otherwise if none of those evaluate tot true, they get a lost message
     * The final evaluation is whether or not the current time is before the close time,
     * if so the status is set to ongoing
     * The user will have to keep checking for notifications to see this update
     */
    protected void receiveWinMessage(WinMessage winMessage) {
        Item item = winMessage.getItem();
        User user = users.get(winMessage.getUserID());
        Date currentDate = new Date();
        Bid highestBid;

        if (item.getBidList().size() == 0) {
            highestBid = null;
        } else {
            highestBid = item.getBidList().get(item.getBidList().size());
        }

        if (item.getCloseTime().before(currentDate)) {
            if (highestBid.getUserID().equals(user.getUserID())) {
                WinMessage response = new WinMessage("won", item);
                sendReply(response);

            } else if(item.getBidList().size() == 0) {
                WinMessage response = new WinMessage("no bid");
                sendReply(response);
            } else {
                WinMessage response = new WinMessage("lost",item);
                sendReply(response);

            }
        } else if (item.getCloseTime().after(currentDate)) {
            WinMessage response = new WinMessage("ongoing");
            sendReply(response);
        }
    }

    // Simply used to send a message back in each receive method
    protected void sendReply(Object message) {
        comms.Response(message);
    }


    public static void main(String[] args) {
        new Server();
    }

}
