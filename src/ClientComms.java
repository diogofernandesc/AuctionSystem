import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;

public class ClientComms {

//    BufferedReader in;
//    PrintWriter out;

    ObjectInputStream in;
    ObjectOutputStream out;
    public void start() throws Exception {

        //Scanner sc = new Scanner(System.in);
        Socket socket = new Socket("127.0.0.1", 28847);

        //Scanner inputStream = new Scanner(socket.getInputStream()); EQUIVALENT
        // In from server:
        //        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        in = new ObjectInputStream(socket.getInputStream());


        //PrintStream printStream = new PrintStream(scanner.getOutputStream()); EQUIVALENT
        // Out to server
        //        out = new PrintWriter (new OutputStreamWriter(socket.getOutputStream()),true);
        out = new ObjectOutputStream(socket.getOutputStream());

        //        LinkedList<Message> inList = new LinkedList<>();
        //        Message serverMessage = null;
        //        inList = (LinkedList<Message>) inList.readObject();
        // System.out.println("Enter any number");
        // number = sc.nextInt();

        //p.println(number); // Here you want to pass the number to the client

    }

    protected void sendRegisterMessage(String givenName, String familyName, String password)  {

        try {
            RegisterMessage rm = new RegisterMessage(givenName, familyName, password);
            out.writeObject(rm);
            out.flush();
        } catch(Exception e) { e.printStackTrace();}
    }

    protected void sendClientMessage() {

    }

    protected void sendServerMessage() {

    }

    protected void receiveServerMessage() {
        try {
            System.out.println((String) in.readObject());
        } catch(Exception e1) {System.out.println(e1 + "receive error");}
    }


}

