import com.sun.org.glassfish.external.statistics.annotations.Reset;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

public class ClientComms  {

    Client client;
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket s;
    RegisterMessage rm;
    SignInMessage sm;
    SellItemMessage sellItemMessage;
    ViewItemMessage viewItemMessage;
    ResetTableMessage resetTableMessage;
    BidMessage bidMessage;
    AddToSellListMessage addToSellListMessage;
    String str = "";
    String messageExpected;
    Boolean readReady;
    Thread t;
    Boolean receive = false;
    Client.Display window;

    public ClientComms(Client client, Client.Display window) {
        this.client = client;
        this.window = window;

        t = new ReceiveThread();
    }

    protected void startConnection() {
        try {
            s = new Socket("127.0.0.1", 28847);
            out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            in = new ObjectInputStream(s.getInputStream());
            t.start();
        }catch(Exception e){}
    }

    protected void sendRegisterMessage(String username, String givenName, String familyName, String password)  {

        try {
            rm = new RegisterMessage(username, givenName, familyName, password);
            out.writeObject(rm);
            messageExpected = "register";
        } catch(Exception e) { e.printStackTrace();}
    }

    protected void sendSignInMessage(String username, String password) {
        try {
            sm = new SignInMessage(username, password);
            out.writeObject(sm);
            messageExpected = "sign in";
        } catch(IOException e) {e.printStackTrace();}
    }

    protected void sendSellItemMessage(String title, String description, String catKeyword, String vendorID, Date startTime, Date closeTime, double reservePrice) {
        try {
            sellItemMessage = new SellItemMessage(title, description, catKeyword, vendorID,startTime, closeTime, reservePrice);
            out.writeObject(sellItemMessage);
            messageExpected = "sell item";

        } catch (IOException e) {e.printStackTrace();}
    }

    protected void sendViewItemMessage(String itemID, String vendorID, String category, Date createdAfter) {
        try {
            viewItemMessage = new ViewItemMessage(itemID, vendorID,category, createdAfter);
            out.writeObject(viewItemMessage);
            messageExpected = "view item";
        } catch(IOException e) {e.printStackTrace();}
    }

    protected void sendResetTableMessage() {
        try {
            resetTableMessage = new ResetTableMessage();
            out.writeObject(resetTableMessage);
            messageExpected = "reset table";
        } catch(IOException e) {e.printStackTrace();}
    }

    protected void sendBidMessage(int itemID, double bidAmount, String userID) {
        try {
            bidMessage = new BidMessage(itemID, bidAmount, userID);
            out.writeObject(bidMessage);
            messageExpected = "bid";
        } catch(IOException e) {e.printStackTrace();}
    }

    protected void sendAddToSellListMessage(String title, double rp, Date closeTime) {
        try {
            addToSellListMessage = new AddToSellListMessage(title, rp, closeTime);
            out.writeObject(addToSellListMessage);
            messageExpected = "add to sell";
        } catch(IOException e) {e.printStackTrace();}
    }

    protected void receiveRegisterMessage(String message) {
        try {
            window.receiveRegisterMessage(message);
            //client.receiveRegisterMessage(message);
        } catch(Exception e1) {System.out.println(e1 + "receive error");}
    }

    protected void receiveSignInMessage(String message) {
        window.receiveSignInMessage(message);

    }

    protected void receiveSellItemMessage(SellItemMessage message) {
        window.receiveSellItemMessage(message);

    }

    protected void receiveViewItemMessage(ViewItemMessage message) {
        window.receiveViewItemMessage(message);
    }

    protected void receiveResetTableMessage(ResetTableMessage message) {
         window.receiveResetTableMessage(message);
    }

    protected void receiveBidMessage(BidMessage message) {
        window.receiveBidMessage(message);
    }

    protected void receiveAddToSellMessage(AddToSellListMessage message) {
        window.receiveAddToSellMessage(message);
    }

    class ReceiveThread extends Thread {

        public void run() {
            while (true) {
                try {
                    Object message = in.readObject();
                    if (messageExpected.equals("register")) {
                        receiveRegisterMessage((String)message);
                        System.out.println("Message: " + message);

                    } else if (messageExpected.equals("sign in")) {
                        receiveSignInMessage((String)message);
                        System.out.println("Message: " + message);

                    } else if (messageExpected.equals("sell item")) {
                        receiveSellItemMessage((SellItemMessage)message);
                        System.out.println("Message: " + message);

                    } else if(messageExpected.equals("view item")) {
                        receiveViewItemMessage((ViewItemMessage) message);
                        System.out.println("Message: " + message);

                    } else if(messageExpected.equals("reset table")) {
                        receiveResetTableMessage((ResetTableMessage) message);
                        System.out.println("Message: " + message);

                    } else if(messageExpected.equals("bid")) {
                        receiveBidMessage((BidMessage) message);
                        System.out.println("Message: " + message);

                    } else if(messageExpected.equals("add to sell")) {
                        receiveAddToSellMessage((AddToSellListMessage) message);
                        System.out.println("Message: " + message);
                        //messageExpected changes to receiveNotification all the other times
                        // changes when u call the receive
                    }

                } catch(IOException e) {e.printStackTrace();
                } catch(ClassNotFoundException e) {e.printStackTrace();}

            }

        }
    }


}

