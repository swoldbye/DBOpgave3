package DAL;

import DTO.IRecipeDTO;
import DTO.RecipeDTO;

import java.util.List;

public interface IRecipeDAO {

    void createRecipe(IRecipeDTO recipe) throws DALException;

    IRecipeDTO getRecipe(int id) throws DALException;

    List<IRecipeDTO> getAllRecipes() throws DALException;

    void updateRecipe(int recipe_id, IRecipeDTO recipe) throws DALException;

    void deleteRecipe(int recipe_id) throws DALException;
}
