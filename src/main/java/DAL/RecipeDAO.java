package DAL;

import DTO.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO implements IRecipeDAO {

    DBConnection dbConnection = new DBConnection();


    /**Metoden opretter en recipe i tabellen Recipe. Opskriftens ingridienser oprettes i Ingredient_line tabellen.
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

            //Ingredient_line tabellen fyldes ud.
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
     * Metoden henter en enkelt recipe.
     * @param recipe_id
     * @return IRecipeDTO objekt.
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

            // i Resultsætter kan godt indeholde flere rækker, men det er kun ingredient_name og quantity der ændrer sin.
            // Derfor anvender vi kun den første række til at difinere recipe_id, recipe_name, registration_date og storage_time.
            resultSet.next();
            IRecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setRecipe_id(resultSet.getInt(1));
            recipeDTO.setRecipe_name(resultSet.getString(2));
            recipeDTO.setRegistration_date(resultSet.getDate(3));
            recipeDTO.setStorage_time(resultSet.getInt(4));

            //Denne indføres for at vi får den første række med fra ResultSet. Når vi anvender resultSet.next() rykker den ned på næste linje.
            IIngredient_lineDTO ingredientLine2 = new Ingredient_lineDTO(resultSet.getDouble(5),resultSet.getString(6));
            recipeDTO.addIngredient_line(ingredientLine2);

            //Her indhentes de næste ingridienser til opskriften. (fra række to og ned)
            while (resultSet.next()){
                IIngredient_lineDTO ingredientLine = new Ingredient_lineDTO(resultSet.getDouble(5),resultSet.getString(6));
                recipeDTO.addIngredient_line(ingredientLine);

            }return recipeDTO;
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }

    }

    /**
     * Metoden henter alle recipes i databasen.
     * Denne metode benytter også af metoden getRecipe().
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

            //Her findes det, hvor mange opskrifter der findes i tabellen ingredient_line.
            while (resultSet.next()) {
                recipeId.add(resultSet.getInt(1));
            }
            //Her hentes de opskrifter der findes i tabellen Ingredeint_line.
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

    /**
     * Metoden benytter sig af metoder deleteRecipe og createRecipe.
     * @param oldRecipe_id
     * @param recipe
     * @throws DALException
     */

    public void updateRecipe(int oldRecipe_id, IRecipeDTO recipe) throws DALException {
        Connection connection = null;
        try{
            connection = dbConnection.createConnection();
            connection.setAutoCommit(false);

            deleteRecipe(oldRecipe_id);
            createRecipe(recipe);

            connection.commit();

        }
        catch (SQLException e){
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DALException(ex.getMessage());
                }
            }
            throw new DALException(e.getMessage());
        }
        finally {
            try { connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DALException(e.getMessage());
            }
        }
    }

    /**Metoden sletter en opskrift, ud fra dens id i tabellerne, recipe, Ingredient_line og Ingredient_recipe.
     *
     * @param recipe_id
     * @throws DALException
     */
    public void deleteRecipe(int recipe_id) throws DALException {
        Connection connection = null;
        try {
            connection = dbConnection.createConnection();
            connection.setAutoCommit(false);

            String queryIngredient_line = "DELETE FROM ingredient_line WHERE recipe_id = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(queryIngredient_line);
            preparedStatement2.setInt(1,recipe_id);
            preparedStatement2.executeUpdate();

            String queryProduct_racipe = "DELETE FROM product_recipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement3 = connection.prepareStatement(queryProduct_racipe);
            preparedStatement3.setInt(1,recipe_id);
            preparedStatement3.executeUpdate();

            String query = "DELETE FROM recipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,recipe_id);
            preparedStatement.executeUpdate();


            connection.commit();
        }
        catch (SQLException e){
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DALException(ex.getMessage());
                }
            }
            throw new DALException(e.getMessage());
        }
        finally {
            try { connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DALException(e.getMessage());
            }
        }
    }

    //_______________________________________________________________________________________________________

    //                          ET PAR ANVENDELIGE HJÆLPEMETODER

    //________________________________________________________________________________________________________

    /**
     * Metode ser om der findes nogen tupler i tabellen Ingredient_line, med et recipe_id som parameter.
     * Retunerer denne metode true betyder det at den er tom.
     * Retunerer den false betyder det at der er noget i den.
     * @param recipe_id
     * @return Retunerer denne metode true betyder det at den er tom. Retunerer den false betyder det at der er noget i den.
     * @throws DALException
     */
    public boolean controleIngredientLine(int recipe_id) throws DALException{

        try(Connection connection = dbConnection.createConnection()) {


            String query1 = "SELECT * FROM ingredient_line WHERE recipe_id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, recipe_id);
            preparedStatement1.executeQuery();

            ResultSet result = preparedStatement1.executeQuery();

            int count = 0;

                while(result.next()){
                    count++;
                }

                if (count==0) { return true;
                }
                return false;
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }
    }

    /**
     * Metode ser om der findes nogen tupler i tabellen shadowRecipe, med et recipe_id som parameter.
     * Retunerer denne metode true betyder det at den er tom.
     * Retunerer den false betyder det at der er noget i den.
     * @param recipe_id
     * @return Retunerer denne metode true betyder det at den er tom. Retunerer den false betyder det at der er noget i den.
     * @throws DALException
     */
    public boolean controleshadowRecipe(int recipe_id) throws DALException{

        try(Connection connection = dbConnection.createConnection()) {


            String query1 = "SELECT * FROM shadowRecipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, recipe_id);
            preparedStatement1.executeQuery();

            ResultSet result = preparedStatement1.executeQuery();

            int count = 0;

            while(result.next()){
                count++;
            }

            if (count==0) { return true;
            }
            return false;
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }
    }
    /**
     * Metode ser om der findes nogen tupler i tabellen shadowIngredient_line, med et recipe_id som parameter.
     * Retunerer denne metode true betyder det at den er tom.
     * Retunerer den false betyder det at der er noget i den.
     * @param recipe_id
     * @return Retunerer denne metode true betyder det at den er tom. Retunerer den false betyder det at der er noget i den.
     * @throws DALException
     */

    public boolean controleshadowIngredient_line(int recipe_id) throws DALException{

        try(Connection connection = dbConnection.createConnection()) {


            String query1 = "SELECT * FROM shadowIngredient_line WHERE recipe_id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, recipe_id);
            preparedStatement1.executeQuery();

            ResultSet result = preparedStatement1.executeQuery();

            int count = 0;

            while(result.next()){
                count++;
            }

            if (count==0) { return true;
            }
            return false;
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }
    }

    /**
     * Metode ser om der findes nogen tupler i tabellen product_Recipe, med et recipe_id som parameter.
     * Retunerer denne metode true betyder det at den er tom.
     * Retunerer den false betyder det at der er noget i den.
     * @param recipe_id
     * @return Retunerer denne metode true betyder det at den er tom. Retunerer den false betyder det at der er noget i den.
     * @throws DALException
     */

    public boolean controleProduct_recipe(int recipe_id) throws DALException{

        try(Connection connection = dbConnection.createConnection()) {


            String query1 = "SELECT * FROM product_recipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, recipe_id);
            preparedStatement1.executeQuery();

            ResultSet result = preparedStatement1.executeQuery();

            int count = 0;

            while(result.next()){
                count++;
            }

            if (count==0) { return true;
            }
            return false;
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }
    }

}
