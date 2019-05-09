import DAL.*;
import DTO.*;


import DAL.IIngridientDAO;
import DAL.IngridientDAO;
import DAL.DALException;
import org.junit.jupiter.api.Assertions;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static org.junit.Assert.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;

import DAL.DALException;
import DAL.IRecipeDAO;
import DAL.RecipeDAO;
import DTO.IIngredient_lineDTO;
import DTO.IRecipeDTO;
import DTO.Ingredient_lineDTO;
import DTO.RecipeDTO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class AllTest {





        /**______________________________________________________________________________________________________
         *
         *                          TEST FOR USERDAO
         * ______________________________________________________________________________________________________
         */
        IUserDAO userDAO = new UserDAO();

    @org.junit.Test
        public void testUserDAO() {
            try {

                ArrayList<String> roles = new ArrayList();
                roles.add("admin"); roles.add("user"); roles.add("worker");
                IUserDTO testUser = new UserDTO(52,"swoldbye", "swl", roles, "300698-1234");

                //create the rolls
                userDAO.createRoll("admin", 95);
                userDAO.createRoll("user", 96);
                userDAO.createRoll("worker", 97);
                userDAO.createRoll("full-Time", 99);
                userDAO.createRoll("part-Time", 98);

                //createUser and getUser
                userDAO.createUser(testUser);
                IUserDTO returnedUser = userDAO.getUser(52);
                assertEquals(testUser.getUserName(),returnedUser.getUserName());
                assertEquals(testUser.getCpr(), returnedUser.getCpr());
                assertEquals(testUser.getIni(), returnedUser.getIni());
                assertEquals(testUser.getRoles().get(0), returnedUser.getRoles().get(0));
                assertEquals(testUser.getRoles().get(1), returnedUser.getRoles().get(1));
                assertEquals(testUser.getRoles().get(2), returnedUser.getRoles().get(2));
                assertEquals(testUser.getRoles().size(), returnedUser.getRoles().size());

                //UserList
                List<IUserDTO> allUsers = userDAO.getUserList();
                boolean found = false;
                for(IUserDTO user: allUsers){
                    if(user.getUserId() == 52) {
                        if (user.getUserId() == testUser.getUserId()) {
                            assertEquals(testUser.getUserName(), user.getUserName());
                            assertEquals(testUser.getCpr(), user.getCpr());
                            assertEquals(testUser.getIni(), user.getIni());
                            assertEquals(testUser.getRoles().get(0), user.getRoles().get(0));
                            assertEquals(testUser.getRoles().get(1), user.getRoles().get(1));
                            assertEquals(testUser.getRoles().get(2), user.getRoles().get(2));
                            assertEquals(testUser.getRoles().size(), user.getRoles().size());
                            found = true;
                        }
                        break;
                    }
                }
                if(!found){fail();}

                roles.clear();
                roles.add("Admin"); //'admin' changed to 'Admin' to test updateRole.
                roles.add("full-Time");
                roles.add("part-Time");
                userDAO.updateRoll("admin", "Admin");

                testUser.setUserName("abcd");
                testUser.setIni("qwer");
                testUser.setCpr("123456-7890");
                testUser.setRoles(roles);
                userDAO.updateUser(testUser);

                returnedUser = userDAO.getUser(52);
                assertEquals(testUser.getUserName(),returnedUser.getUserName());
                assertEquals(testUser.getCpr(), returnedUser.getCpr());
                assertEquals(testUser.getIni(), returnedUser.getIni());
                assertEquals(testUser.getRoles().get(0), returnedUser.getRoles().get(0));
                assertEquals(testUser.getRoles().get(1), returnedUser.getRoles().get(2));
                assertEquals(testUser.getRoles().get(2), returnedUser.getRoles().get(1));
                assertEquals(testUser.getRoles().size(), returnedUser.getRoles().size());



                userDAO.deleteUser(52);
                allUsers = userDAO.getUserList();

                for(IUserDTO user: allUsers){
                    if(user.getUserId() == 52) {fail(); }
                }

                int i = 0;
                List<String> allRoles = userDAO.getRollList();
                for(String roll: allRoles){
                    if(roll.equals("Admin") || roll.equals("user") || roll.equals("worker")
                            || roll.equals("part-Time") || roll.equals("full-Time")){i++;}
                }
                if(i != 5) {fail();}

                userDAO.deleteRoll("admin");
                userDAO.deleteRoll("user");
                userDAO.deleteRoll("worker");
                userDAO.deleteRoll("part-Time");
                userDAO.deleteRoll("full-Time");

                List<String> newRoles = userDAO.getRollList();
                for(String roll: newRoles){
                    if(roll.equals("Admin") || roll.equals("user") || roll.equals("worker")
                            || roll.equals("part-Time") || roll.equals("full-Time")){fail();}
                }
                userDAO.deleteUser(52);

            } catch (IUserDAO.DALException e) {
                e.printStackTrace();
                fail();
            }


        }


    /**______________________________________________________________________________________________________
     *
     *                          TEST FOR INGREDIENTDAO
     * ______________________________________________________________________________________________________
     */




        @org.junit.Test
        /**
         * Test if one can create an ingredient into the database.
         *
         */
        public void createIngridient() throws DALException {
            IIngridientDTO created = new IngridientDTO(99, "Bananer", false);
            IIngridientDAO dal = new IngridientDAO();

            dal.createIngridient(created);

            IIngridientDTO dataBaseIngredient = dal.getIngredient(99);

            assertEquals(created.toString(),dataBaseIngredient.toString());
            dal.deleteIngridient(99);
        }

        @org.junit.Test
        /**
         * Test if one can upgrade an ingredient.
         */
        public void updateIngridient() throws DALException {


            IIngridientDTO ingridien = new IngridientDTO(100, "dsadsadasdsfdsfsdf", true);
            IIngridientDTO ingridien2 = new IngridientDTO(100, "dsadsadasdsfdsfsdf", false);
            IIngridientDAO dal = new IngridientDAO();

            dal.createIngridient(ingridien);
            dal.updateIngridient(ingridien2);
            IIngridientDTO objectFromDatabase = dal.getIngredient(100);

            assertEquals(ingridien2.toString(),objectFromDatabase.toString());

            dal.deleteIngridient(100);


        }

        /**
         * Metoden opretter en ingrediens og sletter den efterfølgende.
         * Herefter hentes en liste for alle ingredienser og der søges efter den slettede ingrediens.
         * @throws DALException
         */

        @org.junit.Test
        public void deleteIngridient() throws DALException {
            IIngridientDTO ingridien = new IngridientDTO(100, "dsadsadasdsfdsfsdf", true);
            IIngridientDAO dal = new IngridientDAO();

            dal.createIngridient(ingridien);
            dal.deleteIngridient(100);
            ArrayList<IIngridientDTO> ingrediensList = dal.getIngredientList();


            for(int i = 0; i < ingrediensList.size(); i++ ){
                if(ingrediensList.get(i).getIngredient_id()==ingridien.getIngredient_id()){

                    fail();
                }

            }
        }

        /**
         * Metoden opretter en ingrediens. Herefter anvendes getIngredient metoden og det ses om to string metoden virser det samme på de to objekter.
         * @throws DALException
         */
        @org.junit.Test
        public void getIngredient() throws DALException {

            IIngridientDTO ingridien = new IngridientDTO(100, "dsadsadasdsfdsfsdf", true);
            IIngridientDAO dal = new IngridientDAO();

            dal.createIngridient(ingridien);
            IIngridientDTO ingridienFromDatabase = dal.getIngredient(100);

            assertEquals(ingridien.toString(),ingridienFromDatabase.toString());

            dal.deleteIngridient(100);

        }

        /**
         * Metoden henter alle ingridienser ud fra databasen som en liste. Det undersøger om antallet af objekter i listen
         * er det samme antal som tubler i databasen. Disse tupler er talt manualt. De kunne også tælles vha aggregering.
         * @throws DALException
         */

        @org.junit.Test
        public void getIngredientList() throws DALException {


            IIngridientDAO dal = new IngridientDAO();
            ArrayList<IIngridientDTO> ingrediensList = dal.getIngredientList();


            int expectedNumberOfRows = 16;
            int actualNumberOfRows = ingrediensList.size();

            assertEquals(expectedNumberOfRows,actualNumberOfRows);

            IIngridientDTO ingredient = new IngridientDTO(101, "dsadsadasdsfdsfsdf", true);
            dal.createIngridient(ingredient);

            ingrediensList = dal.getIngredientList();

            expectedNumberOfRows = 17;
            actualNumberOfRows = ingrediensList.size();



            assertEquals(expectedNumberOfRows,actualNumberOfRows);



            dal.deleteIngridient(100);
            dal.deleteIngridient(101);


        }


    /**______________________________________________________________________________________________________
     *
     *                          TEST FOR RECIPEDAO
     * ______________________________________________________________________________________________________
     */





        /**INDLEDENDE NOTE:
         *
         * IMELLEM HVER TEST KAN DER MED FORDEL AVENDES NEDERFORSTÅENDE QUERY DER NULSTILLER ALLE TABELLER.
         * DETTE ER NØDVENDIGT DA DER ANVENDES SHADOWTABLES, DER GEMMER SLETTET DATA.
         *
         * DELETE FROM shadowRecipe WHERE recipe_id =100;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =100;
         * DELETE FROM recipe WHERE recipe_id =100;
         * DELETE FROM ingredient_line WHERE recipe_id =100;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =100;
         * DELETE FROM shadowRecipe WHERE recipe_id =100;
         *
         * DELETE FROM shadowRecipe WHERE recipe_id =200;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =200;
         * DELETE FROM recipe WHERE recipe_id =200;
         * DELETE FROM ingredient_line WHERE recipe_id =200;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =200;
         * DELETE FROM shadowRecipe WHERE recipe_id =200;
         *
         * DELETE FROM shadowRecipe WHERE recipe_id =201;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =201;
         * DELETE FROM recipe WHERE recipe_id =201;
         * DELETE FROM ingredient_line WHERE recipe_id =201;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =201;
         * DELETE FROM shadowRecipe WHERE recipe_id =201;
         *
         * DELETE FROM shadowRecipe WHERE recipe_id =300;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =300;
         * DELETE FROM recipe WHERE recipe_id =300;
         * DELETE FROM ingredient_line WHERE recipe_id =300;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =300;
         * DELETE FROM shadowRecipe WHERE recipe_id =300;
         *
         * DELETE FROM shadowRecipe WHERE recipe_id =301;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =301;
         * DELETE FROM recipe WHERE recipe_id =301;
         * DELETE FROM ingredient_line WHERE recipe_id =301;
         * DELETE FROM shadowIngredient_line WHERE recipe_id =301;
         * DELETE FROM shadowRecipe WHERE recipe_id =301;
         *
         *
         * Dette skyldet at opskrifter bliver gemt i shadowTabeller, hvor de selvfølgelig ikke skal slettes fra.
         */


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
            Date date = new Date(22,5,2018);
            //iRecipeDAO.deleteRecipe(100);
            IRecipeDTO newRecipe = new RecipeDTO(100,"DenNyeOpskrift",date,80);
            IIngredient_lineDTO ingredient1 = new Ingredient_lineDTO(1,10,"Sildenafil");
            IIngredient_lineDTO ingredient2 = new Ingredient_lineDTO(4,10,"Magnesiumstearat");

            newRecipe.addIngredient_line(ingredient1);
            newRecipe.addIngredient_line(ingredient2);

            iRecipeDAO.createRecipe(newRecipe);
            IRecipeDTO recipeFromDatabase = iRecipeDAO.getRecipe(100);

            Assertions.assertEquals(newRecipe.toString(),recipeFromDatabase.toString());

            //Her testes det om ingredient_line tabellen bliver udfyldt. (False betyder at der er noget i tabellen, med det angivne id.
            boolean Expected_isIngredient_lineEmpty = false;
            boolean Actual__isIngredient_lineEmpty = iRecipeDAO.controleIngredientLine(100);
            Assertions.assertEquals(Expected_isIngredient_lineEmpty,Actual__isIngredient_lineEmpty);

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

            Assertions.assertEquals(Expected_isIngredient_lineEmpty,Actual__isIngredient_lineEmpty);
            Assertions.assertEquals(Expected_isProduct_recipeEmpty,Actual__isProduct_recipeEmpty);
            Assertions.assertEquals(Expected_isShadowRecipeeFull,Actual__isShadowRecipeFull);
            Assertions.assertEquals(Expected_isShadowIngredient_lineFull,Actual__isShadowIngredient_lineFull);


            List<IRecipeDTO> AllRecipes = iRecipeDAO.getAllRecipes();
            boolean found = false;
            for (IRecipeDTO recipe: AllRecipes){
                if (recipe.getRecipe_id() == newRecipe.getRecipe_id()){
                    found = true;
                }
            }
            if (found){
                Assertions.fail();
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

            List<IRecipeDTO> recipeList;

            recipeList = iRecipeDAO.getAllRecipes();


        /*Det faktiske antal af række i tabellen.
        Bemærk at der kan være indsat flere recipes recipes fra da denne tast blev lavet.
         Dermed kan expectedRecipes2 og expectedRecipes2 godt ændre sig.*/
            int expectedRecipes = 3 ;
            int actualNumberOfRecipes = recipeList.size();


            Assertions.assertEquals(expectedRecipes,actualNumberOfRecipes);

            // Lægger vi en opskrift ned i databasen så der findes en opskrift mere.
            //Vi henter en ny liste med getRecipe og ser antallet passer.
            Date date = new Date(22,5,2018);
            IRecipeDTO newRecipe = new RecipeDTO(300,"DenNyeOpskrift",date,80);
            IIngredient_lineDTO ingredient1 = new Ingredient_lineDTO(1,10,"Sildenafil");
            newRecipe.addIngredient_line(ingredient1);

            iRecipeDAO.createRecipe(newRecipe);
            recipeList = iRecipeDAO.getAllRecipes();

            int expectedRecipes2 = 4;
            actualNumberOfRecipes = recipeList.size();

            Assertions.assertEquals(expectedRecipes2,actualNumberOfRecipes);

        }

        /**
         * Metoden er en
         * @throws DALException
         */


        @org.junit.Test
        public void updateRecipe() throws DALException{
            Date date = new Date(22,5,2018);
            IRecipeDTO oldRecipe = new RecipeDTO(200,"Opskrift",date,80);
            IIngredient_lineDTO ingredient1 = new Ingredient_lineDTO(1,10,"Sildenafil");
            oldRecipe.addIngredient_line(ingredient1);

            IRecipeDTO newRecipe = new RecipeDTO(201,"EnEndnuNyererOpskrift",date,80);
            IIngredient_lineDTO ingredient = new Ingredient_lineDTO(1,10,"Sildenafil");
            newRecipe.addIngredient_line(ingredient);

            iRecipeDAO.updateRecipe(newRecipe.getRecipe_id(),newRecipe);

            boolean Expected_isIngredient_lineEmpty = true;
            boolean Actual__isIngredient_lineEmpty = iRecipeDAO.controleIngredientLine(200);

            boolean Expected_isProduct_recipeFull = false;
            boolean Actual__isProduct_recipeFull = iRecipeDAO.controleIngredientLine(201);

            Assertions.assertEquals(Expected_isIngredient_lineEmpty,Actual__isIngredient_lineEmpty);
            Assertions.assertEquals(Expected_isProduct_recipeFull,Actual__isProduct_recipeFull);

        }


    }

