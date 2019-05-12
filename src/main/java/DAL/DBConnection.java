package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s180943?";
    private String username = "s180943";
    private String password = "UXZTadQzbPrlIosGCZYNF";
    private Connection con;

    /**
     * Establishes connection to database
     *
     * @return  Connection
     * @throws  SQLException
     */
    public Connection establishConnection() throws SQLException {
        return con = DriverManager.getConnection(url, username, password);
    }

    /**
     * Singleton to create connection to database if none already exists
     *
     * @return  Connection to database
     * @throws  DALException
     */
    public Connection createConnection()throws DALException {
        try {
            if(con == null || con.isClosed()) {
                return establishConnection();
            } else {
                return con;
            }
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    /**
     * Kills the connection
     *
     * @throws DALException
     */
    public void killConnection() throws DALException {
        try {
            con.close();
        } catch(SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    /**
     * For throwing DALException for Connection.setAutoCommit(boolean) method. Avoids akward try-catch nesting
     *
     * @param setting   True if auto commit on, else false
     * @throws          DALException
     */
    public void toggleAutoCommit(boolean setting) throws DALException {
        try {
            con.setAutoCommit(setting);
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}