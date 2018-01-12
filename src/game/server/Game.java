package game.server;

import java.util.Observable;
import java.util.Observer;
import logic.GameModel;
import logic.ObservableGame;

public class Game implements Observer {

    ObservableGame game;

    public Game(String user1, String user2) {
        game = new ObservableGame();
        game.setPlayerName(1, user1);
        game.setPlayerName(2, user2);
        game.addObserver(Game.this);
        game.startGame();
    }

    public GameModel getGame() {
        return game.getGameModel();
    }

    public void setGame(GameModel game) {
        this.game.setGameModel(game);
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        System.out.println("Game: update!");
    }

}
