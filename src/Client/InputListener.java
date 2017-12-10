/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Bruno Santos
 */
import java.util.Scanner;

public class InputListener implements Runnable
{
    private static final String CMD_EXIT = "exit";
    
    public InputListener()
    {
        
    }
    
    @Override
    public void run()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        while(!Thread.currentThread().isInterrupted())
        {
            input = scanner.nextLine();

            if(input.equalsIgnoreCase(CMD_EXIT))
                return;

        }
    }
    
    //versões estáticas que permitem pedir input diretamente (para casos mais particulares)
    public synchronized static String readLine()
    {
        return readLine(null);
    }
    public synchronized static String readLine(String header)
    {
        //if(header != null)
            
        
        return new Scanner(System.in).nextLine();
    }
}
