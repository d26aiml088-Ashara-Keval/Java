public class Lab03_Driver2 {
    public static void main(String[] args) {

        Card[] cards = new Card[5];

        cards[0] = new Card("Ace", "Spades");
        cards[1] = new Card("King", "Hearts");

        Card newCard = new Card("Ace", "Spades");

        boolean duplicate = false;

        for (int i = 0; i < 2; i++) {
            if (newCard.equals(cards[i])) {
                duplicate = true;
                break;
            }
        }

        if (duplicate) {
            System.out.println("Duplicate found: " + newCard);
        } else {
            cards[2] = newCard;
        }
    }
}