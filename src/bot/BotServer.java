package bot;

import cards.Game;
import cards.Player;
import cards.Stock;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 * Created by njosepa on 22/11/2017.
 */

@Path("/bot")
@Produces(MediaType.APPLICATION_JSON)

public class BotServer {

    public BotServer() {
    }

    //Gets player cash
    @GET
    @Path("/cash/{id}")
    public int getCash(@PathParam("id") int id) {
        return Player.cash.get(id);
    }

    //gets player shares
    @GET
    @Path("/playerShares/{id}")
    public int[] getShare(@PathParam("id") int id) {
        return Player.shares[id];
    }

    //Calls the method to create ids for bots
    @GET
    @Path("/start")
    public String botBegins() {
        return Bot.botStart();
    }

    //gets the stock names from id
    @GET
    @Path("/names/{id}")
    public String companyNames(@PathParam("id") String id) {
        return Stock.values()[Integer.parseInt(id)].toString();
    }

    //Post buy to game to purchase stocks
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/buy")
    public String buyStock(@FormParam("id") int id, @FormParam("stockId") int stockId,
                           @FormParam("amount") int amount) {
        return Game.buy(id, stockId, amount);
    }

    //Post sell to game to sell stocks
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/sell")
    public String sellStock(@FormParam("id") int id, @FormParam("stockId") int stockId,
                            @FormParam("amount") int amount) {
        return Game.sell(id, stockId, amount);
    }

    //Post votes to game
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/vote")
    public String voteStock(@FormParam("company") String company, @FormParam("answer") String answer) {
        return Game.playerVotes(company.toUpperCase() + " " + answer);
    }

}
