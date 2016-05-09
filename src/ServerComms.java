import java.io.*;
import java.net.*;


public class ServerComms {

    ObjectInputStream in;
    ObjectOutputStream out;
    Message message;
    private ServerSocket serverSocket;
    String response;
    Socket socket;

    public ServerComms() throws IOException {
        serverSocket = new ServerSocket(28847);
        response = null;
        socket = serverSocket.accept();
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void start() {
        try {
            while (true) {
                in = new ObjectInputStream(socket.getInputStream());
                message = (Message) in.readObject();
            }

        } catch (IOException e) {e.printStackTrace();
        } catch(ClassNotFoundException e) {e.printStackTrace();}

    }


    public Message readRegisterMessage() {
        RegisterMessage registerMessage = (RegisterMessage) message;
        return registerMessage;

    }

    protected void Response(String response)  {
        try {
            out.writeObject("hello");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}