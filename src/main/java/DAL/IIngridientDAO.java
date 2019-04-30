package DAO;

import IngridientDTO.IIngridientDTO;

public interface IIngridientDAO {

    void createIngridient(IIngridientDTO ingridient);

    void updateIngridient(int ingridient_id);

    void deleteIngridient(int ingridient_id);

    void updateRefill(int ingridient_id);




}
