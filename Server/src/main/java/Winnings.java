/*
Muhammad Muzzammil
Jose Nava
CS 342
Professor Approved Extension With NO DEDUCTION
*/

import java.util.ArrayList;

public class Winnings {

    Boolean queenOrHigher(ArrayList<String> dealerCards){

        if(dealerCards.get(0).contains("Queen") || dealerCards.get(0).contains("King") || dealerCards.get(0).contains("Ace")){
            return true;
        }
        if(dealerCards.get(1).contains("Queen") || dealerCards.get(1).contains("King") || dealerCards.get(1).contains("Ace")){
            return true;
        }
        if(dealerCards.get(2).contains("Queen") || dealerCards.get(2).contains("King") || dealerCards.get(2).contains("Ace")){
            return true;
        }

        return false;
    }

    double findWinnerMoney(int anteWager, int playWager, int plusWager, String wayOfWinning){

        double extraPayout = 1;

        if(wayOfWinning.contains("STRAIGHT FLUSH")){
            extraPayout = 40;
        }
        else if(wayOfWinning.contains("THREE OF A KIND")){
            extraPayout = 30;
        }
        else if(wayOfWinning.contains("STRAIGHT")){
            extraPayout = 6;
        }
        else if(wayOfWinning.contains("FLUSH")){
            extraPayout = 3;
        }
        else if(wayOfWinning.contains("PAIR")){
            extraPayout = 1;
        }

        if(wayOfWinning.contains("You")) { // if user won then we pay all
            double total = extraPayout * ((2 * (anteWager + playWager)) + plusWager);
            return total;
        }

        return plusWager;
    }
}
