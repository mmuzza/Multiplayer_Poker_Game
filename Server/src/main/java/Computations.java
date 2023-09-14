/*
Muhammad Muzzammil
Jose Nava
CS 342
Professor Approved Extension With NO DEDUCTION
 */

import java.util.*;

public class Computations {

    int clientRank = 0;
    int dealerRank = 0;

    public Integer isStraightFlush(ArrayList<String> cards) {
        // Check if all cards are of the same suit
        String suit = cards.get(0).split(" ")[2];
        boolean sameSuit = cards.stream().allMatch(card -> card.split(" ")[2].equals(suit));

        if (!sameSuit) {
            return 0;
        }

        // Sort the cards in ascending order based on their rank
        Map<String, Integer> rankMap = new HashMap<>();
        rankMap.put("Ace", 1);
        rankMap.put("2", 2);
        rankMap.put("3", 3);
        rankMap.put("4", 4);
        rankMap.put("5", 5);
        rankMap.put("6", 6);
        rankMap.put("7", 7);
        rankMap.put("8", 8);
        rankMap.put("9", 9);
        rankMap.put("10", 10);
        rankMap.put("Jack", 11);
        rankMap.put("Queen", 12);
        rankMap.put("King", 13);

        cards.sort((a, b) -> rankMap.get(a.split(" ")[0]) - rankMap.get(b.split(" ")[0]));

        // Check if the difference between the first and second cards and the second and third cards is 1
        int rankDiff1 = rankMap.get(cards.get(1).split(" ")[0]) - rankMap.get(cards.get(0).split(" ")[0]);
        int rankDiff2 = rankMap.get(cards.get(2).split(" ")[0]) - rankMap.get(cards.get(1).split(" ")[0]);


        if (rankDiff1 == 1 && rankDiff2 == 1) {
            return 16; // 5 before
        }

        return 0;
    }

    //--------------------------------------------------------------------

    // Three of a kind
    public Integer isThreeOfAKind(ArrayList<String> cards) {
        // Count the number of cards of each rank
        Map<String, Integer> rankCount = new HashMap<>();

        for (String card : cards) {
            String rank = card.split(" ")[0];
            rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);
        }

        // Check if there are three cards with the same rank
        if(rankCount.values().stream().anyMatch(count -> count == 3)){
            return 8;
        }

        return 0;
        //return rankCount.values().stream().anyMatch(count -> count == 3);
    }

    //--------------------------------------------------------------------

    // Straight --> priority 3
    public Integer isStraight(ArrayList<String> cards) {
        // Sort the cards in ascending order based on their rank
        Map<String, Integer> rankMap = new HashMap<>();
        rankMap.put("Ace", 1);
        rankMap.put("2", 2);
        rankMap.put("3", 3);
        rankMap.put("4", 4);
        rankMap.put("5", 5);
        rankMap.put("6", 6);
        rankMap.put("7", 7);
        rankMap.put("8", 8);
        rankMap.put("9", 9);
        rankMap.put("10", 10);
        rankMap.put("Jack", 11);
        rankMap.put("Queen", 12);
        rankMap.put("King", 13);

        cards.sort((a, b) -> rankMap.get(a.split(" ")[0]) - rankMap.get(b.split(" ")[0]));

        // Check if the difference between the first and second cards and the second and third cards is 1
        int rankDiff1 = rankMap.get(cards.get(1).split(" ")[0]) - rankMap.get(cards.get(0).split(" ")[0]);
        int rankDiff2 = rankMap.get(cards.get(2).split(" ")[0]) - rankMap.get(cards.get(1).split(" ")[0]);

        if (rankDiff1 == 1 && rankDiff2 == 1) {
            return 4;
        }

        // Check if the hand is an ace-low straight (10-J-Q or J-Q-K)
        if (cards.get(0).split(" ")[0].equals("Ace")) {
            if (cards.get(1).split(" ")[0].equals("10") && cards.get(2).split(" ")[0].equals("Jack")) {
                return 4;
            } else if (cards.get(1).split(" ")[0].equals("Jack") && cards.get(2).split(" ")[0].equals("Queen")) {
                return 4;
            } else if (cards.get(1).split(" ")[0].equals("Queen") && cards.get(2).split(" ")[0].equals("King")) {
                return 4;
            }
        }

        return 0;
    }

    //--------------------------------------------------------------------

    // Flush --> priority: 2
    public Integer isFlush(ArrayList<String> cards) {
        // Count the number of cards of each suit
        Map<String, Integer> suitCount = new HashMap<>();

        for (String card : cards) {
            String suit = card.split(" ")[2];
            suitCount.put(suit, suitCount.getOrDefault(suit, 0) + 1);
        }

        // Check if all cards have the same suit
        if(suitCount.size() == 1){
            return 2;
        }

        return 0;
//        return suitCount.size() == 1;
    }

    //--------------------------------------------------------------------------

    // Pair --> priority: 1
    public Integer isPair(ArrayList<String> cards) {
        // Count the number of cards of each rank
        Map<String, Integer> rankCount = new HashMap<>();

        for (String card : cards) {
            String rank = card.split(" ")[0];
            rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);
        }

        // Check if there is exactly one rank with a count of 2
        int count = 0;
        for (int value : rankCount.values()) {
            if (value == 2) {
                count++;
            }
        }

        if(count == 1){
            return 1;
        }

        return 0;

        //return count == 1;


    }


    //--------------------------------------------------------------------------


    public String findWinner(ArrayList<String> clientCards, ArrayList<String> dealerCards){


        clientRank += isStraightFlush(clientCards); // returns 5 if true else 0
        clientRank += isThreeOfAKind(clientCards); // returns 4 if true else 0
        clientRank += isStraight(clientCards); // returns 3 if true else 0
        clientRank += isFlush(clientCards); // returns 2 if true else 0
        clientRank += isPair(clientCards); // returns 1 if true else 0

        dealerRank += isStraightFlush(dealerCards); // returns 5 if true else 0
        dealerRank += isThreeOfAKind(dealerCards); // returns 4 if true else 0
        dealerRank += isStraight(dealerCards); // returns 3 if true else 0
        dealerRank += isFlush(dealerCards); // returns 2 if true else 0
        dealerRank += isPair(dealerCards); // returns 1 if true else 0

        String howTheyWon = "";
        if(clientRank == 16 || dealerRank == 16){
            howTheyWon = "VIA STRAIGHT FLUSH!";
        }
        else if(clientRank == 8 || dealerRank == 8 ){
            howTheyWon = "VIA THREE OF A KIND!";
        }
        else if(clientRank == 4 || dealerRank == 4){
            howTheyWon = "VIA STRAIGHT!";
        }
        else if(clientRank == 2 || dealerRank == 2){
            howTheyWon = "VIA FLUSH!";
        }
        else if(clientRank == 1 || dealerRank == 1){
            howTheyWon = "VIA PAIR!";
        }
        else{
            howTheyWon = "VIA MULTIPLE WAYS!";
        }


        if(clientRank > dealerRank){
            return "You Won " + howTheyWon;
        }
        else if(dealerRank > clientRank){
            return "Dealer Won " + howTheyWon;
        }
        else if(dealerRank == clientRank){
            return "Its a tie";
        }

        return "";
    }
}