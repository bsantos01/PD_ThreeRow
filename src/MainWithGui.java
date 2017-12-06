

import logic.ObservableGame;
import ui.gui.ThreeInRowView;

public class MainWithGui 
{

    public static void main(String[] args)
    {                
        ThreeInRowView GUI = new ThreeInRowView(new ObservableGame());
    }
    
}
