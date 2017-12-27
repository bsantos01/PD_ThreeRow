package GameClient;

import java.io.IOException;

public class Main
{
   
    public static void main(String[] args) throws IllegalStateException, IOException
    { 
       
        GameClient cl = new GameClient("localhost", "7777");
            cl.start();
        
        }
}
