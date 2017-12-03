package bot;

import cards.Game;
import cards.Player;
import cards.Stock;
import org.junit.Test;
import web.TomcatServer;

import java.util.Arrays;

/**
 * Created by njosepa on 28/11/2017.
 */
public class BotTest {

    public BotClient client = new BotClient(TomcatServer.BOT_URL);

    public BotTest() {
    }

    //Tests cash is returned
    @Test
    public void cash() {
        System.out.println(BotClient.getCash(0));
    }

    //tests bot creates botIds for remaining spaces for players
    @Test
    public void play() {
        System.out.println(client.botBegin());
    }

    //Bot calls the sell from game and sells apple of playerid 0
    @Test
    public void sell() {
        System.out.println(client.sell(1, 0, 1));
    }

    //Gets stock name from company id
    @Test
    public void company() {
        System.out.println(client.companyName(1));
    }

    //Bot buys 1 apple of playerid 0
    @Test
    public void buy() {
        System.out.println(client.purchase(1, 0, 1));
    }

    //Bot votes for apple no
    @Test
    public void vote() {
        System.out.println(client.makeVotes(client.companyName(0), "NO"));
    }

    //Bot prints out player 0 shares
    @Test
    public void share() {
        System.out.println(Arrays.toString(client.getShares(0)));
    }

    //Bot gets random values and uses it to make random buy/sell/votes
    @Test
    public void botPlay() {
        Bot.randomBot(0);
    }

}
