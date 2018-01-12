package game.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameDBHandler {

    Connection myConn;
    Statement myStmt;
    ResultSet rs;

    String user;
    String pass;

    String ipAndPort;

    public GameDBHandler(String ipAndPort) {
        myConn = null;
        myStmt = null;
        rs = null;

        user = "root";
        pass = "root123";

        this.ipAndPort = ipAndPort;
    }

    //re-think this
    public List<Pair> getPairs() throws SQLException {
        List<Pair> pair = new ArrayList<>();

        connect();
        rs = myStmt.executeQuery("SELECT * FROM Pairs WHERE status=inCreation;");

        if (rs.next() == false) {
            System.out.println("No pairs waiting.");
            return null;
        } else {

            do {
                pair.add(new Pair(rs.getInt("id"), rs.getString("user1"), rs.getString("user2"), rs.getString("status"), rs.getString("winner")));
            } while (rs.next());
        }
        close();
        return pair;
    }

    public String getPortbyUsername(String username) throws SQLException {
        String port;
        connect();
        rs = myStmt.executeQuery("SELECT port FROM Client WHERE username='" + username + "';");

        if (rs.next() != false) {
            port = rs.getString("port");
        } else {
            port = null;
        }

        close();
        return port;
    }

    public String getIPbyUsername(String username) throws SQLException {
        String ip;
        connect();
        rs = myStmt.executeQuery("SELECT ip FROM Client WHERE username='" + username + "';");

        if (rs.next() != false) {
            ip = rs.getString("ip");
        } else {
            ip = null;
        }

        close();
        return ip;
    }

    public boolean isActive(String username) throws SQLException {//redo
        connect();
        rs = myStmt.executeQuery("SELECT active FROM Client WHERE username='" + username + "';");

        if (rs.next() != false) {
            return rs.getBoolean("active");
        }

        close();
        return false;
    }

//    public boolean saveUnfinishedGame(String user1, String user2, GameModel game) throws SQLException, IOException { //byte[] byteGame
//
//        //this be a level up
//        byte[] gameByte;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(bos);
//        oos.writeObject(game);
//        oos.flush();
//        gameByte = bos.toByteArray();
//        oos.close();
//        bos.close();
//
//        PreparedStatement myPrepStmt;
//        connect();
//
//        myPrepStmt = myConn.prepareStatement("INSERT INTO SAVEGAME(user1, user2, game)) VALUES(?, ?, ?)");
//        myPrepStmt.setString(1, "somefilename");
//        myPrepStmt.setString(2, "somefilename");
//        myPrepStmt.setObject(3, gameByte);//data has to be byte[]
//        myPrepStmt.executeUpdate();
//
//        close();
//
//        return true;
//    }
    private void connect() {
        try {
            //connection to database

            myConn = DriverManager.getConnection("jdbc:mysql://" + ipAndPort + "/teste?verifyServerCertificate=false&useSSL=true", user, pass);
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

    void setInGame(int id) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Pairs SET status=inGame where id='" + id + "';");

            close();
        } catch (SQLException ex) {
            System.out.println("GameServer DbHandler setInGame SQLException " + ex);
        }
    }

    void setInterrupted(int id) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Pairs SET status=interrupted where id='" + id + "';");

            close();
        } catch (SQLException ex) {
            System.out.println("GameServer DbHandler setInGame SQLException " + ex);
        }

    }

    void setWinner(int id, String username) {
        try {
            connect();
            myStmt.executeUpdate("UPDATE Pairs SET winner='" + username + "' where id='" + id + "';");
            myStmt.executeUpdate("UPDATE Pairs SET status=finished where id='" + id + "';");

            close();
        } catch (SQLException ex) {
            System.out.println("GameServer DbHandler setInGame SQLException " + ex);
        }

    }

}
