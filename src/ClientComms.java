import java.io.*;
import java.net.*;

public class ClientComms  {

    ObjectInputStream in;
    ObjectOutputStream out;
    Socket s;
    RegisterMessage rm;
    String str = "";
    String message;

    public ClientComms()
    {
//        try {
//            s = new Socket("127.0.0.1", 28847);
//
//            out = new ObjectOutputStream(s.getOutputStream());
//            //out.flush();
//
//            in = new ObjectInputStream(s.getInputStream());
//        }
//        catch(Exception e){}
    }
    public void start()  {
        try {




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


            //for(int i =0; i < 2; i++ ) {
                String message = (String) in.readObject();
                System.out.println("Message: " + message);
          //  }



//


        } catch(IOException e) {e.printStackTrace();

        } catch(ClassNotFoundException e) {e.printStackTrace();}


        //out.close();

    }

    protected void startConnection() {
        try {
            s = new Socket("127.0.0.1", 28847);
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
            //start();
        }catch(Exception e){}
    }

    protected void sendRegisterMessage(String givenName, String familyName, String password)  {

        try {
            rm = new RegisterMessage(givenName, familyName, password);
            out.writeObject(rm);
            start();
        } catch(Exception e) { e.printStackTrace();}
    }


    protected void receiveServerMessage() {
        try {
            System.out.println((String) in.readObject());
        } catch(Exception e1) {System.out.println(e1 + "receive error");}
    }


}

