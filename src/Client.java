import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client {
    private Socket socket;
    private Scanner input, socket_input;
    private DataOutputStream output;
    ArrayList<String> terminationMessages = new ArrayList<String>(){{
        add("Victory");
        add("Defeat");
    }};


    public Client(String address, int port){

        try (AsynchronousSocketChannel client = AsynchronousSocketChannel.open()){
            Future<Void> result = client.connect(new InetSocketAddress("0.0.0.0", 7621));
            result.get();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Future<Integer> readval, writeval;
            String line = "";
            while(!terminationMessages.contains(line)){
                Scanner scanner = new Scanner(System.in);
                readval = client.read(buffer);
                line = new String(buffer.array()).trim();
                System.out.println(line);
                readval.get();
                buffer.flip();
                buffer = ByteBuffer.wrap(scanner.nextLine().getBytes());
                writeval = client.write(buffer);
                writeval.get();
                buffer.clear();

            }
        } catch (ExecutionException|IOException e){
            System.out.println(e);
        } catch (InterruptedException e){
            System.out.println(e);
        }

    }

    public static void main(String[] args){
        Client client = new Client("0.0.0.0", 7621);
    }
}
