package org.mountm.cribbage;

public enum Card {
	
	ACE_SPADES     (0, 1), 
	TWO_SPADES     (0, 2),
	THREE_SPADES   (0, 3),
	FOUR_SPADES    (0, 4),
	FIVE_SPADES    (0, 5),
	SIX_SPADES     (0, 6),
	SEVEN_SPADES   (0, 7),
	EIGHT_SPADES   (0, 8),
	NINE_SPADES    (0, 9),
	TEN_SPADES     (0, 10),
	JACK_SPADES    (0, 11),
	QUEEN_SPADES   (0, 12),
	KING_SPADES    (0, 13),
	ACE_HEARTS     (1, 1), 
	TWO_HEARTS     (1, 2),
	THREE_HEARTS   (1, 3),
	FOUR_HEARTS    (1, 4),
	FIVE_HEARTS    (1, 5),
	SIX_HEARTS     (1, 6),
	SEVEN_HEARTS   (1, 7),
	EIGHT_HEARTS   (1, 8),
	NINE_HEARTS    (1, 9),
	TEN_HEARTS     (1, 10),
	JACK_HEARTS    (1, 11),
	QUEEN_HEARTS   (1, 12),
	KING_HEARTS    (1, 13),
	ACE_CLUBS      (2, 1), 
	TWO_CLUBS      (2, 2),
	THREE_CLUBS    (2, 3),
	FOUR_CLUBS     (2, 4),
	FIVE_CLUBS     (2, 5),
	SIX_CLUBS      (2, 6),
	SEVEN_CLUBS    (2, 7),
	EIGHT_CLUBS    (2, 8),
	NINE_CLUBS     (2, 9),
	TEN_CLUBS      (2, 10),
	JACK_CLUBS     (2, 11),
	QUEEN_CLUBS    (2, 12),
	KING_CLUBS     (2, 13),
	ACE_DIAMONDS   (3, 1), 
	TWO_DIAMONDS   (3, 2),
	THREE_DIAMONDS (3, 3),
	FOUR_DIAMONDS  (3, 4),
	FIVE_DIAMONDS  (3, 5),
	SIX_DIAMONDS   (3, 6),
	SEVEN_DIAMONDS (3, 7),
	EIGHT_DIAMONDS (3, 8),
	NINE_DIAMONDS  (3, 9),
	TEN_DIAMONDS   (3, 10),
	JACK_DIAMONDS  (3, 11),
	QUEEN_DIAMONDS (3, 12),
	KING_DIAMONDS  (3, 13);
	
	private final int suit;
	private final int value;
	
	Card (int suit, int value) {
		this.suit = suit;
		this.value = value;
	}
	
	public int getSuit() {
		return suit;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getCountingValue() {
		return Math.min(10, value);
	}

}
