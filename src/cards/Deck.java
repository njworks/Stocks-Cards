package cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    public Stock stock;
    public List<Card> cards;

    public Deck(Stock stock) {
        this.stock = stock;
        cards = new ArrayList<>();
        for (int effect : Card.EFFECTS) {
            cards.add(new Card(effect));
            Collections.shuffle(cards);
        }
    }

    public Deck(Stock stock, int... effects) {
        this.stock = stock;
        cards = new ArrayList<>();
        for (int effect : effects) {
            cards.add(new Card(effect));
        }
    }

    @Override
    public String toString() {
        return stock + " " + cards;
    }

    //shows deck updated
    public static String updateDeck() {
        int[] shareValue = Player.sharePrice;
        StringBuilder build = new StringBuilder();
        int i = 0;
        for (Stock s : Stock.values()) {
            build.append("  " + s + ": ");
            build.append("\u00A3" + shareValue[i] + " ");
            i++;
        }
        return build.toString();
    }

    //shows player shares
    public static String playerShare(int id) {
        StringBuilder build = new StringBuilder();
        int i = 0;
        for (Stock s : Stock.values()) {
            build.append("  " + s + ": ");
            build.append(Player.shares[id][i] + " ");
            i++;
        }
        return build.toString();
    }

    public static void main(String[] args) {
        Deck[] decks = new Deck[Stock.values().length];
        for (Stock s : Stock.values()) {
            decks[s.ordinal()] = new Deck(s);
//			System.out.print(s + " ");
        }
        for (Deck d : decks) {
//			System.out.println(d);
        }
    }

}
