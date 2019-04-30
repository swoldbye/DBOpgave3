package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s180943?";
    private String username = "s180943";
    private String password = "UXZTadQzbPrlIosGCZYNF";

    public Connection createConnection()throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

}
