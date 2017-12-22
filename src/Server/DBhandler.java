/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Santos
 */
public class DBhandler {
  
        Connection myConn;
        Statement myStmt;
        ResultSet rs;

        String user;
        String pass;
        
        
    
    public DBhandler() 
    {
         myConn = null;
         myStmt = null;
         rs = null;

        user = "root";
        pass = "root123";
        
    }
        
    public boolean login(String name, String Pass, InetAddress ip, int port) throws SQLException {
            
        connect();

        rs = myStmt.executeQuery("SELECT 1 FROM Client WHERE username='"+name+"' AND pass='"+Pass+"' AND active=FALSE;");
        boolean temp= rs.next();
        if(temp=true){
            myStmt.executeUpdate("UPDATE Client SET active=1, free=true, ingame=0, IP='"+ip+"', PORT='"+port+"' where username='"+name+"';");
        }
          
        close();
        return temp;
                
    }
    
    
    public List<String> getFreePlayers() throws SQLException{
        List<String> FPlist= new ArrayList<String>();
        connect();
        rs = myStmt.executeQuery("SELECT username FROM Client WHERE active=TRUE AND ingame=false AND free=true;");
        
        if (rs.next() == false) { 
            System.out.println("No free players."); 
            return null;
        } else { 

            do{ 
                FPlist.add(rs.getString("username"));   
            }while (rs.next()); 
        }
        close();
        return FPlist;      
    }
    
        public String getPortbyUsername(String username) throws SQLException{
        String ret;
        connect();
        rs = myStmt.executeQuery("SELECT port FROM Client WHERE username='"+username+"';");
        
        if(rs.next()!=false)
           ret= rs.getString("port");
        else 
            ret=null;
        
        close();

        return ret;
    }
    
    public String getUsernamebyPort(String port) throws SQLException{
        String ret;
        connect();
        rs = myStmt.executeQuery("SELECT username FROM Client WHERE port='"+port+"';");
        
        if(rs.next()!=false)
           ret= rs.getString("username");
        else 
            ret=null;
        
        close();

        return ret;
    }
    
    public String getIPbyUsername(String username) throws SQLException{
        String ret;
        connect();
        rs = myStmt.executeQuery("SELECT ip FROM Client WHERE username='"+username+"';");
        
        if(rs.next()!=false)
           ret= rs.getString("ip");
        else 
            ret=null;
        
        close();

        return ret;
    }
    
    public String getUsernamebyIP(String ip) throws SQLException{
        String ret;
        connect();
        rs = myStmt.executeQuery("SELECT username FROM Client WHERE ip='"+ip+"';");
        
        if(rs.next()!=false)
           ret= rs.getString("username");
        else 
            ret=null;
        
        close();

        return ret;
    }
    
    public boolean register(String name, String Pass) throws SQLException, CustomException {
            
        connect();

        rs = myStmt.executeQuery("SELECT 1 FROM Client WHERE username='"+name+"';");
        
        if(rs.next()!=true) //senão for encontrado um registo com o mesmo nome
        {
            myStmt.executeUpdate("INSERT INTO CLIENT(username, pass, active, ingame, free) VALUES('"+name+"', '"+Pass+"', TRUE, false, true);");          
            close();
            return true;
        }
        else
        {
            close();
            throw new CustomException("Username already in use, try another!");    
        }
    
     
    } 
    
    private void connect()
    {
        try {
            //connection to database
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste?verifyServerCertificate=false&useSSL=true", user, pass);

            // Create a statement
            myStmt = myConn.createStatement();
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


        }
    }
    private void close() throws SQLException{
            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            }
    
    };

    void killPlayers() throws SQLException {
        connect();
        
        myStmt.executeUpdate("UPDATE Client SET active=false, ip='', port='';");
        
        close();
    }
}
