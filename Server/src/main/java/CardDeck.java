/*
Muhammad Muzzammil
Jose Nava
CS 342
Professor Approved Extension With NO DEDUCTION
*/

import java.util.ArrayList;
import java.util.Collections;

public class CardDeck {

    ArrayList<String>user = new ArrayList<>();
    ArrayList<String>dealer = new ArrayList<>();
    private static final String[] SUITS = {"Hearts", "Diamonds", "Clubs", "Spades"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

    public ArrayList<String> shuffleDeckAndDealCards() {
        ArrayList<String> deck = new ArrayList<String>();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(rank + " of " + suit);
            }
        }
        Collections.shuffle(deck);
        ArrayList<String> dealtCards = new ArrayList<String>(deck.subList(0, 6));
        return dealtCards;
    }
}