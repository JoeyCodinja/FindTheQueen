import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner input, socket_input;
    private DataOutputStream output;
    ArrayList<String> terminationMessages = new ArrayList<String>(){{
        add("Victory");
        add("Defeat");
    }};


    public Client(String address, int port) throws IOException{

        try {
            socket = new Socket(address, port);

            input = new Scanner(System.in);

            socket_input = new Scanner(socket.getInputStream());

            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e){
            System.out.println(e);
        }

        String line = "";
        ServerReader serverReader = new ServerReader(socket_input, socket);
        serverReader.start();

        while (true){
            synchronized (new Object()){
                line = input.nextLine();
                output.writeUTF(line);
                output.flush();
            }
        }
    }

    public static void main(String[] args){
        try{
            Client client = new Client("0.0.0.0", 7621);
        } catch (IOException e){
            System.out.println("Connection closed");
        }
    }

    class ServerReader extends Thread {
        Scanner inputStream;
        Socket socket;

        ServerReader(Scanner inputStream, Socket socket){
            this.inputStream = inputStream;
            this.socket = socket;
        }

        public void run(){
            ArrayList<String> terminationMessages = new ArrayList<String>(){{
                add("Victory");
                add("Defeat");
            }};
            String line="";

            while (!terminationMessages.contains(line)){
                try{
                    line = this.inputStream.nextLine();
                    System.out.println(line);
                } catch (NoSuchElementException e){
                    line = "";
                }
            }
            try{
                this.socket.close();
            } catch(IOException e){
                System.out.println("Connection closed unexpectedly");
            }
        }
    }
}
