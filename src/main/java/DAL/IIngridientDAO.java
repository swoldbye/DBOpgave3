package DAL;

import IngridientDTO.IIngridientDTO;

public interface IIngridientDAO {

    void createIngridient(IIngridientDTO ingridient);

    void updateIngridient(IIngridientDTO ingridient);

    void deleteIngridient(int ingridient_id);

    void updateRefill(int ingridient_id);




}
