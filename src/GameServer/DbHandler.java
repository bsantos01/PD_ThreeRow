package GameServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import logic.GameModel;

public class DbHandler {

    Connection myConn;
    Statement myStmt;
    ResultSet rs;

    String user;
    String pass;

    String ipAndPort;

    public DbHandler(String ipAndPort) {
        myConn = null;
        myStmt = null;
        rs = null;

        user = "root";
        pass = "root123";

        this.ipAndPort = ipAndPort;
    }

    //re-think this
    public Map<String, String> getPairs() throws SQLException {
        Map<String, String> Pairs = new HashMap<>();
        connect();
        rs = myStmt.executeQuery("SELECT * FROM Pairs WHERE active=TRUE AND status=???;");

        if (rs.next() == false) {
            System.out.println("No pairs waiting.");
            return null;
        } else {

            do {
                Pairs.put(rs.getString("user1"), rs.getString("user2"));
            } while (rs.next());
        }
        close();
        return Pairs;
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

    public boolean saveUnfinishedGame(String user1, String user2, GameModel game) throws SQLException, IOException { //byte[] byteGame

        //this be a level up
        byte[] gameByte;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(game);
        oos.flush();
        gameByte = bos.toByteArray();
        oos.close();
        bos.close();

        PreparedStatement myPrepStmt;
        connect();

        myPrepStmt = myConn.prepareStatement("INSERT INTO SAVEGAME(user1, user2, game)) VALUES(?, ?, ?)");
        myPrepStmt.setString(1, "somefilename");
        myPrepStmt.setString(2, "somefilename");
        myPrepStmt.setObject(3, gameByte);//data has to be byte[]
        myPrepStmt.executeUpdate();

        close();

        return true;
    }

    private void connect() {
        try {
            //connection to database
            myConn = DriverManager.getConnection("jdbc:mysql://" + ipAndPort + "/teste?verifyServerCertificate=false&useSSL=true", user, pass);
            //localhost:3306
            // Create a statement
            myStmt = myConn.createStatement();
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("GameServer DbHandler Connecting SQLException " + ex);
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
            System.out.println("GameServer DbHandler setOcuppied SQLException " + ex);
        }
    }

    void freePlayer(String string) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Client SET free=true where username='" + string + "';");

            close();
        } catch (SQLException ex) {
            System.out.println("GameServer DbHandler setFree SQLException " + ex);
        }
    }

    void setStatusX(String string) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Pairs SET status=??? where username='" + string + "';");

            close();
        } catch (SQLException ex) {
            System.out.println("GameServer DbHandler setStatusX SQLException " + ex);
        }
    }

}
