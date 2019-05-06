import DAL.RecipeDAO;
import DTO.RecipeDTO;
import org.junit.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeDAOTest {

    @Test
    public void createRecipe(){

        RecipeDTO recipe = new RecipeDTO(1, "Panodil", new Date(2019,12,10), 10 );
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
