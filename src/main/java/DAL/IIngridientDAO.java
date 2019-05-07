package DAL;

import IngridientDTO.IIngridientDTO;
import IngridientDTO.IngridientDTO;

import java.util.ArrayList;

public interface IIngridientDAO {

    void createIngridient(IIngridientDTO ingridient) throws DALException;

    void updateIngridient(IIngridientDTO ingridient) throws DALException;

    void deleteIngridient(int ingridient_id) throws DALException;

    //ArrayList<IIngridientDTO> getIngredientList();






}
