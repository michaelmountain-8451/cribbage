package org.mountm.cribbage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

public class CribbageRunner {
	
	private static Map<Integer, List<Double>> values = null;
	private static Map<Integer, List<Double>> cribValues = null;

	public static void main(String[] args) {
		System.out.println("Enter 6 cards, separated by spaces");
		System.out.println("Enter X to quit");
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			Set<Card> bestHand = null;
			int bestHandScore = 0;
			int bestCribScore = 0;
			int bestNetScore = Integer.MIN_VALUE;
			Set<Card> cards = new HashSet<>();
			EnumSet<Card> deck = EnumSet.allOf(Card.class);
			String tokens = scanner.nextLine();
			for (String token : tokens.split(" ")) {
				if ("X".equals(token.toUpperCase())) {
					System.out.println("Goodbye");
					scanner.close();
					return;
				}
				Card card = Card.valueOf(token.toUpperCase());
				cards.add(card);
				deck.remove(card);
			}
			System.out.println("Is it your crib?");
			boolean hasCrib = "Y".equals(scanner.nextLine().toUpperCase());
			if (hasCrib) {
				bestCribScore = Integer.MIN_VALUE;
			}
			for (Set<Card> option : Sets.combinations(cards, 4)) {
				Map<Integer, List<Double>> handValues = new HashMap<>();
				Map<Integer, List<Double>> cribbValues = new HashMap<>();
				EnumSet<Card> heldCards = EnumSet.noneOf(Card.class);
				heldCards.addAll(option);
				int totalScore = 0;
				int netScore = 0;
				int cribScore = 0;
				for (Card starterCard : deck) {
					Hand hand = new Hand(heldCards, starterCard);
					int score = hand.getScore();
					totalScore += score;
					List<Double> scores;
					if (handValues.containsKey(starterCard.getValue())) {
						scores = handValues.get(starterCard.getValue());
						scores.add(1.0 * score);
					} else {
						scores = new ArrayList<>(Arrays.asList(1.0 * score));
					}
					handValues.put(starterCard.getValue(), scores);
					int innerCribScore = 0;
					int count = 0;
					for (Set<Card> cribCards : Sets.combinations(deck, 2)) {
						if (!cribCards.contains(starterCard)) {
							count++;
							EnumSet<Card> crib = EnumSet.noneOf(Card.class);
							crib.addAll(cribCards);
							crib.addAll(cards.stream().filter(c -> !option.contains(c)).collect(Collectors.toSet()));
							Hand cribHand = new Hand(crib, starterCard);
							innerCribScore += cribHand.getScore();
						}
					}
					cribScore += Math.round(1.0 * innerCribScore / count);
					if (cribbValues.containsKey(starterCard.getValue())) {
						scores = cribbValues.get(starterCard.getValue());
						scores.add((1.0 * innerCribScore / count));
					} else {
						scores = new ArrayList<>(Arrays.asList((1.0 * innerCribScore / count)));
					}
					cribbValues.put(starterCard.getValue(), scores);
				}
				if (hasCrib) {
					netScore = totalScore + cribScore;
				} else {
					netScore = totalScore - cribScore;
				}
				if (netScore > bestNetScore) {
					System.out.println("Found better hand");
					bestHand = heldCards;
					bestNetScore = netScore;
					bestHandScore = totalScore;
					if (hasCrib) {
						bestCribScore = netScore - totalScore;
					} else {
						bestCribScore = totalScore - netScore;
					}
					System.out.println("Hand is worth " + bestHandScore + ", crib is worth " + bestCribScore + ", total " + bestNetScore);
					values = handValues;
					cribValues = cribbValues;
				}
				
			}
			
			Map<Integer, Double> preferredStarters = new HashMap<>();
			for (Integer cardValue : values.keySet()) {
				double excessValue;
				if (hasCrib) {
					excessValue = values.get(cardValue).stream().mapToDouble(Double::doubleValue).average().getAsDouble()
							+ cribValues.get(cardValue).stream().mapToDouble(Double::doubleValue).average().getAsDouble()
							- (1.0 * bestNetScore / deck.size());
				} else {
					excessValue = values.get(cardValue).stream().mapToDouble(Double::doubleValue).average().getAsDouble()
							- cribValues.get(cardValue).stream().mapToDouble(Double::doubleValue).average().getAsDouble()
							- (1.0 * bestNetScore / deck.size());
				}
				preferredStarters.put(cardValue, excessValue);
			}
			
			System.out.println("Best hand: " + bestHand);
			System.out.print("Averages ");
			System.out.printf("%.3f", 1.0 * bestHandScore / deck.size());
			System.out.println(" points");
			for (Entry<Integer, List<Double>> val : values.entrySet()) {
				System.out.printf(val.getKey() + ": %.3f %n", val.getValue().stream().mapToDouble(Double::doubleValue).average().getAsDouble());
			}
			System.out.print("Crib averages ");
			System.out.printf("%.3f", 1.0 * bestCribScore / deck.size());
			System.out.println(" points");
			for (Entry<Integer, List<Double>> val : cribValues.entrySet()) {
				System.out.printf(val.getKey() + ": %.3f %n", val.getValue().stream().mapToDouble(Double::doubleValue).average().getAsDouble());
			}
			System.out.println("Good starters: " + preferredStarters.entrySet().stream().filter(e -> e.getValue() > 0).sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).map(e -> e.getKey()).collect(Collectors.toList()));
			System.out.println("Bad starters: " + preferredStarters.entrySet().stream().filter(e -> e.getValue() < 0).sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue())).map(e -> e.getKey()).collect(Collectors.toList()));
			System.out.println("Enter 6 cards, separated by spaces");
			System.out.println("Enter X to quit");
		}
		scanner.close();
	}

}
