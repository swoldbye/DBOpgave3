package DAL;

import DTO.IRecipeDTO;
import DTO.RecipeDTO;

import java.util.List;

public interface IRecipeDAO {

    void createRecipe(IRecipeDTO recipe) throws DALException;

    IRecipeDTO getRecipe(int recipe_id) throws DALException;

    List<IRecipeDTO> getAllRecipes() throws DALException;

    void updateRecipe(int recipe_id, IRecipeDTO recipe) throws DALException;

    void deleteRecipe(int recipe_id) throws DALException;

    //Nedenforstående metoder kan flyttes, men de giver værdiful information.

    boolean controleIngredientLine(int recipe_id) throws DALException;

    boolean controleProduct_recipe(int recipe_id) throws DALException;

    boolean controleshadowIngredient_line(int recipe_id) throws DALException;

    boolean controleshadowRecipe(int recipe_id) throws DALException;
}
