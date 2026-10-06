import java.util.Random;
import java.util.Scanner;

public class RPSLS {

    enum Move { ROCK, PAPER, SCISSORS, LIZARD, SPOCK }

    /** 1 if a beats b, -1 if b beats a, 0 for a tie. */
    static int winner(Move a, Move b) {
        if (a == b) return 0;                          // same move -> tie
        // The switch lists the two moves that 'a' defeats; if b is one of
        // them, a wins, otherwise b wins.
        boolean aWins = switch (a) {
            case ROCK     -> b == Move.LIZARD   || b == Move.SCISSORS;
            case PAPER    -> b == Move.ROCK     || b == Move.SPOCK;
            case SCISSORS -> b == Move.PAPER    || b == Move.LIZARD;
            case LIZARD   -> b == Move.SPOCK    || b == Move.PAPER;
            case SPOCK    -> b == Move.SCISSORS || b == Move.ROCK;
        };
        return aWins ? 1 : -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rng = new Random();
        Move[] moves = Move.values();      // all five moves, as an array
        int you = 0, cpu = 0;

        System.out.println("Best of 5. Moves: ROCK PAPER SCISSORS LIZARD SPOCK");

        for (int round = 1; round <= 5; round++) {
            System.out.print("Round " + round + " - your move: ");
            Move mine;
            try {
                mine = Move.valueOf(sc.next().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("  Invalid move, round skipped.");
                continue;
            }
            Move theirs = moves[rng.nextInt(moves.length)];   // random computer move

            int r = winner(mine, theirs);
            System.out.print("  You: " + mine + " | Computer: " + theirs + " -> ");
            if (r == 1)       { you++; System.out.println("you win the round"); }
            else if (r == -1) { cpu++; System.out.println("computer wins the round"); }
            else              {        System.out.println("tie"); }
        }

        System.out.println("Final score - You " + you + " : " + cpu + " Computer");
        System.out.println(you > cpu ? "You win!" : you < cpu ? "Computer wins!" : "Overall tie!");
        sc.close();
    }
}