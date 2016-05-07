import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    ServerComms comms;
    HashMap users;
    HashMap userPasswords;


    public Server() throws Exception {
        comms = new ServerComms();
        comms.start();
        users = new HashMap();
        userPasswords = new HashMap();
        getMessages();
    }

    protected void getMessages() throws Exception {
        while (true) {
            receiveRegisterMessage();
        }
    }

    // Set the key value to be one more than the size
    // hashmap of userID to given and family name
    protected void receiveRegisterMessage() throws Exception {
        RegisterMessage message = (RegisterMessage) comms.readMessage();
        users.put(users.size() + 1, (message.getGivenName()+ " " + message.getFamilyName()));
        comms.response();
    }


    public static void main(String[] args) throws Exception {
        new Server();

    }

}
