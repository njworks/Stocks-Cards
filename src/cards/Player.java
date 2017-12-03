package cards;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by njosepa on 27/10/2017.
 */
public class Player {

    public static Map<Integer, String> playerID = new TreeMap<Integer, String>(); //keeps player id
    public static Map<Integer, Integer> cash = new TreeMap<>(); //player cash
    public static int[][] shares = new int[4][5]; //player shares
    public static int[] sharePrice = new int[5]; //company share prices

    //adds new player to playerid and cash maps
    public static void createName(int id, String name) {
        if (id == 0) {
            int len = playerID.size();
            int temp = len++;
            playerID.put(temp, name);
            cash.put(temp, 500);
        } else {
            playerID.put(id, name);
            cash.put(id, 500);
        }
//        for (Map.Entry<Integer, String> entry : playerID.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
//        }
    }

    //creates random shares for each player
    public static int[] randomShares() {
        int[] result = new int[5];
        int total = 10;
        Random random = new Random();
        for (int i = 0; i < result.length; i++) {
            if (i == 4) {
                result[4] = total;
                break;
            }
            int temp = random.nextInt(total);
            result[i] = temp;
            total -= temp;
        }

        return result;
    }

    //assign random shares to players and sets the share price to 100
    public static void assignShares() {
        for (int i = 0; i <= 3; i++) {
            shares[i] = randomShares();
        }

        for (int i = 0; i < 5; i++) {
            sharePrice[i] = 100;
        }
    }

    //to change the share price of each company
    public static void sharePriceWatch(int change, int value) {
        int temp = sharePrice[change];
        sharePrice[change] = temp + value;
    }

}
