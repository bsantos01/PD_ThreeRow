


import logic.ObservableGame;
import ui.text.TextUI;

public class MainWithTextUI {

    public static void main(String[] args) {
        TextUI textUI = new TextUI(new ObservableGame());
        textUI.run();
    }
}
