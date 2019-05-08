package DAL;

import DTO.IRecipeDTO;
import DTO.RecipeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO implements IRecipeDAO {

    DBConnection dbConnection = new DBConnection();

    /**
     *
     * @param recipe
     * @throws DALException
     */
    public void createRecipe(IRecipeDTO recipe) throws DALException {

        try(Connection connection = dbConnection.createConnection()){

            String query = "INSERT INTO recipe VALUES(recipe_id = ?, recipe_name = ?, reg_date = ?, storage_time = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, recipe.getRecipe_id());
            statement.setString(2, recipe.getRecipe_name());
            statement.setDate(3, recipe.getRegistration_date());
            statement.setInt(4,recipe.getStorage_time());
            statement.execute();

        }
        catch(SQLException e){
            throw new DALException(e.getMessage());
        }
    }

    /**
     *
     * @param recipe_id
     * @return
     * @throws DALException
     */
    public IRecipeDTO getRecipe(int recipe_id) throws DALException {

        try(Connection connection = dbConnection.createConnection()){

            String query = "SELECT recipe.*, ingredient_line.quantity, ingredient.ingredient_name FROM recipe" +
                    "JOIN ingredient_line ON recipe.recipe_id = ingredient_line.recipe_id" +
                    "JOIN ingredient ON ingredient.ingredient_id = ingredient_line.ingredient_id " +
                    "WHERE recipe.recipe_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,recipe_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                IRecipeDTO recipeDTO = new RecipeDTO();
                recipeDTO.setRecipe_id(resultSet.getInt(1));
                recipeDTO.setRecipe_name(resultSet.getString(2));
                recipeDTO.setRegistration_date(resultSet.getDate(3));
                recipeDTO.setStorage_time(resultSet.getInt(4));
                recipeDTO.setRecipe_name(resultSet.getString(5));
                recipeDTO.setQuantity(resultSet.getDouble(6));
                return recipeDTO;
            }
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }
        return null;
    }

    /**
     *
     * @return
     * @throws DALException
     */
    public List<IRecipeDTO> getAllRecipes() throws DALException {

        List<IRecipeDTO> recipes = new ArrayList<>();

        try(Connection connection = dbConnection.createConnection()){

            String query = "SELECT recipe.*, ingredient_line.quantity, ingredient.ingredient_name FROM recipe" +
                    "JOIN ingredient_line ON recipe.recipe_id = ingredient_line.recipe_id" +
                    "JOIN ingredient ON ingredient.ingredient_id = ingredient_line.ingredient_id " +
                    "WHERE recipe.recipe_id = ingredient_line.recipe_id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                IRecipeDTO recipeDTO = new RecipeDTO();
                recipeDTO.setRecipe_id(resultSet.getInt(1));
                recipeDTO.setRecipe_name(resultSet.getString(2));
                recipeDTO.setRegistration_date(resultSet.getDate(3));
                recipeDTO.setStorage_time(resultSet.getInt(4));
                recipeDTO.setRecipe_name(resultSet.getString(5));
                recipeDTO.setQuantity(resultSet.getDouble(6));
                recipes.add(recipeDTO);
            }
            return recipes;
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }
    }

    public void updateRecipe(int oldRecipe_id, IRecipeDTO recipe) throws DALException {

        try(Connection connection = dbConnection.createConnection()){

            connection.setAutoCommit(false);

            String query = "DELETE FROM recipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,oldRecipe_id);
            preparedStatement.executeQuery();

            connection.commit();

            String query1 = "INSERT INTO recipe VALUES(recipe_id = ?, recipe_name = ?, reg_date = ?, storage_time = ?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1,recipe.getRecipe_id());
            preparedStatement1.setString(2,recipe.getRecipe_name());
            preparedStatement1.setDate(3,recipe.getRegistration_date());
            preparedStatement1.setInt(4,recipe.getStorage_time());
            preparedStatement1.executeQuery();

            connection.commit();

        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }
    }

    /**
     *
     * @param recipe_id
     * @throws DALException
     */
    public void deleteRecipe(int recipe_id) throws DALException {

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
