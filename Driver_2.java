import java.util.Objects;

class Card {
    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object obj) {
        Card c = (Card) obj;

        return rank.equals(c.rank) && suit.equals(c.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}

public class Driver_2 {
    public static void main(String[] args) {

        Card[] cards = new Card[5];

        String[] ranks = {"Ace", "King", "Queen", "Ace", "Jack"};
        String[] suits = {"Spades", "Hearts", "Clubs", "Spades", "Diamonds"};

        int size = 0;

        for (int i = 0; i < ranks.length; i++) {

            Card newCard = new Card(ranks[i], suits[i]);

            boolean duplicate = false;

            for (int j = 0; j < size; j++) {

                if (newCard.equals(cards[j])) {
                    System.out.println("Duplicate found: " + newCard);
                    duplicate = true;
                    break;
                }
            }

            cards[size] = newCard;
            size++;

            if (duplicate)
                break;
        }
    }
}