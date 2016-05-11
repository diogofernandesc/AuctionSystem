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
                    receiveRegisterMessage();

                } catch(IOException e) {e.printStackTrace();
                } catch(ClassNotFoundException e) {e.printStackTrace(); }

            }
        }
    }

//    public void start() {
//        try {
//            //while (true) {
//                socket = serverSocket.accept();
//                out = new ObjectOutputStream(socket.getOutputStream());
//                in = new ObjectInputStream(socket.getInputStream());
//
//
//            //}
//
//        } catch (IOException e) {e.printStackTrace();
//        } catch(ClassNotFoundException e) {e.printStackTrace();}
//
//    }

    public void receiveRegisterMessage() {
        RegisterMessage registerMessage = (RegisterMessage) message;
        server.receiveRegisterMessage(registerMessage);
    }

    protected void Response(String response)  {
        try {
            out.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}