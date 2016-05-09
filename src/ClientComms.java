import java.io.*;
import java.net.*;

public class ClientComms  {

    ObjectInputStream in;
    ObjectOutputStream out;
    RegisterMessage rm;
    String str = "";
    String message;

    public void start()  {
        try {

            Socket socket = new Socket("127.0.0.1", 28847);

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());


            //

            //
            //        while((str = (String) in.readObject()) != null) {
            //            System.out.println(str);
            //            out.writeObject("bye");
            //        }
            //
//            while ((message = (String)in.readObject()) != null) {
//                message = (String) in.readObject();
//                System.out.println("Message: " + message);
//            }


            for(int i =0; i < 2; i++ ) {
                String message = (String) in.readObject();
                System.out.println("Message: " + message);
            }



//


        } catch(IOException e) {e.printStackTrace();

        } catch(ClassNotFoundException e) {e.printStackTrace();}


        //out.close();

    }

    protected void sendRegisterMessage(String givenName, String familyName, String password)  {

        try {
            rm = new RegisterMessage(givenName, familyName, password);
            out.writeObject(rm);

        } catch(Exception e) { e.printStackTrace();}
    }


    protected void receiveServerMessage() {
        try {
            System.out.println((String) in.readObject());
        } catch(Exception e1) {System.out.println(e1 + "receive error");}
    }


}

