package bot;

import cards.Card;
import cards.Game;
import cards.Player;
import org.glassfish.jersey.client.ClientConfig;
import web.GsonMessageBodyHandler;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by njosepa on 22/11/2017.
 */

public class BotClient {
    public static WebTarget webTarget;

    public BotClient(String gameURL) {
        ClientConfig config = new ClientConfig(GsonMessageBodyHandler.class);
        webTarget = ClientBuilder.newClient(config).target(gameURL);
    }

    //Calls the bot to create its IDs
    public String botBegin() {
        return webTarget.path("start").request().get(String.class);
    }

    //calls to get bot cash
    public static Double getCash(int id) {
        return webTarget.path("cash").path(Integer.toString(id)).request().get(Double.class);
    }

    //gets bots shares
    public static int[] getShares(int id) {
        return webTarget.path("playerShares").path(Integer.toString(id)).request().get(int[].class);
    }

    /*
    Pass in buy parameters for Bot to web server
     */
    public String purchase(int id, int stock, int amount) {
        Form form = new Form();
        form.param("id", Integer.toString(id));
        form.param("stockId", Integer.toString(stock));
        form.param("amount", Integer.toString(amount));
        return webTarget.path("buy").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED))
                .readEntity(String.class);
    }

    /*
    Pass in sell parameters for Bot to web server
     */
    public static String sell(int id, int stock, int amount) {
        Form form = new Form();
        form.param("id", Integer.toString(id));
        form.param("stockId", Integer.toString(stock));
        form.param("amount", Integer.toString(amount));
        return webTarget.path("sell").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED))
                .readEntity(String.class);
    }

    //Calls the company name from id to words for voting
    public String companyName(int id) {
        return webTarget.path("names").path(Integer.toString(id)).request().get(String.class);
    }

    //Pass in parameters to votes in web server
    public static String makeVotes(String company, String answer) {
        Form form = new Form();
        form.param("company", company);
        form.param("answer", answer);
        return webTarget.path("vote").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED))
                .readEntity(String.class);
    }

}
