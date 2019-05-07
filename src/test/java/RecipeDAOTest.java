import DAL.RecipeDAO;
import DTO.IRecipeDTO;
import DTO.RecipeDTO;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeDAOTest {

    @Test
    public void createRecipe(){

        IRecipeDTO recipe = new RecipeDTO();
        RecipeDAO dao = new RecipeDAO();


        try{

            dao.createRecipe(recipe);
            dao.createRecipe(recipe);

            fail();

        }catch(Exception e){
            System.out.println(e.getMessage());

        }

    }

}
