import javax.swing.text.View;
import java.io.*;
import java.net.*;


public class ServerComms {

    ObjectInputStream in;
    ObjectOutputStream out;
    Message message;
    private ServerSocket serverSocket;
    String response;
    Socket socket;
    Thread t;
    Server server;

    public ServerComms(Server server) throws IOException {
        this.server = server;
        serverSocket = new ServerSocket(28847);
        response = null;
        t = new Handler();
        t.start();

    }

    public class Handler extends Thread {

        private boolean stop = false;

        public Handler() {
            try {
                socket = serverSocket.accept();
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch(IOException e) {e.printStackTrace();}
        }

        /*
         * This executes for as long as there is users
         * Each message has a type and this will check each messages type
         * To know what message to send out back to the client
         */
        public void run() {
            while(!stop) {
                try {
                    // Handle message and write out
                    message = (Message) in.readObject();

                    if (message.getType().equals("register")) {
                        receiveRegisterMessage();

                    } else if(message.getType().equals("sign in")) {
                        receiveSignInMessage();

                    } else if(message.getType().equals("sell item")) {
                        receiveSellItemMessage();

                    } else if(message.getType().equals("view item")) {
                        receiveViewItemMessage();

                    } else if(message.getType().equals("reset table")) {
                        receiveResetTableMessage();

                    } else if(message.getType().equals("bid")) {
                        receiveBidMessage();

                    } else if(message.getType().equals("add to sell")) {
                        receiveAddToSellMessage();

                    } else if(message.getType().equals("win")) {
                        receiveWinMessage();
                    }


                } catch(IOException e) {
                    System.out.println("All users have exited, please restart server");
                    stop = true;
                } catch(ClassNotFoundException e) {e.printStackTrace(); }

            }
        }

    }

    /*
     * All receive methods convert the standard message to a message of the type they send
     * E.g. receiveSignInMessage casts Message to SignInMessage
     * And then they make the server do stuff with the message by calling the methods in it
     */
    protected void receiveSignInMessage() {
        SignInMessage signInMessage = (SignInMessage) message;
        server.receiveSignInMessage(signInMessage);
    }

    protected void receiveRegisterMessage() {
        RegisterMessage registerMessage = (RegisterMessage) message;
        server.receiveRegisterMessage(registerMessage);
    }

    protected void receiveSellItemMessage() {
        SellItemMessage sellItemMessage = (SellItemMessage) message;
        server.receiveSellItemMessage(sellItemMessage);
    }

    protected void receiveViewItemMessage() {
        ViewItemMessage viewItemMessage = (ViewItemMessage) message;
        server.receiveViewItemMessage(viewItemMessage);
    }

    protected void receiveResetTableMessage() {
        ResetTableMessage resetTableMessage = (ResetTableMessage) message;
        server.receiveResetTableMessage(resetTableMessage);
    }

    protected void receiveBidMessage() {
        BidMessage bidMessage = (BidMessage) message;
        server.receiveBidMessage(bidMessage);
    }

    protected void receiveAddToSellMessage() {
        AddToSellListMessage addToSellListMessage = (AddToSellListMessage) message;
        server.receiveAddToSellMessage(addToSellListMessage);
    }

    protected void receiveWinMessage() {
        WinMessage winMessage = (WinMessage) message;
        server.receiveWinMessage(winMessage);
    }

    protected void Response(Object response)  {
        try {
            out.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}