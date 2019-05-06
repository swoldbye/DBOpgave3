package DAL;

import DTO.RecipeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class RecipeDAO implements IRecipeDAO {


    @Override
    public void createRecipe(RecipeDTO recipe) throws DALException {
        DBConnection connector = new DBConnection();

        try( Connection connection = connector.createConnection() ){

            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO recipe VALUES(recipe_id=?, recipe_name=?, quantity=?, reg_date=?, storage_time=?)");

            statement.setInt(1, recipe.getId());
            statement.setString(2, recipe.getName());
            statement.setInt(3, 1);
            statement.setDate(4, recipe.getRegistrationDate());
            statement.setInt(5,recipe.getStorageTime());

            statement.execute();

        }catch(Exception e){
            throw new DALException(e.getMessage());
        }
    }


    @Override
    public void deleteRecipe(RecipeDTO recipe) throws DALException {

    }

    @Override
    public RecipeDTO getRecipe(int id) throws DALException {
        return null;
    }

    @Override
    public List<RecipeDTO> getAllRecipes() throws DALException {
        return null;
    }
}
