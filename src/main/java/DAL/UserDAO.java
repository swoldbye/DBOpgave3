package DAL;

import DTO.IUserDTO;
import DTO.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    final String dbUrl = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s180943?";
    final String dbUsername = "s180943";
    final String dbPassword = "UXZTadQzbPrlIosGCZYNF";

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    /**
     * Creates a user in 'users' with foreign key to 'user_role'. However needs roles to exist
     * in 'roles'.
     * @param user
     * @throws DALException
     */
    public void createUser(IUserDTO user) throws DALException {

        try (Connection conn = createConnection()) {

            String query3 = "INSERT INTO users VALUES(?,?,?,?)";
            PreparedStatement preparedStatement3 = conn.prepareStatement(query3);
            preparedStatement3.setInt(1, user.getUserId());
            preparedStatement3.setString(2, user.getUserName());
            preparedStatement3.setString(3, user.getIni());
            preparedStatement3.setString(4, user.getCpr());
            preparedStatement3.execute();


            for (String role : user.getRoles()) {

                String query2 = "INSERT INTO user_role (user_id, role_id) " +
                        "VALUES(?,(SELECT role_id FROM roles WHERE role_name = ?))";
                PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
                preparedStmt2.setInt(1, user.getUserId());
                preparedStmt2.setString(2, role);
                preparedStmt2.executeUpdate();

            }

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }

    }

    /**
     * Returns a UserDTO holding a user from the database. Needs the user_id to exist in
     * 'users' and 'user_id'.
     * @param userId
     * @return
     * @throws DALException
     */
    public IUserDTO getUser(int userId) throws DALException {
        try (Connection conn = createConnection()) {


            Statement stat = conn.createStatement();
            String query = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet set = preparedStatement.executeQuery();
            set.next();

            String query2 = "SELECT role_name FROM roles WHERE " +
                    "role_id IN (SELECT role_id FROM user_role WHERE user_id = ?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(query2);
            preparedStatement1.setInt(1, userId);
            ResultSet set2 = preparedStatement1.executeQuery();

            ArrayList<String> list = new ArrayList<>();
            while (set2.next()) {
                list.add(set2.getString(1));
            }


            IUserDTO returnUser = new UserDTO(set.getInt(1), set.getString(2), set.getString(3),
                    list, set.getString(4));

            return returnUser;

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }

    }

    /**
     * Returns an list of UserDTOs.
     * @return
     * @throws DALException
     */
    public List<IUserDTO> getUserList() throws DALException {


        try (Connection conn = createConnection()) {

            Statement stat = conn.createStatement();
            ResultSet rSet = stat.executeQuery("SELECT user_id FROM users");
            List<IUserDTO> users = new ArrayList<>();


            while (rSet.next()) {
                IUserDTO user = getUser(rSet.getInt(1));
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    /**
     * Takes a user, finds it in the database, and updates/deletes where necessary
     * for the new user in the database to be idendentical to the param user.
     * @param user
     * @throws DALException
     */
    public void updateUser(IUserDTO user) throws DALException {
        try (Connection conn = createConnection()) {

            //update users table
            String query = "UPDATE users " +
                    "SET user_name = ?, initials = ?, cpr = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getIni());
            preparedStatement.setString(3, user.getCpr());
            preparedStatement.setInt(4, user.getUserId());
            preparedStatement.execute();

            //query2 will find all the rolls associated with the user_id.
            //Thereafter the UserDTO's String-Array will be compared to the returned resultSet.
            String query2 = "SELECT role_name, role_id FROM roles WHERE " +
                    "role_id IN (SELECT role_id FROM user_role WHERE user_id = ?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(query2);
            preparedStatement1.setInt(1, user.getUserId());
            ResultSet resultSet = preparedStatement1.executeQuery();

            //'copy' will allow me to see which roles are in the database. If any role does not become "-1" after
            //the while-loop it should be added to the database.
            List<String> copy = new ArrayList<>();
            for(String s: user.getRoles()){
                copy.add(s);
            }


            while (resultSet.next()) {
                boolean contains = contains(resultSet.getString(1), user.getRoles());

                for (int i = 0; i < copy.size(); i++) {
                    if (copy.get(i).equals(resultSet.getString(1))) {
                        copy.set(i, "-1");
                    }
                }
                //if contains returns false, it means that there is a roll in the database which does
                //not exist in the arrayList... and therefore needs to be removed.
                if (contains == false) {
                    String query3 = "DELETE FROM user_role WHERE user_id = ? AND role_id = ?";
                    PreparedStatement preparedStatement2 = conn.prepareStatement(query3);
                    preparedStatement2.setInt(1, user.getUserId());
                    preparedStatement2.setInt(2, resultSet.getInt(2));
                    preparedStatement2.execute();
                }
            }
            //all Strings != "-1" are now added into the database.
            for (String s: copy) {
                if (!s.equals("-1")) {
                    String query3 = "INSERT INTO user_role (user_id, role_id) " +
                            " VALUES(?,(SELECT role_id FROM roles WHERE role_name = ?))";
                    PreparedStatement preparedStatement3 = conn.prepareStatement(query3);
                    preparedStatement3.setInt(1, user.getUserId());
                    preparedStatement3.setString(2, s);
                    preparedStatement3.execute();

                }
            }

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    /**
     * Removes user from everywhere in database: 'user' and 'user_role'.
     * @param userId
     * @throws DALException
     */
    public void deleteUser(int userId) throws DALException {

        try (Connection c = createConnection()) {

            String query2 = "DELETE FROM user_role WHERE user_id = ?";
            PreparedStatement preparedStatement = c.prepareStatement(query2);
            preparedStatement.setInt(1, userId);
            preparedStatement.execute();


            String query = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement preparedStmt2 = c.prepareStatement(query);
            preparedStmt2.setInt(1, userId);
            preparedStmt2.execute();


        } catch (SQLException e) {
            //Remember to handle Exceptions gracefully! Connection might be Lost....
            throw new DALException(e.getMessage());
        }
    }

    /**
     * helper function to conclude if s is in the string-Array 'array'
     * @param s
     * @param array
     * @return
     */
    public boolean contains(String s, List<String> array) {
        for (String e : array) {
            if (e.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * creates a role for 'roles' table using given name and id.
     * @param roll
     * @param id
     * @throws DALException
     */
    public void createRoll(String roll, int id) throws DALException {
        try (Connection c = createConnection()) {

            String query = "INSERT INTO roles VALUES(?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, roll);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    /**
     * removes role from everywhere necessary in database: 'user_role' and 'roles'.
      * @param roll
     * @throws DALException
     */
    public void deleteRoll(String roll) throws DALException {

        try (Connection c = createConnection()) {

            String query2 = "DELETE FROM user_role WHERE role_id IN (SELECT role_id FROM roles WHERE role_name = ?)";
            PreparedStatement preparedStatement1 = c.prepareStatement(query2);
            preparedStatement1.setString(1, roll);
            preparedStatement1.execute();

            String query = "DELETE FROM roles WHERE role_name = ?";
            PreparedStatement preparedStatement = c.prepareStatement(query);
            preparedStatement.setString(1, roll);
            preparedStatement.execute();



        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    /**
     * Returns a list of all roles currenctly in the 'roles' table.
     * @return
     * @throws DALException
     */
    public List<String> getRollList() throws DALException {
        try (Connection c = createConnection()) {

            String query = "SELECT role_name FROM roles";
            Statement stat = c.createStatement();

            ResultSet rSet = stat.executeQuery(query);
            List<String> roleList = new ArrayList<String>();

            while (rSet.next()) {
                String role = rSet.getString(1);
                roleList.add(role);
            }
            return roleList;


        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    /**
     *Takes the name of current role in 'roles' table (oldRole) , and renames it (newRole).
     * Also replaces all occurrences of oldRole in 'user_role' table with newRole.
     * @param oldRole
     * @param newRole
     * @throws DALException
     */
    public void updateRoll(String oldRole, String newRole) throws DALException {
        try (Connection c = createConnection()) {
            String query = "UPDATE roles SET role_name = ? WHERE role_name = ?";
            PreparedStatement preparedStatement = c.prepareStatement(query);
            preparedStatement.setString(1, newRole);
            preparedStatement.setString(2, oldRole);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

}
