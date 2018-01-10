package GameServer;

import java.util.Observable;
import java.util.Observer;
import logic.GameModel;
import logic.ObservableGame;

public class Game implements Observer {

    ObservableGame game;

    public Game() {
        game = new ObservableGame();
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
