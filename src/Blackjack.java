import java.util.*;

public class Blackjack {
    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private char whoseTurn;
    private final Player player;
    private final Player computer;
    private List<Card> deck;
    private static Scanner in = new Scanner(System.in);
    private static int chips = 0;
    private final int PLAYER_LOST = 0;
    private final int PLAYER_WON = 1;
    private final int UNDETERMINED = 2;
    private final int TIE = 3;
    private boolean isLastTurn = false;

    public Blackjack() {
        this.whoseTurn = 'P';
        this.player = new Player();
        this.computer = new Player();
    }

    public void play() {

        int bet = 0;
        if (chips == 0) {
            System.out.println("You have no chips. Enter the number of chips to buy in or stop the program to exit.");

            while (true) {
                try {
                    chips = Blackjack.in.nextInt();
                }
                catch (Exception e) {

                }
                if (chips > 0) {
                    break;
                }
                System.out.println("That is not a valid number of chips!");
                in.nextLine();
            }
        }
        System.out.println("Enter the number of chips to bet.");
        System.out.printf("Current chips: %d%n", chips);

        while (true) {
            try {
                bet = in.nextInt();
            }
            catch (Exception e) {

            }
            if (bet > 0 && bet < 26 && bet <= chips) {
                break;
            }

            System.out.println("That is not a valid number of chips! (must be between 1 and 25 and less than or equal to your current chips)");
            in.nextLine();
        }
        // play the game until someone wins
        shuffleAndDeal();
        boolean isFirstTurn = true;
        while (true){
            if (!isLastTurn) {
                showHand(whoseTurn);
            }
            int winner = checkWin(whoseTurn, isLastTurn);
            if (winner == PLAYER_LOST) {
                chips -= bet;
            }
            else if (winner == PLAYER_WON && isFirstTurn == true) {
                chips += (int) ((double) bet * 3.0/2.0);
            }
            else if (winner == PLAYER_WON){
                chips += bet;
            }
            if (winner != UNDETERMINED) {
                break;
            }
            isFirstTurn = false;
            whoseTurn = takeTurn(whoseTurn);
        }
    }

    public void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);  // shuffles the deck
        while (player.getHand().size() < 2) {
            player.takeCard(deck.remove(0));
            computer.takeCard(deck.remove(0));
        }
    }

    ////////// PRIVATE METHODS /////////////////////////////////////////////////////

    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));     // adds 52 cards to the deck (13 ranks, 4 suits)
            }
        }
    }

    private char takeTurn(char turn) {

        if (turn == 'P') {
            System.out.printf("Your turn!%n1. Stand %n2. Hit%n");
            int choice = 0;
            while (true){
                try {
                    choice = in.nextInt();
                }
                catch (Exception e) {

                }
                if (choice == 1 || choice == 2) {
                    break;
                }
                System.out.println("Enter 1 or 2 to stand or hit.");
                in.nextLine();
            }
            if (choice == 1) {
                return 'C';
            }
            else {
                player.takeCard(deck.remove(0));
                return 'P';
            }
        }
        else {
            if (getSumHand(computer) < 17) {
                computer.takeCard(deck.remove(0));
                return 'C';
            }
            else {
                isLastTurn = true;
                return 'C';
            }
        }

    }

    private int getSumHand(Player tempplayer) {
        int sum = 0;
        int aceCount = 0;
        for (Card item: tempplayer.getHand()) {
            if (item.getRank() == "A") {
                aceCount++;
                sum++;
            }
            else {
                sum += item.getOrderedRank(item.getRank());
            }
        }
        for (int i = 0; i < aceCount; i++) {
            if (sum + 10 <= 21) {
                sum += 10;
            }
        }
        return sum;
    }

    private int checkWin(char t, boolean lastTurn) {
        int playersum = getSumHand(player);
        if (playersum > 21) {
                System.out.println("You lost this round.");
                return PLAYER_LOST;
        }
        else if (t == 'C') {
            int computerSum = getSumHand(computer);
            if (computerSum > 21) {
                System.out.println("You won this round.");
                return PLAYER_WON;
            }
            else if (computerSum > playersum) {
                System.out.println("You lost this round.");
                return PLAYER_LOST;
            }
            else if (lastTurn == true && computerSum == playersum) {
                System.out.println("Tie.");
                return TIE;
            }
            else if (lastTurn == true){
                System.out.println("You won this round.");
                return PLAYER_WON;
            }
        }
        return UNDETERMINED;
    }

    private void showHand(char turn) {
        if (turn == 'P') {
            System.out.println("\nPLAYER hand: " + player.getHand());
            System.out.println("\nDEALER hand: " + computer.getHand().get(0));
        }
        else {
            System.out.println("\nPLAYER hand: " + player.getHand());
            System.out.println("\nDEALER hand: " + computer.getHand());
        }
    }

    ////////// MAIN METHOD /////////////////////////////////////////////////////////

    public static void main(String[] args) {
        System.out.println("Blackjack");
        System.out.println("Enter the number of chips to buy in: ");
        while (true) {
            try {
                chips = Blackjack.in.nextInt();
            }
            catch (Exception e) {

            }
            if (chips > 0) {
                break;
            }
            System.out.println("That is not a valid number of chips!");
            in.nextLine();
        }

        while (true) {
            new Blackjack().play();
        }
    }
}