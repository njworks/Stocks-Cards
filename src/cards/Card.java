package cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Card implements Comparable<Card> {
	public static final int[] EFFECTS = new int[] { -20, -10, -5, +5, +10, +20 };
	public static int[][] playCards = new int[5][5]; //Holds all the cards
	public final int effect;
	public Card(int effect) {
		this.effect = effect;
	}

	//Scramble the cards for play
	public static int[][] scrampler(){
		List<Integer> values = new ArrayList<Integer>();
		for(int n = 0; n < 5; n++) {
			for (int i : EFFECTS) {
				values.add(0, i);
			}
			Collections.shuffle(values);

			for (int j = 0; j < 5; j++) {
				playCards[n][j] = values.get(j);
			}
			values.clear();
		}
		return playCards;
	}

	public static Card parse(String s) {
		int effect = Integer.parseInt(s);
		return new Card(effect);
	}

	@Override
	public String toString() {
		return "" + effect;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + effect;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return effect == ((Card) obj).effect;
	}

	@Override
	public int compareTo(Card other) {
		return Integer.compare(effect, other.effect);
	}

}
