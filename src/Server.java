// A Java program for a Server 
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;
    private PrintWriter out = null;

    private Game gameEngine = new Game(5);


    private ArrayList<User> activeUsers = new ArrayList<User>();

    // constructor with port
    public Server(int port)
    {
        final HashMap<String, String> registeredUsers = new HashMap<String, String>(){{
            put("dannyboi", "dre@margh_shelled");
            put("matty7", "win&win99");
        }};

        Game game = new Game(5);

        try{
            ServerSocket server = new ServerSocket(7621, 2);
            System.out.println("Server Started\nNow Listening at 7621");
            Socket socket;
            HashMap<User, Integer[]> responses = new HashMap<User, Integer[]>();
            UserResponse[] userResponses = new UserResponse[2];
            UserResponse userResponse;
            User winningUser, losingUser;
            int userCount = 0;

            while(userCount < 2){
                try{
                    userResponses[userCount] = new UserResponse(server, registeredUsers);
                    ((UserResponse)userResponses[userCount]).start();
                    userCount++;
                } catch (Exception e){
                    // TODO: Report the failure

                }
            }
            for (int response=0; response < 2; response++){
                userResponse = userResponses[response];
                try{
                    userResponse.join();
                    responses.put(
                            ((UserResponse)Thread.currentThread()).getUser(),
                            ((UserResponse)Thread.currentThread()).getResponse());
                } catch (InterruptedException e){
                    // TODO: Report the failure
                }
            }
            int result = game.play((Integer[])responses.values().toArray()[0], (Integer[])responses.values().toArray()[1]);
            if (result == 1){
                winningUser = (User)responses.keySet().toArray()[0];
                losingUser = (User)responses.keySet().toArray()[1];
            } else {
                winningUser = (User)responses.keySet().toArray()[1];
                losingUser = (User)responses.keySet().toArray()[0];
            }

            winningUser.tell("Victory");
            losingUser.tell("Defeat");

            winningUser.tell("Thanks for Playing");
            losingUser.tell("Thanks for Playing");

            winningUser.socket.close();
            losingUser.socket.close();
        } catch(IOException e){

        }
    }

    public static void main(String args[]) {
        Server server = new Server(7621);
    }
}


class UserResponse extends Thread {
    private Thread t;
    private Socket socket;
    private User user;
    private HashMap<String, String> credentials;
    private Integer[] roundResponses = new Integer[5];

    UserResponse(ServerSocket serverSocket, HashMap<String, String> credentials) throws Exception {
        super();
        this.socket = serverSocket.accept();
        try{
            this.user = new User(this.socket);
        } catch (IOException e){
            throw new Exception(e);
        }
    }

    @Override
    public synchronized void run(){
        int programState = 1;
        try {
            while (true) {
                switch (programState) {
                    case 1:
                        // Check the username
                        String username;
                        this.user.tell("Enter your username");
                        username = this.user.listen();
                        if (credentials.containsKey(username)) {
                            programState++;
                            this.user.setUsername(username);
                        } else {
                            this.user.tell("Username incorrect");
                        }
                        break;
                    case 2:
                        // Check the password
                        this.user.tell("Enter your password");
                        if (this.credentials.get(this.user.getUsername()) == this.user.listen()) {
                            programState++;
                        } else {
                            this.user.tell("Password incorrect");
                        }
                        break;
                    case 3:
                        // Play the game
                        this.user.tell("Enter 5 numbers in the range of 1 - 3 (inclusive)");
                        int response;
                        for (int round = 0; round < 5; round++) {
                                response = Integer.parseInt(this.user.listen());
                                if ((response <= 3 | response >= 1)) {
                                    roundResponses[round] = Integer.parseInt(this.user.listen());
                                }
                        }
                        break;
                }
            }
        }
        catch (IOException e){
            this.user.tell("Unexpected error occured");
        }
    }

    public Integer[] getResponse(){
        return roundResponses;
    }

    public User getUser(){
        return this.user;
    }
}


