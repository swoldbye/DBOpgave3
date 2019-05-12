package DAL;

import DTO.IIngridientDTO;

import java.util.ArrayList;

public interface IIngridientDAO {

    IIngridientDTO getIngredient(int ingredient_id) throws DALException;

    void createIngridient(IIngridientDTO ingridient) throws DALException;

    IIngridientDTO updateIngridient(IIngridientDTO ingridient) throws DALException;

    void deleteIngridient(int ingridient_id) throws DALException;

    ArrayList<IIngridientDTO> getIngredientList() throws DALException;





    //ArrayList<IIngridientDTO> getIngredientList();






}
