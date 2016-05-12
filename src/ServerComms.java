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
                    }

                } catch(IOException e) {e.printStackTrace();
                } catch(ClassNotFoundException e) {e.printStackTrace(); }

            }
        }
    }

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

    protected void Response(Object response)  {
        try {
            //out.writeObject(String.valueOf(response));
            out.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}