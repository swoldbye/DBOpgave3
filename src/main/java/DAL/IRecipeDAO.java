package DAL;

import DTO.RecipeDTO;

import java.util.List;

public interface IRecipeDAO {

    void createRecipe(RecipeDTO recipe) throws DALException;

    void deleteRecipe(RecipeDTO recipe) throws DALException;

    RecipeDTO getRecipe( int id ) throws DALException;

    List<RecipeDTO> getAllRecipes() throws DALException;
}
