import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private boolean isAuthenticated;
    private String state;
    private String username;
    private PrintWriter outbound;
    private DataInputStream inbound;
    protected Socket socket;

    // User States
    public static final String FIRST_COMM = "FIRST_COMM";
    public static final String ENTER_USERNAME = "ENTER_USERNAME";
    public static final String ENTER_PASSWORD = "ENTER_PASSWORD";
    public static final String IN_GAME = "IN_GAME";

    private final ArrayList<String> validStates = new ArrayList<String>(){{
        add(FIRST_COMM);
        add(ENTER_PASSWORD);
        add(ENTER_USERNAME);
        add(IN_GAME);
    }};

    User(Socket socket) throws IOException{
        this.socket = socket;
        this.state = FIRST_COMM;
        this.isAuthenticated = false;

        try {
            this.outbound = new PrintWriter(this.socket.getOutputStream(), true);
            this.inbound = new DataInputStream(this.socket.getInputStream());
        }  catch (IOException e){
            throw e;
        }

    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setIsAuthenticated(boolean authenticated){
        this.isAuthenticated = authenticated;
    }

    public String getUsername(){
        return this.username;
    }

    public boolean getIsAuthenticated(){
        return this.isAuthenticated;
    }

    public void userTransition(String newState){
        if (validStates.contains(newState)){
            this.state = newState;
        }
    }

    public void tell(String message){
        this.outbound.println(message);
    }

    public String listen() throws IOException{
        String response = this.inbound.readUTF();
        System.out.print("Client Response");
        System.out.println(response);
        return response;
    }
}
