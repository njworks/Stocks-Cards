package server;

import bot.Bot;
import cards.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

/**
 * Created by njosepa on 01/11/2017.
 * Reference:
 * [1] Cyclic barrier code: http://tutorials.jenkov.com/java-util-concurrent/cyclicbarrier.html
 * Accessed on: 16/11/2017 Time: 14:05 Author: Jakob Jenkov
 */

public class CardService implements Runnable {
    private Scanner input;
    private PrintWriter output;
    private String player;
    private boolean join;
    private Game game;
    private Card card;
    private int userID;
    private Integer round = 1;
    private Player playClass;

    public CardService(Game game, Socket socket) {
        this.game = game;
        join = false;
        try {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void run() {
        login();
        start(userID);
        while (join) {
            try {
                if (round > 5) {
                    Bot.playBot = false;
                    break;
                }
                playGame();
            } catch (NoSuchElementException e) {
                join = false;
            }
        }
        if (userID == 1) {
            game.gameEndScores();
        }
        threadPause();
        printWinner();
        logout();
    }

    /*
    Takes user name and creates a ID to play and starts bot. Checks if player limit reached
     */
    public void login() {
        output.println("Created by Nelvin Joseph" + "\n");
        output.println("Please enter your name to start: ");
        try {
            int totalPlayer = playClass.playerID.size();
            if (totalPlayer <= 4) {
                String in = input.nextLine().trim();
                player = in;
                playClass.createName(0, player);
                userID = playClass.playerID.size();
                System.out.println("user id: " + userID);
                output.println("Welcome " + player + " to Stocks+Cards");
                threadPause();
                if (userID == 1) {
                    System.out.println("Bot Go");
                    Bot.startBot = true;
                }
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                otherPlayerShares();
            } else {
                output.println("4 player limit reached");
                join = false;
                logout();
            }
        } catch (NoSuchElementException e) {
        }
    }

    /*
    Prints out Player cash, shares, share prices, and cards
     */
    private void start(int userID) {
        design("X");
        output.println("Your cash: " + "\u00A3" + game.getCash(userID - 1));
        output.println("Your shares: " + Deck.playerShare(userID - 1));
        output.println("Share prices: ");
        output.println(Deck.updateDeck());
        output.println("The cards: ");
        StringBuilder build = new StringBuilder();
        int i = 0;
        for (Stock s : Stock.values()) {
            build.append("  " + s + ": ");
            build.append(card.playCards[0][i]);
            i++;
        }
        output.println(build.toString());
        design("X");

        join = true;
    }

    //Prints out every other players shares other than your own
    private void otherPlayerShares() {
        design("+");
        for (int j = 0; j < playClass.playerID.size(); j++) {
            if (j == userID - 1) {
                continue;
            }
            output.println("Player: " + playClass.playerID.get(j) + ", Shares: " + Deck.playerShare(j));
        }
        design("+");
    }

    //Prints out share prices and cards
    private void updatedCards() {
        design("><");
        output.println("Share prices: ");
        output.println(Deck.updateDeck());
        output.println("The cards: ");
        StringBuilder build = new StringBuilder();
        int i = 0;
        for (Stock s : Stock.values()) {
            build.append("  " + s + ": ");
            build.append(card.playCards[0][i]);
            i++;
        }
        output.println(build.toString());
        design("><");
    }

    //Prints out cash and shares
    private void updateUser(int userID) {
        design("0");
        output.println("Your cash: " + "\u00A3" + game.getCash(userID - 1));
        output.println("Your shares: " + Deck.playerShare(userID - 1));
        design("0");
    }

    //Prints out line to split print outputs
    private void design(String symbol) {
        output.print(symbol);
        for (int n = 0; n < 74; n++) {
            output.print("-");
        }
        output.println(symbol);
    }

    /*
    playGame(): Each round actions of buy/sell and vote and vote execution
     */
    private void playGame() {
        if (round > 1) {
            start(userID);
        }
        if (userID == 1) {
            Bot.roundBot = true;
        }
        int order = 0;
        boolean action = true;
        String in;
        boolean voting = false;

        output.println("\n" + "Round " + round);
        while (action) {
            if (order >= 1) {
                action = false;
            }
            output.println("Type what you wish to buy/sell: e.g. buy apple 5 or sell apple 5");
            output.println("Buy/Sell: Apple = A, BP = B, Cisco = C, Dell = D, Ericsson = E");
            output.println("If you don't want to buy/sell type: skip");
            in = input.nextLine().trim().toUpperCase();
            String[] response = in.split(" ");
            if (response[0].equals("SKIP")) {
                break;
            }
            if (!response[0].equals("BUY") && !response[0].equals("B") &&
                    !response[0].equals("SELL") && !response[0].equals("S")) {
                output.println("Invalid command");
                continue;
            }
            int stockID = Stock.checkStock(response[1]);
            if (stockID == 6) {
                output.println("Invalid company name");
                continue;
            }
            int value = Integer.parseInt(response[2]);

            if (response[0].equals("BUY") || response[0].equals("B")) {
                output.println(game.buy(userID, stockID, value) + "\n");
                updateUser(userID);
            } else if (response[0].equals("SELL") || response[0].equals("S")) {
                output.println(game.sell(userID, stockID, value) + "\n");
                updateUser(userID);
            }
            order++;
        }
        voting = true;
        order = 2;

        while (voting) {
            if (order >= 3) {
                voting = false;
            }
            updatedCards();
            output.println("Type your vote: e.g. apple yes or bp no");
            output.println("Vote: Apple = A, BP = B, Cisco = C, Dell = D, Ericsson = E");
            output.println("If you don't want to vote type: skip");
            in = input.nextLine().trim().toUpperCase();
            String[] temp = in.split(" ");
            if (temp[0].equals("SKIP")) {
                break;
            }

            game.playerVotes(in);
            order++;


        }
        round++;
        threadPause();
        if (userID == 1) {
            CardServer.LOGGER.info("Before vote " + Deck.updateDeck());
            game.vote();
            CardServer.LOGGER.info("After vote " + Deck.updateDeck());
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Cyclic barrier await method called after each round. See reference [1] at top.
    public void threadPause() {
        try {
            output.println("Waiting for other players");
            System.out.println(Thread.currentThread().getName() + " is waiting on barrier");
            CardServer.barrier.await();
            System.out.println(Thread.currentThread().getName() + " has crossed the barrier");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    //Prints out each players total cash at end of game and the winner
    public void printWinner() {
        int i = 4;
        for (Map.Entry<Integer, String> entry : game.gameEnd.entrySet()) {
            if (i == 1) {
                output.println("WINNER" + ": £" + entry.getKey() + " Player: " + entry.getValue());
                CardServer.LOGGER.info("WINNER" + ": £" + entry.getKey() + " Player: " + entry.getValue());
            } else {
                output.println(i + ": £" + entry.getKey() + " Player: " + entry.getValue());
                CardServer.LOGGER.info(i + ": £" + entry.getKey() + " Player: " + entry.getValue());
            }
            i--;
        }
        output.println("Thank you for playing Stocks+Cards");
        output.println("Created by Nelvin Joseph");
        try {
            Thread.sleep(6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Close scanner, printwriter and closes while loop for socket thread
    public void logout() {
        if (player != null) {
            System.out.println("Bye " + player);
        }
        try {
            Thread.sleep(2000);
            input.close();
            output.close();
            CardServer.online = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
