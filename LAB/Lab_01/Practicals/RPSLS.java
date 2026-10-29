import java.util.Random;
import java.util.Scanner;

public class RPSLS{
    enum Move{
        ROCK,
        PAPER,
        SCISSORS,
        LIZARD,
        SPOCK
    };

    public static int winner(Move a, Move b) {

        if (a == b) {
            return 0;
        }

        switch (a) {

            case ROCK:
                if (b == Move.SCISSORS || b == Move.LIZARD) {
                    return 1;
                }
                return -1;

            case PAPER:
                if (b == Move.ROCK || b == Move.SPOCK) {
                    return 1;
                }
                return -1;

            case SCISSORS:
                if (b == Move.PAPER || b == Move.LIZARD) {
                    return 1;
                }
                return -1;

            case LIZARD:
                if (b == Move.PAPER || b == Move.SPOCK) {
                    return 1;
                }
                return -1;

            case SPOCK:
                if (b == Move.ROCK || b == Move.SCISSORS) {
                    return 1;
                }
                return -1;
        }

        return 0;

}



public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int playerScore = 0;
        int computerScore = 0;

        for (int round = 1; round <= 5; round++) {

            System.out.println("Round " + round);

            System.out.print("Enter your move (ROCK, PAPER, SCISSORS, LIZARD, SPOCK): ");
            Move playerMove = Move.valueOf(sc.next().toUpperCase());

            Move computerMove = Move.values()[random.nextInt(5)];

            System.out.println("Player Move   : " + playerMove);
            System.out.println("Computer Move : " + computerMove);

            int result = winner(playerMove, computerMove);

            if (result == 1) {
                System.out.println("You Win This Round!");
                playerScore++;
            } else if (result == -1) {
                System.out.println("Computer Wins This Round!");
                computerScore++;
            } else {
                System.out.println("Round Tied!");
            }
        }

        System.out.println("Final Result :");
        System.out.println("Player Score   : " + playerScore);
        System.out.println("Computer Score : " + computerScore);

        if (playerScore > computerScore) {
            System.out.println("You Win " + playerScore + "-" + computerScore);
        } else if (computerScore > playerScore) {
            System.out.println("Computer Wins " + computerScore + "-" + playerScore);
        } else {
            System.out.println("Match Tied!");
        }

        sc.close();
    }

}