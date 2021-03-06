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

    String ipAndPort;

    public DBhandler(String ip) {
        myConn = null;
        myStmt = null;
        rs = null;

        user = "root";
        pass = "root123";

        this.ipAndPort = ip;
    }

    public boolean login(String name, String Pass, String ip, int port) throws SQLException {

        connect();

        rs = myStmt.executeQuery("SELECT 1 FROM Client WHERE username='" + name + "' AND pass='" + Pass + "' AND active=FALSE;");
        boolean temp = rs.next();
        if (temp == true) {
            myStmt.executeUpdate("UPDATE Client SET active=1, free=true, ingame=0, IP='" + ip + "', PORT='" + port + "' where username='" + name + "';");
        }

        close();
        return temp;

    }

    public List<String> getFinishedGames() throws SQLException {
        List<String> FPlist = new ArrayList<String>();
        connect();
        rs = myStmt.executeQuery("SELECT user1, user2, winner FROM pairs WHERE status='finished';");

        if (rs.next() == false) {
            System.out.println("No finished games.");
            return null;
        } else {

            do {
                FPlist.add((rs.getString("user1") + " VS " + rs.getString("user2") + " - " + rs.getString("winner")));
            } while (rs.next());
        }
        close();
        return FPlist;
    }

    public List<String> GetHistory(String user) throws SQLException {
        List<String> FPlist = new ArrayList<String>();
        connect();
        rs = myStmt.executeQuery("SELECT user1, user2, status, winner FROM pairs WHERE user1='" + user + "';");
        while (rs.next()) {
            FPlist.add(rs.getString("user1") + " VS " + rs.getString("user2") + " - " + rs.getString("status") + " winner: " + rs.getString("winner"));
        }
        rs = myStmt.executeQuery("SELECT user1, user2, status, winner FROM pairs WHERE user2='" + user + "';");
        while (rs.next()) {
            FPlist.add((rs.getString("user1") + " VS " + rs.getString("user2") + " - " + rs.getString("winner")));
        }
        close();
        return FPlist;
    }

    public List<String> getUnfinishedGames() throws SQLException {
        List<String> FPlist = new ArrayList<String>();
        connect();
        rs = myStmt.executeQuery("SELECT user1, user2, winner FROM pairs WHERE status='interrupted';");

        if (rs.next() == false) {
            System.out.println("No unfinished games.");
            return null;
        } else {

            do {
                FPlist.add((rs.getString("user1") + " VS " + rs.getString("user2") + " - " + rs.getString("winner")));
            } while (rs.next());
        }
        close();
        return FPlist;
    }

    public List<String> getFreePlayers() throws SQLException {
        List<String> FPlist = new ArrayList<String>();
        connect();
        rs = myStmt.executeQuery("SELECT username FROM Client WHERE active=TRUE AND ingame=false AND free=true;");

        if (rs.next() == false) {
            System.out.println("No free players.");
            return null;
        } else {

            do {
                FPlist.add(rs.getString("username"));
            } while (rs.next());
        }
        close();
        return FPlist;
    }

    public List<String> getUsersLogged() throws SQLException {
        List<String> list = new ArrayList<String>();
        connect();

        rs = myStmt.executeQuery("SELECT username, free FROM Client WHERE active=true;");
        if (rs.next() == false) {
            System.out.println("No active players.");
            return null;
        } else {
            do {
                list.add(rs.getString("username") + " is free: " + rs.getBoolean("free"));
            } while (rs.next());
        }
        close();
        return list;
    }
    //AND ingame=false AND free=true

    public List<String> getPairs() throws SQLException {
        List<String> list = new ArrayList<String>();
        connect();

        rs = myStmt.executeQuery("SELECT user1, user2, status FROM Pairs WHERE status='inCreation';");
        if (rs.next() == false) {
            System.out.println("No pairs available.");
            return null;
        } else {
            do {
                list.add(rs.getString("user1") + " : " + rs.getString("user2") + " : " + rs.getString("status"));
            } while (rs.next());
        }
        close();
        return list;
    }

    public String getPortbyUsername(String username) throws SQLException {
        String ret;
        connect();
        rs = myStmt.executeQuery("SELECT port FROM Client WHERE username='" + username + "';");

        if (rs.next() != false) {
            ret = rs.getString("port");
        } else {
            ret = null;
        }

        close();

        return ret;
    }

    public String getUsernamebyPort(String port) throws SQLException {
        String ret;
        connect();
        rs = myStmt.executeQuery("SELECT username FROM Client WHERE port='" + port + "';");

        if (rs.next() != false) {
            ret = rs.getString("username");
        } else {
            ret = null;
        }

        close();

        return ret;
    }

    public String getIPbyUsername(String username) throws SQLException {
        String ret;
        connect();
        rs = myStmt.executeQuery("SELECT ip FROM Client WHERE username='" + username + "';");

        if (rs.next() != false) {
            ret = rs.getString("ip");
        } else {
            ret = null;
        }

        close();

        return ret;
    }

    public String getUsernamebyIP(String ip) throws SQLException {
        String ret;
        connect();
        rs = myStmt.executeQuery("SELECT username FROM Client WHERE ip='" + ip + "';");

        if (rs.next() != false) {
            ret = rs.getString("username");
        } else {
            ret = null;
        }

        close();

        return ret;
    }

    public boolean register(String name, String Pass) throws SQLException, CustomException {

        connect();

        rs = myStmt.executeQuery("SELECT 1 FROM Client WHERE username='" + name + "';");

        if (rs.next() != true) //senão for encontrado um registo com o mesmo nome
        {
            myStmt.executeUpdate("INSERT INTO CLIENT(username, pass, active, ingame, free) VALUES('" + name + "', '" + Pass + "', TRUE, false, true);");
            close();
            return true;
        } else {
            close();
            throw new CustomException("Username already in use, try another!");
        }

    }

    private void connect() {
        try {
            //connection to database
            myConn = DriverManager.getConnection("jdbc:mysql://" + ipAndPort + "/teste?verifyServerCertificate=false&useSSL=true", user, pass);

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

    private void close() throws SQLException {
        if (myStmt != null) {
            myStmt.close();
        }

        if (myConn != null) {
            myConn.close();
        }

    }

    ;

    void killPlayers() throws SQLException {
        connect();

        myStmt.executeUpdate("UPDATE Client SET active=false, ip='', port='';");

        close();
    }

    void setOcuppied(String string) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Client SET free=false where username='" + string + "';");

            close();
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void freePlayer(String string) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Client SET free=true where username='" + string + "';");

            close();
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void createMatch(String P1, String P2) {

        try {
            connect();

            myStmt.executeUpdate("INSERT INTO PAIRS(user1, user2, status, winner) VALUES('" + P1 + "', '" + P2 + "' ,'inRequest', 'none');");
            myStmt.executeUpdate("UPDATE Client SET ingame=true where username='" + P1 + "';");
            myStmt.executeUpdate("UPDATE Client SET ingame=true where username='" + P2 + "';");
            close();
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void logout(String string) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Client SET active=false, ip='', port='' where username= '" + string + "';");
            close();
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void deleteMatch(String string, String string0) {
        try {
            connect();
            myStmt.executeUpdate("DELETE FROM pairs WHERE user1='" + string + "' AND user2='" + string0 + "' AND status='inRequest';");
            close();
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int cancelMatch(String string, String string0) {
        connect();
        int i = 0;
        try {

            i = myStmt.executeUpdate("DELETE FROM pairs WHERE user1='" + string + "' AND user2='" + string0 + "' AND status='inCreation';");
            System.out.println("variavel retorna " + i);
            if (i == 0) {
                i = myStmt.executeUpdate("DELETE FROM pairs WHERE user1='" + string0 + "' AND user2='" + string + "' AND status='inCreation';");
            }
            System.out.println("variavel depois retorna " + i);
            close();

        } catch (SQLException ex) {
            System.out.println("Tentativa de eliminar um registo inexistente.");
        }

        this.freePlayer(string);
        this.freePlayer(string0);
        return i;
    }

    void updateMatch(String string, String string0) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE pairs SET status='inCreation' where user1='" + string + "' AND user2='" + string0 + "';");
            close();
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void updateClientPort(String string, String string0) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Client SET port='" + string0 + "' where username= '" + string + "';");
            close();
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
