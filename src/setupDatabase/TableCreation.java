package setupDatabase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreation {

    Connection myConn;
    Statement myStmt;
    ResultSet rs;

    String user;
    String pass;

    public TableCreation() {
        myConn = null;
        myStmt = null;
        rs = null;

        user = "root";
        pass = "root123";

    }

    private void connect() {
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
                    System.err.println("TableCreation: SqlException" + ex);
                }
            }

        }
    }

    public void generateTableClient() throws SQLException {

        connect();

        DatabaseMetaData dbm = myConn.getMetaData();
        ResultSet res = dbm.getTables(null, null, "CLIENT", null);
        if (!res.next()) {

            myStmt.executeUpdate("CREATE TABLE client (\n"
                    + "  username varchar(45) NOT NULL,\n"
                    + "  pass varchar(45) DEFAULT NULL,\n"
                    + "  active tinyint(4) DEFAULT NULL,\n"
                    + "  ip varchar(20) DEFAULT NULL,\n"
                    + "  port varchar(20) DEFAULT NULL,\n"
                    + "  ingame tinyint(4) DEFAULT NULL,\n"
                    + "  free tinyint(4) DEFAULT NULL,\n"
                    + "  PRIMARY KEY (username)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

            System.out.println("TableCreation: Clients generated!");
        } else {
            System.out.println("Table CLIENT already exists!");
        }
        close();
    }

    public void generateTablePairs() throws SQLException {

        connect();

        DatabaseMetaData dbm = myConn.getMetaData();
        ResultSet res = dbm.getTables(null, null, "PAIRS", null);
        if (!res.next()) {

            myStmt.executeUpdate("CREATE TABLE pairs (\n"
                    + "  id int(11) NOT NULL,\n"
                    + "  user1 varchar(45) DEFAULT NULL,\n"
                    + "  user2 varchar(45) DEFAULT NULL,\n"
                    + "  status varchar(45) DEFAULT NULL,\n"
                    + "  winner varchar(45) DEFAULT NULL,\n"
                    + "  PRIMARY KEY (id)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

            //Status:
            //onRequest
            //inCreation
            //inGame
            //finished
            //interrupted
            //Winner:
            //user1
            //user2
            //null
            System.out.println("TableCreation: Pairs generated!");

        } else {
            System.out.println("Table PAIRS already exists!");
        }

        close();
    }

    //not needed
//    public void generateTableGames() throws SQLException {
//
//        connect();
//
//        DatabaseMetaData dbm = myConn.getMetaData();
//        ResultSet res = dbm.getTables(null, null, "GAMES", null);
//        if (!res.next()) {
//            myStmt.executeUpdate("CREATE TABLE games (\n"
//                    + "  id int(11) NOT NULL,\n"
//                    + "  user1 varchar(45) DEFAULT NULL,\n"
//                    + "  user2 varchar(45) DEFAULT NULL,\n"
//                    + "  game BLOB,\n"
//                    + "  status varchar(45) DEFAULT NULL,\n"
//                    + "  PRIMARY KEY (id)\n"
//                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
//            System.out.println("TableCreation: Savegame generated!");
//
//        } else {
//            System.out.println("Table SAVEGAME already exists!");
//        }
//        close();
//    }
    private void close() throws SQLException {
        if (myStmt != null) {
            myStmt.close();
        }

        if (myConn != null) {
            myConn.close();
        }

    }
;

}
