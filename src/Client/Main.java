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
public class Main
{
   
    public static void main(String[] args) throws IllegalStateException
    { 
//        if(args.length != 2)
//        {
//            System.out.println("Wrong syntax! serviceIP servicePort");
//            return;
//        }
        
        Client cl = new Client("localhost", "6001");
            cl.start();
        
        }
    
}
