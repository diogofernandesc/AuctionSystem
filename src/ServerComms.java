import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerComms  {

    ObjectInputStream in;
    ObjectOutputStream out;
    Message message ;
    ArrayList<Message> inList;


    public void start() throws Exception {
        //28847
        ServerSocket serverSocket = new ServerSocket(28847);

        while (true) {
            Socket socket = serverSocket.accept();

//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // In from client
            in = new ObjectInputStream(socket.getInputStream());

            // Out to client
            out = new ObjectOutputStream(socket.getOutputStream());

            inList = new ArrayList<>();
            Message inMsg = null;
            inList = (ArrayList<Message>) in.readObject();

//            message = (Message) in.readObject();
//            System.out.println(in.readLine());
//            out.println(System.console().readLine());
            //socket.close();
        }

    }

    protected Message readMessage() throws Exception {
        inList.get(0);
        message = (Message) in.readObject();
        return message;
    }

    protected void response() throws IOException {
        out.writeObject("Hello");
    }
}