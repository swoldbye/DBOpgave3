package DAL;

import DTO.IIngridientDTO;
import DTO.IngridientDTO;

import java.sql.*;

public class IngridientDAO implements IIngridientDAO {
    DBConnection conn = new DBConnection();

    //public IngridientDAO(){}

   /* private Connection createConnection() throws SQLException {
        return  DriverManager.establishConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s180943?"
                + "UXZTadQzbPrlIosGCZYNF");
    }*/


    public void createIngridient(IIngridientDTO ingridient) throws DALException {

        try (Connection connection = conn.createConnection()) {

            String sql = "INSERT INTO ingredient(ingredient_id,ingredient_name,needs_refill)" + "VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ingridient.getIngredient_id());
            statement.setString(2, ingridient.getIngredient_name());
            statement.setBoolean(3, ingridient.getNeeds_refill());
            statement.executeQuery();

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }

    }

    public IIngridientDTO updateIngridient(IIngridientDTO ingridient) throws DALException {
        try (Connection connection = conn.createConnection()) {

            String sql = "UPDATE ingredient SET ingridient_name = ?, needs_refill=?  WHERE ingredient_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ingridient.getIngredient_name());
            statement.setBoolean(2, ingridient.getNeeds_refill());
            statement.setInt(3, ingridient.getIngredient_id());
            statement.executeQuery();

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

                String sql = "SELECT * FROM ingredient WHERE ingedient_id = ?";
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
                throw new DALException("hej");
            }
            return null;
        }




  /*  public ArrayList<IIngridientDTO> getIngredientList(){
        IIngridientDTO ingredient;

        ArrayList<IIngridientDTO> ingredintList = new ArrayList<>();
        String sql ="SELECT * FROM ingredient";
        try (Connection connection = createConnection()) {

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);


            while(result.next()) {
                ingredient = getIngredient(result.getInt(1));

            }
            return ingredintList;

        } catch (SQLException e) {
            //Remember to handle Exceptions gracefully! Connection might be Lost....
            e.printStackTrace();

        }


    }
*/

        public static void main (String[]args) throws DALException {
            IIngridientDTO ingridien = new IngridientDTO(1, "dsadsadasdsfdsfsdf", true);

            IngridientDAO one = new IngridientDAO();

            one.deleteIngridient(1);
            one.createIngridient(ingridien);

            ingridien.setIngredient_name("AAAAAA");
            ingridien.setNeeds_refill(false);
            one.updateIngridient(ingridien);

            one.getIngredient(1);

            System.out.println(one.getIngredient(1).getNeeds_refill());


        }


}