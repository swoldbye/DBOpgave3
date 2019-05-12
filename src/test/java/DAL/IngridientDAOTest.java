package DAL;

import DAL.DALException;
import DAL.IIngridientDAO;
import DAL.IngridientDAO;
import DTO.IIngridientDTO;
import DTO.IngridientDTO;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class IngridientDAOTest {

    @org.junit.Test
    /**
     * Test if one can create an ingredient into the database.
     *
     */
    public void createIngridient() throws DALException {
        IIngridientDTO created = new IngridientDTO(99, "Bananer", false);
        IIngridientDAO dal = new IngridientDAO();

        try {
            dal.createIngridient(created);

            IIngridientDTO dataBaseIngredient = dal.getIngredient(99);

            assertEquals(created.toString(),dataBaseIngredient.toString());
        } finally {
            dal.deleteIngridient(99);

        }
    }

    @org.junit.Test
    /**
     * Test if one can upgrade an ingredient.
     */
    public void updateIngridient() throws DALException {


        IIngridientDAO dal;
        IIngridientDTO ingridien = new IngridientDTO(100, "dsadsadasdsfdsfsdf", true);
        IIngridientDTO ingridien2 = new IngridientDTO(100, "dsadsadasdsfdsfsdf", false);
        dal = new IngridientDAO();
        try {

            dal.createIngridient(ingridien);
            dal.updateIngridient(ingridien2);
            IIngridientDTO objectFromDatabase = dal.getIngredient(100);

            assertEquals(ingridien2.toString(),objectFromDatabase.toString());
        } finally {
            dal.deleteIngridient(100);

        }



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

        try {
            dal.createIngridient(ingridien);
            dal.deleteIngridient(100);
            ArrayList<IIngridientDTO> ingrediensList = dal.getIngredientList();


            for(int i = 0; i < ingrediensList.size(); i++ ){
                if(ingrediensList.get(i).getIngredient_id()==ingridien.getIngredient_id()){

                    fail();
                }

            }
        } finally {
            dal.deleteIngridient(100);
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

        try {
            dal.createIngridient(ingridien);
            IIngridientDTO ingridienFromDatabase = dal.getIngredient(100);

            assertEquals(ingridien.toString(),ingridienFromDatabase.toString());
        } finally {
            dal.deleteIngridient(100);

        }


    }

    /**
     * Metoden henter alle ingridienser ud fra databasen som en liste. Det undersøger om antallet af objekter i listen
     * er det samme antal som tubler i databasen. Disse tupler er talt manualt. De kunne også tælles vha aggregering.
     * @throws DALException
     */

    @org.junit.Test
    public void getIngredientList() throws DALException {


        IIngridientDAO dal = new IngridientDAO();
        try {
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
        } finally {
            dal.deleteIngridient(100);
            dal.deleteIngridient(101);

        }




    }
}