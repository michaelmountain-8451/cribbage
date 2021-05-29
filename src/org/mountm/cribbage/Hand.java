package org.mountm.cribbage;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

public class Hand {
	
	private EnumSet<Card> heldCards;
	private Card starterCard;
	
	private EnumSet<Card> allCards;
	
	public Hand(EnumSet<Card> heldCards, Card starterCard) {
		super();
		this.heldCards = heldCards;
		this.starterCard = starterCard;
		allCards = EnumSet.copyOf(heldCards);
		allCards.add(starterCard);
	}

	public Set<Card> getHeldCards() {
		return heldCards;
	}

	public Card getStarterCard() {
		return starterCard;
	}

	public void setHeldCards(EnumSet<Card> heldCards) {
		this.heldCards = heldCards;
		allCards = EnumSet.copyOf(heldCards);
		allCards.add(starterCard);
	}

	public void setStarterCard(Card starterCard) {
		allCards.remove(this.starterCard);
		allCards.add(starterCard);
		this.starterCard = starterCard;
	}
	
	public int getScore() {
		if (heldCards.size() != 4 || starterCard == null) {
			throw new RuntimeException("Incorrect hand size!");
		}
		
		return countFifteens() + countRuns() + countPairs() + countFlush();
	}

	private int countFlush() {
		Set<Integer> suits = heldCards.stream().map(Card::getSuit).collect(Collectors.toSet());
		Set<Card> jacks = heldCards.stream().filter(c -> c.getValue() == 11).collect(Collectors.toSet());
		int nobs = jacks.stream().filter(c -> c.getSuit() == starterCard.getSuit()).collect(Collectors.toSet()).size();
		if (suits.size() == 1) {
			if (starterCard.getSuit() == suits.stream().findAny().orElse(4)) {
				return 5 + nobs;
			}
			return 4 + nobs;
		}
		return nobs;
	}

	private int countPairs() {
		Set<Set<Card>> pairs = Sets.combinations(allCards, 2);
		int val = 0;
		for (Set<Card> p : pairs) {
			if (p.stream().map(Card::getValue).collect(Collectors.toSet()).size() == 1) {
				val += 2;
			}
		}
		return val;
	}

	private int countRuns() {
		int runLength = 5;
		int val = 0;
		do {
			Set<Set<Card>> combinations = Sets.combinations(allCards, runLength);
			check: for (Set<Card> s : combinations) {
				List<Integer> values = s.stream().map(Card::getValue).sorted().collect(Collectors.toList());
				for (int i = 1; i < values.size(); i++) {
					if (values.get(i) != values.get(0) + i) {
						continue check;
					}
				}
				val += s.size();
			}
			runLength--;
		} while (val == 0 && runLength > 2);
		return val;
	}

	private int countFifteens() {
		Set<Set<Card>> powerSet = Sets.powerSet(allCards);
		int val = 0;
		for (Set<Card> s : powerSet) {
			if (s.stream().mapToInt(Card::getCountingValue).sum() == 15) {
				val += 2;
			}
		}
		return val;
	}
	
	public String toString() {
		return heldCards.stream().map(Card::toString).collect(Collectors.joining(", ")) + " with " + starterCard + " starter";
	}

	
	
}
