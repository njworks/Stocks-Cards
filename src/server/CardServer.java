package server;

import bot.Bot;
import cards.Game;
import web.TomcatServer;

import java.io.Console;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.*;

/**
 * Created by njosepa on 27/10/2017.
 */
public class CardServer {

    public static final Logger LOGGER = Logger.getLogger("serverLog");
    static {
        try {
            Handler h = new FileHandler("ServerLog.log");
            h.setFormatter(new SimpleFormatter());
            h.setLevel(Level.ALL);
            Handler console = new StreamHandler(System.out,new SimpleFormatter());
            console.setLevel(Level.INFO);
            LOGGER.addHandler(h);
            LOGGER.addHandler(console);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public static boolean play = false;
    public static boolean online = true;

    private static final int PORT = 8888;
    public static CyclicBarrier barrier;

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        Game card = new Game();
        ServerSocket server = new ServerSocket(PORT);
        Scanner in = new Scanner(System.in);
        System.out.println("How many players? 4 max. (if less than 4, then bots will be added");
        String playerNo = in.nextLine();
        int number = Integer.parseInt(playerNo);
        in.close();
        online = true;
        if(number > 4){
            System.out.println("No more than 4 players");
            System.out.println("4 player game setup");
            number = 4;
        }
            barrier = new CyclicBarrier(number);


        System.out.println("Started Game server at port " + PORT);
        System.out.println("Waiting for players to connect...");

        TomcatServer tom = new TomcatServer();
        Bot myBot = new Bot();

        if (number < 4){
            new Thread(tom).start();
            new Thread(myBot).start();
        }

        while (online) {
            Socket socket = server.accept();
            System.out.println("Player connected.");
            CardService service = new CardService(card, socket);
            new Thread(service).start();
        }
    }
}
