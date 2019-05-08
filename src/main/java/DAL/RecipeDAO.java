package DAL;

import DTO.IIngredient_lineDTO;
import DTO.IRecipeDTO;
import DTO.Ingredient_lineDTO;
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

            String query = "INSERT INTO recipe VALUES(?, ?, DATE ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, recipe.getRecipe_id());
            statement.setString(2, recipe.getRecipe_name());
            statement.setDate(3, recipe.getRegistration_date());
            statement.setInt(4,recipe.getStorage_time());
            statement.executeUpdate();

            for (IIngredient_lineDTO line: recipe.getIngredient_line()){
                String query1 = "INSERT INTO ingredient_line VALUES(?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query1);
                preparedStatement.setInt(1,recipe.getRecipe_id());
                preparedStatement.setInt(2,line.getIngredient_id());
                preparedStatement.setDouble(3,line.getQuantity());
                preparedStatement.executeUpdate();
            }
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

            String query = "SELECT recipe.*, ingredient_line.quantity, ingredient.ingredient_name FROM recipe " +
                    "JOIN ingredient_line ON recipe.recipe_id = ingredient_line.recipe_id " +
                    "JOIN ingredient ON ingredient.ingredient_id = ingredient_line.ingredient_id " +
                    "WHERE recipe.recipe_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,recipe_id);
            ResultSet resultSet = preparedStatement.executeQuery();



            IRecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setRecipe_id(resultSet.getInt(1));
            recipeDTO.setRecipe_name(resultSet.getString(2));
            recipeDTO.setRegistration_date(resultSet.getDate(3));
            recipeDTO.setStorage_time(resultSet.getInt(4));

            while (resultSet.next()){
//                IIngredient_lineDTO ingridientLine = new Ingredient_lineDTO();
//                ingridientLine.setIngredient_name(resultSet.getString(6));
//                recipeDTO.getIngredient_line()
                IIngredient_lineDTO ingredientLine = new Ingredient_lineDTO(resultSet.getDouble(5),
                        resultSet.getString(6));
                recipeDTO.addIngredient_line(ingredientLine);
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
        List<Integer> recipeId = new ArrayList<>();

        try(Connection connection = dbConnection.createConnection()){

            String query = "SELECT DISTINCT recipe.recipe_id FROM recipe " +
                    "JOIN ingredient_line ON recipe.recipe_id = ingredient_line.recipe_id " +
                    "JOIN ingredient ON ingredient.ingredient_id = ingredient_line.ingredient_id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            int count =0;
            while (resultSet.next()) {
                recipeId.add(resultSet.getInt(1));
                /*
                IRecipeDTO recipeDTO = new RecipeDTO();
                recipeDTO.setRecipe_id(resultSet.getInt(1));
                recipeDTO.setRecipe_name(resultSet.getString(2));
                recipeDTO.setRegistration_date(resultSet.getDate(3));
                recipeDTO.setStorage_time(resultSet.getInt(4));*/

                /*while (resultSet.next()){
                    IIngredient_lineDTO ingredientLine = new Ingredient_lineDTO(resultSet.getInt(5),
                            resultSet.getString(6));
                    recipeDTO.addIngredient_line(ingredientLine);
                    recipes.add(recipeDTO);
                }*/
            }
            for(int i = 0; i< recipeId.size(); i++){

                IRecipeDTO one = getRecipe(recipeId.get(i));
                recipes.add(one);
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

            connection.setAutoCommit(true);

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


    public static void main(String[] args) throws DALException {
        RecipeDAO hal = new RecipeDAO();
        IRecipeDTO one = hal.getRecipe(2);



//        List<IRecipeDTO> liste = hal.getAllRecipes();
//
//        for(int i = 0; i<liste.size();i++){
//            liste.get(i).toString();
//        }



    }
}
