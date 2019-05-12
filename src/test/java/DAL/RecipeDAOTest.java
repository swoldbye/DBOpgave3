package DAL;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import DAL.DALException;
import DAL.DBConnection;
import DAL.IRecipeDAO;
import DAL.RecipeDAO;
import DTO.IIngredient_lineDTO;
import DTO.IRecipeDTO;
import DTO.Ingredient_lineDTO;
import DTO.RecipeDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAOTest{

    IRecipeDAO iRecipeDAO = new RecipeDAO();


    /**
     * Test tester metoderne for createRecipe og getRecipe.
     * Metoden virket ved:
     * 1. Der oprettes og indsættes en opskrift i databasen.
     * 2. det laves en getRecipe
     * 3. Det sammen lignes om indeholdet i de to recipes er det samme.
     *
     * Hvis det er det betyder det at tabellen ingredient_line også bliver opdateret, da disse også indgår i indeholdet på opskriften.
     * @throws DALException
     */


    @org.junit.Test
    public void createRecipe() throws DALException{
        clearTest(103);
        try {
            Date date = new Date(22,5,2018);
            //iRecipeDAO.deleteRecipe(100);
            IRecipeDTO newRecipe = new RecipeDTO(103,"DenNyeOpskrift",date,80);
            IIngredient_lineDTO ingredient1 = new Ingredient_lineDTO(1,10,"Sildenafil");
            IIngredient_lineDTO ingredient2 = new Ingredient_lineDTO(4,10,"Magnesiumstearat");

            newRecipe.addIngredient_line(ingredient1);
            newRecipe.addIngredient_line(ingredient2);

            iRecipeDAO.createRecipe(newRecipe);
            IRecipeDTO recipeFromDatabase = iRecipeDAO.getRecipe(103);

            assertEquals(newRecipe.toString(),recipeFromDatabase.toString());

            //Her testes det om ingredient_line tabellen bliver udfyldt. (False betyder at der er noget i tabellen, med det angivne id.
            boolean Expected_isIngredient_lineEmpty = false;
            boolean Actual__isIngredient_lineEmpty = iRecipeDAO.controleIngredientLine(103);
            assertEquals(Expected_isIngredient_lineEmpty,Actual__isIngredient_lineEmpty);
        } finally {
            clearTest(103);
        }
    }


    /**
     *Test if one can delete a recipe.
     *Fordi at der eksisterer en constraint fra ingrediense_line er det ikke muligt at slette fra recipe før at der oså er slettet fra ingredient_line.
     * Dermed vil alle records også være slettet fra ingredient_line.
     *
     * Metoden virker ved at:
     * 1. Oprette og gemme en opskrift i databasen.
     * 2. Slette samme opskrift
     * 3. Hente alle Opskrifter ud og se om id'et på den opskrift vi opretter passer med et i listen.
     * Hvis at id'et ikke findes et opskriften slettet.
     * @throws DALException
     */

    @org.junit.Test
    public void deleteRecipe() throws DALException{
        try {
            iRecipeDAO.deleteRecipe(100);
            Date date = new Date(22,5,2018);
            IRecipeDTO newRecipe = new RecipeDTO(100,"DenNyeOpskrift",date,80);
            IIngredient_lineDTO ingredient1 = new Ingredient_lineDTO(1,10,"Sildenafil");

            newRecipe.addIngredient_line(ingredient1);

            iRecipeDAO.createRecipe(newRecipe);
            iRecipeDAO.deleteRecipe(100);

            //Her testes det om alle tabellerne bliver opdateret.
            boolean Expected_isIngredient_lineEmpty = true;
            boolean Actual__isIngredient_lineEmpty = iRecipeDAO.controleIngredientLine(100);

            boolean Expected_isProduct_recipeEmpty = true;
            boolean Actual__isProduct_recipeEmpty = iRecipeDAO.controleProduct_recipe(100);

            boolean Expected_isShadowRecipeeFull = false;
            boolean Actual__isShadowRecipeFull = iRecipeDAO.controleshadowRecipe(100);

            boolean Expected_isShadowIngredient_lineFull = false;
            boolean Actual__isShadowIngredient_lineFull = iRecipeDAO.controleshadowIngredient_line(100);

            assertEquals(Expected_isIngredient_lineEmpty,Actual__isIngredient_lineEmpty);
            assertEquals(Expected_isProduct_recipeEmpty,Actual__isProduct_recipeEmpty);
            assertEquals(Expected_isShadowRecipeeFull,Actual__isShadowRecipeFull);
            assertEquals(Expected_isShadowIngredient_lineFull,Actual__isShadowIngredient_lineFull);


            List<IRecipeDTO> AllRecipes = iRecipeDAO.getAllRecipes();
            boolean found = false;
            for (IRecipeDTO recipe: AllRecipes){
                if (recipe.getRecipe_id() == newRecipe.getRecipe_id()){
                    found = true;
                }
            }
            if (found){
                fail();
            }
        } finally {
            clearTest(100);
        }
    }

    /**
     * Tester metode om den haenter alle opskrifter ud dra databasen. Det testes om antallet af de opskrifter der hentes ud fra databasen
     * stemmer over ens med det faktiske antal.
     * Det faktiske antal er talt manuelt.
     * Bemærk at der kan være indsat flere recipes recipes fra da denne tast blev lavet. Dermed kan expectedRecipes2 og expectedRecipes2 godt ændre sig.
     *
     * NB: En recipe medtages kun, hvis at den både optræder i ingredient_line og i recipe. Dette skyldes at en pskrift skal have ingridienser.
     *
     *Metoden getAllRecipe() benytter også getRecipe(). Denne metode bliver testet i testen for createRecipe.
     *
     *
     * Bemærk at der kan være indsat flere recipes recipes fra da denne tast blev lavet. Dermed kan expectedRecipes2 og expectedRecipes2 godt ændre sig.
     * @throws DALException
     */

    @org.junit.Test
    public void getAllRecipe() throws DALException{
        try {
            clearTest(300);

            List<IRecipeDTO> recipeList;

            recipeList = iRecipeDAO.getAllRecipes();


        /*Det faktiske antal af række i tabellen.
        Bemærk at der kan være indsat flere recipes recipes fra da denne tast blev lavet.
         Dermed kan expectedRecipes2 og expectedRecipes2 godt ændre sig.*/
            int expectedRecipes = 2 ;
            int actualNumberOfRecipes = recipeList.size();


            assertEquals(expectedRecipes,actualNumberOfRecipes);

            // Lægger vi en opskrift ned i databasen så der findes en opskrift mere.
            //Vi henter en ny liste med getRecipe og ser antallet passer.
            Date date = new Date(22,5,2018);
            IRecipeDTO newRecipe = new RecipeDTO(300,"DenNyeOpskrift",date,80);
            IIngredient_lineDTO ingredient1 = new Ingredient_lineDTO(1,10,"Sildenafil");
            newRecipe.addIngredient_line(ingredient1);

            iRecipeDAO.createRecipe(newRecipe);
            recipeList = iRecipeDAO.getAllRecipes();

            int expectedRecipes2 = 3;
            actualNumberOfRecipes = recipeList.size();

            assertEquals(expectedRecipes2,actualNumberOfRecipes);
        } finally {
            clearTest(300);
        }
    }

    /**
     * Terser at man kan updatere en recipe.
     * @throws DALException
     */


    @org.junit.Test
    public void updateRecipe() throws DALException{
        try {
            clearTest(200);
            clearTest(201);
            Date date = new Date(22,5,2018);
            IRecipeDTO oldRecipe = new RecipeDTO(200,"Opskrift",date,80);
            IIngredient_lineDTO ingredient1 = new Ingredient_lineDTO(1,10,"Sildenafil");
            oldRecipe.addIngredient_line(ingredient1);

            IRecipeDTO newRecipe = new RecipeDTO(201,"EnEndnuNyererOpskrift",date,80);
            IIngredient_lineDTO ingredient = new Ingredient_lineDTO(1,10,"Sildenafil");
            newRecipe.addIngredient_line(ingredient);

            iRecipeDAO.createRecipe(oldRecipe);
            iRecipeDAO.updateRecipe(oldRecipe.getRecipe_id(),newRecipe);

            boolean Expected_isIngredient_lineEmpty = true;
            boolean Actual__isIngredient_lineEmpty = iRecipeDAO.controleIngredientLine(200);

            boolean Expected_isProduct_recipeFull = false;
            boolean Actual__isProduct_recipeFull = iRecipeDAO.controleIngredientLine(201);

            assertEquals(Expected_isIngredient_lineEmpty,Actual__isIngredient_lineEmpty);
            assertEquals(Expected_isProduct_recipeFull,Actual__isProduct_recipeFull);
        } finally {
            clearTest(200);
            clearTest(201);
        }
    }


    public void clearTest(int recipe_id) throws DALException {
        DBConnection dbConnection = new DBConnection();
        Connection connection = null;
        try {
            connection = dbConnection.createConnection();
            //connection.setAutoCommit(false);

            String queryShadowRecipe = "DELETE FROM shadowRecipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryShadowRecipe);
            preparedStatement.setInt(1, recipe_id);
            preparedStatement.executeUpdate();

            String queryShadowIngredient_line = "DELETE FROM shadowIngredient_line WHERE recipe_id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(queryShadowIngredient_line);
            preparedStatement1.setInt(1, recipe_id);
            preparedStatement1.executeUpdate();

            String queryRecipe = "DELETE FROM recipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(queryRecipe);
            preparedStatement2.setInt(1, recipe_id);
            preparedStatement2.executeUpdate();

            String queryIngredient_line = "DELETE FROM ingredient_line WHERE recipe_id = ?";
            PreparedStatement preparedStatement3 = connection.prepareStatement(queryIngredient_line);
            preparedStatement3.setInt(1, recipe_id);
            preparedStatement3.executeUpdate();

            String queryShadowIngredientLine = "DELETE FROM shadowIngredient_line WHERE recipe_id = ?";
            PreparedStatement preparedStatement4 = connection.prepareStatement(queryShadowIngredientLine);
            preparedStatement4.setInt(1, recipe_id);
            preparedStatement4.executeUpdate();

            String query = "DELETE FROM shadowRecipe WHERE recipe_id = ?";
            PreparedStatement preparedStatement5 = connection.prepareStatement(query);
            preparedStatement5.setInt(1, recipe_id);
            preparedStatement5.executeUpdate();

            connection.close();
            // connection.commit();
        } catch (SQLException e) {
            throw new DALException(e.getMessage());

        }
    }



}