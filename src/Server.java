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
            comms = new ServerComms(this);
            //comms.start();
            users = new HashMap();
            userPasswords = new HashMap();

        } catch (Exception e) {e.printStackTrace();}
    }


    // Set the key value to be one more than the size
    // hashmap of userID to given and family name
    protected void receiveRegisterMessage(RegisterMessage message)  {
        try {

//            User user = new User(message.getGivenName(), message.getFamilyName(), message.getPassword());
//           userList.add(new User(message.getGivenName(), message.getFamilyName(), message.getPassword()));
         //   users.put(users.size() + 1, user);
           users.put(users.size() + 1, (message.getGivenName() + " " + message.getFamilyName()));
            //comms.Response(user.getGivenName() + " " + user.getFamilyName() + " " + user.getPassword());]
            comms.Response((String) users.get(1));


        } catch (Exception e) {e.printStackTrace();}
    }


    public static void main(String[] args) {
        new Server();
    }
}
