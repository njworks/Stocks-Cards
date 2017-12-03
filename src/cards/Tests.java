package cards;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Tests {

    public Game game;

    //Sets up the game class
    @Before
    public void setup() {
        game = new Game();
        Player.createName(0, "firstPlayer");
        Player.createName(0, "secondPlayer");
    }

    //Adds players to playerid and cash
    @Test
    public void addPlayers() {
        Assert.assertEquals(Player.playerID.get(0), "firstPlayer");
        Assert.assertEquals(Player.playerID.get(1), "secondPlayer");
    }

    //retrieve cash if player was created
    @Test
    public void cash() {
        Assert.assertTrue("matchs", 500 == Player.cash.get(1));
    }

    //Gets stock shares
    @Test
    public void sharePrices() {
        Assert.assertArrayEquals(Player.sharePrice, new int[]{100, 100, 100, 100, 100});
    }

    //buys stocks
    @Test
    public void buy() {
        int apple = Player.shares[1][0];
        game.buy(1, 0, 1);
        int boughtapple = Player.shares[1][0];
        Assert.assertEquals(boughtapple, apple);
    }

    //sell stocks
    @Test
    public void sell() {
        int apple = Player.shares[1][0];
        game.sell(1, 0, 1);
        int soldapple = Player.shares[1][0];
        Assert.assertEquals(soldapple, apple);
    }

    //vote for apple
    @Test
    public void takeVotes() {
        System.out.println(game.playerVotes("APPLE YES"));
        Assert.assertTrue(1 == game.totalVotes.get(0));
    }

    //check if votes are executed
    @Test
    public void executeVote() {
        int sharePrice = Player.sharePrice[0];
        int cardValue = Card.playCards[0][0];
        int total = sharePrice + cardValue;
        game.playerVotes("APPLE YES");
        game.vote();
        Assert.assertTrue(total == Player.sharePrice[0]);
    }

}
