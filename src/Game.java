import java.util.ArrayList;
import java.util.HashMap;

public class Game{
    private int numberOfRounds;
    private Boolean[] spotterRoundResult;
    private Boolean[] dealerRoundResult;

    public Game(int numberOfRounds){
        this.numberOfRounds = numberOfRounds;
        spotterRoundResult = new Boolean[numberOfRounds];
        dealerRoundResult = new Boolean[numberOfRounds];
    }

    private int declareWinner(){
        int player1Wins = 0;
        int player2Wins = 0;
        for (int i=0; i < numberOfRounds; i++){
            if (spotterRoundResult[i]){
                player1Wins++;
            }
            if (dealerRoundResult[i]){
                player2Wins++;
            }
        }
        if (player1Wins > player2Wins){
            return 1;
        } else {
            return 2;
        }
    }

    private void determineRoundWinner(Integer spotterAnswer,
                                      Integer dealerAnswer,
                                      int roundNumber){
        if (roundNumber <= numberOfRounds){
            if (spotterAnswer == dealerAnswer){
                spotterRoundResult[roundNumber] = true;
                dealerRoundResult[roundNumber] = false;
            } else {
                spotterRoundResult[roundNumber] = false;
                dealerRoundResult[roundNumber] = true;
            }
        }
    }


    public int play(Integer[] user1Responses, Integer[] user2Responses){
        int firstRole = (int)Math.round(Math.random());
        for (int round=0; round < this.numberOfRounds; round++){
            int user1Response = user1Responses[round];
            int user2Response = user2Responses[round];
            if (firstRole == 0){
                determineRoundWinner(user1Response, user2Response, round);
            }
            if (firstRole == 1){
                determineRoundWinner(user2Response, user1Response, round);
            }
        }
        return declareWinner();
    }
}
