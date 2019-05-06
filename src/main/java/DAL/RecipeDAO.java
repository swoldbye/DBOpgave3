//package DAL;
//
//import DTO.IRecipeDTO;
//import DTO.RecipeDTO;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//
//public class RecipeDAO implements IRecipeDAO {
//
//    public void createRecipe(RecipeDTO recipe) throws DALException {
//        DBConnection dbConnection = new DBConnection();
//
//        try( Connection connection = dbConnection.createConnection()){
//
//            PreparedStatement statement = connection.prepareStatement(
//                    "INSERT INTO recipe VALUES(recipe_id=?, recipe_name=?, quantity=?, reg_date=?, storage_time=?)");
//
//            statement.setInt(1, recipe.getRecipe_id());
//            statement.setString(2, recipe.getRecipe_name());
//            statement.setInt(3, recipe.getQuantity());
//            statement.setDate(4, recipe.getRegistration_date());
//            statement.setInt(5,recipe.getStorage_time());
//
//            statement.execute();
//
//        }catch(SQLException e){
//            throw new DALException(e.getMessage());
//        }
//    }
//
//    public void deleteRecipe(RecipeDTO recipe) throws DALException {
//
//    }
//
//    public RecipeDTO getRecipe(int id) throws DALException {
//        return null;
//    }
//
//    public List<RecipeDTO> getAllRecipes() throws DALException {
//        return null;
//    }
//
//}
//class test {
//    public static void main(String[] args) throws DALException {
//
//        IRecipeDAO HSL = new RecipeDAO();
//
//        RecipeDTO s = new RecipeDTO();
//        HSL.createRecipe(s);
//
//    }
//}
