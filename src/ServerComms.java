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

    }

    public class Handler extends Thread
    {
        private boolean stop;

        public Handler()
        {
            //socket setup
        }

        public void run()
        {
            while(!stop)
            {
                //getMessage...
                //Handle message and write out
            }
        }
    }

    public void start() {
        try {
            while (true) {
                socket = serverSocket.accept();
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                message = (Message) in.readObject();

            }

        } catch (IOException e) {e.printStackTrace();
        } catch(ClassNotFoundException e) {e.printStackTrace();}

    }


    public RegisterMessage readRegisterMessage() {
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