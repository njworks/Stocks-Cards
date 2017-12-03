# Stocks-Cards
Java game using sockets/web server + bots. 4 Players max

To run the game, run the CardServer and choose 1-4 players, the rest of players will be bots. Open PuTTY and type localhost as Hostname and 
port 8888 and raw connection.

The game is played by one to four players.
The game involves five stocks: Apple, BP, Cisco, Dell and Ericsson
For each of the stocks, there is a deck with six influence cards which affect the share price of that stock.
The six different cards are: -20, -10, -5, +5, +10, +20. The number indicates the effect of that card on the stock price.
For example, the card "-5" in the Apple deck will decrease the Apple share price by 5, while the card "+10" in the Cisco deck will increase the Cisco share price by 10.
At the start of the game:
All stocks have an initial share price of £100
Each player is given £500
Each player is issued with 10 random shares.
The five decks with influence cards are shuffled. The top-most card of each deck is visible to all the players.
The game has five rounds. Each round proceeds as follows:
At the start of the round, all players are informed of the current stock prices, the currently visible cards, and the current cash and share holding of all players

Each player can perform up to two trading actions. Each trading action consists of either buying or selling a certain number of shares in one stock. Players do not trade shares with each other. Instead, each player trades in secret with the "stock exchange". Trading does not effect the share prices, but there is a fee of £3 for each share bought. Players are not allowed to get into debt, e.g. their cash balance must never be negative. Similarly, players are not allowed to sell more shares than they own, e.g. a share holding must never be negative.

Each player can also submit up to two votes for the visible cards. A vote is either YES or NO. A player cannot vote twice for the same card in one round.

The round ends once every player has finished voting. At this point:
All cards which received more YES than NO votes will be executed. The stock prices will be updated accordingly. These cards are removed from the game.
All cards which received more NO than YES votes will be removed from the game. These cards are not executed.
When a card is removed from the game, then the next card of that deck becomes visible.

After the five rounds, the shares of all players are sold at their final price. The winner of the game is the player with the most cash. Players are notified of the scores of all players with a declaration of the winner(s).

Note that:
The application should support concurrent game play.
During a round, players should perform their actions independently and in secret.
Trading and voting actions are optional. Players should be able to skip these actions if they do not wish to perform them.
The application should provide suitable feedback to players throughout the game.

The Players are implemented on the sockets and the bots on the Jersey web server. 
