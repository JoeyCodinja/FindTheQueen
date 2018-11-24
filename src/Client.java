import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner input, socket_input;
    private DataOutputStream output;
    ArrayList<String> terminationMessages = new ArrayList<String>(){{
        add("Victory");
        add("Defeat");
    }};


    public Client(String address, int port){

        try {
            socket = new Socket(address, port);

            input = new Scanner(System.in);

            socket_input = new Scanner(socket.getInputStream());

            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e){
            System.out.println(e);
        }

        String line = "";

        while (!terminationMessages.contains(line)){
            synchronized (new Object()){
                try{
                    line = socket_input.nextLine();
                    System.out.println(line);
                    System.out.print("Your response: ");
                    line = input.nextLine();
                    output.writeUTF(line);
                    output.flush();
                } catch (IOException e){
                    System.out.println(e);
                }
            }
        }

        try{
            input.close();
            output.close();
            socket.close();
        } catch (IOException e){
            System.out.println(e);
        }


    }

    public static void main(String[] args){
        Client client = new Client("0.0.0.0", 7621);
    }
}
