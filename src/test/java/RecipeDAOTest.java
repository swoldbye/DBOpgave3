
import static org.junit.Assert.*;
import DAL.DALException;
import DAL.IRecipeDAO;
import DAL.RecipeDAO;
import DTO.IRecipeDTO;
import DTO.RecipeDTO;
import java.sql.Date;
import java.util.List;

public class RecipeDAOTest{

    IRecipeDAO iRecipeDAO = new RecipeDAO();

    @org.junit.Test
    public void test()throws DALException{
        try {

            IRecipeDTO testRecipeDTO = new RecipeDTO(5,"Panodil",
                    new Date(2008,05,05),35,"Sidenafil",25.00);

            iRecipeDAO.createRecipe(testRecipeDTO);
            IRecipeDTO returnedRecipe = iRecipeDAO.getRecipe(5);
            assertEquals(testRecipeDTO.toString(),returnedRecipe.toString());

            List<IRecipeDTO> AllRecipes = iRecipeDAO.getAllRecipes();
            boolean found = false;
            for (IRecipeDTO recipe: AllRecipes){
                if (recipe.getRecipe_id() == testRecipeDTO.getRecipe_id()){
                    assertEquals(testRecipeDTO.toString(),recipe.toString());
                    found = true;
                }
            }
            if (!found){
                fail();
            }

            testRecipeDTO.setRecipe_name("PanodilPlus");
            testRecipeDTO.setRegistration_date(new Date(2007,06,06));
            testRecipeDTO.setStorage_time(54);
            iRecipeDAO.updateRecipe(5,testRecipeDTO);

            returnedRecipe = iRecipeDAO.getRecipe(5);
            assertEquals(testRecipeDTO.toString(),returnedRecipe.toString());

            iRecipeDAO.deleteRecipe(5);
            for (IRecipeDTO recipe: iRecipeDAO.getAllRecipes()){
                if (testRecipeDTO.getRecipe_id() == recipe.getRecipe_id()){
                    fail();
                }
            }
        }
        catch (DALException e){
            e.printStackTrace();
            fail();
        }




    }

}