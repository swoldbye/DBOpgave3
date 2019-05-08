package DAL;

import DTO.IIngridientDTO;
import DTO.IngridientDTO;
import sun.rmi.server.InactiveGroupException;

import java.sql.*;
import java.util.ArrayList;

public class IngridientDAO implements IIngridientDAO {
    DBConnection conn = new DBConnection();

    //public IngridientDAO(){}

    /*private Connection createConnection() throws SQLException {
        return  DriverManager.getConnection("jdbc:mysql://localhost:3306/root?"
                + "Firma2018");
    }*/


    public void createIngridient(IIngridientDTO ingridient) throws DALException {

        try (Connection connection = conn.createConnection()) {

            String sql = "INSERT INTO ingredient(ingredient_id,ingredient_name,needs_refill)" + "VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ingridient.getIngredient_id());
            statement.setString(2, ingridient.getIngredient_name());
            statement.setBoolean(3, ingridient.getNeeds_refill());
            statement.execute();

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }

    }

    public IIngridientDTO updateIngridient(IIngridientDTO ingridient) throws DALException {
        try (Connection connection = conn.createConnection()) {

            String sql = "UPDATE ingredient SET ingredient_name = ?, needs_refill=?  WHERE ingredient_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ingridient.getIngredient_name());
            statement.setBoolean(2, ingridient.getNeeds_refill());
            statement.setInt(3, ingridient.getIngredient_id());
            statement.execute();

        } catch (SQLException e) {
            throw new DALException(e.getMessage());

        }
        return null;
    }


    public void deleteIngridient ( int ingridient_id) throws DALException {

            try (Connection connection = conn.createConnection()) {

                String sql = "DELETE FROM ingredient WHERE ingredient_id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, ingridient_id);
                statement.executeUpdate();

            } catch (SQLException e) {
                throw new DALException(e.getMessage());
            }

        }


        public IIngridientDTO getIngredient ( int ingredient_id)throws DALException {


            try (Connection connection = conn.createConnection()) {

                //I do npt use PreparedStatement because data is selectet and not updated.

                String sql = "SELECT * FROM ingredient WHERE ingredient_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, ingredient_id);

                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    IIngridientDTO ingredient = new IngridientDTO();
                    ingredient.setNeeds_refill(result.getBoolean(3));
                    ingredient.setIngredient_name(result.getNString(2));
                    ingredient.setIngredient_id(result.getInt(1));

                    return ingredient;
                }

            } catch (SQLException e) {
                e.getSQLState().toString();
                throw new DALException("hej");
            }
            return null;
        }




  public ArrayList<IIngridientDTO> getIngredientList() throws DALException{


        ArrayList<IIngridientDTO> ingredintList = new ArrayList<>();
        String sql ="SELECT * FROM ingredient";
        try (Connection connection = conn.createConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();


            while(result.next()) {
                IIngridientDTO  ingredient = new IngridientDTO(result.getInt(1),result.getString(2),result.getBoolean(3));
                ingredintList.add(ingredient);
            }
            return ingredintList;

        } catch (SQLException e) {

            throw new DALException("hej");
            //Remember to handle Exceptions gracefully! Connection might be Lost....
        }

    }


        public static void main (String[]args) throws DALException {
            IIngridientDTO ingridien = new IngridientDTO(100, "dsadsadasdsfdsfsdf", true);
            IIngridientDTO ingridien2 = new IngridientDTO(100, "dsadsadasdsfdsfsdf", false);


/*
            one.createIngridient(ingridien);
            one.deleteIngridient(1);*/
            IngridientDAO one = new IngridientDAO();

            //System.out.println(one.getIngredient(2));

            //System.out.println(one.getIngredient(1).toString());
            one.createIngridient(ingridien);
            System.out.println(one.getIngredient(100).toString());
            one.updateIngridient(ingridien2);
            System.out.println(one.getIngredient(100).toString());

            /*ArrayList<IIngridientDTO> list = one.getIngredientList();

            for(int i = 0; i< list.size();i++){
                System.out.println(list.get(i).toString());
            }*/




        }


}