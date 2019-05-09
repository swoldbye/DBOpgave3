package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s180943?";
    private String username = "s180943";
    private String password = "UXZTadQzbPrlIosGCZYNF";
    private Connection con;

    public Connection establishConnection() throws SQLException {
        return con = DriverManager.getConnection(url, username, password);
    }

    public Connection createConnection()throws DALException {
        try {
            if(con == null || con.isClosed()) {
                return establishConnection();
//            } else if() {
//                return establishConnection();
            } else {
                return con;
            }
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    public void killConnection() throws DALException {
        try {
            con.close();
        } catch(SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    public void toggleAutoCommit() throws DALException {
        try {
            con.setAutoCommit(!con.getAutoCommit());
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}