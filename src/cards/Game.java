package cards;

import server.CardService;

import java.util.*;

public class Game {

    static Stock stocks; //Stock class
    static Player player; //Player class
    public static Map<Integer, String> gameEnd; //keeps final cash of all players
    public static Map<Integer, Integer> totalVotes; // stores votes for each round


    /*
    Game(): Creates game object and scramble shares for each player and initialise votes and gameEnd maps
     */
    public Game() {
        Card.scrampler();
        player.assignShares();
        gameEnd = new TreeMap<Integer, String>();
        totalVotes = new TreeMap(Collections.reverseOrder());

        for (int i = 0; i < 5; i++) {
            totalVotes.put(i, 0);
        }

//        for (int p = 0; p < Card.playCards.length; p++) {
//            System.out.println("CARDS " + Arrays.toString(Card.playCards[p]));
//        }
    }

    //Gets player cash
    public static int getCash(int playerId) {
        return player.cash.get(playerId);
    }

    //Gets player shares
    public static int[] getShares(int id) {
        return player.shares[id];
    }

    //Get shares prices
    public static int[] getPrices() {
        return player.sharePrice;
    }

    /*
    Buy(): Buys shares - Checks if player has enough cash for purchase, if more than > 0, then players cash and
    shares updated
     */
    public static String buy(int id, int buyID, int amount) {
        int temp = id - 1;
        int sharePrice = player.sharePrice[buyID];
        int finalPrice = sharePrice * amount;
        int total = player.cash.get(temp) - (finalPrice + (amount * 3));
        String outCome = "";

        if (total > 0) {
            player.shares[temp][buyID] += amount;
            player.cash.put(temp, total);
            outCome = "Buy Complete";
            System.out.println("player: " + player.playerID.get(temp) + " cash: £" + player.cash.get(temp));
            System.out.println("player shares after: " + Arrays.toString(getShares(temp)));
        } else {
            outCome = "Not enough cash, Cost: £" + (finalPrice + (amount * 3)) + " Your cash: £" + getCash(temp);
        }
        System.out.println(outCome);
        return outCome;
    }

    /*
    Sell(): Sell shares if player has shares requested to sell and get stock price for it.
     */
    public static String sell(int id, int sellID, int amount) {
        int temp = id - 1;
        int share = player.shares[temp][sellID];
        int total = share - amount;
        System.out.println("player: " + player.playerID.get(temp) + " player shares before: " +
                Arrays.toString(getShares(temp)));
        System.out.println("share total: " + total);
        String outCome = "";
        if (total >= 0) {
            int cash = player.cash.get(temp);
            int sharePrice = player.sharePrice[sellID];
            player.shares[temp][sellID] = total;
            player.cash.put(temp, cash + (sharePrice * amount));
            outCome = "Sell Complete";
            System.out.println("player: " + player.playerID.get(temp) + " cash: £" + player.cash.get(temp));
            System.out.println("player shares after: " + Arrays.toString(getShares(temp)));
        } else {
            outCome = "Not enough shares to sell, Your Shares: " + share + " To sell: " + amount;
        }
        return outCome;
    }

    /*
    Takes player votes and adds to totalVotes map
     */
    public static String playerVotes(String i) {
        String[] response = i.split(" ");
        String first = response[0];
        int company = stocks.checkStock(first);

        if (response[1].equals("YES") || response[1].equals("Y")) {
            totalVotes.put(company, totalVotes.get(company) + 1);
            return "Vote Valid";
        } else if (response[1].equals("NO") || response[1].equals("N")) {
            totalVotes.put(company, totalVotes.get(company) - 1);
            return "Vote Valid";
        } else {
            return "Vote Invalid";
        }
    }

    /*
    Execute the totalVotes from map and update the stock price depending on cards voted
     */
    public static void vote() {
        for (Map.Entry<Integer, Integer> entry : totalVotes.entrySet()) {
            if (entry.getValue() > 0) {
                int value = Card.playCards[0][entry.getKey()];
                player.sharePriceWatch(entry.getKey(), value);
                updateCards(entry.getKey());
            } else if (entry.getValue() < 0) {
                updateCards(entry.getKey());
            }
        }
        for (int i = 0; i < 5; i++) {
            totalVotes.put(i, 0);
        }

        System.out.println("after vote: " + Arrays.toString(getPrices()));
        System.out.println("Vote Successful");
    }

    /*
    Updates the cards from votes execution
     */
    private static void updateCards(int company) {
        for (int k = 0; k < Card.playCards.length; k++) {
            if (k == 4) {
                Card.playCards[4][company] = 0;
                break;
            }
            Card.playCards[k][company] = Card.playCards[k + 1][company];
        }
    }

    /*
    Works out the total cash for each player
     */
    public static void gameEndScores() {
        for (int i = 0; i < 4; i++) {
            int addtotal = 0;
            for (int p = 0; p < 5; p++) {
                addtotal += Player.shares[i][p] * Player.sharePrice[p];
            }
            addtotal += Player.cash.get(i);
            gameEnd.put(addtotal, Player.playerID.get(i));
        }

        for (Map.Entry<Integer, String> entry : gameEnd.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
    }

}
