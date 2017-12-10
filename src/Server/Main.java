/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Bruno Santos
 */
public class Main
{
    
    public static void main(String[] args) throws IllegalStateException, ClassNotFoundException
    {
//        if(args.length != 3)
//        {
//            System.out.println("Wrong syntax! serverName serviceIP servicePort");
//            return;
//        }
        
        Server server = new Server("server", "localhost", 6001);
        server.start();
    }
    
}
