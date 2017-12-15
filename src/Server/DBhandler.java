/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Santos
 */
public class DBhandler {
  
        Connection myConn;
        Statement myStmt;
        ResultSet myRs;

        String user;
        String pass;
        
        
    
    public DBhandler() 
    {
         myConn = null;
         myStmt = null;
         myRs = null;

        user = "root";
        pass = "root123";
        
    }
        
    public boolean login(String name, String Pass) throws SQLException {
            
        connect();

        myRs = myStmt.executeQuery("SELECT 1 FROM Client WHERE username='"+name+"' AND pass='"+Pass+"' AND active=FALSE;");
        boolean temp= myRs.next();
        close();
        return temp;
                
    }
    
    public boolean register(String name, String Pass) throws SQLException, CustomException {
            
        connect();

        myRs = myStmt.executeQuery("SELECT 1 FROM Client WHERE username='"+name+"';");
        
        if(myRs.next()!=true) //senão for encontrado um registo com o mesmo nome
        {
            myStmt.executeUpdate("INSERT INTO CLIENT(username, pass, active) VALUES('"+name+"', '"+Pass+"', TRUE);");          
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
            if (myRs != null) {
                try {
                    myRs.close();
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
        
        myStmt.executeUpdate("UPDATE Client SET active=false;");
        
        close();
    }
}
