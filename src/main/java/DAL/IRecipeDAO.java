package DAL;

import DTO.RecipeDTO;

import java.util.List;

public interface IRecipeDAO {

    void createRecipe(RecipeDTO recipe) throws DALException;

    RecipeDTO getRecipe(int id) throws DALException;

    List<RecipeDTO> getAllRecipes() throws DALException;

    void deleteRecipe(int recipe_id) throws DALException;
}
