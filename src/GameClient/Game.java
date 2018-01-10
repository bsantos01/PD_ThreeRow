package GameClient;

import java.util.Observable;
import java.util.Observer;
import logic.GameModel;
import logic.ObservableGame;
import ui.gui.PopupView;
import ui.gui.ThreeInRowView;

public class Game implements Observer {

    ObservableGame game;
    ThreeInRowView gui;
    PopupView pop;

    String player;
    boolean turn;

    public Game(String player) {
        this.player = player;

    }

    public ObservableGame getObservableGame() {
        return game;
    }

    public boolean getTurn() {
        return turn;
    }

    public GameModel getGame() {
        return game.getGameModel();
    }

    public void setGame(GameModel game) {
        this.game.setGameModel(game);
    }

    public void updateGame(GameModel model) {
        if (game == null) {
            System.out.print("Game: Yesh, i was null... but no longer!");
            game = new ObservableGame();
            game.addObserver(Game.this);
            game.startGame();
            game.setGameModel(model);

            System.out.print("Game: GameModel arrived! ");
            gui = new ThreeInRowView(game, player);
            if (!game.getCurrentPlayerName().equals(player)) {
                gui.enableGrid(false);
            }
        } else {
            System.out.print("Game: Recieved a GameModel. " + game.getCurrentPlayerName() + " ");
            game.setGameModel(model);
        }

    }

    public void closeGui() {

        pop = new PopupView();
        gui.close();
    }

    public void closePop() {
        pop.close();

    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        System.out.println("Game: sync update");
        if (!game.getCurrentPlayerName().equals(player)) {

            turn = false;
            if (gui != null) {
                gui.enableGrid(false);
            }
        } else {
            if (gui != null) {
                gui.enableGrid(true);
            }
            turn = true;
        }
        System.out.println("Game: update! it's my turn? - " + turn);

    }

}
