package beautyPackage;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/beautyparlor";

    static final String USERNAME = "root";
    static final String PASSWORD = "root";

    private Connection connection;

    public DatabaseConnector() {
        try {

            connection = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Failed");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed");
        }
    }
}
