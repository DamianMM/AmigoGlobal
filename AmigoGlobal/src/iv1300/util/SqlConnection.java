package iv1300.util;

/**
 * Created by arvid on 2016-09-21.
 */

import java.sql.*;

public class SqlConnection {

    private Connection con;
    private static SqlConnection sqlCon = null;

    private SqlConnection() {

    }

    public static SqlConnection getInstance() {
        if (sqlCon == null) {
            sqlCon = new SqlConnection();
        }
        return sqlCon;
    }


    // Method for establishing a DB connection
    public void connect() {
        // Local Access DB static variables
        String URL = "jdbc:mysql:///iv1300";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "password";
        try {
            // Register the driver with DriverManager
            Class.forName(driver);
            // Create a connection to the database
            con = DriverManager.getConnection(URL, user, password);
            // Set the auto commit of the connection to false.
            // An explicit commit will be required in order to accept
            // any changes done to the DB through this connection.
            con.setAutoCommit(false);
            // Some logging
            System.out.println("Connected to: " + URL + ", trhough: "+ driver + "\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getCon(){
        return this.con;
    }
}
