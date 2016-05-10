import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    ServerComms comms;
    HashMap users;
    HashMap userPasswords;
    ArrayList<User> userList;


    public Server()  {
        userList = new ArrayList<>();
        try {
            comms = new ServerComms();
            comms.start();
            users = new HashMap();
            userPasswords = new HashMap();
            receiveRegisterMessage();
        } catch (Exception e) {e.printStackTrace();}
    }

    protected void getMessages() {
        while(true) {
            receiveRegisterMessage();
        }
    }



    // Set the key value to be one more than the size
    // hashmap of userID to given and family name
    protected void receiveRegisterMessage()  {
        try {
            //RegisterMessage message = (RegisterMessage) comms.readMessage();

            RegisterMessage message = comms.readRegisterMessage();

            userList.add(new User(message.getGivenName(), message.getFamilyName(), message.getPassword()));
            users.put(users.size() + 1, (message.getGivenName() + " " + message.getFamilyName()));
            comms.Response((String) users.get(1));


        } catch (Exception e) {e.printStackTrace();}
    }


    public static void main(String[] args) {
        new Server();
    }
}
