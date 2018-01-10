package tableGeneration;

import java.sql.Connection;
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

        close();
    }

    public void generateTablePairs() throws SQLException {

        connect();

        myStmt.executeUpdate("CREATE TABLE pairs (\n"
                + "  id int(11) NOT NULL,\n"
                + "  user1 varchar(45) DEFAULT NULL,\n"
                + "  user2 varchar(45) DEFAULT NULL,\n"
                + "  status varchar(45) DEFAULT NULL,\n"
                + "  winner varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (id)\n"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        close();
    }

    public void generateTableSaveGame() throws SQLException {

        connect();

        myStmt.executeUpdate("CREATE TABLE savegame (\n"
                + "  id int(11) NOT NULL,\n"
                + "  user1 varchar(45) DEFAULT NULL,\n"
                + "  user2 varchar(45) DEFAULT NULL,\n"
                + "  game BLOB,\n"
                + "  PRIMARY KEY (id)\n"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

        close();
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

}
