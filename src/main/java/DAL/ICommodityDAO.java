package DAL;

import DTO.ICommodityDTO;

import java.util.List;

public interface ICommodityDAO {

    void createCommodity() throws DALException;

    ICommodityDTO getCommmodity(int batch_id);

    List<ICommodityDTO> getCommodityList();

    void UpdateCommodity();

    void DeleteCommodity();


}
