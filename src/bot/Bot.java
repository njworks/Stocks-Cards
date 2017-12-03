package bot;

import cards.Card;
import cards.Game;
import cards.Player;
import server.CardServer;
import server.CardService;
import util.SpinLock;
import web.TomcatServer;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by njosepa on 28/11/2017.
 */
public class Bot implements Runnable {
    public static int[] botid; //Holds the bots id
    public static int[] randomValues = new int[4]; //Random values for round execution
    public static boolean startBot = false; //To start the bot
    public static boolean roundBot = false; //To start round for bot
    public static boolean playBot = false;  //To play each round

    public static BotClient client = new BotClient(TomcatServer.BOT_URL);

    public Bot() {
    }

    @Override
    public void run() {
        CardServer.LOGGER.info("Bot thread run");
        SpinLock.spinLock(3000, () -> startBot, sb -> sb);
        if (startBot) {
            CardServer.LOGGER.info("Bot given permission to play");
            CardServer.LOGGER.info(client.botBegin());
            CardServer.LOGGER.info("Bot created bot names");
            playBot = true;
            startBot = false;
        }

        while (playBot) {
            SpinLock.spinLock(3000, () -> roundBot, pb -> pb);
            if (roundBot) {
                CardServer.LOGGER.info("Bot gonna play round");
                for (int i = 0; i < botid.length; i++) {
                    randomBot(botid[i]);
                }
                CardServer.LOGGER.info("Bot finished round");
                roundBot = false;
            }
        }
        CardServer.LOGGER.info("Bot thread end");
    }

    /*
    To fill up the 4 player requirement, the bot fills in the remaining players and store
    it ID for its round actions
     */
    public static String botStart() {
        int temp = 1;
        botid = new int[4 - Player.playerID.size()];
        for (int i = Player.playerID.size(); i < 4; i++) {
            Player.createName(0, "Bot " + temp);
            botid[temp - 1] = i;
            temp++;
        }

        for (Map.Entry<Integer, String> entry : Player.playerID.entrySet()) {
            CardServer.LOGGER.info("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
        return "Bots " + Integer.toString(botid.length);
    }

    /*
    Bots round action: Creates random values and does random buy, sell and votes.
     */
    public static void randomBot(int id) {
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            randomValues[i] = random.nextInt(5);
            CardServer.LOGGER.info("Random values " + randomValues[i]);
        }

        CardServer.LOGGER.info("Bot " + id + " Cash before buy/sell: " + client.getCash(id));

        CardServer.LOGGER.info("Bot " + id + " buy before " + Arrays.toString(client.getShares(id)));
        client.purchase(id + 1, randomValues[0], 1);
        CardServer.LOGGER.info("Bot " + id + " buy after " + Arrays.toString(client.getShares(id)));

        if (Player.shares[id][randomValues[1]] > 1) {
            CardServer.LOGGER.info("Bot " + id + " sell before " + Arrays.toString(client.getShares(id)));
            client.sell(id + 1, randomValues[1], 1);
            CardServer.LOGGER.info("Bot " + id + " sell after " + Arrays.toString(client.getShares(id)));
        }

        CardServer.LOGGER.info("Bot " + id + " Cash after buy/sell: " + client.getCash(id));

        String voteOne = client.companyName(randomValues[2]);
        String voteTwo = client.companyName(randomValues[3]);

        CardServer.LOGGER.info("yes vote: " + voteOne + " no vote: " + voteTwo);

        CardServer.LOGGER.info(client.makeVotes(voteOne, "YES"));
        if (!voteOne.equals(voteTwo)) {
            CardServer.LOGGER.info(client.makeVotes(voteTwo, "NO"));
        }
    }
}
