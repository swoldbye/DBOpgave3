package DAL;

import DTO.IIngridientDTO;
import DTO.IngridientDTO;

import java.sql.*;
import java.util.ArrayList;



public class IngridientDAO implements IIngridientDAO {
    DBConnection conn = new DBConnection();


    /**
     * Inserts an object into the table.
     * @param ingridient
     * @throws DALException
     */

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

    /**
     * Method updates an object by updating the row whitch is defined by the ingredient_id.
     * @param ingridientDTO
     * @return
     * @throws DALException
     */

        public IIngridientDTO updateIngridient(IIngridientDTO ingridientDTO) throws DALException {
            try (Connection connection = conn.createConnection()) {

                String sql = "UPDATE ingredient SET ingredient_name = ?, needs_refill=?  WHERE ingredient_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, ingridientDTO.getIngredient_name());
                statement.setBoolean(2, ingridientDTO.getNeeds_refill());
                statement.setInt(3, ingridientDTO.getIngredient_id());
                statement.execute();

            } catch (SQLException e) {
                throw new DALException(e.getMessage());
            }
            return null;
        }

    /**
     * This method will not be used, because we do not want to delete any ingrediens permanent from the database.
     * We want to be able to see all the ingredients that have been used in old as well as new recipes.
     * @param ingridient_id
     * @throws DALException
     */

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

    /**
     * Method returns a object of IngredientDTO.
     * @param ingredient_id
     * @return IIngridientDTO
     * @throws DALException
     */

        public IIngridientDTO getIngredient ( int ingredient_id)throws DALException {

            try (Connection connection = conn.createConnection()) {

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


    /**
     *Method gets a list of all the ingredients that is used by any recipes.
     * @return A list that contains alle ingredients (id, names, and needs refill attribut)
     * @throws DALException
     */

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
                }
            }
}