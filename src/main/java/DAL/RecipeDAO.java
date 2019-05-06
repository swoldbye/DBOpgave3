package DAL;

import DTO.IRecipeDTO;
import DTO.RecipeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RecipeDAO implements IRecipeDAO {
    /**
     *
     * @param recipe
     * @throws DALException
     */
    public void createRecipe(RecipeDTO recipe) throws DALException {
        DBConnection dbConnection = new DBConnection();

        try(Connection connection = dbConnection.createConnection()){

            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe VALUES(recipe_id=?, recipe_name=?, quantity=?, reg_date=?, storage_time=?)");

            statement.setInt(1, recipe.getRecipe_id());
            statement.setString(2, recipe.getRecipe_name());
            statement.setInt(3, recipe.getQuantity());
            statement.setDate(4, recipe.getRegistration_date());
            statement.setInt(5,recipe.getStorage_time());

            statement.execute();

        }
        catch(SQLException e){
            throw new DALException(e.getMessage());
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws DALException
     */
    public RecipeDTO getRecipe(int id) throws DALException {
        DBConnection dbConnection = new DBConnection();


        return null;
    }

    /**
     *
     * @return
     * @throws DALException
     */
    public List<RecipeDTO> getAllRecipes() throws DALException {
        DBConnection dbConnection = new DBConnection();


        return null;
    }

    public void updateCommodity(IRecipeDTO recipe){

    }

    /**
     *
     * @param recipe_id
     * @throws DALException
     */
    public void deleteRecipe(int recipe_id) throws DALException {
        DBConnection dbConnection = new DBConnection();

        try(Connection connection = dbConnection.createConnection()) {

            String query = "DELETE FROM recipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,recipe_id);
            preparedStatement.executeUpdate();

        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }



    }

}
